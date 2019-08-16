package com.smate.center.batch.dao.tmp;

import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.tmp.TmpEncodedPubSimpleId;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class TmpEncodedPubSimpleIdDao extends SnsHibernateDao<TmpEncodedPubSimpleId, Long> {
  public void saveOrUpdateList(ArrayList<TmpEncodedPubSimpleId> list) {

  }

}
