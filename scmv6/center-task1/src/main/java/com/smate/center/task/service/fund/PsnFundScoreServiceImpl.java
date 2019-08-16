package com.smate.center.task.service.fund;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.smate.center.task.model.fund.rcmd.ConstFundCategory;
import com.smate.center.task.model.sns.psn.PsnPrjGrant;


@Service("psnFundScoreService")
public class PsnFundScoreServiceImpl implements PsnFundScoreService {

  /**
   * 
   */
  private static final long serialVersionUID = -6477510639041800310L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public int countPsnAndFundGrant(List<PsnPrjGrant> psnGrantList, ConstFundCategory fund) {
    for (PsnPrjGrant grant : psnGrantList) {
      if (grant.getNameEn() != null && fund.getGrantNameEn() != null
          && grant.getNameEn().toLowerCase().trim().equals(fund.getGrantNameEn().toLowerCase().trim())) {
        return 1;
      }
      if (grant.getNameZh() != null && fund.getGrantNameZh() != null
          && grant.getNameZh().trim().equals(fund.getGrantNameZh().trim())) {
        return 1;
      }
    }
    return 0;
  }
}
