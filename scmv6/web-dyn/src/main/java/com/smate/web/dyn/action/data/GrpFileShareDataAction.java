package com.smate.web.dyn.action.data;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.dyn.form.dynamic.group.GroupDynProduceForm;
import com.smate.web.dyn.service.dynamic.group.GroupDynamicOptService;
import com.smate.web.dyn.service.group.GroupOptService;

/**
 * @description 移动端群组文件分享到群组动态
 * @author xiexing
 * @date 2019年5月18日
 */
public class GrpFileShareDataAction extends ActionSupport implements Preparable, ModelDriven<GroupDynProduceForm> {

  private static final Logger logger = LoggerFactory.getLogger(GrpFileShareDataAction.class);

  @Autowired
  private GroupOptService groupOptService;

  @Autowired
  private GroupDynamicOptService groupDynamicOptService;

  @Autowired
  private RestTemplate restTemplate;

  @Value("${domainscm}")
  private String domainScm;

  /**
   * 
   */
  private static final long serialVersionUID = 8191785555894253513L;

  private GroupDynProduceForm form;

  /**
   * 群组文件分享到群组动态
   */
  @Action("/dyndata/file/sharetogrp")
  public void shareToGrp() {
    Map<String, Object> result = new HashMap<String, Object>();
    if (verifyParam(form)) {
      try {
        // 0 陌生人 没有申请 ， 1 ，陌生人 已经申请 ，
        Integer permission = groupOptService.getRelationWithGroup(form.getPsnId(), form.getReceiverGrpId());
        Integer grpRela = groupOptService.getRelationWithGrp(form.getPsnId(), form.getReceiverGrpId());
        if ((permission == 0 || permission == 1) && grpRela == null) {
          result.put("status", "error");
          result.put("msg", "noPermission");
        } else {
          checkPublistNewDynForm(form);
          groupDynamicOptService.produceGroupDyn(form);
          // 文件分享统计数
          countShare(form);
          result.put("status", "success");
          result.put("msg", "share success");
        }
      } catch (Exception e) {
        logger.error("分享到群组动态出错,psnId={}", form.getPsnId(), e);
        result.put("status", "error");
        result.put("msg", "system error");
      }
    } else {
      result.put("status", "error");
      result.put("msg", "verify param fail");
    }
    Struts2Utils.renderJson(result, "encoding:UTF-8");
  }

  /**
   * 统计文件分享数
   * 
   * @param form
   */
  public void countShare(GroupDynProduceForm form) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
    params.add("des3GrpFileId", form.getDes3ResId());
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    HttpEntity<MultiValueMap<String, String>> requestEntity =
        new HttpEntity<MultiValueMap<String, String>>(params, headers);
    restTemplate.postForObject(domainScm + "/grpdata/file/countshare", requestEntity, Map.class);
  }

  /**
   * 构建参数
   * 
   * @param form
   */
  public void checkPublistNewDynForm(GroupDynProduceForm form) {
    if (StringUtils.isNotBlank(form.getTempType())) {
      return;
    }
    if (StringUtils.isNotBlank(form.getResType())) {// 说明是添加文件动态
      form.setTempType("ADDRES");
    } else {
      form.setTempType("PUBLISHDYN");
    }
  }

  /**
   * 校验分享群组动态参数,并构建参数
   * 
   * @return
   */
  public boolean verifyParam(GroupDynProduceForm form) {
    if (NumberUtils.isNotNullOrZero(form.getPsnId()) && NumberUtils.isNotNullOrZero(form.getGroupId())
        && StringUtils.isNotEmpty(form.getDes3ReceiverGrpId()) && StringUtils.isNotEmpty(form.getDes3ResId())) {
      return true;
    }
    return false;
  }


  @Override
  public GroupDynProduceForm getModel() {
    return form;
  }

  @Override
  public void prepare() throws Exception {
    if (form == null) {
      form = new GroupDynProduceForm();
    }
  }

}
