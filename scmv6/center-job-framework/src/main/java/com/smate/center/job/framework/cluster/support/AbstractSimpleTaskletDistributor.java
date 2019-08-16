package com.smate.center.job.framework.cluster.support;

import com.smate.center.job.framework.dto.TaskletDTO;
import com.smate.center.job.framework.exception.NoAvailableServerNodeException;
import com.smate.center.job.framework.cluster.ClusterResourceManageable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 一个简单的子任务分发器实现
 *
 * @author houchuanjie
 * @date 2018/04/12 11:44
 */
public abstract class AbstractSimpleTaskletDistributor implements TaskletDistributable {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  //资源管理器
  @Autowired
  private ClusterResourceManageable resourceManager;

  /**
   * 分发子任务到指定节点
   *
   * @param nodeName 节点名称
   * @param list 小任务列表
   */
  public abstract void distributeTo(String nodeName, List<TaskletDTO> list);

  @Override
  public void distribute(List<TaskletDTO> taskletList) {
    logger.debug("本次需要分配的任务数：{}", taskletList.size());
    logger.debug("正在计算分配结果...");
    // 计算每个服务器的分配的任务数
    Map<String, Integer> computeAllocatedResult = computeAllocatedResult(taskletList.size());
    logger.debug("分配结果计算完毕，正在分发任务至各服务器节点...");
    int index = 0;
    for (Entry<String, Integer> entry : computeAllocatedResult.entrySet()) {
      List<TaskletDTO> list = new ArrayList<>(entry.getValue());
      for (int num = 0; num < entry.getValue() && index < taskletList.size(); num++, index++) {
        list.add(taskletList.get(index));
      }
      distributeTo(entry.getKey(), list);
    }
    logger.debug("所有任务列表已分发至各服务器节点。");
  }

  /**
   * <p>
   * 根据需要的线程数{@code surplusTaskletSize}，然后获取服务器可用线程资源，进行分配计算，返回分配后各服务器剩余线程资源数的集合。
   * </p>
   * <b>请注意：</b>
   * <p>
   * 这是一个计算操作，是一个预分配计算，并没有真正分配，获得各服务器剩余线程资源集合后，如果所有服务器节点的剩余线程分配完毕后， 还存在剩余待分配任务，则应该进行下一步的可用队列预分配计算。
   * </p>
   *
   * @param requireThreads 需要分配的任务所占用的线程资源数
   * @return 预分配完成后，各服务器剩余线程资源的集合
   */
  private Map<String, Integer> computeAllocatedResult(int requireThreads)
      throws NoAvailableServerNodeException {
    // 获取每个服务器可用线程数集合
    Map<String, Integer> availableThreadSizeMap = resourceManager.getAvailableThreadSizeMap();

    if (MapUtils.isEmpty(availableThreadSizeMap) && requireThreads > 0) {
      throw new NoAvailableServerNodeException("没有可用的服务器线程资源！");
    }

    int sum = availableThreadSizeMap.values().stream().mapToInt(Integer::intValue).sum();
    // 服务器总线程资源刚好能够满足任务数要求，则服务器线程资源全部分配
    if (sum == requireThreads) {
      return new HashMap<>(availableThreadSizeMap);
    }
    // 保存分配的结果的Map集合，key为serverName，value为对应服务器要分配的任务数
    final Map<String, Integer> resultMap = new HashMap<>(availableThreadSizeMap.size());
    // 服务器总线程资源数大于任务需要的资源数
    if (sum > requireThreads) {
      // 克隆一份，防止原map中的对象值被改变
      Map<String, Integer> clonedMap = new HashMap<>(availableThreadSizeMap);
      // 转换为list，逆序排序，只需排序1次，提高效率
      List<Entry<String, Integer>> list = new ArrayList<>(clonedMap.entrySet());
      Collections.sort(list, (entry1, entry2) -> entry2.getValue() - entry1.getValue());
      // 任务预分配计算，方法中会对resourceThreadMap集合中的对象做修改
      compute(list, requireThreads);
      clonedMap.forEach((k, v) -> {
        resultMap.put(k, availableThreadSizeMap.get(k) - v);
      });
    } else { // 服务器总线程资源不能满足任务数要求，则分配队列资源
      // 获取每个服务器剩余队列大小集合
      Map<String, Integer> availableQueueSizeMap = resourceManager.getAvailableQueueSizeMap();
      HashMap<String, Integer> clonedMap = new HashMap<>(availableQueueSizeMap);
      List<Entry<String, Integer>> list = new ArrayList<>(clonedMap.entrySet());
      Collections.sort(list, (entry1, entry2) -> entry2.getValue() - entry1.getValue());
      compute(list, requireThreads - sum);
      clonedMap.forEach((k, v) -> {
        resultMap.put(k, availableThreadSizeMap.get(k) + availableQueueSizeMap.get(k) - v);
      });
    }
    return resultMap;
  }

