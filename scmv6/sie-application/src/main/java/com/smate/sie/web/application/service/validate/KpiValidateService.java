package com.smate.sie.web.application.service.validate;

import java.io.IOException;
import java.util.Map;

import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.model.Page;
import com.smate.sie.core.base.utils.model.validate.KpiValidateMainUpload;
import com.smate.sie.web.application.form.validate.KpiValidateForm;

public interface KpiValidateService {
  /**
   * 左侧查询初始化
   * 
   * @param form
   * @throws SysServiceException
   */
  public void initLeftMenu(KpiValidateForm form) throws SysServiceException;

  /**
   * 查询科研验证列表
   * 
   * @param form
   * @param page
   */
  public void queryKpiValidateList(KpiValidateForm form, Page<KpiValidateMainUpload> page) throws SysServiceException;

  public void getPsnCountByCon(KpiValidateForm form) throws SysServiceException;

  public Map<String, Object> extractPDFFileContent(KpiValidateForm form, String savePathRoot)
      throws SysServiceException, IOException;

  /**
   * 提交前校验
   * 
   * @param form
   * @return
   * @throws SysServiceException
   */
  public Map<String, Object> beforeSubMit(KpiValidateForm form) throws SysServiceException;

  public Map<String, Object> viewValidateDetail(String uuId, KpiValidateForm form) throws SysServiceException;

  public void refreshStatusCurrent(KpiValidateForm form) throws SysServiceException;

  /**
   * 逻辑删除
   * 
   * @param id
   * @throws SysServiceException
   */
  public void delValidateMainUpload(Long id) throws SysServiceException;

  /**
   * 调接口判断是否已经付费
   * 
   * @param psnId
   * @return
   * @throws SysServiceException
   */
  public Integer isAlreadyPaid(Long psnId, String ip) throws SysServiceException;

  /**
   * 处理科研验证的新增功能的切面
   */
  public int clickAdd(Integer isPay, Long userId, String zhName, String userRealIP);

  /**
   * 根据userId和insId查找人名
   * 
   * @param psnId
   * @param insId
   * @return
   */
  public String findPsnNameByInsIdAndPsnId(Long psnId, Long insId);

  /**
   * 根据PsnId查找人名
   * 
   * @param psnId
   * @return
   */
  public String findPsnNameByPsnId(Long psnId);
}
