package com.smate.center.batch.dao.rol.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.pub.PubAssignCnkiPatDept;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * CnkiPat成果拆分XML数据DAO.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PubAssignCnkiPatDeptDao extends RolHibernateDao<PubAssignCnkiPatDept, Long> {

  /**
   * 获取指定序号、机构ID的成果部门.
   * 
   * @param seqNos
   * @param pubId
   * @param insId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Object[]> getInsPubDept(List<Integer> seqNos, Long pubId, Long insId) {

    String hql =
        "select seqNo,deptName from PubAssignCnkiPatDept where pubId = :pubId and insId = :insId and seqNo in(:seqNos)";
    return super.createQuery(hql).setParameter("pubId", pubId).setParameter("insId", insId)
        .setParameterList("seqNos", seqNos).list();
  }
}
