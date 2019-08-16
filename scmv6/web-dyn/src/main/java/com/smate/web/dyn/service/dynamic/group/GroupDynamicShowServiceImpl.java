package com.smate.web.dyn.service.dynamic.group;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.enums.LikeStatusEnum;
import com.smate.core.base.project.dao.ProjectDao;
import com.smate.core.base.project.model.Project;
import com.smate.core.base.pub.enums.PubDbEnum;
import com.smate.core.base.utils.common.CommonUtils;
import com.smate.core.base.utils.dynamicds.InspgDynamicUtil;
import com.smate.core.base.utils.exception.DynException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.dyn.dao.dynamic.group.GroupDynamicAwardsDao;
import com.smate.web.dyn.dao.dynamic.group.GroupDynamicCommentsDao;
import com.smate.web.dyn.dao.dynamic.group.GroupDynamicContentDao;
import com.smate.web.dyn.dao.dynamic.group.GroupDynamicMsgDao;
import com.smate.web.dyn.dao.dynamic.group.GroupDynamicStatisticDao;
import com.smate.web.dyn.dao.fund.ConstFundCategoryDao;
import com.smate.web.dyn.dao.fund.FundAgencyDao;
import com.smate.web.dyn.dao.fund.FundAgencyInterestDao;
import com.smate.web.dyn.dao.fund.MyFundDao;
import com.smate.web.dyn.dao.grp.GrpFileDao;
import com.smate.web.dyn.dao.pdwhpub.PdwhPubFullTextDAO;
import com.smate.web.dyn.dao.pdwhpub.PubPdwhDAO;
import com.smate.web.dyn.dao.pub.CollectedPubDao;
import com.smate.web.dyn.dao.pub.PubFullTextReqBaseDao;
import com.smate.web.dyn.dao.pub.PubSnsDAO;
import com.smate.web.dyn.dao.pub.PubSnsFullTextDAO;
import com.smate.web.dyn.exception.DynGroupException;
import com.smate.web.dyn.form.dynamic.group.GroupDynCommentsShowInfo;
import com.smate.web.dyn.form.dynamic.group.GroupDynShowForm;
import com.smate.web.dyn.form.dynamic.group.GroupDynShowInfo;
import com.smate.web.dyn.model.dynamic.group.GroupDynamicComments;
import com.smate.web.dyn.model.dynamic.group.GroupDynamicMsg;
import com.smate.web.dyn.model.dynamic.group.GroupDynamicStatistic;
import com.smate.web.dyn.model.fund.ConstFundAgency;
import com.smate.web.dyn.model.fund.ConstFundCategory;
import com.smate.web.dyn.model.grp.GrpFile;
import com.smate.web.dyn.model.pub.PdwhPubFullTextPO;
import com.smate.web.dyn.model.pub.PubFullTextPO;
import com.smate.web.dyn.model.pub.PubPdwhPO;
import com.smate.web.dyn.model.pub.PubSnsPO;
import com.smate.web.dyn.service.psn.PersonQueryservice;

/**
 * 群组动态显示服务实现类
 * 
 * @author zzx
 *
 */
