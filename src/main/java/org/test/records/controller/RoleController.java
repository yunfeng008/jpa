package org.test.records.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.test.records.bean.Role;
import org.test.records.bean.RoleResource;
import org.test.records.service.ResourceService;
import org.test.records.service.RoleService;
import org.test.records.util.CoreException;
import org.test.records.util.retresult.RetResponse;
import org.test.records.util.retresult.RetResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

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
@RequestMapping("/roles")
@Api(tags = {"角色基本信息操作"}, description = "角色基本信息操作")
public class RoleController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private ResourceService resourceService;

    @GetMapping(value = "/roleManager")
    public ModelAndView openRoleManager() {
        return new ModelAndView("role/roleMng");
    }

    @ApiOperation(value = "根据角色ID查找角色",notes = "根据角色ID查找角色")
    @ApiParam(name = "roleId", value = "角色的ID")
    @GetMapping(value = "/roleid")
    public RetResult<Role> findRoleByRoleId(Integer roleId) {
        try{
            if (roleId == null || StringUtils.isEmpty(roleId)){
                throw new CoreException("roleId为空");
            }else {
                Role role = roleService.findRoleByRoleId(roleId);
                if (role == null || StringUtils.isEmpty(role)){
                    throw new CoreException("根据ID查找角色失败");
                }
                return RetResponse.makeOKRsp(role);
            }
        } catch (CoreException e){
            log.error("根据ID查找角色失败", e);
            return RetResponse.makeRsp(400,"根据ID查找角色失败");
        }catch (Exception e) {
            log.error("根据ID查找角色失败",e);
            return RetResponse.makeRsp(400,"根据ID查找角色失败");
        }
    }

    @ApiOperation(value = "分页查询",notes = "分页查询")
    @PostMapping(value = "/pages")
    public RetResult<Page<Role>> findRoleByPage(HttpServletRequest request) {
        try {
            int limit = Integer.parseInt(request.getParameter("limit") == null ? "1" : request.getParameter("limit"));
            int page = Integer.parseInt(request.getParameter("page") == null ? "1" : request.getParameter("page"));
            Page<Role> rolePage = roleService.findRoleByPage(new PageRequest(page-1, limit));
            if (rolePage == null) {
                throw new CoreException("分页查询角色信息失败");
            }
            return RetResponse.makeRsp(0,"success",rolePage);
        } catch (CoreException e){
            log.error("分页查询角色信息失败", e);
            return RetResponse.makeRsp(400,"分页查询角色信息失败");
        }
    }

    @ApiOperation(value = "增加角色", notes = "增加角色")
    @PostMapping(value = "")
    public RetResult<String> save(Role role) {
        try {
            if (role == null) {
                return RetResponse.makeErrRsp("插入角色信息为空");
            }
            Role role1 = roleService.findRoleByName(role.getName());
            if (role1 != null) {
                return RetResponse.makeErrRsp("名称重复,请重新填写");
            } else {
                roleService.save(role);
                //删除岗位资源关联
                resourceService.deleteRoleResource(role.getRoleId());
                //重新添加岗位资源关联
                String[] resourcesids = role.getResourcesids().split(",");
                for (int i = 0; i < resourcesids.length; i++) {
                    if(!StringUtils.isEmpty(resourcesids[i])){
                        RoleResource roleResource = new RoleResource();
                        roleResource.setRoleId(role.getRoleId());
                        roleResource.setResourceId(Integer.parseInt(resourcesids[i]));
                        resourceService.saveRoleResource(roleResource);
                    }
                }
                return RetResponse.makeOKRsp("插入角色信息成功");
            }
        } catch (CoreException e) {
            log.error("插入角色信息失败",e);
            return RetResponse.makeErrRsp("插入角色信息失败");
        } catch (Exception e) {
            e.printStackTrace();
            return RetResponse.makeErrRsp("插入角色信息失败");
        }
    }

    @ApiOperation(value = "更新角色", notes = "更新角色")
    @PutMapping(value = "")
    public RetResult<String> update(Role role) {
        try {
            if (role == null) {
                return RetResponse.makeErrRsp("更新角色信息为空");
            }
            roleService.save(role);
            //删除岗位资源关联
            resourceService.deleteRoleResource(role.getRoleId());
            //重新添加岗位资源关联
            String[] resourcesids = role.getResourcesids().split(",");
            for (int i = 0; i < resourcesids.length; i++) {
                if(!StringUtils.isEmpty(resourcesids[i])){
                    RoleResource roleResource = new RoleResource();
                    roleResource.setRoleId(role.getRoleId());
                    roleResource.setResourceId(Integer.parseInt(resourcesids[i]));
                    resourceService.saveRoleResource(roleResource);
                }
            }
            return RetResponse.makeOKRsp("更新角色信息成功");
        } catch (CoreException e) {
            log.error("更新角色信息失败",e);
            return RetResponse.makeErrRsp("更新角色信息失败");
        } catch (Exception e) {
            e.printStackTrace();
            return RetResponse.makeErrRsp("更新角色信息失败");
        }
    }

    @ApiOperation(value = "删除角色", notes = "删除角色")
    @ApiParam(name = "roleId", value = "roleId")
    @DeleteMapping(value = "/roleid")
    public RetResult<String> delete(Integer roleId) {
        try {
            roleService.delete(roleId);
            //删除关联
            resourceService.deleteRoleResource(roleId);
            return RetResponse.makeOKRsp("删除角色信息成功");
        } catch (CoreException e) {
            log.error("删除角色信息失败",e);
            return RetResponse.makeErrRsp("删除角色信息失败");
        }
    }

}
