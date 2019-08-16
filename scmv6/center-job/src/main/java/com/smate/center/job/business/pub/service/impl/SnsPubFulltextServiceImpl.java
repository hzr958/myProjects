package com.smate.center.job.business.pub.service.impl;

import com.smate.center.job.business.pub.dao.SnsPubFulltextDao;
import com.smate.center.job.business.pub.service.SnsPubFulltextService;
import com.smate.center.job.common.exception.ServiceException;
import com.smate.core.base.file.service.ArchiveFileService;
import com.smate.core.base.utils.string.StringUtils;
import java.util.Optional;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author houchuanjie
 * @date 2018/05/10 15:11
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class SnsPubFulltextServiceImpl implements SnsPubFulltextService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private SnsPubFulltextDao snsPubFulltextDao;
    @Autowired
    private ArchiveFileService archiveFileService;

    @Override
    public void updateFulltextImagePath(Long pubId, String imgPath) throws ServiceException {
        try {
            Optional.ofNullable(snsPubFulltextDao.get(pubId)).ifPresent(pubFulltext -> {
                String oldImgPath = pubFulltext.getFulltextImagePath();
                if(StringUtils.isNotBlank(oldImgPath)){
                    archiveFileService.deleteFileByPath(oldImgPath);
                }
                pubFulltext.setFulltextImagePath(imgPath);
                pubFulltext.setFulltextImagePageIndex(1);
                snsPubFulltextDao.save(pubFulltext);
            });
        } catch (Exception e) {
            logger.error("更新个人库全文图片路径出错！pubId={}, imgPath='{}'", pubId, imgPath, e);
            throw new ServiceException(e);
        }
    }
}
