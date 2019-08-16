package com.smate.center.task.dao.rol.quartz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.rol.quartz.PubMemberRol;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * 成果人员关系dao
 * 
 * @author zjh
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
  public PubMemberRol getPubMemberById(Long assignPmId) throws DaoException {
    return (PubMemberRol) super.createQuery("from PubMemberRol t where t.id=:assignPmId")
        .setParameter("assignPmId", assignPmId).uniqueResult();
  }

  public void savePubMember(PubMemberRol assignPubMember) throws DaoException {
    super.save(assignPubMember);
  }

  public Map<String, Object> buildPubAuthorNames2(Long pubId) throws DaoException {
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
        if (authorNames.length() < 200
            && (authorNames.length() + splitStr.length() + name.length() + endStr.length()) > 200) {
          briefNames = authorNames.toString() + endStr;
        }
        if (authorNames.length() > 0) {
          authorNames.append("; ");
        }
        authorNames.append(name);
      } else {
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
   * 获取成果/人员关系信息.
   * 
   * @param id
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PubMemberRol> getPubMembersByPubId(Long pubId) throws DaoException {
    return super.createQuery("from PubMemberRol t where t.pubId=:pubId order by seqNo ").setParameter("pubId", pubId)
        .list();
  }

  public Integer getPubMemberSeqNo(Long pubId, Long pmId) {
    List<Integer> seqNo = super.createQuery("select seqNo from PubMemberRol t where t.pubId=:pubId and id =:pmId")
        .setParameter("pubId", pubId).setParameter("pmId", pmId).list();
    if (seqNo != null && seqNo.size() > 0) {
      return seqNo.get(0);
    }
    return null;
  }

}
