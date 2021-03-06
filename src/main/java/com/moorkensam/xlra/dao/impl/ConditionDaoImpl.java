package com.moorkensam.xlra.dao.impl;

import com.moorkensam.xlra.dao.BaseDao;
import com.moorkensam.xlra.dao.ConditionDao;
import com.moorkensam.xlra.model.rate.Condition;

public class ConditionDaoImpl extends BaseDao implements ConditionDao {

  @Override
  public Condition updateCondition(Condition condition) {
    return getEntityManager().merge(condition);
  }

}
