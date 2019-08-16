package com.smate.center.open.service.msg;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.consts.V8pubQueryPubConst;
import com.smate.center.open.dao.fund.ConstFundAgencyDao;
import com.smate.center.open.dao.fund.ConstFundCategoryDao;
import com.smate.center.open.dao.grp.GrpFileDao;
import com.smate.center.open.dao.msg.MsgChatRelationDao;
import com.smate.center.open.dao.msg.MsgContentDao;
import com.smate.center.open.dao.msg.MsgRelationDao;
import com.smate.center.open.dao.news.NewsBaseDao;
import com.smate.center.open.model.fund.ConstFundAgency;
import com.smate.center.open.model.fund.ConstFundCategory;
import com.smate.center.open.model.grp.GrpFile;
import com.smate.center.open.model.msg.MsgChatRelation;
import com.smate.center.open.model.msg.MsgContent;
import com.smate.center.open.model.msg.MsgRelation;
import com.smate.center.open.model.news.NewsBase;
import com.smate.core.base.project.dao.ProjectDao;
import com.smate.core.base.project.model.Project;
import com.smate.core.base.psn.dao.PsnFileDao;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.model.PsnFile;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.pub.dto.PubQueryDTO;
import com.smate.core.base.utils.constant.MsgConstants;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.file.ArchiveFileUtil;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;

/**
 * 站内信服务实现类
 * 
 * @author ajb
 *
 */
@Transactional(rollbackFor = Exception.class)
public class CreateSmateInsideLetterMsgServiceImpl extends CreateMsgBase {
  @Autowired
  private MsgContentDao msgContentDao;
  @Autowired
  private MsgRelationDao msgRelationDao;
  @Autowired
  private MsgChatRelationDao msgChatRelationDao;
  @Autowired
  private GrpFileDao grpFileDao;
  @Autowired
  private PsnFileDao psnFileDao;
  @Autowired
  private ConstFundCategoryDao constFundCategoryDao;
  @Autowired
  private ConstFundAgencyDao constFundAgencyDao;
  @Autowired
  private ProjectDao projectDao;
  @Autowired
  private NewsBaseDao newsBaseDao;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;

