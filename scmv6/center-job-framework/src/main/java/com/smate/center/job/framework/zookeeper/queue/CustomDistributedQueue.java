package com.smate.center.job.framework.zookeeper.queue;

import com.smate.center.job.framework.zookeeper.support.ZKNode;
import com.sun.istack.internal.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.EnsureContainers;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义ZooKeeper分布式队列。
 *
 * @author Created by hcj
 * @date 2018/07/05 19:46
 */
public class CustomDistributedQueue<E extends Serializable> {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  protected final CuratorFramework client;
  //队列所在路径
  protected final String path;
  protected final EnsureContainers ensureContainers;
  //队列中节点名称的前缀
  protected final String PREFIX = "qn-";

  public CustomDistributedQueue(@NotNull CuratorFramework client, @NotNull String path) {
    this.client = client;
    this.path = path;
    this.ensureContainers = new EnsureContainers(client, path);
  }

  /*  public Iterator<E> iterator() {
      List<String> children = getChildren();
      elements = new String[children.size()];
      elements = children.toArray(elements);
      return new Itr<E>();
    }*/
  public boolean isEmpty() {
    return size() == 0;
  }

  public int size() {
    return getChildren().size();
  }

  public boolean offer(E element) {
    try {
      String thisPath = ZKPaths.makePath(path, PREFIX);
      client.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL)
          .forPath(thisPath, ZKNode.serialize(element));
      return true;
    } catch (Exception e) {
      logger.error(
          "An error occurs while offer data to the ZK queue. The current queue path is '{}'.",
          path, e);
      return false;
    }
  }

  public E poll() {
    E result = null;
    try {
      result = internalElement(true, null);
    } catch (Exception e) {
      logger.error(
          "An error occurs while poll data from the ZK queue. The current queue path is '{}'.",
          path, e);
    }
    return result;
  }

  /**
   * Retrieves and removes the head of this queue, waiting up to the specified wait time if
   * necessary for an element to become available.
   *
   * @param timeout how long to wait before giving up, in units of
   * <tt>unit</tt>
   * @param unit a <tt>TimeUnit</tt> determining how to interrupt the
   * <tt>timeout</tt> parameter
   * @return the head of this queue, or <tt>null</tt> if the specified waiting time elapses before
   * an element is available
   * @throws Exception errors
   */
  public E poll(long timeout, TimeUnit unit) {
    try {
      return internalPoll(timeout, unit);
    } catch (Exception e) {
      logger.error(
          "An error occurs while poll data from the ZK queue. The current queue path is '{}'.",
          path, e);
      return null;
    }
  }

  public List<ZKNode<E>> toList() {
    List<ZKNode<E>> nodesData = new ArrayList<>();
    List<String> children = getChildren();
    for (String child : children) {
      String thisPath = ZKPaths.makePath(this.path, child);
      try {
        byte[] bytes = client.getData().forPath(thisPath);
        ZKNode<E> zkNode = new ZKNode<>(this.path, child, ZKNode.deserialize(bytes));
        nodesData.add(zkNode);
      } catch (KeeperException.NoNodeException ignore) {
        //Another client removed the node first, try next
      } catch (Exception e) {
        logger.error(
            "An error occurs while get the node data from the queue. The current node name is {}",
            child, e);
      }
    }
    return nodesData;
  }

  /**
   * get the children's node name of the queue path.
   *
   * @return
   */
  public List<String> getChildren() {
    try {
      ensurePath();
      List<String> nodes = client.getChildren().forPath(path);
      Collections.sort(nodes);
      return nodes;
    } catch (KeeperException.NoNodeException dummy) {
      throw new NoSuchElementException();
    } catch (Exception e) {
      logger.error("An error occurs while get the ZK queue size. The current queue path is '{}'.",
          path, e);
    }
    return Collections.emptyList();
  }

  public boolean remove(ZKNode<E> zkNode) {
    String thisPath = zkNode.getPath();
    if (!this.path.equalsIgnoreCase(zkNode.getParentPath())) {
      throw new IllegalArgumentException(
          "The given node is not belong of current queue. The given node's parent path is '"
              + thisPath + "', but the current queue path is '" + this.path + "'");
    }
    try {
      Stat stat = client.checkExists().forPath(thisPath);
      if (stat == null) {
        return false;
      }
      client.delete().guaranteed().forPath(thisPath);
      return true;
    } catch (KeeperException.NoNodeException ignore) {
      //Another client removed the node first, try next
      return true;
    } catch (Exception e) {
      logger.error("An error occurs while delete the ZK node. The path is '{}'.", thisPath, e);
      return false;
    }
  }

  /**
   * @param elements
   * @see #offer(Serializable)
   */
  public void addAll(@NotNull Collection<E> elements) {
    for (E element : elements) {
      offer(element);
    }
  }

  public List<E> clear() {
    List<E> result = new ArrayList<>();
    E ele = null;
    while ((ele = poll()) != null) {
      result.add(ele);
    }
    return result;
  }

  protected void ensurePath() throws Exception {
    ensureContainers.ensure();
  }

  protected E internalPoll(long timeout, TimeUnit unit) throws Exception {
    ensurePath();

    long startMs = System.currentTimeMillis();
    boolean hasTimeout = (unit != null);
    long maxWaitMs = hasTimeout ? TimeUnit.MILLISECONDS.convert(timeout, unit) : Long.MAX_VALUE;
    for (; ; ) {
      final CountDownLatch latch = new CountDownLatch(1);
      Watcher watcher = new Watcher() {
        @Override
        public void process(WatchedEvent event) {
          latch.countDown();
        }
      };
      E result;
      try {
        result = internalElement(true, watcher);
      } catch (NoSuchElementException dummy) {
        logger.debug("Parent containers appear to have lapsed - recreate and retry");
        ensureContainers.reset();
        continue;
      }
      if (result != null) {
        return result;
      }

      if (hasTimeout) {
        long elapsedMs = System.currentTimeMillis() - startMs;
        long thisWaitMs = maxWaitMs - elapsedMs;
        if (thisWaitMs <= 0) {
          return null;
        }
        latch.await(thisWaitMs, TimeUnit.MILLISECONDS);
      } else {
        latch.await();
      }
    }
  }

  protected ZKNode<E> internalElementZKNode(boolean removeIt, Watcher watcher) throws Exception {
    ensurePath();
    List<String> nodes;
    try {
      nodes = (watcher != null) ? client.getChildren().usingWatcher(watcher).forPath(path)
          : client.getChildren().forPath(path);
    } catch (KeeperException.NoNodeException dummy) {
      throw new NoSuchElementException();
    }
    Collections.sort(nodes);

    for (String node : nodes) {
      if (!node.startsWith(PREFIX)) {
        logger.warn("Foreign node in queue path: " + node);
        continue;
      }

      String thisPath = ZKPaths.makePath(path, node);
      try {
        byte[] bytes = client.getData().forPath(thisPath);
        if (removeIt) {
          client.delete().forPath(thisPath);
        }
        return new ZKNode<>(this.path, node, bytes);
      } catch (KeeperException.NoNodeException ignore) {
        //Another client removed the node first, try next
      }
    }
    return null;
  }

  protected E internalElement(boolean removeIt, Watcher watcher) throws Exception {
    ZKNode<E> zkNode = internalElementZKNode(removeIt, watcher);
    if (zkNode != null) {
      return zkNode.getDataObject();
    }
    return null;
  }
}
