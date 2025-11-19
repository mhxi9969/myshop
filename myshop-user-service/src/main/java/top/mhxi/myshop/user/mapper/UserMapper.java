package top.mhxi.myshop.user.mapper;

import java.util.List;
import top.mhxi.myshop.user.entity.User;

public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    User selectByPrimaryKey(Long id);

    List<User> selectAll();

    int updateByPrimaryKey(User record);

    boolean existsByEmail(String email);

    boolean existsByName(String name);

    User findByName(String name);
}