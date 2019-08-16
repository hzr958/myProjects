package com.smate.center.open.service.project;



import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.dao.prj.NsfcProject2Dao;
import com.smate.center.open.model.nsfc.project.NsfcProject;
import com.smate.center.open.model.nsfc.project.NsfcProject2;
import com.smate.center.open.model.nsfc.project.NsfcProjectReport;


/**
 * @author zk
 * 
 */
@Service("syncRolService")
@Transactional(rollbackFor = Exception.class)
public class SyncRolServiceImpl implements SyncRolService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private NsfcProject2Dao nsfcProject2Dao;

  @Override
  public void saveSyncRolProject(NsfcProject snsNsfcProject) throws Exception {
    try {
      NsfcProject2 nsfcProject2 = new NsfcProject2();
      nsfcProject2.setAppAmoint(snsNsfcProject.getAppAmoint());
      nsfcProject2.setCkeywords(snsNsfcProject.getCkeywords());
      nsfcProject2.setCommenceDate(snsNsfcProject.getCommenceDate());
      nsfcProject2.setCompletionDate(snsNsfcProject.getCompletionDate());
      nsfcProject2.setCsummary(snsNsfcProject.getCsummary());
      nsfcProject2.setCtitle(snsNsfcProject.getCtitle());
      nsfcProject2.setDisNo1(snsNsfcProject.getDisNo1());
      nsfcProject2.setDisNo2(snsNsfcProject.getDisNo2());
      nsfcProject2.setDivno(snsNsfcProject.getDivno());
      nsfcProject2.setEkeywords(snsNsfcProject.getEkeywords());
      nsfcProject2.setEsummary(snsNsfcProject.getEsummary());
      nsfcProject2.setEtitle(snsNsfcProject.getEtitle());
      nsfcProject2.setGrantDescription(snsNsfcProject.getGrantDescription());
      nsfcProject2.setGrantTypeId(snsNsfcProject.getGrantTypeId());
      nsfcProject2.setInsId(snsNsfcProject.getInsId());
      nsfcProject2.setInsName(snsNsfcProject.getInsName());
      nsfcProject2.setNsfcPrjId(snsNsfcProject.getNsfcPrjId());
      // 防止 Found shared references to a collection:
      Set<NsfcProjectReport> oldReport = snsNsfcProject.getNsfcProjectReports();
      Set<NsfcProjectReport> newReport = new HashSet<NsfcProjectReport>();
      if (oldReport != null && oldReport.size() > 0) {
        Iterator<NsfcProjectReport> iterator = oldReport.iterator();
        while (iterator.hasNext()) {
          newReport.add(iterator.next());

        }
      }
      nsfcProject2.setNsfcProjectReports(newReport);
      nsfcProject2.setPiPsnId(snsNsfcProject.getPiPsnId());
      nsfcProject2.setPno(snsNsfcProject.getPno());
      nsfcProject2.setPrjId(snsNsfcProject.getPrjId());
      nsfcProject2.setStatus(snsNsfcProject.getStatus());
      nsfcProject2.setStatYear(snsNsfcProject.getStatYear());
      nsfcProject2.setSubGrantTypeId(snsNsfcProject.getSubGrantTypeId());
      nsfcProject2Dao.save(nsfcProject2);
    } catch (Exception e) {
      logger.error("同步nsfc项目数据保存在nsfcrol端出错", e);
      throw new Exception("同步nsfc项目数据保存在nsfcrol端出错", e);
    }
  }

  @Override
  public void deleteRolProject(Long snsPrjId) throws Exception {

    try {
      NsfcProject2 rolNsfcProject = nsfcProject2Dao.get(snsPrjId);
      if (rolNsfcProject != null)
        nsfcProject2Dao.delete(snsPrjId);
    } catch (Exception e) {
      logger.error("根据snsPrjId：{}删除nsfcrol的nsfcProject表数据端出错", snsPrjId, e);
      throw new Exception(e);
    }


  }

}
