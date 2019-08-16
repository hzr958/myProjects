package com.smate.web.dyn.service.statistics;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.dyn.consts.DynamicConstants;
import com.smate.core.base.dyn.dao.DynamicMsgDao;
import com.smate.core.base.dyn.model.DynamicMsg;
import com.smate.core.base.project.dao.ProjectDao;
import com.smate.core.base.project.model.Project;
import com.smate.core.base.project.model.ProjectStatistics;
import com.smate.core.base.pub.enums.PubDbEnum;
import com.smate.core.base.pub.enums.PubPdwhStatusEnum;
import com.smate.core.base.utils.dynamicds.InspgDynamicUtil;
import com.smate.core.base.utils.exception.DynException;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.dyn.dao.dynamic.DynStatisticsDao;
import com.smate.web.dyn.dao.dynamic.DynamicSharePsnDao;
import com.smate.web.dyn.dao.dynamic.DynamicShareResDao;
import com.smate.web.dyn.dao.fund.AgencyStatisticsDao;
import com.smate.web.dyn.dao.fund.ConstFundAgencyDao;
import com.smate.web.dyn.dao.fund.ConstFundCategoryDao;
import com.smate.web.dyn.dao.fund.FundAgencyDao;
import com.smate.web.dyn.dao.fund.FundAgencyInterestDao;
import com.smate.web.dyn.dao.fund.FundStatisticsDao;
import com.smate.web.dyn.dao.fund.MyFundDao;
import com.smate.web.dyn.dao.news.NewsBaseDao;
import com.smate.web.dyn.dao.news.NewsStatisticsDao;
import com.smate.web.dyn.dao.pdwh.PdwhPubShareDao;
import com.smate.web.dyn.dao.pdwh.PdwhPubStatisticsDao;
import com.smate.web.dyn.dao.pdwhpub.PdwhPubFullTextDAO;
import com.smate.web.dyn.dao.pdwhpub.PdwhPubStatisticsDAO;
import com.smate.web.dyn.dao.pdwhpub.PubPdwhDAO;
import com.smate.web.dyn.dao.pub.CollectedPubDao;
import com.smate.web.dyn.dao.pub.ProjectStatisticsDao;
import com.smate.web.dyn.dao.pub.PubFullTextReqBaseDao;
import com.smate.web.dyn.dao.pub.PubSnsDAO;
import com.smate.web.dyn.dao.pub.PubSnsFullTextDAO;
import com.smate.web.dyn.dao.pub.PubStatisticsDAO;
import com.smate.web.dyn.form.dynamic.DynamicForm;
import com.smate.web.dyn.model.dynamic.DynStatistics;
import com.smate.web.dyn.model.dynamic.DynamicSharePsn;
import com.smate.web.dyn.model.dynamic.DynamicShareRes;
import com.smate.web.dyn.model.fund.AgencyStatistics;
import com.smate.web.dyn.model.fund.ConstFundAgency;
import com.smate.web.dyn.model.fund.ConstFundCategory;
import com.smate.web.dyn.model.fund.FundAgencyInterest;
import com.smate.web.dyn.model.fund.FundStatistics;
import com.smate.web.dyn.model.fund.MyFund;
import com.smate.web.dyn.model.news.NewsBase;
import com.smate.web.dyn.model.news.NewsStatistics;
import com.smate.web.dyn.model.pdwhpub.PdwhPubShare;
import com.smate.web.dyn.model.pdwhpub.PdwhPubStatistics;
import com.smate.web.dyn.model.pdwhpub.PdwhPubStatisticsPO;
import com.smate.web.dyn.model.pub.PdwhPubFullTextPO;
import com.smate.web.dyn.model.pub.PubFullTextPO;
import com.smate.web.dyn.model.pub.PubPdwhPO;
import com.smate.web.dyn.model.pub.PubSnsPO;
import com.smate.web.dyn.model.pub.PubSnsStatusEnum;
import com.smate.web.dyn.model.pub.PubStatisticsPO;
import com.smate.web.dyn.service.psn.PersonQueryservice;

