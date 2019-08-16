package com.smate.web.group.action.data;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.file.service.ArchiveFileService;
import com.smate.core.base.utils.app.AppActionUtils;
import com.smate.core.base.utils.constant.IOSHttpStatus;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.group.action.grp.form.GrpFileForm;
import com.smate.web.group.action.grp.form.GrpFileShowInfo;
import com.smate.web.group.action.grp.form.GrpLabelForm;
import com.smate.web.group.action.grp.form.GrpLabelShowInfo;
import com.smate.web.group.model.grp.file.GrpFile;
import com.smate.web.group.model.grp.grpbase.GrpBaseinfo;
import com.smate.web.group.service.grp.file.GrpFileService;
import com.smate.web.group.service.grp.file.GrpFileStatisticsService;
import com.smate.web.group.service.grp.label.GrpFileLabelService;
import com.smate.web.group.service.grp.label.GrpLabelService;
import com.smate.web.group.service.grp.manage.GrpBaseService;
import com.smate.web.group.service.grp.member.GrpMemberShowService;
import com.smate.web.group.service.grp.member.GrpRoleService;

/**
 * @description 移动端获取群组文件数据Action
 * @author xiexing
 * @date 2019年5月13日
 */
public class GroupFileDataAction extends ActionSupport implements ModelDriven<GrpFileForm>, Preparable {
  /**
   * 
   */
  private static final long serialVersionUID = 2960137695877695444L;

  private static final Logger logger = LoggerFactory.getLogger(GroupFileDataAction.class);

  private GrpFileForm grpFileForm;

  @Autowired
  private GrpBaseService grpBaseService;

  @Autowired
  private GrpFileService grpFileService;

  @Autowired
  private GrpRoleService grpRoleService;

  @Autowired
  private ArchiveFileService archiveFileService;

  @Autowired
  private GrpMemberShowService grpMemberShowService;

  @Autowired
  private GrpLabelService grpLabelService;
  @Autowired
  private GrpFileLabelService grpFileLabelService;

  @Autowired
  private GrpFileStatisticsService grpFileStatisticsService;

  /**
   * 移动端获取群组文件列表
   */
  @Action("/grpdata/file/querygrpfilelist")
  public void queryGrpFileList() {
    Long grpId = grpFileForm.getGrpId();
    if (checkParam(grpId)) {
      try {
        buildParam(grpFileForm);
        grpFileService.findGrpFileList(grpFileForm);
        int total = grpFileForm.getPage().getTotalCount().intValue();
        AppActionUtils.renderAPPReturnJson(initShareCount(grpFileForm), total, IOSHttpStatus.OK);// 初始化图片和分享统计数
      } catch (Exception e) {
        AppActionUtils.renderAPPReturnJson("system error", 0, IOSHttpStatus.INTERNAL_SERVER_ERROR);
        logger.error("获取文件列表出错,grpId={}", grpId, e);
      }
    } else {
      AppActionUtils.renderAPPReturnJson("param verify fail", 0, IOSHttpStatus.BAD_REQUEST);
    }
  }

  /**
   * 初始化分享数和图片
   * 
   * @param grpFileForm
   * @return
   */
  public List<GrpFileShowInfo> initShareCount(GrpFileForm grpFileForm) {
    List<GrpFileShowInfo> grpFileShowInfos = grpFileForm.getGrpFileShowInfos();
    if (grpFileShowInfos != null && grpFileShowInfos.size() > 0) {
      try {
        List<Map<String, Object>> list = grpFileStatisticsService.queryShareStatistics();
        Map<String, String> resultMap = new HashMap<String, String>();
        for (Map<String, Object> param : list) {
          resultMap.put(param.get("grpFileId").toString(), param.get("shareCount").toString());
        }
        for (GrpFileShowInfo grpFileShowInfo : grpFileShowInfos) {
          grpFileShowInfo.setShareCount(NumberUtils.toInt(resultMap.get(grpFileShowInfo.getGrpFileId().toString())));
          switch (grpFileShowInfo.getFileType()) {
            case "txt":
              grpFileShowInfo.setImgThumbUrl("/resmod/smate-pc/img/fileicon_txt.png");
              break;
            case "ppt":
            case "pptx":
              grpFileShowInfo.setImgThumbUrl("/resmod/smate-pc/img/fileicon_ppt.png");
              break;
            case "doc":
            case "docx":
              grpFileShowInfo.setImgThumbUrl("/resmod/smate-pc/img/fileicon_doc.png");
              break;
            case "rar":
            case "zip":
              grpFileShowInfo.setImgThumbUrl("/resmod/smate-pc/img/fileicon_zip.png");
              break;
            case "xls":
            case "xlsx":
              grpFileShowInfo.setImgThumbUrl("/resmod/smate-pc/img/fileicon_xls.png");
              break;
            case "pdf":
              grpFileShowInfo.setImgThumbUrl("/resmod/smate-pc/img/fileicon_pdf.png");
              break;
            case "":
              grpFileShowInfo.setImgThumbUrl("/resmod/smate-pc/img/fileicon_default.png");
              break;
            default:
              break;
          }
        }
      } catch (Exception e) {
        logger.error("初始化分享数出错,grpFileForm={}", grpFileForm, e);
      }
    }
    return grpFileShowInfos;
  }

