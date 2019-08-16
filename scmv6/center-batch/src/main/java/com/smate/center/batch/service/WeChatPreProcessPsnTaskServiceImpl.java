package com.smate.center.batch.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.connector.dao.job.BatchJobsDao;
import com.smate.center.batch.connector.model.job.BatchJobs;
import com.smate.center.batch.constant.BatchConfConstant;
import com.smate.center.batch.constant.BatchJobDetailConstant;
import com.smate.center.batch.dao.sns.wechat.OpenUserUnionDao;
import com.smate.center.batch.dao.sns.wechat.WeChatMessageHistoryPsnDao;
import com.smate.center.batch.dao.sns.wechat.WeChatMessagePsnDao;
import com.smate.center.batch.dao.sns.wechat.WeChatMessagePublicDao;
import com.smate.center.batch.dao.sns.wechat.WeChatPreProcessPsnDao;
import com.smate.center.batch.model.sns.wechat.WeChatMessageHistoryPsn;
import com.smate.center.batch.model.sns.wechat.WeChatMessagePsn;
import com.smate.center.batch.model.sns.wechat.WeChatPreProcessPsn;
import com.smate.core.base.utils.dao.wechat.WeChatRelationDao;
import com.smate.core.base.utils.exception.BatchTaskException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.wechat.OpenUserUnion;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 微信消息去重任务业务service接口实现
 * 
 * @author hzr
 * @version 6.0.1
 * 
 */