  @Override
  public Map<String, Object> parameterVeify(Map<String, Object> paramet) throws Exception {
    Map<String, Object> result = new HashMap<String, Object>();
    Object letterType = paramet.get(MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE);
    Object content = paramet.get(MsgConstants.MSG_CONTENT);
    Object pubIdObj = paramet.get(MsgConstants.MSG_PUB_ID);
    Object grpPubIdObj = paramet.get(MsgConstants.MSG_GRP_PUB_ID);
    Object grpIdObj = paramet.get(MsgConstants.MSG_GRP_ID);
    Object des3PrjIdObj = paramet.get(MsgConstants.MSG_DES3_PRJ_ID);

    Object des3NewsIdObj = paramet.get(MsgConstants.MSG_DES3_NEWS_ID);
    Object des3PsnIdObj = paramet.get(MsgConstants.MSG_DES3_PSN_ID);
    Object des3InsIdObj = paramet.get(MsgConstants.MSG_DES3_INS_ID);

    Object fileIdObj = paramet.get(MsgConstants.MSG_FILE_ID);
    Object grpfileIdObj = paramet.get(MsgConstants.MSG_GRP_FILE_ID);

    Object fundIdObj = paramet.get(MsgConstants.MSG_FUND_ID);
    Object agencyIdObj = paramet.get(MsgConstants.MSG_AGENCY_ID);
    Object belongPerson = paramet.get(MsgConstants.MSG_BELONG_PERSON);

    if (letterType == null || StringUtils.isBlank(letterType.toString())) {
      result = super.errorMap(OpenMsgCodeConsts.SCM_709, paramet, "smateInsideLetterType不能为空！");
      return result;
    }

    if (MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE_TEXT.equals(letterType)) {
      if (content == null || StringUtils.isBlank(content.toString())) {
        result = super.errorMap(OpenMsgCodeConsts.SCM_710, paramet, "content不能为空！");
      }
    } else if (MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE_FILE.equals(letterType)) {

      if (belongPerson == null || BooleanUtils.toBooleanObject(belongPerson.toString()) == null) {
        result = super.errorMap(OpenMsgCodeConsts.SCM_711, paramet, "fileId or  grpFileId不能为空,且为数字,belongPerson必须为布尔值");
      }
      if (BooleanUtils.toBoolean(belongPerson.toString())) {
        if (fileIdObj == null || !NumberUtils.isNumber(fileIdObj.toString())) {
          result =
              super.errorMap(OpenMsgCodeConsts.SCM_711, paramet, "fileId or  grpFileId不能为空,且为数字,belongPerson必须为布尔值");
        }
      } else {
        if (grpfileIdObj == null || !NumberUtils.isNumber(grpfileIdObj.toString())) {
          result =
              super.errorMap(OpenMsgCodeConsts.SCM_711, paramet, "fileId or  grpFileId不能为空,且为数字,belongPerson必须为布尔值");
        }
      }

    } else if (MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE_PUB.equals(letterType)) {
      if (belongPerson == null || BooleanUtils.toBooleanObject(belongPerson.toString()) == null) {
        result = super.errorMap(OpenMsgCodeConsts.SCM_712, paramet, "pubId  or grpPubId不能为空,且为数字 ,belongPerson必须为布尔值");
      }
      if (BooleanUtils.toBoolean(belongPerson.toString())) {
        if (pubIdObj == null || !NumberUtils.isNumber(pubIdObj.toString())) {
          result =
              super.errorMap(OpenMsgCodeConsts.SCM_712, paramet, "pubId  or grpPubId不能为空,且为数字 ,belongPerson必须为布尔值");
        }
      } else {
        if (grpPubIdObj == null || !NumberUtils.isNumber(grpPubIdObj.toString())) {
          result =
              super.errorMap(OpenMsgCodeConsts.SCM_712, paramet, "pubId  or grpPubId不能为空,且为数字 ,belongPerson必须为布尔值");
        }
      }
    } else if (MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE_PDWH_PUB.equals(letterType)) {
      if (pubIdObj == null || !NumberUtils.isNumber(pubIdObj.toString())) {
        result = super.errorMap(OpenMsgCodeConsts.SCM_712, paramet, "pubId 不能为空,且为数字");
      }
    } else if (MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE_FUND.equals(letterType)) {
      if (fundIdObj == null || !NumberUtils.isNumber(fundIdObj.toString())) {
        result = super.errorMap(OpenMsgCodeConsts.SCM_712, paramet, "fundId 不能为空,且为数字");
      }
    } else if (MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE_AGENCY.equals(letterType)) {
      if (agencyIdObj == null || !NumberUtils.isNumber(agencyIdObj.toString())) {
        result = super.errorMap(OpenMsgCodeConsts.SCM_712, paramet, "agencyId 不能为空,且为数字");
      }
    } else if (MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE_FULLTEXT.equals(letterType)) {
      if (pubIdObj == null || !NumberUtils.isNumber(pubIdObj.toString())) {
        result = super.errorMap(OpenMsgCodeConsts.SCM_712, paramet, "pubIdObj 不能为空,且为数字");
      }
    } else if (MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE_PRJ.equals(letterType)) {
      if (des3PrjIdObj == null) {
        result = super.errorMap(OpenMsgCodeConsts.SCM_712, paramet, "des3PrjIdObj 不能为空");
      }
    } else if (MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE_NEWS.equals(letterType)) {
      if (des3NewsIdObj == null) {
        result = super.errorMap(OpenMsgCodeConsts.SCM_724, paramet, "des3NewsIdObj 不能为空");
      }
    } else if (MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE_PSN.equals(letterType)) {
      if (des3PsnIdObj == null) {
        result = super.errorMap(OpenMsgCodeConsts.SCM_724, paramet, "des3PsnIdObj 不能为空");
      }
    } else if (MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE_INS.equals(letterType)) {
      if (des3InsIdObj == null) {
        result = super.errorMap(OpenMsgCodeConsts.SCM_727, paramet, "des3InsIdObj 不能为空");
      }
    } else {
      result = super.errorMap(OpenMsgCodeConsts.SCM_709, paramet, "smateInsideLetterType不能为空， 或者类型错误！");
    }

    if (!MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE_TEXT.equals(letterType) && belongPerson != null
        && !BooleanUtils.toBoolean(belongPerson.toString())) {
      if (grpIdObj == null || !NumberUtils.isNumber(grpIdObj.toString())) {
        result = super.errorMap(OpenMsgCodeConsts.SCM_713, paramet, "grpId不能为空,且为数字！");
      }
    }
    return result;
  }

