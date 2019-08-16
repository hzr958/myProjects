package com.smate.center.batch.dao.dynamic;

import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.dynamic.InspgDynamicLinkShare;
import com.smate.core.base.utils.data.BaseMongoDAO;
import com.smate.core.base.utils.exception.DynTaskException;

/**
 * 
 * @author hzr
 *
 */
@Repository
public class InspgDynamicLinkShareDao extends BaseMongoDAO<InspgDynamicLinkShare> {

  /**
   * 
   * 保存实体
   * 
   * @parameter InspgDynamicLinkShare
   */
  public void saveDyn(InspgDynamicLinkShare inspgDynamicLinkShare) throws DynTaskException {

    super.save(inspgDynamicLinkShare);
  }

  /**
   * 
   * 根据DynId查找动态实体
   * 
   * @parameter InspgDynamicLinkShare
   */
  public List<InspgDynamicLinkShare> getByDynId(Long dynId) throws DynTaskException {

    Query myQuery = new Query();
    myQuery.addCriteria(Criteria.where("id").is(dynId));
    List<InspgDynamicLinkShare> resultList = super.find(myQuery, InspgDynamicLinkShare.class);
    return resultList;
  }

}
