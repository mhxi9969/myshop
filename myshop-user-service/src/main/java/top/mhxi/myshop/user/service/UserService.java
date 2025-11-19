package top.mhxi.myshop.user.service;


import top.mhxi.myshop.common.to.UserTO;
import top.mhxi.myshop.user.entity.User;
import top.mhxi.myshop.user.entity.vo.RegisterVO;

import java.util.List;

public interface UserService {
    int insert(User user);
    int deleteById(Long id);
    int update(User user);
    User selectById(Long id);
    List<User> selectAll();

    int register(RegisterVO vo);
    String login(String email, String password);
    void logout(String sessionId);
    UserTO getUserBySession(String sessionId);

    void sendCode(String email);
}