  @Override
  public void buildMsg(Map<String, Object> dateMap, Long senderId, String receiverIds) throws Exception {
    Long contentId = msgContentDao.getContentId();
    MsgContent mc = new MsgContent();
    mc.setContentId(contentId);
    Map<String, Object> contentMap = new HashMap<String, Object>();
    contentMap.put(MsgConstants.MSG_SENDER_ID, senderId);
    contentMap.put(MsgConstants.MSG_RECEIVER_IDS, receiverIds);
    String type = dateMap.get(MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE).toString();

    contentMap.put(MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE, type);
    if (MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE_TEXT.equals(type)) {
      contentMap.put(MsgConstants.MSG_CONTENT,
          dateMap.get(MsgConstants.MSG_CONTENT) == null ? "" : dateMap.get(MsgConstants.MSG_CONTENT));
    } else {
      contentMap.put(MsgConstants.MSG_CONTENT, "");
    }
    if (MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE_FILE.equals(type)) {
      buildFileInfo(dateMap, contentMap);
    } else if (MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE_PUB.equals(type)) {
      // 构建成果信息
      buildPublicationInfo(dateMap, contentMap);
    } else if (MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE_PDWH_PUB.equals(type)) {
      buildPdwhPubInfo(dateMap, contentMap);
    } else if (MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE_FUND.equals(type)) {
      buildFundInfo(dateMap, contentMap);
    } else if (MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE_AGENCY.equals(type)) {
      buildAgencyInfo(dateMap, contentMap);
    } else if (MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE_FULLTEXT.equals(type)) {
      buildFulltextInfo(dateMap, contentMap);
    } else if (MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE_PRJ.equals(type)) {
      buildPrjInfo(dateMap, contentMap);
    } else if (MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE_NEWS.equals(type)) {
      buildNewsInfo(dateMap, contentMap);
    } else if (MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE_PSN.equals(type)) {
      buildPsnInfo(dateMap, contentMap);
    } else if (MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE_INS.equals(type)) {
      buildInsInfo(dateMap, contentMap);
    }

    if (dateMap.get(MsgConstants.MSG_CONTENT_NEWEST) == null || dateMap.get(MsgConstants.MSG_CONTENT_NEWEST) == "") {
      dateMap.put(MsgConstants.MSG_CONTENT_NEWEST,
          dateMap.get(MsgConstants.MSG_CONTENT) == null ? "" : dateMap.get(MsgConstants.MSG_CONTENT));
    }
    if (dateMap.get(MsgConstants.MSG_CONTENT_NEWEST) == null || dateMap.get(MsgConstants.MSG_CONTENT_NEWEST) == "") {
      dateMap.put(MsgConstants.MSG_CONTENT_NEWEST, contentMap.get(MsgConstants.MSG_CONTENT_NEWEST) == null ? ""
          : contentMap.get(MsgConstants.MSG_CONTENT_NEWEST));
    }

    mc.setContent(JacksonUtils.mapToJsonStr(contentMap));
    msgContentDao.save(mc);
    dateMap.put(MsgConstants.MSG_CONTENT_ID, contentId);
  }

  private void buildPsnInfo(Map<String, Object> dateMap, Map<String, Object> contentMap) {
    String des3PsnId = dateMap.get(MsgConstants.MSG_DES3_PSN_ID).toString();
    Long id = Long.parseLong(Des3Utils.decodeFromDes3(des3PsnId));
    PsnProfileUrl psnProfileUrl = psnProfileUrlDao.get(id);
    if (psnProfileUrl != null) {
      String url = domainscm + "/" + ShortUrlConst.P_TYPE + "/" + psnProfileUrl.getPsnIndexUrl();
      contentMap.put(MsgConstants.MSG_PSN_PROFILE_URL, url);
      contentMap.put(MsgConstants.MSG_DES3_PSN_ID, des3PsnId);
      dateMap.put(MsgConstants.MSG_CONTENT_NEWEST, url);
    }

  }

  private void buildInsInfo(Map<String, Object> dateMap, Map<String, Object> contentMap) {
    String des3InsId = dateMap.get(MsgConstants.MSG_DES3_INS_ID).toString();
    String url = String.valueOf(dateMap.get(MsgConstants.MSG_CONTENT));
    contentMap.put(MsgConstants.MSG_INS_HOME_URL, url);
    contentMap.put(MsgConstants.MSG_DES3_INS_ID, des3InsId);
    dateMap.put(MsgConstants.MSG_CONTENT_NEWEST, url);

  }


