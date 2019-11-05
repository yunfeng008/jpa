package org.test.records.dao;

import org.test.records.bean.RoleResource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author dzp
 * @since 2019-10-23
 */
public interface RoleResourceDao extends JpaRepository<RoleResource,Integer> {

    List<RoleResource> findRoleResourcesByRoleId(Integer roleId);

    void deleteRoleResourceByRoleId(Integer roleId);

}
