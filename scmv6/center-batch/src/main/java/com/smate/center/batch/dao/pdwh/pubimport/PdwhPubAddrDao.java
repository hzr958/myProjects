package com.smate.center.batch.dao.pdwh.pubimport;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pubimport.PdwhPubAddr;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 基准库成果地址dao
 * 
 * @author zjh
 *
 */
@Repository
public class PdwhPubAddrDao extends PdwhHibernateDao<PdwhPubAddr, Long> {
  public void deletePdwhPubAddr(Long pubId, Integer language) {
    String hql = "delete from PdwhPubAddr t where t.pubId=:pubId and language=:language";
    super.createQuery(hql.toString()).setParameter("pubId", pubId).setParameter("language", language).executeUpdate();
  }

  /**
   * 获取成果地址
   * 
   * @param pubId
   * @return
   * @author LIJUN
   * @date 2018年3月19日
   */
  public List<String> getPdwhPubAddrById(Long pubId) {
    String hql = "select t.address from PdwhPubAddr t where t.pubId=:pubId";
    return super.createQuery(hql).setParameter("pubId", pubId).list();
  }

}
