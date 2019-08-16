package com.smate.center.batch.dao.rol.pub;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.InstitutionRol;
import com.smate.core.base.utils.data.RolHibernateDao;
import com.smate.core.base.utils.model.InsPortal;
import com.smate.core.base.utils.string.ServiceUtil;



/**
 * 单位数据层接口.
 * 
 * @author new
 * 
 */
@Repository
public class InstitutionRolDao extends RolHibernateDao<InstitutionRol, Long> {

  /**
   * 根据单位名称取得单位.
   * 
   * @param name
   * @return
   * @throws DaoException
   */
  public InstitutionRol findByName(String name) throws DaoException {
    String hql = "from InstitutionRol where enName = ? or zhName = ? ";
    List<InstitutionRol> list = super.find(hql, name, name);
    if (list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  /**
   * 通过单位编号取得单位实体.
   * 
   * @param id
   * @return
   * @throws DaoException
   */
  public InstitutionRol findById(Long id) throws DaoException {

    return super.findUniqueBy("id", id);
  }

  /**
   * 保存单位信息.
   * 
   * @param ins
   * @throws DaoException
   */
  public void saveInstitutionRol(InstitutionRol ins) throws DaoException {
    super.save(ins);
  }

  /**
   * 根据单位Id取得单位所在国家地区编码.
   * 
   * @param ins
   * @throws DaoException
   */
  public Long getInsRegionId(Long insId) throws ServiceException {

    return super.findUnique("select regionId from InstitutionRol where id=? ", insId);
  }

  /**
   * 通过单位名获取单位Id.
   * 
   * @param zhName
   * @param enName
   * @return Long
   * @throws DaoException
   */
  public Long findInsIdByName(String zhName, String enName) throws DaoException {

    return super.findUnique("select id from InstitutionRol where zhName=? or enName=?", zhName, enName);

  }

  /**
   * 更新单位信息.
   * 
   * @param ins
   * @throws DaoException
   */
  public void updateInstitutionRol(InstitutionRol ins) throws DaoException {
    super.getSession().update(ins);
  }

  /**
   * 通过单位名获取单位实体.
   * 
   * @param zhName
   * @param enName
   * @return Long
   * @throws DaoException
   */
  public InstitutionRol findByName(String zhName, String enName) throws DaoException {

    return super.findUnique("from InstitutionRol where zhName=? or enName=?", zhName, enName);

  }

  public Long getNextInsVal() throws DaoException {

    return Long.valueOf(
        super.getSession().createSQLQuery("select seq_institution.nextval from dual").uniqueResult().toString());

  }

  public List<InstitutionRol> getAllInstitutionByPrvId(Long prvId) {
    String hql = "select t from InstitutionRol t ,InsRegion t1 where t.id=t1.insId and t1.prvId=? order by t.id";
    return super.createQuery(hql, prvId).list();
  }

  public List<InstitutionRol> getAllInstitutionByCyId(Long cyId) {
    String hql = "select t from InstitutionRol t ,InsRegion t1 where t.id=t1.insId and t1.cyId=? order by t.id";
    return super.createQuery(hql, cyId).list();
  }

  public List<InstitutionRol> getAllInstitutionByDisId(Long disId) {
    String hql = "select t from InstitutionRol t ,InsRegion t1 where t.id=t1.insId and t1.disId=? order by t.id";
    return super.createQuery(hql, disId).list();
  }

  public List<InstitutionRol> getInsByIds(List<Long> ids) {
    String hql = "from InstitutionRol t where t.id in(:insIds)";
    return super.createQuery(hql).setParameterList("insIds", ids).list();
  }

  /**
   * 完全匹配.
   * 
   * @param name
   * @return
   */
  public InstitutionRol queryByName(String name) {
    boolean isChinese = !StringUtils.isAsciiPrintable(name);
    String hql = null;
    if (isChinese) {
      hql = "from InstitutionRol t where t.zhName=?";
    } else {
      hql = "from InstitutionRol t where t.enName=?";
    }
    return super.findUnique(hql, new Object[] {name});
  }

  /**
   * 批量查询单位，顺序安装ID的顺序排序.
   * 
   * @param prvIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<InstitutionRol> getOrderByIds(List<Long> ids) {

    String hql = "from InstitutionRol t where t.id in(:ids)";
    List<InstitutionRol> list = super.createQuery(hql).setParameterList("ids", ids).list();
    List<InstitutionRol> orderList = new ArrayList<InstitutionRol>();
    for (Long id : ids) {
      for (int i = 0; i < list.size(); i++) {
        InstitutionRol ins = list.get(i);
        if (ins.getId().equals(id)) {
          orderList.add(ins);
          list.remove(i);
          break;
        }
      }
    }
    return orderList;
  }

  /**
   * 获取智能匹配单位列表，只读size条记录.
   * 
   * @param startWith
   * @return
   * @throws Exception
   */

  @SuppressWarnings({"unchecked", "rawtypes"})
  public List<InstitutionRol> getAcInstitution(String startWith, String excludes, Integer status, int size) {

    boolean isChinese = !StringUtils.isAsciiPrintable(startWith);
    startWith = startWith.trim().toLowerCase();
    StringBuilder sb = new StringBuilder();
    Map<String, Object> param = new HashMap<String, Object>();
    // 判断是否是非英文
    if (isChinese) {
      sb.append(
          "select new InstitutionRol(t.id, t.zhName, t1.domain) from InstitutionRol t,InsPortal t1 where t.id = t1.insId and lower(t.zhName) like :namelk ");
    } else {
      sb.append(
          "select new InstitutionRol(t.id, t.enName, t1.domain) from InstitutionRol t,InsPortal t1 where t.id = t1.insId  and lower(t.enName) like :namelk ");
    }
    param.put("namelk", "%" + startWith + "%");
    List<Long> excIds = ServiceUtil.splitStrToLong(excludes);
    if (CollectionUtils.isNotEmpty(excIds)) {
      sb.append(" and t.id not in(:excIds)");
      param.put("excIds", excIds);
    }
    if (status != null) {
      sb.append("and status = :status ");
      param.put("status", status);
    }
    // 开头的排前面
    if (isChinese) {
      sb.append(" order by instr(lower(t.zhName),:orderName) asc,t.id asc ");
    } else {
      sb.append(" order by instr(lower(t.enName),:orderName) asc,t.id asc ");
    }
    param.put("orderName", startWith.trim().toLowerCase());
    Query query = super.createQuery(sb.toString());
    Iterator<String> keyIter = param.keySet().iterator();
    while (keyIter.hasNext()) {
      String key = keyIter.next();
      Object value = param.get(key);
      if (value instanceof Collection) {
        query.setParameterList(key, (Collection) value);
      } else {
        query.setParameter(key, value);
      }
    }
    query.setMaxResults(size);
    List<InstitutionRol> list = query.list();

    return list;
  }

  /**
   * 获取加入科研在线的单位域名信息.
   * 
   * @param insName
   * @return
   */
  @SuppressWarnings("unchecked")
  public InsPortal getJoinInsPortal(String insName) {

    insName = insName.trim().toLowerCase();
    String hql =
        "select t1 from InstitutionRol t,InsPortal t1 where t.id = t1.insId and (lower(t.zhName) = ? or lower(t.enName) = ?) and t.status = 2 ";
    List<InsPortal> list = super.createQuery(hql, insName, insName).list();
    if (list.size() > 0) {
      return list.get(0);
    }

    return null;
  }

  /**
   * 查询省份单位列表.
   * 
   * @param prvId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<InstitutionRol> queryPrvInsList(Long prvId) {
    String hql =
        "select new InstitutionRol(t.id, t.zhName, t.enName, nvl(t.zhName, t.enName), t.contactPerson, t.serverEmail, t.serverTel,t2.domain) from InstitutionRol t,InsRegion t1,InsPortal t2,InsStatus t3 where t.id = t3.insId and t.id = t1.insId and t.id = t2.insId  and t1.prvId = ? and t.status = 2 ";
    return super.createQuery(hql, prvId).list();
  }

  /**
   * 查找指定名称单位列表.
   * 
   * @param prvId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<InstitutionRol> queryJoinInsList(String queryName) {
    boolean isChinese = !StringUtils.isAsciiPrintable(queryName);
    queryName = queryName.trim().toLowerCase();
    StringBuilder sb = new StringBuilder();
    // 判断是否是非英文
    if (isChinese) {
      sb.append(
          "select new InstitutionRol(t.id, t.zhName, t.enName, t.zhName, t.contactPerson, t.serverEmail, t.serverTel,t2.domain) ");
    } else {
      sb.append(
          "select new InstitutionRol(t.id, t.zhName, t.enName, t.enName, t.contactPerson, t.serverEmail, t.serverTel,t2.domain) ");
    }
    sb.append(
        "from InstitutionRol t,InsPortal t2,InsStatus t3 where t.id = t3.insId and t.id = t2.insId  and t.status = 2 ");
    // 判断是否是非英文
    if (isChinese) {
      sb.append(" and lower(t.zhName) like ? ");
    } else {
      sb.append(" and lower(t.enName) like ? ");
    }
    // 开头的排前面
    if (isChinese) {
      sb.append(" order by instr(lower(t.zhName),?) asc,t.id asc ");
    } else {
      sb.append(" order by instr(lower(t.enName),?) asc,t.id asc ");
    }
    return super.createQuery(sb.toString(), "%" + queryName + "%", queryName).setMaxResults(200).list();
  }

  public InstitutionRol queryByName(String name, Long disId) {
    boolean isChinese = !StringUtils.isAsciiPrintable(name);
    List<Object> params = new ArrayList<Object>();
    String hql = null;
    if (isChinese) {
      hql = "from InstitutionRol t where t.zhName=?";
    } else {
      hql = "from InstitutionRol t where t.enName=?";
    }
    params.add(name);
    if (disId != null) {
      hql = hql
          + "and exists(select 1 from InsRegion ir, ConstCnDistrict dis where t.id=ir.insId and (ir.cyId=dis.cyId and dis.cyId is not null) and ir.prvId=dis.prvId and dis.disId=?)";
      params.add(disId);
    }
    return super.findUnique(hql, params.toArray());
  }
}
