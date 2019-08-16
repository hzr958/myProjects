package com.smate.web.prj.service.project.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.file.service.ArchiveFileService;
import com.smate.core.base.utils.file.FileUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.prj.dao.project.PrjFulltextDao;
import com.smate.web.prj.model.common.PrjFulltext;
import com.smate.web.prj.service.project.PrjFulltextService;

/**
 * 项目全文服务实现类
 * 
 * @author houchuanjie
 * @date 2018年3月23日 上午10:03:28
 */
@Service("prjFulltextService")
@Transactional(rollbackOn = Exception.class)
public class PrjFulltextServiceImpl implements PrjFulltextService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PrjFulltextDao prjFulltextDao;
  @Autowired
  private ArchiveFileService archiveFileService;

  @Override
  public void saveOrUpdate(Long prjId, Long fulltextFileId) {
    try {
      PrjFulltext prjFulltext = Optional.ofNullable(prjFulltextDao.get(prjId)).orElseGet(() -> {
        PrjFulltext fulltext = new PrjFulltext();
        fulltext.setFulltextFileId(fulltextFileId);
        fulltext.setFulltextNode(1);
        fulltext.setPrjId(prjId);
        return fulltext;
      });
      Optional.ofNullable(archiveFileService.getArchiveFileById(fulltextFileId)).ifPresent(af -> {
        prjFulltext.setFulltextFileExt(FileUtils.getFileNameExtensionStr(af.getFileName()));
        prjFulltext.setFulltextNode(NumberUtils.INTEGER_ONE);
      });
      prjFulltext.setFulltextFileId(fulltextFileId);
      prjFulltextDao.save(prjFulltext);
    } catch (Exception e) {
      logger.error("更新或保存新增的项目全文出错！prjId={}, fileId={}", prjId, fulltextFileId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void deleteIfExist(Long prjId) {
    try {
      Optional.ofNullable(prjId).ifPresent(id -> prjFulltextDao.deleteByPrjId(id));
    } catch (Exception e) {
      logger.error("删除的项目全文记录出错！prjId={}", prjId, e);
      throw new ServiceException(e);
    }
  }

}
