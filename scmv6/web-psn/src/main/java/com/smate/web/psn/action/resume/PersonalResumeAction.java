package com.smate.web.psn.action.resume;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.form.resume.PersonalResumeForm;
import com.smate.web.psn.model.consts.ResumeModuleConsts;
import com.smate.web.psn.service.resume.PsnResumeBuildService;
import com.smate.web.psn.service.resume.PsnResumeBuilder;

/**
 * 个人简历Action
 * 
 * @author lhd
 *
 */
@Results({@Result(name = "resume_detail", location = "/WEB-INF/jsp/resume/resume_detail.jsp"),
    @Result(name = "resume_list", location = "/WEB-INF/jsp/resume/resume_list.jsp"),
    @Result(name = "resume_education", location = "/WEB-INF/jsp/resume/resume_education.jsp"),
    @Result(name = "resume_workinfo", location = "/WEB-INF/jsp/resume/resume_workinfo.jsp"),
    @Result(name = "resume_prj", location = "/WEB-INF/jsp/resume/resume_prj.jsp"),
    @Result(name = "resume_prj_list", location = "/WEB-INF/jsp/resume/resume_prj_list.jsp"),
    @Result(name = "psn_prj_list", location = "/WEB-INF/jsp/resume/psn_prj_list.jsp"),
    @Result(name = "editEdu", location = "/WEB-INF/jsp/resume/psn_eduhistory_edit.jsp"),
    @Result(name = "editWork", location = "/WEB-INF/jsp/resume/psn_workhistory_edit.jsp"),
    @Result(name = "editPsnInfo", location = "/WEB-INF/jsp/resume/psn_psnInfo_edit.jsp"),
    @Result(name = "resume_pub_info", location = "/WEB-INF/jsp/resume/resume_pub_info.jsp")})
public class PersonalResumeAction extends ActionSupport implements ModelDriven<PersonalResumeForm>, Preparable {

  private static final long serialVersionUID = 6560197519992929786L;
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private PersonalResumeForm form;
  @Autowired
  private PsnResumeBuilder psnResumeBuilder;
  private PsnResumeBuildService psnResumeBuildService;

