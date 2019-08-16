package com.smate.web.fund.service.wechat;

import com.smate.web.prj.exception.FundExcetpion;
import com.smate.web.prj.form.wechat.FundWeChatForm;

public interface FundQueryService {
  /*
   * @zjh 微信基金推荐rcmd2数据原
   */
  public void queryfundinfo(FundWeChatForm form) throws FundExcetpion;


  /**
   * 查询基金分享所需信息
   * 
   * @param form
   * @throws FundExcetpion
   */
  void queryFundInfoForShare(FundWeChatForm form) throws FundExcetpion;

}
