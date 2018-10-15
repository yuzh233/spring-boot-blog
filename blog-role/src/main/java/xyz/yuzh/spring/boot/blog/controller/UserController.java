package xyz.yuzh.spring.boot.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import xyz.yuzh.spring.boot.blog.domain.Authority;
import xyz.yuzh.spring.boot.blog.domain.User;
import xyz.yuzh.spring.boot.blog.service.AuthorityService;
import xyz.yuzh.spring.boot.blog.service.UserService;
import xyz.yuzh.spring.boot.blog.util.UserOperationException;
import xyz.yuzh.spring.boot.blog.vo.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yu.zh [yuzh233@gmail.com]
 */
@RestController
@RequestMapping(value = "/users")
//@PreAuthorize("hasAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
public class UserController {

    public final static Long ROLE_ADMIN_AUTHORITY_ID = 1L;

    private static final Long ROLE_USER_AUTHORITY_ID = 2L;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;

    /**
     * /users: [get] 用户列表（有异步和同步两种方式请求）
     *
     * @param async
     * @param pageIndex
     * @param pageSize
     * @param map
     * @return
     */
    @GetMapping
    public ModelAndView list(@RequestParam(name = "async", required = false, defaultValue = "false") boolean async,
                             @RequestParam(name = "pageIndex", required = false, defaultValue = "0") int pageIndex,
                             @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
                             @RequestParam(name = "name", required = false, defaultValue = "") String name,
                             ModelMap map) {
        // 分页对象
        Pageable pageable = new PageRequest(pageIndex, pageSize);
        Page<User> userPage = userService.listUsersByNameLike(name, pageable);
        List<User> userList = userPage.getContent();
        map.addAttribute("users", userList);
        map.addAttribute("page", userPage);

        if (async) {
            /**
             * users/list :: #mainContainerRepleace
             * ModelAndView 实质上是将页面的内容输出至页面，通过 [:: #id] 可以指定只输出页面的某个部分。
             */
            return new ModelAndView("users/list :: #mainContainerRepleace", map);
        }
        return new ModelAndView("users/list", map);
    }


    /**
     * /register：[POST] 注册 or 更新
     *
     * @param user
     * @param authorityId 角色ID
     * @return
     */
    @PostMapping(value = "/register")
    public ResponseEntity<Response> createAndUpdateUser(User user, Long authorityId) {
        // 默认创建用户角色是管理员
        List<Authority> authorities = new ArrayList<>();
        authorities.add(authorityService.findAuthorityById(ROLE_USER_AUTHORITY_ID));
        user.setAuthorities(authorities);

        /*if (user.getId() == null) {
            // 创建操作
            user.setEncodePassword(user.getPassword());
        } else {
            // 更新操作

            // 判断密码是否做了变更，是否需要重新加密密码
            User originalUser = userService.getUserById(user.getId());
            String rawPassword = originalUser.getPassword();
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            String encodePasswd = encoder.encode(user.getPassword());
            boolean isMatch = encoder.matches(rawPassword, encodePasswd);
            if (!isMatch) {
                // 重新加密
                user.setEncodePassword(user.getPassword());
            } else {
                user.setPassword(user.getPassword());
            }
        }*/

        userService.saveOrUpdateUser(user);

        return ResponseEntity.ok().body(new Response(true, "注册成功"));
    }

    /**
     * /login: [post] 登陆
     *
     * @return
     */
    @PostMapping(value = "/login")
    public ResponseEntity<Response> login(@RequestParam(name = "username") String username,
                                          @RequestParam(name = "password") String password,
                                          @RequestParam(name = "rememberMe", required = false) boolean rememberMe) {
        User user = userService.findByUsername(username);

        if (user == null) {
            throw new UserOperationException.VerificationFailedException();
        }

        /*String rawPassword = user.getPassword();
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodePasswd = encoder.encode(password);
        boolean isMatch = encoder.matches(rawPassword, encodePasswd);

        System.out.println(isMatch);*/

        if (!(user.getUsername().equals(username) && user.getPassword().equals(password))) {
            throw new UserOperationException.VerificationFailedException();
        }

        return ResponseEntity.ok().body(new Response(true, "success"));
    }

    /**
     * /users/{id}: [delete] 删除用户
     *
     * @param id
     * @param map
     * @return
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Response> delete(@PathVariable("id") Long id, ModelMap map) {
        userService.removeUser(id);
        return ResponseEntity.ok().body(new Response(true, "success"));
    }


    /**
     * /users/edit/{id}: [get] 获取某个具体用户编辑页面
     */
    @GetMapping(value = "/edit/{id}")
    public ModelAndView editPage(@PathVariable(name = "id") long id, ModelMap map) {
        User user = userService.getUserById(id);
        map.addAttribute("user", user);
        return new ModelAndView("users/list :: #userFormContainer", map);
    }

    /**
     * /users/add [get] 获取添加用户页面
     *
     * @param map
     * @return
     */
    @GetMapping(value = "/add")
    public ModelAndView addPage(ModelMap map) {
        map.addAttribute("user", null);
        return new ModelAndView("users/list :: #userFormContainer");
    }
}
