package com.smate.web.v8pub.service.poi;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import com.smate.sie.core.base.utils.pub.exception.ServiceException;
import com.smate.web.v8pub.vo.exportfile.ExportPubTemp;

import freemarker.template.TemplateException;

/**
 * 导出RefWorks的txt文档.
 * 
 * @author WeiLong Peng
 * 
 */
public interface RefWorksTxtService extends Serializable {

  /**
   * 
   * @param publications
   * @return
   * @throws ServiceException
   * @throws IOException
   * @throws TemplateException
   */
  String exportPubRefworksTxt(List<ExportPubTemp> publications) throws ServiceException, IOException, TemplateException;
}
