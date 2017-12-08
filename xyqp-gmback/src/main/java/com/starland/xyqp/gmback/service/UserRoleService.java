package com.starland.xyqp.gmback.service;

import java.util.List;

import com.starland.xyqp.gmback.domain.UserRole;
import com.starland.tools.page.PageInfo;
import com.starland.xyqp.gmback.query.UserRoleQuery;
import com.starland.xyqp.gmback.vo.MultipleChoice;

public interface UserRoleService {

	List<UserRole> list(UserRoleQuery query, PageInfo pageInfo);
	
	UserRole get(Integer id);
	
	void add(UserRole userRole);
	
	void delete(Integer id);
	
	void update (UserRole userRole);

	MultipleChoice getMultipleChoice(Integer userId);

	void updateMultipleChoice(MultipleChoice multipleChoice);

}
