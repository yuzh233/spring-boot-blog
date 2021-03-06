package xyz.yuzh.spring.boot.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Spring Security 配置类.
 */
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 自定义配置
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/css/**", "/js/**", "/fonts/**", "/index").permitAll() // 都可以访问
                    .antMatchers("/users/**").hasRole("ADMIN") // 需要相应的角色才能访问
                    .and()
                .formLogin()   // 基于 Form 表单登录验证
                    .loginPage("/login") // 跳转到登陆地址
                    .failureUrl("/login-error"); // 登陆失败跳转地址
    }

    /**
     * 认证信息管理
     *
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication() // 认证信息存储内存中
                .withUser("yuzh").password("admin").roles("ADMIN"); // 硬编码测试
    }
}
