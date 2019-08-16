package com.smate.center.batch.dao.rol.pub;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.rol.pub.InsUnit;
import com.smate.core.base.utils.constant.ScmRolRoleConstants;
import com.smate.core.base.utils.data.RolHibernateDao;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 部门管理dao.
 * 
 * @author lichangwen
 * 
 */
@Repository
public class InsUnitDao extends RolHibernateDao<InsUnit, Long> {

  /**
   * 根据单位id和部门id获取单位名
   * 
   * @param insId
   * @param unitId
   * @return
   * @throws DaoException
   */
  public InsUnit getInsUnitName(Long insId, Long unitId) throws DaoException {
    InsUnit insUnit = super.findUnique(
        "select new InsUnit(t.id,t.zhName,t.enName)  from InsUnit t where t.insId = ? and t.id =? ", insId, unitId);
    return insUnit;
  }

  /**
   * 按部门统计成果数
   */
  public Long countPub(Long insId, Long unitId) {
    String hql =
        "select count(t.id)  from PublicationRol t  where t.insId = ?  and t.status = 2   and (  exists (select pubId from PubPsnRol where confirmResult in(0,1) and (unitId = ? or parentUnitId=?) and insId = ? and pubId = t.id ) ) and exists (select pubId from PubPsnRol where confirmResult in(0,1) and (unitId = ? or parentUnitId=?) and pubId = t.id )";
    return findUnique(hql, insId, unitId, unitId, insId, unitId, unitId);
  }

  /**
   * 按部门统计当前年份成果数------publishYear
   * 
   * @param insId
   * @param unitId
   * @return
   */
  public Long countPub(Long insId, Long unitId, Integer year) {
    String hql =
        "select count(t.id)  from PublicationRol t  where t.insId = ?  and t.status = 2  and t.publishYear in(?,?) and (  exists (select pubId from PubPsnRol where confirmResult in(0,1) and (unitId = ? or parentUnitId=?) and insId = ? and pubId = t.id ) ) and exists (select pubId from PubPsnRol where confirmResult in(0,1) and (unitId = ? or parentUnitId=?) and pubId = t.id )";
    return findUnique(hql, insId, year, year + 1, unitId, unitId, insId, unitId, unitId);
  }

  /**
   * 按部门统计专利数
   */
  public Long countPatent(Long insId, Long unitId) {
    String hql =
        "select count(t.id)  from PublicationRol t  where t.typeId = 5 and t.insId = ?  and t.status = 2   and (  exists (select pubId from PubPsnRol where confirmResult in(0,1) and (unitId = ? or parentUnitId=?) and insId = ? and pubId = t.id ) ) and exists (select pubId from PubPsnRol where confirmResult in(0,1) and (unitId = ? or parentUnitId=?) and pubId = t.id )";
    return findUnique(hql, insId, unitId, unitId, insId, unitId, unitId);
  }

  /**
   * 按部门统计当前年份专利数-------publishYear
   * 
   * @param insId
   * @param unitId
   * @return
   */
  public Long countPatent(Long insId, Long unitId, Integer year) {
    String hql =
        "select count(t.id)  from PublicationRol t  where t.typeId = 5 and t.insId = ?  and t.publishYear in(?,?) and t.status = 2   and (  exists (select pubId from PubPsnRol where confirmResult in(0,1) and (unitId = ? or parentUnitId=?) and insId = ? and pubId = t.id ) ) and exists (select pubId from PubPsnRol where confirmResult in(0,1) and (unitId = ? or parentUnitId=?) and pubId = t.id )";
    return findUnique(hql, insId, year, year + 1, unitId, unitId, insId, unitId, unitId);
  }

  /**
   * 按部门统计项目数
   */
  public Long countPrj(Long insId, Long unitId) {
    String hql = "select count(t.id) from RolProject t " + "where t.ownerInsId = ? and t.status = 0  and "
        + "(exists(select t2.id from RolPsnIns t1,RolPrjMember t2 where"
        + " t1.pk.psnId = t2.psnId and t1.pk.insId = ? and t2.insId = ? "
        + "and t2.prjId = t.id and (t1.unitId = ? or t1.superUnitId = ? )) ";
    hql += ")";
    return findUnique(hql, insId, insId, insId, unitId, unitId);
  }

