package com.smate.center.batch.service.rol.pub;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.smate.center.batch.dao.rol.pub.PrjPubFundDao;
import com.smate.center.batch.dao.rol.pub.PubFundInfoRolDao;
import com.smate.center.batch.dao.rol.pub.RolProjectDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PrjPubFund;
import com.smate.center.batch.model.rol.pub.PubFundInfoRol;
import com.smate.center.batch.model.rol.pub.PubFundPrjRefresh;
import com.smate.center.batch.model.sns.pub.PublicationXml;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pub.PublicationXmlService;
import com.smate.core.base.utils.common.HashUtils;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.string.IrisNumberUtils;

/**
 * 成果项目信息.
 * 
 * @author liqinghua
 * 
 */
@Service("pubFundInfoRolService")
@Transactional(rollbackFor = Exception.class)
public class PubFundInfoRolServiceImpl implements PubFundInfoRolService {

  /**
   * 
   */
  private static final long serialVersionUID = -5985104515739384204L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubFundInfoRolDao pubFundInfoDao;
  @Autowired
  private PublicationXmlService publicationXmlService;
  @Autowired
  private RolProjectDao rolProjectDao;
  @Autowired
  private PrjPubFundDao prjPubFundDao;

  @Override
  public void savePubFundInfo(Long pubId, Long insId, String fundInfo) throws ServiceException {

    try {
      // 不存在成果项目信息，直接删除.
      if (StringUtils.isBlank(fundInfo)) {
        this.removePubFundInfo(pubId);
        return;
      }
      // 获取hashcode
      Long fundHash = this.getPubFundInfoHash(fundInfo);
      if (fundHash == null || fundHash == 0) {
        this.removePubFundInfo(pubId);
        return;
      }
      // 判断是否修改项目信息，没修改，直接退出
      Long preFundHash = this.pubFundInfoDao.getFundHash(pubId);
      if (fundHash.equals(preFundHash)) {
        return;
      }
      List<Long> fundInfoNums = praseFundInfoNum(fundInfo);
      // 最多1000
      fundInfo = StringUtils.substring(fundInfo, 0, 1000);
      // 保存
      this.pubFundInfoDao.savePubFundInfo(pubId, insId, fundInfo, fundHash, fundInfoNums);
      // 刷新关联项目匹配
      this.pubFundInfoDao.addRefresh(pubId, insId);
    } catch (Exception e) {
      logger.error("保存成果项目信息", e);
      throw new ServiceException("保存成果项目信息", e);
    }
  }

  @Override
  public void removePubFundInfo(Long pubId) throws ServiceException {
    try {
      // 删除基本信息
      this.pubFundInfoDao.remove(pubId);
      // 删除关联到的项目
      this.prjPubFundDao.remvovePubFundPrj(pubId);
    } catch (Exception e) {
      logger.error("删除成果项目信息", e);
      throw new ServiceException("删除成果项目信息", e);
    }
  }

  /**
   * 解析成果项目信息里面超过5位连续数字信息.
   * 
   * @param fundInfo
   * @return
   */
  public static List<Long> praseFundInfoNum(String fundInfo) {

    List<Long> fundInfoNums = new ArrayList<Long>();
    Pattern p = Pattern.compile("\\d{5,18}");
    Matcher m = p.matcher(fundInfo);
    while (m.find()) {
      fundInfoNums.add(Long.valueOf(m.group()));
    }
    return fundInfoNums;
  }

  /**
   * 获取成果项目信息hashcode.
   * 
   * @param fundInfo
   * @return
   */
  private Long getPubFundInfoHash(String fundInfo) {

    if (StringUtils.isBlank(fundInfo)) {
      return null;
    } else {
      // html解码
      fundInfo = HtmlUtils.htmlUnescape(fundInfo);
      fundInfo = XmlUtil.trimAllHtml(fundInfo);
      fundInfo = XmlUtil.filterForCompare(fundInfo);
    }
    fundInfo = fundInfo.replaceAll("\\s+", "").trim().toLowerCase();
    if (StringUtils.isBlank(fundInfo)) {
      return null;
    }
    return HashUtils.getStrHashCode(fundInfo);
  }

