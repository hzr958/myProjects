package com.smate.center.batch.service.psn.register;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 计算人员对应成果的匹配分数的业务逻辑实现类.
 * 
 * @author mjg
 * 
 */
@Service("pubScoreManagerService")
@Transactional(rollbackFor = Exception.class)
public class PubScoreManagerServiceImpl implements PubScoreManagerService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubRegisterMatchService registerCnkiPubMatchService;

  @Autowired
  private PubRegisterMatchService registerIsiPubMatchService;

  /**
   * 处理新注册用户的成果匹配.
   * 
   * @param params
   */
  @Override
  public void dealExactMatchedPubScore(Map<String, Object> params) {

    this.registerCnkiPubMatchService.registerUserMatchPub(params);

    this.registerIsiPubMatchService.registerUserMatchPub(params);

  }

}
