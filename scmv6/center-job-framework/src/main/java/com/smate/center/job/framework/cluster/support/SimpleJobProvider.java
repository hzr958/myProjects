package com.smate.center.job.framework.cluster.support;

import com.smate.center.job.common.enums.JobStatusEnum;
import com.smate.center.job.common.enums.JobWeightEnum;
import com.smate.center.job.common.po.BaseJobPO;
import com.smate.center.job.common.po.OfflineJobPO;
import com.smate.center.job.common.po.OnlineJobPO;
import com.smate.center.job.framework.dto.BaseJobDTO;
import com.smate.center.job.framework.dto.OfflineJobDTO;
import com.smate.center.job.framework.dto.OnlineJobDTO;
import com.smate.center.job.framework.service.OfflineJobService;
import com.smate.center.job.framework.service.OnlineJobService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.dozer.Mapper;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 任务、子任务提供器
 *
 * @author houchuanjie
 * @date 2018/04/04 16:58
 */
@Service
public class SimpleJobProvider implements JobProvidable, InitializingBean, DisposableBean {

  /**
   * 队列缓存任务数的最大值
   */
  private static final int MAX_SIZE = 100;
  /**
   * 缓冲池初始大小，计算公式：(需要存储的元素个数 / 负载因子) + 1，负载因子默认为0.75
   */
  private static final int INITIAL_CAPACITY = MAX_SIZE * 2 * 3 / 4 + 1;
  @Autowired
  private OfflineJobService offlineJobService;
  @Autowired
  private OnlineJobService onlineJobService;
  /**
   * 任务缓冲队列，包括在线任务和离线任务
   */
  private ConcurrentLinkedQueue<BaseJobPO> jobQueue;
  /**
   * 在线任务缓冲集合
   */
  private Map<String, OnlineJobPO> onlineJobMap;
  /**
   * 离线任务缓冲集合
   */
  private Map<String, OfflineJobPO> offlineJobMap;

  @Autowired
  private Mapper mapper;

  @Override
  public synchronized BaseJobDTO get() {
    loadJobs();
    // 从队列获取一个任务，然后从对应的任务缓存集合取出，转换为对应的OnlineJobDTO或者OfflineJobDTO，返回
    return Optional.ofNullable(jobQueue.poll()).map(BaseJobPO::getId).map(jobId -> {
      // 根据任务id从在线任务集合中取，并转换为OnlineJobDTO
      BaseJobDTO jobDTO = Optional.ofNullable(onlineJobMap.get(jobId))
          .map(onlineJobPO -> mapper.map(onlineJobPO, OnlineJobDTO.class))
          .map(o -> (BaseJobDTO) o).orElse(null);
      if (Objects.nonNull(jobDTO)) {
        onlineJobMap.remove(jobId);
        return jobDTO;
      }
      // 在线任务集合中取不到，则从离线任务集合中取，并转换为OfflineJobDTO
      jobDTO = Optional.ofNullable(offlineJobMap.get(jobId)).map(offlineJobPO -> {
        return mapper.map(offlineJobPO, OfflineJobDTO.class);
      }).map(o -> (BaseJobDTO) o).orElse(null);
      offlineJobMap.remove(jobId);
      return jobDTO;
    }).orElse(null);
  }

  @Override
  public BaseJobDTO peek() {
    loadJobs();
    // 从队列获取一个任务，然后从对应的任务缓存集合取出，转换为对应的OnlineJobDTO或者OfflineJobDTO，返回
    return Optional.ofNullable(jobQueue.peek()).map(BaseJobPO::getId).map(jobId -> {
      // 根据任务id从在线任务集合中取，并转换为OnlineJobDTO
      BaseJobDTO jobDTO = Optional.ofNullable(onlineJobMap.get(jobId)).map(OnlineJobDTO::new)
          .map(o -> (BaseJobDTO) o).orElse(null);
      if (Objects.nonNull(jobDTO)) {
        return jobDTO;
      }
      // 在线任务集合中取不到，则从离线任务集合中取，并转换为OfflineJobDTO
      jobDTO = Optional.ofNullable(offlineJobMap.get(jobId)).map(offlineJobPO -> {
        return mapper.map(offlineJobPO, OfflineJobDTO.class);
      }).map(o -> (BaseJobDTO) o).orElse(null);
      return jobDTO;
    }).orElse(null);
  }

  @Override
  public BaseJobPO removeFromCache(String jobId) {
    BaseJobPO removed = null;
    OfflineJobPO offlineJobPO = offlineJobMap.remove(jobId);
    if (Objects.nonNull(offlineJobPO)) {
      jobQueue.removeIf(baseJobPO -> baseJobPO.getId().equals(jobId));
      removed = offlineJobPO;
    }
    OnlineJobPO onlineJobPO = onlineJobMap.remove(jobId);
    if (Objects.nonNull(onlineJobPO)) {
      jobQueue.removeIf(baseJobPO -> baseJobPO.getId().equals(jobId));
      removed = onlineJobPO;
    }
    return removed;
  }

