/*
 * © 2016 Copyright Amadeus Unauthorised use and disclosure strictly forbidden.
 */
package com.ttanalyze.dao;

import com.ttanalyze.entity.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 * @author huseyin.kilic
 */
@Repository("profile")
public class ProfileDao extends BaseDao<User> {

  public User findByTwitterId(long twitterId) {
    Criteria criteria = ((Session) em.getDelegate()).createCriteria(User.class);
    return (User) criteria.add(Restrictions.eq("twitterId", twitterId))
            .uniqueResult();
  }

}
