package com.smate.center.job.framework.zookeeper.support;

import com.smate.center.job.framework.zookeeper.exception.ZooKeeperServiceException;
import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import org.apache.commons.lang3.SerializationException;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.EnsureContainers;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基于ZooeeKeeper实现自定义的分布式Map
 *
 * @author Created by hcj
 * @date 2018/07/13 11:36
 */
public class CustomDistributedMap<V extends Serializable> implements Map<String, V> {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  protected final CuratorFramework client;
  //map所在路径
  protected final String path;
  protected final EnsureContainers ensureContainers;

  public CustomDistributedMap(CuratorFramework client, String path) {
    this.client = client;
    this.path = path;
    this.ensureContainers = new EnsureContainers(client, path);
    try {
      ensurePath();
    } catch (Exception e) {
    }
  }

  @Override
  public int size() {
    return keySet().size();
  }

  @Override
  public boolean isEmpty() {
    return keySet().isEmpty();
  }

  @Override
  public boolean containsKey(Object key) {
    return keySet().contains(key);
  }

  @Override
  public boolean containsValue(Object value) {
    return values().contains(value);
  }

  /**
   * @see #get(String)
   */
  @Override
  public V get(Object key) {
    return get((String) key);
  }

  /**
   * Returns the value to which the specified key is mapped, or {@code null} if this map contains no
   * mapping for the key.
   *
   * <p>More formally, if this map contains a mapping from a key
   * {@code k} to a value {@code v} such that {@code (key==null ? k==null : key.equals(k))}, then
   * this method returns {@code v}; otherwise it returns {@code null}.  (There can be at most one
   * such mapping.)
   *
   * <p>If this map permits null values, then a return value of
   * {@code null} does not <i>necessarily</i> indicate that the map contains no mapping for the key;
   * it's also possible that the map explicitly maps the key to {@code null}.  The {@link
   * #containsKey containsKey} operation may be used to distinguish these two cases.
   *
   * @param key the key whose associated value is to be returned
   * @return the value to which the specified key is mapped, or {@code null} if this map contains no
   * mapping for the key
   * @throws ClassCastException if the key is of an inappropriate type for this map (<a
   * href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
   * @throws NullPointerException if the specified key is null and this map does not permit null
   * keys (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
   */
  public V get(String key) {
    try {
      String thisPath = ZKPaths.makePath(this.path, key);
      Stat stat = client.checkExists().forPath(thisPath);
      if (stat != null) {
        return ZKNode.deserialize(client.getData().forPath(thisPath));
      }
    } catch (Exception e) {
      logger.error(
          "An error occurs while get value by key from the ZK map. The map's path is \"{}\" and the key is \"{}\"",
          this.path, key, e);
    }
    return null;
  }

  /**
   * Returns the ZKNode instance witch contains the value to which the specified key is mapped, the
   * specified key and the znode's parent path or {@code null} if this map contains no mapping for
   * the key.
   *
   * <p>More formally, if this map contains a mapping from a key
   * {@code k} to a value {@code v} such that {@code (key==null ? k==null : key.equals(k))}, then
   * this method returns {@code v}; otherwise it returns {@code null}.  (There can be at most one
   * such mapping.)
   *
   * <p>If this map permits null values, then a return value of
   * {@code null} does not <i>necessarily</i> indicate that the map contains no mapping for the key;
   * it's also possible that the map explicitly maps the key to {@code null}.  The {@link
   * #containsKey containsKey} operation may be used to distinguish these two cases.
   *
   * @param key the key whose associated value is to be returned
   * @return the ZKNode instance witch contains the value to which the specified key is mapped, the
   * specified key and the znode's parent path or {@code null} if this map contains no mapping for
   * the key.
   * @throws ClassCastException if the key is of an inappropriate type for this map (<a
   * href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
   * @throws NullPointerException if the specified key is null and this map does not permit null
   * keys (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
   */
  public ZKNode<V> getZKNode(String key) {
    try {
      String thisPath = ZKPaths.makePath(this.path, key);
      Stat stat = client.checkExists().forPath(thisPath);
      if (stat != null) {
        return new ZKNode<>(this.path, key, client.getData().forPath(thisPath));
      }
    } catch (Exception e) {
      logger.error(
          "An error occurs while get value by key from the ZK map. The map's path is \"{}\" and the key is\"{}\"",
          this.path, key, e);
    }
    return null;
  }

  @Override
  public V put(String key, V value) {
    String thisPath = ZKPaths.makePath(this.path, key);
    V previousValue = null;
    try {
      Stat stat = client.checkExists().forPath(thisPath);
      if (stat != null) {
        previousValue = ZKNode.deserialize(client.getData().forPath(thisPath));
      }
    } catch (Exception e) {
      //it's ok
    }
    try {
      ensurePath();
      try {
        client.create().orSetData().withMode(CreateMode.PERSISTENT)
            .forPath(thisPath, ZKNode.serialize(value));
      } catch (KeeperException.NodeExistsException e) {
        client.setData().forPath(thisPath, ZKNode.serialize(value));
      }
    } catch (Exception e) {
      logger.error(
          "An error occurs while set value by key from the ZK map. The map's path is \"{}\", the key is \"{}\", and the value is {}",
          this.path, key, value, e);
    }
    return previousValue;
  }

