package com.smate.center.job.framework.zookeeper.support;

import com.smate.center.job.framework.dto.TaskletDTO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;
import org.apache.commons.lang3.SerializationException;
import org.apache.curator.utils.ZKPaths;

/**
 * ZooKeeper节点封装类
 *
 * @author Created by hcj
 * @date 2018/07/09 11:20
 */
public class ZKNode<T> {

  /**
   * 节点父路径
   */
  private String parentPath;
  /**
   * 节点名称
   */
  private String name;
  /**
   * ZK存储的字节数据
   */
  private byte[] data;

  public ZKNode() {
  }

  public ZKNode(String parentPath, String name, byte[] data) {
    setParentPath(parentPath);
    setName(name);
    setData(data);
  }

  /**
   * 序列化数据对象
   *
   * @return {@link E}类型对象序列化后的字节数组
   * @throws SerializationException 序列化失败时抛出此异常
   */
  public static <E extends Serializable> byte[] serialize(E data) throws SerializationException {
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos)) {
      oos.writeObject(data);
      oos.flush();
      byte[] bytes = bos.toByteArray();
      return bytes;
    } catch (IOException e) {
      throw new SerializationException(
          "An I/O error occurs while serialize the ZK's node data object.", e);
    }
  }

  /**
   * 反序列化数据对象
   *
   * @param bytes {@link E}类型的对象序列化后的字节数据
   * @return 反序列化后 {@link E} 类型的实例对象
   * @throws SerializationException 反序列化失败时抛出此异常
   */
  public static <E> E deserialize(byte[] bytes) throws SerializationException {
    if (Objects.isNull(bytes) || bytes.length == 0) {
      return null;
    }
    try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bis)) {
      return (E) ois.readObject();
    } catch (IOException ex) {
      throw new SerializationException(
          "An I/O error occurs while deserialize the ZK's node data object.", ex);
    } catch (ClassNotFoundException ex) {
      throw new SerializationException("Class of a serialized object cannot be found.", ex);
    }
  }

  public static void main(String[] args) {
    ZKNode<TaskletDTO> zkNode = new ZKNode<>();
    zkNode.setDataObject((TaskletDTO) null);
    System.out.println(zkNode.getDataObject());
  }

  /**
   * 获取节点的路径，可能不是相对于根目录的绝对路径，如果客户端设置了命名空间的话，此路径代表的是相对于命名空间目录的相对路径
   *
   * @return 节点的全路径
   */
  public String getPath() {
    return ZKPaths.makePath(parentPath, name);
  }

  /**
   * 获取节点的父路径
   *
   * @return
   */
  public String getParentPath() {
    return parentPath;
  }

  /**
   * 设置节点的父路径
   *
   * @param parentPath
   */
  public void setParentPath(String parentPath) {
    this.parentPath = parentPath;
  }

  /**
   * 获取节点的名称
   *
   * @return
   */
  public String getName() {
    return name;
  }

  /**
   * 设置节点的名称
   *
   * @param name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * 获取节点数据
   *
   * @return
   */
  public byte[] getData() {
    return data;
  }

  /**
   * 设置节点数据
   *
   * @param data
   */
  public void setData(byte[] data) {
    this.data = data;
  }

  /**
   * 获取节点字节数据（标准java对象序列化字节码）反序列化得到的对象
   *
   * @return
   */
  public T getDataObject() {
    return deserialize(data);
  }

  /**
   * 设置数据对象
   *
   * @param data 序列化对象
   * @param <E> 对象类型，必须是可序列化的
   */
  public <E extends Serializable> void setDataObject(E data) {
    setData(serialize(data));
  }
}
