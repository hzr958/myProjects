package com.smate.center.batch.service.rol.pub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.cnipr.CniprInsName;
import com.smate.center.batch.model.sns.pub.InstitutionAdd;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pdwh.pubmatch.CniprInsNameMatchService;
import com.smate.center.batch.service.pub.InstitutionAddManager;
import com.smate.center.batch.util.pub.ImportCniprAuthorMergeUtils;
import com.smate.core.base.utils.string.IrisNumberUtils;

/**
 * 重构导入CNIPR成果XML作者.
 * 
 * @author liqinghua
 * 
 */
@Service("importCniprXmlRebuildAuthService")
@Transactional(rollbackFor = Exception.class)
public class ImportCniprXmlRebuildAuthServiceImpl implements ImportCniprXmlRebuildAuthService {

  /**
   * 
   */
  private static final long serialVersionUID = -4563438269253577393L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private CniprInsNameMatchService cniprInsNameMatchService;
  @Autowired
  private InstitutionAddManager institutionManager;

  @SuppressWarnings("rawtypes")
  @Override
  public PubXmlDocument rebuildAuthXml(PubXmlDocument doc, Long insId) throws ServiceException {

    doc = ImportCniprAuthorMergeUtils.megergeImportAuthor(doc);
    List authors = doc.getPubAuthorList();
    if (authors != null) {
      for (int i = 0; i < authors.size(); i++) {
        Map<String, CniprInsName> matchedResult = new HashMap<String, CniprInsName>();
        Element author = (Element) authors.get(i);

        // 导入的时候，可以指定人员ID，特例处理
        Long psnId = IrisNumberUtils.createLong(author.attributeValue("psn_id"));
        if (psnId != null) {
          // 单位必须存在
          InstitutionAdd insRol = institutionManager.getInstitution(insId);
          if (insRol != null) {
            // 匹配上的机构ID
            author.addAttribute("matched_ins_id", String.valueOf(insId));
            author.addAttribute("ins_id", String.valueOf(insId));
            String insName = StringUtils.isBlank(insRol.getZhName()) ? insRol.getEnName() : insRol.getZhName();
            author.addAttribute("ins_name", insName);
            continue;
          }
        }

        String pubAddr = author.attributeValue("addr");
        if (StringUtils.isNotBlank(pubAddr)) {
          String lpubAddr = pubAddr.toLowerCase();
          // 比较过的不需要重复比较
          CniprInsName matchCniprName = matchedResult.get(lpubAddr);
          if (matchCniprName == null) {
            matchCniprName = cniprInsNameMatchService.cniprNameMatchAll(lpubAddr, insId);
          }
          if (matchCniprName != null) {
            // 单位必须存在
            InstitutionAdd insRol = institutionManager.getInstitution(matchCniprName.getInsId());
            if (insRol != null) {
              author.addAttribute("ins_id", String.valueOf(matchCniprName.getInsId()));
              // 匹配上的机构ID
              author.addAttribute("matched_ins_id", String.valueOf(matchCniprName.getInsId()));
              String insName = StringUtils.isBlank(insRol.getZhName()) ? insRol.getEnName() : insRol.getZhName();
              author.addAttribute("ins_name", insName);
            }
          }
          matchedResult.put(lpubAddr, matchCniprName);
        }
      }
    }
    return doc;
  }

}
