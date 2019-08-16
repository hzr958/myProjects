package com.smate.web.psn.action.file;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.psn.model.StationFile;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.psn.form.StationFileInfo;
import com.smate.web.psn.model.psninfo.PsnInfo;
import com.smate.web.psn.service.file.MyFileService;
import com.smate.web.psn.service.profile.PersonManager;

/**
 * 我的文件Action
 * 
 * @author zk
 *
 */
@Results({@Result(name = "file_for_group", location = "/WEB-INF/jsp/myfile/myfileforgroup.jsp"),
    @Result(name = "file_for_grp", location = "/WEB-INF/jsp/myfile/myfileforgrp.jsp"),
    @Result(name = "file_for_psn", location = "/WEB-INF/jsp/file/myfile_list.jsp"),
    @Result(name = "file_for_group_sub", location = "/WEB-INF/jsp/myfile/myfileforgroupsub.jsp")})
public class MyFileAction extends ActionSupport implements Preparable {

  private static final long serialVersionUID = 5920613243148646193L;

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Resource
  private MyFileService myFileService;
  @Autowired
  private PersonManager personManager;
  private List<StationFile> sfList; // 用户文件列表
  private List<StationFileInfo> stationFileInfos; // 用户文件列表

  private Page<StationFile> page = new Page<StationFile>();
  private Integer pageSize = 5; // 每页默认显示数量
  private Integer pageNo = 1; // 每次获取数量
  private Long randomStr; // 随机数据
  private String groupOpenType;// 如果是“O”则是非群组成员访问群组，隐藏某些操作
  private PsnInfo psnInfo = new PsnInfo(); // 人员信息
  private String searchKey; // 查询的关键词
  private String des3FileId;
  private boolean isPF = false;// 来源与个人文件列表请求

  /**
   * 在群组文模块获取的文件列表
   * 
   * @return
   */
  @Action("/psnweb/myfile/ajaxfileforgroup")
  public String ajaxFileForGroup() {
    try {
      String des3PsnId = Struts2Utils.getParameter("des3PsnId");
      String des3GroupId = Struts2Utils.getParameter("des3GroupId");
      Long psnId = Long.valueOf(Des3Utils.decodeFromDes3(des3PsnId));
      Long groupId = Long.valueOf(Des3Utils.decodeFromDes3(des3GroupId));
      if (psnId > 0 && groupId > 0) {
        if (pageNo == null && pageNo <= 0) {
          pageNo = 1;
        }
        if (pageSize == null && pageSize <= 0) {
          pageSize = 5;
        }
        if (pageSize == 1 && pageNo == 2) {
          pageNo = 5;
        }
        sfList = myFileService.findFileForGroup(psnId, groupId, pageSize, pageNo);
      }
      randomStr = Math.round((Math.random() * 10000000));
    } catch (Exception e) {
      logger.error("在群组文模块获取的文件列表出错", e);
    }
    if (pageSize == 5) {
      return "file_for_group";
    } else {
      return "file_for_group_sub";
    }
  }

