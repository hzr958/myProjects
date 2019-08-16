package com.smate.batchtask.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.batchtask.test.model.TestJob4Info;
import com.smate.batchtask.test.model.dao.TestJob4InfoDao;
import com.smate.core.base.utils.exception.BatchTaskException;

@Service("testJob4Service")
@Transactional(rollbackFor = Exception.class)
public class TestJob4ServiceImpl implements TestJob4Service {

	@Autowired
	TestJob4InfoDao testJob4InfoDao;
	
	@Override
	public void saveTaskMessage(TestJob4Info testJob4Info)
			throws BatchTaskException {
		
		testJob4InfoDao.save(testJob4Info);
		
	}
	
	
}