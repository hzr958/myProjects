package com.smate.center.task.quartz;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.dao.sns.quartz.PubInfoTmpDao;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.exception.TaskException;
import com.smate.center.task.model.sns.pub.PubInfoTmp;
import com.smate.center.task.model.sns.quartz.Publication;
import com.smate.center.task.model.sns.quartz.ScmPubXml;
import com.smate.center.task.single.oldXml.pub.PubXmlDocument;
import com.smate.center.task.single.service.pub.PublicationService;
import com.smate.center.task.single.service.pub.ScmPubXmlService;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.pubHash.PubHashUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public class GetPubInfoTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PublicationService publicationService;
  @Autowired
  private ScmPubXmlService scmPubXmlService;
  @Autowired
  private PubInfoTmpDao pubInfoTmpDao;
  @Value("${domainscm}")
  private String domain;

  public GetPubInfoTask() {
    super();
  }

  public GetPubInfoTask(String beanName) {
    super(beanName);
  }

  public void doRun() throws SingleTaskException, BatchTaskException {

    if (!super.isAllowExecution()) {
      return;
    }
    try {
      super.closeOneTimeTask();
      List<Publication> pubIdList = publicationService.getPubByPsnId();
      for (Publication pub : pubIdList) {
        ScmPubXml pubXml = scmPubXmlService.getPubXml(pub.getId());
        String des3Id = ServiceUtil.encodeToDes3(pub.getId().toString());
        if (pubXml != null) {
          String xml = pubXml.getPubXml();
          if (StringUtils.isNotBlank(xml)) {
            try {
              PubXmlDocument xmlDocument = new PubXmlDocument(xml);
              String patentType = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_type");
              if (StringUtils.isNotBlank(patentType)) {
                if ("51".equals(patentType)) {
                  patentType = "发明专利";
                } else if ("52".equals(patentType)) {
                  patentType = "实用新型";
                } else if ("53".equals(patentType)) {
                  patentType = "外观专利";
                }
              }
              String patentOrg = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "patent_org");
              String patentPublishDate =
                  xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "publish_date");
              String startDate = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "start_date");
              String endDate = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_PATENT_XPATH, "end_date");
              String orignal = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "original");
              String startPage = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "start_page");
              String endPage = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "end_page");
              String volume = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "volume");
              String issue = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "issue");
              String publishDate = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "publish_date");
              String paperType = xmlDocument.getXmlNodeAttribute(PubXmlConstants.PUB_CONF_PAPER_XPATH, "paper_type");
              if (StringUtils.isNotBlank(paperType)) {
                if ("E".equals(paperType)) {
                  paperType = "分组报告";
                } else if ("A".equals(paperType)) {
                  paperType = "特邀报告";
                } else if ("C".equals(paperType)) {
                  paperType = "墙报展示";
                }
              }
              PubInfoTmp pubInfo = new PubInfoTmp(pub.getId(), pub.getPsnId(), patentType, patentPublishDate, startDate,
                  endDate, orignal, startPage, endPage, volume, issue, publishDate, paperType);
              Long zhTitleHash = PubHashUtils.cleanTitleHash(pub.getZhTitle());
              Long enTitleHash = PubHashUtils.cleanTitleHash(pub.getEnTitle());
              String pubUrl = domain + "/pubweb/details/show?des3Id=" + des3Id + "&currentDomain=/pubweb&pubFlag=1";
              String psnUrl =
                  domain + "/psnweb/homepage/show?des3PsnId=" + ServiceUtil.encodeToDes3(pub.getPsnId().toString());
              pubInfo.setZhTitleHash(zhTitleHash);
              pubInfo.setPsnUrl(psnUrl);
              pubInfo.setEnTitleHash(enTitleHash);
              pubInfo.setPubUrl(pubUrl);
              pubInfo.setPatentOrg(patentOrg);
              publicationService.savePubInfoTmp(pubInfo);
            } catch (DocumentException e) {
              logger.error("获取成果信息出错----pubId=" + pub.getId(), e);
            }
          }
        }

      }
    } catch (TaskException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
  }
}
