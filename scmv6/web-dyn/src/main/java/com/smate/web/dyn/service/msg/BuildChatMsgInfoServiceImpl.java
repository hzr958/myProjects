package com.smate.web.dyn.service.msg;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.file.enums.FileTypeEnum;
import com.smate.core.base.file.service.ArchiveFileService;
import com.smate.core.base.file.service.FileDownloadUrlService;
import com.smate.core.base.project.dao.ProjectDao;
import com.smate.core.base.project.model.Project;
import com.smate.core.base.psn.service.psnpub.PsnPubService;
import com.smate.core.base.pub.enums.PubDbEnum;
import com.smate.core.base.pub.enums.PubPdwhStatusEnum;
import com.smate.core.base.utils.cache.SnsCacheService;
import com.smate.core.base.utils.constant.MsgConstants;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.dyn.dao.fund.ConstFundAgencyDao;
import com.smate.web.dyn.dao.fund.ConstFundCategoryDao;
import com.smate.web.dyn.dao.fund.FundAgencyDao;
import com.smate.web.dyn.dao.msg.MsgContentDao;
import com.smate.web.dyn.dao.news.NewsBaseDao;
import com.smate.web.dyn.dao.pdwhpub.PubPdwhDAO;
import com.smate.web.dyn.dao.pub.GroupPubsDAO;
import com.smate.web.dyn.dao.pub.PubFullTextReqBaseDao;
import com.smate.web.dyn.dao.pub.PubSnsDAO;
import com.smate.web.dyn.dao.pub.PubSnsFullTextDAO;
import com.smate.web.dyn.form.msg.MsgShowForm;
import com.smate.web.dyn.form.msg.mobile.MobileMsgShowForm;
import com.smate.web.dyn.model.fund.ConstFundAgency;
import com.smate.web.dyn.model.fund.ConstFundCategory;
import com.smate.web.dyn.model.msg.MsgContent;
import com.smate.web.dyn.model.msg.MsgRelation;
import com.smate.web.dyn.model.msg.MsgShowInfo;
import com.smate.web.dyn.model.news.NewsBase;
import com.smate.web.dyn.model.pub.GroupPubPO;
import com.smate.web.dyn.model.pub.PubFullTextPO;
import com.smate.web.dyn.model.pub.PubPdwhPO;
import com.smate.web.dyn.model.pub.PubSnsPO;
import com.smate.web.dyn.model.pub.PubSnsStatusEnum;

/**
 * 站内信消息显示实现服务类
 * 
 * @author zzx
 *
 */
@Transactional(rollbackOn = Exception.class)
public class BuildChatMsgInfoServiceImpl extends BuildMsgInfoBase {
  @Resource
  private MsgContentDao msgContentDao;
  @Resource
  private PersonDao personDao;
  @Value("${initOpen.restful.url}")
  private String openResfulUrl;
  @Autowired
  private RestTemplate restTemplate;
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private SnsCacheService snsCacheService;
  @Autowired
  private FileDownloadUrlService fileDownUrlService;
  @Autowired
  private ArchiveFileService archiveFileService;
  @Autowired
  private PubSnsDAO pubSnsDAO;
  @Autowired
  private PubSnsFullTextDAO pubSnsFullTextDAO;
  @Autowired
  private PsnPubService PsnPubService;
  @Autowired
  private GroupPubsDAO groupPubsDAO;
  @Autowired
  private PubFullTextReqBaseDao pubFTReqBaseDao;
  @Autowired
  private ConstFundAgencyDao constFundAgencyDao;
  @Autowired
  private ConstFundCategoryDao constFundCategoryDao;
  @Autowired
  private FundAgencyDao fundAgencyDao;// 机构dao
  @Autowired
  private ProjectDao projectDao;// 项目dao
  @Autowired
  private PubPdwhDAO pubPdwhDAO;
  @Autowired
  private NewsBaseDao newsBaseDao;

