package org.test.records.service;

import lombok.extern.slf4j.Slf4j;
import org.test.records.bean.Role;
import org.test.records.bean.UserRole;
import org.test.records.dao.RoleDao;
import org.test.records.dao.UserRoleDao;
import org.test.records.util.CoreException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dzp
 * @since 2019-10-23
 */
@Slf4j
@Service
@Transactional
public class RoleService {
    @Autowired
    RoleDao roleDao;
    @Autowired
    UserRoleDao userRoleDao;

    public List<Role> findRolesByRoleIds(Integer userId){

        List<Role> roleList = new ArrayList<Role>();
        Role role = new Role();
        List<UserRole> userRolelist = userRoleDao.findUserRolesByUserId(userId);
        for (UserRole userRole:userRolelist) {
            role = roleDao.findRoleByRoleId(userRole.getRoleId());
            roleList.add(role);
        }
        return roleList;
    }

    public Role findRoleByRoleId(Integer roleId) throws Exception{
        try {
            return roleDao.findRoleByRoleId(roleId);
        }catch (Exception e) {
            log.error("根据ID查找角色失败",e);
            throw new CoreException("根据ID查找角色失败");
        }
    }

    public Role findRoleByName(String name) throws Exception{
        try {
            return roleDao.findRoleByName(name);
        }catch (Exception e) {
            log.error("根据名称查找角色失败",e);
            throw new CoreException("根据名称查找角色失败");
        }
    }

    public Page<Role> findRoleByPage(Pageable pageable) throws CoreException{
        return roleDao.findAll((Root<Role> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = null;

            return predicate;
        }, pageable);
    }

    public void save(Role role) throws CoreException{
        try {
            if (role == null) {
                throw new CoreException("插入角色信息为空");
            }
            roleDao.save(role);
        }catch (CoreException e) {
            log.error("插入角色信息失败",e);
            throw new CoreException("插入角色信息失败");
        }
    }

    public void delete(Integer roleId) throws CoreException{
        try {
            if (roleId == null) {
                throw new CoreException("传入信息为空");
            }
            roleDao.deleteById(roleId);
        }catch (CoreException e) {
            log.error("删除角色信息失败",e);
            throw new CoreException("删除角色信息失败");
        }
    }

}