  @Override
  public V remove(Object key) {
    String thisPath = ZKPaths.makePath(this.path, (String) key);
    V previousValue = null;
    try {
      Stat stat = client.checkExists().forPath(thisPath);
      if (stat != null) {
        try {
          previousValue = ZKNode.deserialize(client.getData().forPath(thisPath));
        } catch (Exception e) {
          //it's ok
        }
        client.delete().guaranteed().deletingChildrenIfNeeded().forPath(thisPath);
      }
    } catch (KeeperException.NoNodeException e) {
      //it's ok
    } catch (Exception e) {
      logger.error(
          "An error occurs while remove value by key from the ZK map. The map's path is \"{}\" and the key is \"{}\"",
          this.path, key, e);
    }
    return previousValue;
  }

  /**
   * @throws ZooKeeperServiceException
   */
  @Override
  public void putAll(Map<? extends String, ? extends V> map) {
    if (map != null) {
      try {
        ensurePath();
        for (Entry<? extends String, ? extends V> entry : map.entrySet()) {
          client.create().orSetData().withMode(CreateMode.PERSISTENT)
              .forPath(ZKPaths.makePath(this.path, entry.getKey()),
                  ZKNode.serialize(entry.getValue()));
        }
      } catch (Exception e) {
        logger.error(
            "An error occurs while put all the key-value pair into the ZK map. The map's path is \"{}\"",
            this.path, e);
        throw new ZooKeeperServiceException(
            "KeeperException occurs while create znode or set data.");
      }
    }
  }

  @Override
  public void clear() {
    try {
      client.delete().guaranteed().deletingChildrenIfNeeded().forPath(path);
      ensurePath();
    } catch (Exception e) {
      logger
          .error("An error occurs while clear the ZK map. The map's path is \"{}\"", this.path, e);
    }
  }

  @Override
  public Set<String> keySet() {
    try {
      ensurePath();
      List<String> keys = client.getChildren().forPath(path);
      return new HashSet<>(keys);
    } catch (Exception e) {
      throw new ZooKeeperServiceException(
          "An error occurs while get keys from the ZK map. The map's path is \"" + this.path + '"',
          e);
    }
  }

  /**
   * Returns a {@link Collection} view of the values contained in this map. The collection is backed
   * by the map, so changes to the map are reflected in the collection, and vice-versa.  If the map
   * is modified while an iteration over the collection is in progress (except through the
   * iterator's own <tt>remove</tt> operation), the results of the iteration are undefined.  The
   * collection supports element removal, which removes the corresponding mapping from the map, via
   * the <tt>Iterator.remove</tt>,
   * <tt>Collection.remove</tt>, <tt>removeAll</tt>,
   * <tt>retainAll</tt> and <tt>clear</tt> operations.  It does not
   * support the <tt>add</tt> or <tt>addAll</tt> operations. <br/>
   * <p>
   * <b>Note this method may be very slowly to execute when znodes is too many. If you want to do
   * something with each key-value entry, see {@link #forEach(BiConsumer)} </b>
   * </p>
   *
   * @return a collection view of the values contained in this map
   */
  @Override
  public Collection<V> values() {
    List<V> values = new ArrayList<>();
    Set<String> children = keySet();
    children.parallelStream().forEach(child -> {
      String childPath = ZKPaths.makePath(this.path, child);
      try {
        byte[] bytes = client.getData().forPath(childPath);
        V childValue = ZKNode.deserialize(bytes);
        if (Objects.nonNull(childValue)) {
          values.add(childValue);
        }
      } catch (KeeperException.NoNodeException e) {
        //Another client removed the node, try next
      } catch (SerializationException e) {
        logger.error(
            "An I/O error occurs while deserialize the child node data object. the child path is \"{}\"",
            childPath);
      } catch (Exception e) {
        logger.error("An error occurs while get all values in the ZK map. The map's path is \"{}\"",
            this.path, e);
        throw new ZooKeeperServiceException("KeeperException occurs while get data.");
      }
    });
    return values;
  }

  @Override
  public void forEach(BiConsumer<? super String, ? super V> action) {
    Set<String> keys = keySet();
    keys.parallelStream().forEach(k -> {
      String keyPath = ZKPaths.makePath(this.path, k);
      try {
        byte[] bytes = client.getData().forPath(keyPath);
        V v = ZKNode.deserialize(bytes);
        if (Objects.nonNull(v)) {
          action.accept(k, v);
        }
      } catch (KeeperException.NoNodeException e) {
        //Another client removed the node, try next
      } catch (SerializationException e) {
        logger.error(
            "An I/O error occurs while deserialize the child node data object. the child path is \"{}\"",
            keyPath);
      } catch (Exception e) {
        throw new ZooKeeperServiceException(
            "An error occurs while get all values in the ZK map. The map's path is \"" + this.path
                + '"', e);
      }
    });
  }

  @Override
  public Set<Entry<String, V>> entrySet() {
    try {
      ensurePath();
      Set<Entry<String, V>> values = new HashSet<>();
      List<String> children = client.getChildren().forPath(this.path);
      for (String child : children) {
        String childPath = ZKPaths.makePath(this.path, child);
        V childValue = ZKNode.deserialize(client.getData().forPath(childPath));
        values.add(new SimpleEntry<>(child, childValue));
      }
      return values;
    } catch (Exception e) {
      logger.error("An error occurs while get all key-value pairs in the ZK map.", e);
      throw new ZooKeeperServiceException("KeeperException occurs while get data.");
    }
  }

  protected void ensurePath() throws Exception {
    ensureContainers.ensure();
  }

  public CuratorFramework getClient() {
    return client;
  }

  public String getPath() {
    return path;
  }
}
