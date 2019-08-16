package com.smate.web.group.service.grp.label;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.group.action.grp.form.GrpLabelForm;
import com.smate.web.group.dao.grp.label.GrpFileLabelDao;
import com.smate.web.group.dao.grp.label.GrpLabelDao;
import com.smate.web.group.model.grp.label.GrpLabel;

/**
 * 群组标签服务接口
 * 
 * @author aijiangbin
 *
 */
@Service("grpLabelService")
@Transactional(rollbackOn = Exception.class)
public class GrpLabelServiceImpl implements GrpLabelService {

  @Autowired
  private GrpLabelDao grpLabelDao;

  @Autowired
  private GrpFileLabelDao grpFileLabelDao;

  @Override
  public List<GrpLabel> getAllGrpLabel(GrpLabelForm form) throws Exception {
    List<GrpLabel> list = grpLabelDao.getAllGrpLabel(form.getGrpId());
    return list;
  }

  @Override
  public GrpLabel getLabelByGrpIdAndLabel(GrpLabelForm form) throws Exception {
    GrpLabel grpLabel = grpLabelDao.getLabelByGrpIdAndLabelName(form.getGrpId(), form.getLabelName());
    return grpLabel;
  }

  @Override
  public void createNewLabel(GrpLabelForm form) throws Exception {
    GrpLabel label = buildGrpLabel(form);
    grpLabelDao.save(label);
    form.setDes3LabelId(Des3Utils.encodeToDes3(label.getLabelId().toString()));

  }

  private GrpLabel buildGrpLabel(GrpLabelForm form) {
    GrpLabel label = new GrpLabel();
    label.setGrpId(form.getGrpId());
    label.setLabelName(form.getLabelName());
    label.setCreatePsnId(form.getPsnId());
    label.setCreateDate(new Date());
    label.setStatus(0);
    return label;
  }

  @Override
  public GrpLabel getGrplabelById(GrpLabelForm form) throws Exception {
    GrpLabel label = grpLabelDao.getLabelByGrpIdAndLabelId(form.getGrpId(), form.getLabelId());
    return label;
  }

  @Override
  public GrpLabel delGrplabel(GrpLabelForm form) throws Exception {
    grpLabelDao.deleteGrpLabel(form.getPsnId(), form.getLabelId());
    grpFileLabelDao.deleteGrpFileLabel(form.getPsnId(), form.getLabelId());
    return null;
  }



}
