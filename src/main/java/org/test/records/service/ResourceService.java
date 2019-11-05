package org.test.records.service;

import lombok.extern.slf4j.Slf4j;
import org.test.records.bean.Resource;
import org.test.records.bean.Role;
import org.test.records.bean.RoleResource;
import org.test.records.dao.ResourceDao;
import org.test.records.dao.RoleResourceDao;
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
public class ResourceService {

    @Autowired
    ResourceDao resourceDao;
    @Autowired
    RoleResourceDao roleResourceDao;

    public List<Resource> findResourcesByRole(Role role) throws CoreException {
        try{
            if(role == null){
                throw new Exception("角色为空");
            }
            List<RoleResource> roleResourceList = roleResourceDao.findRoleResourcesByRoleId(role.getRoleId());
            List<Resource> list = new ArrayList<Resource>();
            List<Integer> resourceId = new ArrayList<Integer>();
            if(roleResourceList == null){
                throw new Exception("资源集合为空");
            }
            for(RoleResource roleResource:roleResourceList ){
                resourceId.add(roleResource.getResourceId());
            }
            list = resourceDao.findAllById(resourceId);
            return list;
        }catch (CoreException e){
            throw e;
        }catch (Exception e){
            throw new CoreException("查询失败");
        }
    }

    public List<Resource> findResourceList() throws CoreException {
        try {
            int parentId = 0;
            List<Resource> listFirst = resourceDao.findResourcesByParentId(parentId);
            if(listFirst != null)
            {
                for(int i=0;i<listFirst.size();i++)
                {
                    Resource topResource = listFirst.get(i);
                    List<Resource> childList = resourceDao.findResourcesByParentId(topResource.getResourceId());
                    topResource.setChildList(childList);
                }
            }else{
                throw new CoreException("查询失败");
            }
            return listFirst;
        }catch (CoreException e){
            throw e;
        }catch (Exception e){
            throw new CoreException("查询失败");
        }
    }

    public Resource findResourceByResourceId(Integer resourceId) throws Exception{
        try {
            return resourceDao.findResourceByResourceId(resourceId);
        }catch (Exception e) {
            log.error("根据ID查找资源失败",e);
            throw new CoreException("根据ID查找资源失败");
        }
    }

    public Resource findResourceByResourceName(String name) throws Exception{
        try {
            return resourceDao.findResourceByResourceName(name);
        }catch (Exception e) {
            log.error("根据名称查找资源失败",e);
            throw new CoreException("根据名称查找资源失败");
        }
    }

    public Page<Resource> findResourceByPage(Pageable pageable) throws CoreException{
        return resourceDao.findAll((Root<Resource> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = null;

            return predicate;
        }, pageable);
    }

    public void save(Resource resource) throws CoreException{
        try {
            if (resource == null) {
                throw new CoreException("插入资源信息为空");
            }
            resourceDao.save(resource);
        }catch (CoreException e) {
            log.error("插入资源信息失败",e);
            throw new CoreException("插入资源信息失败");
        }
    }

    public void delete(Integer resourceId) throws CoreException{
        try {
            if (resourceId == null) {
                throw new CoreException("传入资源信息为空");
            }
            resourceDao.deleteById(resourceId);
        }catch (CoreException e) {
            log.error("删除资源信息失败",e);
            throw new CoreException("删除资源信息失败");
        }
    }

    public List<Resource> findAll() throws Exception{
        try {
            return resourceDao.findAll();
        }catch (Exception e) {
            log.error("查询资源信息失败",e);
            throw new CoreException("查询资源信息失败");
        }
    }

    public void saveRoleResource(RoleResource roleResource) throws CoreException{
        try {
            if (roleResource == null) {
                throw new CoreException("插入信息为空");
            }
            roleResourceDao.save(roleResource);
        }catch (CoreException e) {
            log.error("插入信息失败",e);
            throw new CoreException("插入信息失败");
        }
    }

    public void deleteRoleResource(Integer roleId) throws CoreException{
        try {
            if (roleId == null) {
                throw new CoreException("传入信息为空");
            }
            roleResourceDao.deleteRoleResourceByRoleId(roleId);
        }catch (CoreException e) {
            log.error("删除信息失败",e);
            throw new CoreException("删除信息失败");
        }
    }

    public List<RoleResource> findRoleResourcesByRoleId(Integer roleId) throws CoreException {
        try {
            if (roleId == null) {
                throw new CoreException("传入信息为空");
            }
            List<RoleResource> list = roleResourceDao.findRoleResourcesByRoleId(roleId);
            if (list == null) {
                throw new CoreException("查找信息失败");
            }
            return list;
        }catch (CoreException e) {
            log.error("查找信息失败",e);
            throw new CoreException("查找信息失败");
        }
    }
}
