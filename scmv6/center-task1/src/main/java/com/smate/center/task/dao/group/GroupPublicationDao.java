package com.smate.center.task.dao.group;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.group.EmailGroupPubPsn;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class GroupPublicationDao extends SnsHibernateDao<EmailGroupPubPsn, Long> {

  // 把当天新增成果到群组的信息更新到AddGroupPubEmail表中
  public int updateAddGroupPubEmail() {
    // 由于从成果库导入的成果的create_date是有可能很久之前的，所以当天导入到群组的成果不能根据时间等于当天来获得,获取当天前的群组成果新增或者录入的最后一条
    String hql1 = "select p.groupPubsId from GroupPubs p,Publication p2 where p2.pubId = p.pubId "
        + "and p2.articleType = 1 and to_char(p2.createDate,'YYYY-MM-dd')<to_char(sysdate-1,'YYYY-MM-dd')  order by p2.createDate desc ";
    List<Long> groupPubId = super.createQuery(hql1).setMaxResults(1).list();

    String hql =
        "select  p.ownerPsnId as psnId, p.groupId as groupId, p.pubId as pubId, p2.createDate as createDate from GroupPubs p, Publication p2"
            + "  where p2.pubId = p.pubId  and p.groupPubsId>:groupPubId and"
            + " not exists(select 1 from EmailGroupPubPsn a where a.pubId=p.pubId and a.groupId=p.groupId and a.psnId=p.ownerPsnId) order by p.groupPubsId desc";

    List<Map<String, Object>> addedPubList = super.createQuery(hql).setParameter("groupPubId", groupPubId.get(0))
        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    if (addedPubList == null || addedPubList.size() < 1) {
      return 0;
    } else {
      for (Map<String, Object> pub : addedPubList) {
        EmailGroupPubPsn addGroupPubEmail = new EmailGroupPubPsn();
        addGroupPubEmail.setPsnId(NumberUtils.toLong(pub.get("psnId").toString()));
        addGroupPubEmail.setGroupId(NumberUtils.toLong(pub.get("groupId").toString()));
        addGroupPubEmail.setCreateDate((Date) pub.get("createDate"));
        addGroupPubEmail.setPubId(NumberUtils.toLong(pub.get("pubId").toString()));
        addGroupPubEmail.setStatus("0");
        super.save(addGroupPubEmail);
      }
      return addedPubList.size();
    }

  }

  // 处理完后发送更新状态为1
  public void updateGroupPubEmail(Long pubId, Long groupId) {
    String updateString = "update EmailGroupPubPsn set status='1' where pubId=? and groupId=?";
    super.createQuery(updateString, pubId, groupId).executeUpdate();
  }

  /**
   * 获取当天状态为0在群组中增添了成果的人员id列表
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getAddGroupPubEmail() {
    String sql = "select psnId from EmailGroupPubPsn where status=0  group by psnId";
    return super.createQuery(sql).list();
  }


  /**
   * 根据psnId获取状态为0今天新增了成果的群组
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getAddGroup(Long psnId) {
    String sql = "select groupId from EmailGroupPubPsn where status=0  and psnId=:psnId group by groupId";
    return super.createQuery(sql).setParameter("psnId", psnId).list();
  }

  /**
   * 
   * @param pubId
   * @param groupId 根据pubid groupid获取成果添加人员的PsnId
   */

  @SuppressWarnings("unchecked")
  public List<Long> getAddPsn(Long pubId, Long groupId) {
    String sql = "select ownerPsnId from GroupPubs where pubId=:pubId and groupId=:groupId";
    return super.createQuery(sql).setParameter("pubId", pubId).setParameter("groupId", groupId).list();
  }



  /**
   * 根据psnId和groupId获取当天A人员在B群组添加的成果id列表
   * 
   * @param psnId
   * @param groupId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getAddGroupPub(Long psnId, Long groupId) {
    String sql =
        "select pubId from EmailGroupPubPsn where status=0  and psnId=:psnId and groupId=:groupId order by id desc";
    return super.createQuery(sql).setParameter("psnId", psnId).setParameter("groupId", groupId).list();
  }

}
