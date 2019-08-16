package com.smate.web.prj.service.project.search;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.prj.consts.PrjXmlOperationEnum;
import com.smate.web.prj.dao.project.search.ConstRefDbDao;
import com.smate.web.prj.exception.PrjException;
import com.smate.web.prj.form.PrjXmlProcessContext;
import com.smate.web.prj.model.search.ConstRefDb;
import com.smate.web.prj.xml.PrjXmlConstants;
import com.smate.web.prj.xml.PrjXmlDocument;

/**
 * .处理导入项目XML中Meta节点服务
 * 
 * @author wsn
 * @date Dec 18, 2018
 */
public class ImportPrjMetaXmlServiceImpl implements ImportPrjXmlDealService {

  @Autowired
  private ConstRefDbDao constRefDbDao;

  @Override
  public String checkParameter(PrjXmlProcessContext context) throws PrjException {
    Assert.notNull(context.getCurrentUserId(), "currentUserId is null");
    Assert.notNull(context.getCurrentAction(), "current operation is null");
    return null;
  }

  @Override
  public String dealWithXml(PrjXmlDocument xmlDocument, PrjXmlProcessContext context) throws PrjException {
    // 获取meta节点
    Element meta = (Element) xmlDocument.getPrjMeta();
    if (meta == null) {
      meta = xmlDocument.createElement(PrjXmlConstants.PRJ_META_XPATH);
    }
    // 最后更新的人员、时间信息
    String userId = String.valueOf(context.getCurrentUserId());
    String nowStr = ServiceUtil.formateZhDateFull(new Date());
    meta.addAttribute("last_update_psn_id", userId);
    meta.addAttribute("last_update_date", nowStr);
    // prjId
    if (NumberUtils.isNotNullOrZero(context.getCurrentPrjId())) {
      meta.addAttribute("prj_id", context.getCurrentPrjId().toString());
    }
    // 导入
    if (PrjXmlOperationEnum.Import.equals(context.getCurrentAction())) {
      // 如source_db_code="ChinaJournal"
      String sourceDbCode = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PUBLICATION_XPATH, "source_db_code");
      if (StringUtils.isNotBlank(sourceDbCode)) {
        ConstRefDb sourceDb = constRefDbDao.getConstRefDbByCode(sourceDbCode);
        if (sourceDb != null) {
          meta.addAttribute("record_from", "1");
          meta.addAttribute("source_db_id", String.valueOf(sourceDb.getId()));
          meta.addAttribute("zh_source_db_name", sourceDb.getZhCnName());
          meta.addAttribute("en_source_db_name", sourceDb.getEnUsName());
        }
        /*
         * else { meta.addAttribute("source_db_id", "-1"); meta.addAttribute("record_from",
         * String.valueOf(ProjectRecordMode.ONLINE_IMPORT_FROM_FILE)); }
         */
        xmlDocument.removeAttribute(PrjXmlConstants.PUBLICATION_XPATH, "source_db_code");
      }
      meta.addAttribute("record_psn_id", userId);
      meta.addAttribute("create_psn_id", userId);
      meta.addAttribute("create_date", nowStr);
      meta.addAttribute("record_node_id", "1");
    }
    return null;
  }

}
