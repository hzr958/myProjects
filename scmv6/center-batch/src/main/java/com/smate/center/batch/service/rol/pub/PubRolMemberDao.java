package com.smate.center.batch.service.rol.pub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.rol.pub.PubMemberRol;
import com.smate.core.base.utils.data.RolHibernateDao;
import com.smate.core.base.utils.model.Page;


/**
 * 单位成果成员Dao:新增、修改、删除、获取详情.
 * 
 * @author yamingd
 * 
 */
@Repository
public class PubRolMemberDao extends RolHibernateDao<PubMemberRol, Long> {

  /**
   * 获取成果/人员关系信息.
   * 
   * @param id
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PubMemberRol> getPubMembersByPubId(Long pubId) throws DaoException {

    return super.createQuery("from PubMemberRol t where t.pubId= ? order by seqNo ", new Object[] {pubId}).list();
  }

  /**
   * 获取指定序号的memberId.
   * 
   * @param pubId
   * @param seqNo
   * @return
   */
  public Long getPubMemberPmId(Long pubId, Integer seqNo) {

    List<Long> pmIds = super.find("select id from PubMemberRol t where t.pubId= ? and seqNo = ? ", pubId, seqNo);
    if (pmIds != null && pmIds.size() > 0) {
      return pmIds.get(0);
    }
    return null;
  }

  /**
   * 获取指定memberId的序号.
   * 
   * @param pubId
   * @param seqNo
   * @return
   */
  public Integer getPubMemberSeqNo(Long pubId, Long pmId) {

    List<Integer> seqNo = super.find("select seqNo from PubMemberRol t where t.pubId= ? and id = ? ", pubId, pmId);
    if (seqNo != null && seqNo.size() > 0) {
      return seqNo.get(0);
    }
    return null;
  }

  /**
   * 获取成果/人员关系信息.
   * 
   * @param id
   * @return
   * @throws DaoException
   */
  public PubMemberRol getPubMemberById(Long id) throws DaoException {

    return (PubMemberRol) super.findUnique("from PubMemberRol t where t.id= ? ", new Object[] {id});
  }

  /**
   * 获取成果/人员关系信息.
   * 
   * @param id
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public PubMemberRol getPsnPubMember(Long pubId, Long psnId) {

    List<PubMemberRol> list =
        super.createQuery("from PubMemberRol t where t.pubId= ? and t.psnId = ? ", pubId, psnId).list();
    if (list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  /**
   * 查找最大的SEQ_NO.
   * 
   * @param pubId
   * @return
   */
  public Integer getMaxSeqNo(Long pubId) {

    String hql = "select max(seqNo) from PubMemberRol where pubId = ? ";
    return super.findUnique(hql, pubId);
  }

  /**
   * 获取成果/人员关系信息.
   * 
   * @param pubId
   * @param pmId
   * @return
   * @throws DaoException
   */
  public PubMemberRol getPubMemeberByPubPsnd(Long pubId, Long psnId) throws DaoException {

    return (PubMemberRol) super.findUnique("from PubMemberRol t where t.pubId = ? and t.psnId= ? ",
        new Object[] {pubId, psnId});
  }

  /**
   * 获取成果/人员关系信息.
   * 
   * @param pubId
   * @param pmId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PubMemberRol> getPubMemeberByPsnAndIns(Long insId, Long psnId) throws DaoException {

    return super.createQuery("from PubMemberRol t where t.insId = ? and t.psnId= ? ", insId, psnId).list();
  }

  /**
   * 保存成果成员.
   * 
   * @param member PubMemberRol
   * @throws DaoException
   */
  public void savePubMember(PubMemberRol member) throws DaoException {

    super.save(member);
  }

  /**
   * 删除成果成员.
   * 
   * @param member PubMemberRol
   * @throws DaoException
   */
  public void deletePubMember(PubMemberRol member) throws DaoException {

    super.delete(member);
  }

  /**
   * 更新成员-人ID.
   * 
   * @param id
   * @param psnId
   * @return
   * @throws DaoException
   */
  public PubMemberRol updateMemberPsnId(long id, Long psnId) throws DaoException {
    PubMemberRol pm = super.get(id);
    if (pm != null) {
      pm.setPsnId(psnId);
      super.save(pm);
    }
    return pm;
  }