/**
 * 初始化动态统计service接口实现
 * 
 * @author lhd
 *
 */
@Service("dynStatisticsService")
@Transactional(rollbackFor = Exception.class)
public class DynStatisticsServiceImpl implements DynStatisticsService {

  @Autowired
  private DynStatisticsDao dynStatisticsDao;
  @Autowired
  private DynamicMsgDao dynamicMsgDao;
  @Autowired
  private PdwhPubStatisticsDao pdwhPubStatisticsDao;
  @Autowired
  private PubStatisticsDAO pubStatisticsDAO;

  @Autowired
  private FundStatisticsDao fundStatisticsDao;
  @Autowired
  private MyFundDao myFundDao;
  @Autowired
  private ProjectStatisticsDao projectStatisticsDao;
  @Autowired
  private PersonQueryservice personQueryservic;
  @Autowired
  private DynamicShareResDao dynamicShareResDao;
  @Autowired
  private DynamicSharePsnDao dynamicSharePsnDao;
  @Autowired
  private PdwhPubShareDao pdwhPubShareDao;
  @Autowired
  private CollectedPubDao collectedPubDao;
  @Autowired
  private ConstFundAgencyDao constFundAgencyDao;
  @Autowired
  private ConstFundCategoryDao constFundCategoryDao;
  @Autowired
  private PdwhPubStatisticsDAO pdwhStatisticsDAO;
  @Autowired
  private PubSnsFullTextDAO pubSnsFullTextDAO;
  @Autowired
  private PubSnsDAO pubSnsDAO;
  @Autowired
  private PubPdwhDAO pubPdwhDAO;
  @Autowired
  private PdwhPubFullTextDAO pdwhPubFullTextDAO;
  @Autowired
  private AgencyStatisticsDao agencyStatisticsDao;
  @Autowired
  private FundAgencyInterestDao fundAgencyInterestDao;
  @Autowired
  private FundAgencyDao fundAgencyDao;// 机构dao
  @Autowired
  private ProjectDao projectDao;// 项目dao
  @Autowired
  private PubFullTextReqBaseDao pubFTReqBaseDao;
  @Autowired
  private NewsStatisticsDao newsStatisticsDao;
  @Autowired
  private NewsBaseDao newsBaseDao;

