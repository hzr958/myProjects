package com.smate.web.management.dao.mail;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.mail.connector.model.MailOriginalData;
import com.smate.core.base.utils.data.BaseMongoDAO;
import com.smate.core.base.utils.model.Page;
import com.smate.web.management.model.mongodb.mail.MailReturnedRecord;

/**
 * 邮件发送失败记录Dao
 *
 * @author zzx
 *
 */
@Repository
public class MailReturnedRecordDao extends BaseMongoDAO<MailReturnedRecord> {

  public List<MailReturnedRecord> findList(Page<MailOriginalData> page, String account, String address,
      Date sendDateStart, Date sendDateEnd) {
    Criteria criteria = null;
    if (StringUtils.isNotBlank(account) && StringUtils.isNotBlank(address)) {
      criteria = Criteria.where("account").is(account).and("address").is(address).and("sendDate").gt(sendDateStart)
          .lt(sendDateEnd);
    }
    if (StringUtils.isBlank(account) && StringUtils.isNotBlank(address)) {
      criteria = Criteria.where("address").is(address).and("sendDate").gt(sendDateStart).lt(sendDateEnd);
    }
    if (StringUtils.isNotBlank(account) && StringUtils.isBlank(address)) {
      criteria = Criteria.where("account").is(account).and("sendDate").gt(sendDateStart).lt(sendDateEnd);
    }
    if (StringUtils.isBlank(account) && StringUtils.isBlank(address)) {
      criteria = Criteria.where("sendDate").gt(sendDateStart).lt(sendDateEnd);
    }

    Query myQuery = new Query();
    Query myQueryCount = new Query();
    if (criteria != null) {
      myQueryCount.addCriteria(criteria);
      myQuery.addCriteria(criteria).skip((page.getParamPageNo() - 1) * page.getPageSize()).limit(page.getPageSize())
          .with(new Sort(Sort.Direction.DESC, "sendDate"));
    } else {
      myQuery.skip((page.getParamPageNo() - 1) * page.getPageSize()).limit(page.getPageSize());
    }
    page.setTotalCount(super.count(myQueryCount));
    page.setTotalPages(page.getTotalPages());
    return super.find(myQuery);
  }
}
