package xyz.yuzh.spring.boot.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import xyz.yuzh.spring.boot.blog.domain.Authority;
import xyz.yuzh.spring.boot.blog.domain.User;
import xyz.yuzh.spring.boot.blog.service.AuthorityService;
import xyz.yuzh.spring.boot.blog.service.UserService;
import xyz.yuzh.spring.boot.blog.util.UserOperationException;
import xyz.yuzh.spring.boot.blog.vo.Response;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    public void root(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/blogs").forward(request,response);
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

        // 账号和邮箱校验
        String username = user.getUsername();
        String email = user.getEmail();
        if (userService.existsByUsername(username)) {
            throw new UserOperationException.UsernameExistException();
        }
        if (userService.existsByEmail(email)) {
            throw new UserOperationException.EmailExistException();
        }

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
