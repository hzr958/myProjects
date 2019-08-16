package com.smate.center.batch.dao.dynamic;

import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.dynamic.InspgDynamicNewsAdd;
import com.smate.core.base.utils.data.BaseMongoDAO;
import com.smate.core.base.utils.exception.DynTaskException;

/**
 * 
 * @author hzr
 *
 */
@Repository
public class InspgDynamicNewsAddDao extends BaseMongoDAO<InspgDynamicNewsAdd> {

  /**
   * 
   * 保存实体
   * 
   * @parameter InspgDynamicLinkShare
   */
  public void saveDyn(InspgDynamicNewsAdd inspgDynamicNewsAdd) throws DynTaskException {

    super.save(inspgDynamicNewsAdd);
  }

  /**
   * 
   * 根据DynId查找动态实体
   * 
   * @parameter InspgDynamicLinkShare
   */
  public List<InspgDynamicNewsAdd> getByDynId(Long dynId) throws DynTaskException {

    Query myQuery = new Query();
    myQuery.addCriteria(Criteria.where("id").is(dynId));
    List<InspgDynamicNewsAdd> resultList = super.find(myQuery, InspgDynamicNewsAdd.class);
    return resultList;
  }

}
