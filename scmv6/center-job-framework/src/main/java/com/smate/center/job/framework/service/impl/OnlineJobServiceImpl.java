package com.smate.center.job.framework.service.impl;

import com.smate.center.job.common.enums.JobStatusEnum;
import com.smate.center.job.common.enums.JobWeightEnum;
import com.smate.center.job.common.exception.ServiceException;
import com.smate.center.job.common.po.OnlineJobHistoryPO;
import com.smate.center.job.common.po.OnlineJobPO;
import com.smate.center.job.framework.dao.OnlineJobConfigDAO;
import com.smate.center.job.framework.dao.OnlineJobDAO;
import com.smate.center.job.framework.dao.OnlineJobHistoryDAO;
import com.smate.center.job.framework.dto.OnlineJobDTO;
import com.smate.center.job.framework.service.OnlineJobService;
import com.smate.center.job.framework.util.BeanUtil;
import com.smate.center.job.web.support.jqgrid.PageModel;
import com.smate.center.job.web.vo.OnlineJobVO;
import com.smate.core.base.exception.DAOException;
import com.smate.core.base.utils.collections.ListUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.sun.istack.internal.NotNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.dozer.Mapper;
import org.dozer.MappingException;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 在线任务服务实现类
 *
 * @author houchuanjie
 * @date 2018年4月24日 下午5:57:51
 */
