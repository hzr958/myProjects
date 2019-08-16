package com.smate.web.management.service.other;

import java.io.Serializable;

import com.smate.core.base.exception.ServiceException;
import com.smate.web.management.model.other.fund.ConstFundCategory;
import com.smate.web.management.model.other.fund.FundThirdForm;

/**
 * 基金机会.
 * 
 * @author YHX
 * 
 */
public interface FundThirdService extends Serializable {


  void findFundThirdList(FundThirdForm form) throws ServiceException;

  void viewFundThirdDetails(FundThirdForm form) throws ServiceException;

  ConstFundCategory checkFundThird(Long id) throws ServiceException;

  void updateFundThird(Long id, Integer status) throws ServiceException;

  void deleteFundThird(Long id) throws ServiceException;
}
