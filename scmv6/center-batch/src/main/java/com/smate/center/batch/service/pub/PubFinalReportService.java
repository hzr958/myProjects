package com.smate.center.batch.service.pub;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.PublicationForm;

/**
 * 成果结题报告.
 * 
 * @author LY
 * 
 */
public interface PubFinalReportService {

  void syncPublicationToFinalReport(PublicationForm loadXml) throws ServiceException;

}
