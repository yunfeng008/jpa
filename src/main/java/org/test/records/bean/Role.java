package org.test.records.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author dzp
 * @since 2019-10-23
 */
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
@Entity(name = "t_base_role")
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;

    private String name;

    /**
     * 所有菜单id "," 分割
     */
    @Transient
    private String resourcesids;

    @Override
    public String toString() {
        return "Role{" +
                "roleId=" + roleId +
                ", name='" + name + '\'' +
                '}';
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResourcesids() {
        return resourcesids;
    }

    public void setResourcesids(String resourcesids) {
        this.resourcesids = resourcesids;
    }
}
