package com.smate.center.open.service.data.inspg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.model.inspg.Inspg;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.center.open.service.inspg.InspgModuleService;
import com.smate.core.base.utils.service.consts.SysDomainConst;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * Open系统 获取申请联盟机构信息
 * 
 * @author AiJiangBin
 * 
 */
@Transactional(rollbackFor = Exception.class)
public class OpenGetUnionInspglist extends ThirdDataTypeBase {

  @Resource
  private InspgModuleService inspgModuleService;

  @Resource
  private SysDomainConst sysDomainConst;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    String token = paramet.get(OpenConsts.MAP_TOKEN).toString();
    Long openId = NumberUtils.toLong(paramet.get(OpenConsts.MAP_OPENID).toString());
    Long psnId = NumberUtils.toLong(paramet.get(OpenConsts.MAP_PSNID).toString());
    List<Inspg> inspgList = inspgModuleService.ManagerInspgList(psnId);
    // 所有返回数据 并须封装在list<map>里面
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

    if (inspgList != null && inspgList.size() > 0) {
      for (int i = 0; i < inspgList.size(); i++) {
        Inspg inspg = inspgList.get(i);
        Map<String, Object> data = new HashMap<String, Object>();
        inspgToMap(data, inspg);
        dataList.add(data);
      }
    }

    temp.put(OpenConsts.MAP_DATA, dataList);
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    temp.put(OpenConsts.MAP_OPENID, openId);
    temp.put(OpenConsts.RESULT_MSG, "获取申请联盟机构信息成功");// 响应成功

    return temp;
  }

  /**
   * 对象转换成map
   * 
   * @param data
   * @param inspg
   */
  public void inspgToMap(Map<String, Object> data, Inspg inspg) {

    if (inspg.getId() != null) {
      String des3InspgId = ServiceUtil.encodeToDes3(inspg.getId().toString());
      data.put("des3InspgId", des3InspgId);
    } else {
      data.put("des3InspgId", "");
    }

    data.put("ins_name", inspg.getName() == null ? "" : inspg.getName().toString());
    data.put("logo_url",
        inspg.getLogoUrl() == null ? "" : sysDomainConst.getSnsDomain() + inspg.getLogoUrl().toString());
    data.put("create_date", inspg.getCreateTime() == null ? "" : inspg.getCreateTime().toString());

  }



}
