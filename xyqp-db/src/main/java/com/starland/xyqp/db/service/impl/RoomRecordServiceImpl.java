package com.starland.xyqp.db.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.starland.xyqp.db.dao.RoomRecordDao;
import com.starland.xyqp.db.domain.RoomRecord;
import com.starland.xyqp.db.service.RoomRecordService;

@Service
@Deprecated
public class RoomRecordServiceImpl implements RoomRecordService {

	@Resource
	private RoomRecordDao roomRecordDao;
	
	@Override
	public void add(RoomRecord roomRecord) {
		roomRecordDao.add(roomRecord);
	}

}