  /**
   * 获取文件详情
   */
  @Action("/grpdata/file/querygrpfiledetail")
  public void queryGrpFileDetail() {
    Long grpFileId = grpFileForm.getGrpFileId();
    Map<String, Object> result = new HashMap<String, Object>();
    if (NumberUtils.isNotNullOrZero(grpFileId)) {
      try {
        GrpFile grpFile = grpFileService.findGrpFile(grpFileForm);
        if (Objects.nonNull(grpFile)) {
          result.put("fileName", grpFile.getFileName());
          result.put("fileDesc", grpFile.getFileDesc());
          result.put("fileType", grpFile.getFileType());
          result.put("imgThumbUrl", archiveFileService.getImgFileThumbUrl(grpFile.getArchiveFileId()));
          result.put("fileDes3GrpId", grpFile.getDes3GrpFileId());
          result.put("status", IOSHttpStatus.OK);
          result.put("msg", "query data success");
        } else {
          result.put("status", IOSHttpStatus.NOT_FOUND);
          result.put("msg", "resource is not exist");
        }
      } catch (Exception e) {
        logger.error("获取群组文件详情失败,grpFileId={}", grpFileId, e);
        result.put("status", IOSHttpStatus.INTERNAL_SERVER_ERROR);
        result.put("msg", "system error");
      }
    } else {
      result.put("status", IOSHttpStatus.BAD_REQUEST);
      result.put("msg", "param verify fail");
    }
    Struts2Utils.renderJson(result, "encoding:UTF-8");
  }

  /**
   * 获取群组成员与文件标签
   */
  @Action("/grpdata/file/queryconditions")
  public void queryConditions() {
    Long grpId = grpFileForm.getGrpId();
    Map<String, Object> result = new HashMap<String, Object>();
    if (NumberUtils.isNotNullOrZero(grpId)) {
      try {
        // GrpMemberForm form = new GrpMemberForm();
        // form.setGrpId(grpId);
        // List<PsnInfo> psnInfos = grpMemberShowService.queryGrpMembers(form);
        grpFileService.findGrpFileMember(grpFileForm);

        GrpLabelForm labelForm = new GrpLabelForm();
        labelForm.setPsnId(grpFileForm.getPsnId());
        labelForm.setGrpId(grpId);
        labelForm.setFileModuleType(grpFileForm.getGrpFileType());
        List<GrpLabelShowInfo> list = grpFileLabelService.getAllFileLabel(labelForm);

        if (grpFileForm.getGrpFileMemberList() == null) {
          result.put("status", IOSHttpStatus.NOT_FOUND);
          result.put("msg", "resource is not exist");
        } else {
          result.put("psnInfos", grpFileForm.getGrpFileMemberList());
          result.put("grpLabels", list);
          result.put("status", IOSHttpStatus.OK);
          result.put("msg", "query data success");
        }
      } catch (Exception e) {
        logger.error("获取群组文件详情失败,grpId={}", grpId, e);
        result.put("status", IOSHttpStatus.INTERNAL_SERVER_ERROR);
        result.put("msg", "system error");
      }
    } else {
      result.put("status", IOSHttpStatus.BAD_REQUEST);
      result.put("msg", "param verify fail");
    }
    Struts2Utils.renderJson(result, "encoding:UTF-8");
  }

  /**
   * 获取选中的grp信息
   */
  @Action("/grpdata/file/grpinfo")
  public void grpInfo() {
    Long grpId = grpFileForm.getGrpId();
    Map<String, Object> result = new HashMap<String, Object>();
    if (NumberUtils.isNotNullOrZero(grpId)) {
      try {
        GrpBaseinfo grp = grpBaseService.getCurrGrp(grpId);
        if (Objects.isNull(grp)) {
          result.put("status", IOSHttpStatus.NOT_FOUND);
          result.put("msg", "resource is not exist");
        } else {
          result.put("grp", grp);
          result.put("status", IOSHttpStatus.OK);
          result.put("msg", "query data success");
        }
      } catch (Exception e) {
        logger.error("获取群组文件详情失败,grpId={}", grpId, e);
        result.put("status", IOSHttpStatus.INTERNAL_SERVER_ERROR);
        result.put("msg", "system error");
      }
    } else {
      result.put("status", IOSHttpStatus.BAD_REQUEST);
      result.put("msg", "param verify fail");
    }
    Struts2Utils.renderJson(result, "encoding:UTF-8");
  }

