package xyz.yuzh.spring.boot.blog.domain;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

/**
 * 角色实体
 *
 * @author yu.zh [yuzh233@gmail.com]
 * @date 2018/10/14
 */
@Entity
public class Authority implements GrantedAuthority { // 需要实现 spring security 内置对象

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 表的映射字段值不能为空
     */
    @Column(nullable = false)
    private String name;

    private Authority() {

    }

    public Authority(Long roleUserAuthorityId) {
        this.id = roleUserAuthorityId;
    }

    /**
     * 返回认证信息
     */
    @Override
    public String getAuthority() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
