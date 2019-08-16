package com.smate.center.open.dao.union.his;

import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.open.exception.OpenException;
import com.smate.center.open.model.union.his.OpenGroupUnionHis;
import com.smate.core.base.utils.data.SnsHibernateDao;


/**
 * 互联互通 关联群组 历史表操作
 * 
 * @author zjh
 *
 */
@Repository
public class OpenGroupUnionHisDao extends SnsHibernateDao<OpenGroupUnionHis, Long> {

  @SuppressWarnings("unchecked")
  public List<OpenGroupUnionHis> getNeedReUnionHisList(Long ownerOpenId, String token, String delType)
      throws OpenException {

    String hql =
        "from OpenGroupUnionHis t where t.ownerOpenId=:ownerOpenId and t.token=:token and t.status=0 and t.delType=:delType";

    return super.createQuery(hql).setParameter("ownerOpenId", ownerOpenId).setParameter("token", token)
        .setParameter("delType", delType).list();
  }

  @SuppressWarnings("unchecked")
  public List<OpenGroupUnionHis> getNeedDelUnionHisList(String token, String delType) throws OpenException {

    String hql = "from OpenGroupUnionHis t where t.token=:token and t.status=0 and t.delType=:delType";

    return super.createQuery(hql).setParameter("token", token).setParameter("delType", delType).setMaxResults(50)
        .list();
  }

  /**
   * 通过groupCode查找 id
   * 
   * @param psnId
   * @return
   */
  public Long findIdByOwnPsnIdAndGroupCode(String groupCode) {
    String hql = "select t.id  from OpenGroupUnionHis t where t.groupCode=:groupCode ";
    Object object = this.createQuery(hql).setParameter("groupCode", groupCode).uniqueResult();
    if (object != null) {
      return NumberUtils.toLong(object.toString());
    }
    return null;

  }
}