  private void buildNewsInfo(Map<String, Object> dateMap, Map<String, Object> contentMap) {
    String des3NewsId = dateMap.get(MsgConstants.MSG_DES3_NEWS_ID).toString();
    Long id = Long.parseLong(Des3Utils.decodeFromDes3(des3NewsId));
    NewsBase base = newsBaseDao.get(id);
    if (base != null) {
      String title = base.getTitle();
      String brief = StringUtils.isNotBlank(base.getBrief()) ? base.getBrief() : "";
      contentMap.put(MsgConstants.MSG_NEWS_TITLE, title);
      contentMap.put(MsgConstants.MSG_NEWS_BRIEF, brief);
      contentMap.put(MsgConstants.MSG_NEWS_URL, domainscm + "/dynweb/news/details?des3NewsId=" + des3NewsId);
      contentMap.put(MsgConstants.MSG_NEWS_IMG,
          StringUtils.isNotBlank(base.getImage()) ? base.getImage() : "/resmod/smate-pc/img/logo_newsdefault.png");
      contentMap.put(MsgConstants.MSG_DES3_NEWS_ID, des3NewsId);
      dateMap.put(MsgConstants.MSG_CONTENT_NEWEST, title);
    }

  }

  private void buildPrjInfo(Map<String, Object> dateMap, Map<String, Object> contentMap) {
    String des3PrjId = dateMap.get(MsgConstants.MSG_DES3_PRJ_ID).toString();
    Long prjId = Long.parseLong(Des3Utils.decodeFromDes3(des3PrjId));
    Project prj = projectDao.get(prjId);
    if (prj != null) {
      String zhTitle = StringUtils.isNotBlank(prj.getZhTitle()) ? prj.getZhTitle() : prj.getEnTitle();
      String enTitle = StringUtils.isNotBlank(prj.getEnTitle()) ? prj.getEnTitle() : prj.getZhTitle();
      String zhDescr = StringUtils.isNotBlank(prj.getBriefDesc()) ? prj.getBriefDesc() : prj.getBriefDescEn();
      String enDescr = StringUtils.isNotBlank(prj.getBriefDescEn()) ? prj.getBriefDescEn() : prj.getBriefDesc();
      contentMap.put(MsgConstants.MSG_PRJ_TITLE_ZH, zhTitle);
      contentMap.put(MsgConstants.MSG_PRJ_TITLE_EN, enTitle);
      contentMap.put(MsgConstants.MSG_PRJ_AUTHOR_NAME_ZH, prj.getAuthorNames());
      contentMap.put(MsgConstants.MSG_PRJ_AUTHOR_NAME_EN, prj.getAuthorNamesEn());
      contentMap.put(MsgConstants.MSG_PRJ_BRIEF_DESC_ZH, zhDescr);
      contentMap.put(MsgConstants.MSG_PRJ_BRIEF_DESC_EN, enDescr);
      contentMap.put(MsgConstants.MSG_PRJ_URL, domainscm + "/prjweb/project/detailsshow?des3PrjId=" + des3PrjId);
      contentMap.put(MsgConstants.MSG_PRJ_IMG, "/resmod/images_v5/images2016/file_img.jpg");
      contentMap.put(MsgConstants.MSG_DES3_PRJ_ID, des3PrjId);
      dateMap.put(MsgConstants.MSG_CONTENT_NEWEST, zhTitle);
    }

  }

  // 构建文件信息
  private void buildFileInfo(Map<String, Object> dateMap, Map<String, Object> contentMap) {

    boolean belongPerson = BooleanUtils.toBoolean(dateMap.get(MsgConstants.MSG_BELONG_PERSON).toString());
    contentMap.put(MsgConstants.MSG_BELONG_PERSON, belongPerson);
    if (belongPerson) {
      Long fileId = NumberUtils.toLong(dateMap.get(MsgConstants.MSG_FILE_ID).toString());
      PsnFile psnFile = psnFileDao.get(fileId);
      if (psnFile != null) {
        contentMap.put(MsgConstants.MSG_ARCHIVE_FILE_ID, psnFile.getArchiveFileId());
        contentMap.put(MsgConstants.MSG_FILE_NAME, psnFile.getFileName());
        contentMap.put(MsgConstants.MSG_FILE_TYPE, psnFile.getFileType());
        contentMap.put(MsgConstants.MSG_FILE_ID, fileId);
        dateMap.put(MsgConstants.MSG_CONTENT_NEWEST, psnFile.getFileName());
      }
    } else {
      Long grpFileId = NumberUtils.toLong(dateMap.get(MsgConstants.MSG_GRP_FILE_ID).toString());
      GrpFile grpFile = grpFileDao.get(grpFileId);
      Long grpId = NumberUtils.toLong(dateMap.get(MsgConstants.MSG_GRP_ID).toString());
      // 分享其他群组的文件，所有去除 && grpFile.getGrpId().longValue() ==
      // grpId.longValue()
      if (grpFile != null) {
        contentMap.put(MsgConstants.MSG_GRP_ID, grpId);
        contentMap.put(MsgConstants.MSG_ARCHIVE_FILE_ID, grpFile.getArchiveFileId());
        contentMap.put(MsgConstants.MSG_FILE_NAME, grpFile.getFileName());
        contentMap.put(MsgConstants.MSG_FILE_TYPE, grpFile.getFileType());
        contentMap.put(MsgConstants.MSG_FILE_PATH, grpFile.getFilePath());
        contentMap.put(MsgConstants.MSG_GRP_FILE_ID, grpFileId);
        dateMap.put(MsgConstants.MSG_CONTENT_NEWEST, grpFile.getFileName());
      }

    }
  }

