package com.smate.center.batch.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.connector.dao.job.BatchJobsDao;
import com.smate.center.batch.connector.model.job.BatchJobs;
import com.smate.center.batch.constant.BatchConfConstant;
import com.smate.center.batch.dao.sns.pub.ApplicationQuartzSettingDao;
import com.smate.core.base.utils.exception.BatchTaskException;

/**
 * BatchJobs调度Service实现
 * 
 * @author hzr
 * 
 * @since 6.0.1
 * @version 6.0.1
 * @param job
 */
@Service("taskMarkerService")
@Transactional(rollbackFor = Exception.class)
public class TaskMarkerServiceImpl implements TaskMarkerService {

  @Autowired
  private BatchJobsDao batchJobsDao;

  @Autowired
  private ApplicationQuartzSettingDao applicationQuartzSettingDao;

  @Override
  public Long getTaskMsgIdCountByStatus(Integer status) throws BatchTaskException {
    Long num = batchJobsDao.getCountByStatus(status);
    return num;
  }

  @Override
  public List<BatchJobs> getTaskMsgIdListByWeightAndNum(Integer num, String weight) throws BatchTaskException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<BatchJobs> getTaskMsgIdListByWeight(String weight) throws BatchTaskException {
    List<BatchJobs> resultList = new ArrayList<BatchJobs>();

    if (StringUtils.isBlank(weight)) {
      return null;
    } else if (weight.equalsIgnoreCase("all")) {
      resultList = batchJobsDao.getAllTaskWithStatus0();
    } else if (weight.equalsIgnoreCase("A")) {
      resultList = batchJobsDao.getTaskByWeight("A");
    } else if (weight.equalsIgnoreCase("B")) {
      resultList = batchJobsDao.getTaskByWeight("B");
    } else if (weight.equalsIgnoreCase("C")) {
      resultList = batchJobsDao.getTaskByWeight("C");
    } else if (weight.equalsIgnoreCase("D")) {
      resultList = batchJobsDao.getTaskByWeight("D");
    }

    return resultList;
  }

  @Override
  public Long getTaskMsgIdListCountByWeight(String weight) throws BatchTaskException {
    Long count;
    count = batchJobsDao.getTaskMsgIdCountByWeight(weight);
    return count;
  }

  @Override
  public List<BatchJobs> arrangeTask(Integer taskSize) throws BatchTaskException {
    List<BatchJobs> resultList = new ArrayList<BatchJobs>();
    Map<String, Integer> arrage = getArrangedTaskNum(taskSize);
    Integer numAccu = 0;
    Integer numCorr = 0;

    // 每次查询到需要查询A、B、C和D这4种数据，数据补充规则，A不足，B补上，依次类推...，如连D都不足，则有多少取多少
    for (String weight : arrage.keySet()) {
      Integer num = arrage.get(weight);
      numCorr = numAccu - resultList.size();
      List<BatchJobs> list = batchJobsDao.getTaskMsgIdsByWeightAndNum(weight, num + numCorr);

      if (list.size() > 0) {
        resultList.addAll(list);
      }

      numAccu += num;
    }

    return resultList;

    // 方法二，计算每一次的修正值来补足下一次需要查询的数量。修正值可能为正，说明这次取得值不足预定数量；也可能为负，说明。
    /*
     * Integer numAccu = 0; Integer numAccu1 = 0;
     * 
     * 
     * //按A-D的权重查询，如果上一权重不够，则用下一权重任务补充
     * 
     * for(String weight : arrage.keySet()){ Integer num = arrage.get(weight); Integer numCorr = 0;
     * List<BatchJobs> list = batchJobsDao.getTaskMsgIdsByWeightAndNum(weight, num+numAccu);
     * 
     * //如果list比num长，则numCorr为负数；说明已经补足了上一个优先级不足的任务数 numCorr = num - list.size();
     * 
     * 
     * numAccu += numCorr;
     * 
     * resultList.addAll(list); }
     */

  }

  @Override
  public void updateTaskStatus(BatchJobs item) throws BatchTaskException {
    batchJobsDao.save(item);

  }

  /**
   * 按固定比例预先分配不同权重的任务数,每个任务最少数为1
   * 
   * 
   */
  public Map<String, Integer> getArrangedTaskNum(Integer taskSize) throws BatchTaskException {
    Map<String, Integer> result = new LinkedHashMap<String, Integer>();
    Integer numA = 1;
    Integer numB = 1;
    Integer numC = 1;
    Integer numD = 1;

    if (taskSize * BatchConfConstant.TASK_WEIGHT_D_RATIO > 1) {
      numD = (int) (taskSize * BatchConfConstant.TASK_WEIGHT_D_RATIO);
    }

    if (taskSize * BatchConfConstant.TASK_WEIGHT_C_RATIO > 1) {
      numC = (int) (taskSize * BatchConfConstant.TASK_WEIGHT_C_RATIO);
    }

    if (taskSize * BatchConfConstant.TASK_WEIGHT_B_RATIO > 1) {
      numB = (int) (taskSize * BatchConfConstant.TASK_WEIGHT_B_RATIO);
    }

    numA = taskSize - numD - numC - numB;

    if (numA < 1) {
      throw new BatchTaskException("分配任务总数错误，taskSize < 0");
    }

    result.put(BatchConfConstant.TASK_WEIGHT_A, numA);
    result.put(BatchConfConstant.TASK_WEIGHT_B, numB);
    result.put(BatchConfConstant.TASK_WEIGHT_C, numC);
    result.put(BatchConfConstant.TASK_WEIGHT_D, numD);
    return result;
  }

  @Override
  public List<BatchJobs> getTaskListStatus1() throws BatchTaskException {
    return batchJobsDao.getAllTaskWithStatus1();
  }

  @Override
  public Integer getApplicationQuartzSettingValue(String name) {

    return applicationQuartzSettingDao.getAppValue(name);
  }

  @Override
  public void closeQuartzApplication(String name) {
    applicationQuartzSettingDao.closeApplication(name);
  }

}
