package com.smate.batchtask.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.batchtask.test.model.TestJob1Info;
import com.smate.batchtask.test.model.dao.TestJob1InfoDao;
import com.smate.core.base.utils.exception.BatchTaskException;

@Service("testJob1Service")
@Transactional(rollbackFor = Exception.class)
public class TestJob1ServiceImpl implements TestJob1Service {

	@Autowired
	TestJob1InfoDao testJob1InfoDao;
	
	@Override
	public void saveTaskMessage(TestJob1Info testJob1Info)
			throws BatchTaskException {
		
		testJob1InfoDao.save(testJob1Info);
		
	}
	
	
}