  @Override
  public void buildMsgShowInfo(MsgShowForm form, MsgRelation m) throws Exception {
    MsgShowInfo msi = null;
    MsgContent msgContent = msgContentDao.get(m.getContentId());
    if (msgContent != null && JacksonUtils.isJsonString(msgContent.getContent())) {
      msi = (MsgShowInfo) JacksonUtils.jsonObject(msgContent.getContent(), MsgShowInfo.class);
      switch (msi.getSmateInsideLetterType()) {
        case "text":
          break;
        case "pub":
          PubSnsPO pubSns = pubSnsDAO.get(msi.getPubId());
          msi.setResDelete(pubSns != null && pubSns.getStatus() != PubSnsStatusEnum.DELETED ? 1 : 0);
          buildPubMsgInfo(msi, form, m);
          break;
        case "file":
          buildFileMsgInfo(msi, form, m);
          break;
        case "pdwhpub":
          PubPdwhPO pubPdwh = pubPdwhDAO.get(msi.getPubId());
          msi.setResDelete(pubPdwh != null && pubPdwh.getStatus() != PubPdwhStatusEnum.DELETED ? 1 : 0);
          buildPdwhPubMsgInfo(msi, form, m);
          break;
        case "fund":
          ConstFundCategory constFundCategory = constFundCategoryDao.get(msi.getFundId());
          msi.setResDelete(constFundCategory != null ? 1 : 0);
          buildFundMsgInfo(msi, form, m);
          break;
        case "fulltext":
          PubFullTextPO pubFullText = pubSnsFullTextDAO.getPubFullTextByPubId(msi.getPubId());
          msi.setResDelete(pubFullText != null ? 1 : 0);
          msi.setHasPubFulltext(pubFullText != null ? "true" : "false");
          buildFulltextMsgInfo(msi, form, m);
          break;
        case "prj":
          // 需要对数据进行处理
          buildPrjUrlData(msi);
          buildPrjMsgInfo(msi, form, m);

          Project prj = projectDao.get(NumberUtils.toLong(Des3Utils.decodeFromDes3(msi.getDes3PrjId())));
          msi.setResDelete(prj != null && prj.getStatus() != 1 ? 1 : 0);
          break;
        case "agency":
          ConstFundAgency fundAgency = fundAgencyDao.get(msi.getAgencyId());
          msi.setResDelete(fundAgency != null ? 1 : 0);
          buildAgencyShowInfo(msi);
          break;
        case "news":
          NewsBase newsBase = newsBaseDao.get(NumberUtils.toLong(Des3Utils.decodeFromDes3(msi.getDes3NewsId())));
          msi.setResDelete(newsBase != null && newsBase.getStatus() != 9 ? 1 : 0);
          buildNewsShowInfo(msi);
          break;
        case "psnUrl":
          break;
        default:
          break;
      }

      msi.setType(m.getType());
      msi.setCreateDate(m.getCreateDate());
      msi.setMsgRelationId(m.getId());
      msi.setStatus(m.getStatus());
      msi.setContent(msi.getContent() == null ? "" : msi.getContent().replaceAll("\n", "<br/>"));
      if (form.getPsnId().equals(m.getSenderId())) {
        msi.setIAmSender(1);
      }
      msi.setPsnId(form.getPsnId());
      Person senderInfo = personDao.findPersonBase(m.getSenderId());
      buildPsnInfo(msi, senderInfo, "sender");
      // --start-消息列表显示时间逻辑，消息时间间隔超过1800000则要显示时间----
      if (form.getLastDate() != null) {
        Long time = msi.getCreateDate().getTime();
        Long time2 = form.getLastDate().getTime();
        if (time > time2 + 1800000) {
          msi.setIfShowDate(true);
        }
      } else {
        msi.setIfShowDate(true);
      }
      form.setLastDate(msi.getCreateDate());
      // --end-消息列表显示时间逻辑，消息时间间隔超过1800000则要显示时间----
      if (StringUtils.isNotBlank(form.getSearchKey())) {
        // 消息检索过滤
        doSearchContent(form, msi);
      } else {
        form.getMsgShowInfoList().add(msi);
      }
    }
  }

  private void buildPrjUrlData(MsgShowInfo msi) {
    // 过滤有问题的priId
    msi.setDes3PrjId(msi.getDes3PrjId().replace("+", "%2B"));
    msi.setPrjUrl(msi.getPrjUrl().replace("+", "%2B"));
  }

  private void buildPrjMsgInfo(MsgShowInfo msi, MsgShowForm form, MsgRelation m) {
    if ("zh".equals(form.getLanguage())) {
      msi.setTitle(msi.getPrjTitleZh());
      msi.setAuthorName(msi.getPrjAuthorNameZh());
      msi.setBrief(msi.getPrjBriefZh());
    } else {
      msi.setTitle(msi.getPrjTitleEn());
      msi.setAuthorName(msi.getPrjAuthorNameEn());
      msi.setBrief(msi.getPrjBriefEn());
    }
  }

