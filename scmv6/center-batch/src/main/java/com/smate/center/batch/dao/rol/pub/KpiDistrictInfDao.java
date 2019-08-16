package com.smate.center.batch.dao.rol.pub;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.rol.pub.ConstCnCity;
import com.smate.center.batch.model.rol.pub.ConstCnDistrict;
import com.smate.center.batch.model.rol.pub.KpiDistrictInf;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * 市级管辖区统计表.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class KpiDistrictInfDao extends RolHibernateDao<KpiDistrictInf, Long> {

  public List<KpiDistrictInf> getAllKpiDistrictInf() throws DaoException {
    String sql = "from KpiDistrictInf t,ConstCnDistrict t1 where t.disId=t1.disId order by t.pubNum desc";
    return super.find(sql);
  }

  public List<KpiDistrictInf> getDefaultFiveCys() throws DaoException {
    String sql = "from KpiDistrictInf t,ConstCnDistrict t1 where t.disId=t1.disId order by t.pubNum desc";
    List<KpiDistrictInf> list = super.find(sql);
    return list != null && list.size() > 5 ? list.subList(0, 5) : list;
  }

  public List<KpiDistrictInf> getUserSettinCys(String disIdsStr) throws DaoException {
    String sql =
        "from KpiDistrictInf t,ConstCnDistrict t1 where t.disId=t1.disId and t.disId in(?) order by t.pubNum desc";
    return super.find(sql, new Object[] {disIdsStr});
  }

  @SuppressWarnings("unchecked")
  public List getConstProvince() {
    return super.queryForList(
        "select t.PRV_ID,t.ZH_NAME,t.EN_NAME ,t.TYPE from const_cn_province t order by t.zh_seq asc");
  }

  @SuppressWarnings("unchecked")
  public List getConstProvinceById(Long prvId) {
    return super.queryForList(
        "select t.PRV_ID ID,t.ZH_NAME,t.EN_NAME ,t.TYPE from const_cn_province t where t.PRV_ID=?");
  }

  @SuppressWarnings("unchecked")
  public List getConstProvince(String name) {
    return super.queryForList(
        "select t.PRV_ID,t.ZH_NAME,t.EN_NAME ,t.TYPE from const_cn_province t where (t.ZH_NAME like '%" + name
            + "%' or t.EN_NAME like '%" + name + "%')  order by t.zh_seq asc");
  }

  @SuppressWarnings("unchecked")
  public List getConstProvinceById(String id) {
    return super.queryForList(
        "select t.PRV_ID ID,t.ZH_NAME,t.EN_NAME from const_cn_province t where t.prv_id=? order by t.zh_seq asc",
        new Object[] {id});
  }

  @SuppressWarnings("unchecked")
  public List getConstDistrictById(String id) {
    return super.queryForList(
        "select t.DIS_ID ID, t.CY_ID, T.PRV_ID, t.ZH_NAME,t.EN_NAME from const_cn_district t where t.dis_id=? order by t.zh_seq asc",
        new Object[] {id});
  }

  @SuppressWarnings("unchecked")
  public List getConstCityById(String id) {
    return super.queryForList(
        "select t.CY_ID ID, T.PRV_ID, t.ZH_NAME,t.EN_NAME from const_cn_city t where t.cy_id=? order by t.zh_seq asc",
        new Object[] {id});
  }

  public ConstCnCity queryConstCityByCityId(Long cityId) {
    return (ConstCnCity) super.getSession().get(ConstCnCity.class, cityId);
  }

  public ConstCnDistrict queryConstDistrictByDisId(Long disId) {
    return (ConstCnDistrict) super.getSession().get(ConstCnDistrict.class, disId);
  }

  @SuppressWarnings("unchecked")
  public List getConstCitysAll(String name) {
    return super.queryForList("select t.cy_id,t.zh_name,t.en_name from const_cn_city t where (t.zh_name like '%" + name
        + "%' or t.en_name like '%" + name + "%')  order by t.zh_seq asc");
  }

  @SuppressWarnings("unchecked")
  public List getConstCitys(String prvId) {
    return super.queryForList(
        "select t.cy_id ID,t.zh_name,t.en_name from const_cn_city t where t.prv_id=? order by t.zh_seq asc",
        new Object[] {prvId});
  }

  public List getConstDistrictsByCyId(String cyId) {
    return super.queryForList(
        "select t.DIS_ID, t.CY_ID, T.PRV_ID, t.ZH_NAME,t.EN_NAME from const_cn_district t where t.CY_ID=? order by t.zh_seq asc",
        new Object[] {cyId});
  }

  public List getConstDistrictsByPrvId(String prvId) {
    return super.queryForList(
        "select t.DIS_ID, t.CY_ID, T.PRV_ID, t.ZH_NAME,t.EN_NAME from const_cn_district t where t.PRV_ID=? order by t.zh_seq asc",
        new Object[] {prvId});
  }

  @SuppressWarnings("unchecked")
  public List getConstInsList(String cityId) {
    String sql =
        "select t.ins_id,t.zh_name,t.en_name from institution t,ins_region t2 where t.ins_id=t2.ins_id and (t2.cy_id=? or t2.prv_id=?) order by t.en_name asc";
    return super.queryForList(sql, new Object[] {cityId, cityId});
  }

  @SuppressWarnings("unchecked")
  public List getConstInsListAll(String name) {
    String sql = "select t.ins_id,t.zh_name,t.en_name from institution t where (t.zh_name like '%" + name
        + "%' or t.en_name like '%" + name + "%' ) order by t.en_name asc";
    return super.queryForList(sql);
  }

  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> getConstInsName(String insId) {
    String sql = "select t.ins_id,t.zh_name,t.en_name from institution t where t.ins_id=?";
    return super.queryForList(sql, new Object[] {insId});
  }

  @SuppressWarnings("unchecked")
  public List getConstUnitList(String insId) {
    String sql = "select t.unit_id,t.zh_name,t.en_name from ins_unit t where t.ins_id=? order by t.en_name asc";
    return super.queryForList(sql, new Object[] {insId});
  }

  @SuppressWarnings("unchecked")
  public List getConstUnitListAll(String name, Long insId) {
    String sql = "select t.unit_id,t.zh_name,t.en_name from ins_unit t where t.ins_id=" + insId
        + " and (t.zh_name like '%" + name + "%' or t.en_name like '%" + name + "%' )";
    return super.queryForList(sql);
  }

}
