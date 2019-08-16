package com.smate.sie.center.task.service.tmp;

import java.util.List;

import com.smate.core.base.utils.model.consts.SieConstRegion;


/**
 * 后台任务轮询TASK_INS_BAIDU_GET_ADDR， 跑出单位地址信息到本表字段, 服务层
 * 
 * @author ztg
 *
 */
public interface SieBaiduMapGetInsAddsService {

  List<Long> batchGetProcessedData(Integer size);

  void startProcessing(Long id, List<SieConstRegion> cnZhname, List<SieConstRegion> allName) throws Exception;

  void updateStatusById(Long id, int i);

  List<SieConstRegion> getAllCNZhname();

  List<SieConstRegion> getAllName();

}
