package com.smate.center.task.service.sns.pub;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.center.task.base.TaskJobTypeConstants;
import com.smate.center.task.dao.pdwh.quartz.TmpTaskInfoRecordDao;
import com.smate.center.task.dao.sns.pub.PubSameItemDao;
import com.smate.center.task.dao.sns.pub.PubSameRecordDao;
import com.smate.center.task.model.pdwh.quartz.TmpTaskInfoRecord;
import com.smate.center.task.model.sns.pub.PubSameItem;
import com.smate.center.task.model.sns.pub.PubSameRecord;
import com.smate.center.task.v8pub.enums.PubGenreConstants;
import com.smate.core.base.pub.consts.V8pubQueryPubConst;
import com.smate.core.base.pub.dto.PubQueryDTO;
import com.smate.core.base.utils.collections.ListUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.url.RestUtils;

@Service("snsDupPubGroupingService")
@Transactional(rollbackFor = Exception.class)
public class SnsDupPubGroupingServiceImpl implements SnsDupPubGroupingService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private TmpTaskInfoRecordDao tmpTaskInfoRecordDao;
  @Autowired
  private PubSameItemDao pubSameItemDao;
  @Autowired
  private PubSameRecordDao pubSameRecordDao;
  Boolean flag = false;// 标识任务状态
  private static Integer jobType = TaskJobTypeConstants.SnsDupPubGroupingTask;
  @Value("${domainscm}")
  private String domainscm;

  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;

  @Override
  public List<TmpTaskInfoRecord> batchGetJobList(Integer size) throws Exception {
    return tmpTaskInfoRecordDao.getTaskInfoRecordList(size, jobType);
  }

  @Override
  public void startProcessing(TmpTaskInfoRecord job) throws Exception {
    Long psnId = job.getHandleId();
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    pubQueryDTO.setSearchPsnId(psnId);
    pubQueryDTO.setSelf(true);
    // 获取最新更新的成果
    pubQueryDTO.setOrderBy("gmtModified");
    pubQueryDTO.setServiceType(V8pubQueryPubConst.OPEN_PSN_PUBLIC_PUB);
    String SERVER_URL = domainscm + V8pubQueryPubConst.QUERY_PUB_URL;
    Map<String, Object> remoteInfo =
        (Map<String, Object>) RestUtils.getRemoteInfo(pubQueryDTO, SERVER_URL, restTemplate);
    List<Map<String, Object>> resultList = null;
    if (remoteInfo.get("status").equals("success")) {
      resultList = (List<Map<String, Object>>) remoteInfo.get("resultList");
    }
    // 记录和当前遍历成果重复的成果的Id
    List<Long> DupPubIds = new ArrayList<Long>();
    // 记录已经处理过的成果Id，防止重复
    List<Long> checkedId = new ArrayList<>();
    if (CollectionUtils.isEmpty(resultList)) {
      this.updateTaskStatus(job.getJobId(), 1, "该人员没有成果");
      return;
    }
    // 查询这个人成果的最后更新时间
    Date myPubNewestUpdateDate = getPsnPubLastUpdate(psnId);

    // 查询这个人的重复成果分组的最后更新时间（组的更新时间为成果的最后更新时间）;
    Date sameRecNewestUpdateDate = pubSameRecordDao.getSameRecNewestUpdateDate(psnId);
    // 如果这个人成果的最后更新时间大于组的最后更新时间则表示该人员的成果有新增或者编辑 需要重新跑分组
    if (sameRecNewestUpdateDate != null) {
      if (myPubNewestUpdateDate.getTime() <= sameRecNewestUpdateDate.getTime()) {
        this.updateTaskStatus(job.getJobId(), 1, "该人员的成果信息没有变化，不更新分组信息");
        return;
      }
    }

    for (Map<String, Object> pubInfo : resultList) {
      Long pubId = Long.parseLong(pubInfo.get("pubId").toString());
      if (checkedId.contains(pubId)) {
        continue;
      }
      DupPubIds = getDupPubIds(psnId, pubInfo);
      if (CollectionUtils.isNotEmpty(DupPubIds)) {
        // 记录已经分组的成果Id,以便跳过后面与前面成果为一组重复的
        // DupPubIds 和 checkedId 存在交集的处理
        DupPubIds.removeAll(checkedId);
        checkedId.addAll(DupPubIds);
        checkedId.add(pubId);
        this.recordDupPub(psnId, DupPubIds, pubId);
      }
    }
    if (flag) {
      flag = false;
      this.updateTaskStatus(job.getJobId(), 2, "执行成功但是存在重复的分组，重复分组数据不会被更新");
    } else {
      this.updateTaskStatus(job.getJobId(), 1, "");
    }
  }

  /**
   * 记录重复成果
   * 
   * @param psnId
   * @param dupPubIds
   */
  private void recordDupPub(Long psnId, List<Long> dupPubIds, Long pubId) {
    Integer pubCount = dupPubIds.size();
    if (pubCount > 500) {
      logger.error("这条成果的重复成果大于500条，请确认！pubId:" + pubId);
    }
    // 分组信息有改变则更新 T
    List<PubSameItem> itemInfo = pubSameItemDao.getPsnItemInfoByPubId(pubId, psnId);
    if (CollectionUtils.isNotEmpty(itemInfo)) {
      if (itemInfo.size() > 1) {
        flag = true;
        logger.error("该成果存在于2个不同的分组，请确认！pubId:" + pubId);
        return;

      }
      // 需要对分组是否有更新进行判断

      Long recordId = itemInfo.get(0).getRecordId();

      Date pubNewestUpdateDate = getPubNewestUpdateDate(psnId, dupPubIds);// 更新成果后的分组的更新时间

      Date srcupdateDate = pubSameRecordDao.getUpdateDate(recordId);// 该分组原来的更新时间
      // 如果大于则表示该分组的成果有变化，需要更新这个分组（ 有成果被删除不会更新，有状态标识）
      if (srcupdateDate != null) {
        // 这个判断有问题？？？ 2018-10-10
        if (pubNewestUpdateDate.getTime() > srcupdateDate.getTime()) {
          // 先删除旧分组，在重新保存
          pubSameRecordDao.deleteByRecordId(recordId);
          pubSameItemDao.deleteItemByRecordId(recordId);
          // 原有分组内数据变动 ，eg：原来分组有2条重复成果，编辑后在跑只一条了，需要删除原有的历史分组数据。
          if (pubCount < 2) {
            logger.error("原有分组内数据变动 ，pubId" + pubId + ".." + dupPubIds.get(0));
            return;
          }
          // 原有分组内数据变动,eg：原有分组有3条重复成果，编辑后只有2条重复的了，也需要删除，在重新保存新分组。
          PubSameRecord pubSameRecord =
              new PubSameRecord(psnId, Long.valueOf(pubCount), 0, new Date(), getPubNewestUpdateDate(psnId, dupPubIds));
          pubSameRecordDao.save(pubSameRecord);
          Long newRecordId = pubSameRecord.getRecordId();
          if (newRecordId != null) {
            for (Long dupPubId : dupPubIds) {
              pubSameItemDao.save(
                  new PubSameItem(pubSameRecord.getRecordId(), psnId, dupPubId, 0, new Date(), getPubUpdate(dupPubId)));
            }
          }

        }
      }

    } else {
      // 首次查出来只单个成果不记录分组
      if (pubCount < 2) {
        return;
      }
      // 没有记录则 但是dupPubIds 可能存在记录-2018-10-09
      for (Long dupPubId : dupPubIds) {
        if (dupPubId.longValue() == pubId.longValue()) {
          continue;
        }
        List<PubSameItem> dupItemInfo = pubSameItemDao.getPsnItemInfoByPubId(dupPubId, psnId);
        if (dupItemInfo != null && dupItemInfo.size() > 0) {
          pubSameRecordDao.deleteByRecordId(dupItemInfo.get(0).getRecordId());
          pubSameItemDao.deleteItemByRecordId(dupItemInfo.get(0).getRecordId());
        }
      }
      // 没有记录则是新的分组 直接保存
      PubSameRecord pubSameRecord =
          new PubSameRecord(psnId, Long.valueOf(pubCount), 0, new Date(), getPubNewestUpdateDate(psnId, dupPubIds));
      pubSameRecordDao.save(pubSameRecord);

      Long recordId = pubSameRecord.getRecordId();
      if (recordId != null) {
        for (Long dupPubId : dupPubIds) {
          pubSameItemDao.save(
              new PubSameItem(pubSameRecord.getRecordId(), psnId, dupPubId, 0, new Date(), getPubUpdate(dupPubId)));
        }
      }

    }

  }

  public Date getPubNewestUpdateDate(Long psnId, List<Long> dupPubIds) {
    if (CollectionUtils.isEmpty(dupPubIds)) {
      return null;
    }
    Date pubNewestUpdateDate = null;
    // 处理sql in list 长度超过1000的问题
    if (dupPubIds.size() > 1000) {
      List<Long>[] split = ListUtils.split(dupPubIds, 1000);
      for (List<Long> list : split) {
        Date time = getPubUpdateByPubIds(psnId, list);
        if (pubNewestUpdateDate == null) {
          pubNewestUpdateDate = time;
        } else if (time != null && time.getTime() > pubNewestUpdateDate.getTime()) {
          pubNewestUpdateDate = time;
        }

      }
    } else {
      pubNewestUpdateDate = getPubUpdateByPubIds(psnId, dupPubIds);
    }
    return pubNewestUpdateDate;
  }

  @Override
  public void updateTaskStatus(Long jobId, int status, String err) {
    try {
      tmpTaskInfoRecordDao.updateTaskStatusById(jobId, status, err);
    } catch (Exception e) {
      logger.error("更新任务状态记录出错！jobId " + jobId, e);
    }
  }


  /**
   * 获取个人成果最新的更新时间,接口已测
   * 
   * @return
   */
  public Date getPsnPubLastUpdate(Long psnId) {
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    pubQueryDTO.setSearchPsnId(psnId);
    String SERVER_URL = domainscm + V8pubQueryPubConst.QUERY_PUB_URL;
    pubQueryDTO.setServiceType(V8pubQueryPubConst.QUERY_LAST_UPDATE_PUB);
    Map<String, Object> remoteInfo =
        (Map<String, Object>) RestUtils.getRemoteInfo(pubQueryDTO, SERVER_URL, restTemplate);
    List<Map<String, Object>> resultList = null;
    if (remoteInfo.get("status").equals("success")) {
      resultList = (List<Map<String, Object>>) remoteInfo.get("resultList");
      if (CollectionUtils.isNotEmpty(resultList)) {
        Object gmtModified = resultList.get(0).get("gmtModified");
        if (gmtModified != null && StringUtils.isNotBlank(gmtModified.toString())) {
          return new Date(Long.parseLong(gmtModified.toString()));
        }
      }
    }
    return null;
  }

  /**
   * 获取个人成果最新的更新时间
   * 
   * @return
   */
  public Date getPubUpdate(Long pubId) {
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    pubQueryDTO.setSearchPubId(pubId);;
    String SERVER_URL = domainscm + V8pubQueryPubConst.QUERY_PUB_URL;
    pubQueryDTO.setServiceType(V8pubQueryPubConst.PUB_QUERY_BY_PUB_ID);
    Map<String, Object> remoteInfo =
        (Map<String, Object>) RestUtils.getRemoteInfo(pubQueryDTO, SERVER_URL, restTemplate);
    List<Map<String, Object>> resultList = null;
    if (remoteInfo.get("status").equals("success")) {
      resultList = (List<Map<String, Object>>) remoteInfo.get("resultList");
      if (CollectionUtils.isNotEmpty(resultList)) {
        Object gmtModified = resultList.get(0).get("gmtModified");
        if (gmtModified != null && StringUtils.isNotBlank(gmtModified.toString())) {
          return new Date(Long.parseLong(gmtModified.toString()));
        }
      }
    }
    return null;
  }

  /**
   * 获取个人成果最新的更新时间
   * 
   * @return
   */
  public Date getPubUpdateByPubIds(Long psnId, List<Long> pubIds) {
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    pubQueryDTO.setSearchPubIdList(pubIds);
    pubQueryDTO.setSearchPsnId(psnId);
    String SERVER_URL = domainscm + V8pubQueryPubConst.QUERY_PUB_URL;
    pubQueryDTO.setServiceType(V8pubQueryPubConst.QUERY_LAST_UPDATE_PUB_BY_PUBIDS);
    Map<String, Object> remoteInfo =
        (Map<String, Object>) RestUtils.getRemoteInfo(pubQueryDTO, SERVER_URL, restTemplate);
    List<Map<String, Object>> resultList = null;
    if (remoteInfo.get("status").toString().equalsIgnoreCase("success")) {
      resultList = (List<Map<String, Object>>) remoteInfo.get("resultList");
      if (CollectionUtils.isNotEmpty(resultList)) {
        Object gmtModified = resultList.get(0).get("gmtModified");
        if (gmtModified != null && StringUtils.isNotBlank(gmtModified.toString())) {
          return new Date(Long.parseLong(gmtModified.toString()));
        }
      }
    }
    return null;
  }


  /**
   * 获取重复成果ids，已测试正常 title pubYear pubType doi成果doi sourceId成果的sourceId applicationNo专利申请号
   * publicationOpenNo 专利公开（公告）号
   * 
   * @param psnId
   * @return
   */
  public List<Long> getDupPubIds(Long psnId, Map<String, Object> pubInfo) {


    String title = pubInfo.get("title") == null ? "" : pubInfo.get("title").toString();
    String DOI = pubInfo.get("doi") == null ? "" : pubInfo.get("doi").toString();
    Integer pubType = pubInfo.get("pubType") == null ? 7 : Integer.parseInt(pubInfo.get("pubType").toString());
    Integer pubYear =
        pubInfo.get("publishYear") == null ? null : Integer.parseInt(pubInfo.get("publishYear").toString());
    Integer srcDbId = pubInfo.get("srcDbId") == null ? null : Integer.parseInt(pubInfo.get("srcDbId").toString());
    String sourceId = pubInfo.get("sourceId") == null ? "" : pubInfo.get("sourceId").toString();
    String applicationNo = pubInfo.get("applicationNo") == null ? "" : pubInfo.get("applicationNo").toString();
    String publicationOpenNo =
        pubInfo.get("publicationOpenNo") == null ? "" : pubInfo.get("publicationOpenNo").toString();
    String standardNo = pubInfo.get("standardNo") == null ? "" : pubInfo.get("standardNo").toString();
    String registerNo = pubInfo.get("registerNo") == null ? "" : pubInfo.get("registerNo").toString();

    List<Long> dupPubIds = new ArrayList<>();
    Map<String, Object> parmaMap = new HashMap<>();
    parmaMap.put("pubGener", PubGenreConstants.PSN_PUB);
    parmaMap.put("title", title);
    parmaMap.put("pubYear", pubYear);
    parmaMap.put("pubType", pubType);
    parmaMap.put("sourceId", sourceId);
    parmaMap.put("srcDbId", srcDbId);
    parmaMap.put("applicationNo", applicationNo);
    parmaMap.put("publicationOpenNo", publicationOpenNo);
    parmaMap.put("standardNo", standardNo);
    parmaMap.put("registerNo", registerNo);
    parmaMap.put("doi", DOI);
    parmaMap.put("des3PsnId", Des3Utils.encodeToDes3(psnId.toString()));
    String SERVER_URL = domainscm + V8pubQueryPubConst.PUB_DUPCHECK_URL;
    Map<String, Object> remoteInfo = (Map<String, Object>) RestUtils.getRemoteInfo(parmaMap, SERVER_URL, restTemplate);
    if ("SUCCESS".equalsIgnoreCase(remoteInfo.get("status").toString())) {
      Object msgList = remoteInfo.get("msgList");
      if (msgList != null) {
        String[] split = msgList.toString().split(",");
        if (split != null && split.length > 0) {
          for (String pubId : split) {
            if (StringUtils.isNotBlank(pubId) && NumberUtils.isCreatable(pubId)) {
              dupPubIds.add(NumberUtils.toLong(pubId));
            }
          }
        }
      }
    }
    return dupPubIds;
  }


  public static void main(String[] args) {
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    List<Long> list = new ArrayList<>();
    list.add(123123L);
    list.add(12312323L);
    pubQueryDTO.setSearchPubIdList(list);
    System.out.println(JacksonUtils.jsonObjectSerializer(pubQueryDTO));
  }


}
