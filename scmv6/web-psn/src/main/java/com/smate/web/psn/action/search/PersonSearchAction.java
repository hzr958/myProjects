package com.smate.web.psn.action.search;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.web.psn.service.search.PersonSearchService;

/**
 * 人员检索Action
 * 
 * @author zk
 *
 */
@Results({@Result(name = "search_result", location = "/WEB-INF/jsp/psnsearch/main.jsp"),
    @Result(name = "ajax_search_result", location = "/WEB-INF/jsp/psnsearch/psnList.jsp")})
public class PersonSearchAction extends ActionSupport implements ModelDriven<PersonSearchForm>, Preparable {

  private static final long serialVersionUID = -8835910416516102177L;
  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Value("${domainscm}")
  private String domainsns;

  private PersonSearchForm form;
  @Autowired
  private PersonSearchService personSearchService;

  /**
   * 人员检索
   * 
   * @return
   */
  @Action("/psnweb/personsearch/show")
  public String personSearch() {
    try {
      personSearchService.searchPerson(form);
    } catch (Exception e) {
      logger.error("获取人员检索列表出错,searchName=" + form.getSearchName() + ",searchKey=" + form.getSearchKey() + ",sizeNo="
          + form.getPageNo(), e);
    }
    return "search_result";
  }

  /**
   * 人员检索[ajax]
   * 
   * @return
   */
  @Action("/psnweb/personsearch/ajaxshow")
  public String ajaxPersonSearch() {
    try {
      personSearchService.searchPerson(form);
    } catch (Exception e) {
      logger.error("通过ajax获取人员检索列表出错,searchName=" + form.getSearchName() + ",searchKey=" + form.getSearchKey()
          + ",sizeNo=" + form.getPageNo(), e);
    }
    return "ajax_search_result";
  }


  /**
   * 人员检索[ajax]
   * 
   * @return
   */
  @Action("/psnweb/personsearch/ajaxshowtip")
  public String ajaxPersonSearchV2() {
    try {
      personSearchService.searchPerson(form);
    } catch (Exception e) {
      logger.error("通过ajax获取人员检索列表出错,searchName=" + form.getSearchName() + ",searchKey=" + form.getSearchKey()
          + ",sizeNo=" + form.getPageNo(), e);
    }
    return "ajax_search_result";
  }



  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new PersonSearchForm();
    }
  }

  @Override
  public PersonSearchForm getModel() {
    return form;
  }

  public PersonSearchForm getForm() {
    return form;
  }

  public void setForm(PersonSearchForm form) {
    this.form = form;
  }

  public String getDomainsns() {
    return domainsns;
  }

  public void setDomainsns(String domainsns) {
    this.domainsns = domainsns;
  }

}
