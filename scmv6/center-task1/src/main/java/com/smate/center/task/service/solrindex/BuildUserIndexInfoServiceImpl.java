package com.smate.center.task.service.solrindex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.center.task.dao.open.OpenUserUnionDao;
import com.smate.center.task.dao.sns.psn.PsnDisciplineKeyDao;
import com.smate.center.task.dao.sns.psn.PsnScienceAreaDao;
import com.smate.center.task.dao.sns.quartz.InstitutionDao;
import com.smate.center.task.model.sns.quartz.Institution;
import com.smate.center.task.v8pub.dao.sns.PubSnsDetailDAO;
import com.smate.core.base.psn.dao.PsnPubDAO;
import com.smate.core.base.psn.dao.PsnStatisticsDao;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.dao.security.PsnPrivateDao;
import com.smate.core.base.utils.model.security.Person;
import com.smate.web.v8pub.dom.sns.PubSnsDetailDOM;

@Transactional(rollbackFor = Exception.class)
public class BuildUserIndexInfoServiceImpl extends AbstractIndexHandleServiceImpl {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private Integer batchSize = 200;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private OpenUserUnionDao openUserUnionDao;
  @Autowired
  private PsnScienceAreaDao psnScienceAreaDao;
  @Autowired
  private PsnDisciplineKeyDao psnDisciplineKeyDao;
  @Autowired
  private PsnPrivateDao psnPrivateDao;
  @Autowired
  private InstitutionDao institutionDao;
  @Autowired
  private PsnStatisticsDao psnStatisticsDao;
  @Autowired
  private PsnPubDAO psnPubDAO;
  @Autowired
  private PubSnsDetailDAO pubSnsDetailDAO;

  @Override
  public Map<String, Object> checkData(IndexInfoVO indexInfoVO) {
    Map<String, Object> map = new HashMap<>();
    if (indexInfoVO.getLastPsnId() == null) {
      map.put("result", "error");
      map.put("msg", "查询的pubId不能为空");
      return map;
    }

    return null;
  }

  @Override
  public void queryBaseData(IndexInfoVO indexInfoVO) {
    indexInfoVO.setPsnList(personDao.findUserByBatchSize(indexInfoVO.getLastPsnId(), batchSize));

  }

  @Override
  public void buildIndexData(IndexInfoVO indexInfoVO) {
    List<Person> psnList = indexInfoVO.getPsnList();
    if (CollectionUtils.isNotEmpty(psnList)) {
      ArrayList<SolrInputDocument> docList = new ArrayList<SolrInputDocument>();
      for (Person psn : psnList) {
        try {
          SolrInputDocument doc = new SolrInputDocument();
          // 必须字段设定schema.xml配置,配置id时需将其与其他id区分开，以5开头
          doc.setField("businessType", IndexInfoVO.INDEX_TYPE_PSN);
          Assert.notNull(runEnv, "runEnv不能为空,请在properties中配置");
          doc.setField("env", runEnv);
          doc.setField("id", generateIdForIndex(psn.getPersonId(), IndexInfoVO.INDEX_TYPE_PSN));
          doc.setField("psnId", psn.getPersonId());
          doc.setField("psnName", psn.getName());
          doc.setField("enPsnName", psn.getEname());
          // 人员信息完整度
          doc.setField("psnInfoIntegrity", psn.getComplete());
          // 判断是否是isis注册过的用户
          boolean isIsisRegistered = openUserUnionDao.ifRegisteredInTheIns(psn.getPersonId(), "2bcca485");
          if (isIsisRegistered) {
            doc.setField("psnIsisReg", 1);
          } else {
            doc.setField("psnIsisReg", 0);
          }
          if (StringUtils.isBlank(psn.getPosition()) || StringUtils.isBlank(psn.getTitolo())) {
            doc.setField("title", (StringUtils.isBlank(psn.getPosition()) ? "" : psn.getPosition())
                + (StringUtils.isBlank(psn.getTitolo()) ? "" : psn.getTitolo()));
          } else {
            doc.setField("title", psn.getPosition() + ", " + psn.getTitolo());
          }
          // 个人成果中的关键词
          this.getPsnKwsByPubKws(psn.getPersonId(), doc);
          // 人员自填关键词
          List<String> keywords = psnDisciplineKeyDao.getPsnKeywords(psn.getPersonId());
          String psnDisciplineKeyStr = "";
          if (CollectionUtils.isNotEmpty(keywords)) {
            psnDisciplineKeyStr = StringUtils.lowerCase(keywords.toString());
          }
          // 添加人员科技领域
          List<Long> psnScienceAreaList = this.psnScienceAreaDao.findPsnScienceAreaIds(psn.getPersonId());
          if (psnScienceAreaList != null && psnScienceAreaList.size() > 0) {
            doc.setField("psnScienceArea", psnScienceAreaList.toArray());
          }
          // 获取人员的专利数/项目数
          PsnStatistics psnStatistics = psnStatisticsDao.getPsnStatistics(psn.getPersonId());
          doc.setField("psnPatentCount", psnStatistics.getPatentSum());
          doc.setField("psnPrjCount", psnStatistics.getPrjSum());
          doc.setField("psnPubCount", psnStatistics.getPubSum());

          doc.setField("psnDisciplineKey", psnDisciplineKeyStr);
          // 人员是否在隐私列表中
          if (this.psnPrivateDao.existsPsnPrivate(psn.getPersonId())) {
            doc.setField("isPrivate", 1);
          } else {
            doc.setField("isPrivate", 0);
          }
          if (psn.getInsId() != null) {
            Institution ins = institutionDao.get(psn.getInsId());
            if (ins != null) {
              doc.setField("zhInsName", StringUtils.isNotBlank(ins.getZhName()) ? ins.getZhName() : ins.getEnName());
              doc.setField("enInsName", StringUtils.isNotBlank(ins.getEnName()) ? ins.getEnName() : ins.getZhName());
            }
          } else if (StringUtils.isNotEmpty(psn.getInsName())) {
            doc.setField("zhInsName", psn.getInsName());
          }
          doc.setField("psnRegionId", psn.getRegionId());
          doc.setField("zhUnit", psn.getDepartment());
          // openid
          Long openId = openUserUnionDao.getOpenIdByPsnId(psn.getPersonId());
          doc.setField("openId", openId);
          docList.add(doc);
        } catch (Exception e) {
          logger.error("人员数据出错", e);
        }
        if (saveIndex(docList)) {
          indexInfoVO.setStatus("success");
          indexInfoVO.setMsg("创建人员索引成功");
        } else {
          indexInfoVO.setStatus("error");
          indexInfoVO.setMsg("创建人员索引失败");
        }
        indexInfoVO.setLastPsnId(psnList.get(psnList.size() - 1).getPersonId());
      }
    }
  }

  private void getPsnKwsByPubKws(Long personId, SolrInputDocument doc) {
    List<Long> pubIdList = psnPubDAO.getPubIdsByPsnId(personId);
    StringBuilder keywords = new StringBuilder();
    StringBuilder patKeywords = new StringBuilder();
    if (CollectionUtils.isNotEmpty(pubIdList)) {
      for (Long pubId : pubIdList) {
        PubSnsDetailDOM detail = pubSnsDetailDAO.findByPubId(pubId);
        if (detail != null) {
          if (5 == detail.getPubType()) {
            patKeywords.append(detail.getSummary());
            patKeywords.append(detail.getTitle());
          }
          keywords.append(detail.getKeywords());
        }

      }
    }
    doc.setField("psnKeywords", keywords.toString());
    doc.setField("psnPatKeywords", patKeywords.toString());
  }

}
