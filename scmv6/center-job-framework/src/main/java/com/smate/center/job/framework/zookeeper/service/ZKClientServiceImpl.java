package com.smate.center.job.framework.zookeeper.service;

import com.smate.center.job.framework.zookeeper.config.ZKConfig;
import com.smate.center.job.framework.zookeeper.exception.ZooKeeperServiceException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.string.StringUtils;
import com.sun.istack.internal.NotNull;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.shared.SharedCount;
import org.apache.curator.framework.recipes.shared.VersionedValue;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * 使用客户端CuratorFramework操作ZooKeeper的客户端服务实现类
 *
 * @author houchuanjie
 * @date 2018/04/02 10:56
 */
@Service
@Lazy(false)
@Order(0)
public class ZKClientServiceImpl implements ZKClientService, ApplicationContextAware,
    DisposableBean {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Value("zookeeper.url")
  private String zookeeperUrl;
  /**
   * ZooKeeper客户端
   */
  @Autowired
  private CuratorFramework client;

  private ApplicationContext ctx;
  /**
   * 共享的整数ID计数器
   */
  private SharedCount counter;

  /**
   * 路径孩子节点监听缓存器列表
   */
  private Map<String, PathChildrenCache> pathChildrenCacheMap = new HashMap<>();

  @Override
  public boolean isConnected() {
    return client.getState() == CuratorFrameworkState.STARTED;
  }

  @Override
  public boolean isExist(String path) throws ZooKeeperServiceException {
    try {
      return Optional.ofNullable(client.checkExists().forPath(path)).map(stat -> true)
          .orElse(false);
    } catch (Exception e) {
      throw new ZooKeeperServiceException("检查ZNode是否存在出现异常！", e);
    }
  }

  @Override
  public Optional<Stat> checkExists(String path) throws ZooKeeperServiceException {
    try {
      return Optional.ofNullable(client.checkExists().forPath(path));
    } catch (Exception e) {
      throw new ZooKeeperServiceException("检查ZNode是否存在出现异常！", e);
    }
  }

  @Override
  public void createNodeIfNotExist(CreateMode mode, String path, String data)
      throws ZooKeeperServiceException {
    createNodeIfNotExist(mode, path,
        StringUtils.defaultString(data).getBytes(StandardCharsets.UTF_8));
  }

  @Override
  public void createNodeIfNotExist(CreateMode mode, String path, byte[] data) {
    Assert.hasText(path, "创建节点的路径不能为空！");
    try {
      if (!isExist(path)) {
        createNode(mode, path, data);
      }
    } catch (Exception e) {
      throw new ZooKeeperServiceException("创建ZNode时出现异常！nodePath=\"" + path + "\"", e);
    }
  }

  @Override
  public List<String> getChildren(String parentPath) throws ZooKeeperServiceException {
    List<String> list = null;
    if (isExist(parentPath)) {
      try {
        list = client.getChildren().forPath(parentPath);
      } catch (Exception e) {
        throw new ZooKeeperServiceException("获取路径" + parentPath + "下的子节点时出现异常！", e);
      }
    }
    return Optional.ofNullable(list).orElseGet(Collections::emptyList);
  }

  @Override
  public void listenPathChildren(String nodePath, PathChildrenCacheListener listener)
      throws ZooKeeperServiceException {
    Assert.hasText(nodePath, "要监听的目标路径不能为空！");
    Assert.notNull(listener, "目标路径的监听器不能为null！");
    try {

      PathChildrenCache pathChildrenCache = pathChildrenCacheMap.get(nodePath);
      if (Objects.isNull(pathChildrenCache)) {
        pathChildrenCache = new PathChildrenCache(client, nodePath, true);
        pathChildrenCache.start();
        pathChildrenCacheMap.put(nodePath, pathChildrenCache);
      }
      pathChildrenCache.getListenable().removeListener(listener);
      pathChildrenCache.getListenable().addListener(listener);
    } catch (Exception e) {
      throw new ZooKeeperServiceException(e);
    }
  }

  @Override
  public void createOrUpdateNode(CreateMode mode, @NotNull String nodePath, String data)
      throws ZooKeeperServiceException {
    Assert.hasText(nodePath, "节点的路径不能为空！");
    createOrUpdateNode(mode, nodePath,
        StringUtils.defaultString(data).getBytes(StandardCharsets.UTF_8));
  }

  @Override
  public void createOrUpdateNode(CreateMode mode, @NotNull String nodePath, byte[] data) {
    Assert.hasText(nodePath, "节点的路径不能为空！");
    try {
      client.create().orSetData().creatingParentContainersIfNeeded().withMode(mode)
          .forPath(nodePath, data);
    } catch (KeeperException.NodeExistsException e) {
      setData(nodePath, data);
    } catch (Exception e) {
      throw new ZooKeeperServiceException("创建节点或更新节点数据时出现异常！", e);
    }
  }

  @Override
  public <T> T getData(String path, Class<T> tClass) throws ZooKeeperServiceException {
    T obj = null;
    try {
      if (isExist(path)) {
        byte[] bytes = client.getData().forPath(path);
        String data = new String(bytes, StandardCharsets.UTF_8);
        obj = JacksonUtils.jsonObject(data, tClass);
      }
    } catch (Exception e) {
      throw new ZooKeeperServiceException(e);
    }
    return obj;
  }

  @Override
  public byte[] getData(String path) throws ZooKeeperServiceException {
    try {
      if (isExist(path)) {
        return client.getData().forPath(path);
      }
      return null;
    } catch (Exception e) {
      throw new ZooKeeperServiceException(e);
    }
  }

  @Override
  public boolean deleteNode(@NotNull String nodePath) {
    Assert.notNull(nodePath, "要删除的节点路径不能为空！");
    try {
      client.delete().guaranteed().forPath(nodePath);
      return true;
    } catch (NoNodeException e) {
      return true;
    } catch (Exception e) {
      logger.error("删除节点失败！nodePath='{}'", nodePath, e);
      return false;
    }
  }

  @Override
  public boolean deleteNodeWithChildren(String nodePath) {
    Assert.notNull(nodePath, "要删除的节点路径不能为空！");
    try {
      if (isExist(nodePath)) {
        client.delete().guaranteed().deletingChildrenIfNeeded().forPath(nodePath);
      }
      return true;
    } catch (KeeperException.NoNodeException e) {
      return true;
    } catch (Exception e) {
      logger.error("删除节点失败！nodePath='{}'", nodePath, e);
      return false;
    }
  }


  @Override
  public Integer nextID() throws ZooKeeperServiceException {
    try {
      counter.start();
      boolean success = false;
      Integer nextValue = null;
      do {
        VersionedValue<Integer> versionedValue = counter.getVersionedValue();
        Integer preValue = versionedValue.getValue();
        // 取原值+1，尝试设置+1后的新值，直到设置成功
        nextValue = preValue + 1;
        success = counter.trySetCount(versionedValue, nextValue);
      } while (!success);
      return nextValue;
    } catch (Exception e) {
      throw new ZooKeeperServiceException("启动节点ID共享计数器出现异常！", e);
    } finally {
      try {
        counter.close();
      } catch (IOException e) {
        throw new ZooKeeperServiceException("关闭节点ID共享计数器出现异常！", e);
      }
    }
  }

  /**
   * 创建节点，并递归创建所需父节点
   *
   * @param mode 创建模式
   * @param path 节点路径
   * @param data 节点数据
   */
  private void createNode(CreateMode mode, @NotNull String path, String data)
      throws ZooKeeperServiceException {
    createNode(mode, path, StringUtils.defaultString(data).getBytes(StandardCharsets.UTF_8));
  }

  /**
   * 创建节点，并递归创建所需父节点
   *
   * @param mode 创建模式
   * @param path 节点路径
   * @param data 节点数据
   */
  private void createNode(CreateMode mode, @NotNull String path, byte[] data)
      throws ZooKeeperServiceException {
    try {
      mode = mode == null ? CreateMode.PERSISTENT : mode;
      client.create().creatingParentContainersIfNeeded().withMode(mode).forPath(path, data);
    } catch (Exception e) {
      throw new ZooKeeperServiceException("创建ZNode节点时出现异常！nodePath='" + path + "'", e);
    }
  }

  /**
   * 设置节点数据
   *
   * @param path 节点路径
   * @param data 节点数据
   * @throws ZooKeeperServiceException
   */
  @Override
  public void setData(@NotNull String path, byte[] data) throws ZooKeeperServiceException {
    try {
      client.setData().forPath(path, data);
    } catch (Exception e) {
      throw new ZooKeeperServiceException("更新ZNode节点数据时出现异常！nodePath='" + path + "'", e);
    }
  }

  /**
   * 设置节点数据
   *
   * @param path 节点路径
   * @param data 节点字符串数据
   * @throws ZooKeeperServiceException
   */
  @Override
  public void setData(@NotNull String path, String data) throws ZooKeeperServiceException {
    setData(path, StringUtils.defaultString(data).getBytes(StandardCharsets.UTF_8));
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    logger.info("Initializing ZooKeeper Client");
    if (client.getState() != CuratorFrameworkState.STARTED) {
      client.start();
    }
    client.getConnectionStateListenable().addListener((client, newState) -> {
      switch (newState) {
        case CONNECTED:
          logger.info("The ZKClient is connected to ZKServer.");
          break;
        case LOST:
          logger.error("The ZKClient is disconnected to ZKServer.");
          break;
        case SUSPENDED:
          logger.warn("The ZKClient is suspended.");
          break;
        case RECONNECTED:
          logger.info("The ZKClient is reconnected to ZKServer.");
          break;
        case READ_ONLY:
          logger.info("The ZKClient is read only.");
        default:
          break;
      }
    });

    init();
  }

  public void init() throws Exception {

    /*this.counter = new SharedCount(client, ZKConfig.NODE_COUNTER_PATH, ZKConfig
    .NODE_COUNTER_INITIAL_VALUE);
    counter.addListener(new NodeIDSharedCountListener());*/
  }

  @Override
  public CuratorFramework getClient() {
    return client;
  }

  public void setClient(CuratorFramework client) {
    this.client = client;
  }

  @Override
  public void blockUntilConnected() throws InterruptedException {
    client.blockUntilConnected();
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.ctx = applicationContext;
  }

  public void setZookeeperUrl(String zookeeperUrl) {
    this.zookeeperUrl = zookeeperUrl;
  }

  @Override
  public void destroy() throws Exception {
    logger.info("Stop listening PathChildrenCache, Shutting down ZKClient");
    PathChildrenCache pathChildrenCache = pathChildrenCacheMap.get(ZKConfig.SERVER_NODES_PATH);
    pathChildrenCache.getListenable().clear();
    pathChildrenCache.close();
    client.close();
  }
}