@Service("weChatPreProcessPsnTaskService")
@Transactional(rollbackFor = Exception.class)
public class WeChatPreProcessPsnTaskServiceImpl implements WeChatPreProcessPsnTaskService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private WeChatMessagePublicDao weChatMessagePublicDao;

  @Autowired
  private WeChatMessagePsnDao weChatMessagePsnDao;

  @Autowired
  private WeChatPreProcessPsnDao weChatPreProcessPsnDao;

  @Autowired
  private WeChatMessageHistoryPsnDao weChatMessageHistoryPsnDao;

  @Autowired
  private WeChatRelationDao weChatRelationDao;

  @Autowired
  private OpenUserUnionDao openUserUnionDao;

  @Autowired
  private BatchJobsDao batchJobsDao;

  /**
   * 根据状态查询微信消息表中记录
   * 
   * @param Long status 处理状态
   * @return List<WeChatMessagePsn>
   * @version 6.0.1
   */
  @Override
  public List<WeChatMessagePsn> getWeChatMessagePsnListByStatus(Integer status) {
    List<WeChatMessagePsn> resultList = new ArrayList<WeChatMessagePsn>();
    resultList = weChatMessagePsnDao.getMsgByStatus(status);
    return resultList;
  }

  /**
   * 微信消息查重
   * 
   * @param WeChatMessagePsn weChatMessagePsn 消息实体
   * @return boolean
   * @version 6.0.1
   */
  @Override
  public boolean checkDuplicate(WeChatMessagePsn weChatMessagePsn) throws BatchTaskException {
    // WeChatMessagePsn每天会固定清理一次，所以只要是相同内容，发送给同一个人openid的，都算作重复信息
    Long openId = weChatMessagePsn.getOpenId();
    String content = weChatMessagePsn.getContent();

    String contentMd5 = DigestUtils.md5Hex(content);
    // TODO 暂时与preproces表中比较，如果需要把已经发送的任务从preprocess表中删除，就需要与历史表比较
    Object object = weChatPreProcessPsnDao.getWeChatPreProcessPsn(openId, contentMd5);
    if (object == null) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * 查重记录后，插入V_BATCH_PRE_WECHAT_PSN表中
   * 
   * @param WeChatMessagePsn weChatMessagePsn 消息实体
   * @param Long id V_WECHAT_MESSAGE_PSN表主键id
   * @param String content 消息内容
   * @param String contentMd5 消息Md5码
   * @param Long openId 第三方用户来源标识
   * @param Date createTime 创建时间
   * @param Long psnId 发送对象scm中psnid
   * @param String token 第三方系统id
   * @return
   * @version 6.0.1
   */
  @Override
  public void saveToPreProcess(WeChatMessagePsn weChatMessagePsn, Long id, String content, String contentMd5,
      Long openId, Date createTime, Long psnId, String token) throws BatchTaskException {
    try {
      Date date = new Date();
      DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

      WeChatPreProcessPsn oneHis = new WeChatPreProcessPsn();

      oneHis.setId(id);
      oneHis.setContent(content);
      oneHis.setContentMd5(contentMd5);
      oneHis.setOpenId(openId);
      oneHis.setToken(token);
      oneHis.setCreateTime(createTime);
      oneHis.setPsnId(psnId);
      oneHis.setUpdateTime(date);
      oneHis.setStatus(0);
      oneHis.setDay(format.format(date));
      weChatPreProcessPsnDao.save(oneHis);
      // 在WeChatMessagePsn中标记已经处理
      weChatMessagePsn.setStatus(1);
      weChatMessagePsnDao.save(weChatMessagePsn);

    } catch (Exception e) {
      weChatMessagePsn.setStatus(2);
      weChatMessagePsnDao.save(weChatMessagePsn);
      // throw new BatchTaskException(e);
      logger.error("saveToPreProcess出错：", e);
    }

  }

  /**
   * 记录插入历史表中
   * 
   * @param WeChatMessagePsn weChatMessagePsn 消息实体
   * @param Long id V_WECHAT_MESSAGE_PSN表主键id
   * @param String content 消息内容
   * @param Long openId 第三方用户来源标识
   * @param Date createTime 创建时间
   * @param Long psnId 发送对象scm中psnid
   * @param String token 第三方系统id
   * @return
   * @version 6.0.1
   */
  @Override
  public void saveToHistory(WeChatMessagePsn weChatMessagePsn, Long id, String content, String contentMd5, Long openId,
      Date createTime, Long psnId, String token) throws BatchTaskException {
    try {
      // Long openId = weChatMessagePsn.getOpenId();
      // String content = weChatMessagePsn.getContent();
      // String contentMd5 = DigestUtils.md5Hex(content);

      WeChatMessageHistoryPsn oneHis = weChatMessageHistoryPsnDao.getWeChatMessageHistoryPsn(openId, contentMd5);
      // 如果已经有记录，重复数+1
      if (oneHis != null) {
        Long dupCount = (oneHis.getDuplicateCounts() == null) ? 0 : (oneHis.getDuplicateCounts());
        oneHis.setDuplicateCounts(dupCount + 1);
        oneHis.setUpdateTime(new Date());
        weChatMessageHistoryPsnDao.save(oneHis);
        // 在WeChatMessagePsn中标记已经处理
        weChatMessagePsn.setStatus(1);
        weChatMessagePsnDao.save(weChatMessagePsn);
      } else {
        // Long psnId = openUserUnionDao.getPsnIdByOpenId(openId);
        if (psnId == null) {
          throw new BatchTaskException("WeChatPreProcessPsnTask中weChatMessagePsn对象的OpenId对应的psnId为空，id："
              + weChatMessagePsn.getId() + "; openId: " + openId);
        }
        oneHis = new WeChatMessageHistoryPsn();
        oneHis.setId(id);
        oneHis.setContent(content);
        oneHis.setContentMd5(contentMd5);
        oneHis.setOpenId(openId);
        oneHis.setToken(token);
        oneHis.setCreateTime(createTime);
        oneHis.setPsnId(psnId);
        oneHis.setStatus(0);
        oneHis.setUpdateTime(new Date());
        weChatMessageHistoryPsnDao.save(oneHis);
        // 在WeChatMessagePsn中标记已经处理
        weChatMessagePsn.setStatus(1);
        weChatMessagePsnDao.save(weChatMessagePsn);
      }
    } catch (Exception e) {
      weChatMessagePsn.setStatus(2);
      weChatMessagePsnDao.save(weChatMessagePsn);
      // throw new BatchTaskException(e);
      logger.error("saveToHistory出错：", e);
    }
  }

  /**
   * 向V_BATCH_JOBS总表中插入记录
   * 
   * @param WeChatMessagePsn weChatMessagePsn 消息实体
   * @return
   * @version 6.0.1
   */
  @Override
  public void saveToBatchJobs(WeChatMessagePsn weChatMessagePsn) throws BatchTaskException {

    try {
      // 把PreProcess中的主键加入batch_job的jason数据格式中
      BatchJobs newJob = new BatchJobs();
      Map<String, Long> jobContext = new HashMap<String, Long>();
      jobContext.put("msg_id", weChatMessagePsn.getId());
      String jason = JacksonUtils.mapToJsonStr(jobContext);
      newJob.setJobContext(jason);

      // 加入数据来源,设置任务优先级
      newJob.setStrategy(BatchJobDetailConstant.WECHATE_MSG_PSN);
      newJob.setWeight(BatchConfConstant.TASK_WEIGHT_A);
      newJob.setStatus(0);
      batchJobsDao.save(newJob);
    } catch (Exception e) {
      weChatMessagePsn.setStatus(2);
      weChatMessagePsnDao.save(weChatMessagePsn);
      // throw new BatchTaskException(e);
      logger.error("saveToBatchJobs出错：", e);
    }
  }

  /**
   * 通过第三方系统openid与token查询psnid
   * 
   * @param Long openId 第三方用户来源标识
   * @param String token 第三方系统id
   * @return Long psnid
   * @version 6.0.1
   */
  @Override
  public Long getPsnIdByOpenId(Long openId, String token) throws BatchTaskException {
    OpenUserUnion result = new OpenUserUnion();
    result = openUserUnionDao.getOpenUserUnion(openId, token);
    if (result == null) {
      return null;
    } else {
      Long psnId = result.getPsnId();
      return psnId;
    }
  }

  /**
   * 保存到微信消息表
   * 
   * @param WeChatMessagePsn weChatMessagePsn 消息实体
   * @return
   * @version 6.0.1
   */
  @Override
  public void saveToWebChatMessagePsn(WeChatMessagePsn weChatMessagePsn) {
    if (weChatMessagePsn != null) {
      weChatMessagePsnDao.save(weChatMessagePsn);
    } else {
      return;
    }
  }



}
