package com.smate.center.job.framework.service;

import com.smate.center.job.framework.dto.TaskletDTO;
import java.util.List;

/**
 * 任务缓存服务
 * 
 * @author houchuanjie
 * @date 2018年1月4日 下午2:34:54
 */
public interface JobCacheService {

    /**
     * 获取指定服务器节点的任务列表放入缓存的时间戳，如果缓存中获取不到，则返回null
     *
     * @param nodeName
     * @return
     */
    public Long getTimestamp(String nodeName);

    /**
     * 更新任务分片列表放入缓存的时间戳
     * 
     * @param nodeName
     *            节点名称
     * @param timestamp
     *            时间戳
     * @return
     */
    public boolean updateTimestamp(String nodeName, long timestamp);

    /**
     * 向指定服务器节点名称的缓存中添加任务列表
     *
     * @param nodeName
     * @param list
     * @return 添加成功返回true，失败或出现异常返回false
     */
    public boolean setTaskletList(String nodeName, List<TaskletDTO> list);

    /**
     * 向指定服务器节点名称的缓存中添加任务列表
     * @param nodeName 节点名称
     * @param list 新增任务列表
     * @return 添加成功返回true，失败或出现异常返回false
     */
    public boolean addTaskletList(String nodeName, List<TaskletDTO> list);

    /**
     * 获取缓存中分配给某节点的任务分片列表，并从缓存中将其删除
     *
     * @param nodeName
     * @return
     */
    public List<TaskletDTO> pollTaskletList(String nodeName);

    /**
     * 向缓存中写入某节点正在执行的任务统计信息列表
     *
     * @param nodeName
     * @param list
     * @return
     */
    public boolean setTaskletStatistics(String nodeName, List<TaskletDTO> list);

    /**
     * 更新缓存中某服务器节点执行完成、失败、出错的任务列表，当缓存中对应该服务器节点的key存在时，将不会写入内容
     *
     * @param nodeName
     * @param list
     * @return 写入成功or失败
     */
    public boolean updateCompletedTaskletList(String nodeName, List<TaskletDTO> list);

    /**
     * 获取并删除某节点执行完成、出错、失败的任务列表
     * 
     * @param nodeName
     * @return
     */
    List<TaskletDTO> pollCompletedTaskletList(String nodeName);

    /**
     * 获取指定节点正在执行的任务分片统计信息
     *
     * @author houchuanjie
     * @date 2018年1月5日 上午10:38:13
     * @param nodeName
     * @return
     */
    public List<TaskletDTO> getTaskletStatistics(String nodeName);

    /**
     * 设置缓存过期时间
     *
     * @param cacheExpiration
     *            过期时间，秒为单位，默认10分钟
     */
    public void setCacheExpiration(Integer cacheExpiration);

    /**
     * 获取缓存过期时间
     *
     * @return cacheExpiration
     */
    public int getCacheExpiration();

    /**
     * 清理缓存
     */
    void clearCache();
}
