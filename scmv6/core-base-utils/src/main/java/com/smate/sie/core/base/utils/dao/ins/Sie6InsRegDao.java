package com.smate.sie.core.base.utils.dao.ins;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.ins.Sie6InsReg;

/**
 * 单位注册Dao
 * 
 * @author hd
 *
 */
@Repository
public class Sie6InsRegDao extends SieHibernateDao<Sie6InsReg, Long> {

  /**
   * 获取指定insId 的注册信息
   * 
   * @param insId
   * @return
   */
  public Sie6InsReg getByInsId(Long insId) {
    String hql = "from Sie6InsReg where insId = ? ";
    Query query = super.createQuery(hql, insId);
    return (Sie6InsReg) query.uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<Sie6InsReg> findInsRegByInsName(String insName) {
    String status = "R";// 注册待审核
    String hql = "from Sie6InsReg t where trim(t.insName)=trim(?) and t.status =?";
    Query query = this.createQuery(hql, insName, status);
    return query.list();
  }


}
