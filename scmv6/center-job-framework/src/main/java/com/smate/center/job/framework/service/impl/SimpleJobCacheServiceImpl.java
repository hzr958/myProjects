package com.smate.center.job.framework.service.impl;

import com.smate.center.job.framework.dto.TaskletDTO;
import com.smate.center.job.framework.service.JobCacheService;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.string.StringUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.spy.memcached.CASResponse;
import net.spy.memcached.CASValue;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.OperationFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 简单任务缓存服务实现类
 *
 * @author houchuanjie
 * @date 2018年1月4日 下午2:35:49
 */
public class SimpleJobCacheServiceImpl implements JobCacheService {

  // 任务列表放置缓存中的缓存名称前缀
  private static final String CACHE_NAME_PREFIX = "SimpleJob";
  // 任务缓存key
  private static final String CACHE_KEY_TYPE_TASKLET = "Tasklets";
  // 服务器节点读取、更新任务列表的缓存时间戳key
  private static final String CACHE_KEY_TYPE_TIMESTAMP = "Timestamp";
  // 任务统计信息key
  private static final String CACHE_KEY_TYPE_STATISTICS = "Statistics";
  // 执行完成、失败、出错的任务列表key
  private static final String CACHE_KEY_TYPE_COMPLETED = "Completed";
  // 缓存key格式化规则: CacheName_NodeName_Key
  private static final String CACHE_KEY_FORMAT = "%s_%s_%s";
  // 缓存默认过期时间 30分钟
  private static final int CACHE_DEFAULT_EXP = 30 * 60;
  private Logger logger = LoggerFactory.getLogger(getClass());
  // 统计任务列表缓存的中间名
  private String taskStatisticsCacheName = CACHE_KEY_TYPE_TASKLET;
  // 缓存过期时间，由SimpleTaskManager配置
  private Integer cacheExpiration = CACHE_DEFAULT_EXP;

  private MemcachedClient memcachedClient;

  /**
   * 根据节点名称以及key类型获取缓存key
   *
   * @param nodeName
   * @param keyType
   * @return
   * @author houchuanjie
   * @date 2018年1月4日 下午4:18:13
   */
  private String getKey(String nodeName, String keyType) {
    return String.format(CACHE_KEY_FORMAT, CACHE_NAME_PREFIX, nodeName, keyType);
  }

  /**
   * 获取MemcachedClient
   *
   * @return
   * @author houchuanjie
   * @date 2018年1月4日 下午4:13:48
   */
  protected MemcachedClient getClient() {
    return memcachedClient;
  }

  public void setMemcachedClient(MemcachedClient memcachedClient) {
    this.memcachedClient = memcachedClient;
  }

  /**
   * 获取缓存中分配给某节点的任务分片列表，并从缓存中将其删除
   *
   * @param nodeName
   * @return
   * @author houchuanjie
   * @date 2018年1月4日 下午1:58:56
   */
  @Override
  @SuppressWarnings("unchecked")
  public List<TaskletDTO> pollTaskletList(String nodeName) {
    String key = getKey(nodeName, CACHE_KEY_TYPE_TASKLET);
    List<TaskletDTO> list = Collections.emptyList();
    try {
      // 获取任务版本号
      list = Optional.ofNullable(memcachedClient.gets(key)).map(casValue -> {
        // 获取任务分片列表
        List<TaskletDTO> taskletDTOList = getList(key, TaskletDTO.class);
        // 尝试更新
        CASResponse casResponse = memcachedClient.cas(key, casValue.getCas(),
            JacksonUtils.listToJsonStr(Collections.emptyList()));
        if (casResponse.toString().equalsIgnoreCase("OK")) {
          return taskletDTOList;
        }
        return null;
      }).orElseGet(Collections::emptyList);
    } catch (Exception e) {
      logger.error("从Memcached缓存中获取删除任务数据失败！key：{}", key, e);
    }
    return list;
  }

  /**
   * 向缓存中增加分配给某节点的任务分片列表
   *
   * @param nodeName 节点名称
   * @param list 新增任务列表
   * @return
   */
  @Override
  public boolean addTaskletList(String nodeName, List<TaskletDTO> list) {
    String key = getKey(nodeName, CACHE_KEY_TYPE_TASKLET);
    return addList(key, list);
  }

  /**
   * 向缓存中添加指定节点名的任务分片列表
   *
   * @param nodeName
   * @param list
   * @return 添加成功返回true，失败或出现异常返回false
   * @author houchuanjie
   * @date 2018年1月4日 下午6:09:51
   */
  @Override
  public boolean setTaskletList(String nodeName, List<TaskletDTO> list) {
    String key = getKey(nodeName, CACHE_KEY_TYPE_TASKLET);
    return setList(key, list);
  }

  /**
   * 向缓存中添加某节点的任务分片信息列表
   *
   * @param nodeName
   * @param list
   * @return
   * @author houchuanjie
   * @date 2018年1月30日 上午11:27:50
   */
  @Override
  public boolean setTaskletStatistics(String nodeName, List<TaskletDTO> list) {
    String key = getKey(nodeName, CACHE_KEY_TYPE_STATISTICS);
    return setList(key, list);
  }

  @Override
  public boolean updateCompletedTaskletList(String nodeName, List<TaskletDTO> list) {
    String key = getKey(nodeName, CACHE_KEY_TYPE_COMPLETED);
    return StringUtils.isBlank((String) memcachedClient.get(key)) ? setList(key, list) : false;
  }