  // 构建成果信息
  private void buildPublicationInfo(Map<String, Object> dateMap, Map<String, Object> contentMap) {
    boolean belongPerson = BooleanUtils.toBoolean(dateMap.get(MsgConstants.MSG_BELONG_PERSON).toString());
    contentMap.put(MsgConstants.MSG_BELONG_PERSON, belongPerson);
    Long pubId = 0L;

    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    // 个人成果
    if (belongPerson) {
      pubId = NumberUtils.toLong(dateMap.get(MsgConstants.MSG_PUB_ID).toString());
      pubQueryDTO.setServiceType(V8pubQueryPubConst.QUERY_PUB_BY_PUB_ID_SERVICE);
      pubQueryDTO.setSearchPubId(pubId);
    } else {
      Long grpPubId = NumberUtils.toLong(dateMap.get(MsgConstants.MSG_GRP_PUB_ID).toString());
      Long grpId = NumberUtils.toLong(dateMap.get(MsgConstants.MSG_GRP_ID).toString());
      pubQueryDTO.setServiceType(V8pubQueryPubConst.QUERY_GRP_PUB_BY_PUB_ID_SERVICE);
      pubQueryDTO.setSearchPubId(grpPubId);
      pubQueryDTO.setSearchGrpId(grpId);
    }

    Map<String, Object> pubInfo = getRemotePubInfo(pubId, pubQueryDTO);
    if (pubInfo != null && pubInfo.get("pubId") != null) {
      String title = constructParams(String.valueOf(pubInfo.get("title") == null ? "" : pubInfo.get("title")));
      String briefDesc =
          constructParams(String.valueOf(pubInfo.get("briefDesc") == null ? "" : pubInfo.get("briefDesc")));
      String authorNames =
          constructParams(String.valueOf(pubInfo.get("authorNames") == null ? "" : pubInfo.get("authorNames")));
      contentMap.put(MsgConstants.MSG_PUB_TITLE_ZH, title);
      contentMap.put(MsgConstants.MSG_PUB_TITLE_EN, title);
      contentMap.put(MsgConstants.MSG_PUB_BRIEF_DESC_ZH, briefDesc);
      contentMap.put(MsgConstants.MSG_PUB_BRIEF_DESC_EN, briefDesc);
      contentMap.put(MsgConstants.MSG_PUB_AUTHOR_NAME, authorNames);
      contentMap.put(MsgConstants.MSG_CONTENT_NEWEST, title);
      dateMap.put(MsgConstants.MSG_CONTENT_NEWEST, title);
      contentMap.put(MsgConstants.MSG_PUB_SHORT_URL, pubInfo.get("pubIndexUrl"));
      contentMap.put(MsgConstants.MSG_PUB_ID, pubId);
      // 群组成果没有删除
      if (!belongPerson && "false".equals(pubInfo.get("pubHasDel").toString())) {
        Long grpPubId = NumberUtils.toLong(dateMap.get(MsgConstants.MSG_GRP_PUB_ID).toString());
        Long grpId = NumberUtils.toLong(dateMap.get(MsgConstants.MSG_GRP_ID).toString());
        contentMap.put(MsgConstants.MSG_GRP_PUB_ID, grpPubId);
        contentMap.put(MsgConstants.MSG_GRP_ID, grpId);
      }
      // 全文信息
      Long sendPsnId = NumberUtils.toLong(contentMap.get(MsgConstants.MSG_SENDER_ID).toString());
      if ("1".equals(pubInfo.get("hasFulltext").toString())) {
        contentMap.put(MsgConstants.MSG_HAS_PUB_FULLTEXT, true);
        contentMap.put(MsgConstants.MSG_PUB_FULLTEXT_ID, pubInfo.get("fullTextFieId"));
        contentMap.put(MsgConstants.MSG_PUB_FULLTEXT_EXT, "");
        contentMap.put(MsgConstants.MSG_PUB_FULLTEXT_IMAGE_PATH, pubInfo.get("fullTextImgUrl"));
        if ("0".equals(pubInfo.get("fullTextPermission"))) {
          contentMap.put(MsgConstants.MSG_PUB_FULLTEXT_PERMIT, "permit");
        } else {
          if (!sendPsnId.equals(pubInfo.get("ownerPsnId"))) {
            contentMap.put(MsgConstants.MSG_PUB_FULLTEXT_PERMIT, "noPermit");
          }
        }
      } else {
        contentMap.put(MsgConstants.MSG_HAS_PUB_FULLTEXT, false);
        contentMap.put(MsgConstants.MSG_PUB_FULLTEXT_PERMIT, "noPermit");
      }
    }
  }

