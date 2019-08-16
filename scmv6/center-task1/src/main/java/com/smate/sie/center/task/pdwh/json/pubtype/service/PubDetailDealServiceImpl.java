package com.smate.sie.center.task.pdwh.json.pubtype.service;

import java.util.Map;

import com.smate.sie.center.task.pdwh.json.service.process.PubHandlerProcessService;
import com.smate.sie.core.base.utils.pub.dom.PubTypeInfoBean;
import com.smate.sie.core.base.utils.pub.exception.PubHandlerProcessException;
import com.smate.sie.core.base.utils.pub.service.PubJsonDTO;

/**
 * 成果专利详情公共信息的对象
 * 
 * @author ZSJ
 *
 * @date 2019年2月1日
 */
public class PubDetailDealServiceImpl implements PubHandlerProcessService {

  @Override
  public void checkSourcesParameter(PubJsonDTO pub) throws PubHandlerProcessException {
    // TODO Auto-generated method stub

  }

  @Override
  public void checkRebuildParameter(PubJsonDTO pub) throws PubHandlerProcessException {
    // TODO Auto-generated method stub

  }

  @Override
  public Map<String, Object> excute(PubJsonDTO pub) throws PubHandlerProcessException {
    // TODO Auto-generated method stub
    System.out.println("公共");
    return null;
  }

  /**
   * 参数处理 主要是长度的处理
   * 
   * @param pub
   * @return
   */
  private PubTypeInfoBean constructParams(PubJsonDTO pub) {
    // TODO Auto-generated method stub
    return null;
  }

  private Map<String, String> buildData(PubJsonDTO pub) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Integer getPubType() {
    return 0; // 公共节点处理类
  }

}