  /**
   * 获取初始化动态统计信息及发布时间
   */
  @Override
  public Map<String, Map<String, Object>> getDynStatistics(String dynStatisticsIds, Long psnId) throws DynException {
    Map<String, Map<String, Object>> dynTotalMap = new HashMap<String, Map<String, Object>>();
    if (StringUtils.isNotBlank(dynStatisticsIds)) {
      String[] des3DynIds = dynStatisticsIds.split(",");
      for (String des3DynId : des3DynIds) {
        Long dynId = Long.parseLong(Des3Utils.decodeFromDes3(des3DynId));
        DynamicMsg dynamicMsg = dynamicMsgDao.get(dynId);
        Map<String, Object> dynCountMap = new HashMap<String, Object>();
        if (dynamicMsg != null) {
          dynCountMap.put("awardCount", 0);
          dynCountMap.put("commentCount", 0);
          dynCountMap.put("shareCount", 0);
          dynCountMap.put("dynType", dynamicMsg.getDynType());
          if ("B2TEMP".equals(dynamicMsg.getDynType()) || "B3TEMP".equals(dynamicMsg.getDynType())) {
            switch (dynamicMsg.getResType()) {
              case DynamicConstants.RES_TYPE_PUB:// 成果
              case DynamicConstants.RES_TYPE_REF:// 文献
                // SCM-23563 个人库成果查看自己的数据
                PubStatisticsPO ps = pubStatisticsDAO.get(dynamicMsg.getResId());
                if (ps != null) {
                  dynCountMap.put("awardCount", ps.getAwardCount() != null ? ps.getAwardCount() : 0);
                  dynCountMap.put("commentCount", ps.getCommentCount() != null ? ps.getCommentCount() : 0);
                  dynCountMap.put("shareCount", ps.getShareCount() != null ? ps.getShareCount() : 0);
                }
                break;
              case DynamicConstants.RES_TYPE_FUND:// 基金
                FundStatistics sta = fundStatisticsDao.findFundStatisticsById(dynamicMsg.getResId());
                if (sta != null) {
                  dynCountMap.put("awardCount", sta.getAwardCount() != null ? sta.getAwardCount() : 0);
                  dynCountMap.put("shareCount", sta.getShareCount() != null ? sta.getShareCount() : 0);
                }
                break;
              case DynamicConstants.RES_TYPE_AGENCY:// 资助机构
                AgencyStatistics stat = agencyStatisticsDao.findAgencyStatistics(dynamicMsg.getResId());
                if (stat != null) {
                  dynCountMap.put("awardCount", stat.getAwardSum() != null ? stat.getAwardSum() : 0);
                  dynCountMap.put("shareCount", stat.getShareSum() != null ? stat.getShareSum() : 0);
                  dynCountMap.put("interestCount", stat.getInterestSum() != null ? stat.getInterestSum() : 0);
                }
                break;
              case DynamicConstants.RES_TYPE_JOURNALAWARD:// 推荐库
              case DynamicConstants.RES_TYPE_PUB_PDWH:
                buildPdwhStatistics(dynamicMsg.getResId(), dynCountMap);
                break;
              case DynamicConstants.RES_TYPE_PRJ:
                ProjectStatistics ds = projectStatisticsDao.get(dynamicMsg.getResId());
                if (ds != null) {
                  dynCountMap.put("awardCount", ds.getAwardCount());
                  dynCountMap.put("commentCount", ds.getCommentCount());
                  dynCountMap.put("shareCount", ds.getShareCount());
                }
                break;
              case DynamicConstants.RES_TYPE_NEWS:// 新闻
                NewsStatistics ns = newsStatisticsDao.get(dynamicMsg.getResId());
                if (ns != null) {
                  dynCountMap.put("awardCount", ns.getAwardCount() != null ? ns.getAwardCount() : 0);
                  dynCountMap.put("shareCount", ns.getShareCount() != null ? ns.getShareCount() : 0);
                  dynCountMap.put("viewCount", ns.getViewCount() != null ? ns.getViewCount() : 0);
                }
                break;
              default:// 其他resType
                // TODO
                break;
            }
          } else {// 动态
            DynStatistics ds = dynStatisticsDao.get(dynamicMsg.getSameFlag());
            if (ds != null) {
              dynCountMap.put("awardCount", ds.getAwardCount());
              dynCountMap.put("commentCount", ds.getCommentCount());
              dynCountMap.put("shareCount", ds.getShareCount());
            }
          }
          setResHasDelete(dynamicMsg, dynCountMap);// 判断动态资源是否被删除

          /* 根据语言显示时间 */
          String locale = LocaleContextHolder.getLocale().toString();
          if ("en_US".equals(locale)) {
            dynCountMap.put("publishDate", InspgDynamicUtil.formatDateUS(dynamicMsg.getCreateDate()));
          } else {
            dynCountMap.put("publishDate", InspgDynamicUtil.formatDate(dynamicMsg.getCreateDate()));
          }
          // 构建资源图片和权限
          this.buildResImgAndPermission(dynamicMsg, dynCountMap);
          // 构建资源收藏情况
          this.buildResCollectStatus(dynamicMsg, dynCountMap, psnId);
          if (dynamicMsg.getResType() != null) {
            dynCountMap.put("resType", dynamicMsg.getResType());
          }
          dynTotalMap.put(dynId.toString(), dynCountMap);
        }
      }
    }
    return dynTotalMap;
  }

