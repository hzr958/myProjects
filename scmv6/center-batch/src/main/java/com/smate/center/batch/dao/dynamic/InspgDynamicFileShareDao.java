package com.smate.center.batch.dao.dynamic;

import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.dynamic.InspgDynamicFileShare;
import com.smate.core.base.utils.data.BaseMongoDAO;
import com.smate.core.base.utils.exception.DynTaskException;

/**
 * 
 * @author hzr
 *
 */
@Repository
public class InspgDynamicFileShareDao extends BaseMongoDAO<InspgDynamicFileShare> {

  /**
   * 
   * 保存实体
   * 
   * @parameter InspgDynamicFileShare
   */
  public void saveDyn(InspgDynamicFileShare inspgDynamicFileShare) throws DynTaskException {

    super.save(inspgDynamicFileShare);
  }

  /**
   * 
   * 根据DynId查找动态实体
   * 
   * @parameter InspgDynamicFileShare
   */
  public List<InspgDynamicFileShare> getByDynId(Long dynId) throws DynTaskException {

    Query myQuery = new Query();
    myQuery.addCriteria(Criteria.where("id").is(dynId));
    List<InspgDynamicFileShare> resultList = super.find(myQuery);
    return resultList;
  }

}
