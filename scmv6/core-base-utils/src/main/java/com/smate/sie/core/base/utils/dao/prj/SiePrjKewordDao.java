package com.smate.sie.core.base.utils.dao.prj;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.core.base.utils.model.prj.SiePrjKeword;

/**
 * 关键字DAO.
 * 
 * @author yexingyuan
 */
@Repository
public class SiePrjKewordDao extends SieHibernateDao<SiePrjKeword, Long> {

  @SuppressWarnings("unchecked")
  public List<SiePrjKeword> getListByInsId(Long insId) {
    String hql = " from SiePrjKeword t where t.insId= ? order by t.id ";
    return super.createQuery(hql, insId).list();
  }

  @SuppressWarnings("unchecked")
  public List<SiePrjKeword> getPrjKeyWords(long prjId) {
    String hql = "from SiePrjKeword t where t.prjId = ? ";
    return super.createQuery(hql, prjId).list();
  }

  /**
   * 获取项目关键词列表
   * 
   * @param insId
   * @param prjId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<SiePrjKeword> getPrjKeyWords(Long insId, Long prjId) {
    String hql = "from SiePrjKeword t where t.insId = ? and t.prjId = ?";
    return super.createQuery(hql, insId, prjId).list();
  }
}