  // 构建基准库成果信息
  private void buildPdwhPubInfo(Map<String, Object> dateMap, Map<String, Object> contentMap) {
    Long pubId = 0L;
    pubId = NumberUtils.toLong(dateMap.get(MsgConstants.MSG_PUB_ID).toString());

    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    pubQueryDTO.setServiceType(V8pubQueryPubConst.QUERY_PDWH_PUB_BY_PUB_ID_SERVICE);
    pubQueryDTO.setSearchPubId(pubId);

    Map<String, Object> pubInfo = getRemotePubInfo(pubId, pubQueryDTO);
    if (pubInfo != null && pubInfo.get("pubId") != null) {
      String title = constructParams(String.valueOf(pubInfo.get("title") == null ? "" : pubInfo.get("title")));
      String briefDesc =
          constructParams(String.valueOf(pubInfo.get("briefDesc") == null ? "" : pubInfo.get("briefDesc")));
      String authorNames =
          constructParams(String.valueOf(pubInfo.get("authorNames") == null ? "" : pubInfo.get("authorNames")));
      contentMap.put(MsgConstants.MSG_PUB_TITLE_ZH, title);
      contentMap.put(MsgConstants.MSG_PUB_TITLE_EN, title);
      contentMap.put(MsgConstants.MSG_PUB_BRIEF_DESC_ZH, briefDesc);
      contentMap.put(MsgConstants.MSG_PUB_BRIEF_DESC_EN, briefDesc);
      contentMap.put(MsgConstants.MSG_PUB_AUTHOR_NAME, authorNames);
      contentMap.put(MsgConstants.MSG_CONTENT_NEWEST, title);
      contentMap.put(MsgConstants.MSG_PUB_SHORT_URL, pubInfo.get("pubIndexUrl"));
      contentMap.put(MsgConstants.MSG_PUB_ID, pubId);
      contentMap.put(MsgConstants.MSG_PUB_DBID, "");
    }

    // 全文信息
    if ("1".equals(pubInfo.get("hasFulltext").toString())) {
      contentMap.put(MsgConstants.MSG_HAS_PUB_FULLTEXT, true);
      contentMap.put(MsgConstants.MSG_PUB_FULLTEXT_ID, pubInfo.get("fullTextFieId"));
      contentMap.put(MsgConstants.MSG_PUB_FULLTEXT_EXT, "");
      contentMap.put(MsgConstants.MSG_PUB_FULLTEXT_IMAGE_PATH, pubInfo.get("fullTextImgUrl"));
      contentMap.put(MsgConstants.MSG_PUB_FULLTEXT_PERMIT, "permit");
    } else {
      contentMap.put(MsgConstants.MSG_HAS_PUB_FULLTEXT, false);
      contentMap.put(MsgConstants.MSG_PUB_FULLTEXT_PERMIT, "noPermit");
    }
  }