  /**
   * 按部门统计当前年份项目数-----fundingYear
   * 
   * @param insId
   * @param unitId
   * @param year
   * @return
   */
  public Long countPrj(Long insId, Long unitId, Integer year) {
    String hql = "select count(t.id) from RolProject t "
        + "where t.ownerInsId = ? and t.status = 0  and t.fundingYear in(?,?) and "
        + "(exists(select t2.id from RolPsnIns t1,RolPrjMember t2 where"
        + " t1.pk.psnId = t2.psnId and t1.pk.insId = ? and t2.insId = ? "
        + "and t2.prjId = t.id and (t1.unitId = ? or t1.superUnitId = ? )) ";

    if (!(ScmRolRoleConstants.UNIT_RO.equals(SecurityUtils.getCurrentUserRoleId())
        || ScmRolRoleConstants.UNIT_CONTACT.equals(SecurityUtils.getCurrentUserRoleId()))) {
      hql += "or exists (select 1 from RolPrjUnitOwner pu where pu.prjId=t.id and ( pu.unitId=" + unitId
          + " or pu.superUnitId=" + unitId + " ) ) ";
    }
    hql += " ) ";
    return findUnique(hql, insId, year, year + 1, insId, insId, unitId, unitId);
  }

  /**
   * 按部门统计项目数---未登录(ROL-2170领导与秘书角色)
   */

  public Long countPrjLogOut(Long insId, Long unitId) {
    String hql = "select count(t.id) from RolProject t,RolPrjToPublic r where t.id=r.prjId and r.status=? "
        + " and t.status = 0 and t.ownerInsId = ?  and (exists(select t2.id from "
        + "RolPsnIns t1,RolPrjMember t2 where t1.pk.psnId = t2.psnId and t1.pk.insId"
        + "= t2.insId and t2.insId = ? and t2.prjId = t.id and (t1.unitId = ? " + "or t1.superUnitId = ? )) )";
    return findUnique(hql, new Object[] {1, insId, insId, unitId, unitId});
  }

  /**
   * 项目---按全部项目数
   */
  public Long countPrjAll(Long insId) {
    String hql = "select count(t.id) from RolProject t " + "where t.ownerInsId = ? and t.status = 0  ";
    return findUnique(hql, insId);
  }

  /**
   * 项目---按全部项目数----领导与秘书角色
   */
  public Long countPrjAllLeader(Long insId, Long unitId) {
    String hql = "select count(t.id) from RolProject t where"
        + " t.status = 0 and t.ownerInsId = ?  and (exists(select t2.id from "
        + "RolPsnIns t1,RolPrjMember t2 where t1.pk.psnId = t2.psnId and t1.pk.insId"
        + "= t2.insId and t2.insId = ? and t2.prjId = t.id and (t1.unitId = ? "
        + "or t1.superUnitId = ? ))  or exists (select 1 from RolPrjUnitOwner"
        + " pu where pu.prjId=t.id and (pu.unitId=? or pu.superUnitId=?))) ";
    return findUnique(hql, new Object[] {insId, insId, unitId, unitId, unitId, unitId});
  }



  /**
   * 项目---按全部项目数--未登录
   */
  public Long countPrjLogOutAll(Long insId) {
    String hql = "select count(t.id) from RolProject t ,RolPrjToPublic r "
        + "where t.id=r.prjId and r.status=1  and t.ownerInsId = ? and t.status = 0  ";
    return findUnique(hql, insId);
  }

  /**
   * 项目--顶级部门的其他项目数--登陆过 属于顶级部门的 不属于子部门的
   */
  public Long findOtherByUnit(Long insId, Long unitId) {
    String hql = "select count(t.id) from RolProject t " + "where t.ownerInsId = ? and t.status = 0  and "
        + "(exists(select t2.id from RolPsnIns t1,RolPrjMember t2 where"
        + " t1.pk.psnId = t2.psnId and t1.pk.insId = ? and t2.insId = ? " + "and t2.prjId = t.id and (t1.unitId = ?)) ";

    // rol-2321
    hql += " ) ";
    return findUnique(hql, insId, insId, insId, unitId);
  }

