package com.smate.batchtask.test.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.batchtask.test.model.TaskMessageList;
import com.smate.batchtask.test.model.dao.TaskMessageListDao;
import com.smate.core.base.utils.exception.BatchTaskException;

@Service("testService")
@Transactional(rollbackFor = Exception.class)
public class TestServiceImpl implements TestService {
	
	@Autowired
	TaskMessageListDao taskMessageListDao;
	
	@Override
	public TaskMessageList getTaskMessageList() throws BatchTaskException {
		 
		TaskMessageList result = taskMessageListDao.getOneMsg();
		
		if(result != null){
			TaskMessageList update = result;
			//状态置为3，已经处理
			update.setStatus(3);
			taskMessageListDao.save(update);
		}
		
		return result;
	}

	@Override
	public void updateStatus(Long msgId) throws BatchTaskException {
		
		taskMessageListDao.updateStatus(msgId);
	}

	@Override
	public List<TaskMessageList> getAllMsgStatus1() throws BatchTaskException {
		List<TaskMessageList> list = new ArrayList<TaskMessageList>();
		list = taskMessageListDao.getAllTaskWithStatus1();
		return list;
	}
	
	
	
}