  /**
   * 在群组文模块获取的文件列表_最新_2017-03
   * 
   * @return
   */
  @Action("/psnweb/myfile/ajaxfileforgrp")
  public String ajaxFileForGrp() {
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      Long grpId = NumberUtils.toLong((Struts2Utils.getParameter("grpId")));
      if (grpId == null || grpId.equals(0L)) {
        grpId = NumberUtils.toLong(Des3Utils.decodeFromDes3(Struts2Utils.getParameter("des3GrpId")));
      }
      if (psnId > 0 && grpId > 0) {
        if (pageNo == null || pageNo <= 0) {
          pageNo = 1;
        }
        if (pageSize == null || pageSize < 10) {
          pageSize = 10;
        }
        stationFileInfos = myFileService.findFileForGrp(psnId, grpId, page, getSearchKey());
        Person person = personManager.getPersonInfo(psnId);
        psnInfo.setName(personManager.getPsnName(person, LocaleContextHolder.getLocale().toString()));
      }
      randomStr = Math.round((Math.random() * 10000000));
    } catch (Exception e) {
      logger.error("在群组文模块获取的文件列表出错", e);
    }
    return "file_for_grp";
  }

  /**
   * 获取个人的文件列表
   * 
   * @return
   */
  @Action("/psnweb/myfile/ajaxfileforpsn")
  public String ajaxFileForPsn() {
    try {
      Long psnId = SecurityUtils.getCurrentUserId();
      if (pageNo == null || pageNo <= 0) {
        pageNo = 1;
      }
      if (pageSize == null || pageSize < 10) {
        pageSize = 10;
      }
      stationFileInfos = myFileService.findFileForPsn(psnId, page, getSearchKey());
      Person person = personManager.getPersonInfo(psnId);
      psnInfo.setName(personManager.getPsnName(person, LocaleContextHolder.getLocale().toString()));
      randomStr = Math.round((Math.random() * 10000000));
    } catch (Exception e) {
      logger.error("获取个人的文件列表出错!page=" + page, e);
    }
    if (this.isPF) {
      return "file_for_psn";
    } else {
      return "file_for_grp";
    }
  }

  /**
   * 收藏个人文件 des3FileId
   * 
   * @return
   */
  @Action("/psnweb/myfile/ajaxcollectionstationfile")
  public String ajaxCollectionStationFile() {
    Map<String, Object> map = new HashMap<String, Object>();
    Long psnId = SecurityUtils.getCurrentUserId();
    try {

      Long fileId = NumberUtils.toLong(Des3Utils.decodeFromDes3(this.des3FileId));
      if (psnId != null && psnId != 0L && fileId != null) {
        myFileService.collectionStationFile(psnId, fileId);
        map.put("result", "success");
      } else {
        map.put("result", "error");
      }
    } catch (Exception e) {
      logger.error("收藏个人文件，异常psnId=" + psnId, e);
      map.put("result", "error");
    }
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }

  @Override
  public void prepare() throws Exception {

  }

  public List<StationFile> getSfList() {
    return sfList;
  }

  public void setSfList(List<StationFile> sfList) {
    this.sfList = sfList;
  }

  public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public Integer getPageNo() {
    return pageNo;
  }

  public void setPageNo(Integer pageNo) {
    this.pageNo = pageNo;
  }

  public Long getRandomStr() {
    return randomStr;
  }

  public void setRandomStr(Long randomStr) {
    this.randomStr = randomStr;
  }

  public String getGroupOpenType() {
    return groupOpenType;
  }

  public void setGroupOpenType(String groupOpenType) {
    this.groupOpenType = groupOpenType;
  }

  public PsnInfo getPsnInfo() {
    return psnInfo;
  }

  public void setPsnInfo(PsnInfo psnInfo) {
    this.psnInfo = psnInfo;
  }

  public String getSearchKey() {
    if (StringUtils.isNotBlank(searchKey)) {// 过滤特殊字符
      searchKey = StringEscapeUtils.unescapeHtml4(searchKey).toUpperCase().trim();
    }
    return searchKey;
  }

  public void setSearchKey(String searchKey) {
    this.searchKey = searchKey;
  }

  public List<StationFileInfo> getStationFileInfos() {
    return stationFileInfos;
  }

  public void setStationFileInfos(List<StationFileInfo> stationFileInfos) {
    this.stationFileInfos = stationFileInfos;
  }

  public Page<StationFile> getPage() {
    return page;
  }

  public void setPage(Page<StationFile> page) {
    this.page = page;
  }

  public String getDes3FileId() {
    return des3FileId;
  }

  public void setDes3FileId(String des3FileId) {
    this.des3FileId = des3FileId;
  }

  public boolean getIsPF() {
    return isPF;
  }

  public void setIsPF(boolean isPF) {
    this.isPF = isPF;
  }

}