  /**
   * 项目--顶级部门的其他项目数--未登陆 属于顶级部门的 不属于子部门的
   */
  public Long findLogOutOtherByUnit(Long insId, Long unitId) {
    String hql = "select count(t.id) from RolProject t,RolPrjToPublic r "
        + "where t.id=r.prjId and r.status=1 and t.ownerInsId = ? and t.status = 0  and "
        + "(exists(select t2.id from RolPsnIns t1,RolPrjMember t2 where"
        + " t1.pk.psnId = t2.psnId and t1.pk.insId = ? and t2.insId = ? " + "and t2.prjId = t.id and (t1.unitId = ? )) "
        + ")";// rol-2321
    return findUnique(hql, insId, insId, insId, unitId);
  }


  /**
   * 项目---按外层其他项目数
   */
  public Long countPrjOther(Long ownerInsId, Long insId) {// rol-2767
    String hql = "select count(t.id) from RolProject t where t.status = 0 "
        + "and t.ownerInsId = ? and exists (select t2.id from RolPsnIns t1, RolPrjMember t2 "
        + " where t1.pk.psnId = t2.psnId and t1.pk.insId = t2.insId and t2.insId = ? and t2.prjId = t.id "
        + "and t1.unitId is null)";
    return findUnique(hql, ownerInsId, insId);
  }

  /**
   * 项目---按外层其他项目数--未登录
   */
  public Long countPrjLogOutOther(Long ownerInsId, Long insId) {// rol-2767
    String hql = "select count(t.id) from RolProject t,RolPrjToPublic r  where "
        + " t.id=r.prjId and r.status=1  and  t.status = 0 "
        + "and t.ownerInsId = ? and exists (select t2.id from RolPsnIns t1, RolPrjMember t2 "
        + " where t1.pk.psnId = t2.psnId and t1.pk.insId = t2.insId and t2.insId = ? and t2.prjId = t.id "
        + "and t1.unitId is null) ";
    return findUnique(hql, ownerInsId, insId);
  }



  /**
   * 按部门统计人员数
   */
  public Long countPsn(Long insId, Long unitId) {
    String hql = "select count(t.pk.psnId) from RolPsnIns t where t.pk.insId= ? and t.status=1"
        + " and t.unitId in (select t1.id from InsUnit t1 where t1.id = ? or t1.superInsUnitId = ? )";

    return findUnique(hql, insId, unitId, unitId);
  }

  /**
   * 通过单位得到所有部门id
   */

  @SuppressWarnings("unchecked")
  public List<Long> getIdsByIns(Long insId) throws DaoException {
    return super.createQuery("select id from InsUnit where insId =?", new Object[] {insId}).list();
  }

  /**
   * 通过部门名称，获取部门ID.
   * 
   * @param unitName
   * @param insId
   * @return
   * @throws DaoException
   */
  public Long getId(String unitName, Long insId) throws DaoException {
    return findUnique("select id from InsUnit where (zhName=? or enName = ?)  and insId=?", unitName, unitName, insId);
  }

  /**
   * 通过部门名称，获取部门.
   * 
   * @param unitName
   * @param insId
   * @return
   * @throws DaoException
   */
  public InsUnit getInsUnit(String unitName, Long insId) throws DaoException {
    return findUnique("from InsUnit where (zhName=? or enName = ?)  and insId=?", unitName, unitName, insId);
  }

  /**
   * 通过部门名称，获取部门.
   * 
   * @param unitName
   * @param insId
   * @param
   * @return
   * @throws DaoException
   */
  public InsUnit getInsUnit(String unitName, Long superInsUnitId, Long insId) throws DaoException {
    return findUnique("from InsUnit where (zhName=? or enName = ?)  and insId=?", unitName, unitName, insId);
  }

  /**
   * 根据unitId查询其的上级部门.
   * 
   * @param unitId
   * @return
   * @throws DaoException
   */
  public Long findUnitSuperId(Long unitId) throws DaoException {
    String hql = "select superInsUnitId from InsUnit where id =?";
    return findUnique(hql, unitId);
  }

