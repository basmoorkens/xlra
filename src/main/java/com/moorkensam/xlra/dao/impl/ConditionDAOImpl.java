package com.moorkensam.xlra.dao.impl;

import com.moorkensam.xlra.dao.BaseDAO;
import com.moorkensam.xlra.dao.ConditionDAO;
import com.moorkensam.xlra.model.rate.Condition;

public class ConditionDAOImpl extends BaseDAO implements ConditionDAO {

	@Override
	public Condition updateCondition(Condition condition) {
		return getEntityManager().merge(condition);
	}

}
