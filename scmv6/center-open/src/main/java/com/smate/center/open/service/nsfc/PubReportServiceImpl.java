package com.smate.center.open.service.nsfc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.dao.prj.ProjectReportDao;
import com.smate.center.open.dao.reschproject.ProjectReportPubDao;
import com.smate.center.open.model.consts.ConstPubType;
import com.smate.center.open.model.nsfc.project.NsfcPrjRptPub;
import com.smate.center.open.model.nsfc.project.NsfcProjectReport;
import com.smate.center.open.service.consts.ConstPubTypeService;
import com.smate.center.open.service.publication.PublicationService;


/**
 * 成果解题报告模块.
 * 
 * @author LY
 * 
 */
@Service("pubReportService")
@Transactional(rollbackFor = Exception.class)
public class PubReportServiceImpl implements PubReportService, Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -4616364233174850377L;
  private static Logger logger = LoggerFactory.getLogger(PubReportServiceImpl.class);

  @Autowired
  private ProjectReportDao projectReportDao;

  @Autowired
  private ProjectReportPubDao projectReportPubDao;

  @Autowired
  private ConstPubTypeService constPubTypeService;

  @Autowired
  private PublicationService publicationService;

  @Override
  public List<NsfcPrjRptPub> getPrjFinalPubsForStat(Long nsfcRptId) throws Exception {
    try {
      if (nsfcRptId == null) {
        throw new Exception("报告编号Id不能为空");
      }

      NsfcProjectReport prjRpt = projectReportDao.getPrjRptByNsfcRptId(nsfcRptId);
      if (prjRpt == null) {
        throw new Exception("没有找到对应的结题报告nsfcRptId:" + nsfcRptId);
      }
      List<NsfcPrjRptPub> pubs = projectReportPubDao.getPrjRptPubsByRptIdForStat(prjRpt.getRptId());
      return pubs;
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new Exception("查询待统计的结题报告成果失败");
    }
  }

  @Override
  public List<NsfcPrjRptPub> getProjectFinalPubs(Long nsfcRptId) throws Exception {
    try {
      if (nsfcRptId == null) {
        throw new Exception("报告编号Id不能为空");
      }

      NsfcProjectReport prjRpt = this.projectReportDao.getPrjRptByNsfcRptId(nsfcRptId);
      if (prjRpt == null) {
        throw new Exception("没有找到对应的结题报告nsfcRptId:" + nsfcRptId);
      }
      List<NsfcPrjRptPub> pubs = this.getProjectReportSubmitsAll(prjRpt.getRptId());
      return pubs;
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new Exception("查询结题报告成果失败");
    }
  }

  public List<NsfcPrjRptPub> getProjectReportSubmitsAll(Long rptId) throws Exception {
    List<NsfcPrjRptPub> list = new ArrayList<NsfcPrjRptPub>();
    try {
      list = this.projectReportPubDao.getProjectReportPubsByRptId(rptId, 0, 0);
      for (NsfcPrjRptPub rptPub : list) {
        ConstPubType pubType = constPubTypeService.get(rptPub.getPubType());
        rptPub.setPubTypeName(StringUtils.isNotBlank(pubType.getZhName()) ? pubType.getZhName() : pubType.getEnName());

        StringBuffer sourceCitedTimes = new StringBuffer();

        String autoName = publicationService.buildFinalPrjAuthorName(rptPub.getId().getPubId());
        rptPub.setAuthors(autoName);

        String rptSource = rptPub.getSource();
        if (StringUtils.isNotBlank(sourceCitedTimes.toString())) {
          if (rptSource.endsWith(".") || rptSource.endsWith("。")) {
            rptSource = rptSource.substring(0, rptSource.length() - 1);
          }
        }
        rptPub.setNsfcSource(rptSource + sourceCitedTimes);
      }
    } catch (Exception e) {
      logger.error(e.getMessage() + e);
    }
    return list;
  }


}