  @SuppressWarnings("unchecked")
  public List<Long> findSubUnitId(Long unitId) throws DaoException {
    return super.createQuery("select id from InsUnit where superInsUnitId =?", new Object[] {unitId}).list();
  }

  /**
   * 根据unitId查询其的上级部门.
   * 
   * @param unitId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Long> findUnitById(Long unitId) throws DaoException {
    String hql = "select id from InsUnit where id =? or superInsUnitId=?";
    return createQuery(hql, unitId, unitId).list();
  }

  /**
   * 一个部门下的子部门移动到另一个部门.
   * 
   * @param unitId
   * @param insId
   * @param toUnitId
   * @return
   * @throws DaoException
   */
  public int updateUnit(Long unitId, Long insId, Long toUnitId) throws DaoException {
    String hql =
        "update InsUnit set superInsUnitId=? where id in (select id from InsUnit where insId=? and superInsUnitId=?)";
    return createQuery(hql, toUnitId, insId, unitId).executeUpdate();
  }

  /**
   * 一个子项移动到另一个部门.
   * 
   * @param unitId
   * @param insId
   * @param toUnitId
   * @return
   * @throws DaoException
   */
  public int updateUnitId(Long unitId, Long insId, Long toUnitId) throws DaoException {
    String hql = "update InsUnit set superInsUnitId=? where id =? and insId=?";
    return createQuery(hql, toUnitId, unitId, insId).executeUpdate();
  }