  private void buildFulltextMsgInfo(MsgShowInfo msi, MsgShowForm form, MsgRelation m) {
    if (msi.getPubId() != null) {
      String downloadUrl = fileDownUrlService.getDownloadUrl(FileTypeEnum.SNS_FULLTEXT, msi.getPubId());
      msi.setDownloadUrl(downloadUrl);
      PubFullTextPO fullText = pubSnsFullTextDAO.getPubFullTextByPubId(msi.getPubId());
      if (fullText != null) {
        msi.setFileShortDownloadUrl(
            fileDownUrlService.getShortDownloadUrl(FileTypeEnum.SNS_FULLTEXT, fullText.getFileId()));
        if (StringUtils.isBlank(msi.getPubFulltextImagePath())) {
          msi.setPubFulltextImagePath(fullText.getThumbnailPath());
        }
      }
    }
  }

  private void buildFundMsgInfo(MsgShowInfo msi, MsgShowForm form, MsgRelation m) {
    buildFundShowInfo(msi);
  }

  private void buildPdwhPubMsgInfo(MsgShowInfo msi, MsgShowForm form, MsgRelation m) {
    if ("true".equals(msi.getHasPubFulltext()) && msi.getPubId() != null) {
      String downloadUrl = fileDownUrlService.getDownloadUrl(FileTypeEnum.PDWH_FULLTEXT, msi.getPubId());
      msi.setFileShortDownloadUrl(
          fileDownUrlService.getShortDownloadUrl(FileTypeEnum.PDWH_FULLTEXT, msi.getPubFulltextId()));
      msi.setDownloadUrl(downloadUrl);
    }
    if (StringUtils.isBlank(msi.getPubFulltextImagePath())) {
      if ("true".equals(msi.getHasPubFulltext())) {
        msi.setPubFulltextImagePath("/resmod/images_v5/images2016/file_img1.jpg");
      } else {
        msi.setPubFulltextImagePath("/resmod/images_v5/images2016/file_img.jpg");
      }
    }
  }

  private void buildPubMsgInfo(MsgShowInfo msi, MsgShowForm form, MsgRelation m) {
    // 自己的全文，一定允许自己下载
    if (msi.getPubId() != null && msi.getPubFulltextId() != null) {
      PubSnsPO pubSns = pubSnsDAO.get(msi.getPubId());
      if (pubSns != null) {
        Long pubOwnerPsnId = PsnPubService.getPubOwnerId(msi.getPubId());
        if (NumberUtils.isNullOrZero(pubOwnerPsnId)) {
          GroupPubPO groupPub = groupPubsDAO.findByPubId(msi.getPubId());
          if (groupPub != null) {
            pubOwnerPsnId = groupPub.getOwnerPsnId();
          }
        }
        if (form.getPsnId().equals(pubOwnerPsnId)) {
          msi.setPubFulltextPermit("permit");
        }

        PubFullTextPO pubFullText = pubSnsFullTextDAO.getPubFullTextByPubId(msi.getPubId());
        if (pubFullText == null) {
          msi.setHasPubFulltext("false");
        } else {
          msi.setPubFulltextId(pubFullText.getFileId());// 分享了成果换了全文的情况要考虑
          msi.setHasPubFulltext("true");
          msi.setPubFulltextImagePath(pubFullText.getThumbnailPath());
          boolean flag = pubFTReqBaseDao.isFullTextReqAgree(msi.getPubId(), form.getPsnId(), PubDbEnum.SNS);
          if (flag) {
            msi.setPubFulltextPermit("permit");
          }
          if (pubFullText.getPermission() == 0) {
            msi.setPubFulltextPermit("permit");
          }
        }
        msi.setDes3PubOwnerPsnId(Des3Utils.encodeToDes3(pubOwnerPsnId.toString()));
      }
    }
    // 没有权限下载的。设置全文id为null ,页面上好判断。2017-07-14-ajb
    if ("true".equals(msi.getHasPubFulltext()) && !"permit".equals(msi.getPubFulltextPermit())) {
      msi.setPubFulltextId(null);
    }
    if ("true".equals(msi.getHasPubFulltext()) && "permit".equals(msi.getPubFulltextPermit())) {
      String downloadUrl = fileDownUrlService.getDownloadUrl(FileTypeEnum.SNS_FULLTEXT, msi.getPubId());
      msi.setFileShortDownloadUrl(
          fileDownUrlService.getShortDownloadUrl(FileTypeEnum.SNS_FULLTEXT, msi.getPubFulltextId()));
      msi.setDownloadUrl(downloadUrl);
    }
    // 默认全文图片
    if (StringUtils.isBlank(msi.getPubFulltextImagePath())) {
      if ("true".equals(msi.getHasPubFulltext())) {
        msi.setPubFulltextImagePath("/resmod/images_v5/images2016/file_img1.jpg");
      } else {
        msi.setPubFulltextImagePath("/resmod/images_v5/images2016/file_img.jpg");
      }
    }
  }

