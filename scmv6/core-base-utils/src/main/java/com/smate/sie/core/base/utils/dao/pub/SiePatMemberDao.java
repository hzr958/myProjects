package com.smate.sie.core.base.utils.dao.pub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.sie.core.base.utils.model.pub.SiePatMember;

/**
 * 专利成员 Dao
 * 
 * @author hd
 *
 */
@Repository
public class SiePatMemberDao extends SieHibernateDao<SiePatMember, Long> {

  /**
   * 人员专利数统计
   * 
   * @param insId
   * @return
   */
  public Long getConfirmPtTotalNumByPsnId(Long psnId) {
    String hql =
        "select count(t.pmId) from SiePatMember t where t.memberPsnid = ? and  exists (select 1 from SiePatent d where d.patId = t.patId and d.status <> '1')";
    return super.findUnique(hql, psnId);
  }

  /**
   * 
   * @param patId
   * @param authorName
   * @param insId
   * @param i
   */
  public void saveSiePatMember(Long patId, String authorName, Long insId, int i) {
    String hql = "delete from SiePatMember t where t.patId = ? ";
    super.createQuery(hql, patId).executeUpdate();
    SiePatMember member = new SiePatMember();
    member.setPatId(patId);
    member.setMemberName(authorName);
    member.setInsId(insId);
    member.setSeqNo(Long.valueOf(i));
    super.save(member);
    this.getSession().flush();

  }

  /**
   * 
   * @param patId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<SiePatMember> getMembersByPatId(Long patId) {
    return super.createQuery("from SiePatMember t where t.patId= ? order by seqNo ", patId).list();
  }

  /**
   * 
   * @param patId
   */
  public void deletePatMember(Long patId) {
    String hql = "delete from SiePatMember t where t.patId = ? ";
    super.createQuery(hql, patId).executeUpdate();
  }

  /**
   * 获取专利/人员关系信息.
   * 
   * @param id
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<SiePatMember> getPatMembersByPatId(Long patId) {

    return super.createQuery("from SiePatMember t where t.patId= ? order by seqNo ", patId).list();
  }

  /**
   * 获取指定序号的memberId.
   * 
   * @param patId
   * @param seqNo
   * @return
   */
  public Long getPatMemberPmId(Long patId, Integer seqNo) {

    List<Long> pmIds = super.find("select pmId from SiePatMember t where t.patId= ? and seqNo = ? ", patId, seqNo);
    if (pmIds != null && pmIds.size() > 0) {
      return pmIds.get(0);
    }
    return null;
  }

  /**
   * 获取指定memberId的序号.
   * 
   * @param patId
   * @param seqNo
   * @return
   */
  public Integer getPatMemberSeqNo(Long patId, Long pmId) {

    List<Integer> seqNo = super.find("select seqNo from SiePatMember t where t.patId= ? and id = ? ", patId, pmId);
    if (seqNo != null && seqNo.size() > 0) {
      return seqNo.get(0);
    }
    return null;
  }

  /**
   * 获取专利/人员关系信息.
   * 
   * @param id
   * @return
   * @throws DaoException
   */
  public SiePatMember getPatMemberById(Long id) {

    return (SiePatMember) super.findUnique("from SiePatMember t where t.pmId= ? ", new Object[] {id});
  }

  /**
   * 获取专利/人员关系信息.
   * 
   * @param id
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public SiePatMember getPsnPatMember(Long patId, Long psnId) {

    List<SiePatMember> list =
        super.createQuery("from SiePatMember t where t.patId= ? and t.psnId = ? ", patId, psnId).list();
    if (list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  /**
   * 查找最大的SEQ_NO.
   * 
   * @param patId
   * @return
   */
  public Integer getMaxSeqNo(Long patId) {

    String hql = "select max(seqNo) from SiePatMember where patId = ? ";
    return super.findUnique(hql, patId);
  }

  /**
   * 获取专利/人员关系信息.
   * 
   * @param patId
   * @param pmId
   * @return
   * @throws DaoException
   */
  public SiePatMember getPatMemeberByPatPsnd(Long patId, Long psnId) {

    return (SiePatMember) super.findUnique("from SiePatMember t where t.patId = ? and t.psnId= ? ",
        new Object[] {patId, psnId});
  }

  /**
   * 获取专利/人员关系信息.
   * 
   * @param patId
   * @param pmId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<SiePatMember> getPatMemeberByPsnAndIns(Long insId, Long psnId) {

    return super.createQuery("from SiePatMember t where t.insId = ? and t.psnId= ? ", insId, psnId).list();
  }

  /**
   * 保存专利发明人.
   * 
   * @param member SiePatMember
   * @throws DaoException
   */
  public void savePatMember(SiePatMember member) {
    super.save(member);
  }

