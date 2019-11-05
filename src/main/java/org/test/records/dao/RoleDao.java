package org.test.records.dao;

import org.test.records.bean.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author dzp
 * @since 2019-10-23
 */
public interface RoleDao extends JpaRepository<Role,Integer>, JpaSpecificationExecutor<Role> {

    Role findRoleByName(String name);

    Role findRoleByRoleId(Integer roleId);
}