  public void buildPdwhStatistics(Long pdwhPubId, Map<String, Object> dynCountMap) {
    PdwhPubStatisticsPO pps = pdwhStatisticsDAO.get(pdwhPubId);
    if (pps != null) {
      dynCountMap.put("awardCount", pps.getAwardCount() != null ? pps.getAwardCount() : 0);
      dynCountMap.put("commentCount", pps.getCommentCount() != null ? pps.getCommentCount() : 0);
      dynCountMap.put("shareCount", pps.getShareCount() != null ? pps.getShareCount() : 0);
    }
  }

  private void setResHasDelete(DynamicMsg dynamicMsg, Map<String, Object> dynCountMap) {
    if (dynamicMsg.getResType() != null) {
      switch (dynamicMsg.getResType()) {
        case DynamicConstants.RES_TYPE_PUB:// 成果
        case DynamicConstants.RES_TYPE_REF:// 文献
          PubSnsPO pubSns = pubSnsDAO.get(dynamicMsg.getResId());
          dynCountMap.put("resDelete", pubSns != null && pubSns.getStatus() != PubSnsStatusEnum.DELETED ? 1 : 0);
          break;
        case DynamicConstants.RES_TYPE_FUND:// 基金
          ConstFundCategory constFundCategory = constFundCategoryDao.get(dynamicMsg.getResId());
          dynCountMap.put("resDelete", constFundCategory != null ? 1 : 0);
          break;
        case DynamicConstants.RES_TYPE_AGENCY:// 资助机构
          ConstFundAgency fundAgency = fundAgencyDao.get(dynamicMsg.getResId());
          dynCountMap.put("resDelete", fundAgency != null ? 1 : 0);
          break;
        case DynamicConstants.RES_TYPE_JOURNALAWARD:// 推荐库
        case DynamicConstants.RES_TYPE_PUB_PDWH:
          PubPdwhPO pubPdwh = pubPdwhDAO.get(dynamicMsg.getResId());
          dynCountMap.put("resDelete", pubPdwh != null && pubPdwh.getStatus() != PubPdwhStatusEnum.DELETED ? 1 : 0);
          break;
        case DynamicConstants.RES_TYPE_PRJ:// 项目
          Project prj = projectDao.get(dynamicMsg.getResId());
          dynCountMap.put("resDelete", prj != null && prj.getStatus() != 1 ? 1 : 0);
          break;
        default:// 其他resType
          // TODO
          break;
      }
    }
  }

  // 构建资源收藏情况
  private Map<String, Object> buildResCollectStatus(DynamicMsg dynamicMsg, Map<String, Object> dynCountMap,
      Long psnId) {
    if (dynamicMsg.getResType() == DynamicConstants.RES_TYPE_FUND) {// 基金收藏
      MyFund fund = myFundDao.findMyFund(psnId, dynamicMsg.getResId());
      if (fund != null) {
        dynCountMap.put("collected", "1");
      } else {
        dynCountMap.put("collected", "0");
      }
    } else if (dynamicMsg.getResType() == DynamicConstants.RES_TYPE_PUB) {// 个人成果
      boolean collectedPub = collectedPubDao.isCollectedPub(psnId, dynamicMsg.getResId(), PubDbEnum.SNS);
      dynCountMap.put("collected", collectedPub == true ? "1" : "0");
    } else if (dynamicMsg.getResType() == DynamicConstants.RES_TYPE_PUB_PDWH
        || dynamicMsg.getResType() == DynamicConstants.RES_TYPE_JOURNALAWARD) {// 基准库成果
      boolean collectedPub = collectedPubDao.isCollectedPub(psnId, dynamicMsg.getResId(), PubDbEnum.PDWH);
      dynCountMap.put("collected", collectedPub == true ? "1" : "0");
    } else if (dynamicMsg.getResType() == DynamicConstants.RES_TYPE_AGENCY) {
      FundAgencyInterest interestAgency =
          fundAgencyInterestDao.findInterestAgencyByPsnIdAndAgencyId(psnId, dynamicMsg.getResId());
      if (interestAgency != null) {
        dynCountMap.put("collected", "1");
      } else {
        dynCountMap.put("collected", "0");
      }
    }
    return dynCountMap;
  }

