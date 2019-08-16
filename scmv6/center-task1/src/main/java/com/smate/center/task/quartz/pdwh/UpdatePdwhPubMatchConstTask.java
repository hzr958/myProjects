package com.smate.center.task.quartz.pdwh;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.SingleTaskException;
import com.smate.center.task.psn.model.PersonPmName;
import com.smate.center.task.service.pdwh.quartz.UpdatePdwhPubMatchInfoService;
import com.smate.center.task.utils.MessageDigestUtils;
import com.smate.core.base.solr.service.SolrIndexService;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.model.security.Person;

/**
 * 更新基准库成果地址常量，人员别名常量任务
 *
 * @author LIJUN
 * @date 2018年3月27日
 */
public class UpdatePdwhPubMatchConstTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final static Integer SIZE = 1000; // 每次处理的个数

  public UpdatePdwhPubMatchConstTask() {
    super();
  }

  public UpdatePdwhPubMatchConstTask(String beanName) {
    super(beanName);
  }

  @Autowired
  private UpdatePdwhPubMatchInfoService updatePdwhPubMatchInfoService;
  @Autowired
  private SolrIndexService solrIndexSerivce;

  public void doRun() throws SingleTaskException {
    if (!super.isAllowExecution()) {
      return;

    }

    List<Long> constIds = updatePdwhPubMatchInfoService.getNeedUpdateAddrConstId(SIZE);
    List<Long> psnIds = updatePdwhPubMatchInfoService.getNeedUpdatePsnConstId(SIZE);

    if (CollectionUtils.isEmpty(constIds) && CollectionUtils.isEmpty(psnIds)) {
      return;
    }

    for (Long constId : constIds) {
      try {
        updatePdwhPubMatchInfoService.startUpdateAddrConst(constId);
      } catch (Exception e) {
        updatePdwhPubMatchInfoService.updateStatus(constId, 1, 2);
        logger.error("更新地址常量表出错，constId:" + constId, e);
      }
    }
    for (Long psnId : psnIds) {
      try {
        Person person = updatePdwhPubMatchInfoService.getPersonIns(psnId);
        if (person == null) {
          logger.error("获取不到人员信息，psnId:" + psnId);
          updatePdwhPubMatchInfoService.updateStatus(psnId, 2, 1);
          continue;
        }
        try {
          // 先生成别名
          updatePdwhPubMatchInfoService.generalPsnPmName(person);
          // 删除没有确认的记录
          updatePdwhPubMatchInfoService.deleteUnconfirmedRecordByPsn(person.getPersonId());
          // 更新匹配记录
          updatePdwhPubMatchInfoService.updatePubAuthorSnsPsnRecord(person);

          List<PersonPmName> psnPmName = updatePdwhPubMatchInfoService.getPsnPmName(person.getPersonId());
          if (CollectionUtils.isNotEmpty(psnPmName)) {
            // 更新人员和注册人员重新匹配成果
            for (PersonPmName personPmName : psnPmName) {
              this.startMatchPsnPubs(personPmName);
              this.startMatchPsnPats(personPmName);
            }
            // 没有在solr里检索到成果时，需要将人员的email匹配下成果
            updatePdwhPubMatchInfoService.saveOtherTaskRecord(person.getPersonId());
          }
        } catch (Exception e) {
          logger.error("新注册人员或人员姓名更新，匹配成果出错，constId:" + psnId, e);
          updatePdwhPubMatchInfoService.updateStatus(psnId, 2, 2);
          continue;
        }
        updatePdwhPubMatchInfoService.updateStatus(psnId, 2, 1);
      } catch (Exception e) {
        updatePdwhPubMatchInfoService.updateStatus(psnId, 2, 2);
        logger.error("更新人员别名常量表出错，constId:" + psnId, e);
      }
    }

  }

  /**
   * 检索成果
   *
   * @param personPmName
   * @throws SolrServerException
   * @author LIJUN
   * @throws UnsupportedEncodingException
   * @date 2018年6月30日
   */
  public void startMatchPsnPubs(PersonPmName personPmName) throws Exception {
    int i = 0;
    while (true) {
      String md5Name = MessageDigestUtils.messageDigest(XmlUtil.cleanXMLAuthorChars(personPmName.getName()));
      List<Map<String, Object>> searchPsnPubs = solrIndexSerivce.searchPubByAuthorName(md5Name, i);
      if (CollectionUtils.isNotEmpty(searchPsnPubs)) {
        updatePdwhPubMatchInfoService.matchPsnPubs(personPmName, searchPsnPubs);
        i++;
      } else {
        break;
      }
    }
  }

  /**
   * 检索专利
   *
   * @param personPmName
   * @throws SolrServerException
   * @author LIJUN
   * @date 2018年6月30日
   */
  public void startMatchPsnPats(PersonPmName personPmName) throws Exception {
    int j = 0;
    while (true) {
      String md5Name = MessageDigestUtils.messageDigest(XmlUtil.cleanXMLAuthorChars(personPmName.getName()));
      List<Map<String, Object>> searchPsnPats = solrIndexSerivce.searchPatentByAuthorName(md5Name, j);
      if (CollectionUtils.isNotEmpty(searchPsnPats)) {
        updatePdwhPubMatchInfoService.matchPsnPubs(personPmName, searchPsnPats);
        j++;
      } else {
        break;
      }

    }

  }
}
