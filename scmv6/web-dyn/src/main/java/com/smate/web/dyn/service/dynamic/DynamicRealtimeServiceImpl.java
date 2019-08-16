package com.smate.web.dyn.service.dynamic;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.dyn.dao.DynamicContentDao;
import com.smate.core.base.dyn.dao.DynamicMsgDao;
import com.smate.core.base.dyn.dao.DynamicRelationDao;
import com.smate.core.base.dyn.model.DynamicContent;
import com.smate.core.base.dyn.model.DynamicMsg;
import com.smate.core.base.dyn.model.DynamicRelation;
import com.smate.core.base.dyn.model.DynamicRelationPk;
import com.smate.core.base.utils.constant.dyn.DynTemplateConstant;
import com.smate.core.base.utils.exception.DynException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.template.SmateFreeMarkerTemplateUtil;
import com.smate.web.dyn.dao.dynamic.DynStatisticsDao;
import com.smate.web.dyn.dao.dynamic.DynamicShareResDao;
import com.smate.web.dyn.form.dynamic.DynamicForm;
import com.smate.web.dyn.model.dynamic.DynStatistics;

/**
 * 实时动态生成实现类，生成动态id，保存动态的相关信息。
 * 
 * @author zk
 *
 */
@Service("dynamicRealtimeService")
@Transactional(rollbackFor = Exception.class)
public class DynamicRealtimeServiceImpl implements DynamicRealtimeService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private DynamicPrepareService dynamicPrePareService;
  @Autowired
  private DynamicMsgDao dynamicMsgDao;
  @Autowired
  private DynamicRelationDao dynamicRelationDao;
  @Autowired
  private DynamicContentDao dynamicContentDao;
  @Autowired
  private DynStatisticsDao dynStatisticsDao;
  @Autowired
  private SmateFreeMarkerTemplateUtil smateFreeMarkerTemplateUtil;
  @Autowired
  private DynamicShareResDao dynamicShareResDao;

  @Override
  public void dynamicRealtime(DynamicForm form) throws DynException {
    try {
      if (form.getParentDynId() == null && form.getDynId() != null) {
        form.setParentDynId(dynamicMsgDao.getParentDynId(form.getDynId()));
      }
      form.setDynId(dynamicMsgDao.createDynId());
      // 如果没有父动态id，则把当前动态id给父动态id
      if (form.getParentDynId() == null) {
        form.setParentDynId(form.getDynId());
      }
      // 构造动态数据
      dynamicPrePareService.checkAndDealData(form);
      // 保存动态数据
      this.saveDynamicMsg(form);
      this.saveDynamicRelation(form);
      this.saveDynamicContent(form);
      this.saveDynStatistics(form);
    } catch (Exception e) {
      logger.error("生成动态数据出错,DynId=" + form.getDynId(), e);
      throw new DynException(e);
    }
  }

  /***
   * 动态数据初始数据和分享数据的保存
   * 
   * @param form
   */
  private void saveDynStatistics(DynamicForm form) {
    DynStatistics DynStatistics = new DynStatistics();
    DynStatistics.setDynId(form.getDynId());
    DynStatistics.setAwardCount(0);
    DynStatistics.setCommentCount(0);
    DynStatistics.setShareCount(0);
    dynStatisticsDao.save(DynStatistics);
  }

  // 保存DynamicMsg
  private void saveDynamicMsg(DynamicForm form) throws DynException {
    try {
      if (form.getResType() == null) {
        throw new DynException("resType为空");
      }
      DynamicMsg msg = new DynamicMsg();
      msg.setDynId(form.getDynId());
      msg.setDynType(form.getDynType());
      msg.setProducer(form.getPsnId());
      msg.setPermission(0);
      msg.setDynTmp(0);
      msg.setDelstatus(0);
      msg.setRelDealStatus(0);
      Date now = new Date();
      msg.setCreateDate(now);
      msg.setUpdateDate(now);
      msg.setFromType(1);// TODO 动态来源类型id,1:个人,2:群组,3:机构主页,4..(用于查询过滤)
      msg.setTargetId(0L);// TODO 群组id/机构Id,默认0
      msg.setSameFlag(form.getParentDynId() == null ? form.getDynId() : form.getParentDynId());
      msg.setResId(form.getResId());
      if (form.getPlatform() != null) {
        msg.setPlatform(form.getPlatform());
      }
      msg.setResType(form.getResType());
      dynamicMsgDao.save(msg);
    } catch (Exception e) {
      throw new DynException("保存动态基础信息数据出错..", e);
    }
  }

  // 保存DynamicRelation
  private void saveDynamicRelation(DynamicForm form) throws DynException {
    try {
      DynamicRelation relation = new DynamicRelation();
      DynamicRelationPk drPk = new DynamicRelationPk(form.getDynId(), form.getPsnId());
      relation.setId(drPk);
      relation.setDealStatus(0);
      dynamicRelationDao.save(relation);
    } catch (Exception e) {
      throw new DynException("保存动态关系数据出错..", e);
    }
  }

  // 保存DynamicContent
  private void saveDynamicContent(DynamicForm form) throws DynException {
    try {
      String contentZh = smateFreeMarkerTemplateUtil.produceTemplate(form.getDynMap(),
          form.getDynMap().get(DynTemplateConstant.DYN_TEMPLATE_ZH).toString());
      String contentEn = smateFreeMarkerTemplateUtil.produceTemplate(form.getDynMap(),
          form.getDynMap().get(DynTemplateConstant.DYN_TEMPLATE_EN).toString());
      String mobileContentZh = smateFreeMarkerTemplateUtil.produceTemplate(form.getDynMap(),
          form.getDynMap().get(DynTemplateConstant.MOBILE_DYN_TEMPLATE_ZH).toString());
      String mobileContentEn = smateFreeMarkerTemplateUtil.produceTemplate(form.getDynMap(),
          form.getDynMap().get(DynTemplateConstant.MOBILE_DYN_TEMPLATE_EN).toString());

      DynamicContent dynamicContent = new DynamicContent();
      dynamicContent.setDynId(form.getDynId().toString());
      dynamicContent.setDynContentEn(contentEn);
      dynamicContent.setDynContentZh(contentZh);
      dynamicContent.setMobileDynContentEn(mobileContentEn);
      dynamicContent.setMobileDynContentZh(mobileContentZh);
      dynamicContent.setResDetails(JacksonUtils.mapToJsonStr(form.getDynMap()));// 把map内容转换成json字符串存入表中
      dynamicContentDao.save(dynamicContent);
    } catch (Exception e) {
      throw new DynException("保存动态模板数据出错..", e);
    }
  }

}
