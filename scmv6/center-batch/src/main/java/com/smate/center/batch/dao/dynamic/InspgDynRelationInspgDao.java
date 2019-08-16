package com.smate.center.batch.dao.dynamic;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.dynamic.InspgDynRelationInspg;
import com.smate.core.base.utils.data.BaseMongoDAO;
import com.smate.core.base.utils.exception.DynTaskException;

/**
 * 
 * @author hzr
 *
 */
@Repository
public class InspgDynRelationInspgDao extends BaseMongoDAO<InspgDynRelationInspg> {

  /**
   * 
   * 保存实体
   * 
   * @parameter InspgDynamicLinkShare
   */
  public void saveDyn(InspgDynRelationInspg inspgDynRelationInspg) throws DynTaskException {

    super.save(inspgDynRelationInspg);
  }

  /**
   * 
   * 根据DynId查找动态实体
   * 
   * @parameter InspgDynRelationInspg
   */
  public InspgDynRelationInspg getByDynId(Long dynId) throws DynTaskException {

    Query myQuery = new Query();
    myQuery.addCriteria(Criteria.where("id").is(dynId).and("status").is(1));
    InspgDynRelationInspg result = super.findOne(myQuery);
    return result;
  }

  /**
   * 
   * 删除动态：将status置0
   * 
   * @parameter InspgDynamicLinkShare
   */
  public void DeleteDyn(InspgDynRelationInspg inspgDynRelationInspg) throws DynTaskException {

    inspgDynRelationInspg.setStatus(0);
    super.save(inspgDynRelationInspg);
  }

}
