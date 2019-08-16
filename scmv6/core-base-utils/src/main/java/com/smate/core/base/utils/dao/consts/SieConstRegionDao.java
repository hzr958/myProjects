package com.smate.core.base.utils.dao.consts;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.core.base.utils.model.consts.SieConstRegion;

/**
 * 地区数据层接口.
 * 
 * @author hd
 */
@Repository
public class SieConstRegionDao extends SieHibernateDao<SieConstRegion, Long> {

  /**
   * 通过superRegionId查询国家和区域数据
   * 
   * @param superRegionId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<SieConstRegion> findRegionData(Long superRegionId) {
    if (superRegionId == null) {
      return super.createQuery("from SieConstRegion t where t.superRegionId is null").list();

    } else {
      return super.createQuery("from SieConstRegion t where t.superRegionId=?", superRegionId).list();
    }
  }

  @SuppressWarnings("unchecked")
  public List<SieConstRegion> getRegionByName(String name) {
    String hql = "from SieConstRegion t where t.zhName = ? or t.enName = ?";
    List<Object> params = new ArrayList<Object>();
    params.add(name);
    params.add(name);
    return super.createQuery(hql, params.toArray()).list();
  }

  /**
   * 通过superRegionId中国区域数据
   * 
   * @param superRegionId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<SieConstRegion> findCnData(Long superRegionId) {
    if (superRegionId == null) {
      return super.createQuery("from SieConstRegion t where t.id=156").list();

    } else {
      return super.createQuery("from SieConstRegion t where t.superRegionId=?", superRegionId).list();
    }
  }

  /**
   * 获取单个数据.
   * 
   * @param name
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public SieConstRegion getConstRegionByName(String name) {

    name = name.trim().toLowerCase();

    Query q =
        super.createQuery("from SieConstRegion cr where lower(cr.zhName) = lower(?) or lower(cr.enName)=lower(?) ",
            new Object[] {name, name});
    List<SieConstRegion> rets = q.list();
    if (rets.size() > 0) {
      return rets.get(0);
    }

    return null;
  }

  @SuppressWarnings("unchecked")
  public List<SieConstRegion> getCityByZhName(String name) {
    List<Object> params = new ArrayList<Object>();
    String hql =
        "from SieConstRegion cr where lower(cr.zhName) = lower(?) and cr.superRegionId <> 156 and cr.superRegionId is not null";
    params.add(name);
    List<SieConstRegion> list = super.createQuery(hql, params.toArray()).list();
    return list;
  }

  /**
   * 根据 name 获取地区常量记录， name是英文
   * 
   * @param name
   * @return
   */
  public List<SieConstRegion> getCityByEnName(String name) {
    List<Object> params = new ArrayList<Object>();
    String hql =
        "from SieConstRegion cr where lower(cr.enName) = lower(?) and ((cr.superRegionId <> 156 ) or (cr.id in (:blist))) and cr.superRegionId is not null";
    Query query = super.getSession().createQuery(hql);
    query.setString(0, name);
    query.setParameterList("blist", excludeRegionIds().toArray());
    List<SieConstRegion> list = query.list();
    return list;
  }

  /**
   * 根据参数获取 地区常量记录
   * 
   * @param queryParams 处理过的参数
   * @return
   */
  public List<SieConstRegion> getCityByZhNames(List<String> queryParams) {
    String hql =
        "from SieConstRegion cr where cr.zhName IN (:alist) and ((cr.superRegionId <> 156 ) or (cr.id in (:blist))) and cr.superRegionId is not null";
    Query query = super.getSession().createQuery(hql);
    query.setParameterList("alist", queryParams.toArray());
    query.setParameterList("blist", excludeRegionIds().toArray());
    List<SieConstRegion> list = query.list();
    return list;
  }


  public static List<Long> excludeRegionIds() {
    List<Long> list = new ArrayList<Long>();
    list.add(820000L);// 澳门
    list.add(710000L);// 台湾
    list.add(810000L);// 香港
    list.add(110000L);// 北京
    list.add(120000L);// 天津
    list.add(310000L);// 上海
    list.add(500000L);// 重庆
    return list;
  }

  /**
   * 获取单个数据.
   * 
   * @author ztg
   * @param id
   * @return
   * @throws DaoException
   */
  public SieConstRegion getConstRegionById(Long id) {

    return super.findUniqueBy("id", id);
  }

  /**
   * 获取中国所有省市名//省排在前面
   * 
   * @return
   * @author ztg
   * @date 2018-12-28
   */
  public List<SieConstRegion> getAllCNZhname() {
    /**
     * SELECT COUNT(t.region_id) FROM sie2.const_region t WHERE t.super_region_code IN ( SELECT
     * q.region_code FROM sie2.const_region q WHERE q.super_region_code = 'CN' OR q.region_code = 'CN' )
     */
    String hql =
        "select new SieConstRegion(id, zhName, enName, superRegionId) from SieConstRegion cr where cr.superRegionCode IN "
            + "( select scr.regionCode from SieConstRegion scr where scr.superRegionCode = 'CN' OR scr.regionCode = 'CN' )"
            + " order by superRegionId asc";
    return super.createQuery(hql).list();

  }

  /**
   * 获取所有数据
   * 
   * @return
   * @author ztg
   * @date 2018-12-28
   */
  public List<SieConstRegion> getAllName() {
    String hql = "from SieConstRegion";
    return super.createQuery(hql).list();

  }

}
