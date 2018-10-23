package xyz.yuzh.spring.boot.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import xyz.yuzh.spring.boot.blog.domain.Authority;
import xyz.yuzh.spring.boot.blog.domain.User;
import xyz.yuzh.spring.boot.blog.service.AuthorityService;
import xyz.yuzh.spring.boot.blog.service.UserService;
import xyz.yuzh.spring.boot.blog.vo.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yu.zh [yuzh233@gmail.com]
 */
@RestController
@RequestMapping(value = "/users")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
public class UserController {

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
     * 后台管理：新建用户
     *
     * @param user
     * @return
     */
    @PostMapping(value = "/save")
    public ResponseEntity<Response> createAndUpdateUser(User user) {
        List<Authority> authorities = new ArrayList<>();
        authorities.add(authorityService.getAuthorityById(MainController.ROLE_USER_AUTHORITY_ID));
        user.setAuthorities(authorities);

        userService.saveUser(user);
        return ResponseEntity.ok().body(new Response(true, "新建用户成功"));
    }

    /**
     * 后台管理：修改用户
     *
     * @param user
     * @param authorityId
     * @return
     */
    @PostMapping(value = "/modify")
    public ResponseEntity<Response> modifyUser(User user, Long authorityId) {
        List<Authority> authorities = new ArrayList<>();
        authorities.add(authorityService.getAuthorityById(authorityId));
        user.setAuthorities(authorities);

        System.out.println("[修改用户：]"+user);
        userService.updateUser(user);
        return ResponseEntity.ok().body(new Response(true, "用户修改成功"));
    }

    /**
     * /login: [post] 登陆
     *
     * @return
     */
    /*@PostMapping(value = "/login")
    public ResponseEntity<Response> login(@RequestParam(name = "username") String username,
                                          @RequestParam(name = "password") String password,
                                          @RequestParam(name = "rememberMe", required = false) boolean rememberMe) {
        User user = userService.findByUsername(username);

        if (user == null) {
            throw new UserOperationException.VerificationFailedException();
        }

        *//*String rawPassword = user.getPassword();
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodePasswd = encoder.encode(password);
        boolean isMatch = encoder.matches(rawPassword, encodePasswd);

        System.out.println(isMatch);*//*

        if (!(user.getUsername().equals(username) && user.getPassword().equals(password))) {
            throw new UserOperationException.VerificationFailedException();
        }

        return ResponseEntity.ok().body(new Response(true, "success"));
    }*/

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
