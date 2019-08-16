package com.smate.batchtask.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.batchtask.test.model.TestJob3Info;
import com.smate.batchtask.test.model.dao.TestJob3InfoDao;
import com.smate.core.base.utils.exception.BatchTaskException;

@Service("testJob3Service")
@Transactional(rollbackFor = Exception.class)
public class TestJob3ServiceImpl implements TestJob3Service {

	@Autowired
	TestJob3InfoDao testJob3InfoDao;
	
	@Override
	public void saveTaskMessage(TestJob3Info testJob3Info)
			throws BatchTaskException {
		
		testJob3InfoDao.save(testJob3Info);
		
	}
	
	
}