  /**
   * 群组文件分享给联系人
   */
  @Action("/grpdata/file/sharetofriend")
  public void shareToFriend() {
    Map<String, Object> map = new HashMap<String, Object>();
    if (verifyParam(grpFileForm)) {
      try {
        // 通过id分享
        if (StringUtils.isNoneBlank(grpFileForm.getDes3ReceiverIds())) {
          grpFileService.shareGrpFiles(grpFileForm);
        }
        // 通过邮件分享
        if (StringUtils.isNotBlank(grpFileForm.getReceiverEmails())) {
          grpFileService.shareGrpFilesByEmail(grpFileForm);
        }
        // 统计分享数
        grpFileStatisticsService.countShare(grpFileForm);
        map.put("status", "success");
        map.put("msg", "share success");
      } catch (Exception e) {
        logger.error("分享群组文件到联系人失败,psnId={}", grpFileForm.getPsnId(), e);
        map.put("status", "error");
        map.put("msg", "system error");
      }
    } else {
      map.put("status", "error");
      map.put("msg", "param verify fail");
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
  }

  /**
   * 提供统计分享数的接口
   */
  @Action("/grpdata/file/countshare")
  public void countShare() {
    Long grpFileId = grpFileForm.getGrpFileId();
    Map<String, Object> result = new HashMap<String, Object>();
    if (NumberUtils.isNotNullOrZero(grpFileId)) {
      try {
        grpFileStatisticsService.countShare(grpFileForm);
        result.put("status", IOSHttpStatus.OK);
        result.put("msg", "count share success");
      } catch (Exception e) {
        logger.error("获取群组文件详情失败,grpFileId={}", grpFileId, e);
        result.put("status", IOSHttpStatus.INTERNAL_SERVER_ERROR);
        result.put("msg", "system error");
      }
    } else {
      result.put("status", IOSHttpStatus.BAD_REQUEST);
      result.put("msg", "param verify fail");
    }
    Struts2Utils.renderJson(result, "encoding:UTF-8");
  }

  /**
   * 参数校验
   * 
   * @param grpFileForm
   * @return
   */
  public boolean verifyParam(GrpFileForm grpFileForm) {
    Long psnId = grpFileForm.getPsnId();
    Long grpId = grpFileForm.getGrpId();
    List<Long> grpFileIds = grpFileForm.getGrpFileIdList();
    String des3ReceiverIds = grpFileForm.getDes3ReceiverIds();
    String receiverEmails = grpFileForm.getReceiverEmails();
    if (NumberUtils.isNotNullOrZero(psnId) && NumberUtils.isNotNullOrZero(grpId) && grpFileIds != null
        && grpFileIds.size() > 0
        && (StringUtils.isNotEmpty(des3ReceiverIds) || StringUtils.isNotEmpty(receiverEmails))) {
      return true;
    }
    return false;
  }



  /**
   * 收藏文件grpId,fileId
   * 
   * @return
   */
  @Action("/grpdata/file/collect")
  public void collectGrpFile() {
    Map<String, String> map = new HashMap<String, String>();
    try {
      if (StringUtils.isNotBlank(grpFileForm.getDes3PsnId())
          && CollectionUtils.isNotEmpty(grpFileForm.getGrpFileIdList())) {
        grpFileForm.setPsnId(NumberUtils.toLong(Des3Utils.decodeFromDes3(grpFileForm.getDes3PsnId()), 0L));
        grpFileService.collectGrpFile(grpFileForm);
        map.put("count", grpFileForm.getBatchCount().toString());
        map.put("status", "success");
      } else {
        map.put("status", "error");
      }
    } catch (Exception e) {
      logger.error("收藏群组文件异常， psnId={}", grpFileForm.getPsnId(), e);
      map.put("status", "error");
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
  }



  /**
   * 构建查询列表的参数
   * 
   * @param grpFileForm
   * @return
   * @throws Exception
   */
  public GrpFileForm buildParam(GrpFileForm grpFileForm) throws Exception {
    if (Objects.isNull(grpFileForm)) {
      return null;
    }
    // 处理特殊字符串
    grpFileForm.setSearchKey(StringEscapeUtils.unescapeHtml4(grpFileForm.getSearchKey()));
    // TODO 获取群的类别
    grpFileForm.setGrpCategory(grpBaseService.getGrpCategory(grpFileForm.getGrpId()));
    // // 1=群组拥有者,2=管理员,3=组员
    int role = grpRoleService.getGrpRole(grpFileForm.getPsnId(), grpFileForm.getGrpId());
    grpFileForm.setGrpRole(role);
    return grpFileForm;
  }

  /**
   * 校验获取文件列表参数
   * 
   * @param psnId
   * @param grpId
   * @return
   */
  public boolean checkParam(Long psnId) {
    if (NumberUtils.isNotNullOrZero(psnId)) {
      return true;
    }
    return false;
  }

  @Override
  public GrpFileForm getModel() {

    return grpFileForm;
  }

  @Override
  public void prepare() throws Exception {
    if (grpFileForm == null) {
      grpFileForm = new GrpFileForm();
    }
    LocaleContextHolder.setLocale(Locale.SIMPLIFIED_CHINESE);
  }
}
