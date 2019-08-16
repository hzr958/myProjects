package com.smate.center.batch.service.pdwh.pubmatch;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.pdwh.pub.SciencedirectInsNameDao;
import com.smate.center.batch.dao.pdwh.pub.cnipr.CniprInsNameDao;
import com.smate.center.batch.dao.pdwh.pub.cnki.CnkiInsNameDao;
import com.smate.center.batch.dao.pdwh.pub.ei.EiInsNameDao;
import com.smate.center.batch.dao.pdwh.pub.isi.IeeexploreInsNameDao;
import com.smate.center.batch.dao.pdwh.pub.isi.IsiInsNameDao;
import com.smate.center.batch.dao.pdwh.pub.pubmed.PubmedInsNameDao;
import com.smate.center.batch.dao.pdwh.pub.sps.SpsInsNameDao;
import com.smate.center.batch.dao.pdwh.pub.wanfang.WanfangInsNameDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.IeeexploreInsName;
import com.smate.center.batch.model.pdwh.pub.SciencedirectInsName;
import com.smate.center.batch.model.pdwh.pub.cnipr.CniprInsName;
import com.smate.center.batch.model.pdwh.pub.cnki.CnkiInsName;
import com.smate.center.batch.model.pdwh.pub.ei.EiInsName;
import com.smate.center.batch.model.pdwh.pub.isi.IsiInsName;
import com.smate.center.batch.model.pdwh.pub.pubmed.PubmedInsName;
import com.smate.center.batch.model.pdwh.pub.sps.SpsInsName;
import com.smate.center.batch.model.pdwh.pub.wanfang.WanfangInsName;
import com.smate.center.batch.service.pub.mq.InsAliasSyncMessage;


/**
 * 单位别名同步服务.
 * 
 * @author liqinghua
 * 
 */
@Service("insNameSyncService")
@Transactional(rollbackFor = Exception.class)
public class InsNameSyncServiceImpl implements InsNameSyncService {

  /**
   * 
   */
  private static final long serialVersionUID = -7126423844794859847L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private IsiInsNameDao isiInsNameDao;
  @Autowired
  private CnkiInsNameDao cnkiInsNameDao;
  @Autowired
  private SpsInsNameDao spsInsNameDao;
  @Autowired
  private WanfangInsNameDao wanfangInsNameDao;
  @Autowired
  private EiInsNameDao eiInsNameDao;
  @Autowired
  private PubmedInsNameDao pubmedInsNameDao;
  @Autowired
  private IeeexploreInsNameDao ieeexploreInsNameDao;
  @Autowired
  private CniprInsNameDao cniprInsNameDao;
  @Autowired
  private SciencedirectInsNameDao sciencedirectInsNameDao;

  @Override
  public void syncInsName(InsAliasSyncMessage msg) throws ServiceException {

    // isi
    List<IsiInsName> isiInsNameList = msg.getIsiInsNameList();
    this.saveIsiInsName(msg.getInsId(), isiInsNameList);
    // scopus
    List<SpsInsName> spsInsNameList = msg.getSpsInsNameList();
    this.saveSpsInsName(msg.getInsId(), spsInsNameList);
    // cnki
    List<CnkiInsName> cnkiInsNameList = msg.getCnkiInsNameList();
    this.saveCnkiInsName(msg.getInsId(), cnkiInsNameList);
    // 万方
    List<WanfangInsName> wanfangInsNameList = msg.getWanfangInsNameList();
    this.saveWanfangInsName(msg.getInsId(), wanfangInsNameList);
    // EI
    List<EiInsName> eiInsNameList = msg.getEiInsNameList();
    this.saveEiInsName(msg.getInsId(), eiInsNameList);
    // PubMed
    List<PubmedInsName> pubmedInsNameList = msg.getPubmedInsNameList();
    this.savePubmedInsName(msg.getInsId(), pubmedInsNameList);
    // IEEEXplore
    List<IeeexploreInsName> ieeexploreInsNameList = msg.getIeeexploreInsNameList();
    this.saveIeeexploreInsName(msg.getInsId(), ieeexploreInsNameList);
    // CNIPR
    List<CniprInsName> cniprInsNameList = msg.getCniprInsNameList();
    this.saveCniprInsName(msg.getInsId(), cniprInsNameList);
    // ScienceDirect
    List<SciencedirectInsName> sciencedirectInsNameList = msg.getSciencedirectInsNameList();
    this.saveSciencedirectInsName(msg.getInsId(), sciencedirectInsNameList);

  }

