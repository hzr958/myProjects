package com.smate.web.group.service.grp.label;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.group.action.grp.form.GrpFileForm;
import com.smate.web.group.action.grp.form.GrpLabelForm;
import com.smate.web.group.action.grp.form.GrpLabelShowInfo;
import com.smate.web.group.dao.grp.label.GrpFileLabelDao;
import com.smate.web.group.dao.grp.label.GrpLabelDao;
import com.smate.web.group.model.grp.label.GrpFileLabel;
import com.smate.web.group.model.grp.label.GrpLabel;
import com.smate.web.group.service.grp.member.GrpRoleService;

/**
 * 群组文件标签服务接口
 * 
 * @author aijiangbin
 *
 */
@Service("grpFileLabelService")
@Transactional(rollbackOn = Exception.class)
public class GrpFileLabelServiceImpl implements GrpFileLabelService {


  @Autowired
  private GrpFileLabelDao grpFileLabelDao;

  @Autowired
  private GrpLabelDao grpLabelDao;
  @Autowired
  private GrpRoleService grpRoleService;


  @Override
  public List<GrpFileLabel> findLabelByFileId(GrpLabelForm form) throws Exception {
    List<GrpFileLabel> list = grpFileLabelDao.findLabelByFileId(form.getGrpFileId());
    return list;
  }

  @Override
  public void addFileLabel(GrpLabelForm form) throws Exception {
    // 如果已经添加，就返回
    GrpFileLabel grpFileLabel = grpFileLabelDao.findObjByFileIdAndLabelId(form.getGrpFileId(), form.getLabelId());
    if (grpFileLabel != null) {
      form.setResult(4);
      return;
    }
    GrpFileLabel fileLabel = new GrpFileLabel();
    fileLabel.setGrpLabelId(form.getLabelId());
    fileLabel.setCreatePsnId(form.getPsnId());
    fileLabel.setCreateDate(new Date());
    fileLabel.setGrpFileId(form.getGrpFileId());
    fileLabel.setStatus(0);
    grpFileLabelDao.save(fileLabel);
    form.setDes3FileLabelId(Des3Utils.encodeToDes3(fileLabel.getId().toString()));

  }

  @Override
  public GrpFileLabel getFileLabelById(GrpLabelForm form) throws Exception {
    GrpFileLabel fileLabel = grpFileLabelDao.get(form.getFileLabelId());
    return fileLabel;

  }

  @Override
  public void delFileLabelById(GrpLabelForm form) throws Exception {
    grpFileLabelDao.deleteGrpFileLabelById(form.getPsnId(), form.getFileLabelId());

  }


  @Override
  public List<GrpLabelShowInfo> getAllFileLabel(GrpLabelForm form) throws Exception {
    List<GrpLabelShowInfo> showInfoList = new ArrayList<GrpLabelShowInfo>();
    // 先获取群组所有的标签
    List<GrpLabel> list = grpLabelDao.getAllGrpLabel(form.getGrpId());
    if (list == null || list.size() == 0) {
      return showInfoList;
    }
    // 每个标签，获取文件的数量

    for (GrpLabel grpLabel : list) {
      GrpLabelShowInfo showInfo = new GrpLabelShowInfo();
      Integer count =
          grpFileLabelDao.statisticalFileLabelCount(grpLabel.getLabelId(), form.getGrpId(), form.getFileModuleType());
      showInfo.setResCount(count);
      showInfo.setDes3LabelId(Des3Utils.encodeToDes3(grpLabel.getLabelId().toString()));
      showInfo.setLabelName(grpLabel.getLabelName());
      // 是否有权限删除,管理员，和自己创建的要删除 ,1=群组拥有者,2=管理员,
      Integer role = grpRoleService.getGrpRole(form.getPsnId(), form.getGrpId());
      if (role == 1 || role == 2 || grpLabel.getCreatePsnId().longValue() == form.getPsnId().longValue()) {
        showInfo.setShowDel(true);
      }
      showInfoList.add(showInfo);
    }
    return showInfoList;
  }

  @Override
  public List<GrpLabelShowInfo> getAllLabelExcludeOwner(GrpLabelForm form) throws Exception {
    List<GrpLabelShowInfo> showInfoList = new ArrayList<GrpLabelShowInfo>();
    // 先获取群组标签
    List<GrpLabel> list = grpLabelDao.getAllGrpLabelExcludeOwner(form.getGrpId(), form.getGrpFileId());
    if (list == null || list.size() == 0) {
      return showInfoList;
    }
    // 构建信息
    for (GrpLabel grpLabel : list) {
      GrpLabelShowInfo showInfo = new GrpLabelShowInfo();
      showInfo.setDes3LabelId(Des3Utils.encodeToDes3(grpLabel.getLabelId().toString()));
      showInfo.setLabelName(grpLabel.getLabelName());
      showInfoList.add(showInfo);
    }
    return showInfoList;
  }

  @Override
  public void saveUploadFileLabel(GrpFileForm form) throws Exception {
    for (int i = 0; i < form.getGrpLabelIdList().size(); i++) {
      if (i == 10) {
        break;
      }
      GrpFileLabel label = new GrpFileLabel();
      label.setGrpFileId(form.getGrpFileId());
      label.setCreatePsnId(form.getPsnId());
      label.setCreateDate(new Date());
      label.setGrpLabelId(form.getGrpLabelIdList().get(i));
      label.setStatus(0);
      grpFileLabelDao.save(label);
    }

  }

}
