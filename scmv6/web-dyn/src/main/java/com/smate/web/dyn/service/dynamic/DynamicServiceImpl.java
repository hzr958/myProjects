package com.smate.web.dyn.service.dynamic;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.HtmlUtils;

import com.smate.core.base.consts.dao.InstitutionDao;
import com.smate.core.base.consts.model.Institution;
import com.smate.core.base.dyn.consts.DynamicConstants;
import com.smate.core.base.dyn.dao.DynamicContentDao;
import com.smate.core.base.dyn.dao.DynamicMsgDao;
import com.smate.core.base.dyn.dao.DynamicRelationDao;
import com.smate.core.base.dyn.model.DynamicMsg;
import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.project.dao.ProjectDao;
import com.smate.core.base.project.model.Project;
import com.smate.core.base.project.model.ProjectStatistics;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.pub.dao.PubIndexUrlDao;
import com.smate.core.base.pub.model.PubIndexUrl;
import com.smate.core.base.pub.po.dao.PubPdwhSnsRelationDAO;
import com.smate.core.base.utils.common.CommonUtils;
import com.smate.core.base.utils.common.LocaleTextUtils;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.exception.DynException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.template.SmateFreeMarkerTemplateUtil;
import com.smate.web.dyn.dao.dynamic.DynStatisticsDao;
import com.smate.web.dyn.dao.fund.AgencyStatisticsDao;
import com.smate.web.dyn.dao.fund.CategoryMapBaseDao;
import com.smate.web.dyn.dao.fund.ConstFundAgencyDao;
import com.smate.web.dyn.dao.fund.ConstFundCategoryDao;
import com.smate.web.dyn.dao.fund.FundAgencyInterestDao;
import com.smate.web.dyn.dao.fund.FundStatisticsDao;
import com.smate.web.dyn.dao.fund.MyFundDao;
import com.smate.web.dyn.dao.grp.GrpFileDao;
import com.smate.web.dyn.dao.pdwhpub.PdwhPubFullTextDAO;
import com.smate.web.dyn.dao.pdwhpub.PdwhPubIndexUrlDao;
import com.smate.web.dyn.dao.pdwhpub.PdwhPublicationDao;
import com.smate.web.dyn.dao.pdwhpub.PubPdwhDAO;
import com.smate.web.dyn.dao.psn.AttPersonDao;
import com.smate.web.dyn.dao.psn.AttSettingsDao;
import com.smate.web.dyn.dao.pub.ProjectStatisticsDao;
import com.smate.web.dyn.dao.pub.PubFulltextDao;
import com.smate.web.dyn.dao.pub.PubSnsDAO;
import com.smate.web.dyn.dao.pub.PubSnsFullTextDAO;
import com.smate.web.dyn.dao.pub.PublicationDao;
import com.smate.web.dyn.dao.rcmd.ConstFundCategoryDisDao;
import com.smate.web.dyn.dao.rsycrcmd.RcmdSyncPsnInfoDao;
import com.smate.web.dyn.form.dynamic.DynamicForm;
import com.smate.web.dyn.form.dynamic.DynamicRemdForm;
import com.smate.web.dyn.form.news.NewsForm;
import com.smate.web.dyn.model.dynamic.DynStatistics;
import com.smate.web.dyn.model.fund.AgencyStatistics;
import com.smate.web.dyn.model.fund.CategoryMapBase;
import com.smate.web.dyn.model.fund.ConstFundAgency;
import com.smate.web.dyn.model.fund.ConstFundCategory;
import com.smate.web.dyn.model.fund.FundAgencyInterest;
import com.smate.web.dyn.model.fund.FundStatistics;
import com.smate.web.dyn.model.fund.MyFund;
import com.smate.web.dyn.model.grp.GrpFile;
import com.smate.web.dyn.model.news.NewsBase;
import com.smate.web.dyn.model.pdwhpub.PdwhPubIndexUrl;
import com.smate.web.dyn.model.psn.AttSettings;
import com.smate.web.dyn.model.psn.AttSettingsId;
import com.smate.web.dyn.model.pub.PdwhPubFullTextPO;
import com.smate.web.dyn.model.pub.PubFullTextPO;
import com.smate.web.dyn.model.pub.PubPdwhPO;
import com.smate.web.dyn.model.pub.PubSnsPO;
import com.smate.web.dyn.model.rsycrcmd.RcmdSyncPsnInfo;
import com.smate.web.dyn.service.news.NewsBaseService;
import com.smate.web.dyn.service.news.NewsOptService;
import com.smate.web.dyn.service.psn.AttendStatisticsService;
import com.smate.web.dyn.service.share.ShareStatisticsService;
import com.smate.web.dyn.service.statistic.StatisticsService;

/**
 * 动态相关服务
 * 
 * @author zk
 * 
 */