  // 构建基金信息
  private void buildFundInfo(Map<String, Object> dateMap, Map<String, Object> contentMap) {

    boolean belongPerson = BooleanUtils.toBoolean(dateMap.get(MsgConstants.MSG_BELONG_PERSON).toString());
    contentMap.put(MsgConstants.MSG_BELONG_PERSON, belongPerson);
    if (belongPerson) {
      Long fundId = NumberUtils.toLong(dateMap.get(MsgConstants.MSG_FUND_ID).toString());
      ConstFundCategory fund = constFundCategoryDao.get(fundId);
      if (fund != null) {
        contentMap.put(MsgConstants.MSG_FUND_ZH_TITLE, this.dealNullVal(fund.getNameZh()));
        contentMap.put(MsgConstants.MSG_FUND_EN_TITLE, this.dealNullVal(fund.getNameEn()));
        // contentMap.put(MsgConstants.MSG_FUND_AGENCY_NAME,
        // this.dealNullVal(fund.getAgencyViewName()));
        // contentMap.put(MsgConstants.MSG_FUND_SCIENCE_AREA, "科技领域");
        // contentMap.put(MsgConstants.MSG_FUND_APPLY_TIME,
        // this.dealNullVal(buildFundApplyTime(fund)));
        if (dateMap.get(MsgConstants.MSG_FUND_INFO) != null
            && StringUtils.isNotBlank(dateMap.get(MsgConstants.MSG_FUND_INFO).toString())) {
          Map<String, Object> info = JacksonUtils.jsonToMap(dateMap.get(MsgConstants.MSG_FUND_INFO).toString());
          if (info != null) {
            contentMap.put(MsgConstants.MSG_FUND_DESC_ZH, this.dealNullVal(info.get("fund_desc_zh")));
            contentMap.put(MsgConstants.MSG_FUND_DESC_EN, this.dealNullVal(info.get("fund_desc_en")));
            contentMap.put(MsgConstants.MSG_FUND_DESC_ZH_BR, this.dealNullVal(info.get("fund_desc_zh_br")));
            contentMap.put(MsgConstants.MSG_FUND_DESC_EN_BR, this.dealNullVal(info.get("fund_desc_en_br")));
          }
        }
        contentMap.put(MsgConstants.MSG_FUND_ID, fundId);
        if (fund.getAgencyId() != null) {
          contentMap.put(MsgConstants.MSG_FUND_LOGO_URL,
              this.dealNullVal(constFundAgencyDao.findFundAgencyLogoUrl(fund.getAgencyId())));
        }
        dateMap.put(MsgConstants.MSG_CONTENT_NEWEST, this.dealNullVal(getShowTitle(fund)));
      }
    }
  }

  // 构建资助机构信息
  private void buildAgencyInfo(Map<String, Object> dateMap, Map<String, Object> contentMap) {
    boolean belongPerson = BooleanUtils.toBoolean(dateMap.get(MsgConstants.MSG_BELONG_PERSON).toString());
    contentMap.put(MsgConstants.MSG_BELONG_PERSON, belongPerson);
    if (belongPerson) {
      Long agencyId = NumberUtils.toLong(dateMap.get(MsgConstants.MSG_AGENCY_ID).toString());
      ConstFundAgency agency = constFundAgencyDao.get(agencyId);
      if (agency != null) {
        contentMap.put(MsgConstants.MSG_AGENCY_ZH_TITLE, this.dealNullVal(agency.getNameZh()));
        contentMap.put(MsgConstants.MSG_AGENCY_EN_TITLE, this.dealNullVal(agency.getNameEn()));
        if (dateMap.get(MsgConstants.MSG_AGENCY_INFO) != null
            && StringUtils.isNotBlank(dateMap.get(MsgConstants.MSG_AGENCY_INFO).toString())) {
          Map<String, Object> info = JacksonUtils.jsonToMap(dateMap.get(MsgConstants.MSG_AGENCY_INFO).toString());
          if (info != null) {
            contentMap.put(MsgConstants.MSG_AGENCY_DESC_ZH, this.dealNullVal(info.get("agency_desc_zh")));
            contentMap.put(MsgConstants.MSG_AGENCY_DESC_EN, this.dealNullVal(info.get("agency_desc_en")));
          }
        }
        contentMap.put(MsgConstants.MSG_AGENCY_ID, agencyId);
        contentMap.put(MsgConstants.MSG_AGENCY_LOGO_URL, this.dealNullVal(agency.getLogoUrl()));
        dateMap.put(MsgConstants.MSG_CONTENT_NEWEST, this.dealNullVal(getAgencyShowTitle(agency)));
      }
    }
  }

  private String constructParams(String title) {
    title = StringUtils.trimToEmpty(title);
    title = StringUtils.substring(title, 0, 200);
    return title;
  }

  private String buildFundApplyTime(ConstFundCategory fund) {
    SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM-dd");
    Date startTime = fund.getStartDate();
    Date endTime = fund.getEndDate();
    String start = "";
    String end = "";
    if (startTime != null) {
      start = smf.format(startTime);
    }
    if (endTime != null) {
      end = smf.format(endTime);
    }
    if (StringUtils.isNotBlank(start) && StringUtils.isNotBlank(end)) {
      return start + "-" + end;
    } else {
      return start + end;
    }
  }

  /**
   * 处理空值字符串，null的转为""
   * 
   * @param val
   * @return
   */
  private String dealNullVal(Object val) {
    if (val != null && StringUtils.isNotBlank(val.toString())) {
      return val.toString();
    } else {
      return "";
    }
  }

