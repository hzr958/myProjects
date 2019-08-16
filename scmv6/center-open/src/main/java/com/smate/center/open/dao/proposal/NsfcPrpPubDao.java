package com.smate.center.open.dao.proposal;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.open.model.proposal.NsfcPrpPub;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;


/**
 * 
 * 杰青论著Dao.
 * 
 * @author tsz
 * 
 */
@Repository
public class NsfcPrpPubDao extends HibernateDao<NsfcPrpPub, Long> {
  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }

  /**
   * 删除
   * 
   * @param isisGuids
   * 
   */
  public void deletePrpPub(String isisGuids) {

    if (StringUtils.isBlank(isisGuids)) {
      return;
    }
    List<String> guidList = new ArrayList<String>();
    for (String guid : isisGuids.split(",")) {
      guidList.add(guid);
    }
    StringBuffer sb = new StringBuffer();
    sb.append("delete NsfcPrpPub t  where t.isisGuid in(:guidList)");
    super.createQuery(sb.toString()).setParameterList("guidList", guidList).executeUpdate();

  }

  @SuppressWarnings("unchecked")
  public List<NsfcPrpPub> getPubsByGuid(String isisGuid, Long psnId) throws Exception {

    String hql =
        " Select t from NsfcPrpPub t where t.pubId is not null and exists(select p.isisGuid from NsfcProposal p where p.isisGuid = t.isisGuid and p.isisGuid = ? and p.prpPsnId = ?) order by t.seqNo,pType,t.pubYear desc,t.pubMonth desc,t.pubDay desc";
    List<NsfcPrpPub> queryList = super.createQuery(hql, new Object[] {isisGuid, psnId}).list();

    return queryList;

  }

  @SuppressWarnings("unchecked")
  public List<NsfcPrpPub> getPubsOrderByType(String isisGuid, Long psnId) throws Exception {

    String hql =
        " Select t from NsfcPrpPub t where t.pubId is not null and exists(select p.isisGuid from NsfcProposal p where p.isisGuid = t.isisGuid and p.isisGuid = ? and p.prpPsnId = ?) order by pType,t.seqNo,t.pubYear desc,t.pubMonth desc,t.pubDay desc";
    List<NsfcPrpPub> queryList = super.createQuery(hql, new Object[] {isisGuid, psnId}).list();

    return queryList;

  }
}
