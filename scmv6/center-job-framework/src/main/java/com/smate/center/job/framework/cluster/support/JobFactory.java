package com.smate.center.job.framework.cluster.support;

import com.smate.center.job.common.enums.JobTypeEnum;
import com.smate.center.job.framework.dto.BaseJobDTO;
import com.smate.center.job.framework.dto.OfflineJobDTO;
import com.smate.center.job.framework.dto.TaskletPackageDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 任务加工厂
 *
 * @author houchuanjie
 * @date 2018/04/20 15:03
 */
@Component
public class JobFactory {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Resource
  private JobProvidable jobProvider;
  @Resource
  private JobProcessable jobProcessor;
  @Resource
  private JobCheckable jobChecker;

  public List<TaskletPackageDTO<? extends BaseJobDTO>> produce(int size, int maxRedundant) {
    List<TaskletPackageDTO<? extends BaseJobDTO>> list = new ArrayList<>(size + maxRedundant + 1);
    int sum = 0;
    // 循环获取任务，直到满足要求
    while (true) {
      // 预先取出一个任务，做资源检查
      BaseJobDTO job = jobProvider.peek();
      // 任务队列为空时，退出循环
      if (Objects.isNull(job)) {
        break;
      }
      logger.debug("取到新任务：{}，任务类型：{}，任务bean名称：'{}'", job.getId(), job.getJobType(), job
          .getJobName());
      logger.debug("检查线程资源是否满足该任务要求...");
      // 获取需要增加的线程数
      int plusNum =
          job.getJobType() == JobTypeEnum.ONLINE ? 1 : ((OfflineJobDTO) job).getThreadCount();
      // 判断取出此任务后，是否满足要求
      int flag = judge(sum, plusNum, size, maxRedundant);
      // 超过要求的最大允许小任务数，退出循环
      if (flag == -1) {
        logger.debug("任务所需线程资源暂时无法满足，已放回任务等待队列！");
        break;
      }
      logger.debug("线程资源满足！");
      // 获取并移除该任务
      job = jobProvider.get();
      boolean checkResult = jobChecker.check(job);
      if (!checkResult) {
        continue;
      }
      //加子任务数
      sum += plusNum;
      // 对任务进行加工、处理、包装
      list.add(new TaskletPackageDTO<BaseJobDTO>(job, jobProcessor.process(job)));
      logger.debug("任务已加工处理完毕！");
      // 刚好满足size，又不大于 size + maxRedundant，退出循环
      if (flag == 1) {
        break;
      }
      // flag为0时还未满足要求，继续获取任务
    }
    return list;
  }

  /**
   * 计算当前值{@code sum}加上{@code plus}后的大小情况与返回值：
   *
   * <pre>
   * sum + plus < size                            :   return 0;
   * size <= sum + plus <= size + maxRedundant    :   return 1;
   * sum + plus > size + maxRedundant             :   return -1;
   * </pre>
   *
   * @param sum 当前值
   * @param plus 要加的值
   * @param size 参考值
   * @param maxRedundant 最多不超过参考值的数量（最大冗余数值）
   * @return 如果{@code sum + plus}小于{@code size}，返回0，如果大于等于{@code size}小于等于{@code size +
   * maxRedundant}，返回1，否则返回-1
   */
  private int judge(int sum, int plus, int size, int maxRedundant) {
    int mSum = sum + plus;
    int max = size + maxRedundant;
    if (mSum < size) {
      return 0;
    }
    if (mSum >= size && mSum <= max) {
      return 1;
    }
    return -1;
  }
}
