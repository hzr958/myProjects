package com.smate.web.psn.service.representprj;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.project.model.Project;
import com.smate.core.base.project.model.ProjectStatistics;
import com.smate.core.base.project.vo.ProjectInfo;
import com.smate.core.base.psn.consts.PsnCnfConst;
import com.smate.core.base.psn.dao.psncnf.PsnConfigDao;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.prj.xml.PrjXmlConstants;
import com.smate.web.prj.xml.PrjXmlDocument;
import com.smate.web.psn.dao.project.ProjectDao;
import com.smate.web.psn.dao.project.ProjectStatisticsDao;
import com.smate.web.psn.dao.project.ScmPrjXmlDao;
import com.smate.web.psn.dao.representprj.RepresentPrjDao;
import com.smate.web.psn.dao.statistics.DynamicAwardPsnDao;
import com.smate.web.psn.dao.statistics.DynamicAwardResDao;
import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.model.homepage.PersonProfileForm;
import com.smate.web.psn.model.project.ScmPrjXml;
import com.smate.web.psn.model.representprj.RepresentPrj;
import com.smate.web.psn.model.statistics.DynamicAwardPsn;
import com.smate.web.psn.model.statistics.DynamicAwardRes;

/**
 * 人员代表性项目服务
 *
 * @author wsn
 * @createTime 2017年3月14日 下午8:31:46
 *
 */

