package com.smate.web.psn.service.psnhtml;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.psn.dao.psnhtml.PsnHtmlRefreshDao;
import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.model.psnhtml.PsnHtmlRefresh;


/**
 * 人员Html刷新服务类
 * 
 * @author zk
 * 
 */
@Service("psnHtmlRefreshService")
@Transactional(rollbackFor = Exception.class)
public class PsnHtmlRefreshServiceImpl implements PsnHtmlRefreshService {

  @Autowired
  private PsnHtmlRefreshDao psnHtmlRefreshDao;

  /**
   * 获取需要刷新的人员
   */
  @Override
  public List<PsnHtmlRefresh> getPsnHtmlNeedRefresh(Integer max) throws PsnException {

    return psnHtmlRefreshDao.findNeedRefresh(max);
  }


  private List<Long> getPsnIdList(List<PsnHtmlRefresh> phrList) {

    List<Long> psnIdList = new ArrayList<Long>();
    if (CollectionUtils.isNotEmpty(phrList)) {
      for (PsnHtmlRefresh phr : phrList) {
        psnIdList.add(phr.getPsnId());
      }
    }
    return psnIdList;
  }

  @Override
  public void updatePsnHtmlRefresh(Long psnId) throws PsnException {
    psnHtmlRefreshDao.updateRefresh(psnId, 1);
  }
}