  private void buildFileMsgInfo(MsgShowInfo msi, MsgShowForm form, MsgRelation m) {
    if (msi.getFileId() != null || msi.getGrpFileId() != null) {
      String downloadUrl, imgThumbUrl;
      // 取文件下载地址
      if ("true".equals(msi.getBelongPerson())) {
        downloadUrl = fileDownUrlService.getDownloadUrl(FileTypeEnum.PSN, msi.getFileId(), 0L);
        msi.setFileShortDownloadUrl(fileDownUrlService.getShortDownloadUrl(FileTypeEnum.PSN, msi.getFileId()));
      } else {
        downloadUrl = fileDownUrlService.getDownloadUrl(FileTypeEnum.GROUP, msi.getGrpFileId(), 0L);
        msi.setFileShortDownloadUrl(fileDownUrlService.getShortDownloadUrl(FileTypeEnum.GROUP, msi.getGrpFileId()));
      }
      // 取图片类型文件缩略图
      imgThumbUrl = archiveFileService.getImgFileThumbUrl(msi.getArchiveFileId());
      msi.setImgThumbUrl(imgThumbUrl);
      // 添加文件预览图片地址，方便app端直接获取
      if ("ppt".equals(msi.getFileType()) || "pptx".equals(msi.getFileType())) {
        msi.setFileIconPath("/resmod/smate-pc/img/fileicon_ppt.png");
      } else if ("docx".equals(msi.getFileType()) || "doc".equals(msi.getFileType())) {
        msi.setFileIconPath("/resmod//smate-pc/img/fileicon_doc.png");
      } else if ("zip".equals(msi.getFileType()) || "rar".equals(msi.getFileType())) {
        msi.setFileIconPath("/resmod/smate-pc/img/fileicon_zip.png");
      } else if ("xlsx".equals(msi.getFileType()) || "xls".equals(msi.getFileType())) {
        msi.setFileIconPath("/resmod/smate-pc/img/fileicon_xls.png");
      } else if ("txt".equals(msi.getFileType())) {
        msi.setFileIconPath("/resmod/smate-pc/img/fileicon_txt.png");
      } else if ("imgIc".equals(msi.getFileType()) || "jpg".equals(msi.getFileType()) || "png".equals(msi.getFileType())
          || "pdf".equals(msi.getFileType())) {
        // 图片类型取图片类型文件缩略图
        msi.setFileIconPath(imgThumbUrl);
      } else {
        msi.setFileIconPath("/resmod/smate-pc/img/fileicon_default.png");
      }
      msi.setFileDownloadUrl(downloadUrl);
      msi.setDownloadUrl(downloadUrl);
    }
  }

