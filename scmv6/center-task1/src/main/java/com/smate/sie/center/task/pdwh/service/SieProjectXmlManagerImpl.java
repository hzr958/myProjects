package com.smate.sie.center.task.pdwh.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.sie.center.task.dao.PrjJsonPoDao;
import com.smate.sie.core.base.utils.json.prj.ProjectJsonDTO;
import com.smate.sie.core.base.utils.model.prj.ProjectJsonPo;

/**
 * 项目XML处理服务
 * 
 * @author yexingyuan
 */
@Transactional(rollbackFor = Exception.class)
@Service("sieProjectXmlManager")
public class SieProjectXmlManagerImpl implements SieProjectXmlManager {

  @SuppressWarnings("unused")
  private Logger logger = LoggerFactory.getLogger(getClass());
  /*
   * @Autowired private SiePrjXmlDao siePrjXmlDao;
   */
  @Autowired
  private PrjJsonPoDao prjJsonPoDao;
  @Autowired
  private SieProjectDupService sieProjectDupService;
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Value("${initOpen.restful.url}")
  private String SERVER_URL;

  /**
   * 查询项目是否在单位中重复，不重复的条件是存在xml数据，并且xml中的数据不重复。
   * 
   * @param prjId
   * @param InsId
   * @return true 不重复，false为重复
   */
  @Override
  public boolean getRepeatPrjStatus(Long prjId, Long InsId) throws SysServiceException, DocumentException {
    boolean flag = true;
    ProjectJsonPo po = prjJsonPoDao.get(prjId);
    if (po != null) {
      String prjJson = po.getPrjJson();
      if (StringUtils.isNotBlank(prjJson)) {
        ProjectJsonDTO prj = JacksonUtils.jsonObject(prjJson, ProjectJsonDTO.class);
        try {
          Map<Integer, List<Long>> dupMap = this.sieProjectDupService.getDupPrjByImportPrj(prj, InsId);
          if (dupMap.size() > 0) {
            flag = false;
          }
        } catch (Exception e) {
          logger.error("单位合并任务，项目查重异常，项目ID:" + prjId + ",单位Id:" + InsId);
          e.printStackTrace();
        }
      }
    }
    return flag;
  }

  /*
   * @Deprecated public boolean getRepeatPrjStatus(Long prjId, Long InsId) throws SysServiceException,
   * DocumentException { boolean flag = true; SiePrjXml siePrjXml = siePrjXmlDao.get(prjId); if
   * (siePrjXml != null) { String importXml = siePrjXml.getPrjXml(); Document doc =
   * DocumentHelper.parseText(importXml); Node pitem = doc.selectSingleNode("/data"); Element data =
   * (Element) pitem.selectSingleNode("project"); if (data != null) { data.addAttribute("zh_title",
   * data.attributeValue("ZH_TITLE")); data.addAttribute("prj_exter_no",
   * data.attributeValue("PRJ_EXTER_NO"));
   * 
   * Element dataMeta = (Element) pitem.selectSingleNode("prj_meta"); if (dataMeta != null) {
   * data.addAttribute("source_db_code", dataMeta.attributeValue("source_db_id")); }
   * 
   * Map<Integer, List<Long>> dupMap = this.sieProjectDupService.getDupPrjByImportPrj(data, InsId); if
   * (dupMap.size() > 0) { flag = false; } } } return flag; }
   */


}
