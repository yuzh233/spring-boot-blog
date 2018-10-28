package xyz.yuzh.spring.boot.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import xyz.yuzh.spring.boot.blog.domain.Blog;
import xyz.yuzh.spring.boot.blog.domain.User;
import xyz.yuzh.spring.boot.blog.domain.Vote;
import xyz.yuzh.spring.boot.blog.service.BlogService;
import xyz.yuzh.spring.boot.blog.service.UserService;
import xyz.yuzh.spring.boot.blog.vo.Response;

import java.util.List;

/**
 * 用户主页空间控制器.
 *
 * @author yu.zh
 */
@Controller
@RequestMapping("/u")
public class UserspaceController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private BlogService blogService;

    @Value("${file.server.url}")
    private String fileServerUrl;

    /**
     * `/u/{username}`: [get] 具体某个用户的主页
     */
    @GetMapping("/{username}")
    public String userSpace(@PathVariable("username") String username, Model model) {
        User user = (User) userDetailsService.loadUserByUsername(username);
        model.addAttribute("user", user);
        return "redirect:/u/" + username + "/blogs";
    }

    /**
     * `/u/{username}/blogs`: [get] 查询用户博客
     */
    @GetMapping("/{username}/blogs")
    public String listBlogsByOrder(@PathVariable("username") String username,
                                   @RequestParam(value = "order", required = false, defaultValue = "new") String order,
                                   @RequestParam(value = "category", required = false) Long category,
                                   @RequestParam(value = "async", required = false) boolean async,
                                   @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                   @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
                                   @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                   Model model) {

        User user = (User) userDetailsService.loadUserByUsername(username);
        model.addAttribute("user", user);

        if (category != null) {
            System.out.print("category:" + category);
            System.out.print("selflink:" + "redirect:/u/" + username + "/blogs?category=" + category);
            return "/u";
        }

        Page<Blog> page = null;
        if (order.equals("hot")) {
            // 最热查询
            Sort sort = new Sort(Sort.Direction.DESC, "reading", "comments", "likes");
            Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
            page = blogService.listBlogsByTitleLikeAndSort(user, keyword, pageable);
        }
        if (order.equals("new")) {
            // 最新查询
            Pageable pageable = new PageRequest(pageIndex, pageSize);
            page = blogService.listBlogsByTitleLike(user, keyword, pageable);
        }

        // 当前所在页面数据列表
        List<Blog> list = page.getContent();

        model.addAttribute("order", order);
        model.addAttribute("page", page);
        model.addAttribute("blogList", list);
        return (async == true ? "/userspace/u :: #mainContainerRepleace" : "/userspace/u");
    }

    /**
     * `/u/{username}/blogs/{id}`: [get] 获取博客展示页面
     */
    @GetMapping("/{username}/blogs/{id}")
    public String getBlogById(@PathVariable("username") String username, @PathVariable("id") Long id, Model model) {

        User principal = null;
        Blog blog = blogService.getBlogById(id);

        // 每次读取，简单的可以认为阅读量增加1次
        blogService.readingIncrease(id);

        boolean isBlogOwner = false;

        // 判断操作用户是否是博客的所有者
        if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
            principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal != null && username.equals(principal.getUsername())) {
                isBlogOwner = true;
            }
        }

        // 判断操作用户的点赞情况
        List<Vote> votes = blog.getVotes();
        // 当前用户的点赞情况
        Vote currentVote = null;

        if (principal != null) {
            for (Vote vote : votes) {
                if (vote.getUser().getUsername().equals(principal.getUsername())){
                    currentVote = vote;
                    break;
                }
            }
        }

        model.addAttribute("isBlogOwner", isBlogOwner);
        model.addAttribute("blogModel", blog);
        model.addAttribute("currentVote",currentVote);

        return "/userspace/blog";
    }


    /**
     * `/u/{username}/blogs/edits`: [get] 获取新增博客页面
     */
    @GetMapping("/{username}/blogs/edit")
    public ModelAndView createBlog(Model model) {
        model.addAttribute("blog", new Blog(null, null, null));
        model.addAttribute("fileServerUrl", fileServerUrl);
        return new ModelAndView("/userspace/blogedit", "blogModel", model);
    }

    /**
     * `/u/{username}/blogs/edit/{id}`: [get] 获取编辑博客的页面
     */
    @GetMapping("/{username}/blogs/edit/{id}")
    public ModelAndView editBlog(@PathVariable("username") String username, @PathVariable("id") Long id, Model model) {
        model.addAttribute("blog", blogService.getBlogById(id));
        model.addAttribute("fileServerUrl", fileServerUrl);
        return new ModelAndView("/userspace/blogedit", "blogModel", model);
    }

    /**
     * `/u/{username}/blogs/edit`: [post] 保存博客
     */
    @PostMapping("/{username}/blogs/edit")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> saveBlog(@PathVariable("username") String username, @RequestBody Blog blog) {

        // 判断是修改还是新增
        if (blog.getId() != null) {
            Blog originBlog = blogService.getBlogById(blog.getId());
            originBlog.setTitle(blog.getTitle());
            originBlog.setContent(blog.getContent());
            originBlog.setSummary(blog.getSummary());
            blogService.saveBlog(originBlog);
        } else {
            User user = (User) userDetailsService.loadUserByUsername(username);
            blog.setUser(user);
            blogService.saveBlog(blog);
        }

        String redirectUrl = "/u/" + username + "/blogs/" + blog.getId();
        return ResponseEntity.ok().body(new Response(true, "处理成功", redirectUrl));
    }

    /**
     * `/u/{username}/blogs/delete/{id}`: [delete] 删除博客
     */
    @DeleteMapping("/{username}/blogs/{id}")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> deleteBlog(@PathVariable("username") String username, @PathVariable("id") Long id) {

        blogService.removeBlog(id);

        String redirectUrl = "/u/" + username + "/blogs";
        return ResponseEntity.ok().body(new Response(true, "处理成功", redirectUrl));
    }

    /**
     * `/u/{username}/profile`: [get] 获取个人设置页面
     *
     * @PreAuthorize 请求该方法的的用户是当前用户才有权限访问
     */
    @GetMapping(value = "/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView profile(@PathVariable("username") String username, ModelMap modelMap) {
        User user = (User) userDetailsService.loadUserByUsername(username);
        modelMap.addAttribute("user", user);
        // 把文件服务器的地址发送给客户端
        modelMap.addAttribute("fireServerUrl", fileServerUrl);
        return new ModelAndView("/userspace/profile", "userModel", modelMap);
    }

    /**
     * `/u/{username}/profile`: [post] 保存个人设置
     */
    @PostMapping(value = "/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")
    public String saveProfile(@PathVariable("username") String username, User user) {
        User originalUser = userService.getUserById(user.getId());
        originalUser.setEmail(user.getEmail());
        originalUser.setName(user.getName());

        // 判断密码是否做了变更
        /*String rawPassword = originalUser.getPassword();
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodePasswd = encoder.encode(user.getPassword());
        boolean isMatch = encoder.matches(rawPassword, encodePasswd);
        if (!isMatch) {
            originalUser.setEncodePassword(user.getPassword());
        }*/
        originalUser.setPassword(user.getPassword());

        userService.saveUser(originalUser);
        return "redirect:/u/" + username + "/profile";
    }

    /**
     * `/u/{username}/avatar`: [get] 获取个人头像页面
     */
    @GetMapping(value = "/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView avatar(@PathVariable("username") String username, ModelMap modelMap) {
        User user = (User) userDetailsService.loadUserByUsername(username);
        modelMap.addAttribute("user", user);
        return new ModelAndView("/userspace/avatar", "userModel", modelMap);
    }

    /**
     * `/u/{username}/avatar`: [post] 保存个人头像
     */
    @PostMapping(value = "/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")
    @ResponseBody
    public ResponseEntity<Response> saveAvatar(@PathVariable("username") String username, @RequestBody User user) {
        String avatarUrl = user.getAvatar();

        User originUser = userService.getUserById(user.getId());
        originUser.setAvatar(avatarUrl);
        userService.saveUser(originUser);

        return ResponseEntity.ok().body(new Response(true, "success", avatarUrl));
    }

}
