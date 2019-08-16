package com.smate.center.batch.dao.dynamic;

import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.dynamic.InspgDynamicMemberAdd;
import com.smate.core.base.utils.data.BaseMongoDAO;
import com.smate.core.base.utils.exception.DynTaskException;

/**
 * 
 * @author hzr
 *
 */
@Repository
public class InspgDynamicMemberAddDao extends BaseMongoDAO<InspgDynamicMemberAdd> {

  /**
   * 
   * 保存实体
   * 
   * @parameter InspgDynamicLinkShare
   */
  public void saveDyn(InspgDynamicMemberAdd inspgDynamicMemberAdd) throws DynTaskException {

    super.save(inspgDynamicMemberAdd);
  }

  /**
   * 
   * 根据DynId查找动态实体
   * 
   * @parameter InspgDynamicLinkShare
   */
  public List<InspgDynamicMemberAdd> getByDynId(Long dynId) throws DynTaskException {

    Query myQuery = new Query();
    myQuery.addCriteria(Criteria.where("id").is(dynId));
    List<InspgDynamicMemberAdd> resultList = super.find(myQuery, InspgDynamicMemberAdd.class);
    return resultList;
  }

}
