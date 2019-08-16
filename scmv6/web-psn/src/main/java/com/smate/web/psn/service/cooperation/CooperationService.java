package com.smate.web.psn.service.cooperation;

import org.hibernate.service.spi.ServiceException;

import com.smate.core.base.utils.model.Page;
import com.smate.web.psn.model.cooperation.PsnKnowCopartnerForm;

public interface CooperationService {
  /**
   * 查看全部合作者
   * 
   * @param page
   * @param psnId
   * @param pubCpt
   * @param prjCpt
   * @return
   * @throws ServiceException
   */

  Page findPsnKnowCopartner(Page page, PsnKnowCopartnerForm form, Integer CptType) throws ServiceException;
}
