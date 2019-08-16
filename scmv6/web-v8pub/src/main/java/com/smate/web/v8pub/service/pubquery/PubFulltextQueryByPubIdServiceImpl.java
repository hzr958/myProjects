package com.smate.web.v8pub.service.pubquery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.pub.vo.PubInfo;
import com.smate.web.v8pub.consts.V8pubConst;
import com.smate.web.v8pub.po.sns.PubFullTextPO;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.service.sns.PubFullTextService;
import com.smate.web.v8pub.vo.PubListResult;

/**
 * 通过pubId 查询SNS 库的成果全文信息
 * 
 * @author aijiangbin
 * @date 2018年8月14日
 */
@Transactional(rollbackFor = Exception.class)
public class PubFulltextQueryByPubIdServiceImpl extends AbstractPubQueryService {

  @Resource
  private PubFullTextService pubFullTextService;

  @Override
  public Map<String, Object> checkData(PubQueryDTO pubQueryDTO) {
    Map<String, Object> map = new HashMap<>();
    if (pubQueryDTO.getSearchPubId() == null || pubQueryDTO.getSearchPubId() == 0L) {
      map.put(V8pubConst.RESULT, V8pubConst.ERROR);
      map.put(V8pubConst.ERROR_MSG, "查询的成果的searchPubId不能为空");
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
    List<PubInfo> list = new ArrayList<>();

    PubInfo pubInfo = new PubInfo();
    PubFullTextPO fullTextPO = pubFullTextService.findPubfulltext(pubQueryDTO.getSearchPubId());
    // 全文
    if (fullTextPO != null) {
      pubInfo.setFullTextFieId(fullTextPO.getFileId());
      pubInfo.setFullTextImgUrl(fullTextPO.getThumbnailPath());
      pubInfo.setFullTextName(fullTextPO.getFileName());
      pubInfo.setFullTextId(fullTextPO.getId());
      pubInfo.setFullTextPermission(fullTextPO.getPermission());
    }
    list.add(pubInfo);
    listResult.setResultList(list);

    return listResult;
  }

}
