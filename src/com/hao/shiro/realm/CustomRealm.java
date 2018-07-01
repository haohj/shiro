package com.hao.shiro.realm;

import com.hao.shiro.po.ActiveUser;
import com.hao.shiro.po.SysPermission;
import com.hao.shiro.service.SysService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class CustomRealm extends AuthorizingRealm {
    @Autowired
    private SysService sysService;

    @Override
    public String getName() {
        return "customRealm";
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }


    /**
     * 认证
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //身份信息，用户名
        String username = (String) token.getPrincipal();

        //根据用户名从数据库中查询用户
        String password = "123456";

        ActiveUser activeUser = new ActiveUser();
        activeUser.setUserid("zhangsan");
        activeUser.setUsercode("zhangsan");
        activeUser.setUsername("张三");

        //根据用户id获取菜单
        List<SysPermission> menuList = null;
        try {
            menuList = sysService.findMenuListByUserId(activeUser.getUserid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        activeUser.setMenus(menuList);

        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(activeUser, password, this.getName());
        return simpleAuthenticationInfo;
    }

    /**
     * 授权
     *
     * @param principal
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        ActiveUser activeUser = (ActiveUser) principal.getPrimaryPrincipal();

        //模拟从数据库中查询权限
        List<String> permissions = new ArrayList<String>();
        permissions.add("user:create");//用户的创建
//        permissions.add("item:query");//商品查询权限
        permissions.add("item:add");//商品添加权限
        permissions.add("item:edit");//商品修改权限

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addStringPermissions(permissions);

        return simpleAuthorizationInfo;
    }
}
