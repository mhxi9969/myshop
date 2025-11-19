package top.mhxi.myshop.user.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import top.mhxi.myshop.common.handler.MyShopException;
import top.mhxi.myshop.common.to.UserTO;
import top.mhxi.myshop.common.utils.ResultCode;
import top.mhxi.myshop.common.utils.SnowflakeIdGenerator;
import top.mhxi.myshop.user.entity.vo.RegisterVO;
import top.mhxi.myshop.user.mapper.UserMapper;
import top.mhxi.myshop.user.entity.User;
import top.mhxi.myshop.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    private JavaMailSenderImpl javaMailSender;


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


    // 用户注册
    public int register(RegisterVO vo) {
        String name = vo.getName();
        String password = vo.getPassword();
        String email = vo.getEmail();
        String inputCode = vo.getCode();

        if (userMapper.existsByName(name)) {
            throw new MyShopException(ResultCode.ERROR, "用户名已经存在");       }

        if (userMapper.existsByEmail(email)) {
            throw new MyShopException(ResultCode.ERROR, "邮箱已经存在");
        }

        // 去reis拿验证码，验证和前端传来的是否一样
        String key = "email:code:" + email;
        String code = (String) redisTemplate.opsForValue().get(key);
        if (inputCode == null || !inputCode.equals(code)) {
            throw new MyShopException(ResultCode.ERROR, "验证码错误");
        }

        // 加密器
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // 新增一个记录时，用雪花算法生成主键
        SnowflakeIdGenerator idWorker = new SnowflakeIdGenerator(1, 1);

        User user = new User();
        user.setId(idWorker.nextId());
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("USER");  //默认角色
        return userMapper.insert(user);
    }


    public String login(String name, String password) {

        // 1. 查询用户
        User user = userMapper.findByName(name);
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
        BeanUtils.copyProperties(user, userTO);
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

    // 发送验证码
    @Override
    public void sendCode(String email) {
        // 生成 4 位随机数字
        String code = String.format("%04d", new Random().nextInt(10000));

        // 存入 Redis，有效期 5 分钟
        String key = "email:code:" + email;
        redisTemplate.opsForValue().set(key, code, 5, TimeUnit.MINUTES);

        // 构建邮件
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("MyShop 注册验证码");
        mailMessage.setText("您的验证码是：" + code + "，有效期 5 分钟。");

        try {
            javaMailSender.send(mailMessage);
            System.out.println("验证码邮件发送成功: " + code);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("邮件发送失败：" + e.getMessage());
        }
    }

}