  private void saveIsiInsName(Long insId, List<IsiInsName> isiInsNameList) throws ServiceException {

    if (CollectionUtils.isEmpty(isiInsNameList)) {
      return;
    }
    try {
      List<IsiInsName> oldIsiInsNameList = this.isiInsNameDao.getIsiInsName(insId);
      // 把多余的删除
      outLoop: for (int i = 0; i < oldIsiInsNameList.size(); i++) {
        IsiInsName oldInsName = oldIsiInsNameList.get(i);
        for (IsiInsName insName : isiInsNameList) {
          // 存在，跳过
          if (oldInsName.getId().equals(insName.getId())) {
            continue outLoop;
          }
        }
        // 不存在，删除
        isiInsNameDao.removeIsiInsName(oldInsName.getId());
        oldIsiInsNameList.remove(i);
        i--;
      }
      // 新增的添加
      outLoop: for (IsiInsName insName : isiInsNameList) {
        for (IsiInsName oldInsName : oldIsiInsNameList) {
          // 存在，跳过
          if (oldInsName.getId().equals(insName.getId())) {
            continue outLoop;
          }
        }
        // 不存在，创建
        this.isiInsNameDao.save(insName);
      }
    } catch (Exception e) {
      logger.error("同步保存单位ISI单位别名数据insId:" + insId, e);
      throw new ServiceException("同步保存单位ISI单位别名数据insId:" + insId, e);
    }
  }

  private void saveCnkiInsName(Long insId, List<CnkiInsName> cnkiInsNameList) throws ServiceException {

    if (CollectionUtils.isEmpty(cnkiInsNameList)) {
      return;
    }
    try {
      List<CnkiInsName> oldCnkiInsNameList = this.cnkiInsNameDao.getCnkiInsName(insId);
      // 把多余的删除
      outLoop: for (int i = 0; i < oldCnkiInsNameList.size(); i++) {
        CnkiInsName oldInsName = oldCnkiInsNameList.get(i);
        for (CnkiInsName insName : cnkiInsNameList) {
          // 存在，跳过
          if (oldInsName.getId().equals(insName.getId())) {
            continue outLoop;
          }
        }
        // 不存在，删除
        cnkiInsNameDao.removeCnkiInsName(oldInsName.getId());
        oldCnkiInsNameList.remove(i);
        i--;
      }
      // 新增的添加
      outLoop: for (CnkiInsName insName : cnkiInsNameList) {
        for (CnkiInsName oldInsName : oldCnkiInsNameList) {
          // 存在，跳过
          if (oldInsName.getId().equals(insName.getId())) {
            continue outLoop;
          }
        }
        // 不存在，创建
        this.cnkiInsNameDao.save(insName);
      }
    } catch (Exception e) {
      logger.error("同步保存单位CNKI单位别名数据insId:" + insId, e);
      throw new ServiceException("同步保存单位CNKI单位别名数据insId:" + insId, e);
    }
  }

  private void saveSpsInsName(Long insId, List<SpsInsName> spsInsNameList) throws ServiceException {

    if (CollectionUtils.isEmpty(spsInsNameList)) {
      return;
    }
    try {
      List<SpsInsName> oldSpsInsNameList = this.spsInsNameDao.getSpsInsName(insId);
      // 把多余的删除
      outLoop: for (int i = 0; i < oldSpsInsNameList.size(); i++) {
        SpsInsName oldInsName = oldSpsInsNameList.get(i);
        for (SpsInsName insName : spsInsNameList) {
          // 存在，跳过
          if (oldInsName.getId().equals(insName.getId())) {
            continue outLoop;
          }
        }
        // 不存在，删除
        spsInsNameDao.removeSpsInsName(oldInsName.getId());
        oldSpsInsNameList.remove(i);
        i--;
      }
      // 新增的添加
      outLoop: for (SpsInsName insName : spsInsNameList) {
        for (SpsInsName oldInsName : oldSpsInsNameList) {
          // 存在，跳过
          if (oldInsName.getId().equals(insName.getId())) {
            continue outLoop;
          }
        }
        // 不存在，创建
        this.spsInsNameDao.save(insName);
      }
    } catch (Exception e) {
      logger.error("同步保存单位SCOPUS单位别名数据insId:" + insId, e);
      throw new ServiceException("同步保存单位SCOPUS单位别名数据insId:" + insId, e);
    }
  }

