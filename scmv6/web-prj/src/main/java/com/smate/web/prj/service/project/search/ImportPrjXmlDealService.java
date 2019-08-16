package com.smate.web.prj.service.project.search;

import com.smate.web.prj.exception.PrjException;
import com.smate.web.prj.form.PrjXmlProcessContext;
import com.smate.web.prj.xml.PrjXmlDocument;

/**
 * .检索导入项目xml处理服务接口
 * 
 * @author wsn
 * @date Dec 18, 2018
 */
public interface ImportPrjXmlDealService {

  /**
   * .检查参数
   * 
   * @return
   */
  public String checkParameter(PrjXmlProcessContext context) throws PrjException;

  /**
   * .处理xml
   * 
   * @return
   */
  public String dealWithXml(PrjXmlDocument xmlDocument, PrjXmlProcessContext context) throws PrjException;

}
