package org.test.records.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.test.records.bean.Resource;
import org.test.records.bean.User;
import org.test.records.service.ResourceService;
import org.test.records.service.UserService;
import org.test.records.util.Constants;
import org.test.records.util.CoreException;
import org.test.records.util.UuidUtil;
import org.test.records.util.retresult.RetResponse;
import org.test.records.util.retresult.RetResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author dzp
 * @since 2019-10-23
 */
@Slf4j
@RestController
@RequestMapping("/users")
@Api(tags = {"用户基本信息操作"}, description = "用户基本信息操作")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ResourceService resourceService;

    @ApiIgnore
    @PostMapping("/login")
    public ModelAndView login(HttpServletRequest request, Model model) throws Exception {
        try {
            //获得登陆的用户名和密码
            String name = request.getParameter("name");
            String password = request.getParameter("password");
            //验证登陆
            UsernamePasswordToken token = new UsernamePasswordToken(name, password);
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
            //获取菜单
            List<Resource> resourceList = resourceService.findResourceList();
            //登陆成功保存session
            Session session = SecurityUtils.getSubject().getSession();
            session.setAttribute(Constants.SESSION_USERNM, name);
            session.setAttribute(Constants.SESSION_RESLIST,resourceList);
            //重定向到主页面去
            return new ModelAndView("main");
        } catch (UnknownAccountException e) {
            log.error("用户名或密码错误!");
            model.addAttribute("error", "用户名或密码错误");
            return new ModelAndView("login");
        } catch (IncorrectCredentialsException e) {
            log.error("用户名或密码错误!");
            model.addAttribute("error", "用户名或密码错误");
            return new ModelAndView("login");
        }catch (DisabledAccountException e) {
            log.error("账号错误，请联系管理员！");
            model.addAttribute("error", "账号错误，请联系管理员！");
            return new ModelAndView("login");
        } catch (AuthenticationException e) {
            log.error("登录失败!");
            model.addAttribute("error", "登录失败!");
            return new ModelAndView("login");
        }
    }

    @ApiOperation(value = "移动端登录", notes = "移动端登录")
    @PostMapping("/app/login")
    public RetResult<Map> login(String username, String password)
    {
        Map map = new HashMap();
        try {
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
            User user = (User) subject.getPrincipal();
            map.put("msg", user.getToken());
            return RetResponse.makeOKRsp(map);
        } catch (UnknownAccountException e) {
            log.error("用户名或密码错误!");
            map.put("msg", "用户名或密码错误");
            return RetResponse.makeErrRsp(map);
        } catch (DisabledAccountException e) {
            log.error("账号错误，请联系管理员！");
            map.put("msg", "账号错误，请联系管理员！");
            return RetResponse.makeErrRsp(map);
        } catch (AuthenticationException e) {
            log.error("登录失败!");
            map.put("msg", "登录失败");
            return RetResponse.makeErrRsp(map);
        }
    }

    @ApiOperation(value = "根据用户token查找用户",notes = "根据用户token查找用户")
    @ApiParam(name = "token", value = "用户的token")
    @GetMapping(value = "/app/token")
    public RetResult<Map> findUserByToken(String token) {
        Map map = new HashMap();
        try{
            if (token == null || StringUtils.isEmpty(token)){
                throw new CoreException("token为空");
            }else {
                User user = userService.findUserByToken(token);
                if (user != null){
                    map.put("msg","查找用户成功");
                    return RetResponse.makeOKRsp(map);
                }else {
                    map.put("msg","根据用户token查找用户失败");
                    return RetResponse.makeErrRsp(map);
                }
            }
        } catch (CoreException e){
            log.error("根据用户token查找用户失败", e);
            map.put("msg","根据用户token查找用户失败");
            return RetResponse.makeErrRsp(map);
        }catch (Exception e) {
            log.error("根据用户token查找用户失败",e);
            map.put("msg","根据用户token查找用户失败");
            return RetResponse.makeErrRsp(map);
        }
    }

    @GetMapping(value = "/userManager")
    public ModelAndView openUserManager() {
        return new ModelAndView("user/userMng");
    }

    @ApiOperation(value = "根据用户ID查找用户",notes = "根据用户ID查找用户")
    @ApiParam(name = "userId", value = "用户的ID")
    @GetMapping(value = "/userid")
    public RetResult<User> findUserByUserId(Integer userId) {
        try{
            if (userId == null || StringUtils.isEmpty(userId)){
                throw new CoreException("userId为空");
            }else {
                User user = userService.findUserByUserId(userId);
                if (user == null || StringUtils.isEmpty(user)){
                    throw new CoreException("根据用户ID查找用户失败");
                }
                return RetResponse.makeOKRsp(user);
            }
        } catch (CoreException e){
            log.error("根据用户ID查找用户失败", e);
            return RetResponse.makeRsp(400,"根据用户ID查找用户失败");
        }catch (Exception e) {
            log.error("根据用户ID查找用户失败",e);
            return RetResponse.makeRsp(400,"根据用户ID查找用户失败");
        }
    }

    @ApiOperation(value = "查找用户集合",notes = "查找用户集合")
    @GetMapping(value = "/userlist")
    public RetResult<List<User>> findAll() {
        try{
            List<User> list = userService.findAll();
            if (list == null || StringUtils.isEmpty(list)){
                throw new CoreException("查找用户失败");
            }
            return RetResponse.makeOKRsp(list);
        } catch (CoreException e){
            log.error("查找用户失败", e);
            return RetResponse.makeRsp(400,"查找用户失败");
        }catch (Exception e) {
            log.error("查找用户失败",e);
            return RetResponse.makeRsp(400,"查找用户失败");
        }
    }

    @ApiOperation(value = "根据用户名称查找用户",notes = "根据用户名称查找用户")
    @ApiParam(name = "userName", value = "用户的userName")
    @GetMapping(value = "/username")
    public RetResult<User> findUserByUserName(String userName) {
        try{
            if (userName == null || StringUtils.isEmpty(userName)){
                throw new CoreException("用户名为空");
            }else {
                User user = userService.findUserByUserName(userName);
                if (user == null || StringUtils.isEmpty(user)){
                    throw new CoreException("根据用户名称查找用户失败");
                }
                return RetResponse.makeOKRsp(user);
            }
        } catch (CoreException e){
            log.error("根据用户名称查找用户失败", e);
            return RetResponse.makeRsp(400,"根据用户名称查找用户失败");
        } catch (Exception e) {
            log.error("根据用户名称查找用户失败",e);
            return RetResponse.makeRsp(400,"根据用户名称查找用户失败");
        }
    }

    @ApiOperation(value = "分页查询",notes = "分页查询")
    @PostMapping(value = "/pages")
    public RetResult<Page<User>> findUserByPage(HttpServletRequest request,String flag) {
        try {
            int limit = Integer.parseInt(request.getParameter("limit") == null ? "1" : request.getParameter("limit"));
            int page = Integer.parseInt(request.getParameter("page") == null ? "1" : request.getParameter("page"));
            if(flag.equals("0")){
                Page<User> userPage = userService.findUserByPage(new PageRequest(page-1, limit),null);
                if (userPage == null) {
                    throw new CoreException("分页查询用户信息失败");
                }
                return RetResponse.makeRsp(0,"success",userPage);
            }else {
                String username = request.getParameter("username");
                if (username == null) {
                    throw new CoreException("请输入查询内容");
                }
                Page<User> userPage = userService.findUserByPage(new PageRequest(page-1, limit),username);
                if (userPage == null) {
                    throw new CoreException("分页查询用户信息失败");
                }
                return RetResponse.makeRsp(0,"success",userPage);
            }
        } catch (CoreException e){
            log.error("分页查询用户信息失败", e);
            return RetResponse.makeRsp(400,"分页查询用户信息失败");
        }
    }

    @ApiOperation(value = "增加用户", notes = "增加用户")
    @PostMapping(value = "")
    public RetResult<String> save(User user) {
        try {
            if (user == null) {
                return RetResponse.makeErrRsp("插入用户信息为空");
            }
            user.setType(2);
            ByteSource salt = ByteSource.Util.bytes(user.getUsername());
            user.setPassword(String.valueOf(new SimpleHash("MD5", user.getPassword(), salt, 1024)));
            String token = String.valueOf(new SimpleHash("MD5", UuidUtil.get47UUID("TK"), "", 1024));
            user.setToken(token);
            User user1 = userService.findUserByUserName(user.getUsername());
            if (user1 != null) {
                return RetResponse.makeErrRsp("用户名称重复,请重新填写");
            } else {
                userService.save(user);
                return RetResponse.makeOKRsp("插入用户信息成功");
            }
        } catch (CoreException e) {
            log.error("插入用户信息失败",e);
            return RetResponse.makeErrRsp("插入用户信息失败");
        } catch (Exception e) {
            e.printStackTrace();
            return RetResponse.makeErrRsp("插入用户信息失败");
        }
    }

    @ApiOperation(value = "更新用户", notes = "更新用户")
    @PutMapping(value = "")
    public RetResult<String> update(User user) {
        try {
            if (user == null) {
                return RetResponse.makeErrRsp("更新用户信息为空");
            }
            if(user.getPassword().length() != 32){
                ByteSource salt = ByteSource.Util.bytes(user.getUsername());
                user.setPassword(String.valueOf(new SimpleHash("MD5", user.getPassword(), salt, 1024)));
            }
            userService.save(user);
            return RetResponse.makeOKRsp("更新用户信息成功");
        } catch (CoreException e) {
            log.error("更新用户信息失败",e);
            return RetResponse.makeErrRsp("更新用户信息失败");
        } catch (Exception e) {
            e.printStackTrace();
            return RetResponse.makeErrRsp("更新用户信息失败");
        }
    }

    @ApiOperation(value = "删除用户", notes = "删除用户")
    @ApiParam(name = "userId", value = "用户的userId")
    @DeleteMapping(value = "/userid")
    public RetResult<String> delete(Integer userId) {
        try {
            userService.delete(userId);
            return RetResponse.makeOKRsp("删除用户信息成功");
        } catch (CoreException e) {
            log.error("删除用户信息失败",e);
            return RetResponse.makeErrRsp("删除用户信息失败");
        }
    }

}
