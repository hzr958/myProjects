package com.smate.sie.center.task.pdwh.json.pubtype.factory;

import java.util.List;

import com.smate.sie.center.task.pdwh.json.service.process.PubHandlerProcessService;
import com.smate.sie.core.base.utils.pub.exception.PubHandlerProcessServiceNotFoundException;

/**
 * 生产成果不同类型服务的工厂 PubHandlerProcessServiceNotFoundException
 * 
 * @author ZSJ
 *
 * @date 2019年2月1日
 */
public class PubTypeServiceFactory {

  private List<PubHandlerProcessService> pubTypeServices;

  public void setPubTypeServices(List<PubHandlerProcessService> pubTypeServices) {
    this.pubTypeServices = pubTypeServices;
  }

  public PubHandlerProcessService getPubTypeServices(Integer pubType) throws PubHandlerProcessServiceNotFoundException {
    PubHandlerProcessService result = null;
    for (int i = 0; i < pubTypeServices.size(); i++) {
      PubHandlerProcessService item = pubTypeServices.get(i);
      if (pubType == 2 || pubType == 10) {// 著作、书籍章节同一个PubHandlerProcessService
        pubType = 2;
      }
      if (pubType.equals(item.getPubType())) {
        result = item;
        break;
      }
    }
    if (result == null) {
      throw new PubHandlerProcessServiceNotFoundException(pubType);
    }
    return result;
  }

}
