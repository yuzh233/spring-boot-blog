package xyz.yuzh.spring.boot.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import xyz.yuzh.spring.boot.blog.entity.User;
import xyz.yuzh.spring.boot.blog.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author yu.zh
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User saveOrUpdateUser(User user) {
        return userRepository.save(user);
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public User registerUser(User user) {
        return userRepository.save(user);
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void removeUser(Long id) {
        userRepository.delete(id);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findOne(id);
    }

    @Override
    public List<User> listUser() {
        return userRepository.findAll();
    }

    @Override
    public Page<User> listUsersByNameLike(String name, Pageable pageable) {
        name = "%" + name + "%";
        return userRepository.findByNameLike(name, pageable);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
