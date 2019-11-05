package org.test.records.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author dzp
 * @since 2019-10-23
 */
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
@Entity(name = "t_base_roleresource")
public class RoleResource implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer resourceId;

    private Integer roleId;

    @Override
    public String toString() {
        return "RoleResource{" +
                "id=" + id +
                ", resourceId=" + resourceId +
                ", roleId=" + roleId +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
