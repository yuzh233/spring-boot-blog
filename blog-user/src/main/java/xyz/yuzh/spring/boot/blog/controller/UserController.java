package xyz.yuzh.spring.boot.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import xyz.yuzh.spring.boot.blog.entity.User;
import xyz.yuzh.spring.boot.blog.service.UserService;
import xyz.yuzh.spring.boot.blog.util.UserOperationException;
import xyz.yuzh.spring.boot.blog.vo.Response;

import java.util.List;

/**
 * @author yu.zh [yuzh233@gmail.com]
 */
@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService userService;

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
     * @return
     */
    @PostMapping(value = "/add")
    public ResponseEntity<Response> createAndUpdateUser(User user) {
        userService.saveOrUpdateUser(user);
        return ResponseEntity.ok().body(new Response(true, "注册成功", user));
    }

    /**
     * /login: [get] 获取登陆页面
     *
     * @return
     */
    @GetMapping(value = "/login")
    public ModelAndView loginPage() {
        return new ModelAndView("login");
    }

    /**
     * /login: [post] 登陆
     *
     * @return
     */
    @PostMapping(value = "/login")
    public ResponseEntity<Response> login(@RequestParam(name = "username") String username,
                                          @RequestParam(name = "password") String password,
                                          @RequestParam(name = "rememberMe", required = false, defaultValue = "false")
                                                  boolean rememberMe) {

        User user = userService.findByUsername(username);

        if (user == null) {
            throw new UserOperationException.VerificationFailedException();
        }
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
