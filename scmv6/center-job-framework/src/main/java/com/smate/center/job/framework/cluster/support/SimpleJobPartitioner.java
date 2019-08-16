package com.smate.center.job.framework.cluster.support;

import com.smate.center.job.framework.dto.OfflineJobDTO;
import com.smate.center.job.framework.dto.TaskletDTO;
import com.smate.center.job.framework.service.OfflineJobService;
import com.smate.core.base.utils.number.NumberUtils;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

/**
 * 一个简单的自动对离线任务进行分片的实现类
 *
 * @author Created by hcj
 * @date 2018/07/12 10:14
 */
@Component
public class SimpleJobPartitioner implements JobPartitionable {

  @Resource
  private OfflineJobService offlineJobService;

  @Override
  public List<TaskletDTO> autoPartition(OfflineJobDTO offlineJobDTO) {
    long count = offlineJobDTO.getCount();
    //需要处理的记录数小于等于线程数的话，分count个线程处理。
    if (offlineJobDTO.getCount() <= offlineJobDTO.getThreadCount()) {
      offlineJobDTO.setThreadCount(offlineJobDTO.getCount().intValue());
    }
    // 取分区数量
    int partCount = offlineJobDTO.getThreadCount();
    List<TaskletDTO> resultList = new ArrayList<>(partCount);
    // 求每个分区的平均数量
    long average = ((Double) Math.ceil(count * 1.0 / offlineJobDTO.getThreadCount())).longValue();
    /*
     * 获取每个分区的开始和结束位置序号，先获取前n-1个分区的，再获取最后一个分区的，
     * 通常来讲，最后一个分区被分到的数量小于等于平均分区数量
     */
    List<Long> seqList = new ArrayList<>();
    for (long i = 0; i < partCount - 1; i++) {
      seqList.add(average * i + 1);
      seqList.add(average * (i + 1));
    }
    seqList.add(average * (partCount - 1) + 1);
    seqList.add(count);
    // 获取每个分区开始和结束记录的主键
    List<Long> uniqueKeyList = offlineJobService.getUniqueKeyList(offlineJobDTO, seqList);
    if (CollectionUtils.isNotEmpty(uniqueKeyList)) {
      // 任务分片时间戳，同一次任务分配的分片具有相同的时间戳，来区分不同时间任务的执行
      long timestamp = Instant.now().toEpochMilli();
      // 原子计数器
      AtomicInteger threadNumber = new AtomicInteger(0);
      //最后一个分片只有一个id的情况特别注意，uniqueKeyList的大小为奇数
      int size = uniqueKeyList.size();
      if (NumberUtils.isOdd(size)) {
        //将最后一个id再复制一遍，以构造成区间
        uniqueKeyList.add(uniqueKeyList.get(size - 1));
        size += 1;
      }
      // 设置每个分片的开始和结束id，以及分片需要处理的记录数量
      for (int i = 0; i < size - 1; i += 2) {
        TaskletDTO taskletDTO = new TaskletDTO(offlineJobDTO, average, uniqueKeyList.get(i),
            uniqueKeyList.get(i + 1), threadNumber.incrementAndGet(), timestamp);
        resultList.add(taskletDTO);
      }
      // 最后一个分片的数量要特别处理
      TaskletDTO taskletDTO = resultList.get(resultList.size() - 1);
      taskletDTO.setCount(count - average * (partCount - 1));
    }
    return resultList;
  }
}