@Service
@Transactional(value = "transactionManager-sns", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class OnlineJobServiceImpl implements OnlineJobService {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private OnlineJobConfigDAO configDao;
  @Autowired
  private OnlineJobHistoryDAO onlineJobHistoryDAO;
  @Autowired
  private OnlineJobDAO onlineJobDAO;
  @Autowired
  private Mapper mapper;

  @Transactional(value = "transactionManager-sns", propagation = Propagation.NEVER, rollbackFor =
      Exception.class)
  @Override
  public List<OnlineJobPO> getEnableList(JobWeightEnum weight, int size) {
    try {
      return Optional.ofNullable(onlineJobDAO.getEnableList(weight, size))
          .orElseGet(Collections::emptyList);
    } catch (Exception e) {
      logger.error("获取已启动的在线任务列表时出现异常！", e);
      throw new ServiceException(e);
    }
  }

  @Transactional(value = "transactionManager-sns", propagation = Propagation.REQUIRES_NEW,
      rollbackFor = Exception.class)
  @Override
  public void batchUpdateUnprocessed(List<OnlineJobDTO> list) {
    if (CollectionUtils.isEmpty(list)) {
      return;
    }
    List<OnlineJobPO> updateList = list.parallelStream()
        .filter(onlineJobDTO -> onlineJobDTO.getStatus() != JobStatusEnum.PROCESSED)
        .map(onlineJobDTO -> mapper.map(onlineJobDTO, OnlineJobPO.class))
        .collect(Collectors.toList());
    List<OnlineJobPO>[] updateLists = ListUtils.split(updateList, 1000);
    Arrays.stream(updateLists).parallel().forEach(onlineJobDAO::batchUpdate);
  }

  @Transactional(value = "transactionManager-sns", propagation = Propagation.REQUIRES_NEW,
      rollbackFor = ServiceException.class)
  @Override
  public void batchUpdateProcessed(List<OnlineJobPO> list) {
    if (CollectionUtils.isEmpty(list)) {
      return;
    }
    List<OnlineJobHistoryPO> historyList = list.parallelStream()
        .filter(onlineJobDTO -> onlineJobDTO.getStatus() == JobStatusEnum.PROCESSED)
        .map(onlineJobPO -> mapper.map(onlineJobPO, OnlineJobHistoryPO.class))
        .collect(Collectors.toList());
    List<String> deleteList = list.parallelStream().map(OnlineJobPO::getId)
        .collect(Collectors.toList());
    try {
      onlineJobHistoryDAO.batchSave(historyList);
      onlineJobDAO.batchDelete(deleteList);
    } catch (DAOException e) {
      logger.error("批量刪除已执行完毕的任务，并保存到历史记录时出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void batchUpdate(List<OnlineJobPO> list) {
    if (CollectionUtils.isEmpty(list)) {
      return;
    }
    List<OnlineJobPO>[] updateLists = ListUtils.split(list, 1000);
    Arrays.stream(updateLists).parallel().forEach(onlineJobDAO::batchUpdate);
  }

  @Override
  public void batchSave(List<OnlineJobPO> list) {
    if (CollectionUtils.isEmpty(list)) {
      return;
    }
    onlineJobDAO.batchSave(list);
  }

  @Override
  public PageModel<OnlineJobVO> searchUnprocessed(PageModel<OnlineJobVO> page) {
    try {
      page.setClassMapping(OnlineJobVO.class, OnlineJobPO.class);
      Order order = page.getHibernateOrder();
      Criterion criterion = page.getHibernateCriterion();
      Criterion expression = Restrictions.not(Restrictions.eq("status",
          JobStatusEnum.FAILED));
      // 查询总记录数
      Long totalCount = onlineJobDAO.getTotalCount(criterion, expression);
      // 查询结果集
      List<OnlineJobPO> resultList = onlineJobDAO
          .search(page.getRows(), page.getPage(), order, criterion, expression);
      // 对查询结果集进行映射，并获取正在执行的任务信息替换，用于页面实时展示
      List<OnlineJobVO> voList = Optional.ofNullable(resultList)
          .map(list -> list.stream().map(onlineJobPO -> {
            return mapper.map(onlineJobPO, OnlineJobVO.class);
          }).collect(Collectors.toList())).orElseGet(Collections::emptyList);
      page.setResultData(totalCount, voList);
    } catch (DAOException | MappingException e) {
      logger.error("查询结果集出错！", e);
      page.setErrMsg("服务器发生内部错误！");
    }
    return page;
  }

  @Override
  public PageModel<OnlineJobVO> searchHistory(PageModel<OnlineJobVO> page) {
    page.setClassMapping(OnlineJobVO.class, OnlineJobHistoryPO.class);
    Order order = page.getHibernateOrder();
    Criterion criterion = page.getHibernateCriterion();
    // 查询总记录数
    Long totalCount = onlineJobHistoryDAO.getTotalCount(criterion);
    // 查询结果集
    List<OnlineJobHistoryPO> resultList = onlineJobHistoryDAO
        .search(page.getRows(), page.getPage(), order, criterion);
    // 对查询结果集进行映射，并获取正在执行的任务信息替换，用于页面实时展示
    List<OnlineJobVO> voList = Optional.ofNullable(resultList)
        .map(list -> list.stream().map(onlineJobHistoryPO -> {
          OnlineJobVO onlineJobVO = mapper.map(onlineJobHistoryPO, OnlineJobVO.class);
          onlineJobVO.setStatus(JobStatusEnum.PROCESSED);
          return onlineJobVO;
        }).collect(Collectors.toList())).orElseGet(Collections::emptyList);
    page.setResultData(totalCount, voList);
    return page;
  }

  @Override
  public PageModel<OnlineJobVO> searchFailed(PageModel<OnlineJobVO> page) {
    try {
      page.setClassMapping(OnlineJobVO.class, OnlineJobPO.class);
      Order order = page.getHibernateOrder();
      Criterion criterion = page.getHibernateCriterion();
      SimpleExpression expression = Restrictions.eq("status", JobStatusEnum.FAILED);
      // 查询总记录数
      Long totalCount = onlineJobDAO.getTotalCount(criterion, expression);
      // 查询结果集
      List<OnlineJobPO> resultList = onlineJobDAO
          .search(page.getRows(), page.getPage(), order, criterion, expression);
      // 对查询结果集进行映射，并获取正在执行的任务信息替换，用于页面实时展示
      List<OnlineJobVO> voList = Optional.ofNullable(resultList)
          .map(list -> list.stream().map(onlineJobPO -> {
            return mapper.map(onlineJobPO, OnlineJobVO.class);
          }).collect(Collectors.toList())).orElseGet(Collections::emptyList);
      page.setResultData(totalCount, voList);
    } catch (DAOException | MappingException e) {
      logger.error("查询结果集出错！", e);
      page.setErrMsg("服务器发生内部错误！");
    }
    return page;
  }

  @Override
  public void batchDelete(List<String> idList) {
    try {
      onlineJobDAO.batchDelete(idList);
    } catch (DAOException e) {
      logger.error("批量删除在线任务出错！idList = {}", JacksonUtils.listToJsonStr(idList), e);
      throw new ServiceException("批量删除任务失败！", e);
    }
  }

  @Override
  public void batchDeleteHistory(List<String> idList) {
    try {
      onlineJobHistoryDAO.batchDelete(idList);
    } catch (DAOException e) {
      logger.error("批量删除在线任务出错！idList = {}", JacksonUtils.listToJsonStr(idList), e);
      throw new ServiceException("批量删除任务失败！", e);
    }
  }

  @Override
  public OnlineJobVO saveOrUpdate(@NotNull OnlineJobVO onlineJobVO) throws ServiceException {
    try {
      OnlineJobPO onlineJobPO = mapper.map(onlineJobVO, OnlineJobPO.class);
      if (Objects.isNull(onlineJobPO.getId())) {
        onlineJobDAO.save(onlineJobPO);
        onlineJobVO = mapper.map(onlineJobPO, OnlineJobVO.class);
      } else {
        OnlineJobPO persistent = onlineJobDAO.get(onlineJobVO.getId());
        BeanUtil.mergeProperties(onlineJobPO, persistent);
        onlineJobDAO.update(persistent);
        onlineJobVO = mapper.map(persistent, OnlineJobVO.class);
      }
      return onlineJobVO;
    } catch (Exception e) {
      logger.error("保存或更新在线任务信息出错！{}", onlineJobVO, e);
      throw new ServiceException("保存或更新在线任务信息失败！", e);
    }
  }
}
