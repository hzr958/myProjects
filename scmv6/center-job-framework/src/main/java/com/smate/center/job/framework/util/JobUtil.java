package com.smate.center.job.framework.util;

import com.smate.center.job.framework.dto.TaskletDTO;
import com.smate.core.base.utils.string.StringUtils;
import com.sun.istack.internal.NotNull;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.quartz.JobKey;

/**
 * 任务工具类
 *
 * @author houchuanjie
 * @date 2018/04/18 16:44
 */
public class JobUtil {

  /**
   * 根据任务信息获取唯一的JobKey
   *
   * @param taskletDTO 任务信息
   * @return 唯一确定的JobKey对象
   */
  public static JobKey getJobKey(TaskletDTO taskletDTO) {
    return getJobKey(taskletDTO.getJobName(), taskletDTO.getId(), taskletDTO.getThreadNo());
  }

  /**
   * 根据任务名称、任务id、分片号等信息获取唯一的JobKey
   *
   * @param jobName 任务名称
   * @param jobId 任务id
   * @param threadNo 分片号
   * @return
   */
  public static JobKey getJobKey(String jobName, String jobId, Integer threadNo) {
    String group = jobName;
    String name = jobId + "_" + threadNo;
    return new JobKey(name, group);
  }

  /**
   * 通过jobKey取jobId
   *
   * @param jobKey
   * @return
   */
  public static String getJobId(JobKey jobKey) {
    return StringUtils.substringBeforeLast(jobKey.getName(), "_");
  }

  /**
   * 根据提供的时间戳和超时时长，判断当前时间是否已经超时
   *
   * @param timestamp 时间戳
   * @param timeout 超时时长，约定多长时间算作超时
   * @param unit 超时时长的单位
   * @return 当前时间已经超出timestamp给定的超时时长timeout，返回true，或者timestamp是null，也返回true，否则返回false
   */
  public static boolean isTimeout(Long timestamp, @NotNull Long timeout, @NotNull TimeUnit unit) {
    if (Objects.isNull(timestamp)) {
      return true;
    }
    boolean isTimeout = Instant.now().minusMillis(TimeUnit.MILLISECONDS.convert(timeout, unit))
        .isAfter(Instant.ofEpochMilli(timestamp));
    return isTimeout;
  }
}
