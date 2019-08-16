package com.smate.web.prj.service.project.impl;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.exception.ServiceException;
import com.smate.web.prj.dao.project.PrjSchemeDao;
import com.smate.web.prj.model.common.PrjScheme;
import com.smate.web.prj.service.project.PrjSchemeService;
import com.smate.web.prj.vo.PrjSchemeVO;

/**
 * 项目资助类别.
 * 
 * @author liqinghua
 * 
 */
@Service("prjSchemeService")
@Transactional(rollbackFor = Exception.class)
public class PrjSchemeServiceImpl implements PrjSchemeService {

  /**
   * 
   */
  private static final long serialVersionUID = -2485041014431827715L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PrjSchemeDao prjSchemeDao;

  @Override
  public PrjScheme find(String name, Long agencyId) throws ServiceException {
    try {
      return prjSchemeDao.find(name, agencyId);
    } catch (Exception e) {
      logger.error("通过名字查找项目资助类别错误", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<PrjSchemeVO> searchPrjSchemes(String nameKeyword, Long agencyId, int maxSize, String language)
      throws ServiceException {
    try {
      if (Objects.isNull(agencyId)) {
        return Collections.emptyList();
      }

      Optional<List<PrjScheme>> optList =
          Optional.ofNullable(prjSchemeDao.searchPrjSchemeAgencies(nameKeyword, agencyId, maxSize));
      return optList.map(list -> list.stream().map(ps -> new PrjSchemeVO(ps, language)).collect(Collectors.toList()))
          .orElseGet(Collections::emptyList);

    } catch (Exception e) {
      logger.error("查询指定智能匹配前 N 条数据错误", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public PrjScheme findByName(String name) throws ServiceException {
    try {
      return prjSchemeDao.findByName(name);
    } catch (Exception e) {
      logger.error("通过名字查找项目资助类别错误", e);
      throw new ServiceException(e);
    }
  }

}
