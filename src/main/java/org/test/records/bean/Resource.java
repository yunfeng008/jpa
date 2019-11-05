package org.test.records.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author dzp
 * @since 2019-10-23
 */
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
@Entity(name = "t_base_resource")
public class Resource implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer resourceId;

    private String resourceName;

    private String resourceUrl;

    private Integer parentId;

    private Integer resourceLevel;

    private Integer resourceOrder;

    private String resourceType;

    private String permission;

    private String icon;

    @Transient
    private Integer id;

    @Transient
    private Integer pid;

    @Transient
    private boolean checked = false;

    @Transient
    private List<Resource> childList;

    @Override
    public String toString() {
        return "Resource{" +
                "resourceId=" + resourceId +
                ", resourceName='" + resourceName + '\'' +
                ", resourceUrl='" + resourceUrl + '\'' +
                ", parentId=" + parentId +
                ", resourceLevel=" + resourceLevel +
                ", resourceOrder=" + resourceOrder +
                ", resourceType='" + resourceType + '\'' +
                ", permission='" + permission + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getResourceLevel() {
        return resourceLevel;
    }

    public void setResourceLevel(Integer resourceLevel) {
        this.resourceLevel = resourceLevel;
    }

    public Integer getResourceOrder() {
        return resourceOrder;
    }

    public void setResourceOrder(Integer resourceOrder) {
        this.resourceOrder = resourceOrder;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<Resource> getChildList() {
        return childList;
    }

    public void setChildList(List<Resource> childList) {
        this.childList = childList;
    }

    public Integer getId() {
        return resourceId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return parentId;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
