package com.smate.center.job.framework.service.impl;

import com.smate.center.job.common.enums.JobStatusEnum;
import com.smate.center.job.common.enums.JobWeightEnum;
import com.smate.center.job.common.exception.ServiceException;
import com.smate.center.job.common.po.OfflineJobPO;
import com.smate.center.job.framework.dao.DynamicDataSourceDAO;
import com.smate.center.job.framework.dao.OfflineJobDAO;
import com.smate.center.job.framework.dto.OfflineJobDTO;
import com.smate.center.job.framework.service.OfflineJobService;
import com.smate.center.job.framework.util.BeanUtil;
import com.smate.center.job.framework.zookeeper.config.ZKConfig;
import com.smate.center.job.framework.zookeeper.exception.ZooKeeperServiceException;
import com.smate.center.job.framework.zookeeper.service.ZKClientService;
import com.smate.center.job.framework.zookeeper.support.ZKNode;
import com.smate.center.job.web.support.jqgrid.PageModel;
import com.smate.center.job.web.vo.OfflineJobVO;
import com.smate.core.base.exception.DAOException;
import com.smate.core.base.utils.collections.ListUtils;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.string.StringUtils;
import com.sun.istack.internal.NotNull;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.curator.utils.ZKPaths;
import org.dozer.Mapper;
import org.dozer.MappingException;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * 离线任务相关操作服务实现类
 *
 * @author houchuanjie
 * @date 2017年12月28日 下午4:53:50
 */
@Service
@Transactional(value = "transactionManager-sns", propagation = Propagation.REQUIRED, rollbackFor
    = Exception.class)
public class OfflineJobServiceImpl implements OfflineJobService {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private OfflineJobDAO offlineJobDAO;
  @Autowired
  private ZKClientService zkClientService;
  @Autowired
  private DynamicDataSourceDAO dynamicDataSourceDAO;
  @Autowired
  private Mapper mapper;

  @Override
  public boolean hasDbSessionEnum(DBSessionEnum dbSessionEnum) {
    return dynamicDataSourceDAO.hasDbSessionEnum(dbSessionEnum);
  }

  @Override
  public boolean isStopped(String jobId) throws ServiceException {
    try {
      OfflineJobPO offlineJobPO = offlineJobDAO.get(jobId);
      return !offlineJobPO.isEnable() || offlineJobPO.getStatus()
          .in(JobStatusEnum.FAILED, JobStatusEnum.PROCESSED);
    } catch (DAOException e) {
      logger.error("查询任务是否正在执行时出错！", e);
      throw new ServiceException(e);
    }
  }

  @Transactional(value = "transactionManager-sns", propagation = Propagation.NEVER, rollbackFor =
      Exception.class)
  @Override
  public boolean hasUniqueKey(OfflineJobDTO jobInfo) {
    // 检查任务信息表给定的唯一键是否唯一
    boolean isUniqueKey = dynamicDataSourceDAO
        .isUniqueKey(jobInfo.getDbSessionEnum(), jobInfo.getTableName(), jobInfo.getUniqueKey());
    return isUniqueKey;
  }

  @Transactional(value = "transactionManager-sns", propagation = Propagation.NEVER, rollbackFor =
      Exception.class)
  @Override
  public boolean hasTable(OfflineJobDTO jobInfo) {
    boolean hasTable = dynamicDataSourceDAO
        .hasTable(jobInfo.getDbSessionEnum(), jobInfo.getTableName());
    return hasTable;
  }

  @Transactional(value = "transactionManager-sns", propagation = Propagation.NEVER, rollbackFor =
      Exception.class)
  @Override
  public long getRecordCount(OfflineJobDTO jobInfo) {
    return dynamicDataSourceDAO.getRecordCount(jobInfo);
  }

  @Transactional(value = "transactionManager-sns", propagation = Propagation.NEVER, rollbackFor =
      Exception.class)
  @Override
  public List<Long> getUniqueKeyList(OfflineJobDTO jobInfo, List<Long> seqList) {
    return dynamicDataSourceDAO.getUniqueKeyList(jobInfo, seqList);
  }

  @Transactional(value = "transactionManager-sns", propagation = Propagation.NEVER, rollbackFor =
      Exception.class)
  @Override
  public List<OfflineJobPO> getAll() {
    return offlineJobDAO.getAll();
  }

