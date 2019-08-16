package com.smate.web.institution.action;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.institution.consts.InstitutionConsts;
import com.smate.web.institution.form.InstitutionForm;
import com.smate.web.institution.service.InstitutionOptService;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * 机构操作的action
 *
 * @author AJB
 *
 */

@Results ({@Result (name = "create_main", location = "/WEB-INF/jsp/institution/create_main.jsp"),
           @Result (name = "add_economic", location = "/WEB-INF/jsp/institution/add_economic.jsp"),
           @Result (name = "add_csei", location = "/WEB-INF/jsp/institution/add_csei.jsp"),
           @Result (name = "my_page", location = "/WEB-INF/jsp/institution/my_page.jsp"),
           @Result (name = "my_ins_page_list", location = "/WEB-INF/jsp/institution/my_ins_page_list.jsp"),
           @Result (name = "my_follow_ins_page_list", location = "/WEB-INF/jsp/institution/my_follow_ins_page_list.jsp"),
        })
public class InstitutionOptAction extends ActionSupport implements Preparable, ModelDriven<InstitutionForm> {

  private static final long serialVersionUID = -9090557623490845340L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private InstitutionForm form;
  @Autowired
  private InstitutionOptService institutionOptService;



  /**
   * 进入创建机构主页jsp
   * @return
   */
  @Actions({@Action("/prjweb/ins/create/main")})
  public String createInsMain() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {

    } catch (Exception e) {
      logger.error("进入创建机构主页异常", e);
    }
    return "create_main";
  }


  /**
   * 进入我的机构主页
   * @return
   */
  @Actions({@Action("/prjweb/ins/mypage")})
  public String insMainPage() {
    Map<String, Object> map = new HashMap<String, Object>();

    return "my_page";
  }
  /**
   * 获取我的机构主页列表
   * @return
   */
  @Actions({@Action("/prjweb/ins/ajaxinspagelist")})
  public String ajaxMyInsPageList() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      institutionOptService.findInsPageList(form);
    } catch (Exception e) {
      logger.error("查询机构主页列表异常", e);
    }
    if(form.getInsPageType() == 1){
      return "my_ins_page_list";
    }
    return "my_follow_ins_page_list";
  }

  /**
   * 添加经济产业
   *
   * @return
   */
  @Action("/prjweb/ins/create/ajaxaddeconomic")
  public String ajaxAddEconomic() {
    try {
      // 获取经济产业构建成的Map
      institutionOptService.buildEconomicInfo(form);
    } catch (Exception e) {
      logger.error("经济产业弹出框出错，psnId = " + form.getPsnId(), e);
    }
    return "add_economic";
  }

  /**
   * 新兴产业代码
   *
   * @return
   */
  @Action("/prjweb/ins/create/ajaxaddcsei")
  public String ajaxAddCsei() {
    try {
      // 获取新兴产业代码构建成的Map
      institutionOptService.buildCseiInfo(form);
    } catch (Exception e) {
      logger.error("新兴产业代码弹出框出错，psnId = " + form.getPsnId(), e);
    }
    return "add_csei";
  }




  /**i
   * 检查机构名称或者单位
   * 
   * @return
   */
  @Actions({@Action("/prjweb/ins/checkins")})
  public String checkInsName() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
       if(validCheckInsName(form)){
         Boolean flag = institutionOptService.checkInsName(form);
         map.put("result",flag ? "success" :"error");
         map.put("domain", InstitutionConsts.HTTPS+form.getInsDomain());
       }else{
         map.put("result","error");
       }
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("检查机构出错,psnId=", e);
    }
    Struts2Utils.renderJson(map, "encoding:utf-8");
    return null;
  }

  public boolean  validCheckInsName(InstitutionForm form){
    if(form.getCheckInsNameType() == null){
      return false;
    }
    if(form.getCheckInsNameType() == 1 && StringUtils.isNotBlank(form.getInsName())){
      return  true ;
    }
    if(form.getCheckInsNameType() == 2 && StringUtils.isNotBlank(form.getInsDomain())){
      return  true ;
    }
    return false;
  }

  /**
   *保存机构主页
   */
  @Action("/prjweb/ins/ajaxsaveins")
  public String ajaxSaveIns() throws Exception {
    Map<String, Object> result = new HashMap<String, Object>();
    dealFormDataLength();
    if(validCreateInsData()){
      boolean   flag = institutionOptService.createInsPage(form);
      result.put("status",flag ? "success" : "error");
      result.put("forwardUrl",form.getForwardUrl());
    }else{
      result.put("status","error");
    }
    Struts2Utils.renderJson(result, "encoding:UTF-8");
    return null;
  }
  public boolean validCreateInsData(){
    return true ;
  }
  public void dealFormDataLength(){
     //form.setInsName(StringUtils.subMaxLengthString(form.getInsName(),20));
     //form.setInsDomain(StringUtils.subMaxLengthString(form.getInsDomain(),10));
     //form.setUrl(StringUtils.subMaxLengthString(form.getUrl(),100));
     //form.setDescription(StringUtils.subMaxLengthString(form.getDescription(),400));
  }


  /**i
   * 取消关注机构
   *
   * @return
   */
  @Actions({@Action("/prjweb/ins/ajaxcancelfollow")})
  public String cancelFollow() {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      if(NumberUtils.isNotNullOrZero(form.getInsId()) && NumberUtils.isNotNullOrZero(form.getPsnId()) ){
        Boolean flag = institutionOptService.cancelFollow(form);
        map.put("result",flag ? "success" :"error");
      }else{
        map.put("result","error");
      }
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("取消关注出错,psnId=", e);
    }
    Struts2Utils.renderJson(map, "encoding:utf-8");
    return null;
  }

  @Override
  public InstitutionForm getModel() {
    if(form == null){
      form = new InstitutionForm();
    }
    return form;
  }

  @Override
  public void prepare() throws Exception {

  }
}
