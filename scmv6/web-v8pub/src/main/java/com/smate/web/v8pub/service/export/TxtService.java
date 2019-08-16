package com.smate.web.v8pub.service.export;

import java.io.IOException;
import java.util.List;

import com.smate.core.base.pub.vo.PubInfo;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.vo.exportfile.ExportPubTemp;

import freemarker.template.TemplateException;

/**
 * 导出txt文档服务
 * 
 * @author yhx
 *
 */
public interface TxtService {

  public String exportPubTxt(List<PubInfo> pubInfoList) throws Exception;

  List<PubInfo> getInfoList(List<Long> pubIds) throws ServiceException;

  /**
   * 导出成果EndNote.
   * 
   * @param pubXmlList
   * @return
   * @throws ServiceException
   */
  public String exportPubEndnoteTxt(List<ExportPubTemp> publications)
      throws ServiceException, IOException, TemplateException;
}
