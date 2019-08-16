package com.smate.center.batch.dao.pdwh.pub.cnki;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.cnki.CnkiInsNameReg;
import com.smate.core.base.utils.data.PdwhHibernateDao;


/**
 * 单位CNKI别名.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class CnkiInsNameRegDao extends PdwhHibernateDao<CnkiInsNameReg, Long> {

  /**
   * 查询指定单位ID的cnki别名.
   * 
   * @param insId
   * @return
   */
  public List<CnkiInsNameReg> getCnkiInsNameReg(Long insId) {

    String hql = "from CnkiInsNameReg where insId = ? ";
    return super.find(hql, insId);
  }
}