  /**
   * 从数据库获取任务
   *
   * @author houchuanjie
   * @date 2018年4月25日 下午5:32:00
   */
  private synchronized void loadJobs() {
    if (jobQueue.isEmpty()) {
      // 在线任务最多90个
      int onlineSum = MAX_SIZE * 9 / 10;
      List<OnlineJobPO> onlineJobs = getOnlineJobs(onlineSum);
      onlineJobs.forEach(o -> {
        //设置为等待状态
        o.setStatus(JobStatusEnum.LOADED);
        onlineJobMap.put(o.getId(), o);
        jobQueue.add(o);
      });
      // 离线任务最少10个
      int offlineSum = MAX_SIZE - onlineJobs.size();
      List<OfflineJobPO> offlineJobs = getOfflineJobs(offlineSum);
      offlineJobs.forEach(o -> {
        //设置为等待状态
        o.setStatus(JobStatusEnum.LOADED);
        offlineJobMap.put(o.getId(), o);
        jobQueue.add(o);
      });
      onlineJobService.batchUpdate(onlineJobs);
      offlineJobService.batchUpdate(offlineJobs);
    }
  }

  /**
   * 按权重比例获取所有权重的离线任务
   *
   * @param sum 要获取的任务数
   * @return list，实际大小可能会小于sum
   */
  private synchronized List<OfflineJobPO> getOfflineJobs(int sum) {
    List<OfflineJobPO> result = new ArrayList<>(sum);
    // 要获取权重A的任务数占比50%
    int sizeA = sum * 1 / 2;
    List<OfflineJobPO> listA = offlineJobService.getEnableList(JobWeightEnum.A, sizeA);
    result.addAll(listA);
    // 要获取权重B的任务数占比30% + 获取权重A后的任务数不满足sizeA的剩余数
    int sizeB = sum * 3 / 10 + sizeA - listA.size();
    List<OfflineJobPO> listB = offlineJobService.getEnableList(JobWeightEnum.B, sizeB);
    result.addAll(listB);
    // 要获取权重C的任务数占比15% + 获取权重B后的任务数不满足sizeB的剩余数
    int sizeC = sum * 3 / 20 + sizeB - listB.size();
    sizeC = sizeC < 2 ? 2 : sizeC;
    List<OfflineJobPO> listC = offlineJobService.getEnableList(JobWeightEnum.C, sizeC);
    result.addAll(listC);
    // 要获取权重D的任务数占比5% + 获取权重C后的任务数不满足sizeC的剩余数
    int sizeD = sum * 1 / 20 + sizeC - listC.size();
    sizeD = sizeD < 1 ? 1 : sizeD;
    List<OfflineJobPO> listD = offlineJobService.getEnableList(JobWeightEnum.D, sizeD);
    result.addAll(listD);
    return result;
  }

  /**
   * 按权重比例获取所有权重的在线任务
   *
   * @param sum 要获取的任务数
   * @return list，实际大小可能会小于sum
   */
  private synchronized List<OnlineJobPO> getOnlineJobs(int sum) {
    List<OnlineJobPO> result = new ArrayList<>(sum);
    // 要获取权重A的任务数占比50%
    int sizeA = sum * 1 / 2;
    List<OnlineJobPO> listA = onlineJobService.getEnableList(JobWeightEnum.A, sizeA);
    result.addAll(listA);
    // 要获取权重B的任务数占比30% + 获取权重A后的任务数不满足sizeA的剩余数
    int sizeB = sum * 3 / 10 + sizeA - listA.size();
    List<OnlineJobPO> listB = onlineJobService.getEnableList(JobWeightEnum.B, sizeB);
    result.addAll(listB);
    // 要获取权重C的任务数占比15% + 获取权重B后的任务数不满足sizeB的剩余数
    int sizeC = sum * 3 / 20 + sizeB - listB.size();
    List<OnlineJobPO> listC = onlineJobService.getEnableList(JobWeightEnum.C, sizeC);
    result.addAll(listC);
    // 要获取权重D的任务数占比5% + 获取权重C后的任务数不满足sizeC的剩余数
    int sizeD = sum * 1 / 20 + sizeC - listC.size();
    List<OnlineJobPO> listD = onlineJobService.getEnableList(JobWeightEnum.D, sizeD);
    result.addAll(listD);
    return result;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    this.jobQueue = new ConcurrentLinkedQueue<>();
    this.onlineJobMap = new HashMap<>(INITIAL_CAPACITY);
    this.offlineJobMap = new HashMap<>(INITIAL_CAPACITY);
  }

  @Override
  public void destroy() throws Exception {
    jobQueue.clear();
    List<OnlineJobPO> onlineJobPOList = new ArrayList<>(onlineJobMap.values());
    List<OfflineJobPO> offlineJobPOList = new ArrayList<>(offlineJobMap.values());
    onlineJobMap.clear();
    offlineJobMap.clear();
    for (OnlineJobPO onlineJobPO : onlineJobPOList) {
      onlineJobPO.setStatus(JobStatusEnum.UNPROCESS);
    }
    for (OfflineJobPO offlineJobPO : offlineJobPOList) {
      offlineJobPO.setStatus(JobStatusEnum.UNPROCESS);
    }
    onlineJobService.batchUpdate(onlineJobPOList);
    offlineJobService.batchUpdate(offlineJobPOList);
  }
}
