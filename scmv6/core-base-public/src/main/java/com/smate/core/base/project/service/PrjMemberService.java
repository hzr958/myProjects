package com.smate.core.base.project.service;

import java.util.Optional;

import com.smate.core.base.project.model.PrjMember;

/**
 * 项目成员服务接口
 * 
 * @author houchuanjie
 * @date 2018年3月22日 下午5:58:12
 */
public interface PrjMemberService {
  /**
   * 移除项目成员
   *
   * @author houchuanjie
   * @date 2018年3月22日 下午6:43:18
   * @param pmId
   * @return
   */
  Optional<PrjMember> removeById(Long pmId);

  /**
   * 获取项目成员
   * 
   * @author houchuanjie
   * @date 2018年3月22日 下午6:43:08
   * @param pmId
   * @return
   */
  Optional<PrjMember> getPrjMember(Long pmId);

  /**
   * 保存项目成员
   * 
   * @author houchuanjie
   * @date 2018年3月22日 下午6:54:37
   * @param pm
   */
  void savePrjMember(PrjMember pm);
}