  /**
   * 查询部门匹配列表.
   * 
   * @param name
   * @param insId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<InsUnit> findByNameLike(String name, Long insId, int maxSize) throws DaoException {
    name = name.toLowerCase();
    String hql = "from InsUnit where insId=? and (lower(zhName) like ? or lower(enName) like ? )";
    Query query = createQuery(hql, insId, "%" + name + "%", "%" + name + "%");
    query.setMaxResults(maxSize);
    return query.list();
  }

  /**
   * 查询学院匹配列表.
   * 
   * @param name
   * @param insId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<InsUnit> findByNameLikeSuper(String name, Long insId, int maxSize) throws DaoException {
    name = name.toLowerCase();
    String hql =
        "from InsUnit where  superInsUnitId is null  and  insId=? and (lower(zhName) like ? or lower(enName) like ?  )";
    Query query = createQuery(hql, insId, "%" + name + "%", "%" + name + "%");
    query.setMaxResults(maxSize);
    return query.list();
  }

  /**
   * 通过输入部门名称的语言种类查询部门信息
   * 
   * @param startWith
   * @param insId
   * @param unitId
   * @param maxSize
   * @param isHasEnglish
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<InsUnit> findAcInsUnit(String startWith, Long insId, Long superUnitId, int maxSize) throws DaoException {
    StringBuilder hql = new StringBuilder();
    List<Object> params = new ArrayList<Object>();
    String lowerStartWith = startWith.toLowerCase();
    if (superUnitId != null && superUnitId.longValue() != 0) {
      hql.append("from InsUnit where (superInsUnitId=? and (lower(enName) like ? or lower(zhName) like ?))")
          .append(" or (id=? and (lower(enName) like ? or lower(zhName) like ?))");
      params.add(superUnitId);
      params.add("%" + lowerStartWith + "%");
      params.add("%" + lowerStartWith + "%");
      params.add(superUnitId);
      params.add("%" + lowerStartWith + "%");
      params.add("%" + lowerStartWith + "%");
    } else {
      hql.append("from InsUnit where insId=? and (lower(enName) like ? or lower(zhName) like ?)");
      params.add(insId);
      params.add("%" + lowerStartWith + "%");
      params.add("%" + lowerStartWith + "%");
    }

    return super.createQuery(hql.toString(), params.toArray()).setMaxResults(maxSize).list();
  }

  /**
   * 获取某部门的子部门列表.
   * 
   * @param superId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<InsUnit> getInsUnitBySuperId(Long superId) throws DaoException {

    return super.createQuery("from InsUnit t where  t.superInsUnitId = ? order by t.id", superId).list();
  }

  /**
   * 获取单位部门,获取最顶层，且加载子部门. 含部门
   * 
   * @param insId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<InsUnit> getInsUnitWithSubByInsIdAndUnitId(Long insId, Long unitId) throws DaoException {
    List<InsUnit> superUnits = new ArrayList<InsUnit>();
    StringBuilder hql = new StringBuilder("from InsUnit t where t.insId = ?  ");
    if (unitId != null && unitId != 0L) {
      // 部门 管理原 只可以查看本部门的
      hql.append(" and  id=? ");
      hql.append(" and t.superInsUnitId is null");
      superUnits = super.createQuery(hql.toString(), insId, unitId).list();
    } else {
      hql.append(" and t.superInsUnitId is null");
      hql.append(" order by t.id ");
      superUnits = super.createQuery(hql.toString(), insId).list();
    }

    // 获取子部门.
    if (superUnits != null && superUnits.size() > 0) {
      // 所有子部门
      List<InsUnit> allSubUnits = super.createQuery(
          "from InsUnit t where t.insId = ? and t.superInsUnitId is not null order by t.superInsUnitId,t.id", insId)
              .list();

      for (InsUnit superUnit : superUnits) {
        List<InsUnit> subUnits = null;
        for (int i = 0; i < allSubUnits.size(); i++) {
          InsUnit subUnit = allSubUnits.get(i);
          if (subUnit.getSuperInsUnitId().equals(superUnit.getId())) {
            subUnits = subUnits == null ? new ArrayList<InsUnit>() : subUnits;
            subUnits.add(subUnit);
            allSubUnits.remove(i);
            i--;
          }
        }
        superUnit.setSubInsUnit(subUnits);
      }
    }
    return superUnits;
  }

  /**
   * 获取单位部门,获取最顶层，且加载子部门.
   * 
   * @param insId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<InsUnit> getInsUnitWithSubByInsId(Long insId) throws DaoException {
    List<InsUnit> superUnits =
        super.createQuery("from InsUnit t where t.insId = ? and t.superInsUnitId is null order by t.id", insId).list();
    // 获取子部门.
    if (superUnits != null && superUnits.size() > 0) {
      // 所有子部门
      List<InsUnit> allSubUnits = super.createQuery(
          "from InsUnit t where t.insId = ? and t.superInsUnitId is not null order by t.superInsUnitId,t.id", insId)
              .list();

      for (InsUnit superUnit : superUnits) {
        List<InsUnit> subUnits = null;
        for (int i = 0; i < allSubUnits.size(); i++) {
          InsUnit subUnit = allSubUnits.get(i);
          if (subUnit.getSuperInsUnitId().equals(superUnit.getId())) {
            subUnits = subUnits == null ? new ArrayList<InsUnit>() : subUnits;
            subUnits.add(subUnit);
            allSubUnits.remove(i);
            i--;
          }
        }
        superUnit.setSubInsUnit(subUnits);
      }
    }
    return superUnits;
  }

  @SuppressWarnings("unchecked")
  public InsUnit getUnitWithSub(Long insId, Long unitId) throws DaoException {

    InsUnit superUnit = this.get(unitId);

    // 获取子部门.

    return superUnit;
  }

  /**
   * 获取指定单位部门,获取最顶层，且加载子部门.
   * 
   * @param insId
   * @return
   * @throws DaoException
   */
  public InsUnit getInsUnitWithSubByUnitId(Long insId, Long unitId) throws DaoException {
    InsUnit insUnit = null;
    if (unitId == null) {
      return null;
    } else {
      insUnit = super.findUnique("from InsUnit t where t.insId = ? and t.id = ?", insId, unitId);
    }
    // 获取子部门.
    if (insUnit != null && insUnit.getSuperInsUnitId() == null) {
      List<InsUnit> subUnits = this.getInsUnitBySuperId(insUnit.getId());
      insUnit.setSubInsUnit(subUnits);
    }
    return insUnit;
  }

