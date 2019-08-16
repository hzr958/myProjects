package com.smate.web.v8pub.dao.sns;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.BaseMongoDAO;
import com.smate.web.v8pub.po.sns.PubModifyHistory;

/**
 * 个人库成果修改历史记录
 * 
 * @author yhx
 *
 */
@Repository
public class PubModifyHistoryDao extends BaseMongoDAO<PubModifyHistory> {

  public PubModifyHistory findListByPubIdAndPsnId(Long pubId, Long psnId) {
    Query query = new Query();
    query.addCriteria(Criteria.where("pubId").is(pubId).and("psnId").is(psnId));
    query.with(new Sort(Direction.DESC, "id"));
    List<PubModifyHistory> list = super.find(query);
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

}
