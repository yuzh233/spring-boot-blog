package xyz.yuzh.spring.boot.blog.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import xyz.yuzh.spring.boot.blog.entity.User;


/**
 * @author yu.zh
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据用户名分页查询列表
     *
     * @param name
     * @param pageable
     * @return
     */
    Page<User> findByNameLike(String name, Pageable pageable);

    /**
     * 根据用户账号查询用户
     *
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * 查找用户名是否被占用
     *
     * @return
     */
    User queryUserByUsername(String username);

    /**
     * 查找邮箱是否被占用
     *
     * @param email
     * @return
     */
    User queryUserByEmail(String email);

}
