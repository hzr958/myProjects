package com.smate.web.prj.service.project.impl;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.core.base.consts.model.Institution;
import com.smate.core.base.consts.service.InstitutionService;
import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.psn.dao.WorkHistoryDao;
import com.smate.core.base.psn.model.WorkHistory;
import com.smate.web.prj.service.project.PrjInstitutionService;
import com.smate.web.prj.vo.PrjInsVO;

/**
 * 项目依托单位服务实现类
 * 
 * @author houchuanjie
 * @date 2018年3月26日 上午11:28:52
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class PrjInstitutionServiceImpl implements PrjInstitutionService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private InstitutionService institutionService;
  @Autowired
  private WorkHistoryDao workHistoryDao;

  @Override
  public List<PrjInsVO> searchPrjIns(String keyword, int maxSize, String excludes, Long psnId) throws ServiceException {
    try {
      List<PrjInsVO> result = Collections.emptyList();
      if (StringUtils.isBlank(keyword)) {
        List<WorkHistory> insList = workHistoryDao.findAcInsByPsnId(psnId, excludes);
        result = Optional.ofNullable(insList)
            .map(list -> list.stream().filter(wh -> Objects.nonNull(wh.getInsId()))
                .map(wh -> new PrjInsVO(wh.getInsId(), wh.getInsName())).distinct().collect(Collectors.toList()))
            .orElseGet(Collections::emptyList);
      } else {
        List<Institution> searchIns = institutionService.searchIns(keyword, maxSize, excludes);
        result = searchIns.stream().map(ins -> new PrjInsVO(ins.getId(), ins.getName())).collect(Collectors.toList());
      }
      return Optional.ofNullable(result).orElseGet(Collections::emptyList);

    } catch (Exception e) {
      logger.error("查询指定智能匹配前 N 条数据错误", e);
      throw new ServiceException(e);
    }
  }

}
