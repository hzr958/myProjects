package com.smate.web.group.service.grp.file;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.web.group.action.grp.form.GrpFileForm;
import com.smate.web.group.dao.grp.file.GrpFileStatisticsDao;
import com.smate.web.group.model.grp.file.GrpFileStatistics;

/**
 * @description群组文件统计serviceImpl
 * @author xiexing
 * @date 2019年5月15日
 */
@Service
@Transactional
public class GrpFileStatisticsServiceImpl implements GrpFileStatisticsService {

  @Autowired
  private GrpFileStatisticsDao grpFileStatisticsDao;

  @Override
  public List<Map<String, Object>> queryShareStatistics() throws Exception {
    // TODO Auto-generated method stub
    return grpFileStatisticsDao.queryShareStatistics();
  }

  @Override
  public void countShare(GrpFileForm grpFileForm) throws Exception {
    GrpFileStatistics grpFileStatistics = grpFileStatisticsDao.get(grpFileForm.getGrpFileId());
    if (Objects.isNull(grpFileStatistics)) {
      grpFileStatistics = new GrpFileStatistics();
      grpFileStatistics.setGrpFileId(grpFileForm.getGrpFileId());
      grpFileStatistics.setShareCount(0);
    }
    grpFileStatistics.setShareCount(grpFileStatistics.getShareCount() + 1);
    grpFileStatisticsDao.save(grpFileStatistics);
  }

}
