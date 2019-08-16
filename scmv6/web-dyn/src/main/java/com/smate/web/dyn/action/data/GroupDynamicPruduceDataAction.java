package com.smate.web.dyn.action.data;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.dyn.form.dynamic.group.GroupDynProduceForm;
import com.smate.web.dyn.service.dynamic.group.GroupDynamicOptService;
import com.smate.web.dyn.service.group.GroupOptService;
import com.smate.web.dyn.service.psn.InsInfoService;
import com.smate.web.dyn.service.psn.PersonQueryservice;

/**
 * 群组动态数据接口
 * 
 * @author wsn
 * @date May 30, 2019
 */
public class GroupDynamicPruduceDataAction implements Preparable, ModelDriven<GroupDynProduceForm> {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private GroupDynProduceForm form;
  @Autowired
  private PersonQueryservice personQueryservice;
  @Autowired
  private InsInfoService insInfoService;
  @Autowired
  private GroupOptService groupOptService;
  @Autowired
  private GroupDynamicOptService groupDynamicOptService;



  /**
   * 动态发布时当前人员信息
   * 
   * @return
   */
  @Action("/dyndata/publish/psninfo")
  public void dynPublish() {
    Map<String, String> result = new HashMap<String, String>();
    String status = "error";
    try {
      Long psnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(form.getDes3Psnid()), 0L);
      if (NumberUtils.isNotNullOrZero(psnId)) {
        Person person = personQueryservice.findPersonInsAndPos(psnId);
        if (person != null) {
          Map<String, String> insInfoMap = insInfoService.findPsnInsInfo(psnId);
          if (insInfoMap != null) {
            result.put("insInfo", insInfoMap.get(InsInfoService.INS_INFO_ZH));
          }
          result.put("avatars", person.getAvatars());
          result.put("psnName", personQueryservice.getPsnName(person, "zh_CN"));
          status = "success";
        }
      }
    } catch (Exception e) {
      logger.error("获取群组动态发布页面所需信息异常, psnId={}", form.getDes3Psnid(), e);
    }
    result.put("result", status);
    Struts2Utils.renderJson(result, "encoding: utf-8");
  }



  /**
   * 发布新群组动态
   * 
   * @return
   */
  @Action("/dyndata/grpdyn/publish")
  public void publishGrpDyn() {
    Map<String, Object> map = new HashMap<String, Object>();
    String status = "error";
    if (checkPublistNewDynNeedParam(form)) {
      form.setPsnId(NumberUtils.parseLong(Des3Utils.decodeFromDes3(form.getDes3Psnid()), 0L));
      try {
        // 0 陌生人 没有申请 ， 1 ，陌生人 已经申请 ，
        Integer permission = groupOptService.getRelationWithGroup(form.getPsnId(), form.getReceiverGrpId());
        Integer grpRela = groupOptService.getRelationWithGrp(form.getPsnId(), form.getReceiverGrpId());
        if ((permission == 0 || permission == 1) && grpRela == null) {
          status = "noPermission";
        } else {
          checkPublistNewDynForm(form);
          String result = groupDynamicOptService.produceGroupDynWithoutUpdateStatistics(form);
          // 群组动态统计数，另起url更新统计数，参考群组动态分享到群组、我的成果分享到群组
          status = result;
        }
      } catch (Exception e) {
        logger.error("进入动态发布页面异常：psnId={}, groupId={}", form.getPsnId(), form.getGroupId(), e);
      }
    }
    map.put("status", status);
    Struts2Utils.renderJson(map, "encoding:UTF-8");
  }



  /**
   * 检查必须要的参数参数
   * 
   * @param form
   */
  boolean checkPublistNewDynNeedParam(GroupDynProduceForm form) {
    if (NumberUtils.isNullOrZero(NumberUtils.parseLong(Des3Utils.decodeFromDes3(form.getDes3Psnid()), 0L))) {
      return false;
    } else if (NumberUtils.isNullOrZero(form.getGroupId()) && NumberUtils.isNullOrZero(form.getReceiverGrpId())) {
      return false;
    } else if (StringUtils.isBlank(form.getDynContent())
        && (StringUtils.isBlank(form.getDes3ResId()) && form.getResId() == null)) {
      return false;
    } else {
      return true;
    }

  }

  /**
   * 检查参数
   * 
   * @param form
   */
  void checkPublistNewDynForm(GroupDynProduceForm form) {
    if (StringUtils.isNotBlank(form.getTempType())) {
      return;
    }
    if (StringUtils.isNotBlank(form.getResType())) {// 说明是添加文件动态
      form.setTempType("ADDRES");
    } else {
      form.setTempType("PUBLISHDYN");
    }
  }



  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new GroupDynProduceForm();
    }
  }

  @Override
  public GroupDynProduceForm getModel() {
    return form;
  }

}