  // 构建资源图片路径等信息
  private Map<String, Object> buildResImgAndPermission(DynamicMsg dynamicMsg, Map<String, Object> dynCountMap) {
    // 构建sns库成果全文图片相关信息
    this.buildSnsPubFulltextInfo(dynamicMsg, dynCountMap);
    // 构建pdwh库成果全文图片相关信息
    this.buildPdwhPubFulltextInfo(dynamicMsg, dynCountMap);
    // 构建基金图片相关信息
    this.buildFundImgInfo(dynamicMsg, dynCountMap);
    // 构建资助机构图片信息
    this.buildAgencyImgInfo(dynamicMsg, dynCountMap);
    // 构建新闻图片信息
    this.buildNewsImgInfo(dynamicMsg, dynCountMap);
    return dynCountMap;
  }

  // 构建sns库成果全文图片相关信息
  private Map<String, Object> buildSnsPubFulltextInfo(DynamicMsg dynamicMsg, Map<String, Object> dynCountMap) {
    if (dynamicMsg.getResType() == 1) {
      PubFullTextPO fullText = pubSnsFullTextDAO.getPubFullTextByPubId(dynamicMsg.getResId());
      PubSnsPO pubSns = pubSnsDAO.get(dynamicMsg.getResId());
      String resOwnerDes3Id = null;
      if (pubSns != null) {
        resOwnerDes3Id = Des3Utils.encodeToDes3(String.valueOf(pubSns.getCreatePsnId()));
      }
      dynCountMap.put("resOwnerDes3Id", resOwnerDes3Id);
      boolean flag =
          pubFTReqBaseDao.isFullTextReqAgree(dynamicMsg.getResId(), SecurityUtils.getCurrentUserId(), PubDbEnum.SNS);
      if (fullText != null) {
        if (StringUtils.isBlank(fullText.getThumbnailPath()) || pubSns == null
            || (pubSns != null && "DELETED".equals(pubSns.getStatus().toString()))) {
          dynCountMap.put("imgUrl", "/resmod/images_v5/images2016/file_img1.jpg");
        } else {
          dynCountMap.put("imgUrl", fullText.getThumbnailPath());
        }
        dynCountMap.put("des3FileId", Des3Utils.encodeToDes3(fullText.getFileId().toString()));
        dynCountMap.put("permission", fullText.getPermission());
        if (flag) {
          dynCountMap.put("permission", 0);
        }
      }
    }
    return dynCountMap;
  }

  // 构建pdwh库成果全文图片相关信息
  private Map<String, Object> buildPdwhPubFulltextInfo(DynamicMsg dynamicMsg, Map<String, Object> dynCountMap) {
    if (dynamicMsg.getResType() == 22 || dynamicMsg.getResType() == 24) {
      PdwhPubFullTextPO pdwhPubFullText = pdwhPubFullTextDAO.getFullTextByPubId(dynamicMsg.getResId());
      if (pdwhPubFullText != null) {
        dynCountMap.put("des3FileId", Des3Utils.encodeToDes3(pdwhPubFullText.getFileId().toString()));
        String imgUrl = "";
        if (pdwhPubFullText.getFileId() != null && pdwhPubFullText.getFileId() > 0) {
          if (pdwhPubFullText.getThumbnailPath() != null) {
            imgUrl = pdwhPubFullText.getThumbnailPath();
          }
          if (StringUtils.isBlank(imgUrl)) {
            imgUrl = "/resmod/images_v5/images2016/file_img1.jpg";
          }
        }
        dynCountMap.put("imgUrl", imgUrl);
        dynCountMap.put("permission", 0);
      }
    }
    return dynCountMap;
  }

