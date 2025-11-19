package top.mhxi.myshop.user.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.access.prepost.PreAuthorize;
import top.mhxi.myshop.common.utils.R;
import top.mhxi.myshop.common.to.UserTO;
import top.mhxi.myshop.user.entity.vo.EmailVO;
import top.mhxi.myshop.user.entity.vo.RegisterVO;
import top.mhxi.myshop.user.entity.User;
import top.mhxi.myshop.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@Tag(name = "User管理")
@RestController
@RequestMapping("/user/user")
public class UserController {

    @Autowired
    private UserService userService;


    @Operation(summary = "添加一个User")
    @PostMapping
    public R insert(@Valid @RequestBody UserTO userTO) {  // @Valid开启校验
        User user = new User();
        BeanUtils.copyProperties(userTO, user);

        int i = userService.insert(user);
        if (i == 1) {
            return R.ok();
        } else {
            return R.error();
        }
    }


    @Operation(summary = "根据id删除一个User")
    @DeleteMapping("/{id}")
    public R deleteById(@PathVariable Long id) {
        int i = userService.deleteById(id);
        if (i == 1) {
            return R.ok();
        } else {
            return R.error();
        }
    }


    @Operation(summary = "更新一个User")
    @PutMapping
    public R update(@Valid @RequestBody UserTO userTO) {
        User user = new User();
        BeanUtils.copyProperties(userTO, user);

        int i = userService.update(user);
        if (i == 1) {
            return R.ok();
        } else {
            return R.error();
        }
    }


    @Operation(summary = "根据id查询一个User")
    @GetMapping("/{id}")
    public R selectById(@PathVariable Long id) {
        User user = userService.selectById(id);

        if (user != null) {
            UserTO userTO = new UserTO();
            BeanUtils.copyProperties(user, userTO);
            return R.ok().data("record", userTO);
        } else {
            return R.error();
        }
    }


    @Operation(summary = "查询所有User")
    @GetMapping("/selectAll")
    public R selectAll() {
        List<User> users = userService.selectAll();

        if (!users.isEmpty()) {
            List<UserTO> userTOS = new ArrayList<>();

            for (User user : users) {
                UserTO userTO = new UserTO();
                BeanUtils.copyProperties(user, userTO);
                userTOS.add(userTO);
            }

            return R.ok().data("record", userTOS);
        } else {
            return R.error();
        }
    }


    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public R register(@Valid @RequestBody RegisterVO vo) {
        int i = userService.register(vo);
        if (i == 1) {
            return R.ok();
        } else {
            return R.error();
        }
    }


    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public R login(@RequestBody RegisterVO vo, HttpServletResponse response) {

        // 登录，往redis中放入sessionid，然后作为cookie，返回给前端
        String sessionId = userService.login(vo.getName(), vo.getPassword());

        ResponseCookie cookie = ResponseCookie.from("SESSIONID", sessionId)
                .path("/")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")  // 支持跨域发送
                .maxAge(24 * 60 * 60)  //一天
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return R.ok();
    }


    @Operation(summary = "用户退出登录")
    @PostMapping("/logout")
    public R logout(@CookieValue(value = "SESSIONID", required = false) String sessionId) {
        userService.logout(sessionId);
        return R.ok();
    }


    @Operation(summary = "根据session查询用户信息")
    @PostMapping("/getUserBySession")
    public R getUserBySession(@CookieValue(value = "SESSIONID", required = false) String sessionId) {

        UserTO userTO = userService.getUserBySession(sessionId);

        if (userTO != null) {
            return R.ok().data("record", userTO);
        } else {
            return R.error();
        }
    }

    @Operation(summary = "发送验证码")
    @PostMapping("/sendCode")
    // string要用对象包装起来，否则会出错
    public R sendCode(@Valid @RequestBody EmailVO vo) {
        userService.sendCode(vo.getEmail());
        return R.ok().message("发送成功");
    }

}