package xyz.yuzh.spring.boot.blog.service;

import xyz.yuzh.spring.boot.blog.domain.Authority;

/**
 * 角色实体服务
 *
 * @author yu.zh [yuzh233@gmail.com]
 * @date 2018/10/14
 */
public interface AuthorityService {

    Authority findAuthorityById(Long id);
}
