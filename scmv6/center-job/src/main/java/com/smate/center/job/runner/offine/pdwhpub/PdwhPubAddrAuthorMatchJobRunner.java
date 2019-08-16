package com.smate.center.job.runner.offine.pdwhpub;

import com.smate.center.job.business.pdwhpub.model.PdwhMatchTaskRecord;
import com.smate.center.job.business.pub.service.PdwhPublicationService;
import com.smate.center.job.framework.exception.PrecheckException;
import com.smate.center.job.framework.runner.BaseOfflineJobRunner;
import com.smate.core.base.utils.pubxml.ImportPubXmlDocument;
import org.apache.commons.lang.StringUtils;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("PdwhPubAddrAuthorMatchJobRunner")
public class PdwhPubAddrAuthorMatchJobRunner extends BaseOfflineJobRunner {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private PdwhPublicationService pdwhPublicationService;

	@Override
	protected void process(Object entity, JobDataMap jobDataMap) throws JobExecutionException {
		PdwhMatchTaskRecord taskRecord = (PdwhMatchTaskRecord) entity;
		Long pubId = taskRecord.getPdwhPubId();
		ImportPubXmlDocument xmlDocument = pdwhPublicationService.getPubXmlDocById(pubId);
		if (xmlDocument==null){
			pdwhPublicationService.updatePubMatchStatus(pubId, 1, "xml为空");// 都匹配上
			return;
		}
		try {
			pdwhPublicationService.matchPubAddr(pubId,xmlDocument);
		} catch (Exception e) {
			logger.error("基准库成果地址匹配任务出错，pubid:" + pubId, e);
			pdwhPublicationService.updatePubMatchStatus(pubId, 2,
					"地址匹配出错," + e.getCause() == null ? "" : StringUtils.substring(e.getCause().toString(), 0, 120));// 地址匹配出错
		}
		try {
			pdwhPublicationService.matchPubAuthor(pubId,xmlDocument);
		} catch (Exception e) {
			logger.error("基准库成果作者匹配任务出错，pubid:" + pubId, e);
			pdwhPublicationService.updatePubMatchStatus(pubId, 3,
					"作者名匹配出错" + e.getCause() == null ? "" : StringUtils.substring(e.getCause().toString(), 0, 120));// 地址匹配出错
		}
		pdwhPublicationService.updatePubMatchStatus(pubId, 1, "");// 都匹配上*/

	}

	@Override
	public void preCheck(JobDataMap jobDataMap) throws PrecheckException {

	}

	@Override
	protected Class<?> getPersistentClass() {
		return PdwhMatchTaskRecord.class;
	}

}
