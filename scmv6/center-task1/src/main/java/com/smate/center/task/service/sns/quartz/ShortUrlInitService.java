package com.smate.center.task.service.sns.quartz;

import java.util.List;

import com.smate.center.task.model.sns.quartz.GrpPubIndexUrl;

/**
 * 短地址数据初始化接口
 * 
 * @author LJ
 *
 */
public interface ShortUrlInitService {

  public List<Long> getNeedInitPsnId(Integer index, Integer batchSize) throws Exception;

  public List<Long> getNeedInitGroupId(Integer index, Integer batchSize) throws Exception;

  public List<Long> getNeedInitAPubId(Integer index, Integer batchSize) throws Exception;

  public List<Long> getNeedInitSPubId(Integer index, Integer batchSize) throws Exception;

  public void initUrlData(Long Id1, Long Id, String type) throws Exception;

  public List<GrpPubIndexUrl> getNeedInitBPubId(int index, int batchSize) throws Exception;

  public void insertData();

  public List<Long> getNeedInitATPubId(int index, int batchSize) throws Exception;

}