  @Override
  public void prasePubFundInfo(Long pubId) throws Exception {
    PublicationXml pubXml = this.publicationXmlService.getById(pubId);
    String xml = pubXml.getXmlData();
    if (StringUtils.isBlank(xml)) {
      return;
    }
    try {
      PubXmlDocument pubXmlDoc = new PubXmlDocument(pubXml.getXmlData());
      // 项目信息为空，直接退出
      String fundInfo = pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "fundinfo");
      if (StringUtils.isBlank(fundInfo)) {
        return;
      }
      Long insId =
          IrisNumberUtils.createLong(pubXmlDoc.getXmlNodeAttribute(PubXmlConstants.PUB_META_XPATH, "record_ins_id"));
      this.savePubFundInfo(pubId, insId, fundInfo);
    } catch (Exception e) {
      logger.error("解析成果XML中的项目信息", e);
      throw new ServiceException("解析成果XML中的项目信息", e);
    }

  }

  @Override
  public List<PubFundPrjRefresh> loadRefreshList() throws ServiceException {
    try {
      return this.pubFundInfoDao.loadRefreshList();
    } catch (Exception e) {
      logger.error("获取需要刷新成果关联项目的成果列表", e);
      throw new ServiceException("获取需要刷新成果关联项目的成果列表", e);
    }
  }

  @Override
  public void markRefreshError(Long pubId) throws ServiceException {
    try {
      this.pubFundInfoDao.markRefreshError(pubId);
    } catch (Exception e) {
      logger.error("标注刷新成果关联项目失败", e);
      throw new ServiceException("标注刷新成果关联项目失败", e);
    }

  }

  @Override
  public void removeRefresh(Long pubId) throws ServiceException {
    try {
      this.pubFundInfoDao.removeRefresh(pubId);
    } catch (Exception e) {
      logger.error("删除刷新成果关联项目信息", e);
      throw new ServiceException("删除刷新成果关联项目信息", e);
    }
  }

  @Override
  public void refreshPubFundPrj(Long pubId, Long insId) throws ServiceException {

    PubFundInfoRol pubFund = this.pubFundInfoDao.get(pubId);
    // 不存在成果项目信息，删除之前关联的数据
    if (pubFund == null || StringUtils.isBlank(pubFund.getLfundInfo())) {
      this.prjPubFundDao.remvovePubFundPrj(pubId);
      return;
    }
    List<Long> prjIds = this.rolProjectDao.findPrjByPubFund(insId, pubFund.getLfundInfo());
    if (CollectionUtils.isEmpty(prjIds)) {
      this.prjPubFundDao.remvovePubFundPrj(pubId);
      return;
    }
    // 找出之前的成果项目关联信息，删除差异，并保留用户手动删除的关联（防止下次匹配上用户又要再删除一次）
    List<PrjPubFund> prePrjPubList = this.prjPubFundDao.loadPubPrj(pubId);
    OUT_LOOP: for (PrjPubFund prePrjPub : prePrjPubList) {
      Long prePrjId = prePrjPub.getPrjId();
      for (int i = 0; i < prjIds.size(); i++) {
        Long prjId = prjIds.get(i);
        // 找到，不需要创建新的关系
        if (prjId.equals(prePrjId)) {
          prjIds.remove(i);
          continue OUT_LOOP;
        }
      }
      // 未找到，删除之前的关系
      if (prePrjPub.getStatus() == 1) {
        prjPubFundDao.delete(prePrjPub.getId());
      }
    }

    // 逐个保存
    for (Long prjId : prjIds) {
      prjPubFundDao.savePrjPubFund(prjId, pubId, insId);
    }
  }

  public static void main(String[] args) {

    String a = "sfasf1234568fdf245554563sfdljlj45678s4054453640584089sfdfd34343f3438afd123456789123456789123456789000";

    Pattern p = Pattern.compile("\\d{5,18}");
    Matcher m = p.matcher(a);
    while (m.find()) {
      System.out.println(m.group());
    }
  }
}
