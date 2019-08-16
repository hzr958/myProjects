package com.smate.center.batch.dao.rol.pub;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.rol.pub.KpiCityInf;
import com.smate.core.base.utils.data.RolHibernateDao;


/**
 * 市级地区统计表.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class KpiCityInfDao extends RolHibernateDao<KpiCityInf, Long> {

  public List<KpiCityInf> getAllKpiCityInf() throws DaoException {
    String sql = "from KpiCityInf t,ConstCnCity t1 where t.cyId=t1.cyId order by t.pubNum desc";
    return super.find(sql);
  }

  public List<KpiCityInf> getDefaultFiveCys() throws DaoException {
    String sql = "from KpiCityInf t,ConstCnCity t1 where t.cyId=t1.cyId order by t.pubNum desc";
    List<KpiCityInf> list = super.find(sql);
    return list != null && list.size() > 5 ? list.subList(0, 5) : list;
  }

  public List<KpiCityInf> getUserSettinCys(String prvIdsStr) throws DaoException {
    String sql = "from KpiCityInf t,ConstCnCity t1 where t.cyId=t1.cyId and t.cyId in(?) order by t.pubNum desc";
    return super.find(sql, new Object[] {prvIdsStr});
  }

  @SuppressWarnings("unchecked")
  public List getConstProvince() {
    return super.queryForList(
        "select t.PRV_ID,t.ZH_NAME,t.EN_NAME ,t.TYPE from const_cn_province t order by t.zh_seq asc");
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
        "select t.PRV_ID,t.ZH_NAME,t.EN_NAME from const_cn_province t where t.prv_id=? order by t.zh_seq asc",
        new Object[] {id});
  }

  @SuppressWarnings("unchecked")
  public List getConstCitysAll(String name) {
    return super.queryForList("select t.cy_id,t.zh_name,t.en_name from const_cn_city t where (t.zh_name like '%" + name
        + "%' or t.en_name like '%" + name + "%')  order by t.zh_seq asc");
  }

  @SuppressWarnings("unchecked")
  public List getConstCitys(String prvId) {
    return super.queryForList(
        "select t.cy_id,t.zh_name,t.en_name from const_cn_city t where t.prv_id=? order by t.zh_seq asc",
        new Object[] {prvId});
  }

  public List getConstDistrictsByPrvId(String prvId) {
    return super.queryForList(
        "select t.dis_id,t.zh_name,t.en_name from const_cn_district t where t.prv_id=? order by t.zh_seq asc",
        new Object[] {prvId});
  }

  public List getConstDistrictsByCyId(String cyId) {
    return super.queryForList(
        "select t.dis_id,t.zh_name,t.en_name from const_cn_district t where t.cy_id=? order by t.zh_seq asc",
        new Object[] {cyId});
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
