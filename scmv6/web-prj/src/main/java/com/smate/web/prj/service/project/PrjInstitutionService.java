package com.smate.web.prj.service.project;

import java.util.List;

import com.smate.core.base.exception.ServiceException;
import com.smate.web.prj.vo.PrjInsVO;

/**
 * 项目依托单位服务接口
 * 
 * @author houchuanjie
 * @date 2018年3月26日 上午11:24:18
 */
public interface PrjInstitutionService {
  /**
   * 获取项目依托单位，根据关键字查询，限制结果数，并通过excludes参数进行过滤排除
   *
   * @author houchuanjie
   * @date 2018年3月26日 上午11:41:08
   * @param keyword 关键字
   * @param maxSize 最大条数
   * @param excludes 排除内容
   * @param psnId TODO
   * @return
   * @throws ServiceException
   */
  List<PrjInsVO> searchPrjIns(String keyword, int maxSize, String excludes, Long psnId) throws ServiceException;
}
