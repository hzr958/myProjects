package com.smate.web.group.service.grp.label;

import java.util.List;

import com.smate.web.group.action.grp.form.GrpFileForm;
import com.smate.web.group.action.grp.form.GrpLabelForm;
import com.smate.web.group.action.grp.form.GrpLabelShowInfo;
import com.smate.web.group.model.grp.label.GrpFileLabel;
import com.smate.web.group.model.grp.label.GrpLabel;

/**
 * 群组文件标签服务接口
 * 
 * @author aijiangbin
 *
 */
public interface GrpFileLabelService {

  /**
   * 查找群组文件标签集合
   * 
   * @param grpFileId
   * @return
   * @throws Exception
   */
  public List<GrpFileLabel> findLabelByFileId(GrpLabelForm form) throws Exception;

  /**
   * 给文件添加标签服务接口
   * 
   * @param form
   * @throws Exception
   */
  public void addFileLabel(GrpLabelForm form) throws Exception;

  /**
   * 得到文件添加标签
   * 
   * @param form
   * @throws Exception
   */
  public GrpFileLabel getFileLabelById(GrpLabelForm form) throws Exception;

  /**
   * 删除文件添加标签
   * 
   * @param form
   * @throws Exception
   */
  public void delFileLabelById(GrpLabelForm form) throws Exception;

  /**
   * 得到所有的文件标签
   * 
   * @param form
   * @return
   * @throws Exception
   */
  public List<GrpLabelShowInfo> getAllFileLabel(GrpLabelForm form) throws Exception;

  /**
   * 得到所有的文件标签,排除该文件拥有的标签
   * 
   * @param form
   * @return
   * @throws Exception
   */
  public List<GrpLabelShowInfo> getAllLabelExcludeOwner(GrpLabelForm form) throws Exception;

  /**
   * 保存上传文件的，标签
   * 
   * @param form
   * @throws Exception
   */
  public void saveUploadFileLabel(GrpFileForm form) throws Exception;

}
