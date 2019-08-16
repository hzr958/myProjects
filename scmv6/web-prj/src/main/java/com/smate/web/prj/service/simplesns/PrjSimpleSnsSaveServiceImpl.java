package com.smate.web.prj.service.simplesns;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.project.model.Project;
import com.smate.web.prj.dao.project.ScmPrjXmlDao;
import com.smate.web.prj.dao.project.SnsProjectQueryDao;
import com.smate.web.prj.form.ProjectForm;
import com.smate.web.prj.model.common.ScmPrjXml;
import com.smate.web.prj.xml.PrjXmlConstants;
import com.smate.web.prj.xml.PrjXmlDocument;

/**
 * 
 * 保存项目的上传全文
 * 
 * @author zk
 *
 */
@Service("prjSimpleSnsSaveService")
@Transactional(rollbackFor = Exception.class)
public class PrjSimpleSnsSaveServiceImpl implements PrjSimpleSnsSaveService {

  @Autowired
  private SnsProjectQueryDao snsProjectQueryDao;
  @Autowired
  private ScmPrjXmlDao scmPrjXmlDao;

  @Override
  public Map<String, String> uploadPubXmlFulltext(ProjectForm form) throws Exception {

    Map<String, String> map = new HashMap<String, String>();
    Project prj = snsProjectQueryDao.get(form.getId());
    if (prj == null) {
      map.put("fulltext", "error");
      return map;
    }
    Date updateTime = new Date();
    prj.setFulltextFileId(form.getFulltextField());
    prj.setFulltextExt(StringUtils.substring(form.getFulltextExt(), 0, 30));
    prj.setStatus(0);
    prj.setUpdateDate(updateTime);
    /*
     * String fulltextIcon = ArchiveFileUtil.getFileTypeIco("/resmod",form.getFulltextExt(),
     * LocaleContextHolder.getLocale()); map.put("fulltextIcon", StringUtils.trimToEmpty(fulltextIcon));
     */
    snsProjectQueryDao.save(prj);
    // 权限设置
    Integer permission = snsProjectQueryDao.findPsnPrjPrivacy(form.getId());
    if (permission != null) {
      if (permission == 7) {
        form.setPermission("0");
      } else {
        form.setPermission("2");
      }
    } else {
      form.setPermission("0");
    }
    map.put("result", "sucess");
    // 更新项目全文对应的xml数据
    updateFulltextXml(form);
    return map;
  }

  public void updateFulltextXml(ProjectForm form) throws Exception {
    ScmPrjXml scmPrjXml = scmPrjXmlDao.get(form.getId());
    PrjXmlDocument xmlDocument = new PrjXmlDocument(scmPrjXml.getPrjXml());
    if (!xmlDocument.existsNode(PrjXmlConstants.PRJ_FULLTEXT_XPATH)) {
      xmlDocument.createElement(PrjXmlConstants.PRJ_FULLTEXT_XPATH);
    }
    if (StringUtils.isNotBlank(form.getFileDesc())) {
      xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PRJ_FULLTEXT_XPATH, "file_desc", form.getFileDesc());
    }
    if (StringUtils.isNotBlank(form.getFileName())) {
      xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PRJ_FULLTEXT_XPATH, "file_name", form.getFileName());
    }
    xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PRJ_FULLTEXT_XPATH, "file_id", form.getFulltextField());
    Date update = new Date();
    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PRJ_FULLTEXT_XPATH, "upload_date", date);
    if (StringUtils.isNotBlank(form.getFulltextExt())) {
      xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PRJ_FULLTEXT_XPATH, "file_ext", form.getFulltextExt());
    }
    xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PRJ_FULLTEXT_XPATH, "permission", form.getPermission());
    scmPrjXml.setPrjXml(xmlDocument.getXmlString());
    scmPrjXmlDao.save(scmPrjXml);

  }

}
