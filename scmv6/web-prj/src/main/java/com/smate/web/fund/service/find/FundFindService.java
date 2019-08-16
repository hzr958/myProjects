package com.smate.web.fund.service.find;

import java.util.List;

import com.smate.core.base.consts.model.ConstRegion;
import com.smate.web.fund.find.model.FundFindForm;
import com.smate.web.prj.exception.PrjException;

public interface FundFindService {

  void fundFindConditionsShow(FundFindForm form) throws PrjException;

  public FundFindForm fundFindListSearch(FundFindForm form) throws PrjException;

  List<ConstRegion> getFundRegion();

  void fundFindListForWeChat(FundFindForm form) throws PrjException;

  String findSuperRegion(String searchRegion, String locale) throws PrjException;

}
