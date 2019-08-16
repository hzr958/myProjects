package com.smate.test.file;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.smate.core.base.pub.enums.PubDbEnum;
import com.smate.web.file.dao.PsnFileShareRecordQueryDao;
import com.smate.web.file.dao.fulltext.PubFullTextReqQueryDao;

/**
 *
 * @author houchuanjie
 * @date 2017年11月29日 下午2:46:45
 */
@Transactional(rollbackOn = Exception.class)
@TransactionConfiguration(transactionManager = "transactionManager-sns")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext.xml",
		"classpath:spring/applicationContext-sessionfactory-database-sns.xml",
		"classpath:spring/applicationContext-file-server.xml" })
public class SpringHibernateTest {
	@Autowired
	private PsnFileShareRecordQueryDao psnFileSRQDao;
	@Autowired
	private PubFullTextReqQueryDao pubFTReqQueryDao;

	@Test
	public void testPsnFileShareRecordDao() {
		boolean existIn = psnFileSRQDao.isExistIn(1000000731929L, 1100000032L);
		System.out.println("----------------------------------------");
		System.out.println(existIn);
		System.out.println("----------------------------------------");
	}

	@Test
	public void testPubFTReqQueryDao() {
		boolean fullTextReqAgree = pubFTReqQueryDao.isFullTextReqAgree(1000000006203L, 1000000020503L, PubDbEnum.SNS);
		System.out.println("----------------------------------------");
		System.out.println(fullTextReqAgree);
		System.out.println("----------------------------------------");
	}

}