  // 构建基金图片相关信息
  private Map<String, Object> buildFundImgInfo(DynamicMsg dynamicMsg, Map<String, Object> dynCountMap) {
    if (dynamicMsg.getResType() == DynamicConstants.RES_TYPE_FUND) {
      // 查询基金资助机构
      ConstFundCategory constFundCategory = constFundCategoryDao.get(dynamicMsg.getResId());
      Long agencyId = Optional.ofNullable(constFundCategory).map(ConstFundCategory::getAgencyId).orElse(0L);
      ConstFundAgency agency = constFundAgencyDao.findFundAgencyInfo(agencyId);
      String imgUrl = "";
      if (constFundCategory != null && constFundCategory.getAgencyId() != null) {
        agency = constFundAgencyDao.findFundAgencyInfo(constFundCategory.getAgencyId());
      }
      if (agency != null) {
        // 图片,有些图片在resmod里面
        if (StringUtils.isNotBlank(agency.getLogoUrl()) && agency.getLogoUrl().contains("http")) {
          imgUrl = agency.getLogoUrl();
        } else {
          imgUrl = agency.getLogoUrl() == null ? "" : "/resmod" + agency.getLogoUrl();
        }
      }
      // 没有图片直接显示保存在mongoDb中的html中的图片
      // if (StringUtils.isBlank(imgUrl)) {
      // imgUrl = "/ressns/images/default/default_fund_logo.jpg";
      // }
      dynCountMap.put("imgUrl", imgUrl);
    }
    return dynCountMap;
  }

  // 构建资助机构图片信息
  private Map<String, Object> buildAgencyImgInfo(DynamicMsg dynamicMsg, Map<String, Object> dynCountMap) {
    if (dynamicMsg.getResType() == DynamicConstants.RES_TYPE_AGENCY) {
      String imgUrl = "";
      ConstFundAgency agency = constFundAgencyDao.findFundAgencyInfo(dynamicMsg.getResId());
      if (agency != null) {
        // 图片,有些图片在resmod里面
        if (StringUtils.isNotBlank(agency.getLogoUrl()) && agency.getLogoUrl().contains("http")) {
          imgUrl = agency.getLogoUrl();
        } else {
          imgUrl = StringUtils.isBlank(agency.getLogoUrl()) ? "" : "/resmod" + agency.getLogoUrl();
        }
      }
      // 没有图片直接显示保存在mongoDb中的html中的图片
      // if (StringUtils.isBlank(imgUrl)) {
      // imgUrl = "/resmod/smate-pc/img/logo_instdefault.png";
      // }
      dynCountMap.put("imgUrl", imgUrl);
    }
    return dynCountMap;
  }

  // 构建新闻图片信息
  private Map<String, Object> buildNewsImgInfo(DynamicMsg dynamicMsg, Map<String, Object> dynCountMap) {
    if (dynamicMsg.getResType() == DynamicConstants.RES_TYPE_NEWS) {
      String imgUrl = "";
      NewsBase news = newsBaseDao.get(dynamicMsg.getResId());
      if (news != null) {
        // 图片,有些图片在resmod里面
        if (StringUtils.isNotBlank(news.getImage()) && news.getImage().contains("http")) {
          imgUrl = news.getImage();
        } else {
          imgUrl = StringUtils.isBlank(news.getImage()) ? "" : "/resmod" + news.getImage();
        }
      }
      dynCountMap.put("imgUrl", imgUrl);
    }
    return dynCountMap;
  }