  private void saveWanfangInsName(Long insId, List<WanfangInsName> wanfangInsNameList) throws ServiceException {

    if (CollectionUtils.isEmpty(wanfangInsNameList)) {
      return;
    }
    try {
      List<WanfangInsName> oldWanfangInsNameList = this.wanfangInsNameDao.getWanfangInsName(insId);
      // 把多余的删除
      outLoop: for (int i = 0; i < oldWanfangInsNameList.size(); i++) {
        WanfangInsName oldInsName = oldWanfangInsNameList.get(i);
        for (WanfangInsName insName : wanfangInsNameList) {
          // 存在，跳过
          if (oldInsName.getId().equals(insName.getId())) {
            continue outLoop;
          }
        }
        // 不存在，删除
        wanfangInsNameDao.removeWanfangInsName(oldInsName.getId());
        oldWanfangInsNameList.remove(i);
        i--;
      }
      // 新增的添加
      outLoop: for (WanfangInsName insName : wanfangInsNameList) {
        for (WanfangInsName oldInsName : oldWanfangInsNameList) {
          // 存在，跳过
          if (oldInsName.getId().equals(insName.getId())) {
            continue outLoop;
          }
        }
        // 不存在，创建
        this.wanfangInsNameDao.save(insName);
      }
    } catch (Exception e) {
      logger.error("同步保存单位万方单位别名数据insId:" + insId, e);
      throw new ServiceException("同步保存单位万方单位别名数据insId:" + insId, e);
    }
  }

  private void saveEiInsName(Long insId, List<EiInsName> eiInsNameList) throws ServiceException {

    if (CollectionUtils.isEmpty(eiInsNameList)) {
      return;
    }
    try {
      List<EiInsName> oldEiInsNameList = this.eiInsNameDao.getEiInsName(insId);
      // 把多余的删除
      outLoop: for (int i = 0; i < oldEiInsNameList.size(); i++) {
        EiInsName oldInsName = oldEiInsNameList.get(i);
        for (EiInsName insName : eiInsNameList) {
          // 存在，跳过
          if (oldInsName.getId().equals(insName.getId())) {
            continue outLoop;
          }
        }
        // 不存在，删除
        eiInsNameDao.removeEiInsName(oldInsName.getId());
        oldEiInsNameList.remove(i);
        i--;
      }
      // 新增的添加
      outLoop: for (EiInsName insName : eiInsNameList) {
        for (EiInsName oldInsName : oldEiInsNameList) {
          // 存在，跳过
          if (oldInsName.getId().equals(insName.getId())) {
            continue outLoop;
          }
        }
        // 不存在，创建
        this.eiInsNameDao.save(insName);
      }
    } catch (Exception e) {
      logger.error("同步保存单位EI单位别名数据insId:" + insId, e);
      throw new ServiceException("同步保存单位EI单位别名数据insId:" + insId, e);
    }
  }

  private void savePubmedInsName(Long insId, List<PubmedInsName> pubmedInsNameList) throws ServiceException {

    if (CollectionUtils.isEmpty(pubmedInsNameList)) {
      return;
    }
    try {
      List<PubmedInsName> oldPubmedInsNameList = this.pubmedInsNameDao.getPubmedInsName(insId);
      // 把多余的删除
      outLoop: for (int i = 0; i < oldPubmedInsNameList.size(); i++) {
        PubmedInsName oldInsName = oldPubmedInsNameList.get(i);
        for (PubmedInsName insName : pubmedInsNameList) {
          // 存在，跳过
          if (oldInsName.getId().equals(insName.getId())) {
            continue outLoop;
          }
        }
        // 不存在，删除
        pubmedInsNameDao.removePubmedInsName(oldInsName.getId());
        oldPubmedInsNameList.remove(i);
        i--;
      }
      // 新增的添加
      outLoop: for (PubmedInsName insName : pubmedInsNameList) {
        for (PubmedInsName oldInsName : oldPubmedInsNameList) {
          // 存在，跳过
          if (oldInsName.getId().equals(insName.getId())) {
            continue outLoop;
          }
        }
        // 不存在，创建
        this.pubmedInsNameDao.save(insName);
      }
    } catch (Exception e) {
      logger.error("同步保存单位PubMed单位别名数据insId:" + insId, e);
      throw new ServiceException("同步保存单位PubMed单位别名数据insId:" + insId, e);
    }
  }

