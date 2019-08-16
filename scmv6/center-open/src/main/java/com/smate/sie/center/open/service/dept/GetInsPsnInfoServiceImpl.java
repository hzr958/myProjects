package com.smate.sie.center.open.service.dept;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.dao.data.OpenTokenServiceConstDao;
import com.smate.center.open.dao.data.OpenUserUnionDao;
import com.smate.center.open.model.OpenTokenServiceConst;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.utils.dao.security.UserDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.cas.security.User;
import com.smate.core.base.utils.model.wechat.OpenUserUnion;
import com.smate.sie.center.open.dao.dept.Sie6InsPsnDao;
import com.smate.sie.center.open.model.dept.SiePsnIns;

/**
 * 获取单位人员信息.
 * 
 * @author xys
 *
 */
public class GetInsPsnInfoServiceImpl extends ThirdDataTypeBase {

  @Autowired
  private OpenTokenServiceConstDao openTokenServiceConstDao;
  @Autowired
  private Sie6InsPsnDao sie6InsPsnDao;
  @Autowired
  private UserDao userDao;
  @Autowired
  private OpenUserUnionDao openUserUnionDao;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    Object data = paramet.get(OpenConsts.MAP_DATA);
    if (data == null || StringUtils.isEmpty(data.toString())) {
      logger.error("Open系统-接收接口-输入数据不能为空");
      temp = super.errorMap("Open系统-接收接口-输入数据不能为空", paramet, "Open系统-接收接口-输入数据不能为空");
      return temp;
    }

    Map<String, Object> dataMap = JacksonUtils.jsonToMap(data.toString());
    if (dataMap == null) {
      logger.error("获取单位人员服务 参数 格式不正确 不能转换成map");
      temp = super.errorMap("获取单位人员服务 参数 格式不正确 不能转换成map", paramet, "");
      return temp;
    }
    if (dataMap.get("pageNo") == null || !NumberUtils.isDigits(dataMap.get("pageNo").toString())) {
      logger.error("获取单位人员服务 参数 pageNo 不能为空或非数字");
      temp = super.errorMap("获取单位人员服务 参数 pageNo 不能为空或非数字", paramet, "");
      return temp;
    }
    if (dataMap.get("pageSize") == null || !NumberUtils.isDigits(dataMap.get("pageSize").toString())) {
      logger.error("获取单位人员服务 参数 pageSize 不能为空或非数字");
      temp = super.errorMap("获取单位人员服务 参数 pageSize 不能为空或非数字", paramet, "");
      return temp;
    }

    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    Object data = paramet.get(OpenConsts.MAP_DATA);
    String token = paramet.get(OpenConsts.MAP_TOKEN).toString();
    String serviceType = paramet.get(OpenConsts.MAP_TYPE).toString();
    OpenTokenServiceConst openTokenServiceConst =
        openTokenServiceConstDao.findObjBytokenAndServiceType(token, serviceType);
    if (openTokenServiceConst.getInsId() == null || openTokenServiceConst.getInsId() == 0) {
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
      temp.put(OpenConsts.RESULT_MSG, "单位id不能为空，请检查是否已配置单位id");
      return temp;
    }
    Map<String, Object> dataMap = JacksonUtils.jsonToMap(data.toString());
    dataMap.put("insId", openTokenServiceConst.getInsId());
    Page<SiePsnIns> page = new Page<SiePsnIns>();
    page.setPageNo(Integer.valueOf(dataMap.get("pageNo").toString()));
    page.setPageSize(Integer.valueOf(dataMap.get("pageSize").toString()));
    page = sie6InsPsnDao.getInsPsns(dataMap, page);
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    // 构建返回分页参数、页数与记录数
    Map<String, Object> pageMap = new HashMap<String, Object>();
    pageMap.put("page_no", ObjectUtils.toString(page.getPageNo()));
    pageMap.put("page_size", ObjectUtils.toString(page.getPageSize()));
    pageMap.put("total_pages", ObjectUtils.toString(page.getTotalPages()));
    pageMap.put("total_count", ObjectUtils.toString(page.getTotalCount()));
    dataList.add(pageMap);

    // 构建返回数据
    if (!CollectionUtils.isEmpty(page.getResult())) {
      for (SiePsnIns psn : page.getResult()) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("name", StringUtils.isBlank(psn.getZhName()) ? ObjectUtils.toString(psn.getEnName())
            : ObjectUtils.toString(psn.getZhName()));
        resultMap.put("email", ObjectUtils.toString(psn.getEmail()));
        User user = userDao.get(psn.getPk().getPsnId());
        resultMap.put("login_name", user == null ? "" : user.getLoginName());
        OpenUserUnion openUserUnion = openUserUnionDao.getOpenUserUnionByPsnIdAndToken(psn.getPk().getPsnId(), token);
        resultMap.put("open_id", openUserUnion == null ? "" : openUserUnion.getOpenId());
        dataList.add(resultMap);
      }
    }
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    temp.put(OpenConsts.RESULT_MSG, "scm-0000");// 响应成功
    temp.put(OpenConsts.RESULT_DATA, dataList);
    return temp;
  }

}
