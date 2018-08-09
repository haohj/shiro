package com.hao.shiro.realm;

import com.hao.shiro.po.ActiveUser;
import com.hao.shiro.po.SysPermission;
import com.hao.shiro.po.SysUser;
import com.hao.shiro.service.SysService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

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
        SysUser user = null;
        try {
            user = sysService.findSysUserByUserCode(username);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //判断账号是否存在
        if (user == null) {
            return null;
        }

        //根据用户id获取菜单
        List<SysPermission> menuList = null;
        try {
            menuList = sysService.findMenuListByUserId(user.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //获取用户密码
        String password = user.getPassword();
        //盐
        String salt = user.getSalt();

        ActiveUser activeUser = new ActiveUser();
        activeUser.setUserid(user.getId());
        activeUser.setUsercode(user.getUsercode());
        activeUser.setUsername(user.getUsername());
        activeUser.setMenus(menuList);

        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(activeUser, password, ByteSource.Util.bytes(salt), this.getName());
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

        //用户id
        String userId = activeUser.getUserid();

        //模拟从数据库中查询权限
        List<SysPermission> permissions = null;

        try {
            permissions = sysService.findPermissionListByUserId(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //构建shiro授权信息
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        for (SysPermission sysPermission : permissions) {
            simpleAuthorizationInfo.addStringPermission(sysPermission.getPercode());
        }

        return simpleAuthorizationInfo;
    }
}
