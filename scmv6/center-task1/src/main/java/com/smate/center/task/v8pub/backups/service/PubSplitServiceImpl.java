package com.smate.center.task.v8pub.backups.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.v8pub.dao.sns.PubDoiDAO;
import com.smate.center.task.v8pub.dao.sns.PubJournalDAO;
import com.smate.center.task.v8pub.dao.sns.PubPatentDAO;
import com.smate.center.task.v8pub.dao.sns.SnsPubDetailDAO;
import com.smate.center.task.v8pub.sns.po.PubDoiPO;
import com.smate.center.task.v8pub.sns.po.PubJournalPO;
import com.smate.center.task.v8pub.sns.po.PubPatentPO;
import com.smate.center.task.v8pub.sns.po.PubSnsDetailPO;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.v8pub.dom.JournalInfoBean;
import com.smate.web.v8pub.dom.PatentInfoBean;
import com.smate.web.v8pub.dom.sns.PubSnsDetailDOM;

/**
 * 个人库mongodb数据拆分任务
 * 
 * @author YJ
 *
 *         2019年3月29日
 */
@Service(value = "pubSplitService")
@Transactional(rollbackFor = Exception.class)
public class PubSplitServiceImpl implements PubSplitService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private SnsPubDetailDAO snsPubDetailDAO;
  @Autowired
  private PubDoiDAO pubDoiDAO;
  @Autowired
  private PubJournalDAO pubJournalDAO;
  @Autowired
  private PubPatentDAO pubPatentDAO;

  @Override
  public void backUpSnsPubJson(PubSnsDetailDOM pubSnsDetailDOM) throws ServiceException {
    try {
      if (pubSnsDetailDOM != null) {
        Long pubId = pubSnsDetailDOM.getPubId();
        PubSnsDetailPO pubSnsDetailPO = snsPubDetailDAO.get(pubId);
        if (pubSnsDetailPO == null) {
          pubSnsDetailPO = new PubSnsDetailPO();
          pubSnsDetailPO.setPubId(pubId);
        }
        // 获取json数据
        String pubPdwhJson = JacksonUtils.jsonObjectSerializer(pubSnsDetailDOM);
        pubSnsDetailPO.setPubJson(pubPdwhJson);
        snsPubDetailDAO.saveOrUpdate(pubSnsDetailPO);
      }
    } catch (Exception e) {
      logger.error("备份个人库成果json数据出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void backUpSnsPubDoi(PubSnsDetailDOM pubSnsDetailDOM) throws ServiceException {
    try {
      if (pubSnsDetailDOM != null && StringUtils.isNotBlank(pubSnsDetailDOM.getDoi())) {
        Long pubId = pubSnsDetailDOM.getPubId();
        PubDoiPO pubDoiPO = pubDoiDAO.get(pubId);
        if (pubDoiPO == null) {
          pubDoiPO = new PubDoiPO();
          pubDoiPO.setPubId(pubId);
        }
        String doi = StringUtils.substring(pubSnsDetailDOM.getDoi(), 0, 200);
        pubDoiPO.setDoi(doi);
        pubDoiDAO.saveOrUpdate(pubDoiPO);
      } else {
        // 不存在doi的成果直接进行删除记录
        pubDoiDAO.deleteByPubId(pubSnsDetailDOM.getPubId());
      }
    } catch (Exception e) {
      logger.error("备份个人库成果DOI数据出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void backUpSnsPubJournal(PubSnsDetailDOM pubSnsDetailDOM) throws ServiceException {
    try {
      if (pubSnsDetailDOM != null && pubSnsDetailDOM.getPubType() == 4 && pubSnsDetailDOM.getTypeInfo() != null) {
        Long pubId = pubSnsDetailDOM.getPubId();
        PubJournalPO pubJournalPO = pubJournalDAO.get(pubId);
        if (pubJournalPO == null) {
          pubJournalPO = new PubJournalPO();
          pubJournalPO.setPubId(pubId);
        }
        JournalInfoBean journalInfoBean = (JournalInfoBean) pubSnsDetailDOM.getTypeInfo();
        if (journalInfoBean != null && (StringUtils.isNotBlank(journalInfoBean.getName())
            || StringUtils.isNotBlank(journalInfoBean.getISSN()))) {
          pubJournalPO.setJid(journalInfoBean.getJid());
          String name = StringUtils.substring(journalInfoBean.getName(), 0, 500);
          pubJournalPO.setName(name);
          String issn = StringUtils.substring(journalInfoBean.getISSN(), 0, 100);
          pubJournalPO.setIssn(issn);
          pubJournalDAO.saveOrUpdate(pubJournalPO);
        } else {
          // 不存在期刊信息的成果直接进行删除记录
          pubJournalDAO.deleteByPubId(pubSnsDetailDOM.getPubId());
        }
      }
    } catch (Exception e) {
      logger.error("备份个人库成果期刊数据出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void backUpSnsPubPatent(PubSnsDetailDOM pubSnsDetailDOM) throws ServiceException {
    try {
      // 信息为空的成果不进行保存记录
      if (pubSnsDetailDOM != null && pubSnsDetailDOM.getPubType() == 5 && pubSnsDetailDOM.getTypeInfo() != null) {
        Long pubId = pubSnsDetailDOM.getPubId();
        PubPatentPO pubPatentPO = pubPatentDAO.get(pubId);
        if (pubPatentPO == null) {
          pubPatentPO = new PubPatentPO();
          pubPatentPO.setPubId(pubId);
        }
        PatentInfoBean patentInfoBean = (PatentInfoBean) pubSnsDetailDOM.getTypeInfo();
        if (patentInfoBean != null && (StringUtils.isNotBlank(patentInfoBean.getApplicationNo())
            || StringUtils.isNotBlank(patentInfoBean.getPublicationOpenNo()))) {
          String applicationNo = StringUtils.substring(patentInfoBean.getApplicationNo(), 0, 200);
          pubPatentPO.setApplicationNo(applicationNo);
          String publicationOpenNo = StringUtils.substring(patentInfoBean.getPublicationOpenNo(), 0, 200);
          pubPatentPO.setPublicationOpenNo(publicationOpenNo);
          pubPatentDAO.saveOrUpdate(pubPatentPO);
        } else {
          // 不存在专利信息的成果直接进行删除记录
          pubPatentDAO.deleteByPubId(pubSnsDetailDOM.getPubId());
        }
      }
    } catch (Exception e) {
      logger.error("备份个人库成果专利数据出错！", e);
      throw new ServiceException(e);
    }
  }



}
