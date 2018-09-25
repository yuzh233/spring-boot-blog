package xyz.yuzh.spring.boot.blog.repository;

import org.springframework.stereotype.Repository;
import xyz.yuzh.spring.boot.blog.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author yu.zh [yuzh233@gmail.com] 2018/09/24 16:47
 */
@Repository
public class UserRepositoryImpl implements UserRepository {
    /**
     * 累加器
     */
    private static AtomicLong counter = new AtomicLong();

    /**
     * 暂存数据
     */
    private static final ConcurrentMap<Long, User> userMap = new ConcurrentHashMap<>();

    @Override
    public User saveOrUpdateUser(User user) {
        Long id = user.getId();
        if (id <= 0) {
            id = counter.incrementAndGet();
            user.setId(id);
        }
        userMap.put(id, user);
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        userMap.remove(id);
    }

    @Override
    public User getUserById(Long id) {
        return userMap.get(id);
    }

    @Override
    public List<User> listUser() {
        return new ArrayList<>(userMap.values());
    }
}