  /**
   * 把成果成员的名字用逗号分隔连接起来.
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public Map<String, String> buildPubAuthorNames(long pubId) throws DaoException {

    List<PubMemberRol> authors = getPubMembersByPubId(pubId);

    return buildPubAuthorNames(authors);
  }

  /**
   * 把成果成员的名字用逗号分隔连接起来.
   * 
   * @param authors
   * @return
   */
  private Map<String, String> buildPubAuthorNames(List<PubMemberRol> authors) {
    StringBuffer authorNames = new StringBuffer(0);
    String allNames = "";
    String briefNames = "";
    String splitStr = ", ";
    String endStr = "……";
    Map<String, String> map = new HashMap<String, String>();
    for (int i = 0; i < authors.size(); i++) {
      PubMemberRol item = authors.get(i);
      String name = StringUtils.trimToEmpty(item.getName());
      if (StringUtils.isBlank(name)) {
        continue;
      }
      Long psnId = item.getPsnId();
      if (item.getAuthorPos() != null && 1 == item.getAuthorPos()) {
        name = "*" + name; // 是通讯作者，则名称前加*号
      }
      if (null == psnId) {
        // 缩略
        if (authorNames.length() < 200
            && (authorNames.length() + splitStr.length() + name.length() + endStr.length()) > 200) {
          briefNames = authorNames.toString() + endStr;
        }
        if (authorNames.length() > 0) {
          authorNames.append("; ");
        }
        authorNames.append(name);
      } else {
        // 缩略
        name = String.format("<strong>%s</strong>", name);
        if (authorNames.length() < 200
            && (authorNames.length() + splitStr.length() + name.length() + endStr.length()) > 200) {
          briefNames = authorNames.toString() + endStr;
        }
        if (authorNames.length() > 0) {
          authorNames.append("; ");
        }
        authorNames.append(name); // 可以关联到psn_id,则加粗显示
      }
    }
    if (authors.size() > 0) {
      allNames = authorNames.toString();
      if (StringUtils.isBlank(briefNames)) {
        briefNames = allNames;
      }
    }
    map.put("all_names", allNames);
    map.put("brief_names", briefNames);
    return map;
  }

  /**
   * 把成果成员的名字用逗号分隔连接起来.
   * 
   * @param pubId
   * @return
   * @throws DaoException
   */
  public Map<String, Object> buildPubAuthorNames2(long pubId) throws DaoException {
    Map<String, Object> map = new HashMap<String, Object>();

    List<PubMemberRol> authors = getPubMembersByPubId(pubId);
    Map<String, String> authorNames = buildPubAuthorNames(authors);
    map.put("AuthorNames", authorNames.get("all_names"));
    map.put("BriefAuthorNames", authorNames.get("brief_names"));
    if (authors.size() > 0) {
      map.put("FirstAuthor", authors.get(0).getPsnId());
    }
    return map;
  }

  /**
   * 成果作者是否匹配到人员.
   * 
   * @param pubId
   * @return
   */
  public boolean hasMatchPsn(long pubId) {

    String hql = "select count(t.id) from PubMemberRol t where t.psnId is not null and t.pubId = ? ";
    Long count = super.findUnique(hql, pubId);
    if (count > 0) {
      return true;
    }
    return false;
  }

  /**
   * 获取成果作者列表，完善成果统计数据.
   * 
   * @param pubIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PubMemberRol> queryPubMembers(List<Long> pubIds, Long insId) {

    String hql = "from PubMemberRol t where t.pubId in (:pubIds) and insId = :insId order by t.pubId,t.seqNo asc ";
    return super.createQuery(hql).setParameterList("pubIds", pubIds).setParameter("insId", insId).list();
  }

  @SuppressWarnings("unchecked")
  public List<PubMemberRol> queryPubMemberByPsnId(Long psnId) throws DaoException {
    return super.createQuery("from PubMemberRol t where t.psnId= ?", new Object[] {psnId}).list();
  }

  @SuppressWarnings("unchecked")
  public List<String> queryPubMemberName(Long pubId) throws DaoException {
    return super.createQuery("select t.name from PubMemberRol t where t.pubId=? order by t.id desc", pubId).list();
  }

  @SuppressWarnings("unchecked")
  public List<PubMemberRol> getMembersByPubId(Long pubId) throws DaoException {

    return super.createQuery("from PubMemberRol t where t.pubId= ? order by seqNo ", new Object[] {pubId}).list();
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public List<PubMemberRol> getMoreMembersByPubId(Page page, Long pubId) throws DaoException {
    String countHql = "select count(distinct t.id) ";
    String hql = " from PubMemberRol t where t.pubId= ? order by seqNo ";
    Long totalCount = super.findUnique(countHql + hql, pubId);
    page.setTotalCount(totalCount);
    Query queryResult = super.createQuery(hql, pubId);
    queryResult.setFirstResult(page.getFirst() - 1);
    queryResult.setMaxResults(page.getPageSize());
    return queryResult.list();
  }

  public int getMembersCount(Long pubId) throws DaoException {
    String hql = "select count(t.id) from PubMemberRol t where t.pubId= ? ";
    Long count = super.findUnique(hql, pubId);
    if (count > 0) {
      return count.intValue();
    }
    return 0;
  }
}
