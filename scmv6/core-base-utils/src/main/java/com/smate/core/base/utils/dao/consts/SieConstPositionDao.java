package com.smate.core.base.utils.dao.consts;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.core.base.utils.model.consts.SieConstPosition;

/**
 * 职务常量
 * 
 * @author hd
 *
 */
@Repository
public class SieConstPositionDao extends SieHibernateDao<SieConstPosition, Long> {

  /**
   * 获取自动匹配的职务列表.
   * 
   * @param pos
   * @param size
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<SieConstPosition> getPosLike(String pos) {
    pos = pos.toLowerCase();
    List<Object> params = new ArrayList<Object>();
    String hql =
        "from SieConstPosition t where  lower(t.enName) like ? or lower(t.zhName) like ? and t.superId is not null order by t.id";
    params.add("%" + pos + "%");
    params.add("%" + pos + "%");
    List<SieConstPosition> list = super.createQuery(hql, params.toArray()).list();
    return list;
  }

  /**
   * 通过名称查找职称等级.
   * 
   * @param pos
   * @param size
   * @return
   */
  @SuppressWarnings("unchecked")
  public SieConstPosition getPosByName(String name) {
    if (StringUtils.isBlank(name)) {
      return null;
    }
    List<Object> params = new ArrayList<Object>();
    params.add(name.toLowerCase());
    params.add(name.toLowerCase());
    String hql = "from SieConstPosition t where lower(t.enName) = ? or lower(t.zhName) = ? ";
    List<SieConstPosition> list = super.createQuery(hql, params.toArray()).list();
    if (list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  /**
   * 获取所有一级职称
   * 
   * @return
   */
  public List<SieConstPosition> getAllFirstPos() {
    String hql = "from SieConstPosition t where t.superId is null ";
    return super.createQuery(hql).list();
  }

  /**
   * 获取所有一级职称
   * 
   * @return
   */
  public List<SieConstPosition> getSecondPosByFirst(Long superId) {
    String hql = "from SieConstPosition t where t.superId  = ? order by t.id ";
    return super.createQuery(hql, superId).list();
  }


  /**
   * 获取指定职务ID的等级.
   * 
   * @param id
   * @return
   */
  public String getPosGrades(Long id) {

    String hql = "select posGrades from SieConstPosition t where t.id = ?";
    return super.findUnique(hql, id);
  }
}
