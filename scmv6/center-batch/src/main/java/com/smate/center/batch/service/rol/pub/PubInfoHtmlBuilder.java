package com.smate.center.batch.service.rol.pub;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.batch.constant.TemplateConstants;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PubHtml;

/**
 * 成果html构造
 * 
 * @author zk
 * 
 */
@Service("pubInfoHtmlBuilder")
public class PubInfoHtmlBuilder implements IHtmlBuilder {

  /**
   * 
   */
  private static final long serialVersionUID = 6251456067979948727L;

  private List<FtlBuilder> iFtlBuilderList;

  @Autowired
  private PubHtmlService pubHtmlService;

  @Override
  public void buildHtml(Object... params) throws ServiceException {
    for (FtlBuilder ftlBuilder : iFtlBuilderList) {
      Map<String, String> htmlMap = ftlBuilder.builderHtml(params);
      if (MapUtils.isNotEmpty(htmlMap)) {
        PubHtml pubHtml = new PubHtml();
        pubHtml.setPubId(Long.valueOf(htmlMap.get(TemplateConstants.OBJ_CODE)));
        pubHtml.setTempCode(Integer.valueOf(ObjectUtils.toString(htmlMap.get(TemplateConstants.TEMP_CODE))));
        pubHtml.setHtmlZh(ObjectUtils.toString(htmlMap.get(TemplateConstants.ZH_HTML)));
        pubHtml.setHtmlEn(ObjectUtils.toString(htmlMap.get(TemplateConstants.EN_HTML)));
        pubHtmlService.savePubHtml(pubHtml);
      }
    }

  }

  public List<FtlBuilder> getiFtlBuilderList() {
    return iFtlBuilderList;
  }

  public void setiFtlBuilderList(List<FtlBuilder> iFtlBuilderList) {
    this.iFtlBuilderList = iFtlBuilderList;
  }

}