  @Override
  public void saveOrUpdate(@NotNull OfflineJobPO offlineJobPO) throws ServiceException {
    Assert.notNull(offlineJobPO, "保存或更新的OfflineJobPO对象不能为空！");
    try {
      Optional<OfflineJobPO> old = Optional.of(offlineJobPO.getId()).map(offlineJobDAO::get);
      if (old.isPresent()) {
        offlineJobDAO.update(offlineJobPO);
      } else {
        offlineJobDAO.save(offlineJobPO);
      }
    } catch (DAOException e) {
      logger.error("保存或更新时发生错误！", e);
      throw new ServiceException("保存或更新时发生错误！");
    }
  }

  @Transactional(value = "transactionManager-sns", propagation = Propagation.NEVER, rollbackFor =
      Exception.class)
  @Override
  public List<OfflineJobPO> getEnableList(JobWeightEnum weight, int size) {
    return Optional.ofNullable(offlineJobDAO.getEnableList(weight, size))
        .orElseGet(Collections::emptyList);
  }

  @Override
  public void batchUpdateJobInfo(List<OfflineJobDTO> list) {
    if (CollectionUtils.isEmpty(list)) {
      return;
    }
    List<OfflineJobDTO>[] lists = ListUtils.split(list, 1000);
    for (List<OfflineJobDTO> offlineJobDTOList : lists) {
      // 获取要更新的离线任务对象列表
      List<OfflineJobPO> offlineJobPOList = offlineJobDTOList.stream()
          .map(offlineJobDTO -> mapper.map(offlineJobDTO, OfflineJobPO.class))
          .collect(Collectors.toList());
      // 批量更新保存
      offlineJobDAO.batchUpdate(offlineJobPOList);
    }
  }

  @Transactional(value = "transactionManager-sns", propagation = Propagation.NEVER, rollbackFor =
      Exception.class)
  @Override
  public PageModel<OfflineJobVO> search(PageModel<OfflineJobVO> page) {
    try {
      page.setClassMapping(OfflineJobVO.class, OfflineJobPO.class);
      Order order = page.getHibernateOrder();
      Criterion criterion = page.getHibernateCriterion();

      // 查询总记录数
      Long totalCount = offlineJobDAO.getTotalCount(criterion);
      // 查询结果集
      List<OfflineJobPO> resultList = offlineJobDAO
          .search(page.getRows(), page.getPage(), order, criterion);
      // 对查询结果集进行映射，并获取正在执行的任务信息替换，用于页面实时展示
      List<OfflineJobVO> voList = convert2VOList(resultList);
      page.setResultData(totalCount, voList);
    } catch (DAOException | MappingException e) {
      logger.error("查询结果集出错！", e);
      page.setErrMsg("服务器发生内部错误！");
    }

    return page;
  }

  @Override
  public void addJob(@NotNull OfflineJobVO offlineJobVO) throws ServiceException {
    try {
      OfflineJobPO offlineJobPO = mapper.map(offlineJobVO, OfflineJobPO.class);
      offlineJobPO.setId(null);
      offlineJobPO.setStatus(JobStatusEnum.UNPROCESS);
      offlineJobDAO.save(offlineJobPO);
    } catch (MappingException e) {
      logger.error("映射OfflineJobVO对象为OfflineJobPO对象时发生异常！{}", offlineJobVO, e);
      throw new ServiceException("新增保存离线任务信息出错！");
    } catch (DAOException e) {
      logger.error("保存或更新OfflineJobPO对象时出现错误！", e);
      throw new ServiceException("新增保存离线任务信息出错！");
    }
  }

  @Override
  public void batchDelete(List<String> idList) throws ServiceException {
    try {
      offlineJobDAO.batchDelete(idList);
    } catch (DAOException e) {
      logger.error("批量删除离线任务出错！idList = {}", JacksonUtils.listToJsonStr(idList), e);
      throw new ServiceException("批量删除离线任务出错！");
    }
  }