@Service("representPrjService")
@Transactional(rollbackFor = Exception.class)
public class RepresentPrjServiceImpl implements RepresentPrjService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private RepresentPrjDao representPrjDao;
  @Autowired
  private ProjectDao projectDao;
  @Autowired
  private ScmPrjXmlDao scmPrjXmlDao;
  @Autowired
  private PsnConfigDao psnConfigDao;
  @Autowired
  private ProjectStatisticsDao projectStatisticsDao;
  @Autowired
  private DynamicAwardResDao dynamicAwardResDao;
  @Autowired
  private DynamicAwardPsnDao dynamicAwardPsnDao;

  @Override
  public List<Project> findPsnRepresentPrjInfoList(Long psnId, Integer status, boolean isMyself) {
    List<Project> prjList = new ArrayList<Project>();
    List<Project> sortList = new ArrayList<Project>();
    try {
      Long cnfId = psnConfigDao.getPsnConfId(psnId);
      // 查询代表性成果ID
      List<Long> prjIds = representPrjDao.findPsnRepresentPrjIds(psnId, status, cnfId,
          PsnCnfConst.ALLOWS_FRIEND + PsnCnfConst.ALLOWS_SELF);
      // 查看哪些ID是权限是本人下载,只有其他人查看的时候才会调用
      List<Long> fulltextPrjIds = new ArrayList<Long>();
      if (!isMyself && CollectionUtils.isNotEmpty(prjIds)) {
        fulltextPrjIds = getPermissions(prjIds);
      }
      String prjIdStr = "";
      if (prjIds != null && prjIds.size() > 0) {
        // 根据ID获取成果详细信息
        for (Long prjId : prjIds) {
          prjIdStr += "," + prjId;
        }
        // 去掉第一个，号
        prjIdStr = prjIdStr.replaceFirst(",", "");
        prjList = projectDao.getProjectListByPrjIds(prjIdStr);
        // 标示项目有权限设置为仅本人或者好友下载
        if (prjList != null && fulltextPrjIds != null && isMyself != true) {
          for (Project prj : prjList) {
            for (int i = 0; i < fulltextPrjIds.size(); i++) {
              if (fulltextPrjIds.get(i).equals(prj.getId())) {
                prj.setDownUploadFulltext(false);
              }
            }
          }
        }

      }
      if (prjIds != null && prjList.size() > 0) {
        for (Long prjId : prjIds) {
          for (Project prj : prjList) {
            if (prjId.equals(prj.getId())) {
              sortList.add(prj);
            }
          }
        }
      }
    } catch (Exception e) {
      logger.error("查询人员代表性项目出错， psnId= " + psnId, e);
    }
    return sortList;
  }

  @Override
  public List<Project> findPsnOpenPrjList(Long psnId, Long cnfId, Page page, Integer anyUser, String queryString) {
    return projectDao.findPsnOpenPrjList(psnId, cnfId, anyUser, page, queryString);
  }

  @Override
  public PersonProfileForm findPsnOpenPrjListByForm(PersonProfileForm form) {
    form = projectDao.queryPsnOpenPrjList(form);
    List<Long> representPrjIds =
        representPrjDao.findPsnRepresentPrjIds(form.getPsnId(), 0, form.getCnfId(), form.getAnyUser());
    if (representPrjIds != null && representPrjIds.size() > 0) {
      for (Long id : representPrjIds) {
        for (Project openPrj : form.getOpenPrjList()) {
          if (id.longValue() == openPrj.getId().longValue()) {
            openPrj.setIsRepresentPrj(1);
          }
        }
      }
    }
    if (CollectionUtils.isNotEmpty(form.getOpenPrjList())) {
      for (Project openPrj : form.getOpenPrjList()) {
        this.buildPrjInfoByLanguage(openPrj);
      }
    }
    return form;
  }

  @Override
  public List<Project> savePsnRepresentPrj(Long psnId, String encodePrjIds) {
    List<Project> prjList = new ArrayList<Project>();
    try {
      // 先将人员所有的代表性项目置为无效
      representPrjDao.updatePsnRepresentPrjStatus(psnId, 1);
      // 新增或更新人员代表性项目
      if (StringUtils.isNotBlank(encodePrjIds)) {
        String[] prjIdArr = encodePrjIds.split(",");
        if (prjIdArr != null && prjIdArr.length > 0) {
          int seqNo = 0;
          for (String des3PrjId : prjIdArr) {
            // 记得加密pubId
            // Long pubId =
            // NumberUtils.toLong(ServiceUtil.decodeFromDes3(des3PubId));
            Long prjId = NumberUtils.toLong(des3PrjId);
            prjId = prjId != 0 ? prjId : NumberUtils.toLong(ServiceUtil.decodeFromDes3(des3PrjId));
            if (prjId != 0) {
              // 只能操作自己的项目
              boolean isOwner = projectDao.isOwnerOfProject(psnId, prjId);
              if (isOwner) {
                RepresentPrj repPrj = representPrjDao.findPsnRepresentPrj(psnId, prjId);
                if (repPrj == null) {
                  repPrj = new RepresentPrj();
                }
                repPrj.setRepPsnId(psnId);
                repPrj.setRepPrjId(prjId);
                repPrj.setStatus(0);
                repPrj.setSeqNo(seqNo++);
                representPrjDao.save(repPrj);
              }
            }
          }
        }
      }
    } catch (Exception e) {
      logger.error("保存人员代表性项目出错， psnId = " + psnId + ", encodePrjIds = " + encodePrjIds, e);
    }
    return prjList;
  }

  /**
   * 构建代表性项目信息
   * 
   * @param form
   * @return
   */
  @Override
  public PersonProfileForm buildPsnRepresentPrjInfo(PersonProfileForm form) {
    form.setRepresentPrjList(this.findPsnRepresentPrjInfoList(form.getPsnId(), 0, form.getIsMySelf()));
    if (form.getRepresentPrjList() != null) {
      for (Project prj : form.getRepresentPrjList()) {
        this.buildProjectFullTextImg(prj);
        prj.setDes3Id(ServiceUtil.encodeToDes3(prj.getId().toString()));
        this.buildPrjInfoByLanguage(prj);
      }
    }
    return form;
  }

  /**
   * 构建项目全文图片路径
   * 
   * @param prj
   * @return
   */
  private Project buildProjectFullTextImg(Project prj) {
    // String domain = domainscm + "/";
    String fullTextImageUrl = "";
    if (StringUtils.isNotBlank(prj.getFulltextFileId())) {
      fullTextImageUrl = "/resscmwebsns/images_v5/images2016/file_img1.jpg";
    } else { // 没有全文用默认的
      fullTextImageUrl = "/resscmwebsns/images_v5/images2016/file_img.jpg";
    }
    prj.setFullTextImgPath(fullTextImageUrl);
    return prj;
  }

  public List<Long> getPermissions(List<Long> prjIds) throws Exception {

    List<ScmPrjXml> scmPrjXmls = scmPrjXmlDao.getBatchScmPrjXml(prjIds);
    List<Long> fulltextPrjIds = new ArrayList<Long>();
    if (CollectionUtils.isNotEmpty(scmPrjXmls)) {
      for (ScmPrjXml scmPrjXml : scmPrjXmls) {
        PrjXmlDocument xmlDocument = new PrjXmlDocument(scmPrjXml.getPrjXml());
        if (!xmlDocument.existsNode(PrjXmlConstants.PRJ_FULLTEXT_XPATH)) {
          continue;
        }
        String permission = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PRJ_FULLTEXT_XPATH, "permission");
        if ("2".equals(permission) || "1".equals(permission)) {
          fulltextPrjIds.add(scmPrjXml.getPrjId());
        }
      }
    }
    return fulltextPrjIds;
  }

  /**
   * 构建ProjectInfo对象
   * 
   * @param prj
   */
  private void buildPrjInfoByLanguage(Project prj) {
    Long currentPsnId = SecurityUtils.getCurrentUserId();
    ProjectInfo prjInfo = new ProjectInfo();
    prjInfo = prjInfo.buildProjectInfo(prj, prjInfo);
    if ("en_US".equals(LocaleContextHolder.getLocale().toString())) {
      prjInfo.setShowAuthorNames(
          StringUtils.isNotBlank(prjInfo.getAuthorNamesEn()) ? prjInfo.getAuthorNamesEn() : prjInfo.getAuthorNames());
      prjInfo.setShowTitle(StringUtils.isNotBlank(prjInfo.getEnTitle()) ? prjInfo.getEnTitle() : prjInfo.getZhTitle());
      prjInfo.setShowBriefDesc(
          StringUtils.isNotBlank(prjInfo.getBriefDescEn()) ? prjInfo.getBriefDescEn() : prjInfo.getBriefDesc());
    } else {
      prjInfo.setShowAuthorNames(
          StringUtils.isNotBlank(prjInfo.getAuthorNames()) ? prjInfo.getAuthorNames() : prjInfo.getAuthorNamesEn());
      prjInfo.setShowTitle(StringUtils.isNotBlank(prjInfo.getZhTitle()) ? prjInfo.getZhTitle() : prjInfo.getEnTitle());
      prjInfo.setShowBriefDesc(
          StringUtils.isNotBlank(prjInfo.getBriefDesc()) ? prjInfo.getBriefDesc() : prjInfo.getBriefDescEn());
    }
    ProjectStatistics projectStatistics = projectStatisticsDao.get(prj.getId());// TODO
    if (projectStatistics != null) {
      prjInfo.setAwardCount(projectStatistics.getAwardCount());
      prjInfo.setCommentCount(projectStatistics.getCommentCount());
      prjInfo.setShareCount(projectStatistics.getShareCount());
    }
    // 是否赞过
    DynamicAwardRes dynamicAwardRes = dynamicAwardResDao.getDynamicAwardRes(prj.getId(), 4, 1);
    prjInfo.setAward(0);
    if (dynamicAwardRes != null && currentPsnId != null) {
      DynamicAwardPsn dynamicAwardPsn =
          dynamicAwardPsnDao.getDynamicAwardPsn(currentPsnId, dynamicAwardRes.getAwardId());
      if (dynamicAwardPsn != null) {
        prjInfo.setAward(dynamicAwardPsn.getStatus());
      }
    }
    prj.setPrjInfo(prjInfo);
    prj.setPrjInfo(prjInfo);
  }

  /**
   * 构建代表性项目信息
   * 
   * @param psnId 人员ID
   * @param isMyself 是否是本人
   * @return
   */
  @Override
  public List<Project> buildMobilePsnRepresentPrjInfo(Long psnId, boolean isMyself) throws PsnException {
    List<Project> representPrjList = new ArrayList<Project>();
    try {
      representPrjList = this.findPsnRepresentPrjInfoList(psnId, 0, isMyself);
      if (CollectionUtils.isNotEmpty(representPrjList)) {
        for (Project prj : representPrjList) {
          this.buildProjectFullTextImg(prj);
          prj.setDes3Id(ServiceUtil.encodeToDes3(prj.getId().toString()));
          this.buildPrjInfoByLanguage(prj);
        }
      }
    } catch (Exception e) {
      logger.error("构建移动端人员代表性项目信息出错， psnId = " + psnId, e);
      throw new PsnException(e);
    }
    return representPrjList;
  }

}