  /**
   * 删除专利成员.
   * 
   * @param member SiePatMember
   * @throws DaoException
   */
  public void deletePatMember(SiePatMember member) {

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
  public SiePatMember updateMemberPsnId(long id, Long psnId) {
    SiePatMember pm = super.get(id);
    if (pm != null) {
      super.save(pm);
    }
    return pm;
  }

  /**
   * 把专利成员的名字用逗号分隔连接起来.
   * 
   * @param patId
   * @return
   * @throws ServiceException
   */
  public Map<String, String> buildPatAuthorNames(long patId) {

    List<SiePatMember> authors = getPatMembersByPatId(patId);

    return buildPatAuthorNames(authors);
  }

  /**
   * 把专利成员的名字用逗号分隔连接起来.
   * 
   * @param authors
   * @return
   */
  private Map<String, String> buildPatAuthorNames(List<SiePatMember> authors) {
    StringBuffer authorNames = new StringBuffer(0);
    String allNames = "";
    String briefNames = "";
    String splitStr = ", ";
    String endStr = "……";
    Map<String, String> map = new HashMap<String, String>();
    for (int i = 0; i < authors.size(); i++) {
      SiePatMember item = authors.get(i);
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
   * 把专利成员的名字用逗号分隔连接起来.
   * 
   * @param patId
   * @return
   * @throws DaoException
   */
  public Map<String, Object> buildPatAuthorNames2(long patId) {
    Map<String, Object> map = new HashMap<String, Object>();
    List<SiePatMember> authors = getPatMembersByPatId(patId);
    Map<String, String> authorNames = buildPatAuthorNames(authors);
    map.put("AuthorNames", authorNames.get("all_names"));
    map.put("BriefAuthorNames", authorNames.get("brief_names"));
    if (authors.size() > 0) {
      map.put("FirstAuthor", authors.get(0).getPmInsId());
    }
    return map;
  }

  /**
   * 专利作者是否匹配到人员.
   * 
   * @param patId
   * @return
   */
  public boolean hasMatchPsn(long patId) {

    String hql = "select count(t.pmId) from SiePatMember t where t.psnId is not null and t.patId = ? ";
    Long count = super.findUnique(hql, patId);
    if (count > 0) {
      return true;
    }
    return false;
  }

  /**
   * 获取专利作者列表，完善专利统计数据.
   * 
   * @param pubIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<SiePatMember> queryPatMembers(List<Long> patIds, Long insId) {
    String hql = "from SiePatMember t where t.patId in (:patIds) and insId = :insId order by t.patId,t.seqNo asc ";
    return super.createQuery(hql).setParameterList("patIds", patIds).setParameter("insId", insId).list();
  }

  @SuppressWarnings("unchecked")
  public List<SiePatMember> queryPubMemberByPsnId(Long psnId) {
    return super.createQuery("from SiePatMember t where t.pmInsId= ?", new Object[] {psnId}).list();
  }

  @SuppressWarnings("unchecked")
  public List<String> queryPubMemberName(Long patId) {
    return super.createQuery("select t.MemberName from SiePatMember t where t.patId=? order by t.pmId desc", patId)
        .list();
  }

  @SuppressWarnings("unchecked")
  public List<SiePatMember> getMembersByPubId(Long patId) {

    return super.createQuery("from SiePatMember t where t.patId= ? order by seqNo ", new Object[] {patId}).list();
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public List<SiePatMember> getMoreMembersByPatId(Page page, Long patId) {
    String countHql = "select count(distinct t.pmId) ";
    String hql = " from SiePatMember t where t.patId= ? order by seqNo ";
    Long totalCount = super.findUnique(countHql + hql, patId);
    page.setTotalCount(totalCount);
    Query queryResult = super.createQuery(hql, patId);
    queryResult.setFirstResult(page.getFirst() - 1);
    queryResult.setMaxResults(page.getPageSize());
    return queryResult.list();
  }

  public int getMembersCount(Long patId) {
    String hql = "select count(t.pmId) from SiePatMember t where t.patId= ? ";
    Long count = super.findUnique(hql, patId);
    if (count > 0) {
      return count.intValue();
    }
    return 0;
  }

  @SuppressWarnings("unchecked")
  public List<Long> getPmIdByPatId(Long patId) {
    String hql = "select t.pmId from SiePatMember t where t.patId= ? ";
    return super.createQuery(hql, patId).list();
  }

  @SuppressWarnings("unchecked")
  public List<SiePatMember> getListByInsId(Long insId) {
    String hql = " from SiePatMember t where t.insId= ? order by patId ";
    return super.createQuery(hql, insId).list();
  }
}
