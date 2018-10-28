package xyz.yuzh.spring.boot.blog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.yuzh.spring.boot.blog.domain.User;

import java.util.List;

/**
 * 用户服务接口 实现 UserDetailsService
 *
 * @author yu.zh
 */
public interface UserService{

    User saveUser(User user);

    User updateUser(User user);

    void removeUser(Long id);

    User getUserById(Long id);

    List<User> listUser();

    Page<User> listUsersByNameLike(String name, Pageable pageable);

    User findByUsername(String username);

    boolean existsByUsername(String primaryKey);

    boolean existsByEmail(String primaryKey);
}
