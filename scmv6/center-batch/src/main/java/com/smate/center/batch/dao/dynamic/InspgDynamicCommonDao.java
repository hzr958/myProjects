package com.smate.center.batch.dao.dynamic;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.exception.DynTaskException;


/**
 * 动态系统公共访问方法类 <MongoDB表:inspg_dynamic>
 * 
 * @author lxz
 * @since 6.0.1-snapshot
 * @version 6.0.1-snapshot
 */
@Repository
public class InspgDynamicCommonDao {

  @Autowired
  private MongoTemplate mongoTemplate;

  /**
   * 
   * 根据DynId查找动态实体
   * 
   * @parameter InspgDynamicFileShare
   */
  @SuppressWarnings("rawtypes")
  public List<Map> getByDynId(Long dynId) throws DynTaskException {
    Query myQuery = new Query();
    myQuery.addCriteria(Criteria.where("dynId").is(dynId));
    List<Map> resultList = mongoTemplate.find(myQuery, Map.class, "InspgDynamic");
    return resultList;
  }


}
