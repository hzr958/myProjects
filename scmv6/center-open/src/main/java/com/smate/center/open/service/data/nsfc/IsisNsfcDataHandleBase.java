package com.smate.center.open.service.data.nsfc;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.dao.nsfc.logs.LogInfoDao;
import com.smate.center.open.model.nsfc.logs.LogInfo;
import com.smate.center.open.utils.xml.WebServiceUtils;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * isis成果在线 具体服务接口抽象实现
 * 
 * @author tsz
 *
 */
@Transactional(rollbackFor = Exception.class)
public abstract class IsisNsfcDataHandleBase implements IsisNsfcDataHandleService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private LogInfoDao logInfoDao;

  /**
   * 校验参数 主要是校验各个服务自己的参数
   * 
   * 校验通过就放回 通过标识，失败就返回错误信息 信息的封装按 就系统的逻辑
   * 
   * @param paramet
   * @return
   */
  public abstract String doVerifyIsisData(Map<String, Object> dataParamet) throws Exception;

  // 数据处理方法
  public abstract String doHandlerIsisData(Map<String, Object> dataParamet) throws Exception;

  /**
   * 这个方法返回的都是 isis原来的数据拼接格式
   */
  @Override
  public String handleIsisData(Map<String, Object> dataParamet) {

    try {
      String verifyResult = doVerifyIsisData(dataParamet);
      if (verifyResult == null) {
        String handleResult = doHandlerIsisData(dataParamet);
        // 请求成功
        this.saveAccessLog(dataParamet, handleResult, 1, "数据处理");
        return handleResult;
      } else {
        // 参数校验不通过
        this.saveAccessLog(dataParamet, verifyResult, 2, "业务参数校验");
        return verifyResult;
      }
    } catch (Exception e) {
      // 吃掉异常，当做open系统的正确请求来处理
      logger.error(e.getMessage() + JacksonUtils.mapToJsonStr(dataParamet), e);
      this.saveAccessLog(dataParamet, e.toString(), 2, e.getMessage());
      return WebServiceUtils.setResut2("-5", e.getMessage());
    }

  }

  /**
   * 日志信息记录
   * 
   * @param dataParamet
   * @param msg
   * @param status
   */
  private void saveAccessLog(Map<String, Object> dataParamet, String msg, int status, String desc) {
    LogInfo log = new LogInfo();
    log.setActionDate(new Date());
    log.setClientIP(dataParamet.get(OpenConsts.MAP_TOKEN).toString());
    log.setActionResource(msg);
    log.setMethodName(dataParamet.get(OpenConsts.MAP_SERVICE_TYPE).toString());
    log.setParameters(JacksonUtils.mapToJsonStr(dataParamet));
    log.setStatus(status); // 1成功 ,2失败
    log.setDescription(desc);
    logInfoDao.save(log);
  }



}
