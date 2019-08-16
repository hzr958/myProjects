package com.smate.core.base.psn.dao.psncnf;

import java.util.List;
import java.util.Map;

import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.smate.core.base.psn.model.psncnf.PsnConfigContact;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 个人配置：联系方式
 * 
 * @author zhuangyanming
 * 
 */
@Repository
public class PsnConfigContactDao extends SnsHibernateDao<PsnConfigContact, Long> {

  /**
   * 获取人员配置
   * 
   * @param psnIdList
   * @return
   * @throws DaoException
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public List<Map> getPsnConfigContactByPsnIds(List<Long> psnIdList) {
    String hql =
        "select t.anyViewEmail as VIEWEMAIL,t.anyViewTel as VIEWTEL,t.anyViewMobile as VIEWMOBILE,c.psnId as PSNID from PsnConfigContact t,PsnConfig c where t.cnfId=c.cnfId and c.psnId in (:psnIds)";
    return super.createQuery(hql).setParameterList("psnIds", psnIdList)
        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
  }
}
