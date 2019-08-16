package com.smate.center.job.business.pub.service;

import com.smate.center.job.common.exception.ServiceException;

/**
 * @author houchuanjie
 * @date 2018/05/10 15:10
 */
public interface SnsPubFulltextService {

    /**
     * 更新全文图片缩略图路径
     * @param pubId
     * @param imgPath
     * @throws ServiceException
     */
    void updateFulltextImagePath(Long pubId, String imgPath) throws ServiceException;
}
