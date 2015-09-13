package com.moorkensam.xlra.dao.impl;

import com.moorkensam.xlra.dao.BaseDAO;
import com.moorkensam.xlra.dao.ConditionTypeDAO;
import com.moorkensam.xlra.model.rate.Condition;

public class ConditionTypeDAOImpl extends BaseDAO implements ConditionTypeDAO {

	@Override
	public Condition updateCondition(Condition condition) {
		return getEntityManager().merge(condition);
	}

}
