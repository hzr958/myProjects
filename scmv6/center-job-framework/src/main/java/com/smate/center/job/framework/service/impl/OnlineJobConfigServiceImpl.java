package com.smate.center.job.framework.service.impl;

import com.smate.center.job.common.exception.ServiceException;
import com.smate.center.job.common.po.OnlineJobConfigPO;
import com.smate.center.job.framework.dao.OnlineJobConfigDAO;
import com.smate.center.job.framework.dao.OnlineJobDAO;
import com.smate.center.job.framework.dao.OnlineJobHistoryDAO;
import com.smate.center.job.framework.service.OnlineJobConfigService;
import com.smate.center.job.web.support.jqgrid.PageModel;
import com.smate.center.job.web.vo.OnlineJobConfigVO;
import com.smate.core.base.exception.DAOException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.string.StringUtils;
import com.sun.istack.internal.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.dozer.Mapper;
import org.dozer.MappingException;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * @author houchuanjie
 * @date 2018/04/28 15:17
 */
@Service
@Transactional(value = "transactionManager-sns", propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class OnlineJobConfigServiceImpl implements OnlineJobConfigService {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private OnlineJobConfigDAO onlineJobConfigDAO;
  @Autowired
  private OnlineJobDAO onlineJobDAO;
  @Autowired
  private OnlineJobHistoryDAO onlineJobHistoryDAO;
  @Autowired
  private Mapper mapper;

  @Override
  public void batchUpdate(List<OnlineJobConfigPO> list) {
    if (CollectionUtils.isEmpty(list)) {
      return;
    }
    onlineJobConfigDAO.batchUpdate(list);
  }

  @Override
  public void saveOrUpdate(OnlineJobConfigPO onlineJobConfigPO) throws ServiceException {
    try {
      Assert.notNull(onlineJobConfigPO, "保存或更新onlineJobConfigPO不能为null！");
      Optional<OnlineJobConfigPO> old = Optional
          .ofNullable(onlineJobConfigPO.getId()).map(onlineJobConfigDAO::get);
      if (old.isPresent()) {
        onlineJobConfigDAO.update(onlineJobConfigPO);
      } else {
        onlineJobConfigDAO.save(onlineJobConfigPO);
      }
    } catch (DAOException e) {
      logger.error("保存在线任务配置信息出错！{}", onlineJobConfigPO, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public PageModel<OnlineJobConfigVO> search(PageModel<OnlineJobConfigVO> page) {
    try {
      page.setClassMapping(OnlineJobConfigVO.class, OnlineJobConfigPO.class);
      Order order = page.getHibernateOrder();
      Criterion criterion = page.getHibernateCriterion();
      // 查询总记录数
      Long totalCount = onlineJobConfigDAO.getTotalCount(criterion);
      // 查询结果集
      List<OnlineJobConfigPO> resultList = onlineJobConfigDAO
          .search(page.getRows(), page.getPage(), order, criterion);
      // 对查询结果集进行映射，并获取正在执行的任务信息替换，用于页面实时展示
      List<OnlineJobConfigVO> voList = Optional.ofNullable(resultList)
          .map(list -> list.stream().map(onlineJobConfigPO -> {
            OnlineJobConfigVO vo = mapper.map(onlineJobConfigPO, OnlineJobConfigVO.class);
            long failedCnt = onlineJobDAO.countFailingByJobName(vo.getName());
            Long normalCnt = onlineJobDAO.countNormalByJobName(vo.getName());
            long doneCnt = onlineJobHistoryDAO.countByJobName(vo.getName());
            vo.setDoneCount(doneCnt);
            vo.setNotDoneCount(normalCnt);
            vo.setErrCount(failedCnt);
            return vo;
          }).collect(Collectors.toList())).orElseGet(Collections::emptyList);
      //设置此次查询结果
      page.setResultData(totalCount, voList);
    } catch (DAOException | MappingException e) {
      logger.error("查询结果集出错！", e);
      page.setErrMsg("服务器发生内部错误！");
    }

    return page;
  }

  @Override
  public void addJob(@NotNull OnlineJobConfigVO onlineJobConfigVO) throws ServiceException {
    try {
      OnlineJobConfigPO onlineJobConfigPO = mapper.map(onlineJobConfigVO, OnlineJobConfigPO.class);
      onlineJobConfigPO.setId(null);
      saveOrUpdate(onlineJobConfigPO);
    } catch (MappingException e) {
      logger.error("映射OnlineJobConfigVO对象为OnlineJobConfigPO对象时发生异常！{}", onlineJobConfigVO, e);
      throw new ServiceException("新增保存在线任务信息出错！");
    } catch (Exception e) {
      logger.error("保存或更新OnlineJobConfigPO对象时出现错误！", e);
      throw new ServiceException("新增保存在线任务信息出错！");
    }
  }

  @Override
  public void batchDelete(List<String> idList) throws ServiceException {
    try {
      onlineJobConfigDAO.batchDelete(idList);
    } catch (DAOException e) {
      logger.error("批量删除在线任务出错！idList = {}", JacksonUtils.listToJsonStr(idList), e);
      throw new ServiceException("批量删除任务失败！", e);
    }
  }

  @Override
  public void updateJobInfo(OnlineJobConfigVO onlineJobConfigVO) throws ServiceException {
    try {
      if (StringUtils.isBlank(onlineJobConfigVO.getId())) {
        logger.error("id不能为空，无法更新{}", onlineJobConfigVO);
        throw new ServiceException("更新离线任务id不能为空！");
      }
      OnlineJobConfigPO persistent = onlineJobConfigDAO.get(onlineJobConfigVO.getId());
      OnlineJobConfigPO detached = mapper.map(onlineJobConfigVO, OnlineJobConfigPO.class);
      BeanUtils.copyProperties(detached, persistent, "id", "gmtCreate", "gmtModified");
      onlineJobConfigDAO.update(persistent);
    } catch (Exception e) {
      logger.error("更新在线任务配置信息出错！{}", onlineJobConfigVO, e);
      throw new ServiceException("更新在线任务配置信息失败！", e);
    }
  }
}
