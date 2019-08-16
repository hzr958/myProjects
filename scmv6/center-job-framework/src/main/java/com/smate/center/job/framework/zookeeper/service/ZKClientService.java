package com.smate.center.job.framework.zookeeper.service;

import com.smate.center.job.framework.zookeeper.exception.ZooKeeperServiceException;
import com.sun.istack.internal.NotNull;
import java.util.List;
import java.util.Optional;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.InitializingBean;

/**
 * ZooKeeper客户端操作通用服务接口
 *
 * @author houchuanjie
 * @date 2018/04/02 10:55
 */
public interface ZKClientService extends InitializingBean {

  /**
   * 判断ZooKeeper是否连接成功
   *
   * @return 连接成功返回true，否则返回false
   */
  boolean isConnected();

  /**
   * 检查节点是否存在
   *
   * @param path 节点路径
   * @return 节点存在返回true，不存在返回false
   */
  boolean isExist(@NotNull String path) throws ZooKeeperServiceException;

  /**
   * 获取父节点下的所有子节点名称，如果父节点不存在或者没有子节点，返回空集合
   *
   * @param parentPath
   * @return
   * @throws ZooKeeperServiceException
   */
  List<String> getChildren(@NotNull String parentPath) throws ZooKeeperServiceException;

  /**
   * 通过自增计数器，获取下一个ID
   *
   * @return id，整数型
   * @throws ZooKeeperServiceException
   */
  Integer nextID() throws ZooKeeperServiceException;

  void setData(@NotNull String path, byte[] data) throws ZooKeeperServiceException;

  void setData(@NotNull String path, String data) throws ZooKeeperServiceException;

  /**
   * 获取ZooKeeper客户端
   *
   * @return
   */
  CuratorFramework getClient();

  /**
   * 阻塞线程，直到客户端连接到ZooKeeper。此方法会一直阻塞除非ZooKeeper连接可用或者被打断， 被打断将会抛出{@link InterruptedException}异常。
   *
   * @throws InterruptedException If interrupted while waiting
   */
  void blockUntilConnected() throws InterruptedException;

  /**
   * 检查节点状态
   *
   * @param path 节点路径
   * @return
   * @throws ZooKeeperServiceException
   */
  Optional<Stat> checkExists(String path) throws ZooKeeperServiceException;

  /**
   * 如果path路径节点不存在，则创建节点
   *
   * @param mode 创建模式，为null默认为Persistent
   * @param path 节点存储路径，必须不能为空
   * @param data
   */
  void createNodeIfNotExist(CreateMode mode, @NotNull String path, String data)
      throws ZooKeeperServiceException;

  /**
   * 如果path路径节点不存在，则创建路径
   *
   * @param mode 模式
   * @param path 路径
   * @param data 数据
   */
  void createNodeIfNotExist(CreateMode mode, String path, byte[] data)
      throws ZooKeeperServiceException;

  /**
   * 监听指定路径下的所有子节点的创建、更新、删除等事件
   *
   * @param nodePath
   * @param listener
   */
  void listenPathChildren(@NotNull String nodePath, PathChildrenCacheListener listener)
      throws ZooKeeperServiceException;

  /**
   * 设置或更新节点数据。如果节点不存在，则创建该节点，如果该节点存在，则更新节点数据
   *
   * @param mode 节点创建模式，当节点不存在需要创建时使用，不可为{@code null}
   * @param nodePath 节点路径，不可为{@code null}
   * @param data 节点数据，如果为{@code null}表示删除节点数据信息
   */
  void createOrUpdateNode(@NotNull CreateMode mode, @NotNull String nodePath, String data)
      throws ZooKeeperServiceException;

  /**
   * 获取节点数据，通过json反序列化为指定对象类型
   *
   * @param path 节点路径
   * @param tClass 反序列化对象类型Class
   * @param <T> 反序列化对象类型
   * @return 反序列化后的对象
   * @throws ZooKeeperServiceException
   */
  <T> T getData(@NotNull String path, Class<T> tClass) throws ZooKeeperServiceException;

  /**
   * 获取节点数据
   *
   * @param path 节点路径
   * @return 字节数组
   * @throws ZooKeeperServiceException
   */
  byte[] getData(@NotNull String path) throws ZooKeeperServiceException;

  /**
   * 删除节点，如果 {@code nodePath} 节点下有子节点，则删除失败，如果要一并删除子节点，请使用方法{@link #deleteNodeWithChildren(String)}
   *
   * @param nodePath 节点路径
   * @return 删除失败返回false，成功返回true
   */
  boolean deleteNode(@NotNull String nodePath) throws ZooKeeperServiceException;

  /**
   * 删除指定节点及其子节点，会指定一直尝试进行删除，直到删除成功
   *
   * @param nodePath 节点路径
   * @return 当且仅当出现异常时返回false
   */
  boolean deleteNodeWithChildren(@NotNull String nodePath) throws ZooKeeperServiceException;

  /**
   * 设置或更新节点数据。如果节点不存在，则创建该节点，如果该节点存在，则更新节点数据
   *
   * @param mode 节点创建模式，当节点不存在需要创建时使用，不可为{@code null}
   * @param nodePath 节点路径，不可为{@code null}
   * @param data 节点数据，如果为{@code null}表示删除节点数据信息
   */
  void createOrUpdateNode(CreateMode mode, @NotNull String nodePath, byte[] data)
      throws ZooKeeperServiceException;

}
