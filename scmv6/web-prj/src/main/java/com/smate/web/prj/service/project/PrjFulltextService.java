package com.smate.web.prj.service.project;

/**
 * 项目全文服务接口
 * 
 * @author houchuanjie
 * @date 2018年3月23日 上午10:02:42
 */
public interface PrjFulltextService {

  /**
   * 新增或更新项目全文记录
   *
   * @author houchuanjie
   * @date 2018年3月23日 上午10:23:00
   * @param prjId
   * @param fulltextFileId
   */
  void saveOrUpdate(Long prjId, Long fulltextFileId);

  /**
   * 删除全文记录
   *
   * @author houchuanjie
   * @date 2018年3月23日 上午10:23:38
   * @param prjId
   */
  void deleteIfExist(Long prjId);
}
