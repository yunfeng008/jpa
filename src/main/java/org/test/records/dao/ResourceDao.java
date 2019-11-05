package org.test.records.dao;

import org.test.records.bean.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author dzp
 * @since 2019-10-23
 */
public interface ResourceDao extends JpaRepository<Resource,Integer>, JpaSpecificationExecutor<Resource> {

    @Override
    List<Resource> findAllById(Iterable<Integer> iterable);

    List<Resource> findResourcesByParentId(Integer parentId);

    Resource findResourceByResourceName(String name);

    Resource findResourceByResourceId(Integer resourceId);

}
