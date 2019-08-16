package com.smate.center.batch.dao.pdwh.pub;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.model.pdwh.pub.MongoPdwhPubAuthorSnsPsnRecord;
import com.smate.core.base.exception.DAOException;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.BaseMongoDAO;

/**
 * @Author LIJUN
 * @Description MongoDB PdwhPubAuthorSnsPsnRecord 数据操作 Dao
 * @Date 14:31 2018/6/28
 **/

@Repository
public class MongoPdwhPubAuthorSnsPsnRecordDao extends BaseMongoDAO<MongoPdwhPubAuthorSnsPsnRecord> {
  protected SessionFactory sessionFactory;
  @Resource(name = "sessionFactoryMap")
  private Map<String, SessionFactory> sessionFactoryMap; // 装载数据源
  protected Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 根据pubId删除记录
   *
   * @param pubId
   * @author LIJUN
   * @date 2018年3月20日
   */

  public void deleteRecordByPubId(Long pubId) {
    Query myQuery = new Query();
    myQuery.addCriteria(Criteria.where("pubId").is(pubId));
    super.remove(myQuery);
  }

  /**
   * @return java.util.List<com.smate.core.base.pub.model.pdwh.MongoPdwhPubAuthorSnsPsnRecord>
   * @Author LIJUN
   * @Description 通过pub_id和人名获取记录
   * @Date 14:26 2018/6/28
   * @Param [pubId, matchName]
   **/

  public List<MongoPdwhPubAuthorSnsPsnRecord> findRecByPubIdAndName(Long pubId, String matchName) {
    Query myQuery = new Query();
    myQuery
        .addCriteria(Criteria.where("pubId").is(pubId).and("psnName").is(matchName).and("status").in(new int[] {3, 4}));
    List<MongoPdwhPubAuthorSnsPsnRecord> resultList = super.find(myQuery, MongoPdwhPubAuthorSnsPsnRecord.class);
    return resultList;
  }

  /**
   * 更新记录
   *
   * @param id
   * @author LIJUN
   * @date 2018年3月29日
   */
  public void updateTime(Long id) {
    Query myQuery = new Query();
    myQuery.addCriteria(Criteria.where("id").is(id));
    MongoPdwhPubAuthorSnsPsnRecord recordById = super.findById(id);
    if (recordById == null) {
      return;
    }
    recordById.setUpdateTime(new Date());
    super.update(recordById);

  }

  /**
   * 删除不在姓名列表中
   *
   * @param pubId
   * @param list
   * @author LIJUN
   * @date 2018年3月29日
   */

  public void deleteRecordByPubIdAndNameList(Long pubId, List<String> list) {
    Query myQuery = new Query();
    myQuery.addCriteria(Criteria.where("pubId").is(pubId).and("psnName").not().in(list));
    super.remove(myQuery);
  }

  /**
   * 删除没有被用户确认的记录
   *
   * @param pubId
   * @author LIJUN
   * @date 2018年3月30日
   */

  public void deleteUnconfirmedRecord(Long pubId) {
    Query myQuery = new Query();
    myQuery.addCriteria(Criteria.where("pubId").is(pubId).and("status").not().in(new int[] {3, 4}));
    super.remove(myQuery);
  }

  /**
   * 查询记录
   *
   * @param pubId
   * @param psnId
   * @param insId
   * @param psnName
   * @param nameType
   * @return
   * @author LIJUN
   * @date 2018年5月23日
   */

  public MongoPdwhPubAuthorSnsPsnRecord getPsnRecord(Long pubId, Long psnId, Long insId, String psnName,
      Integer nameType) {
    Query myQuery = new Query();
    myQuery.addCriteria(Criteria.where("pubId").is(pubId).and("insId").is(insId).and("psnName").is(psnName).and("psnId")
        .is(psnId).and("nameType").is(nameType));
    List<MongoPdwhPubAuthorSnsPsnRecord> resultList = super.find(myQuery, MongoPdwhPubAuthorSnsPsnRecord.class);
    if (CollectionUtils.isEmpty(resultList)) {
      return null;
    }
    return resultList.get(0);
  }

  /**
   * 获取被确认的记录
   *
   * @param pubId
   * @param matchName
   * @return
   * @author LIJUN
   * @date 2018年3月30日
   */
  public List<MongoPdwhPubAuthorSnsPsnRecord> findConfirmRecByPubIdAndName(Long pubId, String matchName) {
    Query myQuery = new Query();
    myQuery.addCriteria(
        Criteria.where("pub_Id").is(pubId).and("psn_name").is(matchName).and("status").in(new int[] {3, 4}));
    List<MongoPdwhPubAuthorSnsPsnRecord> resultList = super.find(myQuery, MongoPdwhPubAuthorSnsPsnRecord.class);
    return resultList;

  }

  /**
   * @return void
   * @Author LIJUN
   * @Description //保存新的记录
   * @Date 14:28 2018/6/28
   * @Param [pubId, psnId, matchName, insId, insName, status, date, type]
   **/
  public void saveNewPsnRecord(Long pubId, Long psnId, String matchName, Long insId, String insName, int status,
      Date date, Integer type) {
    super.save(
        new MongoPdwhPubAuthorSnsPsnRecord(getNextVal(), pubId, psnId, matchName, insId, insName, status, date, type));

  }

  /**
   * @Author LIJUN
   * @Description 保存新记录，自动生成id
   * @Date 16:42 2018/6/28
   * @Param [record]
   * @return void
   **/
  public void save(MongoPdwhPubAuthorSnsPsnRecord record) {
    record.setId(getNextVal());
    super.save(record);
  }

  /**
   * @Author LIJUN
   * @Description //获取SEQ_PDWH_AUTH_SNS_PSN_RECORD.nextval 序列
   * @Date 16:38 2018/6/28
   * @Param []
   * @return java.lang.Long
   **/
  public Long getNextVal() throws DAOException {
    BigDecimal seq = null;
    try {
      String sql = "select SEQ_PDWH_AUTH_SNS_PSN_RECORD.nextval from dual";
      Session session = getSession(DBSessionEnum.PDWH);
      SQLQuery sqlQuery = session.createSQLQuery(sql);
      seq = (BigDecimal) sqlQuery.uniqueResult();
    } catch (Exception e) {
      logger.error("获取oracle SEQ_PDWH_AUTH_SNS_PSN_RECORD.nextval 出错", e);
      throw new DAOException(e);
    }
    if (seq != null && seq.longValue() > 0) {
      return seq.longValue();
    }
    return null;
  }

  protected Session getSession(DBSessionEnum dbSessionEnum) {
    this.sessionFactory = sessionFactoryMap.get(dbSessionEnum.toString());
    return sessionFactory.getCurrentSession();
  }

  /**
   * 新开启事务保存
   * 
   * @param pdwhPubAuthorSnsPsnRecord
   * @author LIJUN
   * @date 2018年7月9日
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void saveWithNewTransaction(MongoPdwhPubAuthorSnsPsnRecord pdwhPubAuthorSnsPsnRecord) {
    this.save(pdwhPubAuthorSnsPsnRecord);
  }
}
