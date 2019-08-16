package com.smate.center.task.service.journal;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.task.dao.journal.BaseJournalDao;
import com.smate.center.task.dao.journal.BaseJournalTitleDao;
import com.smate.center.task.model.pdwh.pub.BaseJournal;
import com.smate.center.task.model.pdwh.pub.BaseJournalTitle;



@Service("BaseJournalTitleRepairService")
@Transactional(rollbackOn = Exception.class)
public class BaseJournalTitleRepairServiceImpl implements BaseJournalTitleRepairService {

  @Autowired
  private BaseJournalDao baseJournalDao;
  @Autowired
  private BaseJournalTitleDao baseJournalTitleDao;

  /**
   * 查询数据base_journal和base_journal_title表中数据不一致的数据
   */
  @Override
  public List<Map<String, Object>> getNeedRepairJnlIds() {
    return baseJournalDao.findTitlePissnNotSame();
  }

  /**
   * 对指定的期刊id的数据进行数据修复
   */
  @Override
  public void repairData(Long jnlId) {
    BaseJournal baseJournal = baseJournalDao.get(jnlId);
    if (baseJournal == null) {
      return;
    }
    List<BaseJournalTitle> titles = baseJournalTitleDao.findTitleByJnlId(jnlId);
    if (CollectionUtils.isNotEmpty(titles)) {
      BaseJournalTitle newestTitle = titles.get(0);
      if (baseJournalDao.isDuplicateBaseJournal(newestTitle.getTitleEn(), newestTitle.getTitleXx(),
          newestTitle.getPissn())) {// 如果将title表中的数据同步过去，会导致出现违反唯一索引（中英文标题和pissn构成的索引）
        return;
      } else {// 不重复，直接将title表中最新的数据的标题和pissn复制到base_journal表中
        baseJournal.setTitleEn(newestTitle.getTitleEn());
        baseJournal.setTitleXx(newestTitle.getTitleXx());
        baseJournal.setPissn(newestTitle.getPissn());
        baseJournalDao.update(baseJournal);
      }
    }
  }

}
