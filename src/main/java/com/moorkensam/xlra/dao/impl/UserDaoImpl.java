package com.moorkensam.xlra.dao.impl;

import com.moorkensam.xlra.dao.BaseDao;
import com.moorkensam.xlra.dao.UserDao;
import com.moorkensam.xlra.model.security.Role;
import com.moorkensam.xlra.model.security.User;
import com.moorkensam.xlra.model.security.UserStatus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

public class UserDaoImpl extends BaseDao implements UserDao {

  private static final Logger logger = LogManager.getLogger();

  @SuppressWarnings("unchecked")
  @Override
  public List<User> getAllUsers() {
    Query query = getEntityManager().createNamedQuery("User.findAll");
    List<User> users = (List<User>) query.getResultList();
    for (User user : users) {
      lazyLoadUser(user);
    }
    return users;
  }

  private void lazyLoadUser(User user) {
    user.getRoles().size();
    for (Role r : user.getRoles()) {
      r.getPermissions().size();
    }
  }

  @Override
  public void createUser(User user) {
    getEntityManager().persist(user);
  }

  @Override
  public User updateUser(User user) {
    return getEntityManager().merge(user);
  }

  @Override
  public User resetPassword(User user, String generatedPassword) {
    user.setPassword(generatedPassword);
    return getEntityManager().merge(user);
  }

  @Override
  public User getUserbyId(long id) {
    Query query = getEntityManager().createNamedQuery("User.findById");
    query.setParameter("id", id);
    User user = (User) query.getSingleResult();
    lazyLoadUser(user);
    return user;
  }

  @Override
  public User getUserByEmail(String email) {
    Query query = getEntityManager().createNamedQuery("User.findByEmail");
    query.setParameter("email", email);
    User user = (User) query.getSingleResult();
    lazyLoadUser(user);
    return user;
  }

  @Override
  public User getUserByUserName(String userName) {
    Query query = getEntityManager().createNamedQuery("User.findByUserName");
    query.setParameter("userName", userName);
    User user = (User) query.getSingleResult();
    lazyLoadUser(user);
    return user;
  }

  @Override
  public void deleteUser(User user) {
    getEntityManager().remove(user);
  }

  @Override
  public User isValidPasswordRequest(String email, String token) {
    Query query = buildPasswordVerificationQuery(email, token, UserStatus.FIRST_TIME_LOGIN);
    User user = null;
    try {
      user = (User) query.getSingleResult();
    } catch (NoResultException nre) {
      logger.info("Could not find user for " + email
          + " with specified token. Possible intrustion attempt!");
    }
    return user;
  }

  @Override
  public User isValidPasswordResetRequest(String email, String token) {
    Query query = buildPasswordVerificationQuery(email, token, UserStatus.PASSWORD_RESET);
    User user = null;
    try {
      user = (User) query.getSingleResult();
    } catch (NoResultException nre) {
      logger.info("Could not find user for " + email
          + " with specified token. Possible intrustion attempt!");
    }
    return user;
  }

  private Query buildPasswordVerificationQuery(String email, String token, UserStatus status) {
    Query query = getEntityManager().createNamedQuery("User.findByEmailAndToken");
    query.setParameter("email", email);
    query.setParameter("token", token);
    query.setParameter("userStatus", status);
    return query;
  }

}
