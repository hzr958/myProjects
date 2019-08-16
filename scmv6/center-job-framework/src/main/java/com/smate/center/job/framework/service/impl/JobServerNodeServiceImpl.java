package com.smate.center.job.framework.service.impl;

import com.smate.center.job.framework.po.JobServerNodePO;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.smate.center.job.framework.dao.JobServerNodeDAO;
import com.smate.center.job.framework.service.JobServerNodeService;
import com.smate.core.base.exception.DAOException;
import com.smate.core.base.exception.ServiceException;

/**
 * 任务服务器节点服务实现类
 *
 * @author houchuanjie
 * @date 2018/04/02 15:55
 */
@Service
@Order(2)
@Transactional(rollbackOn = Exception.class)
public class JobServerNodeServiceImpl implements JobServerNodeService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private JobServerNodeDAO jobServerNodeDAO;

    @Override
    public JobServerNodePO getServerNode(String name) throws ServiceException {
        Assert.notNull(name, "serverName不能为空！");
        try {
            return jobServerNodeDAO.getEntity(name);
        } catch (DAOException e) {
            logger.error("获取服务器节点信息出现错误！name={}", name, e);
            throw new ServiceException("获取服务器节点信息出现错误！name=\"" + name + "\"");
        }
    }

    @Override
    public JobServerNodePO getServerNode(Integer id) throws ServiceException {
        Assert.notNull(id, "serverID不能为空！");
        try {
            return jobServerNodeDAO.getEntity(id);
        } catch (DAOException e) {
            logger.error("获取服务器节点信息出现错误！id={}", id, e);
            throw new ServiceException("获取服务器节点信息出现错误！id=" + id);
        }
    }

    @Override
    public void saveServerNode(JobServerNodePO node) throws ServiceException {
        Assert.notNull(node, "要保存的askServerNodeT实例不能为空！");
        try {
            Date date = new Date();
            node.setGmtCreate(date);
            node.setGmtModified(date);
            jobServerNodeDAO.saveEntity(node);
        } catch (DAOException e) {
            logger.error("保存服务器节点对象出现错误！{}", node, e);
            throw new ServiceException("保存服务器节点对象出现错误！" + node);
        }
    }

    @Override
    public void updateServerNode(JobServerNodePO node) throws ServiceException {
        Assert.notNull(node, "要更新的askServerNode实例不能为空！");
        try {
            node.setGmtModified(new Date());
            jobServerNodeDAO.updateEntity(node);
        } catch (DAOException e) {
            logger.error("更新服务器节点对象出现错误！{}", node, e);
            throw new ServiceException("更新服务器节点对象出现错误！" + node);
        }
    }

    @Override
    public List<JobServerNodePO> getServerNodeListByIds(List<Integer> serverIdList) throws ServiceException {
        try {
            return jobServerNodeDAO.getListByIds(serverIdList);
        } catch (DAOException e) {
            logger.error("获取服务器节点列表时出现异常！serverIdList={}", serverIdList, e);
            throw new ServiceException("获取服务器节点列表出现错误！");
        }
    }

    @Override
    public List<JobServerNodePO> getServerNodeListByNames(List<String> serverNameList) throws ServiceException {
        if (CollectionUtils.isEmpty(serverNameList)) {
            return Collections.emptyList();
        }
        try {
            return jobServerNodeDAO.getListByNames(serverNameList);
        } catch (DAOException e) {
            logger.error("获取服务器节点列表时出现异常！serverNameList={}", serverNameList, e);
            throw new ServiceException("获取服务器节点列表出现错误！");
        }
    }
}
