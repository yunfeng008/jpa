package org.test.records.dao;

import org.test.records.bean.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author dzp
 * @since 2019-10-23
 */
public interface UserRoleDao extends JpaRepository<UserRole,Integer> {

    List<UserRole> findUserRolesByUserId(Integer userId);
}
