package com.smate.web.management.service.institution;

import java.util.List;

import com.google.gdata.util.ServiceException;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.bpo.BpoInsPortal;
import com.smate.core.base.utils.model.cas.security.User;
import com.smate.web.management.model.institution.bpo.FileUploadSimple;
import com.smate.web.management.model.institution.bpo.InstitutionBpo;
import com.smate.web.management.model.institution.bpo.InstitutionRolForm;


public interface InstitutionBpoService {
  /**
   * 分页查询单位数据
   * 
   * @param form
   * @return
   * @throws ServiceException
   */
  @SuppressWarnings("rawtypes")
  Page<InstitutionBpo> getInstitutionByPage(InstitutionRolForm form, Page page) throws Exception;

  InstitutionRolForm getEditInstitutionDetail(InstitutionRolForm form) throws Exception;

  BpoInsPortal findInsPortal(Long insId) throws Exception;

  int sendResetPwdEmail(String jsonParam, String scmUrl) throws Exception;

  boolean checkInsNameIsUsing(Long insId, String trim) throws Exception;

  boolean checkDomainIsUsing(Long insId, String insDomain) throws Exception;

  void editInstitution(InstitutionRolForm form) throws Exception;

  Page getInsEditRemarkByPage(Long insId, Page page) throws Exception;

  List<User> getInsAdminByInsId(Long insId) throws Exception;

  FileUploadSimple uploadAndSaveInsFax(FileUploadSimple fileUploadSimple) throws Exception;

  FileUploadSimple uploadAndSaveInsLogo(FileUploadSimple fileUploadSimple) throws Exception;

}