  @Override
  public void updateJobInfo(@NotNull OfflineJobVO offlineJobVO) throws ServiceException {
    try {
      if (StringUtils.isBlank(offlineJobVO.getId())) {
        logger.error("id不能为空，无法更新{}", offlineJobVO);
        throw new ServiceException("更新离线任务id不能为空！");
      }
      OfflineJobPO persistent = offlineJobDAO.get(offlineJobVO.getId());
      OfflineJobPO detached = mapper.map(offlineJobVO, OfflineJobPO.class);

      BeanUtils.copyProperties(detached, persistent, "id", "status", "gmtCreate", "gmtModified");
      offlineJobDAO.update(persistent);
      mapper.map(persistent, offlineJobVO);
    } catch (Exception e) {
      logger.error("更新离线任务信息出错！{}", offlineJobVO, e);
      throw new ServiceException("更新离线任务出错！");
    }
  }

  @Override
  public void updateJobInfo(@NotNull OfflineJobDTO offlineJobDTO) {
    if (StringUtils.isBlank(offlineJobDTO.getId())) {
      logger.error("id不能为空，无法更新taskInfo={}", offlineJobDTO);
      return;
    }
    try {
      OfflineJobPO offlineJobPO = offlineJobDAO.get(offlineJobDTO.getId());
      copyProperties(offlineJobPO, offlineJobDTO);
      offlineJobDAO.update(offlineJobPO);
    } catch (DAOException e) {
      logger.error("更新离线任务信息出错！{}", offlineJobDTO, e);
    }
  }

  @Transactional(value = "transactionManager-sns", propagation = Propagation.NEVER, rollbackFor =
      Exception.class)
  @Override
  public List<OfflineJobVO> getNewestList(@NotNull List<String> idList) {
    Assert.notNull(idList, "获取最新的离线任务Id列表不能为空");
    try {
      // 从数据库中先获取最新数据
      List<OfflineJobPO> poList = offlineJobDAO.getByIds(idList);
      // 取zookeeper中正在执行的任务状态信息
      List<OfflineJobVO> voList = poList.stream().map(offlineJobPO -> {
        String path = ZKPaths.makePath(ZKConfig.DISTRIBUTED_JOB_PATH, offlineJobPO.getId());
        try {
          if (offlineJobPO.getStatus()
              .in(JobStatusEnum.DISTRIBUTED, JobStatusEnum.WAITING, JobStatusEnum.PROCESSING)) {
            OfflineJobDTO newData = ZKNode.deserialize(zkClientService.getData(path));
            if (Objects.nonNull(newData)) {
              copyProperties(offlineJobPO, newData);
            }
          }
        } catch (ZooKeeperServiceException e) {
          logger.error("读取正在执行的离线任务对应的ZKNode节点'{}'时出错！", path, e);
        }
        return mapper.map(offlineJobPO, OfflineJobVO.class);
      }).collect(Collectors.toList());
      return voList;
    } catch (Exception e) {
      logger.error("获取离线任务最新信息时出错！idList = {}", JacksonUtils.listToJsonStr(idList), e);
      return Collections.emptyList();
    }
  }

  @Override
  public void batchUpdate(List<OfflineJobPO> offlineJobs) {
    if (CollectionUtils.isEmpty(offlineJobs)) {
      return;
    }
    List<OfflineJobPO>[] lists = ListUtils.split(offlineJobs, 1000);
    Arrays.stream(lists).forEach(offlineJobDAO::batchUpdate);
  }

  @Override
  public List<?> getJobData(long begin, long end, int batchSize, Class<?> persistentClass,
      String jobId) throws ServiceException {
    try {
      OfflineJobPO offlineJobPO = offlineJobDAO.get(jobId);
      return dynamicDataSourceDAO.getJobData(begin, end, batchSize, persistentClass, offlineJobPO);
    } catch (DAOException e) {
      String errMsg = MessageFormat
          .format("加载离线任务数据出错！jobId={0}, 数据区间：[{1}, {2}]，获取条数={3}，classType={4}；", jobId, begin,
              end, batchSize, persistentClass);
      throw new ServiceException(errMsg + e.getMessage(), e);
    }
  }

  @Transactional
  @Override
  public OfflineJobVO enableJob(String jobId, boolean enable) throws ServiceException {
    try {
      OfflineJobPO offlineJobPO = offlineJobDAO.get(jobId);
      offlineJobPO.setEnable(enable);
      offlineJobDAO.update(offlineJobPO);
      return mapper.map(offlineJobPO, OfflineJobVO.class);
    } catch (DAOException e) {
      logger.error("{}任务，更新数据时发生错误！jobId='{}'", enable ? "开启" : "禁用", jobId);
      throw new ServiceException(
          MessageFormat.format("更新任务开关状态为{0}时出现DAO异常！jobId=''{1}''", enable ? "开启" : "禁用", jobId));
    } catch (MappingException e) {
      logger.error("{}任务，返回结果映射时发生错误！jobId='{}'", enable ? "开启" : "禁用", jobId);
      return null;
    }
  }

