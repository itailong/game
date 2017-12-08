package com.starland.xyqp.lobby.dao;

import java.util.List;

import com.starland.xyqp.lobby.domain.Notice;

public interface NoticeDao {

	List<Notice> list();
	
	Notice get(Integer id);
	
	void add(Notice notice);
	
	void delete(Integer id);
	
	void update (Notice notice);
	
}
