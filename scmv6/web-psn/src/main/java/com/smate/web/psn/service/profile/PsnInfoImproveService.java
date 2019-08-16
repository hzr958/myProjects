package com.smate.web.psn.service.profile;

import java.util.Map;

import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.model.homepage.PersonProfileForm;

/**
 * 人员信息完善服务接口
 * 
 * @author WSN
 *
 *         2017年8月15日 上午11:52:26
 *
 */
public interface PsnInfoImproveService {

  /**
   * 用户是否关键词和科技领域都填写了信息
   * 
   * @param psnId
   * @return
   * @throws PsnException
   */
  public Map<String, Boolean> psnHasScienceAreaAndKeywords(Long psnId) throws PsnException;

  /**
   * 构建人员科技领域信息完善
   * 
   * @param form
   * @return
   * @throws PsnException
   */
  public PersonProfileForm buildPsnImproveScienceAreaInfo(PersonProfileForm form) throws PsnException;

  /**
   * 构建人员关键词信息完善
   * 
   * @param form
   * @return
   * @throws PsnException
   */
  public PersonProfileForm buildPsnImproveKeywordsInfo(PersonProfileForm form) throws PsnException;
}