  @Override
  public void updateShareCount(DynamicForm form) throws DynException {
    Integer resType = 0;
    if (form.getResType() == null || form.getResType() == 0) {
      switch (form.getResTypeStr()) {
        case "GRP_SHAREPRJ":
          resType = 4;
          break;
        case "pdwhpub":
          resType = 22;
          break;
        case "pub":
          resType = 1;
          break;
        case "fund":
          resType = 11;
          break;
        default:
          break;
      }
      form.setResType(resType);
    }
    uapdateDynShareCount(form);// 更新动态
    if (!"ATEMP".equals(form.getDynType()) && !"B1TEMP".equals(form.getDynType())) {// 带文字的
      updateSourceShareCount(form);// 更新资源
    } else {// 更新统计数
      updateDynStatistics(form.getDynId());
    }
  }

  private void updateDynStatistics(Long dynId) {
    if (dynId != null) {
      DynStatistics dynStatistics = dynStatisticsDao.getDynamicStatistics(dynId);
      if (dynStatistics != null) {
        Integer count = dynStatistics.getShareCount() == null ? 0 : dynStatistics.getShareCount();
        dynStatisticsDao.updateDynamicShareStatistics(dynId, count + 1);
      }
    }
  }

  private void uapdateDynShareCount(DynamicForm form) throws DynException {
    Long resId = form.getResId();
    Integer resType = form.getResType();
    if (!"B2TEMP".equals(form.getDynType()) && !"B3TEMP".equals(form.getDynType())) {
      resId = form.getDynId();
      resType = 0;
    }
    Person person = personQueryservic.findPerson(form.getPsnId());
    if (person == null) {
      throw new DynException("分享动态时，不能获取到分享人员的person对象");
    }
    Date now = new Date();
    DynamicShareRes dynamicShareRes = this.dynamicShareResDao.getDynamicShareRes(form.getResId(), form.getResType(), 1);
    // 更新资源的分享次数
    if (dynamicShareRes == null) {
      dynamicShareRes = new DynamicShareRes();
      dynamicShareRes.setResId(resId);
      dynamicShareRes.setResType(resType);
      dynamicShareRes.setResNode(1);
      dynamicShareRes.setShareTimes(1l);
      dynamicShareRes.setUpdateDate(now);
    } else {
      dynamicShareRes.setShareTimes(dynamicShareRes.getShareTimes() + 1);
      dynamicShareRes.setUpdateDate(now);
    }
    dynamicShareResDao.save(dynamicShareRes);
    // 添加人员分享记录
    DynamicSharePsn dynamicSharePsn = new DynamicSharePsn();
    dynamicSharePsn.setShareId(dynamicShareRes.getShareId());
    dynamicSharePsn.setShareTitle("分享到他（她）的动态。");
    dynamicSharePsn.setShareEnTitle("share to him (her) dynamic.");
    dynamicSharePsn.setSharerPsnId(person.getPersonId());
    dynamicSharePsn.setSharerName(personQueryservic.getPsnName(person, "zh_CN"));
    dynamicSharePsn.setSharerEnName(personQueryservic.getPsnName(person, "en_US"));
    dynamicSharePsn.setSharerAvatar(person.getAvatars());
    dynamicSharePsn.setShareDate(now);
    dynamicSharePsnDao.save(dynamicSharePsn);
  }

  private void updateSourceShareCount(DynamicForm form) throws DynException {
    if (form.getResId() != null && form.getResType() != null) {
      switch (form.getResType()) {
        case 4:
          addPrjShareStatistics(form);
          break;
        case 22:
        case 24:
          // addPdwhPubShareStatistics(form);
          break;
        case 1:
        case 2:
          // addPubShareStatistics(form);
          break;
        case 11:
          addFundShareStatistics(form);
          break;
        default:
          break;
      }
    }
  }

  private void addFundShareStatistics(DynamicForm form) {
    FundStatistics fs = fundStatisticsDao.get(form.getResId());
    if (fs == null) {
      fs = new FundStatistics();
      fs.setFundId(form.getResId());
      fs.setShareCount(0);
    }
    if (fs.getShareCount() == null) {
      fs.setShareCount(0);
    }
    fs.setShareCount(fs.getShareCount() + 1);
    fundStatisticsDao.save(fs);

  }

