package com.smate.center.batch.dao.pdwh.pub;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.BaseMongoDAO;
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

  public List<PubPdwhDetailDOM> findPatDetails(Long patId) {
    Query myQuery = new Query();
    myQuery.addCriteria(Criteria.where("pubId").gte(patId).and("pubType").is(5)).limit(100);
    myQuery.with(new Sort(Direction.DESC, "pubId"));
    List<PubPdwhDetailDOM> list = find(myQuery);
    if (list != null && list.size() > 0) {
      return list;
    }
    return null;
  }

  public List<PubPdwhDetailDOM> findPaperDetails(Long patId) {
    Query myQuery = new Query();
    myQuery.addCriteria(Criteria.where("pubId").gte(patId).and("pubType").in(1, 2, 3, 4, 7, 8, 10)).limit(100);
    myQuery.with(new Sort(Direction.DESC, "pubId"));
    List<PubPdwhDetailDOM> list = find(myQuery);
    if (list != null && list.size() > 0) {
      return list;
    }
    return null;
  }

}