  /**
   * 获取单位部门,获取最顶层，不加载子部门.
   * 
   * @param insId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<InsUnit> getSuperUnitByInsId(Long insId) throws DaoException {

    List<InsUnit> superUnits =
        super.createQuery("from InsUnit t where t.insId = ? and t.superInsUnitId is null order by t.id", insId).list();
    return superUnits;
  }

  /**
   * 获取单位部门,获取最顶层，不加载子部门.
   * 
   * @param insId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<InsUnit> getSuperUnitByInsId(Long insId, int maxSize) throws DaoException {

    List<InsUnit> superUnits =
        super.createQuery("from InsUnit t where t.insId = ? and t.superInsUnitId is null ", insId)
            .setMaxResults(maxSize).list();
    return superUnits;
  }

  /**
   * 获取单个部门实体对象.
   * 
   * @param id
   * @return
   * @throws DaoException
   */
  public InsUnit getInsUnitRolById(Long id) throws DaoException {

    return super.findUnique("from InsUnit t where t.id = ?", id);
  }

  /**
   * 删除部门.
   * 
   * @param unitId
   * @return
   * @throws DaoException
   */
  public int deleteUnit(Long unitId) throws DaoException {

    String hql = "delete from InsUnit where id =? or superInsUnitId=?";
    int count = createQuery(hql, unitId, unitId).executeUpdate();

    return count;
  }

  /**
   * 合并时删除部门.
   * 
   * @param unitId
   * @return
   * @throws DaoException
   */
  public int deleteMergeUnit(Long unitId) throws DaoException {
    String hql = "delete from InsUnit where id =? or superInsUnitId=?";
    int count = createQuery(hql, unitId, unitId).executeUpdate();
    return count;
  }

  /**
   * 根据部id获取部门名.
   * 
   * @param unitName
   * @throws DaoException
   */
  public String getUnitName(Long unitId, Long insId, Locale locale) throws DaoException {
    if (unitId == null)
      return null;

    InsUnit insUnit = super.findUnique("from InsUnit where id = ? and insId=?", unitId, insId);

    return (insUnit == null ? null : insUnit.getName());
  }

  /**
   * 根据部id获取部门名.
   * 
   * @param unitName
   * @throws DaoException
   */
  public String getUnitName(Long unitId, Locale locale) throws DaoException {
    if (unitId == null)
      return null;

    InsUnit insUnit = super.findUnique("from InsUnit where id = ?", unitId);

    return (insUnit == null ? null : insUnit.getName());
  }

  /**
   * 根据部id获取部门英文名.
   * 
   * @param unitName
   * @throws DaoException
   */
  public String getUnitEnName(Long unitId) throws DaoException {
    if (unitId == null)
      return null;

    InsUnit insUnit = super.findUnique("from InsUnit where id = ?", unitId);
    if (insUnit != null) {
      return StringUtils.isBlank(insUnit.getEnName()) ? insUnit.getZhName() : insUnit.getEnName();
    } else {
      return null;
    }
  }

  /**
   * 根据部id获取部门名.
   * 
   * @param unitIds
   * @param locale
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public Map<Long, String> getUnitName(Collection<Long> unitIds, Locale locale) throws DaoException {
    if (CollectionUtils.isEmpty(unitIds))
      return null;

    List<InsUnit> insUnits =
        super.createQuery("from InsUnit where id in(:unitIds)").setParameterList("unitIds", unitIds).list();
    Map<Long, String> map = new HashMap<Long, String>();
    for (InsUnit insUnit : insUnits) {
      map.put(insUnit.getId(), insUnit.getName());
    }
    return map;
  }


  public List<String> findNameByInsId(Long insId) throws DaoException {
    return find("select zhName from InsUnit where insId=? ", insId);
  }


  /**
   * 判断部门名称是否已经使用.
   * 
   * @param insId
   * @param unitName
   * @return
   * @throws Exception
   */
  public Boolean isUnitNameExit(Long insId, String unitName, Long exUid) {
    Long count = 0L;
    if (exUid != null) {
      String hql = "select count(id) from InsUnit where (zhName = ? or enName = ?) and insId = ? and id not in(?) ";
      count = super.findUnique(hql, unitName, unitName, insId, exUid);
    } else {
      String hql = "select count(id) from InsUnit where (zhName = ? or enName = ?) and insId = ? ";
      count = super.findUnique(hql, unitName, unitName, insId);
    }

    if (count > 0) {
      return true;
    }
    return false;
  }

