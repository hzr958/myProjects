package com.smate.web.group.dao.grp.member;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.group.action.grp.form.GrpMemberForm;
import com.smate.web.group.model.grp.member.GrpProposer;

/**
 * 申请中成员关系Dao类
 * 
 * @author zzx
 *
 */
@Repository
public class GrpProposerDao extends SnsHibernateDao<GrpProposer, Long> {
  /**
   * 获取邀请我的群组关系记录
   * 
   * @return
   */
  public List<GrpProposer> hasIviteList(Long psnId) {
    String hql =
        "select new GrpProposer(t.id,t.grpId,t.inviterId) from GrpProposer t where t.type=2 and t.isAccept=2 and t.psnId=:psnId and exists (select 1 from Person  p where p.personId = t.inviterId)  order by t.createDate desc";
    return this.createQuery(hql).setParameter("psnId", psnId).list();
  }

  /**
   * 是否有被邀请的群组
   * 
   * @return
   */
  public boolean isHasIviteGrp(Long psnId) {
    String hql =
        "select t.id from GrpProposer t where exists(select 1 from GrpBaseinfo t1 where t1.grpId = t.grpId and t1.status='01' ) and t.type=2 and t.isAccept=2 and t.psnId=:psnId and exists (select 1 from Person  p where p.personId = t.inviterId) ";
    List<Object> list = this.createQuery(hql).setParameter("psnId", psnId).list();
    if (list != null && list.size() > 0) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * 根据psnId 获取待批准的群组数量
   * 
   * @return
   */
  public Long getGrpCountByPsnId(Long psnId) {
    String hql = "select count(t.id) from GrpProposer t where t.isAccept=2 and t.type=1 and t.psnId=:psnId";
    Object object = this.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
    if (object == null) {
      return 0L;
    } else {
      return (Long) object;
    }
  }

  /**
   * 获取待批准成员数量
   * 
   * @return
   */
  public Long getPendingApproval(Long grpId) {
    String hql =
        "select count(t.id) from GrpProposer t where t.isAccept=2 and t.type=1 and t.grpId=:grpId and exists (select 1 from Person  p where p.personId = t.psnId)";

    Object object = this.createQuery(hql).setParameter("grpId", grpId).uniqueResult();
    if (object == null) {
      return 0L;
    } else {
      return (Long) object;
    }
  }

  /**
   * 获取我的所有待批准的群组Id
   * 
   * @param psnId
   * @param searchByRole
   * @return
   */
  public List<Long> getMyGrpIds(Long psnId, Integer grpCategory, String searchKey) {

    String hql = "select 1 from GrpBaseinfo t1 where t1.status='01' and t1.grpId=t.grpId ";
    if (StringUtils.isNotBlank(searchKey)) {
      hql += " and instr(upper(t1.grpName),upper('" + searchKey + "'))>0  ";
    }
    if (grpCategory != null && grpCategory != 0) {
      hql += " and t1.grpCategory =" + grpCategory;
    }

    String hql1 = "select t.grpId from GrpProposer t  where exists(" + hql
        + ") and t.type=1  and t.isAccept=2 and t.psnId=:psnId order by createDate desc";

    return this.createQuery(hql1).setParameter("psnId", psnId).list();
  }

  /**
   * 获取群组申请中人员列表数量
   * 
   * @param grpId
   * @return
   */
  public Long getProposerCount(Long grpId) {
    String hqlCount =
        "select count(t.id) from GrpProposer t where  t.type=1 and t.isAccept=2 and t.grpId=:grpId and exists (select 1 from Person  p where p.personId = t.psnId)";
    return (Long) this.createQuery(hqlCount).setParameter("grpId", grpId).uniqueResult();
  }

  /**
   * 获取群组申请中人员列表
   * 
   * @return
   */
  public List<GrpProposer> getProposerBySearch(GrpMemberForm form) {
    Page page = form.getPage();
    String hql =
        "select new GrpProposer(t.id,t.psnId) from GrpProposer t where  t.type=1 and t.isAccept=2 and t.grpId=:grpId and exists (select 1 from Person  p where p.personId = t.psnId)";
    return this.createQuery(hql).setParameter("grpId", form.getGrpId()).setFirstResult(page.getFirst() - 1)
        .setMaxResults(page.getPageSize()).list();
  }

  /**
   * 接受/忽略 申请、邀请 请求
   * 
   * @return
   */
  public void setAccept(Long targetPsnId, long grpId, Long currentPsnId, Integer isAccept, Integer type) {
    if (type == null || type == 0) {
      type = 1;
    }
    Date date = new Date();
    GrpProposer grpProposer = this.getByPsnIdAndGrpId(targetPsnId, grpId, type);
    if (grpProposer != null) {
      grpProposer.setIsAccept(isAccept);
      grpProposer.setApproverId(currentPsnId);
      grpProposer.setApproverDate(date);
      if (isAccept == 2) {
        grpProposer.setCreateDate(date);
      }
      this.save(grpProposer);
    }
    // 同步处理请求 ， 如果是申请，就要处理邀请中的请求，反之亦然
    if (isAccept == 1) {
      if (type == 1) {
        type = 2;
      } else {
        type = 1;
      }
      grpProposer = null;
      grpProposer = this.getByPsnIdAndGrpId(targetPsnId, grpId, type);
      if (grpProposer != null) {
        grpProposer.setIsAccept(isAccept);
        grpProposer.setApproverId(currentPsnId);
        grpProposer.setApproverDate(date);
        if (isAccept == 2) {
          grpProposer.setCreateDate(date);
        }
        this.save(grpProposer);
      }
    }

  }

  /**
   * 通过psnId和grpId获取GrpProposer对象
   * 
   * @param psnId
   */
  public GrpProposer getByPsnIdAndGrpId(Long psnId, Long grpId, Integer type) {
    String hql = "from GrpProposer t where t.type=:type and t.psnId=:psnId and t.grpId=:grpId";
    List<GrpProposer> list = this.createQuery(hql).setParameter("type", type).setParameter("psnId", psnId)
        .setParameter("grpId", grpId).list();
    if (list != null && list.size() > 0) {
      return list.get(0);
    } else {
      return null;
    }
  }

  /**
   * 通过psnId和grpId获取GrpProposer对象
   * 
   * @param psnId
   */
  public GrpProposer getByPsnIdAndGrpId(Long psnId, Long grpId, Integer type, Integer isAccept) {
    String hql = "from GrpProposer t where t.type=:type and t.psnId=:psnId and t.isAccept=:isAccept and t.grpId=:grpId";
    return (GrpProposer) this.createQuery(hql).setParameter("type", type).setParameter("psnId", psnId)
        .setParameter("isAccept", isAccept).setParameter("grpId", grpId).uniqueResult();
  }

  /**
   * 通过psnId和grpId获取GrpMember对象
   * 
   * @param psnId
   */
  public GrpProposer getGrpProposer(Long psnId, Long grpId, Integer type) {
    String hql = "from GrpProposer t where t.type=:type and t.psnId=:psnId and t.grpId=:grpId and t.isAccept=2";
    Object obj = this.createQuery(hql).setParameter("type", type).setParameter("psnId", psnId)
        .setParameter("grpId", grpId).uniqueResult();
    if (obj != null) {
      return (GrpProposer) obj;
    }
    return null;
  }

  /**
   * 添加申请中人员
   * 
   * @return
   */
  public void addGrpProposer(GrpProposer g) {
    GrpProposer grpProposer = this.getByPsnIdAndGrpId(g.getPsnId(), g.getGrpId(), g.getType());
    Date date = new Date();
    if (grpProposer != null) {
      grpProposer.setIsAccept(g.getIsAccept());
      grpProposer.setInviterId(g.getInviterId());
      if (g.getIsAccept() == 2) {
        grpProposer.setCreateDate(date);
      }
      this.save(grpProposer);
    } else {
      if (g.getIsAccept() == 2) {
        g.setCreateDate(date);
      }
      this.save(g);
    }
  }

  /**
   * 取消申请
   * 
   * @return
   */
  public Integer cancelApply(Long psnId, Long grpId) {
    return null;
  }

  public List<GrpProposer> queryAllGrpReq(Long psnId, Page page) {
    String hql = "from GrpProposer t where t.isAccept=2 and t.type=1 "
        + " and exists (select 1 from GrpMember t2 where t.grpId=t2.grpId and t2.psnId=:psnId and (t2.grpRole=1 or t2.grpRole=2) and t2.status='01') "
        + " and exists (select 1 from GrpBaseinfo t3 where t3.grpId=t.grpId and t3.status='01' )"
        + " and exists (select 1 from Person  p where p.personId = t.psnId) "
        + " order by t.createDate desc,t.id desc ";
    page.setTotalCount((Long) this.createQuery("select count(1) " + hql).setParameter("psnId", psnId).uniqueResult());
    return this.createQuery(hql).setParameter("psnId", psnId).setFirstResult(page.getFirst() - 1)
        .setMaxResults(page.getPageSize()).list();
  }

  public List<GrpProposer> queryGrpReq(Long psnId, Page page) {
    String hql = "from GrpProposer t where t.isAccept=2 and t.type=1  "
        + " and exists (select 1 from GrpMember t2 where t.grpId=t2.grpId and t2.psnId=:psnId and (t2.grpRole=1 or t2.grpRole=2) and t2.status='01') "
        + " and exists (select 1 from GrpBaseinfo t3 where t3.grpId=t.grpId and t3.status='01' )"
        + " and exists (select 1 from Person  p where p.personId = t.psnId) "
        + " order by t.createDate desc,t.id desc ";
    return this.createQuery(hql).setParameter("psnId", psnId).setMaxResults(2).list();
  }

  public List<GrpProposer> queryAllGrpInvite(Long psnId, Page page) {
    String hql = "from GrpProposer t where t.isAccept=2 and t.type=2 and t.psnId=:psnId"
        + " and exists (select 1 from GrpBaseinfo t3 where t3.grpId=t.grpId and t3.status='01' )"
        + " and exists (select 1 from Person  p where p.personId = t.inviterId) "
        + " order by t.createDate desc,t.id desc ";
    page.setTotalCount((Long) this.createQuery("select count(1) " + hql).setParameter("psnId", psnId).uniqueResult());
    return this.createQuery(hql).setParameter("psnId", psnId).setFirstResult(page.getFirst() - 1)
        .setMaxResults(page.getPageSize()).list();
  }

  public List<GrpProposer> queryGrpInvite(Long psnId, Page page) {
    String hql = "from GrpProposer t where t.isAccept=2 and t.type=2 and t.psnId=:psnId"
        + " and exists (select 1 from GrpBaseinfo t3 where t3.grpId=t.grpId and t3.status='01' )"
        + " and exists (select 1 from Person  p where p.personId = t.inviterId) "
        + " order by t.createDate desc,t.id desc ";
    return this.createQuery(hql).setParameter("psnId", psnId).setMaxResults(2).list();
  }
}
