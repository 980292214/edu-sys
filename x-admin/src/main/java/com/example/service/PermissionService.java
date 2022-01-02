package com.example.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Permission;
import com.example.entity.Role;
import com.example.mapper.PermissionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class PermissionService extends ServiceImpl<PermissionMapper, Permission> {

    @Resource
    private PermissionMapper permissionMapper;

    @Resource
    private RoleService roleService;
    //传进角色
    public List<Permission> getByRoles(List<Role> roles) {
        List<Permission> permissions = new ArrayList<>();  //权限list
        for (Role role : roles) {
            //遍历这些角色
            Role r = roleService.getById(role.getId());
            //如果这个角色的权限不为空
            if (CollUtil.isNotEmpty(r.getPermission())) {
                //遍历该角色的权限
                for (Object permissionId : r.getPermission()) {
                    Permission permission = getById((int) permissionId);
                    if (permissions.stream().noneMatch(p -> p.getPath().equals(permission.getPath()))) {
                        permissions.add(permission);
                    }
                }
            }
        }
        return permissions;
    }

    @Transactional
    public void delete(Long id) {
        removeById(id);
        // 删除角色分配的菜单
        List<Role> list = roleService.list();
        for (Role role : list) {
            // 重新分配权限
            List<Long> newP = new ArrayList<>();
            for (Object p : role.getPermission()) {
                Long pl = Long.valueOf(p + "");
                if (!id.equals(pl)) {
                    newP.add(Long.valueOf(p + ""));
                }
            }
            role.setPermission(newP);
            roleService.updateById(role);
        }
    }
}