@Service("GroupDynamicShowService")
@Transactional(rollbackFor = Exception.class)
public class GroupDynamicShowServiceImpl implements GroupDynamicShowService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private GroupDynamicMsgDao groupDynamicMsgDao;
  @Autowired
  private GroupDynamicContentDao groupDynamicContentDao;
  @Autowired
  private GroupDynamicCommentsDao groupDynamicCommentsDao;
  @Autowired
  private GroupDynamicStatisticDao groupDynamicStatisticDao;
  @Autowired
  private PersonQueryservice personQueryservice;
  @Autowired
  private GroupDynamicAwardsDao groupDynamicAwardsDao;
  @Autowired
  private CollectedPubDao collectedPubDao;
  @Autowired
  private PubSnsDAO pubSnsDAO;
  @Autowired
  private PubSnsFullTextDAO pubSnsFullTextDAO;
  @Autowired
  private PubFullTextReqBaseDao pubFullTextReqBaseDao;
  @Autowired
  private FundAgencyInterestDao fundAgencyInterestDao;
  @Autowired
  private MyFundDao myFundDao;
  @Autowired
  private PdwhPubFullTextDAO pdwhPubFullTextDAO;
  @Autowired
  private PubPdwhDAO pubPdwhDAO;
  @Autowired
  private FundAgencyDao fundAgencyDao;
  @Autowired
  private ConstFundCategoryDao constFundCategoryDao;
  @Autowired
  private GrpFileDao grpFileDao;
  @Autowired
  private ProjectDao projectDao;

  /**
   * zzx 获取群组动态列表
   */
  @Override
  public void buildGroupDynList(GroupDynShowForm form) throws DynGroupException {
    // 获取动态列表
    List<GroupDynamicMsg> list = groupDynamicMsgDao.getGroupDyn(form);
    if (list != null && list.size() > 0) {
      Long userId = SecurityUtils.getCurrentUserId();
      boolean admin = groupDynamicMsgDao.isAdmin(userId, form.getGroupId());
      ArrayList<GroupDynShowInfo> groupDynShowInfoList = new ArrayList<GroupDynShowInfo>();
      for (GroupDynamicMsg gdm : list) {// 遍历列表
        groupDynShowInfoList.add(buildDynInfo(gdm, form, admin));
      }
      form.setGroupDynShowInfoList(groupDynShowInfoList);
    }
  }


  @Override
  public void buildGrpDynListJsonInfo(GroupDynShowForm form) throws DynGroupException {
    try {
      // 获取动态列表
      List<GroupDynamicMsg> list = groupDynamicMsgDao.getGroupDyn(form);
      if (CollectionUtils.isNotEmpty(list)) {
        Long userId = SecurityUtils.getCurrentUserId();
        boolean admin = groupDynamicMsgDao.isAdmin(userId, form.getGroupId());
        ArrayList<GroupDynShowInfo> groupDynShowInfoList = new ArrayList<GroupDynShowInfo>();
        for (GroupDynamicMsg gdm : list) {// 遍历列表
          if (form.getShowJsonDynInfo()) {
            groupDynShowInfoList.add(buildDynJsonInfo(gdm, form, admin));
          } else {
            groupDynShowInfoList.add(buildDynInfo(gdm, form, admin));
          }
        }
        form.setGroupDynShowInfoList(groupDynShowInfoList);
      }
    } catch (Exception e) {
      logger.error("构建群组动态列表json信息异常, des3GrpId={}", form.getDes3GroupId(), e);
      throw new DynGroupException(e);
    }
  }

  /**
   * 获取单个群组动态信息
   * 
   * @throws Exception
   */
  @Override
  public void buildGrpDynDetailInfo(GroupDynShowForm form) throws Exception {
    GroupDynamicMsg gdm = groupDynamicMsgDao.getGroupDynDetails(form.getGroupId(), form.getDynId(), form.getShowNew());
    if (gdm != null) {
      boolean admin = groupDynamicMsgDao.isAdmin(form.getCurrentPsnId(), form.getGroupId());
      if (form.getShowJsonDynInfo()) {
        form.setGrpDynShowInfo(buildDynJsonInfo(gdm, form, admin));
      } else {
        form.setGrpDynShowInfo(buildDynInfo(gdm, form, admin));
      }
    }
  }


  private GroupDynShowInfo buildDynInfo(GroupDynamicMsg gdm, GroupDynShowForm form, boolean admin) {
    GroupDynShowInfo gsf = new GroupDynShowInfo();
    // 获取动态统计数
    GroupDynamicStatistic gds = groupDynamicStatisticDao.get(gdm.getDynId());
    // 获取动态内容
    form.setLocale(LocaleContextHolder.getLocale().toString());
    gsf.setDynContent(groupDynamicContentDao.getDynContent(gdm.getDynId(), form.getLocale()));
    // 构建动态时间
    if (gdm.getCreateDate() != null) {
      if ("en_US".equals(form.getLocale())) {
        gsf.setDynDateForShow(InspgDynamicUtil.formatDateUS(gdm.getCreateDate()));
      } else {
        gsf.setDynDateForShow(InspgDynamicUtil.formatDate(gdm.getCreateDate()));
      }
    }
    try {// 同步数据到群组动态显示类GroupDynShowInfo
      BeanUtils.copyProperties(gsf, gdm);
      if (gds != null) {
        BeanUtils.copyProperties(gsf, gds);
      }
    } catch (Exception e) {
      logger.error("获取群组动态数据合并到GroupDynShowInfo出错,dynId=" + gdm.getDynId(), e);
    }
    LikeStatusEnum status = groupDynamicAwardsDao.getLikeStatusEnum(gdm.getDynId(), form.getCurrentPsnId());
    if (status == null || status == LikeStatusEnum.UNLIKE) {
      gsf.setAwardstatus(0);
    } else {
      gsf.setAwardstatus(1);
    }
    if (admin) {
      gsf.setIsCanDel(1);
    } else if (CommonUtils.compareLongValue(form.getCurrentPsnId(), gdm.getProducer())) {
      gsf.setIsCanDel(1);
    } else {
      gsf.setIsCanDel(0);
    }
    if (gsf.getResId() != null) {
      gsf.setDes3ResId(Des3Utils.encodeToDes3(gsf.getResId().toString()));
    }
    // 是否收藏成果
    dealCollectionPub(form, gsf);
    // 是否关注过资助机构
    dealAgencyInterestStatus(form, gsf);
    // 是否收藏过基金
    dealFundCollectStatus(form, gsf);
    // 获取动态中成果全文的改变情况，上传了全文的，进行图片和地址的增加，下载的权限
    updateFullTextPub(gdm, gsf);
    return gsf;
  }


  /**
   * 获取json格式动态信息
   * 
   * @param gdm
   * @param form
   * @param admin
   * @return
   * @throws Exception
   */
  private GroupDynShowInfo buildDynJsonInfo(GroupDynamicMsg gdm, GroupDynShowForm form, boolean admin)
      throws Exception {
    GroupDynShowInfo gsf = new GroupDynShowInfo();
    // 获取动态统计数
    GroupDynamicStatistic gds = groupDynamicStatisticDao.get(gdm.getDynId());
    // 获取动态内容
    String jsonDynInfo = groupDynamicContentDao.getJsonDynInfo(gdm.getDynId());
    if (StringUtils.isNotBlank(jsonDynInfo)) {
      gsf.setJsonDynInfo(JacksonUtils.jsonToMap2(jsonDynInfo));
    }
    // 构建动态时间
    if (gdm.getCreateDate() != null) {
      gsf.setDynDateForShow(InspgDynamicUtil.formatDate(gdm.getCreateDate()));
    }
    try {// 同步数据到群组动态显示类GroupDynShowInfo
      BeanUtils.copyProperties(gsf, gdm);
      if (gds != null) {
        BeanUtils.copyProperties(gsf, gds);
      }
    } catch (Exception e) {
      logger.error("获取群组动态数据合并到GroupDynShowInfo出错,dynId=" + gdm.getDynId(), e);
    }
    // 对动态赞的状态
    LikeStatusEnum status = groupDynamicAwardsDao.getLikeStatusEnum(gdm.getDynId(), form.getCurrentPsnId());
    gsf.setAwardstatus((status == null || status == LikeStatusEnum.UNLIKE) ? 0 : 1);
    // 是否有删除动态权限
    gsf.setIsCanDel((admin || CommonUtils.compareLongValue(form.getCurrentPsnId(), gdm.getProducer())) ? 1 : 0);
    // 资源的删除状态
    dealWithResStatus(gdm, gsf);
    // 是否收藏成果
    dealCollectionPub(form, gsf);
    // 是否关注过资助机构
    dealAgencyInterestStatus(form, gsf);
    // 是否收藏过基金
    dealFundCollectStatus(form, gsf);
    // 获取动态中成果全文的改变情况，上传了全文的，进行图片和地址的增加，下载的权限
    updateFullTextPub(gdm, gsf);
    return gsf;
  }



  /**
   * 获取动态中成果全文的改变情况，上传了全文的，进行图片和地址的增加
   * 
   * @param gdm
   * @param gsf
   */
  private void updateFullTextPub(GroupDynamicMsg gdm, GroupDynShowInfo gsf) {
    if ("pub".equalsIgnoreCase(gdm.getResType()) && NumberUtils.isNotNullOrZero(gdm.getResId())) {
      PubSnsPO pub = pubSnsDAO.get(gdm.getResId());
      PubFullTextPO fullText = pubSnsFullTextDAO.getPubFullTextByPubId(gdm.getResId());
      // 全文图片
      if (pub != null && fullText != null && StringUtils.isNoneBlank(String.valueOf(fullText.getFileId()))) {
        gsf.setResFullTextFileId(fullText.getFileId().toString());
        gsf.setResFullTextImage(
            StringUtils.defaultIfBlank(fullText.getThumbnailPath(), "/resmod/images_v5/images2016/file_img1.jpg"));
        // 全文下载权限，0所有人可下载，1好友可下载，2自己可下载
        // 如果是隐私全文，则判断是否同意全文请求
        Long userId = SecurityUtils.getCurrentUserId();
        if (fullText.getPermission() == 0) {
          gsf.setResPremission(fullText.getPermission());
        } else {
          boolean agree = pubFullTextReqBaseDao.isFullTextReqAgree(pub.getPubId(), userId, PubDbEnum.SNS);
          gsf.setResPremission(agree ? 0 : fullText.getPermission());
        }
      } else {
        gsf.setResFullTextImage("/resmod/images_v5/images2016/file_img.jpg");
      }
    } else if ("pdwhpub".equalsIgnoreCase(gdm.getResType()) && NumberUtils.isNotNullOrZero(gdm.getResId())) {
      dealWithPdwhpubFulltext(gdm, gsf);
    }
  }

  /**
   * 处理基准库成果全文信息
   * 
   * @param gdm
   * @param gsf
   */
  private void dealWithPdwhpubFulltext(GroupDynamicMsg gdm, GroupDynShowInfo gsf) {
    PubPdwhPO pdwhPub = pubPdwhDAO.get(gdm.getResId());
    PdwhPubFullTextPO fulltext = pdwhPubFullTextDAO.getFullTextByPubId(gdm.getResId());
    if (pdwhPub != null && fulltext != null && StringUtils.isNotBlank(String.valueOf(fulltext.getFileId()))) {
      gsf.setResFullTextFileId(fulltext.getFileId().toString());
      gsf.setResFullTextImage(
          StringUtils.defaultIfBlank(fulltext.getThumbnailPath(), "/resmod/images_v5/images2016/file_img1.jpg"));
      gsf.setResPremission(0);
    } else {
      gsf.setResFullTextImage("/resmod/images_v5/images2016/file_img.jpg");
    }
  }

  /**
   * 处理资源的删除状态
   */
  private void dealWithResStatus(GroupDynamicMsg gdm, GroupDynShowInfo gsf) {
    if (NumberUtils.isNotNullOrZero(gdm.getResId()) && StringUtils.isNotBlank(gdm.getResType())) {
      switch (gdm.getResType()) {
        case "pub":
          PubSnsPO pub = pubSnsDAO.get(gdm.getResId());
          if (pub == null
              || (pub.getStatus() != null && CommonUtils.compareIntegerValue(pub.getStatus().getValue(), 1))) {
            gsf.setResNotExists(1);
          }
          break;
        case "pdwhpub":
          PubPdwhPO pdwhPub = pubPdwhDAO.get(gdm.getResId());
          if (pdwhPub == null
              || (pdwhPub.getStatus() != null && CommonUtils.compareIntegerValue(pdwhPub.getStatus().getValue(), 1))) {
            gsf.setResNotExists(1);
          }
          break;
        case "agency":
          ConstFundAgency fundAgency = fundAgencyDao.get(gdm.getResId());
          if (fundAgency == null) {
            gsf.setResNotExists(1);
          }
          break;
        case "fund":
          ConstFundCategory fund = constFundCategoryDao.get(gdm.getResId());
          if (fund == null) {
            gsf.setResNotExists(1);
          }
          break;
        case "grpfile":
          GrpFile grpFile = grpFileDao.get(gdm.getResId());
          if (grpFile == null || CommonUtils.compareIntegerValue(1, grpFile.getFileStatus())) {
            gsf.setResNotExists(1);
          }
          break;
        case "prj":
          Project prj = projectDao.get(gdm.getResId());
          if (prj == null || CommonUtils.compareIntegerValue(prj.getStatus(), 1)) {
            gsf.setResNotExists(1);
          }
          break;
        default:
          break;
      }
      if (gsf.getJsonDynInfo() != null) {
        gsf.getJsonDynInfo().put("RES_NOT_EXISTS", gsf.getResNotExists() == 1 ? true : false);
      }
    }
  }

  private void dealCollectionPub(GroupDynShowForm form, GroupDynShowInfo gsf) {
    PubDbEnum pubDb = null;
    if ("pdwhpub".equalsIgnoreCase(gsf.getResType())) {
      pubDb = PubDbEnum.PDWH;
    } else if ("pub".equalsIgnoreCase(gsf.getResType())) {
      pubDb = PubDbEnum.SNS;
    }
    if (pubDb != null) {
      boolean hasCollenciton = collectedPubDao.isCollectedPub(form.getCurrentPsnId(), gsf.getResId(), pubDb);
      gsf.setHasCollenciton(hasCollenciton);
    }
  }

  /**
   * 是否关注过资助机构
   * 
   * @param form
   * @param gsf
   */
  private void dealAgencyInterestStatus(GroupDynShowForm form, GroupDynShowInfo gsf) {
    if ("agency".equalsIgnoreCase(gsf.getResType())) {
      gsf.setHasCollenciton(fundAgencyInterestDao.hasCollectedAgency(form.getCurrentPsnId(), gsf.getResId()));
    }
  }


  private void dealFundCollectStatus(GroupDynShowForm form, GroupDynShowInfo gsf) {
    if ("fund".equalsIgnoreCase(gsf.getResType())) {
      gsf.setHasCollenciton(myFundDao.hasCollectedFund(form.getCurrentPsnId(), gsf.getResId()));
    }
  }

  /**
   * zzx 获取动态评论列表
   */
  @Override
  public void getGroupDynCommentList(GroupDynShowForm form) throws DynGroupException {
    // 获取动态评论列表
    List<GroupDynamicComments> groupDynContent = null;
    if (form.getMaxResults() == 1) {
      groupDynContent = groupDynamicCommentsDao.getGroupDynContent(form.getDynId());
    } else {
      groupDynContent = groupDynamicCommentsDao.getGroupDynContents(form);
    }
    if (groupDynContent != null && groupDynContent.size() > 0) {
      ArrayList<GroupDynCommentsShowInfo> groupDynShowInfoList = new ArrayList<GroupDynCommentsShowInfo>();
      for (GroupDynamicComments g : groupDynContent) {// 遍历列表
        GroupDynCommentsShowInfo commentsShowInfo = new GroupDynCommentsShowInfo();
        Person person = null;
        try {// 获取评论人员的基本信息
          person = personQueryservice.findPersonBase(g.getCommentPsnId());
          person.setName(getPersonZhName(person));
        } catch (DynException e) {
          logger.error(
              "获取群组动态评论人的信息出错,dynId=" + form.getDynId() + ";评论id=" + g.getId() + ";psnId=" + g.getCommentPsnId(), e);
        }
        try {// 同步数据到群组动态评论显示类GroupDynCommentsShowInfo
          g.setCommentContent(g.getCommentContent() == null ? "" : g.getCommentContent().replaceAll("\n", "<br/>"));
          BeanUtils.copyProperties(commentsShowInfo, g);
          if (person != null) {
            BeanUtils.copyProperties(commentsShowInfo, person);
            commentsShowInfo.setDes3PersonId(Des3Utils.encodeToDes3(person.getPersonId().toString()));
          }
        } catch (Exception e) {
          logger.error("获取群组动态评论数据合并到GroupDynCommentsShowInfo出错,dynId=" + form.getDynId() + ";评论id=" + g.getId(), e);
        }
        // 构建评论时间
        form.setLocale(LocaleContextHolder.getLocale().toString());
        if (commentsShowInfo.getCommentDate() != null) {
          if ("en_US".equals(form.getLocale())) {
            commentsShowInfo.setCommentDateForShow(InspgDynamicUtil.formatDateUS(commentsShowInfo.getCommentDate()));
          } else {
            commentsShowInfo.setCommentDateForShow(InspgDynamicUtil.formatDate(commentsShowInfo.getCommentDate()));
          }
        }
        // 构建评论资源
        if (g.getCommentResId() != null) {
          PubSnsPO pub = pubSnsDAO.get(g.getCommentResId());
          if (pub != null) {
            commentsShowInfo.setCommentResId(pub.getPubId());
            commentsShowInfo.setCommentResEnTitle(pub.getTitle());
            commentsShowInfo.setCommentResZhTitle(pub.getTitle());

          }
        }

        groupDynShowInfoList.add(commentsShowInfo);
      }
      form.setGroupDynCommentsShowInfoList(groupDynShowInfoList);
    }
  }

  /**
   * 优先显示中文名，没有中文名显示 firstname + “ ” +lastname 还是没有的话取enName
   * 
   * @param person
   * @return
   */
  private String getPersonZhName(Person person) {
    if (StringUtils.isNotBlank(person.getName()))
      return person.getName();
    else if (StringUtils.isNotBlank(person.getLastName()) || StringUtils.isNotBlank(person.getFirstName()))
      return person.getFirstName() + " " + person.getLastName();
    else
      return person.getEname();
  }

  @Override
  public void delGrpDyn(GroupDynShowForm form) throws DynGroupException {
    Long userId = SecurityUtils.getCurrentUserId();
    if (userId != null && userId != 0L) {
      boolean admin = groupDynamicMsgDao.isAdmin(userId, form.getGroupId());
      GroupDynamicMsg dynamicMsg = groupDynamicMsgDao.get(form.getDynId());
      if (dynamicMsg != null) {
        if (admin) {
          if (form.getGroupId().equals(dynamicMsg.getGroupId())) {
            dynamicMsg.setStatus(99);
          }
        } else {
          if (form.getGroupId().equals(dynamicMsg.getGroupId()) && userId.equals(dynamicMsg.getProducer())) {
            dynamicMsg.setStatus(99);
          }
        }
      }
    }
    // TODO Auto-generated method stub

  }

}
