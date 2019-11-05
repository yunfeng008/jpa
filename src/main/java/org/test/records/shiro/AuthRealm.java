package org.test.records.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.test.records.bean.Resource;
import org.test.records.bean.Role;
import org.test.records.bean.User;
import org.test.records.service.ResourceService;
import org.test.records.service.RoleService;
import org.test.records.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;

public class AuthRealm extends AuthorizingRealm {

    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    ResourceService resourceService;

    /**
     * 授权用户权限
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        User user  = (User)principals.getPrimaryPrincipal();
        try {
            List<Role> roleList = roleService.findRolesByRoleIds(user.getUserId());
            if (roleList != null){
                for(int i=0; i<roleList.size(); i++){
                    authorizationInfo.addRole(roleList.get(i).getName());
                    List<Resource> resourceList = resourceService.findResourcesByRole(roleList.get(i));
                    if(resourceList != null){
                        for(int j=0; j<resourceList.size(); j++){
                            authorizationInfo.addStringPermission(resourceList.get(j).getPermission());
                        }
                    }
                }
            }
            if (authorizationInfo != null && authorizationInfo.getStringPermissions() != null) {
                Set<String> permissions = authorizationInfo.getStringPermissions();
                for (String permission : permissions) {
                    if (StringUtils.isEmpty(permission)) {
                        permissions.remove(permission);
                    }
                }
            }
            return authorizationInfo;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return authorizationInfo;
        }
    }

    /**
     * 验证用户身份
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
            throws AuthenticationException {
        //获取用户的输入的账号
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        //盐值
        ByteSource salt = ByteSource.Util.bytes(token.getUsername());
        SimpleHash simpleHash = new SimpleHash("MD5", token.getPassword(), salt, 1024);
        token.setPassword(String.valueOf(simpleHash).toCharArray());

        String name = token.getUsername();
        User user = new User();
        try {
            user = userService.findUserByUserName(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 从数据库获取用户
        if (null == user) {
            throw new AccountException("该用户不存在！");
        }else if(user.getStatus() == null || user.getStatus().shortValue() != 1){
            throw new DisabledAccountException("用户状态不正常！");
        }

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user, //用户
                user.getPassword(), //密码
                getName()  //realm name
        );
        return authenticationInfo;
    }

}