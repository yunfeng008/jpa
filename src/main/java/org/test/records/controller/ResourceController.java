package org.test.records.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.test.records.bean.Resource;
import org.test.records.bean.RoleResource;
import org.test.records.service.ResourceService;
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
import java.util.ArrayList;
import java.util.List;

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
@RequestMapping("/resources")
@Api(tags = {"资源基本信息操作"}, description = "资源基本信息操作")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @GetMapping(value = "/resManager")
    public ModelAndView openResourceManager() {
        return new ModelAndView("res/resMng");
    }

    @ApiOperation(value = "根据资源ID查找资源",notes = "根据资源ID查找资源")
    @ApiParam(name = "resourceId", value = "资源的ID")
    @GetMapping(value = "/resourceid")
    public RetResult<Resource> findResourceByResourceId(Integer resourceId) {
        try{
            if (resourceId == null || StringUtils.isEmpty(resourceId)){
                throw new CoreException("resourceId为空");
            }else {
                Resource resource = resourceService.findResourceByResourceId(resourceId);
                if (resource == null || StringUtils.isEmpty(resource)){
                    throw new CoreException("根据ID查找资源失败");
                }
                return RetResponse.makeOKRsp(resource);
            }
        } catch (CoreException e){
            log.error("根据ID查找资源失败", e);
            return RetResponse.makeRsp(400,"根据ID查找资源失败");
        }catch (Exception e) {
            log.error("根据ID查找资源失败",e);
            return RetResponse.makeRsp(400,"根据ID查找资源失败");
        }
    }

    @ApiOperation(value = "根据角色ID查找资源,用于编辑页面的回显",notes = "根据角色ID查找资源,用于编辑页面的回显")
    @ApiParam(name = "roleId", value = "roleId")
    @GetMapping(value = "/roleid")
    public RetResult<List<Resource>> findResourceByRoleId(Integer roleId) {
        try{
            if (roleId == null || StringUtils.isEmpty(roleId)){
                throw new CoreException("roleId为空");
            }else {
                List<Resource> resourceList = new ArrayList<Resource>();
                List<RoleResource> list = resourceService.findRoleResourcesByRoleId(roleId);
                for (RoleResource r : list) {
                    Resource resource = resourceService.findResourceByResourceId(r.getResourceId());
                    if (resource == null || StringUtils.isEmpty(resource)){
                        throw new CoreException("根据ID查找资源失败");
                    }
                    resourceList.add(resource);
                }
                List<Resource> resourceList1 = resourceService.findAll();
                if(resourceList1!=null && resourceList!=null){
                    for(Resource s : resourceList1) {
                        for (Resource str : resourceList) {
                            if (s.getResourceId().equals(str.getResourceId())){
                                s.setChecked(true);
                            }
                        }
                    }
                }

                return RetResponse.makeOKRsp(resourceList1);
            }
        } catch (CoreException e){
            log.error("根据ID查找资源失败", e);
            return RetResponse.makeRsp(400,"根据ID查找资源失败");
        }catch (Exception e) {
            log.error("根据ID查找资源失败",e);
            return RetResponse.makeRsp(400,"根据ID查找资源失败");
        }
    }

    @ApiOperation(value = "查询所有资源", notes = "查询所有资源")
    @GetMapping(value = "/resourcelist")
    public RetResult<List<Resource>> findAll(){
        try {
            List<Resource> resourcesList = resourceService.findAll();
            if(resourcesList==null){
                throw new CoreException("未查到数据");
            }else{
                return RetResponse.makeOKRsp(resourcesList);
            }
        } catch (CoreException e) {
            log.error("查询所有菜单失败", e);
            return RetResponse.makeRsp(400,"查询所有菜单失败");
        } catch (Exception e) {
            log.error("查询所有资源失败",e);
            return RetResponse.makeRsp(400,"查询所有菜单失败");
        }
    }

    @ApiOperation(value = "分页查询",notes = "分页查询")
    @PostMapping(value = "/pages")
    public RetResult<Page<Resource>> findResourceByPage(HttpServletRequest request) {
        try {
            int limit = Integer.parseInt(request.getParameter("limit") == null ? "1" : request.getParameter("limit"));
            int page = Integer.parseInt(request.getParameter("page") == null ? "1" : request.getParameter("page"));
            Page<Resource> resourcePage = resourceService.findResourceByPage(new PageRequest(page-1, limit));
            if (resourcePage == null) {
                throw new CoreException("分页查询资源信息失败");
            }
            return RetResponse.makeRsp(0,"success",resourcePage);
        } catch (CoreException e){
            log.error("分页查询资源信息失败", e);
            return RetResponse.makeRsp(400,"分页查询资源信息失败");
        }
    }

    @ApiOperation(value = "增加资源", notes = "增加资源")
    @PostMapping(value = "")
    public RetResult<String> save(Resource resource) {
        try {
            if (resource == null) {
                return RetResponse.makeErrRsp("插入资源信息为空");
            }
            Resource resource1 = resourceService.findResourceByResourceName(resource.getResourceName());
            if (resource1 != null) {
                return RetResponse.makeErrRsp("名称重复,请重新填写");
            } else {
                resource.setResourceType("1");
                resourceService.save(resource);
                return RetResponse.makeOKRsp("插入资源信息成功");
            }
        } catch (CoreException e) {
            log.error("插入资源信息失败",e);
            return RetResponse.makeErrRsp("插入资源信息失败");
        } catch (Exception e) {
            e.printStackTrace();
            return RetResponse.makeErrRsp("插入资源信息失败");
        }
    }

    @ApiOperation(value = "更新资源", notes = "更新资源")
    @PutMapping(value = "")
    public RetResult<String> update(Resource resource) {
        try {
            if (resource == null) {
                return RetResponse.makeErrRsp("更新资源信息为空");
            }
            resource.setResourceType("1");
            resourceService.save(resource);
            return RetResponse.makeOKRsp("更新资源信息成功");
        } catch (CoreException e) {
            log.error("更新资源信息失败",e);
            return RetResponse.makeErrRsp("更新资源信息失败");
        } catch (Exception e) {
            e.printStackTrace();
            return RetResponse.makeErrRsp("更新资源信息失败");
        }
    }

    @ApiOperation(value = "删除资源", notes = "删除资源")
    @ApiParam(name = "resourceId", value = "roleId")
    @DeleteMapping(value = "/resourceid")
    public RetResult<String> delete(Integer resourceId) {
        try {
            resourceService.delete(resourceId);
            return RetResponse.makeOKRsp("删除资源信息成功");
        } catch (CoreException e) {
            log.error("删除资源信息失败",e);
            return RetResponse.makeErrRsp("删除资源信息失败");
        }
    }

}
