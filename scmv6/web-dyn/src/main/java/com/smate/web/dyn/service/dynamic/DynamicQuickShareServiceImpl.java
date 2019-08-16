package com.smate.web.dyn.service.dynamic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.smate.core.base.dyn.dao.DynamicMsgDao;
import com.smate.core.base.utils.exception.DynException;
import com.smate.web.dyn.dao.dynamic.DynStatisticsDao;
import com.smate.web.dyn.dao.dynamic.DynamicShareResDao;
import com.smate.web.dyn.form.dynamic.DynamicForm;
import com.smate.web.dyn.model.dynamic.DynStatistics;

/**
 * 快速分享
 * 
 * @zx
 */
@Service("dynamicQuickShareService")
@Transactional(rollbackFor = Exception.class)
public class DynamicQuickShareServiceImpl implements DynamicQuickShareService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private DynStatisticsDao dynStatisticsDao;
  @Autowired
  private DynamicRealtimeService dynamicRealtimeService;
  @Autowired
  private DynamicShareService dynamicShareService;
  @Autowired
  private DynamicShareResDao dynamicShareResDao;
  @Autowired
  private DynamicMsgDao dynamicMsgDao;

  @Override
  public void quickShare(DynamicForm form) throws DynException {
    try {
      // 处理分享内容的转义问题
      String dynText = HtmlUtils.htmlUnescape(form.getDynText());
      form.setDynText(dynText);
      dynamicRealtimeService.dynamicRealtime(form);
      dynamicShareService.shareDynamic(form);
      if (form.getParentDynId() != null) {
        DynStatistics dynStatistics = dynStatisticsDao.get(form.getParentDynId());
        if (dynStatistics == null) {
          dynStatistics = new DynStatistics();
          dynStatistics.setAwardCount(0);
          dynStatistics.setCommentCount(0);
          dynStatistics.setShareCount(0);
        }
        dynStatistics.setShareCount(dynStatistics.getShareCount() + 1);
        dynStatisticsDao.save(dynStatistics);
      }

    } catch (Exception e) {
      throw new DynException(e);
    }
  }

}
