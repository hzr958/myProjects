package com.smate.center.batch.service.pub;

import java.util.Locale;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.PdwhPublicationAll;
import com.smate.center.batch.model.sns.pub.Publication;

/**
 * 对Publication进行包装类.<br/>
 * 如果有特殊 的要求如：html包装、xml包装、excel保证等请实现该类，而不要新增方法.
 * 
 * @author Ly
 * 
 */
public interface PublicationWrapService {

  void wrapPopulateDataItems(Publication item, boolean isFillErrorField, boolean isViewUploadFulltext,
      String des3ResSendId, String des3ResRecId);

  void wrapPopulateDataItems(Publication item, boolean isFillErrorField, Locale locale, boolean isViewUploadFulltext,
      String des3ResSendId, String des3ResRecId);

  String buildHtmlAbstract(Publication item, Locale locale, boolean isViewUploadFulltext, String des3ResSendId,
      String des3ResRecId);

  void wrapQueryResultTypeName(Publication pe) throws ServiceException;

  String buildHtmlAbstract(PdwhPublicationAll item) throws ServiceException;

}
