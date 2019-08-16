package com.smate.center.task.v8pub.dao.sns;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.task.utils.data.BaseMongoDAO;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;

/**
 * 基准库成果详情DAO
 * 
 * @author houchuanjie
 * @date 2018/05/31 16:02
 */
@Repository
public class PubPdwhDetailDAO extends BaseMongoDAO<PubPdwhDetailDOM> {

  public PubPdwhDetailDOM findByObject(Object object) {
    return findById(object);
  }

  public PubPdwhDetailDOM findByPubId(Long pubId) {
    Query myQuery = new Query();
    myQuery.addCriteria(Criteria.where("pubId").is(pubId));
    List<PubPdwhDetailDOM> list = find(myQuery);
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  public List<PubPdwhDetailDOM> findPatDetails(Long patId, Long endPubId) {
    Query myQuery = new Query();
    myQuery.addCriteria(Criteria.where("pubId").gt(patId).lte(endPubId).and("pubType").is(5)).limit(2000);
    myQuery.with(new Sort(Direction.ASC, "pubId"));
    List<PubPdwhDetailDOM> list = find(myQuery);
    if (list != null && list.size() > 0) {
      return list;
    }
    return null;
  }

  public List<PubPdwhDetailDOM> findPaperDetails(Long patId, Long endPubId) {
    Query myQuery = new Query();
    myQuery.addCriteria(Criteria.where("pubId").gt(patId).lte(endPubId).and("pubType").in(1, 2, 3, 4, 7, 8, 10))
        .limit(2000);
    myQuery.with(new Sort(Direction.ASC, "pubId"));
    List<PubPdwhDetailDOM> list = find(myQuery);
    if (list != null && list.size() > 0) {
      return list;
    }
    return null;
  }

  public List<PubPdwhDetailDOM> getPdwhPubIds(Long lastPubId, int batchSize) {
    Query myQuery = new Query();
    myQuery.addCriteria(Criteria.where("pubId").gt(lastPubId).and("srcDbId").in(4, 15, 16, 17)).limit(2000);
    myQuery.with(new Sort(Direction.ASC, "pubId"));
    List<PubPdwhDetailDOM> list = find(myQuery);
    if (list != null && list.size() > 0) {
      return list;
    }
    return null;
  }

}
