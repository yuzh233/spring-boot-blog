package xyz.yuzh.spring.boot.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
 * 首页控制器
 *
 * @author yu.zh
 */
@Controller
public class MainController {

    /**
     * 管理员
     */
    public final static Long ROLE_ADMIN_AUTHORITY_ID = 1L;


    /**
     * 普通用户
     */
    public static final Long ROLE_USER_AUTHORITY_ID = 2L;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;

    @GetMapping(value = {"/", "/index"})
    public String root() {
        return "index";
    }


    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        model.addAttribute("errorMsg", "登陆失败，用户名或密码错误！");
        return "login";
    }


    /**
     * 注册用户
     */
    @PostMapping("/register")
    public ResponseEntity<Response> createAndUpdateUser(User user) {
        List<Authority> authorities = new ArrayList<>();
        authorities.add(authorityService.getAuthorityById(ROLE_USER_AUTHORITY_ID));
        user.setAuthorities(authorities);

        userService.saveUser(user);
        return ResponseEntity.ok().body(new Response(true, "用户注册成功"));
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/search")
    public String search() {
        return "search";
    }
}