  /**
   * 判断指定部门或者子部门是否是部门字串.
   * 
   * @param deptName
   * @param unitId
   * @return
   */
  public boolean isDeptContainUnitName(String deptName, Long unitId) {

    String hql = "select count(t.id) from InsUnit t where (t.id = ? or t.superInsUnitId = ?)   "
        + "and ((trim(t.zhName) is not null and ? like '%'|| lower(t.zhName) ||'%') or (trim(t.enName) is not null and ? like '%'|| lower(t.enName) ||'%' ))";
    Long count = super.findUnique(hql, unitId, unitId, deptName, deptName);
    if (count > 0) {
      return true;
    }
    return false;
  }

  /**
   * 通过ID获取部门列表.
   * 
   * @param ids
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<InsUnit> getInsUnitByIds(Collection<Long> ids) {
    String hql = "from InsUnit t where t.id in(:unitIds)";
    return super.createQuery(hql).setParameterList("unitIds", ids).list();
  }

  /**
   * 批量查询单位，顺序安装ID的顺序排序.
   * 
   * @param prvIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<InsUnit> getOrderByIds(List<Long> ids) {

    String hql = "from InsUnit t where t.id in(:ids)";
    List<InsUnit> list = super.createQuery(hql).setParameterList("ids", ids).list();
    List<InsUnit> orderList = new ArrayList<InsUnit>();
    for (Long id : ids) {
      for (int i = 0; i < list.size(); i++) {
        InsUnit unit = list.get(i);
        if (unit.getId().equals(id)) {
          orderList.add(unit);
          list.remove(i);
          break;
        }
      }
    }
    return orderList;
  }

  /**
   * 取得部门中子部门的总数
   * 
   * @param superId
   * @return
   * @throws DaoException
   */
  public Long getSubUnitCountBySuperUnitId(Long superId) throws DaoException {
    String hql = "select count(iu.id) from InsUnit iu where iu.superInsUnitId=?";
    return super.findUnique(hql, superId);
  }

  /**
   * 查询某个单位的部门总数
   * 
   * @param insId
   * @return
   * @throws DaoException
   */
  public Long getInsUnitCountByInsId(Long insId) throws DaoException {
    String hql = "select count(iu.id) from InsUnit iu where iu.insId=?";
    return super.findUnique(hql, insId);
  }

  @SuppressWarnings("unchecked")
  public List<InsUnit> queryInsUnitBySuperId(Long superId, Long insId) throws DaoException {

    return super.createQuery("from InsUnit t where t.insId = ? and t.superInsUnitId = ? order by t.id",
        new Object[] {insId, superId}).list();
  }

  /**
   * 查找某个单位或者某个部门下面的部门
   * 
   * @param insId
   * @param unitId
   * @return
   */
  public List<InsUnit> findAllInsUnit(Long insId, Long unitId) throws DaoException {
    String hql = "from InsUnit t where t.insId = :insId";
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("insId", insId);
    if (unitId != null && unitId != 0l) {
      hql += " and (t.id = :unitId or t.superInsUnitId = :unitId)";
      params.put("unitId", unitId);
    }
    return super.createQuery(hql, params).list();
  }

  /**
   * 查找某个单位或者某个部门下面的顶级部门
   * 
   * @param insId
   * @param unitId
   * @return
   */
  public List<InsUnit> findTopInsUnit(Long insId, Long unitId) throws DaoException {
    String hql = "from InsUnit t where t.insId = :insId";
    Map<String, Object> params = new HashMap<String, Object>();
    params.put("insId", insId);
    if (unitId != null && unitId != 0l) {
      hql += " and (t.id = :unitId or t.superInsUnitId = :unitId)";
      params.put("unitId", unitId);
    } else {
      hql += " and t.superInsUnitId is null";
    }
    return super.createQuery(hql, params).list();
  }

  /**
   * 是否是二级部门
   * 
   * @param insId
   * @param unitId
   * @return
   */
  public boolean isSecondUnit(Long insId, Long unitId) {
    String hql = "from InsUnit t where t.insId = ? and t.id = ?";
    InsUnit insUnit = super.findUnique(hql, insId, unitId);
    if (insUnit != null && insUnit.getSuperInsUnitId() != null) {
      return true;
    } else {
      return false;
    }
  }

}
