package com.smate.center.open.service.nsfc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.center.open.dao.nsfc.ReschPrjRptDao;
import com.smate.center.open.dao.nsfc.ReschPrjRptPubDao;
import com.smate.center.open.model.consts.ConstPubType;
import com.smate.center.open.model.nsfc.NsfcReschPrjRptPub;
import com.smate.center.open.model.nsfc.PubFulltext;
import com.smate.center.open.model.nsfc.ReschProjectReportPubModel;
import com.smate.center.open.model.nsfc.project.NsfcReschProjectReport;
import com.smate.center.open.model.publication.PubXmlDocument;
import com.smate.center.open.service.consts.ConstDefTypeName;
import com.smate.center.open.service.consts.ConstPubTypeService;
import com.smate.center.open.service.publication.PublicationXmlService;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.service.consts.SysDomainConst;
import com.smate.core.base.utils.string.IrisStringUtils;

/*
 * @Author zjh 获取研究成果信息
 */
/**
 * 
 * @author Administrator
 *
 */
@Service("pubResearchReportService")
@Transactional(rollbackFor = Exception.class)
public class PubResearchReportServiceImpl implements PubResearchReportService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private ReschPrjRptDao reschPrjRptDao;
  @Autowired
  private ReschPrjRptPubDao reschPrjRptPubDao;
  @Autowired
  private ConstPubTypeService constPubTypeService;
  @Autowired
  private PublicationXmlService publicationXmlService;
  @Autowired
  private SysDomainConst sysDomainConst;

  private boolean isOriginalVersion(Long rptId) throws Exception {

    try {
      NsfcReschProjectReport rpt = this.reschPrjRptDao.getProjectReport(rptId);

      return rpt.getVersionId().longValue() == 0;
    } catch (Exception e) {
      logger.error("调用isOriginalVersion异常！", e);
      throw new Exception(e);

    }

  }


  public void updateReschRptVersionId(ReschProjectReportPubModel form) throws Exception {

    Assert.notNull(form.getRptId(), "报告rptId不允许为空！");

    try {

      if (!form.isIgnore() && this.isOriginalVersion(form.getRptId())) {

        return;
      }

      NsfcReschProjectReport nsfcReschPrjRpt = this.reschPrjRptDao.getProjectReport(form.getRptId());
      nsfcReschPrjRpt.setVersionId(nsfcReschPrjRpt.getVersionId() + 1);
      this.reschPrjRptDao.saveProjectReport(nsfcReschPrjRpt);
    } catch (Exception e) {

      logger.error("修改版本号异常！", e);
      throw new Exception(e);
    }

  }

  public List<NsfcReschPrjRptPub> getProjectReportSubmitsAll(Long rptId) throws Exception {
    List<NsfcReschPrjRptPub> list = new ArrayList<NsfcReschPrjRptPub>();
    try {
      list = this.reschPrjRptPubDao.getProjectReportPubsByRptId(rptId, 0, 0);
      if (list.size() > 0) {
        for (NsfcReschPrjRptPub rptPub : list) {
          ConstPubType pubType = this.getConstPubTypeService().get(rptPub.getPubType());
          rptPub
              .setPubTypeName(StringUtils.isNotBlank(pubType.getZhName()) ? pubType.getZhName() : pubType.getEnName());
          rptPub.setDefTypeName(ConstDefTypeName.defTypeNameMap.get(rptPub.getDefType()));
        }
      } else {
        throw new Exception("获取到研究报告的成果为空" + "rptID=" + rptId);
      }
    } catch (Exception e) {
      logger.error(e.getMessage() + e);
    }
    return list;
  }

  private ConstPubTypeService getConstPubTypeService() throws Exception {

    return this.constPubTypeService;
  }

  // 根据年份和项目编号获取成果研究报告
  @Override
  public List<NsfcReschPrjRptPub> getProjectFinalPubs(Long nsfcPrjId, Long rptYear) throws Exception {
    try {
      if (nsfcPrjId == null) {
        throw new Exception("项目编号不能为空");
      }
      List<NsfcReschProjectReport> prjRpt = reschPrjRptDao.getPrjRptByNsfcPrjIdYear(nsfcPrjId, rptYear);
      if (CollectionUtils.isEmpty(prjRpt) || prjRpt.size() > 1) {
        throw new Exception("没有找到对应的结题报告nsfcPrjId:" + nsfcPrjId + " rptYear:" + rptYear);
      }
      Long rptId = prjRpt.get(0).getRptId();
      if (isOriginalVersion(rptId)) {// 判断是否为原始的项目
        ReschProjectReportPubModel form = new ReschProjectReportPubModel();
        form.setIgnore(true);
        form.setRptId(rptId);
        this.updateReschRptVersionId(form);
      }

      List<NsfcReschPrjRptPub> pubs = this.getProjectReportSubmitsAll(prjRpt.get(0).getRptId());
      return pubs;
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new Exception("查询结题报告成果失败", e);
    }
  }

  @Override
  public String genResultXml(List<NsfcReschPrjRptPub> pubs, String resultCode, Long nsfcPrjId, Long rptYear)
      throws Exception {
    String result = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><data><final_meta prjcode=\"" + nsfcPrjId
        + "\" rptyear=\"" + rptYear + "\" resultcode=\"" + resultCode + "\" /></data>";

    try {
      Document doc = DocumentHelper.parseText(result);
      Element root = doc.getRootElement();
      Element publications = root.addElement("publications");
      Long versionId = this.getReschRptVersionId(nsfcPrjId, rptYear);
      publications.addAttribute("versionid", versionId.toString());
      if (pubs != null) {

        publications.addAttribute("hastop5files", "" + this.validateTop5PubAttachment(nsfcPrjId, rptYear) + "");

      } else {

        publications.addAttribute("hastop5files", "true");
      }

      Integer seq = 1;
      for (NsfcReschPrjRptPub pub : pubs) {
        pub.setSeqNo(seq);
        Element publication = publications.addElement("publication");
        createPublication(publication, pub);
        seq++;
      }
      return doc.asXML();
    } catch (Exception e) {
      result = result.replace("resultcode=\"" + resultCode + "\"",
          "resultcode=\"exception occured when generate document \"");
      return result;
    }
  }


  @Override
  public Long getReschRptVersionId(Long nsfcPrjId, Long rptYear) throws Exception {
    try {
      if (nsfcPrjId == null || rptYear == null) {
        throw new ServiceException("项目编号和项目年度不能为空");
      }

      List<NsfcReschProjectReport> prjRpt = this.reschPrjRptDao.getPrjRptByNsfcPrjIdYear(nsfcPrjId, rptYear);
      if (CollectionUtils.isEmpty(prjRpt) || prjRpt.size() > 1) {
        throw new ServiceException("没有找到对应的结题报告nsfcPrjId:" + nsfcPrjId + " rptYear:" + rptYear);
      }

      return prjRpt.get(0).getVersionId();

    } catch (Exception e) {

      logger.error("获取成果研究报告versionId异常!", e);
      throw new Exception(e);
    }
  }


  @Override
  public boolean validateTop5PubAttachment(Long nsfcPrjId, Long rptYear) throws Exception {

    try {
      if (nsfcPrjId == null || rptYear == null) {
        throw new ServiceException("项目编号和项目年度不能为空");
      }

      List<NsfcReschProjectReport> prjRpt = reschPrjRptDao.getPrjRptByNsfcPrjIdYear(nsfcPrjId, rptYear);
      if (CollectionUtils.isEmpty(prjRpt) || prjRpt.size() > 1) {
        throw new ServiceException("没有找到对应的结题报告nsfcPrjId:" + nsfcPrjId + " rptYear:" + rptYear);
      }

      List<Long> list = this.reschPrjRptPubDao.getTop5ProjectReportPubsByRptId(prjRpt.get(0).getRptId());

      if (list != null) {

        boolean flag = true;
        for (Long pubId : list) {
          try {

            String xmlData = this.publicationXmlService.getById(pubId).getXmlData();

            PubXmlDocument doc = new PubXmlDocument(xmlData);
            PubFulltext fulltext = doc.getFulltext();
            Long fileId = fulltext.getFullTextFileId();

            if (!doc.existsNode(PubXmlConstants.PUB_ATTACHMENTS_ATTACHMENT_XPATH)
                && (fileId == null || fileId.longValue() == 0)) {

              flag = false;
              break;

            }

          } catch (Exception e) {
            logger.error("解析pub xml异常！", e);
            return false;
          }

        }

        return flag;

      }

    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new Exception("查询结题报告成果失败");
    }

    return false;
  }


  @SuppressWarnings("rawtypes")
  private void createPublication(Element publication, NsfcReschPrjRptPub pub) throws Exception {

    try {
      Map pros = BeanUtils.describe(pub);
      for (Iterator iterator = pros.keySet().iterator(); iterator.hasNext();) {
        String key = ObjectUtils.toString(iterator.next());
        if ("id".equals(key)) {
          publication.addAttribute("rptId", String.valueOf(pub.getId().getRptId()));
          publication.addAttribute("pubId", String.valueOf(pub.getId().getPubId()));
        } else if (!"class".equalsIgnoreCase(key) && !"pubCw".equalsIgnoreCase(key)
            && !"serialVersionUID".equals(key)) {
          String str = ObjectUtils.toString(pros.get(key));
          // 出去特殊字符
          str = IrisStringUtils.filterIllegalXmlChar(str);
          publication.addElement(key).addText(str);
        }
        // 添加成果域名
        String pubWebUrl = null;
        String domain = sysDomainConst.getSnsDomain();
        if (domain != null) {
          pubWebUrl = "https://" + domain + sysDomainConst.getSnsContext();
        }
        publication.addAttribute("pubWebUrl", pubWebUrl);
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new Exception(e.getMessage());
    }

  }
}
