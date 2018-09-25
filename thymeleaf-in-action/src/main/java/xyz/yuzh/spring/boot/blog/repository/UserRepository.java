package xyz.yuzh.spring.boot.blog.repository;

import xyz.yuzh.spring.boot.blog.entity.User;

import java.util.List;

/**
 * @author yu.zh [yuzh233@gmail.com] 2018/09/24 16:43
 */
public interface UserRepository {

    /**
     * 新增或者修改用户
     * @param user
     * @return
     */
    User saveOrUpdateUser(User user);

    /**
     * 删除用户
     * @param id
     */
    void deleteUser(Long id);

    /**
     * 根据用户id获取用户
     * @param id
     * @return
     */
    User getUserById(Long id);

    /**
     * 获取所有用户的列表
     * @return
     */
    List<User> listUser();
}
