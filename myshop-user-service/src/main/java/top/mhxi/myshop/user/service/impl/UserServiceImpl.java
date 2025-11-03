package top.mhxi.myshop.user.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import top.mhxi.myshop.common.handler.MyShopException;
import top.mhxi.myshop.common.to.UserTO;
import top.mhxi.myshop.common.utils.ResultCode;
import top.mhxi.myshop.common.utils.SnowflakeIdGenerator;
import top.mhxi.myshop.user.mapper.UserMapper;
import top.mhxi.myshop.user.entity.User;
import top.mhxi.myshop.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    RedisTemplate redisTemplate;



    public int insert(User user) {
        // 新增一个记录时，用雪花算法生成主键
        SnowflakeIdGenerator idWorker = new SnowflakeIdGenerator(1, 1);
        user.setId(idWorker.nextId());
        return userMapper.insert(user);
    }


    public int deleteById(Long id) {
        return userMapper.deleteByPrimaryKey(id);
    }


    public int update(User user) {
        return userMapper.updateByPrimaryKey(user);
    }


    public User selectById(Long id) {
        return userMapper.selectByPrimaryKey(id);
    }


    public List<User> selectAll() {
        return userMapper.selectAll();
    }

    public int register(String name, String password) {
        if(userMapper.existsByUsername(name)) {
            throw new MyShopException(ResultCode.ERROR, "用户名已经存在");
        }

        // 加密器
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // 新增一个记录时，用雪花算法生成主键
        SnowflakeIdGenerator idWorker = new SnowflakeIdGenerator(1, 1);

        User user = new User();
        user.setId(idWorker.nextId());
        user.setName(name);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("USER");  //默认角色
        return userMapper.insert(user);
    }



    public String login(String name, String password) {

        // 1. 查询用户
        User user = userMapper.findByUsername(name);
        if (user == null) {
            throw new MyShopException(ResultCode.ERROR, "用户不存在");
        }

        // 2. 校验密码
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new MyShopException(ResultCode.ERROR, "密码错误");
        }

        // 3. 生成 sessionId
        String sessionId = UUID.randomUUID().toString();


        // 4. 保存用户信息到 Redis，其他模块要从reids中查询，所以要用UserTO
        user.setPassword("");
        UserTO userTO = new UserTO();
        BeanUtils.copyProperties(user,userTO);
        redisTemplate.opsForValue().set("session:" + sessionId, userTO, 24 * 60 * 60, TimeUnit.SECONDS);

        // 5. 返回 sessionId 给前端（Cookie）
        return sessionId;
    }

    /**
     * 登出方法
     */
    public void logout(String sessionId) {
        redisTemplate.delete("session:" + sessionId);
    }

    /**
     * 根据 sessionId 获取用户信息
     */
    public UserTO getUserBySession(String sessionId) {
        return (UserTO) redisTemplate.opsForValue().get("session:" + sessionId);
    }

}