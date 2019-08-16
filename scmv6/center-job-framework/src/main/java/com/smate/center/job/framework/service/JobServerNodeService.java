package com.smate.center.job.framework.service;

import java.util.List;

import com.smate.center.job.framework.po.JobServerNodePO;
import com.smate.core.base.exception.ServiceException;

/**
 * 任务服务器节点服务接口
 *
 * @author houchuanjie
 * @date 2018/04/02 15:47
 */
public interface JobServerNodeService {
    /**
     * 通过服务器名称获取服务器节点实体
     *
     * @param name
     *            服务器名称
     * @return
     */
    JobServerNodePO getServerNode(String name) throws ServiceException;

    /**
     * 通过服务器id获取服务器节点实体类
     *
     * @param id
     * @return
     */
    JobServerNodePO getServerNode(Integer id) throws ServiceException;

    /**
     * 保存服务器节点
     *
     * @param node
     */
    void saveServerNode(JobServerNodePO node) throws ServiceException;

    /**
     * 新增服务器节点
     *
     * @param node
     */
    void updateServerNode(JobServerNodePO node) throws ServiceException;

    /**
     * 根据服务器节点ID列表获取服务器节点对象列表
     * 
     * @param serverIdList
     * @return
     * @throws ServiceException
     */
    List<JobServerNodePO> getServerNodeListByIds(List<Integer> serverIdList) throws ServiceException;


    /**
     * 根据服务器节点名称列表批量获取服务器节点对象列表
     * 
     * @param serverNameList
     * @return
     * @throws ServiceException
     */
    List<JobServerNodePO> getServerNodeListByNames(List<String> serverNameList) throws ServiceException;
}
