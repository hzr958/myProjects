package com.smate.batchtask.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.batchtask.test.model.TestJob2Info;
import com.smate.batchtask.test.model.dao.TestJob2InfoDao;
import com.smate.core.base.utils.exception.BatchTaskException;

@Service("testJob2Service")
@Transactional(rollbackFor = Exception.class)
public class TestJob2ServiceImpl implements TestJob2Service {

	@Autowired
	TestJob2InfoDao testJob2InfoDao;
	
	@Override
	public void saveTaskMessage(TestJob2Info testJob2Info)
			throws BatchTaskException {
		
		testJob2InfoDao.save(testJob2Info);
		
	}
	
	
}