@Service("dynamicService")
@Transactional(rollbackFor = Exception.class)
public class DynamicServiceImpl implements DynamicService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private DynamicMsgDao dynamicMsgDao;
  @Autowired
  private DynamicContentDao dynamicContentDao;
  @Autowired
  private DynamicRelationDao dynamicRelationDao;
  @Autowired
  private DynStatisticsDao dynStatisticsDao;
  @Autowired
  private AttPersonDao attPersonDao;
  @Autowired
  private AttSettingsDao attSettingsDao;
  @Autowired
  private RcmdSyncPsnInfoDao rcmdSyncPsnInfoDao;
  @Autowired
  private AttendStatisticsService attendStatisticsService;
  @Autowired
  private DynamicAwardService dynamicAwardService;
  @Autowired
  private MyFundDao myFundDao;
  @Autowired
  private PublicationDao publicationDao;
  @Autowired
  private PubIndexUrlDao pubIndexUrlDao;
  @Autowired
  private PubFulltextDao pubFulltextDao;
  @Autowired
  private ProjectDao projectDao;
  @Autowired
  private ConstFundCategoryDao constFundCategoryDao;
  @Autowired
  private ConstFundAgencyDao constFundAgencyDao;
  @Autowired
  private ConstFundCategoryDisDao constFundCategoryDisDao;
  @Autowired
  private CategoryMapBaseDao categoryMapBaseDao;
  @Autowired
  private PdwhPublicationDao pdwhPublicationDao;
  @Autowired
  private PdwhPubIndexUrlDao pdwhPubIndexUrlDao;
  @Value("${domainscm}")
  private String domainscm;
  @Value("${domainMobile}")
  private String domainMobile;
  @Autowired
  private SmateFreeMarkerTemplateUtil smateFreeMarkerTemplateUtil;
  @Autowired
  private ShareStatisticsService shareStatisticsService;
  @Autowired
  private StatisticsService statisticsService;
  @Autowired
  private PubSnsDAO pubSnsDAO;
  @Autowired
  private PubSnsFullTextDAO pubSnsFullTextDAO;
  @Autowired
  private PubPdwhDAO pubPdwhDAO;
  @Autowired
  private DynamicShareService dynamicShareService;
  @Autowired
  private ProjectStatisticsDao projectStatisticsDao;
  @Autowired
  private PdwhPubFullTextDAO pdwhFullTextDAO;
  @Autowired
  private FundStatisticsDao fundStatisticsDao;
  @Autowired
  private AgencyStatisticsDao agencyStatisticsDao;
  @Autowired
  private FundAgencyInterestDao fundAgencyInterestDao;
  @Autowired
  private PubPdwhSnsRelationDAO pubPdwhSnsRelationDAO;
  @Autowired
  private RestTemplate restTemplate;
  @Autowired
  private NewsBaseService newsBaseService;
  @Autowired
  private NewsOptService newsOptService;
  @Autowired
  private GrpFileDao grpFileDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Autowired
  private InstitutionDao institutionDao;

  @Override
  public void dynamicShow(DynamicForm form) throws DynException {
    List<Long> dynIds =
        dynamicMsgDao.getDynListForPsnAll(form.getPsnId(), form.getPage().getPageSize(), form.getPage().getPageNo());
    form.getPage().setTotalCount(dynamicMsgDao.getDynListForPsnCount(form.getPsnId()));
    if (CollectionUtils.isNotEmpty(dynIds)) {
      form.setDynIds(dynIds);
      this.handleDynamicMsg(form);
    }
  }

  // 对动态信息进行加工
  private void handleDynamicMsg(DynamicForm form) throws DynException {
    List<String> dynDatas = new ArrayList<String>();
    for (Long dynId : form.getDynIds()) {
      String dynData = dynamicContentDao.getDynContent(dynId, form.getLocale(), form.getPlatform());
      // 这条动态的权限判断，添加成果的动态
      // DynamicMsg dynamicMsg = dynamicMsgDao.get(dynId);
      Boolean permit = true;
      // 先注释掉，SCM-16070
      /*
       * if("B3TEMP".equalsIgnoreCase(dynamicMsg.getDynType())) { permit =
       * publicPrivacyService.canLookConsumerAddRefDyn(form.getPsnId(), dynamicMsg.getProducer() ); }
       */
      if (permit && StringUtils.isNotBlank(dynData)) {
        dynDatas.add(dynData);
      }
    }
    form.setDynLists(dynDatas);
  }

  @Override
  public void deleteDyn(DynamicForm form) throws Exception {
    // 当前动态
    DynamicMsg dynamicMsg = dynamicMsgDao.get(form.getDynId());
    if (dynamicMsg == null) {
      throw new Exception("查找不到该条动态dynId=" + form.getDynId());
    }
    List<DynamicMsg> dynamicMsgList =
        dynamicMsgDao.findShieldDynamicMsg(dynamicMsg.getDynId(), dynamicMsg.getResId(), dynamicMsg.getResType());
    // 删除自己的动态
    if (form.getPsnId().longValue() == dynamicMsg.getProducer().longValue()) {
      dynamicMsgDao.updateDynStatus(dynamicMsg.getDynId(), DynamicConstants.NINE_NINE);
    } else {
      // 屏蔽子动态 查询要要屏蔽的dynId列表
      if (dynamicMsgList != null && dynamicMsgList.size() > 0) {
        for (DynamicMsg msg : dynamicMsgList) {
          dynamicRelationDao.shieldDynRelation(msg.getDynId(), form.getPsnId());
        }
      }
    }
  }

  @Override
  public void skipPsnDyn(DynamicForm form) throws Exception {
    dynamicRelationDao.updateRelationStatus(form.getDynId(), form.getResPsnId(), form.getPsnId(),
        DynamicConstants.NINE_NINE);
    attPersonDao.deleteAttPerson(form.getPsnId(), form.getResPsnId());
    attendStatisticsService.addAttRecord(form.getPsnId(), form.getResPsnId(), DynamicConstants.ZERO);
  }

  @Override
  public void skipTypeDyn(DynamicForm form) throws Exception {
    List<Integer> dynTmpList = new ArrayList<Integer>();
    List<Integer> dynTypeList = new ArrayList<Integer>();
    if (form.getAttId() != null) {
      dynTmpList = this.collectDynTmp(form.getAttId());
    } else {
      DynamicMsg dynamicMsg = dynamicMsgDao.getDynTypeAndTmp(form.getDynId());
      if (dynamicMsg != null) {
        // form.setAttId(dynTypeToAtt(dynamicMsg.getDynType()));
        // dynTypeList.add(dynamicMsg.getDynType());
      }
    }
    this.cancelAtt(form);
    dynamicRelationDao.updateRelationStatus(form.getPsnId(), dynTmpList, dynTypeList, DynamicConstants.ZERO);
  }

  // 取消关注
  private void cancelAtt(DynamicForm form) {
    if (form.getAttId() != null && form.getPsnId() != null) {
      AttSettings attSettings = new AttSettings();
      AttSettingsId pk = new AttSettingsId(form.getPsnId(), form.getAttId());
      attSettings.setAttSettingsId(pk);
      attSettingsDao.delete(attSettings);
      // 同步数据至推荐服务器
      RcmdSyncPsnInfo rsp = rcmdSyncPsnInfoDao.get(form.getPsnId());
      if (rsp == null) {
        rsp = new RcmdSyncPsnInfo(form.getPsnId());
      }
      rsp.setAttFlag(1);
      rcmdSyncPsnInfoDao.save(rsp);
    }
  }

  private String dynTypeToAtt(int dynType) {
    String attId = "";
    switch (dynType) {
      case DynamicConstants.DYN_TYPE_ADD + DynamicConstants.RES_TYPE_REF:// 文献变更
        attId = "02";
        break;
      case DynamicConstants.DYN_TYPE_UPLOAD + DynamicConstants.RES_TYPE_REF:// 文献全文上传
        attId = "02";
        break;
      case DynamicConstants.DYN_TYPE_ADD + DynamicConstants.RES_TYPE_PUB:// 成果变更
        attId = "07";
        break;
      case DynamicConstants.DYN_TYPE_UPLOAD + DynamicConstants.RES_TYPE_PUB:// 成果全文上传
        attId = "07";
        break;
      case DynamicConstants.DYN_TYPE_ADD + DynamicConstants.RES_TYPE_PRJ:// 项目变更
        attId = "14";
        break;
      case DynamicConstants.DYN_TYPE_UPLOAD + DynamicConstants.RES_TYPE_PRJ:// 项目全文上传
        attId = "14";
        break;
    }
    return attId;
  }

  // 收集关注对应的模版id列表
  private List<Integer> collectDynTmp(String attId) {
    List<Integer> tmpList = new ArrayList<Integer>();
    if ("01".equals(attId)) {// 添加好友
      tmpList.add(5);
      tmpList.add(17);
    } else if ("03".equals(attId)) {// 群组信息变更
      tmpList.add(7);
      tmpList.add(26);
    } else if ("05".equals(attId)) {// 更新个人资料
      tmpList.add(4);
      tmpList.add(16);
      tmpList.add(27);
      tmpList.add(28);
      tmpList.add(29);
      tmpList.add(35);
    } else if ("09".equals(attId)) {// 个人心情
      tmpList.add(1);
      tmpList.add(2);
    } else if ("11".equals(attId)) {// 评价
      tmpList.add(14);
      tmpList.add(15);
      tmpList.add(24);
      tmpList.add(25);
    }
    return tmpList;
  }

  @Override
  public void buildDynamicDetail(DynamicForm form) throws DynException {
    if (form.getDynId() != null) {
      String dynamicContent = dynamicContentDao.getDynContent(form.getDynId(), form.getLocale(), form.getPlatform());
      DynamicMsg dynamicMsg = dynamicMsgDao.get(form.getDynId());
      DynStatistics ds = dynStatisticsDao.getDynamicStatistics(dynamicMsg.getSameFlag());
      Map<String, Object> resultMap = new HashMap<String, Object>();
      resultMap.put("dynamicContent", StringEscapeUtils.unescapeHtml4(dynamicContent));// 不解码动态评论详情页面的换行标签会显示成<br>
      if (ds != null) {
        // 获取正确的赞数 评论数 和分享数
        resultMap.put("dyn-statis-award",
            ds.getAwardCount() == null || ds.getAwardCount() < 0 ? 0 : ds.getAwardCount());
        resultMap.put("dyn-statis-reply",
            ds.getCommentCount() == null || ds.getCommentCount() < 0 ? 0 : ds.getCommentCount());
        resultMap.put("dyn-statis-share",
            ds.getShareCount() == null || ds.getShareCount() < 0 ? 0 : ds.getShareCount());
        // 获取准确的点赞状态
        resultMap.put("dyn-statis-hasaward", dynamicAwardService.getPsnHasAward(form.getPsnId(), form.getDynId()));
      } else {
        resultMap.put("dyn-statis-award", 0);
        resultMap.put("dyn-statis-reply", 0);
        resultMap.put("dyn-statis-share", 0);
      }
      switch (dynamicMsg.getResType()) {
        case 1:
        case 2:
        case 22:
        case 24:
          /**
           * 校验成果是否有全文,之前有全文后删除应该替换为默认图片
           */
          String des3PubId = NumberUtils.isNotNullOrZero(dynamicMsg.getResId())
              ? Des3Utils.encodeToDes3(dynamicMsg.getResId().toString())
              : "";
          if (StringUtils.isNotEmpty(des3PubId)) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("des3PubId", des3PubId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<String> httpEntity = new HttpEntity<String>(JacksonUtils.mapToJsonStr(params), headers);
            String fullTextImageUrl =
                restTemplate.postForObject(domainscm + "/data/pub/getfulltextimageurl", httpEntity, String.class);
            if (StringUtils.isNotEmpty(fullTextImageUrl)) {
              if ("/resmod/images_v5/images2016/file_img1.jpg".equalsIgnoreCase(fullTextImageUrl)) {
                // 有全文但没有缩略图
                resultMap.put("hasFullText", "true");
                resultMap.put("hasThumbnailPath", "false");
              } else {
                // 有全文且有缩略图
                resultMap.put("hasThumbnailPath", "true");
                resultMap.put("hasFullText", "true");
              }
            } else {
              // 没有全文
              resultMap.put("hasFullText", "false");
              resultMap.put("hasThumbnailPath", "false");
            }
          }
          resultMap.put("dyn-statis-collected", "0");
          break;
        case 11:
          MyFund fund = myFundDao.findMyFund(form.getPsnId(), dynamicMsg.getResId());
          if (fund != null) {
            resultMap.put("dyn-statis-collected", "1");
          } else {
            resultMap.put("dyn-statis-collected", "0");
          }
          // 基金的默认图片有错误，替换掉
          dynamicContent = dynamicContent
              .replaceAll("/resmod/images_v5/images2016/file_img1.jpg", "/ressns/images/default/default_fund_logo.jpg")
              .replaceAll("/resmod/images_v5/images2016/file_img.jpg", "/ressns/images/default/default_fund_logo.jpg");
          resultMap.put("dynamicContent", dynamicContent);
          break;
        case 25:
          FundAgencyInterest interest =
              fundAgencyInterestDao.findInterestAgencyByPsnIdAndAgencyId(form.getPsnId(), dynamicMsg.getResId());
          if (interest != null) {
            resultMap.put("dyn-statis-collected", interest.getStatus());
          } else {
            resultMap.put("dyn-statis-collected", "0");
          }
          if (resultMap.get("dyn-statis-collected").toString().equals("0")) {
            dynamicContent = dynamicContent.replace("收藏", "关注");
          } else {
            dynamicContent = dynamicContent.replace("收藏", "取消关注").replace("paper_footer-comment\">",
                "paper_footer-comment__flag\">");
          }

          // 资助机构的默认图片有错误，替换掉
          dynamicContent = dynamicContent.replaceAll("/resmod/images_v5/images2016/file_img1.jpg",
              "/resmod/smate-pc/img/logo_instdefault.png");
          resultMap.put("dynamicContent", dynamicContent);
          break;
        default:
          resultMap.put("dyn-statis-collected", "0");
          break;
      }
      form.setDynMap(resultMap);
    } else {
      throw new DynException("dynId为空");
    }
  }

  @Override
  public void getShareTxt(DynamicForm form) throws Exception {
    form.setResultMap(new HashMap<String, Object>());
    checkParam(form);
    form.setLocale(LocaleContextHolder.getLocale().toString());
    Long resOwnerID = null;
    switch (form.getResType()) {
      case 1:
      case 2:// 个人库成果
        buildPubShareTxt(form);
        dynamicShareService.addPubShareStatistics(form, form.getResId());
        dynamicShareService.sysSnsPdwhShareStatistics(form);
        // 资源拥有者id
        break;
      case 4:// 项目
        buildPrjShareTxt(form);
        addPrjShareStatistics(form);
        resOwnerID = statisticsService.findPsnId(form.getResId(), 1, 4);
        if (resOwnerID != null) {
          shareStatisticsService.addShareRecord(form.getPsnId(), resOwnerID, form.getResId(), 4);
        }
        break;
      case 11:// 基金
        buildFundShareTxt(form);
        addFundShareStatis(form.getResId());
        break;
      case 22:// 基准库成果
      case 24:// 基准库成果
        buildPdwhPubShareTxt(form);
        dynamicShareService.addPdwhPubShareStatistics(form.getResId(), form);
        dynamicShareService.sysPdwhSnsShare(form);// 个人库关联成果数据同步
        break;
      case DynamicConstants.RES_TYPE_AGENCY:// 资助机构
        buildAgencyShareTxt(form);
        addAgencyShareStatis(form.getResId());
        break;
      case DynamicConstants.RES_TYPE_NEWS:// 新闻消息
        buildNewsShareTxt(form);
        NewsForm newsForm = new NewsForm();
        // 更新统计数 和 分享统计数
        newsForm.setNewsId(form.getResId());
        newsForm.setPsnId(form.getPsnId());
        newsForm.setPlatform(Integer.parseInt(form.getPlatform()));
        newsOptService.addNewsShare(newsForm);
        break;
      case DynamicConstants.RES_TYPE_PSNRESUME:// 个人主页
        buildPersonShareTxt(form);
        break;
      case DynamicConstants.RES_TYPE_INS:// 机构
        buildInsShareTxt(form);
        // SCM-26424 机构主页--分享方式不管选哪种，表里platform分享平台都是1？
        if ("4".equals(form.getPlatform())) {
          form.setPlatform("1");
        } else if ("5".equals(form.getPlatform())) {
          form.setPlatform("2");
        } else if ("6".equals(form.getPlatform())) {
          form.setPlatform("3");
        } else if ("7".equals(form.getPlatform())) {
          form.setPlatform("4");
        } else if ("8".equals(form.getPlatform())) {
          form.setPlatform("6");
        }
        addInsShareStatistic(form);
        break;
      default:
        break;
    }
    List<Map<String, Object>> resDetailList = new ArrayList<Map<String, Object>>();
    resDetailList.add(form.getResultMap());
    Map<String, Object> tmpMap = new HashMap<String, Object>();
    tmpMap.put("resCount", form.getResCount());
    tmpMap.put("resType", form.getResType());
    tmpMap.put("resDetails", resDetailList);
    form.getResultMap().put("resCount", form.getResCount());
    form.getResultMap().put("showTxt", this.buildShareTxt(tmpMap, form));
    changeHtmlStr(form.getResultMap().get("showTxt").toString());
  }

  public void addInsShareStatistic(DynamicForm form) throws ServiceException {
    try {
      statisticsService.addInsRecord(form.getPsnId(), form.getPlatform(), form.getResId());
    } catch (Exception e) {
      logger.error("调用机构社交化分享接口出错， 机构ID : " + form.getResId() + "psnId:" + form.getPsnId(), e);
      throw new ServiceException();
    }
  }

  @Override
  public void updateResShareStatic(DynamicForm form) throws Exception {
    Long resOwnerID = null;
    switch (form.getResType()) {
      case 1:
      case 2:// 个人库成果
        dynamicShareService.addPubShareStatistics(form, form.getResId());
        dynamicShareService.sysSnsPdwhShareStatistics(form);
        // 资源拥有者id
        break;
      case 4:// 项目
        addPrjShareStatistics(form);
        resOwnerID = statisticsService.findPsnId(form.getResId(), 1, 4);
        if (resOwnerID != null) {
          shareStatisticsService.addShareRecord(form.getPsnId(), resOwnerID, form.getResId(), 4);
        }
        break;
      case 11:// 基金
        addFundShareStatis(form.getResId());
        break;
      case 22:// 基准库成果
      case 24:// 基准库成果
        dynamicShareService.addPdwhPubShareStatistics(form.getResId(), form);
        dynamicShareService.sysPdwhSnsShare(form);// 个人库关联成果数据同步
        break;
      case DynamicConstants.RES_TYPE_AGENCY:// 资助机构
        addAgencyShareStatis(form.getResId());
        break;
      default:
        break;
    }
  }

  private void addAgencyShareStatis(Long resId) {
    AgencyStatistics fs = agencyStatisticsDao.get(resId);
    if (fs == null) {
      fs = new AgencyStatistics();
      fs.setAgencyId(resId);
      fs.setShareSum(0L);
    }
    if (fs.getShareSum() == null) {
      fs.setShareSum(0L);
    }
    Long count = fs.getShareSum() + 1L;
    fs.setShareSum(count);
    agencyStatisticsDao.save(fs);
  }

  // 基金分享统计数
  private void addFundShareStatis(Long resId) {
    FundStatistics fs = fundStatisticsDao.get(resId);
    if (fs == null) {
      fs = new FundStatistics();
      fs.setFundId(resId);
      fs.setShareCount(0);
    }
    if (fs.getShareCount() == null) {
      fs.setShareCount(0);
    }
    fs.setShareCount(fs.getShareCount() + 1);
    fundStatisticsDao.save(fs);

  }

  /**
   * 分享完成后将数据增加项目的分享数量到数据库中
   * 
   * @param form
   */
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

  private String buildShareTxt(Map<String, Object> tmpMap, DynamicForm form) {
    try {
      return smateFreeMarkerTemplateUtil.produceTemplate(tmpMap,
          "dynamic_template_shareTxt_" + form.getLocale() + ".ftl");
    } catch (DynException e) {
      logger.error("资源分享到站外构建模版出错，tmpMap=" + tmpMap.toString(), e);
      form.getResultMap().put("result", "error");
    }
    return "";
  }

  private void buildPdwhPubShareTxt(DynamicForm form) throws Exception {
    PubPdwhPO pub = pubPdwhDAO.get(form.getResId());
    if (pub == null) {
      throw new Exception("分享到站外的资源不存在，resId=" + form.getResId() + ",resType=" + form.getResType());
    }
    Map<String, Object> map = form.getResultMap();
    String title = "";
    String source = "";
    String language = "";
    String pic = "";
    String resLink = domainscm + "/pub/outside/pdwhdetails?des3PubId=" + form.getDes3ResId();
    String mobileResLink =
        domainMobile + "/pub/details/pdwh?des3PubId=" + URLEncoder.encode(form.getDes3ResId(), "utf-8");
    Long resId = Long.parseLong(Des3Utils.decodeFromDes3(form.getDes3ResId()));
    PdwhPubFullTextPO fullText = pdwhFullTextDAO.getFullTextByPubId(resId);
    if (fullText != null && StringUtils.isNotBlank(fullText.getThumbnailPath())) {
      pic = domainscm + fullText.getThumbnailPath();
    } else {
      if (fullText != null) {
        pic = domainscm + "/resmod/images_v5/images2016/file_img1.jpg";
      } else {
        pic = domainscm + "/resmod/images_v5/images2016/file_img.jpg";
      }
    }
    if ("zh_CN".equals(form.getLocale())) {
      title = pub.getTitle();
      source = pub.getBriefDesc();
      language = "zh";
    } else {
      title = pub.getTitle();
      source = pub.getBriefDesc();
      language = "en";
    }
    PdwhPubIndexUrl pdwhPubIndexUrl = pdwhPubIndexUrlDao.get(form.getResId());
    if (pdwhPubIndexUrl != null && StringUtils.isNotBlank(pdwhPubIndexUrl.getPubIndexUrl())) {
      resLink = domainscm + "/" + ShortUrlConst.S_TYPE + "/" + pdwhPubIndexUrl.getPubIndexUrl();
    }
    map.put("title", changeHtmlStr(title));
    map.put("resOther", changeHtmlStr(source));
    map.put("id", form.getResId());
    map.put("des3Id", form.getDes3ResId());
    map.put("authorNames", pub.getAuthorNames());
    map.put("language", language);
    map.put("resLink", resLink);
    map.put("mobileResLink", mobileResLink);
    map.put("pic", pic);
  }

  private void buildInsShareTxt(DynamicForm form) throws Exception {
    Map<String, Object> map = form.getResultMap();
    String title = form.getInsName();
    String source = "";
    String language = "";
    String resLink = form.getInsUrl();
    String mobileResLink = resLink;
    String pic = domainscm + form.getInsPic();
    if ("zh_CN".equals(form.getLocale())) {
      language = "zh";
    } else {
      language = "en";
    }
    map.put("title", changeHtmlStr(title));
    map.put("resOther", changeHtmlStr(source));
    map.put("id", form.getResId());
    map.put("des3Id", form.getDes3ResId());
    map.put("authorNames", "");
    map.put("language", language);
    map.put("resLink", resLink);
    map.put("mobileResLink", mobileResLink);
    map.put("pic", pic);
  }

  // 构建个人主页信息
  private void buildPersonShareTxt(DynamicForm form) throws Exception {
    Person person = personDao.get(form.getResId());
    if (person == null) {
      throw new Exception("分享到站外的资源不存在，resId=" + form.getResId() + ",resType=" + form.getResType());
    }
    Map<String, Object> map = form.getResultMap();
    String name = "";
    String language = "";
    String pic = StringUtils.isNotBlank(person.getAvatars()) ? person.getAvatars()
        : domainscm + "/resmod/smate-pc/img/logo_psndefault.png";
    PsnProfileUrl psnProfileUrl = psnProfileUrlDao.get(form.getResId());
    String resLink = "";
    if (psnProfileUrl != null && StringUtils.isNotBlank(psnProfileUrl.getPsnIndexUrl())) {
      resLink = domainscm + "/" + ShortUrlConst.P_TYPE + "/" + psnProfileUrl.getPsnIndexUrl();
    }
    // 获取单位名称
    String insName = person.getInsName();
    if (StringUtils.isBlank(insName) && person.getInsId() != null) {
      Institution ins = institutionDao.findInsName(person.getInsId());
      if (ins != null) {
        insName = StringUtils.isNotBlank(ins.getZhName()) ? ins.getZhName() : ins.getEnName();
      }
    }
    // 获取部门名
    String department = person.getDepartment();
    // 构建单位和部门信息
    String insAndDepInfo = "";
    if (StringUtils.isBlank(insName) || StringUtils.isBlank(department)) {
      insAndDepInfo =
          (StringUtils.isBlank(insName) ? "" : insName) + (StringUtils.isBlank(department) ? "" : department);
    } else {
      insAndDepInfo = (insName + ", " + department);
    }
    // 获取职称信息
    String position = person.getPosition();
    // 获取头衔信息
    String titolo = person.getTitolo();
    // 构建职称和头衔信息
    String positionAndTitolo = "";
    if (StringUtils.isBlank(position) || StringUtils.isBlank(titolo)) {
      positionAndTitolo = (StringUtils.isBlank(position) ? "" : position) + (StringUtils.isBlank(titolo) ? "" : titolo);
    } else {
      positionAndTitolo = position + ", " + titolo;
    }
    if ("en_US".equals(form.getLocale())) {
      name = StringUtils.isNotBlank(person.getEname()) ? person.getEname() : person.getName();
      language = "en";
    } else {
      name = StringUtils.isNotBlank(person.getName()) ? person.getName() : person.getEname();
      language = "zh";
    }
    map.put("title", changeHtmlStr(name));
    map.put("resOther", changeHtmlStr(positionAndTitolo));
    map.put("id", form.getResId());
    map.put("des3Id", form.getDes3ResId());
    map.put("language", language);
    map.put("resLink", resLink);
    map.put("pic", pic);
    map.put("mobileResLink", resLink);
    map.put("authorNames", insAndDepInfo);
  }

  // 构建新闻消息类
  private void buildNewsShareTxt(DynamicForm form) throws Exception {
    NewsBase newsBase = newsBaseService.get(form.getResId());
    if (newsBase == null) {
      throw new Exception("分享到站外的资源不存在，resId=" + form.getResId() + ",resType=" + form.getResType());
    }
    Map<String, Object> map = form.getResultMap();
    String title = "";
    String source = "";
    String language = "";
    String pic = StringUtils.isNotBlank(newsBase.getImage()) ? newsBase.getImage()
        : domainscm + "/resmod/smate-pc/img/logo_newsdefault.png";
    String resLink = domainscm + "/dynweb/news/details?des3NewsId=" + form.getDes3ResId() + "&origin=outsideShare";
    Long resId = Long.parseLong(Des3Utils.decodeFromDes3(form.getDes3ResId()));
    if ("zh_CN".equals(form.getLocale())) {
      language = "zh";
    } else {
      language = "en";
    }
    title = newsBase.getTitle();
    source = newsBase.getBrief();
    map.put("title", changeHtmlStr(title));
    map.put("resOther", changeHtmlStr(source));
    map.put("id", form.getResId());
    map.put("des3Id", form.getDes3ResId());
    map.put("language", language);
    map.put("resLink", resLink);
    map.put("pic", pic);
    map.put("authorNames", "");
  }

  private void buildFundShareTxt(DynamicForm form) throws Exception {
    ConstFundCategory fund = constFundCategoryDao.get(form.getResId());
    if (fund == null) {
      throw new Exception("分享到站外的资源不存在，resId=" + form.getResId() + ",resType=" + form.getResType());
    }
    Map<String, Object> map = form.getResultMap();
    String title = "";
    String source = "";
    String language = "";
    String resLink = domainscm + "/prjweb/outside/showfund?encryptedFundId=" + form.getDes3ResId();
    String mobileResLink = domainMobile + "/prjweb/wechat/findfundsxml?des3FundId=" + form.getDes3ResId();
    String pic = domainscm + "/ressns/images/default/default_fund_logo.jpg";
    if (fund.getAgencyId() != null) {
      String logoUrl = constFundAgencyDao.findFundAgencyLogoUrl(fund.getAgencyId());
      if (StringUtils.isNotBlank(logoUrl)) {
        if (logoUrl.contains("http")) {
          pic = logoUrl;
        } else {
          pic = domainscm + "/resmod" + logoUrl;
        }
      }
      // 查询基金资助机构
      ConstFundAgency agency = constFundAgencyDao.findFundAgencyInfo(fund.getAgencyId());
      if (agency != null) {
        source = buildSource(agency, fund, form);
      }
    }
    // 校验文件不存在替换为默认图片
    if (fileIsNotExist(pic)) {
      pic = domainscm + "/ressns/images/default/default_fund_logo.jpg";
    }
    if ("zh_CN".equals(form.getLocale())) {
      title = StringUtils.isBlank(fund.getNameZh()) ? fund.getNameEn() : fund.getNameZh();
      language = "zh";
    } else {
      title = StringUtils.isBlank(fund.getNameEn()) ? fund.getNameZh() : fund.getNameEn();
      language = "en";
    }
    map.put("title", changeHtmlStr(title));
    map.put("resOther", changeHtmlStr(source));
    map.put("id", form.getResId());
    map.put("des3Id", form.getDes3ResId());
    map.put("authorNames", fund.getInsName());
    map.put("language", language);
    map.put("resLink", resLink);
    map.put("mobileResLink", mobileResLink);
    map.put("pic", pic);
  }

  /**
   * 判断文件不存在
   * 
   * @param fileUrl
   * @return
   */
  public boolean fileIsNotExist(String fileUrl) {
    if (StringUtils.isEmpty(fileUrl)) {
      return true;
    }
    if (fileUrl.contains("http")) {
      // URL文件
      try {
        URL url = new URL(fileUrl.trim());
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        return httpURLConnection.getResponseCode() == 200 ? false : true;
      } catch (Exception e) {
        logger.error("文件不存在,fileUrl={}", fileUrl, e);
        return true;
      }
    } else {
      File file = new File(fileUrl);
      if (!file.exists()) {
        return true;
      }
    }
    return false;
  }

  private void buildAgencyShareTxt(DynamicForm form) throws Exception {
    // 查询基金资助机构
    ConstFundAgency agency = constFundAgencyDao.findFundAgencyInfo(form.getResId());
    if (agency == null) {
      throw new Exception("分享到站外的资源不存在，resId=" + form.getResId() + ",resType=" + form.getResType());
    }
    Map<String, Object> map = form.getResultMap();
    String title = "";
    String source = "";
    String language = "";
    String resLink = domainscm + "/prjweb/outside/agency?des3FundAgencyId=" + form.getDes3ResId();
    String mobileResLink = domainMobile + "/prj/outside/agency?des3FundAgencyId=" + form.getDes3ResId();
    String pic = domainscm + "/resmod/smate-pc/img/logo_instdefault.png";
    String logoUrl = agency.getLogoUrl();
    if (StringUtils.isNotBlank(logoUrl)) {
      if (logoUrl.contains("http")) {
        pic = logoUrl;
      } else {
        pic = domainscm + "/resmod" + logoUrl;
      }
    }
    if ("zh_CN".equals(form.getLocale())) {
      title = StringUtils.isBlank(agency.getNameZh()) ? agency.getNameEn() : agency.getNameZh();
      source = StringUtils.isNotBlank(agency.getAddress()) ? agency.getAddress() : agency.getEnAddress();
      language = "zh";
    } else {
      title = StringUtils.isBlank(agency.getNameEn()) ? agency.getNameZh() : agency.getNameEn();
      source = StringUtils.isNotBlank(agency.getEnAddress()) ? agency.getEnAddress() : agency.getAddress();
      language = "en";
    }
    map.put("title", changeHtmlStr(title));
    map.put("resOther", changeHtmlStr(source));
    map.put("id", form.getResId());
    map.put("authorNames", "");
    map.put("des3Id", form.getDes3ResId());
    map.put("language", language);
    map.put("resLink", resLink);
    map.put("mobileResLink", mobileResLink);
    map.put("pic", pic);
  }

  private String buildSource(ConstFundAgency agency, ConstFundCategory fund, DynamicForm form) {
    String scienceArea = buildscienceAreaNames(form);
    String agencyName = LocaleTextUtils.getStrByLocale(form.getLocale(), agency.getNameZh(), agency.getNameEn());
    String showDate = buildFundApplyTime(fund.getStartDate(), fund.getEndDate());
    return getFundShowDescByLocale(agencyName, scienceArea, showDate, form.getLocale());
  }

  private String buildscienceAreaNames(DynamicForm form) {
    String scienceArea = "";
    List<Long> idList = constFundCategoryDisDao.findFundDisciplineIds(form.getResId());
    if (CollectionUtils.isNotEmpty(idList)) {
      List<Integer> ids = new ArrayList<Integer>();
      for (Long id : idList) {
        ids.add(id.intValue());
      }
      StringBuffer scienceAreaNames = new StringBuffer();
      // 获取科技领域
      List<CategoryMapBase> list = categoryMapBaseDao.findCategoryByIds(ids);
      if (CollectionUtils.isNotEmpty(list)) {
        if ("zh_CN".equals(form.getLocale())) {
          for (CategoryMapBase ca : list) {
            String zhTitle = StringUtils.isNotBlank(ca.getCategoryZh()) ? ca.getCategoryZh() : ca.getCategoryEn();
            if (StringUtils.isBlank(scienceAreaNames.toString())) {
              scienceAreaNames.append(zhTitle);
            } else {
              scienceAreaNames.append("," + zhTitle);
            }
          }
        } else {
          for (CategoryMapBase ca : list) {
            String enTitle = StringUtils.isNotBlank(ca.getCategoryEn()) ? ca.getCategoryEn() : ca.getCategoryZh();
            if (StringUtils.isBlank(scienceAreaNames.toString())) {
              scienceAreaNames.append(enTitle);
            } else {
              scienceAreaNames.append("," + enTitle);
            }
          }
        }
        scienceArea = scienceAreaNames.toString();
      }
    }
    return scienceArea;
  }

  private String getFundShowDescByLocale(String fundAgency, String scienceArea, String applyTime, String locale) {
    String showDesc = "";
    String joinStr = "zh_CN".equals(locale) ? "，" : ", ";
    if (StringUtils.isNotBlank(fundAgency)) {
      showDesc += fundAgency;
    }
    if (StringUtils.isNotBlank(scienceArea)) {
      if (StringUtils.isNotBlank(showDesc)) {
        showDesc += joinStr + scienceArea;
      } else {
        showDesc += scienceArea;
      }
    }
    if (StringUtils.isNotBlank(applyTime)) {
      if (StringUtils.isNotBlank(showDesc)) {
        showDesc += joinStr + applyTime;
      } else {
        showDesc += applyTime;
      }
    }
    return showDesc;
  }

  private String buildFundApplyTime(Date startDate, Date endDate) {
    SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM-dd");
    String start = "";
    String end = "";
    if (startDate != null) {
      start = smf.format(startDate);
    }
    if (endDate != null) {
      end = smf.format(endDate);
    }
    if (StringUtils.isNotBlank(start) || StringUtils.isNotBlank(end)) {
      return start + " ~ " + end;
    } else {
      return start + end;
    }
  }

  private void buildPrjShareTxt(DynamicForm form) throws Exception {
    Project prj = projectDao.get(form.getResId());
    if (prj == null) {
      throw new Exception("分享到站外的资源不存在，resId=" + form.getResId() + ",resType=" + form.getResType());
    }
    Map<String, Object> map = form.getResultMap();
    String title = "";
    String source = "";
    String language = "";
    String resLink =
        domainscm + "/prjweb/outside/project/detailsshow?des3PrjId=" + URLEncoder.encode(form.getDes3ResId(), "utf-8");
    String mobileResLink =
        domainMobile + "/prjweb/wechat/findprjxml?des3PrjId=" + URLEncoder.encode(form.getDes3ResId(), "utf-8");
    String pic = domainscm + "/resmod/images_v5/images2016/file_img.jpg";
    if ("zh_CN".equals(form.getLocale())) {
      title = StringUtils.isBlank(prj.getZhTitle()) ? prj.getEnTitle() : prj.getZhTitle();
      source = StringUtils.isBlank(prj.getBriefDesc()) ? prj.getBriefDescEn() : prj.getBriefDesc();
      language = "zh";
    } else {
      title = StringUtils.isBlank(prj.getEnTitle()) ? prj.getZhTitle() : prj.getEnTitle();
      source = StringUtils.isBlank(prj.getBriefDescEn()) ? prj.getBriefDesc() : prj.getBriefDescEn();
      language = "en";
    }
    map.put("title", changeHtmlStr(title));
    map.put("resOther", changeHtmlStr(source));
    map.put("id", form.getResId());
    map.put("des3Id", form.getDes3ResId());
    map.put("authorNames", prj.getAuthorNames());
    map.put("language", language);
    map.put("resLink", resLink);
    map.put("mobileResLink", mobileResLink);
    map.put("pic", pic);
  }

  private void buildPubShareTxt(DynamicForm form) throws Exception {
    PubSnsPO pub = pubSnsDAO.get(form.getResId());
    if (pub == null) {
      throw new Exception("分享到站外的资源不存在，resId=" + form.getResId() + ",resType=" + form.getResType());
    }
    Map<String, Object> map = form.getResultMap();
    String title = "";
    String source = "";
    String language = "";
    String resLink = "";
    String pic = "";
    if ("zh_CN".equals(form.getLocale())) {
      title = pub.getTitle();
      source = pub.getBriefDesc();
      language = "zh";
    } else {
      title = pub.getTitle();
      source = pub.getBriefDesc();
      language = "en";
    }
    PubIndexUrl pubIndexUrl = pubIndexUrlDao.get(form.getResId());
    if (pubIndexUrl != null && StringUtils.isNotBlank(pubIndexUrl.getPubIndexUrl())) {
      resLink = domainscm + "/" + ShortUrlConst.B_TYPE + "/" + pubIndexUrl.getPubIndexUrl();
    } else {
      resLink = domainscm + "/pub/outside/details?des3PubId=" + form.getDes3ResId();
    }
    String mobileResLink =
        domainMobile + "/pub/outside/details/snsnonext?des3PubId=" + URLEncoder.encode(form.getDes3ResId(), "utf-8");
    PubFullTextPO fullText = pubSnsFullTextDAO.getPubFullTextByPubId(form.getResId());
    if (fullText != null && StringUtils.isNotBlank(fullText.getThumbnailPath())) {
      pic = domainscm + fullText.getThumbnailPath();
    } else {
      if (fullText != null) {
        pic = domainscm + "/resmod/images_v5/images2016/file_img1.jpg";
      } else {
        pic = domainscm + "/resmod/images_v5/images2016/file_img.jpg";
      }
    }
    map.put("title", changeHtmlStr(title));
    map.put("resOther", changeHtmlStr(source));
    map.put("id", form.getResId());
    map.put("des3Id", form.getDes3ResId());
    map.put("authorNames", changeHtmlStr(pub.getAuthorNames()));
    map.put("language", language);
    map.put("resLink", resLink);
    map.put("mobileResLink", mobileResLink);
    map.put("pic", pic);
  }

  private String changeHtmlStr(String str) {
    if (str == null) {
      return "";
    }
    str = checkHtml(str);
    // 进行数据中文字符的去除，把中文字符全部替换成英文字符加空格
    str = str.replaceAll("，", ", ").replaceAll("；", "; ");
    return HtmlUtils.htmlUnescape(str);
  }

  // 过滤html
  private String checkHtml(String title) {
    if (StringUtils.isBlank(title)) {
      return "";
    }
    Pattern pattern = Pattern.compile("<([^>]*)>");
    Matcher matcher = pattern.matcher(title);
    StringBuffer sb = new StringBuffer();
    boolean result1 = matcher.find();
    while (result1) {
      matcher.appendReplacement(sb, "");
      result1 = matcher.find();
    }
    matcher.appendTail(sb);
    return sb.toString();
  }

  private void checkParam(DynamicForm form) throws Exception {
    if (form.getResId() == null) {
      throw new Exception("resId资源主键为空");
    }
    if (form.getResType() == null) {
      throw new Exception("resType资源类型为空");
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public void buildDynPubRecommend(DynamicForm form, Map<String, Object> map) {
    List<DynamicRemdForm> dynRemdRes = new ArrayList<DynamicRemdForm>();
    try {
      if (map != null && map.get("status").equals("success")) {
        List<LinkedHashMap<String, Object>> resultList = (List<LinkedHashMap<String, Object>>) map.get("resultList");
        if (resultList != null && resultList.size() > 0) {
          for (LinkedHashMap<String, Object> pubMap : resultList) {
            DynamicRemdForm remd = new DynamicRemdForm();
            remd.setDes3ResId(String.valueOf(pubMap.get("des3PubId")));
            remd.setResId(pubMap.get("pubId") == null ? 0L : Long.parseLong(pubMap.get("pubId").toString()));
            remd.setResTitle(pubMap.get("title") == null ? "" : String.valueOf(pubMap.get("title")));
            remd.setResBriefDesc(pubMap.get("briefDesc") == null ? "" : String.valueOf(pubMap.get("briefDesc")));
            remd.setResAuthorNames(pubMap.get("authorNames") == null ? "" : String.valueOf(pubMap.get("authorNames")));
            remd.setHasFulltext(Integer.parseInt(String.valueOf(pubMap.get("hasFulltext"))));
            remd.setFullTextUrl(
                pubMap.get("fullTextImgUrl") == null ? "" : String.valueOf(pubMap.get("fullTextImgUrl")));
            remd.setFullTextDownloadUrl(
                pubMap.get("fullTextDownloadUrl") == null ? "" : String.valueOf(pubMap.get("fullTextDownloadUrl")));
            remd.setType(1);
            dynRemdRes.add(remd);
          }
        }

      }
    } catch (Exception e) {
      logger.error("构建首页推荐论文信息出错", e);
    }
    // form.setRemdForm(remd);
    form.setDynRemdResList(dynRemdRes);
  }

  @Override
  public int findResDeleteStatus(Integer resType, Long resId) throws DynException {
    int hasDeleted = 0; // 0:未删除， 1：已删除
    if (NumberUtils.isNotNullOrZero(resType) && NumberUtils.isNotNullOrZero(resId)) {
      switch (resType) {
        case 1:// 个人库成果
        case 2:
          PubSnsPO pub = pubSnsDAO.get(resId);
          if (pub == null
              || (pub.getStatus() != null && CommonUtils.compareIntegerValue(pub.getStatus().getValue(), 1))) {
            hasDeleted = 1;
          }
          break;
        case 22:// 基准库成果
        case 24:
          PubPdwhPO pdwhPub = pubPdwhDAO.get(resId);
          if (pdwhPub == null
              || (pdwhPub.getStatus() != null && CommonUtils.compareIntegerValue(pdwhPub.getStatus().getValue(), 1))) {
            hasDeleted = 1;
          }
          break;
        case 25:// 资助机构
          ConstFundAgency fundAgency = constFundAgencyDao.get(resId);
          if (fundAgency == null) {
            hasDeleted = 1;
          }
          break;
        case 11:// 基金
          ConstFundCategory fund = constFundCategoryDao.get(resId);
          if (fund == null) {
            hasDeleted = 1;
          }
          break;
        case 27:// 群组文件
          GrpFile grpFile = grpFileDao.get(resId);
          if (grpFile == null || CommonUtils.compareIntegerValue(1, grpFile.getFileStatus())) {
            hasDeleted = 1;
          }
          break;
        case 4:// 项目
          Project prj = projectDao.get(resId);
          if (prj == null || CommonUtils.compareIntegerValue(prj.getStatus(), 1)) {
            hasDeleted = 1;
          }
          break;
        default:
          break;
      }
    }
    return hasDeleted;
  }
}
