package com.smate.sie.center.open.dao.dept;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.open.model.dept.ImportThirdPsns;
import com.smate.sie.center.open.model.dept.ImportThirdPsnsPK;

/**
 * 第三方人员信息DAO.
 * 
 * @author xys
 *
 */
@Repository
public class ImportThirdPsnsDao extends SieHibernateDao<ImportThirdPsns, ImportThirdPsnsPK> {

  /**
   * 删除某个单位的第三方人员信息.
   * 
   * @param insId
   * @throws Exception
   */
  public void deleteImportThirdPsnsByInsId(Long insId) throws Exception {
    super.createQuery("delete from ImportThirdPsns t where t.pk.insId=?", insId).executeUpdate();
  }

  /**
   * 通过email查找所在单位的关系数据.
   * 
   * @param email
   * @param insId
   * @return
   * @throws Exception
   */
  public ImportThirdPsns findImportThirdPsns(String email, Long insId) throws Exception {
    String hql = "from ImportThirdPsns where pk.email=? and pk.insId=?";
    return findUnique(hql, email, insId);
  }
}
