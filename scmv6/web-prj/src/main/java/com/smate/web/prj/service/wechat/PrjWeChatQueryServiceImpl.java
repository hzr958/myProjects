package com.smate.web.prj.service.wechat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.project.dao.ProjectDao;
import com.smate.core.base.project.model.Project;
import com.smate.core.base.project.model.ProjectStatistics;
import com.smate.core.base.psn.dao.psncnf.PsnConfigDao;
import com.smate.core.base.psn.dao.psncnf.PsnConfigPrjDao;
import com.smate.core.base.psn.model.psncnf.PsnConfig;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.fund.recommend.dao.DynamicAwardPsnDao;
import com.smate.web.fund.recommend.dao.DynamicAwardResDao;
import com.smate.web.prj.build.factor.PrjInfoBuildFactory;
import com.smate.web.prj.dao.project.ScmPrjXmlDao;
import com.smate.web.prj.dao.project.SnsProjectQueryDao;
import com.smate.web.prj.dao.wechat.ProjectStatisticsDao;
import com.smate.web.prj.dao.wechat.WechatProjectDao;
import com.smate.web.prj.enums.PrjInfoQueryEnum;
import com.smate.web.prj.exception.PrjException;
import com.smate.web.prj.form.wechat.PrjWeChatForm;
import com.smate.web.prj.model.common.DynamicAwardPsn;
import com.smate.web.prj.model.common.DynamicAwardRes;
import com.smate.web.prj.model.common.PrjInfo;
import com.smate.web.prj.model.common.ScmPrjXml;
import com.smate.web.prj.xml.PrjXmlConstants;
import com.smate.web.prj.xml.PrjXmlDocument;

/**
 * 项目-WeChat查询类
 * 
 * @author tj
 * @since 6.0.1
 *
 */
@Service("prjWeChatQueryService")
@Transactional(rollbackFor = Exception.class)
public class PrjWeChatQueryServiceImpl implements PrjWeChatQueryService {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private ScmPrjXmlDao scmPrjXmlDao;
  @Autowired
  private PrjInfoBuildFactory prjInfoBuildFactory;
  @Autowired
  private WechatProjectDao wechatProjectDao;
  @Autowired
  private ProjectStatisticsDao projectStatisticsDao;
  @Autowired
  private PsnConfigDao psnConfigDao;
  @Autowired
  private SnsProjectQueryDao snsProjectQueryDao;
  @Autowired
  private ProjectDao projectDao;
  @Autowired
  private PsnConfigPrjDao psnConfigPrjDao;
  @Autowired
  private DynamicAwardResDao dynamicAwardResDao;
  @Autowired
  private DynamicAwardPsnDao dynamicAwardPsnDao;


  @Override
  public void queryPrjForWeChat(PrjWeChatForm form) throws PrjException {
    if (form.getPsnId() != null) {
      if (form.getIsMyPrj() == false) {
        form.setCnfId(psnConfigDao.getPsnConfId(form.getPsnId()));
      }
      wechatProjectDao.queryPrjForWeChat(form);
      if (CollectionUtils.isNotEmpty(form.getPrjList())) {
        List<PrjInfo> prjInfoList = new ArrayList<PrjInfo>();
        for (Project prj : form.getPrjList()) {
          PrjInfo prjInfo = new PrjInfo();
          prjInfo.setPrj(prj);
          prjInfo.setProjectStatistics(projectStatisticsDao.get(prj.getId()));// TODO
          prjInfoBuildFactory.buildPubInfo(PrjInfoQueryEnum.WECHAT.toInt(), prjInfo);

          Long cnfId = psnConfigDao.getPsnConfId(form.getPsnId());
          String anyUser = String.valueOf(psnConfigPrjDao.getAnyUser(cnfId, prj.getId()));
          prjInfo.setAnyUser(anyUser);// 获取项目的隐私
          // 是否赞过
          DynamicAwardRes dynamicAwardRes = dynamicAwardResDao.getDynamicAwardRes(prj.getId(), 4, 1);
          prjInfo.setAward(0);
          if (dynamicAwardRes != null && form.getCurrentPsnId() != null) {
            DynamicAwardPsn dynamicAwardPsn =
                dynamicAwardPsnDao.getDynamicAwardPsn(form.getCurrentPsnId(), dynamicAwardRes.getAwardId());
            if (dynamicAwardPsn != null) {
              prjInfo.setAward(dynamicAwardPsn.getStatus());
            }
          }
          prjInfoList.add(prjInfo);
        }
        // form.setResultList(prjInfoList);
        form.getPage().setResult(prjInfoList);
      }
      /*
       * if (form.getFlag() == 0) { // 拿到标签和状态 wechatProjectDao.queryPrjFoldersWc(form);
       * wechatProjectDao.queryPrjStatusForWc(form); }
       */
    }
  }

