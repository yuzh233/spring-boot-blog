package xyz.yuzh.spring.boot.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.yuzh.spring.boot.blog.domain.Authority;
import xyz.yuzh.spring.boot.blog.repository.AuthorityRepository;

/**
 * @author yu.zh [yuzh233@gmail.com]
 * @date 2018/10/14
 */
@Service
public class AuthorityServiceImpl implements AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public Authority findAuthorityById(Long id) {
        return authorityRepository.findOne(id);
    }

}
