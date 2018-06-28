package com.hao.shiro.realm;

import com.hao.shiro.po.ActiveUser;
import com.hao.shiro.po.SysPermission;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.ArrayList;
import java.util.List;

public class CustomRealm extends AuthorizingRealm {
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
        //凭证信息，密码
        String password = (String) token.getCredentials();

        //根据用户名从数据库中查询用户
        //模拟用户名不匹配
        if (!"zhang".equals(username)) {
            return null;
        }

        //从数据库查询用户具有权限的菜单列表
        List<SysPermission> menus = new ArrayList();
        SysPermission menu1 = new SysPermission();
        SysPermission menu2 = new SysPermission();

        menu1.setId((long) 11);
        menu1.setName("商品管理");
        menu1.setType("menu");
        menu1.setUrl("/item/queryItem.action");
        menu1.setParentid((long) 1);
        menu1.setParentids("0/1/");
        menu1.setSortstring("1.");
        menu1.setAvailable("1");
        menus.add(menu1);

        menu2.setId((long) 21);
        menu2.setName("用户管理");
        menu2.setType("menu");
        menu2.setUrl("/user/query.action");
        menu2.setPercode("user:query");
        menu2.setParentid((long) 1);
        menu2.setParentids("0/1/");
        menu2.setSortstring("2.");
        menu2.setAvailable("1");
        menus.add(menu2);

        //构造认证用户
        ActiveUser user = new ActiveUser();
        user.setMenus(menus);
        user.setUserid(username);
        user.setUsername(username);
        user.setUsercode(username);

        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(user, password, this.getName());
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

        String userId = activeUser.getUserid();
        //根据用户id从数据库查询授权url
        List<String> permissions = new ArrayList<String>();
        permissions.add("item:query");
        permissions.add("item:update");


        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        for (String permission:permissions){
            simpleAuthorizationInfo.addStringPermission(permission);
        }

        return simpleAuthorizationInfo;
    }
}