  @Override
  public List<TaskletDTO> pollCompletedTaskletList(String nodeName) {
    String key = getKey(nodeName, CACHE_KEY_TYPE_COMPLETED);
    List<TaskletDTO> list = Collections.emptyList();
    try {
      // 获取任务版本号
      list = Optional.ofNullable(memcachedClient.gets(key)).map(casValue -> {
        // 获取任务分片列表
        List<TaskletDTO> taskletDTOList = getList(key, TaskletDTO.class);
        // 尝试更新
        CASResponse casResponse = memcachedClient.cas(key, casValue.getCas(), "");
        if (casResponse.toString().equalsIgnoreCase("OK")) {
          return taskletDTOList;
        }
        return null;
      }).orElseGet(Collections::emptyList);

    } catch (Exception e) {
      logger.error("读取Memcached缓存中服务器节点{}已完成任务列表时出错！", nodeName, e);
    }
    return list;
  }

  private boolean isExist(String key) {
    Object o = getClient().get(key);
    return Objects.nonNull(o);
  }

  private boolean setList(String key, List<?> list) {
    String jsonStr = JacksonUtils.listToJsonStr(list);
    return set(key, jsonStr);
  }

  private boolean addList(String key, List<?> list) {
    boolean flag = false;
    try {
      CASValue<Object> casValue = memcachedClient.gets(key);
      if (Objects.nonNull(casValue)) {
        CASResponse casResponse = null;
        do {
          casValue = memcachedClient.gets(key);
          List<Object> allList = new ArrayList<>(list);
          // 获取数据
          allList.addAll(getList(key, Object.class));
          // 将新列表和旧列表合并后转换为json
          String newJson = JacksonUtils.listToJsonStr(allList);
          // 尝试更新
          casResponse = memcachedClient.cas(key, casValue.getCas(), newJson);
        } while (casResponse == CASResponse.EXISTS);
        if (casResponse == CASResponse.OK) {
          flag = true;
        } else {
          flag = false;
        }
      } else {
        flag = setList(key, list);
      }
    } catch (Exception e) {
      logger.error("向Memcached缓存key = '{}'中增加列表数据失败！", key, e);
    }
    return flag;
  }

  @SuppressWarnings("unchecked")
  private <E> List<E> getList(String key, Class<E> entityClass) {
    List<E> list = null;
    String jsonStr = (String) memcachedClient.get(key);
    if (StringUtils.isNotBlank(jsonStr)) {
      list = JacksonUtils.jsonToCollection(jsonStr, List.class, entityClass);
    }
    return Optional.ofNullable(list).orElseGet(Collections::emptyList);
  }

  /**
   * 获取任务分片列表放入缓存的时间戳
   *
   * @param nodeName
   * @return
   * @author houchuanjie
   * @date 2018年1月4日 下午3:51:20
   */
  @Override
  public Long getTimestamp(String nodeName) {
    // 获取时间戳
    Long timestamp = (Long) memcachedClient.get(getKey(nodeName, CACHE_KEY_TYPE_TIMESTAMP));
    return timestamp;
  }

  /**
   * 更新任务分片列表放入缓存的时间戳
   *
   * @param nodeName 节点名称
   * @param timestamp 时间戳
   * @return
   * @author houchuanjie
   * @date 2018年2月2日 下午12:42:17
   */
  @Override
  public boolean updateTimestamp(String nodeName, long timestamp) {
    String key = getKey(nodeName, CACHE_KEY_TYPE_TIMESTAMP);
    return set(key, timestamp);
  }

  /**
   * 获取指定节点正在执行的任务分片统计信息
   *
   * @param nodeName
   * @return 获取不到返回null，获取到返回任务分片信息列表
   * @author houchuanjie
   * @date 2018年1月5日 上午10:38:13
   */
  @Override
  @SuppressWarnings("unchecked")
  public List<TaskletDTO> getTaskletStatistics(String nodeName) {
    String key = getKey(nodeName, CACHE_KEY_TYPE_STATISTICS);
    return getList(key, TaskletDTO.class);
  }

  private boolean set(String key, Object value) {
    boolean flag = false;
    try {
      OperationFuture<Boolean> result = memcachedClient.set(key, cacheExpiration, value);
      flag = result.get();
    } catch (Exception e) {
      logger.error("Memcached缓存放入列表数据出错！key：{}，Value：{}", key, value, e.getMessage());
      flag = false;
    }
    return flag;
  }

  /**
   * 获取缓存过期时间
   *
   * @return cacheExpiration
   */
  @Override
  public int getCacheExpiration() {
    return cacheExpiration;
  }

  /**
   * 设置缓存过期时间
   *
   * @param cacheExpiration 过期时间，秒为单位，默认10分钟
   */
  @Override
  public void setCacheExpiration(Integer cacheExpiration) {
    this.cacheExpiration = cacheExpiration;
  }

  /**
   * 获取任务统计信息缓存名称
   *
   * @return taskStatisticsCacheName
   */
  public String getTaskStatisticsCacheName() {
    return taskStatisticsCacheName;
  }

  /**
   * 设置任务统计信息缓存名称
   *
   * @param taskStatisticsCacheName 中间名称
   */
  public void setTaskStatisticsCacheName(String taskStatisticsCacheName) {
    this.taskStatisticsCacheName = taskStatisticsCacheName;
  }

  @Override
  public void clearCache() {
    try {
      memcachedClient.delete(getKey("node1", CACHE_KEY_TYPE_STATISTICS));
      memcachedClient.delete(getKey("node1", CACHE_KEY_TYPE_COMPLETED));
      memcachedClient.delete(getKey("node1", CACHE_KEY_TYPE_TASKLET));
    } catch (Exception e) {
      //
    }
  }
}
