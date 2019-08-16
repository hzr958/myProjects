package com.smate.batchtask.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.batchtask.test.model.TestJob5Info;
import com.smate.batchtask.test.model.dao.TestJob5InfoDao;
import com.smate.core.base.utils.exception.BatchTaskException;

@Service("testJob5Service")
@Transactional(rollbackFor = Exception.class)
public class TestJob5ServiceImpl implements TestJob5Service {

	@Autowired
	TestJob5InfoDao testJob5InfoDao;
	
	@Override
	public void saveTaskMessage(TestJob5Info testJob5Info)
			throws BatchTaskException {
		
		testJob5InfoDao.save(testJob5Info);
		
	}
	
	
}