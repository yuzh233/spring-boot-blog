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

    Page<User> listUsersByNameLike(String name, Pageable pageable);

    User findByUsername(String username);

    User queryUserByUsername(String username);

    User queryUserByEmail(String email);

}
