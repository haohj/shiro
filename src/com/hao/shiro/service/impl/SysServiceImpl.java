package com.hao.shiro.service.impl;

import com.hao.shiro.controller.exceptionResolver.CustomException;
import com.hao.shiro.mapper.SysPermissionMapperCustom;
import com.hao.shiro.mapper.SysUserMapper;
import com.hao.shiro.po.ActiveUser;
import com.hao.shiro.po.SysPermission;
import com.hao.shiro.po.SysUser;
import com.hao.shiro.po.SysUserExample;
import com.hao.shiro.service.SysService;
import com.hao.shiro.utils.MD5;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SysServiceImpl implements SysService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysPermissionMapperCustom sysPermissionMapperCustom;

    @Override
    public ActiveUser authenticat(String userCode, String password) throws Exception {
        /**
         认证过程：
         根据用户身份（账号）查询数据库，如果查询不到用户不存在
         对输入的密码 和数据库密码 进行比对，如果一致，认证通过
         */
        SysUser sysUser = this.findSysUserByUserCode(userCode);
        if(sysUser==null){
            throw new CustomException("用户账号不存在");
        }

        //验证密码
        if (!sysUser.getPassword().equalsIgnoreCase(new MD5().getMD5ofStr(password))){
            throw new CustomException("用户名或密码错误!");
        }

        //根据用户id查询菜单
        List<SysPermission> menus = this.findMenuListByUserId(sysUser.getId());
        //根据用户id查询授权url
        List<SysPermission> permissions = this.findPermissionListByUserId(sysUser.getId());

        ActiveUser activeUser = new ActiveUser();
        activeUser.setUserid(sysUser.getId());
        activeUser.setUsercode(userCode);
        activeUser.setUsername(sysUser.getUsername());//用户名称
        activeUser.setMenus(menus);
        activeUser.setPermissions(permissions);
        return activeUser;
    }

    @Override
    public SysUser findSysUserByUserCode(String userCode) throws Exception {
        SysUserExample sysUserExample = new SysUserExample();
        SysUserExample.Criteria criteria = sysUserExample.createCriteria();
        criteria.andIdEqualTo(userCode);
        List<SysUser> list = sysUserMapper.selectByExample(sysUserExample);
        if (list != null && list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<SysPermission> findMenuListByUserId(String userid) throws Exception {
        return sysPermissionMapperCustom.findMenuByUserid(userid);
    }

    @Override
    public List<SysPermission> findPermissionListByUserId(String userid) throws Exception {
        return sysPermissionMapperCustom.findPermissionByUserid(userid);
    }
}
