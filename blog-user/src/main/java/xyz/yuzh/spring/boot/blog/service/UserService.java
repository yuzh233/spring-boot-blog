package xyz.yuzh.spring.boot.blog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.yuzh.spring.boot.blog.entity.User;

import java.util.List;

/**
 * @author yu.zh
 */
public interface UserService {

    User saveOrUpdateUser(User user);

    User registerUser(User user);

    void removeUser(Long id);

    User getUserById(Long id);

    List<User> listUser();

    /**
     * 根据用户名模糊查询
     *
     * @param name
     * @param pageable
     * @return
     */
    Page<User> listUsersByNameLike(String name, Pageable pageable);

    User findByUsername(String username);

}