  /**
   * 最大空闲资源优先分配算法， 给定每个服务器可用资源数量的集合，和要分配的子任务需要的资源数，进行计算预分配， 空闲资源多的优先分配。计算分配结束后，返回未满足要求的资源数量
   *
   * @param resourceEntryList 经过逆序排序的服务器可用资源集合
   * @param requireNum 需要的资源数
   * @return 预分配完成后，剩余需要的资源数。
   */
  private int compute(List<Entry<String, Integer>> resourceEntryList, int requireNum) {
    List<Integer> resourceList = resourceEntryList.stream().map(entry -> entry.getValue())
        .collect(Collectors.toList());
    final int reference = findFirstLesserNumber(resourceList);
    // 对资源集合按照值逆序排序、筛选出大于参考值的对象
    List<Entry<String, Integer>> filteredResourceList = resourceEntryList.stream()
        .filter(entry -> entry.getValue() > reference).collect(Collectors.toList());
    // 取大于参考值的可分配的资源总数
    int sum = filteredResourceList.stream().mapToInt(entry -> entry.getValue() - reference).sum();
    if (sum == 0) {
      return requireNum;
    }
    // 可分配资源数小于需要资源数，则将这些可分配的先全部分配
    if (sum < requireNum) {
      for (Entry<String, Integer> entry : filteredResourceList) {
        entry.setValue(reference);
      }
      requireNum -= sum;
      // 剩余需要分配的任务递归调用此算法
      return compute(resourceEntryList, requireNum);
    }
    // 可分配的资源数大于等于需要资源数，则依次进行分配，每次循环的服务节点分配一个任务
    while (requireNum > 0) {
      /**
       * 遍历排序筛选后的服务器列表，然后每个服务器分配一个任务，直到剩余待分配任务为0为止。
       * 如果一遍循环结束，仍有待分配任务，则继续遍历整个列表，再次执行分配
       */
      for (Entry<String, Integer> entry : filteredResourceList) {
        if (requireNum > 0 && entry.getValue() > 0) {
          entry.setValue(entry.getValue() - 1);
          requireNum--;
        }
      }
    }
    // 分配结束，所有需要的资源数量都可分配，则剩余需要分配的资源数为0
    return 0;
  }

  /**
   * 给定一个集合列表，逆序排序后，查找集合中第一个比最大值（{@code largest}）小的值，如果找不到这个次小值， 则返回largest - 1，
   * 但最小为0，该值表达式：largest-- > 0 ? largest : 0;
   *
   * @param list 资源集合
   * @return 集合中比最大值较小的值，如果未找到，则返回最大值-1，但最小为0
   */
  private int findFirstLesserNumber(List<Integer> list) {
    Collections.reverse(list);
    int largest = list.get(0);
    for (int i = 1, size = list.size(); i < size; i++) {
      if (list.get(i) < largest) {
        return list.get(i);
      }
    }
    return largest-- > 0 ? largest : 0;
  }

  public ClusterResourceManageable getResourceManager() {
    return resourceManager;
  }

  public void setResourceManager(ClusterResourceManageable resourceManager) {
    this.resourceManager = resourceManager;
  }
}
