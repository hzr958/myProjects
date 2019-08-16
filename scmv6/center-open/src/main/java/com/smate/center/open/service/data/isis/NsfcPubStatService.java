package com.smate.center.open.service.data.isis;



import java.util.Map;



/**
 * 结题项目成果按 单位统计
 * 
 * @author hp
 * @date 2015-10-21
 */
public interface NsfcPubStatService {

  Map<String, Object> getPubStatByIns(Map<String, Object> dataMap) throws Exception;


}
