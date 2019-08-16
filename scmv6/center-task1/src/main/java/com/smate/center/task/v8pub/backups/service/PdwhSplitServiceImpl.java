package com.smate.center.task.v8pub.backups.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubDetailDAO;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubDoiDAO;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubInsDAO;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubJournalDAO;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubPatentDAO;
import com.smate.center.task.v8pub.pdwh.po.PdwhPubDoiPO;
import com.smate.center.task.v8pub.pdwh.po.PdwhPubInsPO;
import com.smate.center.task.v8pub.pdwh.po.PdwhPubJournalPO;
import com.smate.center.task.v8pub.pdwh.po.PdwhPubPatentPO;
import com.smate.center.task.v8pub.pdwh.po.PubPdwhDetailPO;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.v8pub.dom.JournalInfoBean;
import com.smate.web.v8pub.dom.PatentInfoBean;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;

/**
 * 基准库mongodb数据拆分服务
 * 
 * @author YJ
 *
 *         2019年3月29日
 */
@Service(value = "pdwhSplitService")
@Transactional(rollbackFor = Exception.class)
public class PdwhSplitServiceImpl implements PdwhSplitService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PdwhPubDetailDAO pdwhPubDetailDAO;
  @Autowired
  private PdwhPubDoiDAO pdwhPubDoiDAO;
  @Autowired
  private PdwhPubJournalDAO pdwhPubJournalDAO;
  @Autowired
  private PdwhPubPatentDAO pdwhPubPatentDAO;
  @Autowired
  private PdwhPubInsDAO pdwhPubInsDAO;

  @Override
  public void backUpPdwhPubJson(PubPdwhDetailDOM pubPdwhDetailDOM) throws ServiceException {
    try {
      if (pubPdwhDetailDOM != null) {
        Long pdwhPubId = pubPdwhDetailDOM.getPubId();
        PubPdwhDetailPO pubPdwhDetailPO = pdwhPubDetailDAO.get(pdwhPubId);
        if (pubPdwhDetailPO == null) {
          pubPdwhDetailPO = new PubPdwhDetailPO();
          pubPdwhDetailPO.setPubId(pdwhPubId);
        }
        // 获取json数据
        String pubPdwhJson = JacksonUtils.jsonObjectSerializer(pubPdwhDetailDOM);
        pubPdwhDetailPO.setPubJson(pubPdwhJson);
        pdwhPubDetailDAO.saveOrUpdate(pubPdwhDetailPO);
      }
    } catch (Exception e) {
      logger.error("备份pdwh库成果json数据出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void backUpPdwhPubDoi(PubPdwhDetailDOM pubPdwhDetailDOM) throws ServiceException {
    try {
      if (pubPdwhDetailDOM != null && StringUtils.isNotBlank(pubPdwhDetailDOM.getDoi())) {
        Long pdwhPubId = pubPdwhDetailDOM.getPubId();
        PdwhPubDoiPO pdwhPubDoiPO = pdwhPubDoiDAO.get(pdwhPubId);
        if (pdwhPubDoiPO == null) {
          pdwhPubDoiPO = new PdwhPubDoiPO();
          pdwhPubDoiPO.setPdwhPubId(pdwhPubId);
        }
        String doi = StringUtils.substring(pubPdwhDetailDOM.getDoi(), 0, 200);
        pdwhPubDoiPO.setDoi(doi);
        pdwhPubDoiDAO.saveOrUpdate(pdwhPubDoiPO);
      } else {
        // 新导入的数据doi不存在的话，就直接删除备份表的数据
        pdwhPubDoiDAO.deleteByPdwhPubId(pubPdwhDetailDOM.getPubId());
      }
    } catch (Exception e) {
      logger.error("备份pdwh库成果DOI数据出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void backUpPdwhPubJournal(PubPdwhDetailDOM pubPdwhDetailDOM) throws ServiceException {
    try {
      if (pubPdwhDetailDOM != null && pubPdwhDetailDOM.getPubType() == 4 && pubPdwhDetailDOM.getTypeInfo() != null) {
        Long pdwhPubId = pubPdwhDetailDOM.getPubId();
        PdwhPubJournalPO pdwhPubJournalPO = pdwhPubJournalDAO.get(pdwhPubId);
        if (pdwhPubJournalPO == null) {
          pdwhPubJournalPO = new PdwhPubJournalPO();
          pdwhPubJournalPO.setPdwhPubId(pdwhPubId);
        }
        JournalInfoBean journalInfoBean = (JournalInfoBean) pubPdwhDetailDOM.getTypeInfo();
        if (journalInfoBean != null && (StringUtils.isNotBlank(journalInfoBean.getName())
            || StringUtils.isNotBlank(journalInfoBean.getISSN()))) {
          pdwhPubJournalPO.setJid(journalInfoBean.getJid());
          String name = StringUtils.substring(journalInfoBean.getName(), 0, 500);
          pdwhPubJournalPO.setName(name);
          String issn = StringUtils.substring(journalInfoBean.getISSN(), 0, 100);
          pdwhPubJournalPO.setIssn(issn);
          pdwhPubJournalDAO.saveOrUpdate(pdwhPubJournalPO);
        } else {
          // 如果期刊数据中期刊名和issn都为空的话，就直接删除备份表数据
          pdwhPubJournalDAO.deleteByPdwhPubId(pdwhPubId);
        }
      }
    } catch (Exception e) {
      logger.error("备份pdwh库成果期刊数据出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void backUpPdwhPubPatent(PubPdwhDetailDOM pubPdwhDetailDOM) throws ServiceException {
    try {
      if (pubPdwhDetailDOM != null && pubPdwhDetailDOM.getPubType() == 5 && pubPdwhDetailDOM.getTypeInfo() != null) {
        Long pdwhPubId = pubPdwhDetailDOM.getPubId();
        PdwhPubPatentPO pdwhPubPatentPO = pdwhPubPatentDAO.get(pdwhPubId);
        if (pdwhPubPatentPO == null) {
          pdwhPubPatentPO = new PdwhPubPatentPO();
          pdwhPubPatentPO.setPdwhPubId(pdwhPubId);
        }
        PatentInfoBean patentInfoBean = (PatentInfoBean) pubPdwhDetailDOM.getTypeInfo();
        if (patentInfoBean != null && (StringUtils.isNotBlank(patentInfoBean.getApplicationNo())
            || StringUtils.isNotBlank(patentInfoBean.getPublicationOpenNo()))) {
          String applicationNo = StringUtils.substring(patentInfoBean.getApplicationNo(), 0, 200);
          pdwhPubPatentPO.setApplicationNo(applicationNo);
          String publicationOpenNo = StringUtils.substring(patentInfoBean.getPublicationOpenNo(), 0, 200);
          pdwhPubPatentPO.setPublicationOpenNo(publicationOpenNo);
          pdwhPubPatentDAO.saveOrUpdate(pdwhPubPatentPO);
        } else {
          // 专利数据中申请号与公开号均为空的话，就直接删除备份表记录
          pdwhPubPatentDAO.deleteByPdwhPubId(pdwhPubId);
        }
      }
    } catch (Exception e) {
      logger.error("备份pdwh库成果专利数据出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void backUpPdwhPubIns(PubPdwhDetailDOM pubPdwhDetailDOM) throws ServiceException {
    try {
      if (pubPdwhDetailDOM != null && NumberUtils.isNotNullOrZero(pubPdwhDetailDOM.getInsId())) {
        Long pdwhPubId = pubPdwhDetailDOM.getPubId();
        PdwhPubInsPO pdwhPubInsPO = pdwhPubInsDAO.get(pdwhPubId);
        if (pdwhPubInsPO == null) {
          pdwhPubInsPO = new PdwhPubInsPO();
          pdwhPubInsPO.setPdwhPubId(pdwhPubId);
        }
        pdwhPubInsPO.setInsId(pubPdwhDetailDOM.getInsId());
        pdwhPubInsDAO.saveOrUpdate(pdwhPubInsPO);
      } else {
        // 专利数据中insId为空的话，就直接删除备份表记录
        pdwhPubInsDAO.deleteByPdwhPubId(pubPdwhDetailDOM.getPubId());
      }
    } catch (Exception e) {
      logger.error("备份pdwh库成果indId数据出错！", e);
      throw new ServiceException(e);
    }
  }

}