  @Override
  public void queryPrjXml(PrjWeChatForm form) throws PrjException {
    form.setResultMap(new HashMap<String, Object>());
    String prjIdStr = Des3Utils.decodeFromDes3(form.getDes3PrjId());
    if (prjIdStr == null) {// 防止解密失败返回null，导致后面的强转操作出现错误
      form.getResultMap().put("result", "notExists");
      form.getResultMap().put("msg", "项目不存在");
      return;
    }
    Long prjId = Long.valueOf(prjIdStr);
    ScmPrjXml xml = scmPrjXmlDao.get(prjId);
    Project project = projectDao.get(prjId);
    if (xml != null && project.getStatus().intValue() == 0) {
      form.setPrjXml(xml.getPrjXml());
      ArrayList<Project> prjList = new ArrayList<Project>();
      prjList.add(project);
      form.setPrjList(prjList);
      Long ownerPsnId = wechatProjectDao.findPrjOwnerPsnId(prjId);
      if (ownerPsnId != null) {
        form.setDes3OwnerPsnId(Des3Utils.encodeToDes3(ownerPsnId.toString()));
        //
        if (SecurityUtils.getCurrentUserId().compareTo(ownerPsnId) == 0) {
          // 项目拥有者id与当前id一致，说明是本人访问
          form.setIsMyPrj(true);
        } else {
          // 非本人访问 判断是否有权限判断
          Integer privacy = snsProjectQueryDao.findPsnPrjPrivacy(prjId);
          if (privacy == null || privacy != 7) {
            form.getResultMap().put("result", "noPrivacy");
            form.getResultMap().put("msg", "没有权限查看项目！");
            return;
          }

        }
      }
    } else {
      form.getResultMap().put("result", "notExists");
      form.getResultMap().put("msg", "项目不存在");
      return;
    }
  }

  @Override
  public void appHandlePrjStatistics(PrjWeChatForm form) throws Exception {
    // 初始化默认数据
    initData(form);
    // 查找赞、分享、评论和查看统计数
    findPrjStatistice(form);
    // 查找自己的赞记录
    findAwardInfo(form);
    // 查找收藏状态
    findCollecteInfo(form);
  }

  private void findCollecteInfo(PrjWeChatForm form) {
    // TODO collectStatus
  }

  private void findAwardInfo(PrjWeChatForm form) {
    boolean award = snsProjectQueryDao.isAward(form.getPsnId(), form.getPrjId());
    form.getResultMap().put("awardStatus", award ? "true" : "false");
  }

  private void findPrjStatistice(PrjWeChatForm form) {
    ProjectStatistics statistics = projectStatisticsDao.get(form.getPrjId());
    if (statistics == null) {
      statistics = new ProjectStatistics(form.getPrjId(), 0, 0, 0, 0, 0, 0);
      projectStatisticsDao.save(statistics);
    }
    form.getResultMap().put("awardCount", statistics.getAwardCount());
    form.getResultMap().put("commentCount", statistics.getCommentCount());
    form.getResultMap().put("shareCount", statistics.getShareCount());

  }

  private void initData(PrjWeChatForm form) {
    form.getResultMap().put("awardCount", 0);
    form.getResultMap().put("commentCount", 0);
    form.getResultMap().put("shareCount", 0);
    form.getResultMap().put("awardStatus", "false");
    form.setPsnId(SecurityUtils.getCurrentUserId());
  }

  @Override
  public boolean hasPrivatePrj(Long psnId) throws Exception {
    boolean hasPrivatePrj = false;
    PsnConfig conf = psnConfigDao.getByPsn(psnId);
    if (conf != null) {
      hasPrivatePrj = psnConfigPrjDao.hasPrivatePrj(conf.getCnfId(), psnId);
    }
    return hasPrivatePrj;
  }

  @Override
  public void queryStaticAndcomment(PrjWeChatForm form) throws Exception {
    Long prjId = form.getPrjId();
    if (prjId == null || prjId == 0L) {
      return;
    }
    PrjInfo prjInfo = new PrjInfo();
    prjInfo.setPrjId(prjId);
    ProjectStatistics statics = projectStatisticsDao.get(prjId);
    if (statics != null) {
      prjInfo.setAwardCount(statics.getAwardCount());
      prjInfo.setCommentCount(statics.getCommentCount());
      prjInfo.setShareCount(statics.getShareCount());
    }
    // 是否赞过
    DynamicAwardRes dynamicAwardRes = dynamicAwardResDao.getDynamicAwardRes(form.getPrjId(), 4, 1);
    prjInfo.setAward(0);
    if (dynamicAwardRes != null && form.getCurrentPsnId() != null) {
      DynamicAwardPsn dynamicAwardPsn =
          dynamicAwardPsnDao.getDynamicAwardPsn(form.getCurrentPsnId(), dynamicAwardRes.getAwardId());
      if (dynamicAwardPsn != null) {
        prjInfo.setAward(dynamicAwardPsn.getStatus());
      }
    }
    prjInfo = buildBasePrjInfo(form, prjInfo);
    form.setPrjInfo(prjInfo);
  }


  /**
   * 构建详情页要显示的项目信息
   * 
   * @param prjXml
   * @param form
   * @throws DocumentException
   */
  protected PrjInfo buildBasePrjInfo(PrjWeChatForm form, PrjInfo info) throws PrjException {
    try {
      PrjXmlDocument xmlDocument = new PrjXmlDocument(form.getPrjXml());
      // 摘要
      String prjAbstract = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "zh_abstract");
      if (StringUtils.isBlank(prjAbstract)) {
        prjAbstract = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "en_abstract");
      }
      // 标题
      String prjTitle = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "zh_title");
      if (StringUtils.isBlank(prjTitle)) {
        prjTitle = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "en_title");
      }
      if (info == null) {
        info = new PrjInfo();
      }
      info.setPrjAbstract(XmlUtil.trimAllHtml(prjAbstract));
      info.setTitle(XmlUtil.trimAllHtml(prjTitle));
    } catch (Exception e) {
      throw new PrjException(e);
    }
    return info;
  }

  @Override
  public Long queryPrivatePrjCount(Long searchPsnId) throws ServiceException {
    // TODO Auto-generated method stub
    return wechatProjectDao.queryPrivatePrjCount(searchPsnId);
  }
}
