package com.smate.center.task.service.thirdparty;

import com.smate.core.base.utils.string.StringUtils;

import java.util.Map;

/**
 * 业务系统提供数据解析 基类.
 * 
 * @author tsz
 *
 */
public abstract class ThirdPartyDataAnalysisBase implements ThirdPartyDataAnalysisService {


  @Override
  public void handle(String token, Map<String, Object> sourcesData) throws Exception {
    this.checkData(token ,sourcesData);
    Object obj = this.analysisData(sourcesData);
    this.saveData(obj);
  }

  /**
   * 校验数据.
   * 
   * @throws Exception
   */
  public abstract void checkData(String token, Map<String, Object> sourcesData) throws Exception;

  /**
   * 解析数据.
   * 
   * @throws Exception
   */
  public abstract Object analysisData(Map<String, Object> sourcesData) throws Exception;

  /**
   * 保存数据.
   * 
   * @throws Exception
   */
  public abstract void saveData(Object obj) throws Exception;


  /**
   * 检查map对象中的字段 是否为空；
   * @param sourcesData
   * @param field
   * @return
   */
  public boolean checkBankField(Map<String, Object> sourcesData , String field){
    if(sourcesData == null || sourcesData.size()==0){
      return true ;
    }
    if(sourcesData.get(field) == null || StringUtils.isBlank(sourcesData.get(field).toString())){
      return true ;
    }
    return false ;
  }
}
