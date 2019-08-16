package com.smate.center.job.framework.dao;

import com.smate.center.job.framework.po.JobServerNodePO;
import java.util.List;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Repository;

import com.smate.core.base.exception.DAOException;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 任务服务器节点实体
 *
 * @author houchuanjie
 * @date 2018/04/02 15:45
 */
@Repository
@Order(1)
public class JobServerNodeDAO extends SnsHibernateDao<JobServerNodePO, Integer> {
    private static String LIST_BY_ID_HQL = "from JobServerNodePO where id in(:idList)";
    private static String LIST_BY_NAME_HQL = "from JobServerNodePO where name in(:nameList)";

    /**
     * 通过id获取
     *
     * @param id
     * @return
     * @throws DAOException
     */
    public JobServerNodePO getEntity(Integer id) throws DAOException {
        try {
            return super.get(id);
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    /**
     * 通过服务器名称获取
     *
     * @param name
     * @return
     * @throws DAOException
     */
    public JobServerNodePO getEntity(String name) throws DAOException {
        try {
            return super.findUniqueBy("name", name);
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    /**
     * 保存服务器节点
     *
     * @param serverNode
     * @throws DAOException
     */
    public void saveEntity(JobServerNodePO serverNode) throws DAOException {
        try {
            getSession().save(serverNode);
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    /**
     * 更新服务器节点信息
     *
     * @param serverNode
     */
    public void updateEntity(JobServerNodePO serverNode) throws DAOException {
        try {
            getSession().update(serverNode);
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    /**
     * 根据id批量获取服务器信息
     * 
     * @param serverIdList
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<JobServerNodePO> getListByIds(List<Integer> serverIdList) throws DAOException {
        try {
            List<JobServerNodePO> result = getSession().createQuery(LIST_BY_ID_HQL)
                    .setParameterList("idList", serverIdList).list();
            return result;
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    /**
     * 根据名称批量获取服务器列表
     * 
     * @param serverNameList
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<JobServerNodePO> getListByNames(List<String> serverNameList) throws DAOException {
        try {
            List<JobServerNodePO> result = getSession().createQuery(LIST_BY_NAME_HQL)
                    .setParameterList("nameList", serverNameList).list();
            return result;
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }
}
