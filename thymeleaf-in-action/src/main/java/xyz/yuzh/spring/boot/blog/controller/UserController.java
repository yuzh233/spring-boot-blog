package xyz.yuzh.spring.boot.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import xyz.yuzh.spring.boot.blog.entity.User;
import xyz.yuzh.spring.boot.blog.repository.UserRepository;

import java.util.List;

/**
 * @author yu.zh [yuzh233@gmail.com] 2018/09/24 16:44
 */
@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    public List<User> getUserList() {
        return userRepository.listUser();
    }

    /**
     * 查询所有用户 [ GET /users ]
     *
     */
    @GetMapping
    public ModelAndView list(ModelMap map) {
        map.addAttribute("users", getUserList());
        map.addAttribute("title", "用户管理");
        return new ModelAndView("users/list", "userModel", map);
    }

    /**
     * 根据 id 查询用户 [ GET /users/{id} ]
     */
    @GetMapping(value = "/{id}")
    public ModelAndView getUserById(@PathVariable("id") Long id, ModelMap map) {
        User user = userRepository.getUserById(id);
        System.out.println(user);
        map.addAttribute("user", user);
        map.addAttribute("title", "查看用户");
        return new ModelAndView("users/view", "userModel", map);
    }

    /**
     * 跳转到新建用户 [ GET /users/form ]
     */
    @GetMapping(value = "/form")
    public ModelAndView createForm(ModelMap map) {
        map.addAttribute("user", new User());
        map.addAttribute("title", "创建用户");
        return new ModelAndView("users/form", "userModel", map);
    }

    /**
     * 新建及修改 [ POST /users ]
     */
    @PostMapping
    public ModelAndView create(User user) {
        userRepository.saveOrUpdateUser(user);
        return new ModelAndView("redirect:/users");
    }

    /**
     * 删除用户 [ GET /users/delete/{id} ]
     */
    @GetMapping(value = "delete/{id}")
    public ModelAndView delete(@PathVariable("id") Long id, ModelMap map) {
        userRepository.deleteUser(id);
        map.addAttribute("users", getUserList());
        return new ModelAndView("redirect:/users");
    }

    /**
     * 跳转修改页面 [ GET /users/modify/{id} ]
     */
    @GetMapping(value = "modify/{id}")
    public ModelAndView update(@PathVariable("id") Long id, ModelMap map) {
        User user = userRepository.getUserById(id);
        map.addAttribute("user", user);
        map.addAttribute("title", "修改用户");
        return new ModelAndView("users/form", "userModel", map);
    }

}
