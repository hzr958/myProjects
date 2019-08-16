package com.smate.center.task.service.pdwh.quartz;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.task.base.TaskJobTypeConstants;
import com.smate.center.task.dao.pdwh.quartz.PdwhPubCitedTimesDao;
import com.smate.center.task.dao.pdwh.quartz.PdwhPubXmlDao;
import com.smate.center.task.dao.pdwh.quartz.TmpTaskInfoRecordDao;
import com.smate.center.task.model.pdwh.pub.PdwhPubCitedTimes;
import com.smate.center.task.single.dao.solr.PdwhPublicationDao;
import com.smate.center.task.single.oldXml.pub.ImportPubXmlDocument;
import com.smate.center.task.single.service.pub.ConstRefDbService;

@Service("updateAllPdwhPubCiteTimesService")
@Transactional(rollbackOn = Exception.class)
public class UpdateAllPdwhPubCiteTimesServiceImpl implements UpdateAllPdwhPubCiteTimesService {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PdwhPubXmlDao pdwhPubXmlDao;
  @Autowired
  private PdwhPublicationDao pdwhPublicationDao;
  @Autowired
  PdwhPubCitedTimesDao pdwhPubCitedTimesDao;
  @Autowired
  TmpTaskInfoRecordDao tmpTaskInfoRecordDao;
  @Autowired
  private ConstRefDbService constRefDbService;
  private static Integer jobType = TaskJobTypeConstants.UpdateAllPdwhPubCiteTimesTask;

  @Override
  public List<Long> getPdwhPubList(Integer batchSize) throws Exception {
    return tmpTaskInfoRecordDao.getbatchhandleIdList(batchSize, jobType);
  }

  @Override
  public Map<Integer, Integer> handlePdwhCiteTimes(Long pubId) throws Exception {
    Map<Integer, Integer> map = new HashMap<Integer, Integer>();
    try {
      map = this.getSyscitedTimes(pubId);
    } catch (Exception e) {
      logger.error("获取xml引用记录出错，pubid:" + pubId, e);
      tmpTaskInfoRecordDao.updateTaskStatus(pubId, 2, "获取xml引用记录出错", jobType);
    }
    return map;

  }

  /**
   * 获取xml引用值
   * 
   * @param currentPubId
   * @return
   * @throws Exception
   */
  public Map<Integer, Integer> getSyscitedTimes(Long currentPubId) throws Exception {
    String xmlString = pdwhPubXmlDao.getXmlStringByPubId(currentPubId);

    if (StringUtils.isEmpty(xmlString)) {
      logger.error("获取到的xml为空，pubId:" + currentPubId);
      tmpTaskInfoRecordDao.updateTaskStatus(currentPubId, 2, "获取到的xml为空", jobType);
      return null;
    }
    Map<Integer, Integer> map = new HashMap<Integer, Integer>();

    // 清理XML数据
    int sysiteTimes = 0;
    try {
      xmlString = xmlString.replaceAll("[\\x00-\\x08\\x0b-\\x0c\\x0e-\\x1f]", "");
      ImportPubXmlDocument doc = new ImportPubXmlDocument(xmlString);
      // 获取成果xml中各库的引用次数记录情况
      // 获取到cite_times,成果dbId = (15,16,17),来自sci,ssci,istp;
      // isi,dbId在引用记录表中统一记录为99
      if (doc.getCiteTimes() != null) {
        sysiteTimes = doc.getCiteTimes();
        map.put(99, sysiteTimes);
      }
      // 获取到cnki_cite_time，成果dbId =4:cnki;
      if (doc.getCnkiCiteTimes() != null) {
        sysiteTimes = doc.getCnkiCiteTimes();
        map.put(4, sysiteTimes);
      }
      // 获取到cnipr_cite_times 如果 dbId =21:cnkiPat;dbid=31:rainpat
      // cnkipat,rainpat,dbId在引用记录表中统一记录为98
      if (doc.getCniprCiteTimes() != null) {
        sysiteTimes = doc.getCniprCiteTimes();
        map.put(98, sysiteTimes);
      }
      // 获取到 ei_cite_time，dbId =14, EI;
      if (doc.getEiCiteTimes() != null) {
        sysiteTimes = doc.getEiCiteTimes();
        map.put(14, sysiteTimes);
      }
    } catch (NullPointerException e) {
      tmpTaskInfoRecordDao.updateTaskStatus(currentPubId, 2, "获取xml引用记录数节点为空", jobType);
      logger.error("获取xml引用记录数节点为空，currentPubId：" + currentPubId);
    }
    return map;

  }

  /**
   * 更新引用计数
   * 
   * @param currentPubId
   * @param sysiteTimes
   * @throws Exception
   */
  @Override
  public void updateCitedTimes(Long currentPubId, Integer dbId, Integer sysciteTimes) throws Exception {

    PdwhPubCitedTimes pdwhPubCitedTimes = pdwhPubCitedTimesDao.getcitesByPubDBId(currentPubId, dbId);
    // 如果PdwhPubCitedTimes没有记录，则新增
    if (pdwhPubCitedTimes == null) {
      PdwhPubCitedTimes citedtimes = new PdwhPubCitedTimes();
      citedtimes.setPdwhPubId(currentPubId);
      citedtimes.setDbId(dbId);
      citedtimes.setCitedTimes(sysciteTimes);
      citedtimes.setUpdateDate(new Date());
      citedtimes.setType(0);// 后台更新
      pdwhPubCitedTimesDao.save(citedtimes);
      tmpTaskInfoRecordDao.updateTaskStatus(currentPubId, 1, "", jobType);// 成功
    }

    else {
      pdwhPubCitedTimes.setCitedTimes(sysciteTimes);
      pdwhPubCitedTimes.setUpdateDate(new Date());
      pdwhPubCitedTimes.setDbId(dbId);// 更新dbId 修复原有dbid为98，99的处理
      pdwhPubCitedTimesDao.save(pdwhPubCitedTimes);
      tmpTaskInfoRecordDao.updateTaskStatus(currentPubId, 1, "", jobType);// 成功
    }

  }

  @Override
  public boolean needUpdate() {
    try {
      Long pubCount = pdwhPublicationDao.getPubCount();
      Long pubCitedTimesCount = pdwhPubCitedTimesDao.getPubCitedTimesCount();
      if (pubCitedTimesCount >= pubCount) {
        return false;
      }
    } catch (Exception e) {
      logger.error("获取pdwhPublication，pdwhPubCitedTimes记录数出错！", e);
      return false;
    }
    return true;
  }

  @Override
  public void updateTaskStatus(Long psnId, int status, String err) {
    try {
      tmpTaskInfoRecordDao.updateTaskStatus(psnId, status, err, jobType);
    } catch (Exception e) {
      logger.error("更新任务状态记录出错！handleId,status,jobtype=" + psnId + ",status" + ",jobtype", e);
    }
  }
}
