package com.smate.web.v8pub.service.pdwh;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.v8pub.consts.V8pubConst;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.service.query.PubQueryhandlerService;
import com.smate.web.v8pub.vo.PubListResult;
import com.smate.web.v8pub.vo.PubListVO;

@Service("pdwhSearchService")
@Transactional(rollbackFor = Exception.class)
public class PdwhSearchServiceImpl implements PdwhSearchService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubQueryhandlerService pubQueryhandlerService;

  @SuppressWarnings("unchecked")
  @Override
  public void getPapers(PubListVO pubListVO) {
    try {
      PubListResult result = pubQueryhandlerService.queryPub(pubListVO.getPubQueryDTO());
      if (result.status.equals(V8pubConst.SUCCESS)) {
        pubListVO.setResultList(result.resultList);
        pubListVO.setTotalCount(result.totalCount);
        if (PubQueryDTO.SEQ_2.equals(pubListVO.getPubQueryDTO().getSeqQuery())) {// 要构建左侧状态菜单栏
          Map<String, Object> resultData = result.getResultData();
          pubListVO.setYearMap((Map<Long, Long>) resultData.get("pubYear"));
          pubListVO.setPubTypeMap((Map<Long, Long>) resultData.get("pubType"));
          pubListVO.setLanguageMap((Map<String, Long>) resultData.get("languageType"));
          pubListVO.setSearchMenu(true);
        }
      }
    } catch (Exception e) {
      logger.error("查询论文推荐列表 异常", e);
    }
  }

  @Override
  public Map<String, Object> getPaperLeftMenu(PubListVO pubListVO) {
    PubListResult result = pubQueryhandlerService.queryPub(pubListVO.getPubQueryDTO());
    return result.getResultData();
  }

  @SuppressWarnings("unchecked")
  @Override
  public void getPatents(PubListVO pubListVO) {
    try {
      PubListResult result = pubQueryhandlerService.queryPub(pubListVO.getPubQueryDTO());
      if (result.status.equals(V8pubConst.SUCCESS)) {
        pubListVO.setResultList(result.resultList);
        pubListVO.setTotalCount(result.totalCount);
        if (PubQueryDTO.SEQ_2.equals(pubListVO.getPubQueryDTO().getSeqQuery())) {// 要构建左侧状态菜单栏
          Map<String, Object> resultData = result.getResultData();
          pubListVO.setYearMap((Map<Long, Long>) resultData.get("pubYear"));
          pubListVO.setPubTypeMap((Map<Long, Long>) resultData.get("pubType"));
          pubListVO.setLanguageMap((Map<String, Long>) resultData.get("languageType"));
          pubListVO.setSearchMenu(true);
        }
      }
    } catch (Exception e) {
      logger.error("查询专利推荐列表 异常", e);
    }
  }

  @Override
  public Map<String, Object> getPatentLeftMenu(PubListVO pubListVO) {
    PubListResult result = pubQueryhandlerService.queryPub(pubListVO.getPubQueryDTO());
    return result.getResultData();
  }

}
