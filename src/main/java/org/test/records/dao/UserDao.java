package org.test.records.dao;

import org.test.records.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author dzp
 * @since 2019-10-23
 */
public interface UserDao extends JpaRepository<User,Integer>, JpaSpecificationExecutor<User> {

    User findUserByUsername(String userName);

    User findUserByToken(String token);

    User findUserByUserId(Integer userId);

//    @Query(value = "select count(*) from t_book",nativeQuery = true)
//    Long totalCount();
//
//    @Query(value = "update t_book set author=?1 where book_name=?2",nativeQuery = true)
//    @Query(value = "update t_book set author=:author where book_name=:name",nativeQuery = true)
//    @Modifying
//    Integer updateBookByName(@Param("author") String author, @Param("name") String name);
}