  private void addPdwhPubShareStatistics(DynamicForm form) {
    PdwhPubShare pdwhPubShare = new PdwhPubShare();
    pdwhPubShare.setSharePsnId(form.getPsnId());
    Date date = new Date();
    pdwhPubShare.setShareDate(date);
    pdwhPubShare.setResId(form.getResId());
    pdwhPubShare.setResType(form.getResType());
    pdwhPubShareDao.save(pdwhPubShare);
    PdwhPubStatistics s = pdwhPubStatisticsDao.get(form.getResId());
    if (s == null) {
      s = new PdwhPubStatistics();
      s.setPubId(form.getResId());
      s.setDbId(form.getDbId());
      s.setShareCount(0);
    }
    if (s.getShareCount() == null) {
      s.setShareCount(0);
    }
    s.setShareCount(s.getShareCount() + 1);
    pdwhPubStatisticsDao.save(s);

  }

  private void addPubShareStatistics(DynamicForm form) {
    PubStatisticsPO pubStatisticsPO = pubStatisticsDAO.get(form.getResId());
    if (pubStatisticsPO == null) {
      pubStatisticsPO = new PubStatisticsPO(form.getResId());
    }
    pubStatisticsPO.setShareCount(pubStatisticsPO.getShareCount() + 1);
    pubStatisticsDAO.save(pubStatisticsPO);
  }

  private void addPrjShareStatistics(DynamicForm form) {
    ProjectStatistics ps = projectStatisticsDao.get(form.getResId());
    if (ps == null) {
      ps = new ProjectStatistics();
      ps.setProjectId(form.getResId());
      ps.setShareCount(0);
    }
    if (ps.getShareCount() == null) {
      ps.setShareCount(0);
    }
    ps.setShareCount(ps.getShareCount() + 1);
    projectStatisticsDao.save(ps);
  }

  @Override
  public Map<String, Object> getDynCollect(String des3id, Long psnId) throws DynException {
    Map<String, Object> dynCountMap = new HashMap<String, Object>();
    if (StringUtils.isNotBlank(des3id)) {
      Long dynId = Long.parseLong(Des3Utils.decodeFromDes3(des3id));
      DynamicMsg dynamicMsg = dynamicMsgDao.get(dynId);
      if (dynamicMsg != null) {
        // 更新基金收藏情况
        if (dynamicMsg.getResType() == DynamicConstants.RES_TYPE_FUND) {// 基金收藏
          MyFund fund = myFundDao.findMyFund(psnId, dynamicMsg.getResId());
          if (fund != null) {
            dynCountMap.put("collected", "1");
          } else {
            dynCountMap.put("collected", "0");
          }
        } else if (dynamicMsg.getResType() == DynamicConstants.RES_TYPE_PUB) {// 个人成果
          boolean collectedPub = collectedPubDao.isCollectedPub(psnId, dynamicMsg.getResId(), PubDbEnum.SNS);
          dynCountMap.put("collected", collectedPub == true ? "1" : "0");
        } else if (dynamicMsg.getResType() == DynamicConstants.RES_TYPE_PUB_PDWH) {// 基准库成果
          boolean collectedPub = collectedPubDao.isCollectedPub(psnId, dynamicMsg.getResId(), PubDbEnum.PDWH);
          dynCountMap.put("collected", collectedPub == true ? "1" : "0");
        }
        if (dynamicMsg.getResType() != null) {
          dynCountMap.put("resType", dynamicMsg.getResType());
        }
      }
    }
    return dynCountMap;
  }

  @Override
  public String getPubOwner(Long pubId) throws DynException {
    String des3PubOwnerId = null;
    PubSnsPO pubSns = pubSnsDAO.get(pubId);
    if (pubSns != null) {
      des3PubOwnerId = Des3Utils.encodeToDes3(String.valueOf(pubSns.getCreatePsnId()));
    }
    return des3PubOwnerId;
  }
}