  private void saveIeeexploreInsName(Long insId, List<IeeexploreInsName> ieeexploreInsNameList)
      throws ServiceException {

    if (CollectionUtils.isEmpty(ieeexploreInsNameList)) {
      return;
    }
    try {
      List<IeeexploreInsName> oldIeeexploreInsNameList = this.ieeexploreInsNameDao.getIeeexploreInsName(insId);
      // 把多余的删除
      outLoop: for (int i = 0; i < oldIeeexploreInsNameList.size(); i++) {
        IeeexploreInsName oldInsName = oldIeeexploreInsNameList.get(i);
        for (IeeexploreInsName insName : ieeexploreInsNameList) {
          // 存在，跳过
          if (oldInsName.getId().equals(insName.getId())) {
            continue outLoop;
          }
        }
        // 不存在，删除
        ieeexploreInsNameDao.removeIeeexploreInsName(oldInsName.getId());
        oldIeeexploreInsNameList.remove(i);
        i--;
      }
      // 新增的添加
      outLoop: for (IeeexploreInsName insName : ieeexploreInsNameList) {
        for (IeeexploreInsName oldInsName : oldIeeexploreInsNameList) {
          // 存在，跳过
          if (oldInsName.getId().equals(insName.getId())) {
            continue outLoop;
          }
        }
        // 不存在，创建
        this.ieeexploreInsNameDao.save(insName);
      }
    } catch (Exception e) {
      logger.error("同步保存单位Ieeexplore单位别名数据insId:" + insId, e);
      throw new ServiceException("同步保存单位Ieeexplore单位别名数据insId:" + insId, e);
    }
  }

  private void saveCniprInsName(Long insId, List<CniprInsName> cniprInsNameList) throws ServiceException {

    if (CollectionUtils.isEmpty(cniprInsNameList)) {
      return;
    }
    try {
      List<CniprInsName> oldCniprInsNameList = this.cniprInsNameDao.getCniprInsName(insId);
      // 把多余的删除
      outLoop: for (int i = 0; i < oldCniprInsNameList.size(); i++) {
        CniprInsName oldInsName = oldCniprInsNameList.get(i);
        for (CniprInsName insName : cniprInsNameList) {
          // 存在，跳过
          if (oldInsName.getId().equals(insName.getId())) {
            continue outLoop;
          }
        }
        // 不存在，删除
        cniprInsNameDao.removeCniprInsName(oldInsName.getId());
        oldCniprInsNameList.remove(i);
        i--;
      }
      // 新增的添加
      outLoop: for (CniprInsName insName : cniprInsNameList) {
        for (CniprInsName oldInsName : oldCniprInsNameList) {
          // 存在，跳过
          if (oldInsName.getId().equals(insName.getId())) {
            continue outLoop;
          }
        }
        // 不存在，创建
        this.cniprInsNameDao.save(insName);
      }
    } catch (Exception e) {
      logger.error("同步保存单位Cnipr单位别名数据insId:" + insId, e);
      throw new ServiceException("同步保存单位Cnipr单位别名数据insId:" + insId, e);
    }
  }

  private void saveSciencedirectInsName(Long insId, List<SciencedirectInsName> sciencedirectInsNameList)
      throws ServiceException {

    if (CollectionUtils.isEmpty(sciencedirectInsNameList)) {
      return;
    }
    try {
      List<SciencedirectInsName> oldSciencedirectInsNameList =
          this.sciencedirectInsNameDao.getSciencedirectInsName(insId);
      // 把多余的删除
      outLoop: for (int i = 0; i < oldSciencedirectInsNameList.size(); i++) {
        SciencedirectInsName oldInsName = oldSciencedirectInsNameList.get(i);
        for (SciencedirectInsName insName : sciencedirectInsNameList) {
          // 存在，跳过
          if (oldInsName.getId().equals(insName.getId())) {
            continue outLoop;
          }
        }
        // 不存在，删除
        sciencedirectInsNameDao.removeSciencedirectInsName(oldInsName.getId());
        oldSciencedirectInsNameList.remove(i);
        i--;
      }
      // 新增的添加
      outLoop: for (SciencedirectInsName insName : sciencedirectInsNameList) {
        for (SciencedirectInsName oldInsName : oldSciencedirectInsNameList) {
          // 存在，跳过
          if (oldInsName.getId().equals(insName.getId())) {
            continue outLoop;
          }
        }
        // 不存在，创建
        this.sciencedirectInsNameDao.save(insName);
      }
    } catch (Exception e) {
      logger.error("同步保存单位ScienceDirect单位别名数据insId:" + insId, e);
      throw new ServiceException("同步保存单位ScienceDirect单位别名数据insId:" + insId, e);
    }
  }

}
