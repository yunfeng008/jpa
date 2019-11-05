package org.test.records.service;

import lombok.extern.slf4j.Slf4j;
import org.test.records.bean.User;
import org.test.records.dao.UserDao;
import org.test.records.util.CoreException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author dzp
 * @since 2019-10-23
 */
@Slf4j
@Service
@Transactional
public class UserService {

    @Autowired
    UserDao userDao;

    public List<User> findAll() throws Exception{
        try {
            return userDao.findAll();
        }catch (Exception e) {
            log.error("查找用户失败",e);
            throw new CoreException("查找用户失败");
        }
    }

    public User findUserByUserId(Integer userId) throws Exception{
        try {
            return userDao.findUserByUserId(userId);
        }catch (Exception e) {
            log.error("根据用户ID查找用户失败",e);
            throw new CoreException("根据用户ID查找用户失败");
        }
    }

    public User findUserByUserName(String userName) throws Exception{
        try {
            return userDao.findUserByUsername(userName);
        }catch (Exception e) {
            log.error("根据用户名称查找用户失败",e);
            throw new CoreException("根据用户名称查找用户失败");
        }
    }

    public User findUserByToken(String token) throws Exception{
        try {
            return userDao.findUserByToken(token);
        }catch (Exception e) {
            log.error("根据用户token查找用户失败",e);
            throw new CoreException("根据用户token查找用户失败");
        }
    }

    public Page<User> findUserByPage(Pageable pageable,String username) throws CoreException{
        return userDao.findAll((Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = null;
            if (username != null) {
                predicate = criteriaBuilder.equal(root.get("username"), username);
            }
            return predicate;
        }, pageable);
    }

    public void save(User user) throws CoreException{
        try {
            if (user == null) {
                throw new CoreException("插入用户信息为空");
            }
            userDao.save(user);
        }catch (CoreException e) {
            log.error("插入用户信息失败",e);
            throw new CoreException("插入用户信息失败");
        }
    }

    public void delete(Integer userId) throws CoreException{
        try {
            if (userId == null) {
                throw new CoreException("传入信息为空");
            }
            userDao.deleteById(userId);
        }catch (CoreException e) {
            log.error("删除用户信息失败",e);
            throw new CoreException("删除用户信息失败");
        }
    }
}
