package com.smate.center.task.service.fund;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.task.exception.ServiceException;


@Service("selfCodeScoreService")
public class SelfCodeScoreServiceImpl implements SelfCodeScoreService {
  /**
   * 
   */
  private static final long serialVersionUID = -4364921574893101880L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private ConstFundCategoryDisService constFundCategoryDisService;



  @Override
  public int countPsnAndFundDiscode(List<String> psnCode, Long categoryId) {
    try {
      if (CollectionUtils.isNotEmpty(psnCode)) {
        List<String> fundList = constFundCategoryDisService.findFundDisCodeByCategoryId(categoryId);
        if (CollectionUtils.isNotEmpty(fundList)) {
          int count = 0;
          loop: for (String psn : psnCode) {
            for (String fund : fundList) {
              if (psn.equals(fund)) {
                count++;
                continue loop;
              }
            }
          }
          return count;
        }
      }
    } catch (ServiceException e) {
      logger.error("个人申请代码与基金代码匹配错误", e);
    }
    return 0;
  }
}
