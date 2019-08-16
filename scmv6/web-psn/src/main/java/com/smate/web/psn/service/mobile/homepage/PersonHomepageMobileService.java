package com.smate.web.psn.service.mobile.homepage;

import java.util.HashMap;
import java.util.List;

import com.smate.web.psn.exception.PsnCnfException;
import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.form.mobile.PsnHomepageMobileForm;
import com.smate.web.psn.model.keyword.PsnScienceArea;

/**
 * 移动端 个人主页服务
 * 
 * @author tsz
 *
 */
public interface PersonHomepageMobileService {

  /**
   * 构建个人主页 显示 需要的数据
   * 
   * @param form
   */
  public void buildPsnHomepageData(PsnHomepageMobileForm form) throws PsnException;

  public List<PsnScienceArea> findPsnScienceAreaList(Long psnId) throws PsnException;

  public String getPersonUrl(String psnId);

  public void bulidMyHome(PsnHomepageMobileForm form) throws PsnException;

  public boolean isFriend(Long currentUserId, Long psnId);

  public void buildPsnInfoConfig(HashMap<String, Object> map, PsnHomepageMobileForm form) throws PsnCnfException;

}
