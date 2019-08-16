package com.smate.center.job.framework.zookeeper.listener;

import com.smate.center.job.framework.cluster.ClusterResourceManager;
import com.smate.center.job.framework.po.JobServerNodePO;
import com.smate.center.job.framework.zookeeper.config.ZKConfig;
import com.smate.center.job.framework.zookeeper.support.ZKNode;
import java.util.Objects;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 服务器节点路径（{@link ZKConfig#SERVER_NODES_PATH}）监听器 监听所有服务器节点的创建、删除以及节点数据的更新，无限监听，但其子节点不再监听
 *
 * @author houchuanjie
 * @date 2018/04/03 16:22
 */
@Component
public class ServerNodesPathChildrenCacheListener implements PathChildrenCacheListener {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private ClusterResourceManager resourceManager;

  @Override
  public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
    if (Objects.nonNull(event) && Objects.nonNull(event.getData())) {
      JobServerNodePO serverNode = ZKNode.deserialize(event.getData().getData());
      switch (event.getType()) {
        // 服务器节点创建
        case CHILD_ADDED:
          resourceManager.addEnabledServerNode(serverNode);
          break;
        // 服务器节点删除
        case CHILD_REMOVED:
          resourceManager.addDisabledServerNode(serverNode);
          break;
        // 服务器节点数据更新
        case CHILD_UPDATED:
          resourceManager.updateServerNode(serverNode);
          break;
        default:
          break;
      }
    }
  }
}