  private void doSearchContent(MsgShowForm form, MsgShowInfo msi) {
    String type = msi.getSmateInsideLetterType();
    String language = form.getLanguage();
    String content = "";
    switch (type) {
      case "text":
        content = msi.getContent();
        if (StringUtils.isNotBlank(content) && content.indexOf(form.getSearchKey()) != -1) {
          msi.setContent(buildSearchContent(content, form.getSearchKey()));
          form.getMsgShowInfoList().add(msi);
        }
        break;
      case "pub":
      case "pdwhpub":
        if ("zh".equals(language)) {
          content = msi.getPubTitleZh();
          if (StringUtils.isNotBlank(content) && content.indexOf(form.getSearchKey()) != -1) {
            msi.setPubTitleZh(buildSearchContent(content, form.getSearchKey()));
            form.getMsgShowInfoList().add(msi);
          }
        } else {
          content = msi.getPubTitleEn();
          if (StringUtils.isNotBlank(content) && content.indexOf(form.getSearchKey()) != -1) {
            msi.setPubTitleEn(buildSearchContent(content, form.getSearchKey()));
            form.getMsgShowInfoList().add(msi);
          }
        }
        break;
      case "file":
        content = msi.getFileName();
        if (StringUtils.isNotBlank(content) && content.indexOf(form.getSearchKey()) != -1) {
          msi.setFileName(buildSearchContent(content, form.getSearchKey()));
          form.getMsgShowInfoList().add(msi);
        }
        break;
      case "fund":
        if ("zh".equals(language)) {
          content = msi.getFundZhTitle();
          if (StringUtils.isNotBlank(content) && content.indexOf(form.getSearchKey()) != -1) {
            msi.setFundZhTitle(buildSearchContent(content, form.getSearchKey()));
            form.getMsgShowInfoList().add(msi);
          }
        } else {
          content = msi.getFundEnTitle();
          if (StringUtils.isNotBlank(content) && content.indexOf(form.getSearchKey()) != -1) {
            msi.setFundEnTitle(buildSearchContent(content, form.getSearchKey()));
            form.getMsgShowInfoList().add(msi);
          }
        }
        break;
      default:
        break;
    }
  }

