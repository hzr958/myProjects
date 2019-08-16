package com.smate.sie.core.base.utils.dao.pub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.sie.core.base.utils.model.pub.SiePubMember;

/**
 * 成果成员 Dao
 * 
 * @author hd
 *
 */
@Repository
public class SiePubMemberDao extends SieHibernateDao<SiePubMember, Long> {

  /**
   * 人员成果数统计
   * 
   * @param insId
   * @return
   */
  public Long getConfirmPubTotalNumByPsnId(Long psnId) {
    String hql =
        "select count(t.pmId) from SiePubMember t where t.memberPsnid = ? and  exists (select 1 from SiePublication d where d.pubId = t.pubId and d.status <> '1') ";
    return super.findUnique(hql, psnId);
  }

  /**
   * 
   * @param pubId
   * @param authorName
   * @param insId
   * @param i
   */
  public void saveSiePubMember(Long pubId, String authorName, Long insId, int i) {

    SiePubMember member = new SiePubMember();
    member.setPubId(pubId);
    member.setMemberName(authorName);
    member.setInsId(insId);
    member.setSeqNo(Long.valueOf(i));
    super.save(member);
    this.getSession().flush();

  }

  /**
   * 
   * @param pubId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<SiePubMember> getMembersByPubId(Long pubId) {
    return super.createQuery("from SiePubMember t where t.pubId= ? order by seqNo ", pubId).list();

  }

  /**
   * 
   * @param pubId
   */
  public void deletePubMember(Long pubId) {
    String hql = "delete from SiePubMember t where t.pubId = ? ";
    super.createQuery(hql, pubId).executeUpdate();
  }

  /**
   * 获取成果/人员关系信息.
   * 
   * @param id
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<SiePubMember> getPubMembersByPubId(Long pubId) {

    return super.createQuery("from SiePubMember t where t.pubId= ? order by seqNo ", pubId).list();
  }

  /**
   * 获取指定序号的memberId.
   * 
   * @param pubId
   * @param seqNo
   * @return
   */
  public Long getPubMemberPmId(Long pubId, Integer seqNo) {

    List<Long> pmIds = super.find("select pmId from SiePubMember t where t.pubId= ? and seqNo = ? ", pubId, seqNo);
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

    List<Integer> seqNo = super.find("select seqNo from SiePubMember t where t.pubId= ? and id = ? ", pubId, pmId);
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
  public SiePubMember getPubMemberById(Long id) {

    return (SiePubMember) super.findUnique("from SiePubMember t where t.pmId= ? ", new Object[] {id});
  }

  /**
   * 获取成果/人员关系信息.
   * 
   * @param id
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public SiePubMember getPsnPubMember(Long pubId, Long psnId) {

    List<SiePubMember> list =
        super.createQuery("from SiePubMember t where t.pubId= ? and t.psnId = ? ", pubId, psnId).list();
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

    String hql = "select max(seqNo) from SiePubMember where pubId = ? ";
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
  public SiePubMember getPubMemeberByPubPsnd(Long pubId, Long psnId) {

    return (SiePubMember) super.findUnique("from SiePubMember t where t.pubId = ? and t.psnId= ? ",
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
  public List<SiePubMember> getPubMemeberByPsnAndIns(Long insId, Long psnId) {

    return super.createQuery("from SiePubMember t where t.insId = ? and t.psnId= ? ", insId, psnId).list();
  }

  /**
   * 保存成果成员.
   * 
   * @param member SiePubMember
   * @throws DaoException
   */
  public void savePubMember(SiePubMember member) {

    super.save(member);
  }

  /**
   * 删除成果成员.
   * 
   * @param member SiePubMember
   * @throws DaoException
   */
  public void deletePubMember(SiePubMember member) {

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
  public SiePubMember updateMemberPsnId(long id, Long psnId) {
    SiePubMember pm = super.get(id);
    if (pm != null) {
      // pm.setPsnId(psnId);
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
  public Map<String, String> buildPubAuthorNames(long pubId) {

    List<SiePubMember> authors = getPubMembersByPubId(pubId);

    return buildPubAuthorNames(authors);
  }

  /**
   * 把成果成员的名字用逗号分隔连接起来.
   * 
   * @param authors
   * @return
   */
  private Map<String, String> buildPubAuthorNames(List<SiePubMember> authors) {
    StringBuffer authorNames = new StringBuffer(0);
    String allNames = "";
    String briefNames = "";
    String splitStr = ", ";
    String endStr = "……";
    Map<String, String> map = new HashMap<String, String>();
    for (int i = 0; i < authors.size(); i++) {
      SiePubMember item = authors.get(i);
      String name = StringUtils.trimToEmpty(item.getMemberName());
      if (StringUtils.isBlank(name)) {
        continue;
      }
      Long psnId = item.getPmInsId();
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
  public Map<String, Object> buildPubAuthorNames2(long pubId) {
    Map<String, Object> map = new HashMap<String, Object>();

    List<SiePubMember> authors = getPubMembersByPubId(pubId);
    Map<String, String> authorNames = buildPubAuthorNames(authors);
    map.put("AuthorNames", authorNames.get("all_names"));
    map.put("BriefAuthorNames", authorNames.get("brief_names"));
    if (authors.size() > 0) {
      map.put("FirstAuthor", authors.get(0).getPmInsId());
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

    String hql = "select count(t.pmId) from SiePubMember t where t.psnId is not null and t.pubId = ? ";
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
  public List<SiePubMember> queryPubMembers(List<Long> pubIds, Long insId) {

    String hql = "from SiePubMember t where t.pubId in (:pubIds) and insId = :insId order by t.pubId,t.seqNo asc ";
    return super.createQuery(hql).setParameterList("pubIds", pubIds).setParameter("insId", insId).list();
  }

  @SuppressWarnings("unchecked")
  public List<SiePubMember> queryPubMemberByPsnId(Long psnId) {
    return super.createQuery("from SiePubMember t where t.pmInsId= ?", new Object[] {psnId}).list();
  }

  @SuppressWarnings("unchecked")
  public List<String> queryPubMemberName(Long pubId) {
    return super.createQuery("select t.MemberName from SiePubMember t where t.pubId=? order by t.pmId desc", pubId)
        .list();
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public List<SiePubMember> getMoreMembersByPubId(Page page, Long pubId) {
    String countHql = "select count(distinct t.pmId) ";
    String hql = " from SiePubMember t where t.pubId= ? order by seqNo ";
    Long totalCount = super.findUnique(countHql + hql, pubId);
    page.setTotalCount(totalCount);
    Query queryResult = super.createQuery(hql, pubId);
    queryResult.setFirstResult(page.getFirst() - 1);
    queryResult.setMaxResults(page.getPageSize());
    return queryResult.list();
  }

  public int getMembersCount(Long pubId) {
    String hql = "select count(t.pmId) from SiePubMember t where t.pubId= ? ";
    Long count = super.findUnique(hql, pubId);
    if (count > 0) {
      return count.intValue();
    }
    return 0;
  }

  @SuppressWarnings("unchecked")
  public List<Long> getPmIdByPubId(Long pubId) {
    String hql = "select t.pmId from SiePubMember t where t.pubId= ? ";
    return super.createQuery(hql, pubId).list();
  }

  @SuppressWarnings("unchecked")
  public List<SiePubMember> getListByInsId(Long insId) {
    String hql = " from SiePubMember t where t.insId= ? order by seqNo ";
    return super.createQuery(hql, insId).list();
  }
}
