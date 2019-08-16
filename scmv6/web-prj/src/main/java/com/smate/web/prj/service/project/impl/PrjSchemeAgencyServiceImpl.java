package com.smate.web.prj.service.project.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.exception.ServiceException;
import com.smate.web.prj.dao.project.PrjSchemeAgencyDao;
import com.smate.web.prj.model.common.PrjSchemeAgency;
import com.smate.web.prj.service.project.PrjSchemeAgencyService;
import com.smate.web.prj.vo.PrjSchemeAgencyVO;

/**
 * 项目资助机构.
 *
 * @author houchuanjie
 * @date 2018年3月20日 下午8:33:16
 */
@Service("prjSchemeAgencyService")
@Transactional(rollbackFor = Exception.class)
public class PrjSchemeAgencyServiceImpl implements PrjSchemeAgencyService {
  private static final long serialVersionUID = 7195171329244250994L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PrjSchemeAgencyDao prjSchemeAgencyDao;

  @Override
  public PrjSchemeAgency findByName(String name) throws ServiceException {
    try {
      return prjSchemeAgencyDao.findByName(name);
    } catch (Exception e) {
      logger.error("通过名字查找项目资助机构错误", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<PrjSchemeAgencyVO> searchPrjSchemeAgencies(String keyword, int maxSize, String language)
      throws ServiceException {
    try {
      List<PrjSchemeAgencyVO> collect = prjSchemeAgencyDao.searchPrjSchemeAgencies(keyword, maxSize).stream()
          .map(psa -> new PrjSchemeAgencyVO(psa, language)).collect(Collectors.toList());
      return collect;
    } catch (Exception e) {
      logger.error("查询指定智能匹配前 N条数据错误", e);
      throw new ServiceException(e);
    }
  }

}