  @Transactional
  @Override
  public OfflineJobVO updateWithOnlyChanged(OfflineJobVO offlineJobVO) throws ServiceException {
    try {
      OfflineJobPO modified = mapper.map(offlineJobVO, OfflineJobPO.class);
      OfflineJobPO persistent = offlineJobDAO.get(modified.getId());
      BeanUtil.mergeProperties(modified, persistent);
      offlineJobDAO.update(persistent);
      BeanUtil.map(persistent, offlineJobVO);
      return offlineJobVO;
    } catch (MappingException e) {
      logger.error("修改任务信息成功后返回结果映射是发生错误！", e);
    } catch (BeansException e) {
      logger.error("合并修改的任务信息时发生错误！合并的数据：{}", JacksonUtils.jsonObjectSerializer(offlineJobVO), e);
      throw new ServiceException("更新任务信息出错！", e);
    } catch (DAOException e) {
      logger.error("更新任务数据时发生错误！更新的信息：{}", JacksonUtils.jsonObjectSerializer(offlineJobVO));
      throw new ServiceException("更新任务信息出错！", e);
    }
    return offlineJobVO;
  }

  @Override
  public OfflineJobPO getById(@NotNull String jobId) {
    try {
      OfflineJobPO offlineJobPO = offlineJobDAO.getStateless(jobId);
      return offlineJobPO;
    } catch (Exception e) {
      logger.error("获取离线任务信息时出错！jobId='{}'", jobId, e);
      throw new ServiceException("获取离线任务信息出错！");
    }
  }


  private List<OfflineJobVO> convert2VOList(List<OfflineJobPO> list) {
    if (Objects.isNull(list)) {
      return Collections.emptyList();
    }
    List<OfflineJobVO> voList = list.stream().map(offlineJobPO -> {
      OfflineJobVO offlineJobVO = mapper.map(offlineJobPO, OfflineJobVO.class);
      if (offlineJobVO.getStatus()
          .in(JobStatusEnum.DISTRIBUTED, JobStatusEnum.WAITING, JobStatusEnum.PROCESSING)) {
        String nodePath = ZKPaths.makePath(ZKConfig.DISTRIBUTED_JOB_PATH, offlineJobVO.getId());
        try {
          if (zkClientService.isExist(nodePath)) {
            OfflineJobDTO jobDTO = ZKNode.deserialize(zkClientService.getData(nodePath));
            offlineJobVO.setErrMsg(jobDTO.getErrMsg());
            offlineJobVO.setStatus(jobDTO.getStatus());
            offlineJobVO.setPercent(jobDTO.getPercent());
          }
        } catch (ZooKeeperServiceException e) {
          logger.error("获取ZKNode节点'{}'数据时出现异常！", nodePath, e);
        }
      }
      return offlineJobVO;
    }).collect(Collectors.toList());
    return voList;
  }

  /**
   * 复制offlineJobDTO属性值到offlineJobPO对应属性上
   */
  private void copyProperties(final OfflineJobPO offlineJobPO, final OfflineJobDTO offlineJobDTO) {
    offlineJobPO.setCount(offlineJobDTO.getCount());
    offlineJobPO.setPercent(offlineJobDTO.getPercent());
    offlineJobPO.setStatus(offlineJobDTO.getStatus());
    offlineJobPO.setThreadCount(offlineJobDTO.getThreadCount());
    offlineJobPO.setStartTime(offlineJobDTO.getStartTime());
    offlineJobPO.setElapsedTime(offlineJobDTO.getElapsedTime());
    switch (offlineJobDTO.getStatus()) {
      case FAILED:
        offlineJobPO.setErrMsg(offlineJobDTO.getErrMsg());
        break;
      case UNPROCESS:
      case WAITING:
      case PROCESSING:
      case PROCESSED:
      default:
        offlineJobPO.setErrMsg("");
        break;
    }
  }
}
