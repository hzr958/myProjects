package com.smate.center.job.framework.cluster;

import com.smate.center.job.framework.po.JobServerNodePO;
import java.util.List;
import java.util.Map;

/**
 * 服务器资源管理器接口
 *
 * @author houchuanjie
 * @date 2018/04/04 17:05
 */
public interface ClusterResourceManageable {


    /**
     * 更新服务器节点元数据
     * 
     * @param serverNode
     */
    void updateServerNode(JobServerNodePO serverNode);

    /**
     * 获取可用服务器节点列表
     * 
     * @return 可用服务器节点信息list
     */
    List<JobServerNodePO> getEnabledServerNodeList();

    /**
     * 获取不可用服务器节点列表
     * 
     * @return
     */
    List<JobServerNodePO> getDisabledServerNodeList();

    /**
     * 获取最近发现的所有服务器节点列表，包含不可用服务器节点
     * @return
     */
    List<JobServerNodePO> getRecentDiscoveredServerNodes();

    /**
     * 获取每个服务器节点对应的剩余可用线程资源的集合。
     * 
     * @return 返回Map的结构：服务器名称作为key，剩余可用线程数为value
     */
    Map<String, Integer> getAvailableThreadSizeMap();

    /**
     * 获取每个服务器节点对应的剩余队列大小的集合
     * 
     * @return 返回Map的结构：服务器名称作为key，剩余队列大小为value
     */
    Map<String, Integer> getAvailableQueueSizeMap();

    /**
     * 获取所有可用服务器节点的剩余队列大小之和
     *
     * @return
     */
    int getAvailableQueueSize();

    /**
     * 获取所有可用服务器节点的剩余线程数之和
     *
     * @return
     */
    int getAvailableThreadSize();
}
