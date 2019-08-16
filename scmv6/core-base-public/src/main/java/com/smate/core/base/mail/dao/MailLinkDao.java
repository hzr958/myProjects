package com.smate.core.base.mail.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.mail.model.mongodb.MailLink;
import com.smate.core.base.utils.data.BaseMongoDAO;
import com.smate.core.base.utils.model.Page;

/**
 * 邮件链接dao
 * 
 * @author zzx
 *
 */
@Repository
public class MailLinkDao extends BaseMongoDAO<MailLink> {
  @Autowired
  private MongoTemplate mongoTemplate;

  public Boolean checkShortUrlIsExists(String shortUrl) {
    Query myQuery = new Query();
    myQuery.addCriteria(Criteria.where("linkId").is(shortUrl));
    MailLink mailLink = super.findOne(myQuery);
    if (mailLink != null) {
      return true;
    }
    return false;
  }

  public MailLink findByShortUrl(String shortUrl) {
    Query myQuery = new Query();
    myQuery.addCriteria(Criteria.where("linkId").is(shortUrl));
    return super.findOne(myQuery);
  }

  public List<MailLink> findListByMailId(Long mailId, Page page) {
    Query myQuery = new Query();
    myQuery.addCriteria(Criteria.where("mailId").is(mailId)).skip((page.getPageNo() - 1) * page.getPageSize())
        .limit(page.getPageSize());
    return super.find(myQuery);
  }

  @SuppressWarnings("unchecked")
  public List<Map<String, Integer>> countLinks(Integer templateCode, Date startCreateDate, Date endCreateDate,
      Page<MailLink> page) {
    MatchOperation match = Aggregation.match(
        Criteria.where("templateCode").is(templateCode).and("createDate").gte(startCreateDate).lte(endCreateDate));
    GroupOperation group = Aggregation.group("templateCode", "linkKey").sum("count").as("linksum").first("urlDesc")
        .as("urlDesc").last("templateCode").as("templateCode");
    Aggregation aggregation = Aggregation.newAggregation(match, group);

    // 执行操作
    AggregationResults<MailLink> aggregationResults =
        this.mongoTemplate.aggregate(aggregation, "V_MAIL_LINK", MailLink.class);
    List<Map<String, Integer>> agg = (List<Map<String, Integer>>) aggregationResults.getRawResults().get("result");
    // page.setTotalCount(agg.size());
    return agg;
  }

}
