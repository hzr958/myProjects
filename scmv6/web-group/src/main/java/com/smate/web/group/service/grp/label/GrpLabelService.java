package com.smate.web.group.service.grp.label;

import java.util.List;

import com.smate.web.group.action.grp.form.GrpLabelForm;
import com.smate.web.group.model.grp.label.GrpLabel;

/**
 * 群组标签服务接口
 * 
 * @author aijiangbin
 *
 */
public interface GrpLabelService {

  /**
   * 查询群组的标签通过grpId
   * 
   * @param form
   * @return
   * @throws Exception
   */
  public List<GrpLabel> getAllGrpLabel(GrpLabelForm form) throws Exception;

  /**
   * 通过lable和grpId查询群组标签
   * 
   * @param form
   * @return
   * @throws Exception
   */
  public GrpLabel getLabelByGrpIdAndLabel(GrpLabelForm form) throws Exception;

  /**
   * 创建新的群组标签
   * 
   * @param form
   */
  void createNewLabel(GrpLabelForm form) throws Exception;

  /**
   * 同过id获取label
   * 
   * @return
   * @throws Exception
   */
  public GrpLabel getGrplabelById(GrpLabelForm form) throws Exception;

  /**
   * 删除群组标签
   * 
   * @param form
   * @return
   * @throws Exception
   */
  public GrpLabel delGrplabel(GrpLabelForm form) throws Exception;


}