  private String buildSearchContent(String content, String searchKey) {
    if (content.indexOf(searchKey) >= 0) {
      String[] split = content.split(searchKey);
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < split.length; i++) {
        if (i == split.length - 1) {
          if (split.length == 1) {
            if (content.indexOf(searchKey) == 0) {
              sb.append("<span style='color:red!important'>" + searchKey + "</span>" + split[i]);
            } else {
              sb.append(split[i] + "<span style='color:red!important'>" + searchKey + "</span>");
            }
          } else {
            sb.append(split[i]);
          }
        } else {
          sb.append(split[i] + "<span style='color:red!important'>" + searchKey + "</span>");
        }
      }
      if (split.length == 0) {
        if (content.indexOf(searchKey) == 0 && content.length() == searchKey.length()) {
          content = "<span style='color:red!important'>" + searchKey + "</span>";
        } else {
          content = "<span style='color:red!important'>" + content + "</span>";
        }
      } else {
        content = sb.toString();
      }
    }
    return content;
  }

  @Override
  public void buildMobileMsgShowInfo(MobileMsgShowForm form, MsgRelation m) throws Exception {
    MsgShowInfo msi = null;
    MsgContent msgContent = msgContentDao.get(m.getContentId());
    if (msgContent != null && JacksonUtils.isJsonString(msgContent.getContent())) {
      msi = (MsgShowInfo) JacksonUtils.jsonObject(msgContent.getContent(), MsgShowInfo.class);
      msi.setType(m.getType());
      msi.setCreateDate(m.getCreateDate());
      msi.setMsgRelationId(m.getId());
      msi.setStatus(m.getStatus());
      msi.setPsnId(form.getPsnId());
      Person senderInfo = personDao.findPersonBase(m.getSenderId());
      buildPsnInfo(msi, senderInfo, "sender");
      if (msi.getPubId() != null && msi.getPubId() > 0l) {
        msi.setDes3PubId(ServiceUtil.encodeToDes3(msi.getPubId() + ""));
      }
      // 构建文件下载链接
      if ("file".equals(msi.getSmateInsideLetterType())) {
        String fileDownloadUrl = "";
        if (msi.getGrpId() != null && msi.getGrpFileId() != null) {// 群组文件
          fileDownloadUrl = fileDownUrlService.getShortDownloadUrl(FileTypeEnum.GROUP, msi.getGrpFileId());
        } else if (msi.getFileId() != null) {
          fileDownloadUrl = fileDownUrlService.getShortDownloadUrl(FileTypeEnum.PSN, msi.getFileId());
        }
        msi.setFileDownloadUrl(fileDownloadUrl);
      }
      form.getMsgShowInfoList().add(msi);
    }

  }

  // 构建基金显示信息
  public void buildFundShowInfo(MsgShowInfo msi) {
    if (MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE_FUND.equals(msi.getSmateInsideLetterType())) {
      String locale = LocaleContextHolder.getLocale().toString();
      if ("en_US".equals(locale)) {
        msi.setShowFundTitle(
            StringUtils.isNotBlank(msi.getFundEnTitle()) ? msi.getFundEnTitle() : msi.getFundZhTitle());
        if (StringUtils.isNotBlank(msi.getEnFundDescBr())) {
          msi.setShowDesc(msi.getEnFundDescBr().replaceAll("\\[br]", "</br>"));
        } else if (StringUtils.isNotBlank(msi.getZhFundDescBr())) {
          msi.setShowDesc(msi.getZhFundDescBr().replaceAll("\\[br]", "</br>"));
        }
      } else {
        msi.setShowFundTitle(
            StringUtils.isNotBlank(msi.getFundZhTitle()) ? msi.getFundZhTitle() : msi.getFundEnTitle());
        if (StringUtils.isNotBlank(msi.getZhFundDescBr())) {
          msi.setShowDesc(msi.getZhFundDescBr().replaceAll("\\[br]", "</br>"));
        } else if (StringUtils.isNotBlank(msi.getEnFundDescBr())) {
          msi.setShowDesc(msi.getEnFundDescBr().replaceAll("\\[br]", "</br>"));
        }
      }
      if (!StringUtils.contains(msi.getFundLogoUrl(), "http")) {
        msi.setFundLogoUrl("/resmod" + msi.getFundLogoUrl());
      }
      if (StringUtils.isBlank(msi.getFundLogoUrl())) {
        msi.setFundLogoUrl("/ressns/images/default/default_fund_logo.jpg");
      }
      // String showDesc = "";
      // if (StringUtils.isNotBlank(msi.getFundAgencyName())) {
      // showDesc += msi.getFundAgencyName();
      // }
      // if (StringUtils.isNotBlank(msi.getFundScienceArea())) {
      // if (StringUtils.isNotBlank(showDesc)) {
      // showDesc += ", " + msi.getFundScienceArea();
      // } else {
      // showDesc += msi.getFundScienceArea();
      // }
      // }
      // if (StringUtils.isNotBlank(msi.getFundApplyTime())) {
      // if (StringUtils.isNotBlank(showDesc)) {
      // showDesc += ", " + msi.getFundApplyTime();
      // } else {
      // showDesc += msi.getFundApplyTime();
      // }
      // }
      // msi.setShowDesc(showDesc);
    }
  }


  // 构建资助机构显示信息
  public void buildAgencyShowInfo(MsgShowInfo msi) {
    if (MsgConstants.MSG_SMATE_INSIDE_LETTER_TYPE_AGENCY.equals(msi.getSmateInsideLetterType())) {
      String locale = LocaleContextHolder.getLocale().toString();
      if (NumberUtils.isNotNullOrZero(msi.getAgencyId())) {
        msi.setDes3AgencyId(Des3Utils.encodeToDes3(msi.getAgencyId().toString()));
      }
      if ("en_US".equals(locale)) {
        msi.setShowAgencyTitle(
            StringUtils.isNotBlank(msi.getAgencyEnTitle()) ? msi.getAgencyEnTitle() : msi.getAgencyZhTitle());
        if (StringUtils.isNotBlank(msi.getEnAgencyDesc())) {
          msi.setShowDesc(msi.getEnAgencyDesc());
        } else if (StringUtils.isNotBlank(msi.getZhAgencyDesc())) {
          msi.setShowDesc(msi.getZhAgencyDesc());
        }
      } else {
        msi.setShowAgencyTitle(
            StringUtils.isNotBlank(msi.getAgencyZhTitle()) ? msi.getAgencyZhTitle() : msi.getAgencyEnTitle());
        if (StringUtils.isNotBlank(msi.getZhAgencyDesc())) {
          msi.setShowDesc(msi.getZhAgencyDesc());
        } else if (StringUtils.isNotBlank(msi.getEnAgencyDesc())) {
          msi.setShowDesc(msi.getEnAgencyDesc());
        }
      }
      if (!StringUtils.contains(msi.getAgencyLogoUrl(), "http")) {
        msi.setAgencyLogoUrl("/resmod" + msi.getAgencyLogoUrl());
      }
      if (StringUtils.isBlank(msi.getAgencyLogoUrl())) {
        msi.setAgencyLogoUrl("/resmod/smate-pc/img/logo_instdefault.png");
      }
    }
  }


  // 构建新闻显示信息
  public void buildNewsShowInfo(MsgShowInfo msi) {

  }
}
