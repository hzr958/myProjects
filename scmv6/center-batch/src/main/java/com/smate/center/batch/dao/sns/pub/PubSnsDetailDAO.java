package com.smate.center.batch.dao.sns.pub;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.BaseMongoDAO;
import com.smate.web.v8pub.dom.sns.PubSnsDetailDOM;

/**
 * 个人库成果详情DAO
 * 
 * @author tsz
 */
@Repository
public class PubSnsDetailDAO extends BaseMongoDAO<PubSnsDetailDOM> {

  public PubSnsDetailDOM findByPubId(Long pubId) {
    Query myQuery = new Query();
    myQuery.addCriteria(Criteria.where("pubId").is(pubId));
    List<PubSnsDetailDOM> list = find(myQuery);
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  public List<PubSnsDetailDOM> findPubSnsDetails(Long lastSnsPubId) {
    Query myQuery = new Query();
    myQuery.addCriteria(Criteria.where("pubId").gte(lastSnsPubId)).limit(2000);
    myQuery.with(new Sort(Direction.DESC, "pubId"));
    List<PubSnsDetailDOM> list = find(myQuery);
    if (list != null && list.size() > 0) {
      return list;
    }
    return null;

  }

}
