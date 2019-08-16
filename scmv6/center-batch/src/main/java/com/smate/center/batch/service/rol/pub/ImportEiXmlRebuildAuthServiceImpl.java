package com.smate.center.batch.service.rol.pub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.ei.EiInsName;
import com.smate.center.batch.model.sns.pub.InstitutionAdd;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pdwh.pubmatch.EiInsNameMatchService;
import com.smate.center.batch.service.pub.InstitutionAddManager;
import com.smate.center.batch.util.pub.ImportEiAuthorMergeUtils;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.string.IrisNumberUtils;

/**
 * 重构导入EI成果XML作者.
 * 
 * @author liqinghua
 * 
 */
@Service("importEiXmlRebuildAuthService")
@Transactional(rollbackFor = Exception.class)
public class ImportEiXmlRebuildAuthServiceImpl implements ImportEiXmlRebuildAuthService {

  /**
   * 
   */
  private static final long serialVersionUID = -4684285039942097223L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private EiInsNameMatchService eiInsNameMatchService;
  @Autowired
  private InstitutionAddManager institutionManager;

  @SuppressWarnings("rawtypes")
  @Override
  public PubXmlDocument rebuildAuthXml(PubXmlDocument doc, Long insId) throws ServiceException {

    doc = ImportEiAuthorMergeUtils.megergeImportAuthor(doc);
    List authors = doc.getPubAuthorList();
    if (authors != null) {
      for (int i = 0; i < authors.size(); i++) {
        Map<String, EiInsName> matchedResult = new HashMap<String, EiInsName>();
        Element author = (Element) authors.get(i);
        // 导入的时候，可以指定人员ID，特例
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
        if (StringUtils.isBlank(pubAddr)) {
          continue;
        }

        String lpubAddr = pubAddr.toLowerCase();
        EiInsName matchEiName = null;
        // 比较过的不需要重复比较
        if (!matchedResult.containsKey(lpubAddr)) {
          matchEiName = this.getAddrEiInsName(lpubAddr, insId);
        } else {
          matchEiName = matchedResult.get(lpubAddr);
        }
        if (matchEiName != null) {

          // 单位必须存在
          InstitutionAdd insRol = institutionManager.getInstitution(matchEiName.getInsId());
          if (insRol != null) {
            // 匹配上的机构ID
            author.addAttribute("matched_ins_id", String.valueOf(matchEiName.getInsId()));
            author.addAttribute("ins_id", String.valueOf(matchEiName.getInsId()));
            String insName = StringUtils.isBlank(insRol.getZhName()) ? insRol.getEnName() : insRol.getZhName();
            author.addAttribute("ins_name", insName);
          }
        } else {
          // 其他不确定的机构
          author.addAttribute("matched_ins_id", "1");
          author.addAttribute("ins_id", "1");
          author.addAttribute("ins_name", "");
        }
        matchedResult.put(lpubAddr, matchEiName);

      }
    }
    return doc;
  }

  /**
   * 获取作者地址匹配上的机构ID.
   * 
   * @param pubAddr
   * @param insId
   * @return
   * @throws ServiceException
   */
  public EiInsName getAddrEiInsName(String pubAddr, Long insId) throws ServiceException {
    if (StringUtils.isBlank(pubAddr)) {
      return null;
    }
    Set<String> orgsSet = XmlUtil.parseIsiPubAddrs(pubAddr);
    EiInsName findMatchInsName = null;
    for (String orgName : orgsSet) {
      EiInsName matchInsName = eiInsNameMatchService.eiNameMatchAll(orgName, insId);
      if (matchInsName == null) {
        continue;
        // 如果找的是其他单位可以继续尝试更多地址
      } else if (!insId.equals(matchInsName.getInsId())) {
        findMatchInsName = matchInsName;
      }
      return matchInsName;
    }
    findMatchInsName = eiInsNameMatchService.eiNameMatchOther(pubAddr, insId);

    return findMatchInsName;
  }
}