  // 获取title
  private String getShowTitle(ConstFundCategory fund) {
    String locale = LocaleContextHolder.getLocale().toString();
    if ("en_US".equals(locale)) {
      return StringUtils.isNotBlank(fund.getNameEn()) ? fund.getNameEn() : fund.getNameZh();
    } else {
      return StringUtils.isNotBlank(fund.getNameZh()) ? fund.getNameZh() : fund.getNameEn();
    }
  }

  // 获取title
  private String getAgencyShowTitle(ConstFundAgency agency) {
    String locale = LocaleContextHolder.getLocale().toString();
    if ("en_US".equals(locale)) {
      return StringUtils.isNotBlank(agency.getNameEn()) ? agency.getNameEn() : agency.getNameZh();
    } else {
      return StringUtils.isNotBlank(agency.getNameZh()) ? agency.getNameZh() : agency.getNameEn();
    }
  }

  @Override
  public void produceMsg(Map<String, Object> dateMap, Long senderId, Long receiverId) throws Exception {
    Long contentId = NumberUtils.toLong(dateMap.get(MsgConstants.MSG_CONTENT_ID).toString());
    Date date = new Date();
    MsgRelation mr = new MsgRelation();
    mr.setContentId(contentId);
    mr.setCreateDate(date);
    mr.setDealDate(date);
    mr.setDealStatus(0);
    mr.setStatus(0);
    mr.setSenderId(senderId);
    mr.setReceiverId(receiverId);
    mr.setType(Integer.parseInt(MsgConstants.MSG_TYPE_SMATE_INSIDE_LETTER));
    msgRelationDao.save(mr);
    dateMap.put(MsgConstants.MSG_RELATION_ID, mr.getId());

    // 保存关联信息
    MsgChatRelation sendChatRelation = null;
    MsgChatRelation receiveChatRelation = null;
    sendChatRelation = msgChatRelationDao.findMsgChatRelation(senderId, receiverId);
    receiveChatRelation = msgChatRelationDao.findMsgChatRelation(receiverId, senderId);

    if (sendChatRelation == null) {
      sendChatRelation = new MsgChatRelation();
      sendChatRelation.setSenderId(senderId);
      sendChatRelation.setReceiverId(receiverId);
    }
    sendChatRelation.setContentNewest(dateMap.get(MsgConstants.MSG_CONTENT_NEWEST).toString());
    sendChatRelation.setStatus(0);
    sendChatRelation.setUpdateDate(date);

    if (receiveChatRelation == null) {
      receiveChatRelation = new MsgChatRelation();
      receiveChatRelation.setSenderId(receiverId);
      receiveChatRelation.setReceiverId(senderId);
    }
    receiveChatRelation.setContentNewest(dateMap.get(MsgConstants.MSG_CONTENT_NEWEST).toString());
    receiveChatRelation.setStatus(0);
    receiveChatRelation.setUpdateDate(date);

    msgChatRelationDao.save(sendChatRelation);
    msgChatRelationDao.save(receiveChatRelation);

  }

  // 构建全文信息
  private void buildFulltextInfo(Map<String, Object> dateMap, Map<String, Object> contentMap) {

    Long pubId = NumberUtils.toLong(dateMap.get(MsgConstants.MSG_PUB_ID).toString());

    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    pubQueryDTO.setServiceType(V8pubQueryPubConst.QUERY_PUB_FULLTEXT_BY_PUB_ID_SERVICE);
    pubQueryDTO.setSearchPubId(pubId);

    Map<String, Object> pubInfo = getRemotePubInfo(pubId, pubQueryDTO);

    if (pubInfo != null && pubInfo.get("fullTextId") != null) {
      contentMap.put(MsgConstants.MSG_PUB_FULLTEXT_ID, pubInfo.get("fullTextId"));
      contentMap.put(MsgConstants.MSG_PUB_FULLTEXT_EXT, "");
      contentMap.put(MsgConstants.MSG_PUB_FULLTEXT_IMAGE_PATH, pubInfo.get("fullTextImgUrl"));

      contentMap.put(MsgConstants.MSG_ARCHIVE_FILE_ID, pubInfo.get("fullTextFieId"));
      contentMap.put(MsgConstants.MSG_FILE_NAME, pubInfo.get("fullTextName"));
      dateMap.put(MsgConstants.MSG_CONTENT_NEWEST, pubInfo.get("fullTextName"));
      contentMap.put(MsgConstants.MSG_PUB_ID, pubId);
      String fileType = ArchiveFileUtil.getFileType(pubInfo.get("fullTextName").toString());
      contentMap.put(MsgConstants.MSG_FILE_TYPE, fileType);

    }

  }
}
