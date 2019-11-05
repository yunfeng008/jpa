package org.test.records.controller;

import org.test.records.bean.User;
import org.test.records.service.UserService;
import org.test.records.util.CoreException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;


public abstract class DefaultController {

    @Autowired
    private UserService userService;

    public DefaultController(){}

    public User findUserByToken(String token) {
        try{
            if (token == null || StringUtils.isEmpty(token)){
                throw new CoreException("token为空");
            }else {
                User user = userService.findUserByToken(token);
                if (user == null){
                    throw new CoreException("根据用户token查找用户失败");
                }
                return user;
            }
        } catch (CoreException e){
            e.printStackTrace();
            return null;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