  /**
   * 导出word简历
   * 
   * @return
   */
  @Action("/psnweb/resume/ajaxwordexport")
  public String exportWord() {
    try {
      // TODO 判断是否为简历拥有者
      if (form.getPsnId() != null && form.getPsnId() > 0l) {
        HttpServletResponse response = Struts2Utils.getResponse();
        // String randResumeId = randResumeId();
        response.setCharacterEncoding("utf-8");
        response.setContentType("APPLICATION/msword");
        File file = psnResumeBuildService.exportCVToWord(form);
        String userAgent = Struts2Utils.getRequest().getHeader("User-Agent");
        String fileName = form.getCvFileName();
        // 针对IE或者以IE为内核的浏览器：
        if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
          fileName = URLEncoder.encode(fileName, "UTF-8");
        } else { // 非IE浏览器的处理：
          fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        }
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        // File file = wordService.exportWordFile(form);
        FileInputStream fis = new FileInputStream(file);
        OutputStream out = response.getOutputStream();
        BufferedInputStream bif = new BufferedInputStream(fis);
        byte[] buffer = new byte[fis.available()];
        if (buffer.length > 0) {
          while (fis.read(buffer) != -1) {
            out.write(buffer);
          }
        }
        out.flush();
        out.close();
        bif.close();
        // 删除临时文件
        if (file.exists()) {
          file.delete();
        }
      }
    } catch (Exception e) {
      logger.error("导出word简历出错,psnId=" + form.getPsnId(), e);
    }
    return null;
  }

  /**
   * 修改简历名称
   * 
   * @return
   */
  @Action("/psnweb/resume/ajaxeditcvname")
  public String ajaxeditcvname() {
    Map<String, String> dataMap = new HashMap<String, String>();
    try {
      psnResumeBuildService.editCvName(form);
      dataMap.put("resumeName", form.getResumeName());
      dataMap.put("result", "success");
    } catch (Exception e) {
      logger.error("修改简历名称出错， psnId = " + form.getPsnId() + ", resumeId = " + form.getResumeId(), e);
      dataMap.put("result", "error");
    }
    dataMap.put("baseinfo", form.getPsnBaseInfo());
    Struts2Utils.renderJson(dataMap, "encoding:UTF-8");
    return null;
  }

  /**
   * 构建人员基本信息
   * 
   * @return
   */
  @Action("/psnweb/resume/ajaxbaseinfo")
  public String buildPsnResumeBaseInfo() {
    Map<String, String> dataMap = new HashMap<String, String>();
    try {
      psnResumeBuildService.buildPsnBaseInfo(form);
      dataMap.put("result", "success");
    } catch (Exception e) {
      logger.error("构建人员基本信息出错， psnId = " + form.getPsnId() + ", resumeId = " + form.getResumeId(), e);
      dataMap.put("result", "error");
    }
    dataMap.put("baseinfo", form.getPsnBaseInfo());
    dataMap.put("isShowMoule", form.getIsShowMoule());
    Struts2Utils.renderJson(dataMap, "encoding:UTF-8");
    return null;
  }

  /**
   * 修改人员基本信息
   * 
   * @return
   */
  @Action("/psnweb/resume/ajaxeditpsninfo")
  public String ajaxeditpsninfo() {
    Map<String, String> dataMap = new HashMap<String, String>();
    try {
      psnResumeBuildService.editpsninfo(form);
      dataMap.put("result", "success");
      dataMap.put("psnBaseInfo", form.getPsnBaseInfo());
    } catch (Exception e) {
      logger.error("修改人员基本信息出错， psnId = " + form.getPsnId() + ", resumeId = " + form.getResumeId(), e);
      dataMap.put("result", "error");
    }
    dataMap.put("baseinfo", form.getPsnBaseInfo());
    Struts2Utils.renderJson(dataMap, "encoding:UTF-8");
    return null;
  }

  /**
   * 获取人员基本信息
   * 
   * @return
   */
  @Action("/psnweb/resume/ajaxgetpsninfo")
  public String ajaxgetpsninfo() {
    try {
      psnResumeBuildService.getpsninfo(form);
      return "editPsnInfo";
    } catch (Exception e) {
      logger.error("获取人员基本信息出错， psnId = " + form.getPsnId() + ", resumeId = " + form.getResumeId(), e);
    }
    return null;
  }

  /**
   * 获取工作经历
   * 
   * @return
   */
  @Action("/psnweb/resume/ajaxworkinfo")
  public String buildPsnResumeWorkInfo() {
    try {
      psnResumeBuildService.buildPsnWorkInfo(form);
    } catch (Exception e) {
      logger.error("获取工作经历出错,psnId=" + form.getPsnId(), e);
    }
    return "resume_workinfo";
  }

  /**
   * 获取教育经历
   * 
   * @return
   */
  @Action("/psnweb/resume/ajaxeduinfo")
  public String buildPsnResumeEduInfo() {
    try {
      psnResumeBuildService.buildPsnEduInfo(form);
    } catch (Exception e) {
      logger.error("获取教育经历出错,psnId=" + form.getPsnId(), e);
    }
    return "resume_education";
  }

  /**
   * 简历项目信息
   * 
   * @return
   */
  @Action("/psnweb/resume/ajaxprjinfo")
  public String buildPsnResumePrjInfo() {
    try {
      form.setModuleId(ResumeModuleConsts.PRJ_INFO);
      psnResumeBuildService.buildPsnResumePrjInfo(form);
    } catch (Exception e) {
      logger.error("简历项目信息出错,psnId= " + form.getPsnId(), e);
    }
    return "resume_prj";
  }

  /**
   * 更新项目信息
   * 
   * @return
   */
  @Action("/psnweb/resume/ajaxupdateprjinfo")
  public String updatePrjInfo() {
    try {
      if (psnResumeBuilder.isPsnResumeOwner(form.getResumeId())) {
        form.setModuleId(ResumeModuleConsts.PRJ_INFO);
        psnResumeBuildService.updateResumePrjInfo(form);
      }
    } catch (Exception e) {
      logger.error("更新项目信息出错,psnId= " + form.getPsnId(), e);
    }
    return "resume_prj";
  }

  /**
   * 已导入项目信息
   * 
   * @return
   */
  @Action("/psnweb/resume/ajaxresumeprjlist")
  public String addedPrjInfo() {
    try {
      psnResumeBuildService.getResumePrjList(form);
    } catch (Exception e) {
      logger.error("更新项目信息出错,psnId= " + form.getPsnId(), e);
    }
    return "resume_prj_list";
  }

  /**
   * 获取个人项目列表
   * 
   * @return
   */
  @Action("/psnweb/resume/ajaxprjlist")
  public String getPsnPrjInfo() {
    try {
      psnResumeBuildService.queryPrjInfo(form);
    } catch (Exception e) {
      logger.error("更新项目信息出错,psnId= " + form.getPsnId(), e);
    }
    return "psn_prj_list";
  }

  /**
   * 简历成果信息
   * 
   * @return
   */
  @Action("/psnweb/resume/ajaxpubinfo")
  public String buildPsnResumePubInfo() {
    try {
      psnResumeBuildService.buildPsnResumePubInfo(form);
    } catch (Exception e) {
      logger.error("获取简历代表性成果出错， psnId = " + SecurityUtils.getCurrentUserId() + ", resumeId=" + form.getResumeId(), e);
    }
    return "resume_pub_info";
  }

  /**
   * 创建简历
   * 
   * @return
   */
  @Action("/psnweb/resume/ajaxadd")
  public String createPsnResume() {
    Map<String, String> resultMap = new HashMap<String, String>();
    try {
      psnResumeBuildService.createPsnResume(form);
      if (StringUtils.isNotBlank(form.getResultMsg())) {
        resultMap.put("result", "overtake");
      } else {
        resultMap.put("des3ResumeId", form.getDes3ResumeId());
      }
      Struts2Utils.renderJson(resultMap, "encoding:UTF-8");
    } catch (Exception e) {
      logger.error("创建简历出错,form= " + form, e);
    }
    return null;
  }

  /**
   * 简历详情
   * 
   * @return
   */
  @Action("/psnweb/resume/resumedetail")
  public String resumedetail() {
    try {
      psnResumeBuilder.initPsnCVInfo(form);
    } catch (Exception e) {
      logger.error("进入人员简历出错， psnId = " + SecurityUtils.getCurrentUserId() + ", cvId=" + form.getResumeId(), e);
    }
    return "resume_detail";
  }

  /**
   * 删除简历
   * 
   * @return
   */
  @Action("/psnweb/resume/ajaxdelete")
  public String deletePsnResume() {
    Map<String, String> result = new HashMap<String, String>();
    try {
      if (form.getPsnId() != null && form.getPsnId() > 0l && form.getResumeId() != null && form.getResumeId() > 0L) {
        psnResumeBuilder.deletePsnResume(form.getResumeId());
      }
      result.put("result", "success");
    } catch (Exception e) {
      logger.error("删除个人简历出错， psnId = " + form.getPsnId() + ", resumeId = " + form.getResumeId(), e);
      result.put("result", "error");
    }
    Struts2Utils.renderJson(result, "encoding:utf-8");
    return null;
  }

  /**
   * 查找简历列表
   * 
   * @return
   */
  @Action("/psnweb/resume/ajaxlist")
  public String findPsnResumeList() {
    try {
      psnResumeBuilder.findPsnResumeList(form);
    } catch (PsnException e) {
      logger.error("查找简历列表出错,psnId= " + form.getPsnId(), e);
    }
    if (CollectionUtils.isEmpty(form.getResumeList())) {
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("msg", "toDetail");
      Struts2Utils.renderJson(map, "encoding:utf-8");
      return null;
    }
    return "resume_list";
  }

  /**
   * 保存代表性成果信息
   * 
   * @return
   */
  @Action("/psnweb/resumesave/ajaxrepresentpub")
  public String savePsnCVRepresentPubInfo() {
    Map<String, String> resultMap = new HashMap<String, String>();
    try {
      boolean result = psnResumeBuilder.checkPubInfo(form);
      if (result) {
        psnResumeBuilder.savePsnResumeModule(form);
      } else {
        resultMap.put("pubCheck", "false");
      }
      resultMap.put("result", "success");
    } catch (Exception e) {
      resultMap.put("result", "error");
      logger.error("保存个人简历成果信息出错， psnId = " + form.getPsnId() + ", resumeId = " + form.getResumeId(), e);
    }
    Struts2Utils.renderJson(resultMap, "encoding:utf-8");
    return null;
  }

  /**
   * 编辑添加教育经历
   * 
   * @return
   */
  @Action("/psnweb/resume/ajaxupdateedu")
  public String ajaxupdateedu() {
    Map<String, String> resultMap = new HashMap<String, String>();
    try {
      psnResumeBuildService.updateEduInfo(form);
      resultMap.put("result", "success");
    } catch (Exception e) {
      logger.error("编辑添加教育经历出错， psnId = " + form.getPsnId() + ", resumeId = " + form.getResumeId(), e);
      resultMap.put("result", "error");
    }
    Struts2Utils.renderJson(resultMap, "encoding:UTF-8");
    return null;
  }

  /**
   * 编辑添加工作经历
   * 
   * @return
   */
  @Action("/psnweb/resume/ajaxupdateWork")
  public String ajaxupdatework() {
    Map<String, String> resultMap = new HashMap<String, String>();
    try {
      psnResumeBuildService.updateWorkInfo(form);
      resultMap.put("result", "success");
    } catch (Exception e) {
      logger.error("编辑添加工作经历出错， psnId = " + form.getPsnId() + ", resumeId = " + form.getResumeId(), e);
      resultMap.put("result", "error");
    }
    Struts2Utils.renderJson(resultMap, "encoding:UTF-8");
    return null;
  }

  /**
   * 删除教育经历
   * 
   * @return
   */
  @Action("/psnweb/resume/ajaxdeleteedu")
  public String ajaxdeleteedu() {
    Map<String, String> resultMap = new HashMap<String, String>();
    try {
      psnResumeBuildService.deleteEduInfo(form);
      resultMap.put("result", "success");
    } catch (Exception e) {
      logger.error("删除教育经历出错， psnId = " + form.getPsnId() + ", resumeId = " + form.getResumeId(), e);
      resultMap.put("result", "error");
    }
    Struts2Utils.renderJson(resultMap, "encoding:UTF-8");
    return null;
  }

  /**
   * 删除工作经历
   * 
   * @return
   */
  @Action("/psnweb/resume/ajaxdeletework")
  public String ajaxdeletework() {
    Map<String, String> resultMap = new HashMap<String, String>();
    try {
      psnResumeBuildService.deleteWorkInfo(form);
      resultMap.put("result", "success");
    } catch (Exception e) {
      logger.error("删除工作经历出错， psnId = " + form.getPsnId() + ", resumeId = " + form.getResumeId(), e);
      resultMap.put("result", "error");
    }
    Struts2Utils.renderJson(resultMap, "encoding:UTF-8");
    return null;
  }

  /**
   * 获取教育经历
   * 
   * @return
   */
  @Action("/psnweb/resume/ajaxeditedu")
  public String ajaxeditedu() {
    try {
      psnResumeBuildService.getEduInfo(form);
    } catch (Exception e) {
      logger.error("获取教育经历出错， psnId = " + form.getPsnId() + ", resumeId = " + form.getResumeId(), e);
    }
    return "editEdu";
  }

  /**
   * 获取工作经历
   * 
   * @return
   */
  @Action("/psnweb/resume/ajaxeditwork")
  public String ajaxeditwork() {
    try {
      psnResumeBuildService.getWorkInfo(form);
    } catch (Exception e) {
      logger.error("获取工作经历出错， psnId = " + form.getPsnId() + ", resumeId = " + form.getResumeId(), e);
    }
    return "editWork";
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new PersonalResumeForm();
    }
    // 初始化简历服务类型
    if (StringUtils.isNotBlank(form.getServiceType())) {
      psnResumeBuildService = psnResumeBuilder.initPsnResumeService(form.getServiceType());
    }

  }

  @Override
  public PersonalResumeForm getModel() {
    return form;
  }

}
