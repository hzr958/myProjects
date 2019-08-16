package com.smate.web.institution.service;

import com.smate.web.institution.form.InstitutionForm;

public interface InstitutionOptService {

  /**
   * 获取经济产业信息
   * @param form
   */
  public void buildEconomicInfo(InstitutionForm form);

  /**
   * 新兴产业代码
   * @param form
   */
  public void buildCseiInfo(InstitutionForm form);

  /**
   *  检查机构名称和域名
   * @param form
   */
  public boolean  checkInsName(InstitutionForm form);
  /**
   *  保存机构主页信息
   * @param form
   */
  public boolean  createInsPage(InstitutionForm form);

  /**
   * 查询机构主页列表
   * @param form
   */
  public void  findInsPageList(InstitutionForm form);

  /**
   *  取消关注
   * @param form
   */
  public boolean  cancelFollow(InstitutionForm form);

}
