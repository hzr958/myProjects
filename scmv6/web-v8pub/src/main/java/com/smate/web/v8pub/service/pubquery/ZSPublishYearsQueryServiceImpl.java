package com.smate.web.v8pub.service.pubquery;

import com.smate.core.base.psn.service.psnpub.PsnPubService;
import com.smate.web.v8pub.consts.V8pubConst;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.service.sns.PubFullTextService;
import com.smate.web.v8pub.service.sns.PubSnsService;
import com.smate.web.v8pub.vo.PubListResult;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * 中山系统，获取成果列表查询
 *
 * @author aijiangbin
 * @date 2018-10-22
 */

@Transactional(rollbackFor = Exception.class)
public class ZSPublishYearsQueryServiceImpl extends AbstractPubQueryService {

  @Resource
  private PubSnsService pubSnsService;
  @Resource
  private PubFullTextService pubFullTextService;
  @Resource
  private PsnPubService psnPubService;

  @Override
  public Map<String, Object> checkData(PubQueryDTO pubQueryDTO) {
    Map<String, Object> map = new HashMap<>();
    if (pubQueryDTO.getSearchPsnId() == null || pubQueryDTO.getSearchPsnId() == 0L) {
      map.put(V8pubConst.RESULT, V8pubConst.ERROR);
      map.put(V8pubConst.ERROR_MSG, "查询的人员psnId不能为空");
      return map;
    }
    return null;
  }

  @Override
  public void queryPubs(PubQueryDTO pubQueryDTO) {

  }

  @Override
  public PubListResult assembleData(PubQueryDTO pubQueryDTO) {
    PubListResult listResult = new PubListResult();
    listResult.setPublishYearList(pubSnsService.queryPublishYearList(pubQueryDTO));
    return listResult;
  }

}
