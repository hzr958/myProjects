package com.smate.sie.center.open.service.dept;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.cache.OpenCacheService;
import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.dao.data.OpenTokenServiceConstDao;
import com.smate.center.open.exception.OpenException;
import com.smate.center.open.model.OpenTokenServiceConst;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.sie.center.open.dao.dept.ImportThirdUnitsDao;
import com.smate.sie.center.open.dao.dept.ImportThirdUnitsErrorDao;
import com.smate.sie.center.open.model.dept.ImportThirdUnits;
import com.smate.sie.center.open.model.dept.ImportThirdUnitsError;
import com.smate.sie.center.open.model.dept.ImportThirdUnitsPK;

@Transactional(rollbackFor = Exception.class)
public class SyncUnitServiceImpl extends ThirdDataTypeBase {

  @Autowired
  private OpenCacheService openCacheService;
  @Autowired
  private OpenTokenServiceConstDao openTokenServiceConstDao;
  @Autowired
  private ImportThirdUnitsDao importThirdUnitsDao;
  @Autowired
  private ImportThirdUnitsErrorDao importThirdUnitsErrorDao;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    Object data = paramet.get(OpenConsts.MAP_DATA);
    if (data == null || StringUtils.isEmpty(data.toString())) {
      logger.error("Open系统-接收接口-输入数据不能为空");
      temp = super.errorMap("Open系统-接收接口-输入数据不能为空", paramet, "Open系统-接收接口-输入数据不能为空");
      return temp;
    }
    if (!checkIpCount((String) paramet.get("userIP"))) {
      logger.error("Open系统-该ip访问次数已达到上限");
      temp = super.errorMap("Open系统-该ip访问次数已达到上限", paramet, "Open系统-该ip访问次数已达到上限");
      return temp;
    }
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    Object mapData = paramet.get(OpenConsts.MAP_DATA);
    String token = paramet.get(OpenConsts.MAP_TOKEN).toString();
    String serviceType = paramet.get(OpenConsts.MAP_TYPE).toString();
    OpenTokenServiceConst openTokenServiceConst =
        openTokenServiceConstDao.findObjBytokenAndServiceType(token, serviceType);
    if (openTokenServiceConst.getInsId() == null || openTokenServiceConst.getInsId() == 0) {
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
      temp.put(OpenConsts.RESULT_MSG, "单位id不能为空，请检查是否已配置单位id");
      return temp;
    }
    Long insId = openTokenServiceConst.getInsId();
    List<Map> dataList = JacksonUtils.jsonToList(mapData.toString());
    int totalCount = 0;// 总记录数
    int successCount = 0;// 成功数
    if (CollectionUtils.isNotEmpty(dataList)) {
      try {
        importThirdUnitsDao.deleteImportThirdUnitsByInsId(insId);
      } catch (Exception e) {
        logger.error("删除单位第三方人员信息失败了喔,insId={}", insId, e);
      }
      totalCount = dataList.size();
      for (Map<String, Object> dataMap : dataList) {
        String unitName = ObjectUtils.toString(dataMap.get("unit_name"));
        String unitId = ObjectUtils.toString(dataMap.get("unit_id"));
        String pid = ObjectUtils.toString(dataMap.get("pid"));
        try {
          this.isBlank("unit_id", unitId);// unit_id必填
          this.isBlank("unit_name", unitName);// unit_name必填
          ImportThirdUnits importThirdUnits = new ImportThirdUnits();
          importThirdUnits.setPk(new ImportThirdUnitsPK(this.checkLength(unitId, 20), insId));
          importThirdUnits.setZhName(this.checkLength(unitName, 200));
          if (StringUtils.isNotBlank(pid)) {
            importThirdUnits.setSuperUnitId(this.checkLength(pid, 20));
          }
          importThirdUnits.setCreateDate(new Date());
          importThirdUnitsDao.save(importThirdUnits);
          successCount++;
        } catch (Exception e) {
          logger.error("保存第三方部门信息出错了喔,insId:{},unitId:{},unitName:{},pid:{}",
              new Object[] {insId, unitId, unitName, pid, e});
          ImportThirdUnitsError importThirdUnitsError = new ImportThirdUnitsError();
          importThirdUnitsError.setInsId(insId);
          importThirdUnitsError.setParams(paramet.toString());
          importThirdUnitsError.setErrorMsg("insId:" + insId + ",unitId:" + unitId + ",unitName:" + unitName + ",pid:"
              + pid + ",msg:" + e.getMessage() + "######" + e.toString());
          importThirdUnitsError.setCreateDate(new Date());
          importThirdUnitsErrorDao.save(importThirdUnitsError);
        }
      }
    }
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("count", "共" + totalCount + "条记录，其中导入成功" + successCount + "条");
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    temp.put(OpenConsts.RESULT_MSG, "scm-0000");// 响应成功
    temp.put(OpenConsts.RESULT_DATA, map);
    return temp;
  }

  private boolean checkIpCount(String ip) {
    Integer ipCount = 10;
    Integer count = 0;
    // 先从缓存中获取ip对应的访问次数
    if (null != openCacheService.get("ipCountUnit", ip)) {
      count = (Integer) openCacheService.get("ipCountUnit", ip);
    }
    // ip限制判断
    if (count == 0) {
      openCacheService.put("ipCountUnit", 10 * 60, ip, 1);
    } else {
      openCacheService.put("ipCountUnit", 10 * 60, ip, count + 1);
    }

    if (count >= ipCount) {
      return false;
    }
    return true;
  }

  /**
   * 判断是否为空.
   * 
   * @param name 字段名
   * @param value 字段值
   * @return
   * @throws OpenException
   */
  private boolean isBlank(String name, String value) throws OpenException {
    if (StringUtils.isBlank(value)) {
      throw new OpenException(name + "不能为空");
    } else {
      return false;
    }
  }

  /**
   * 检查字符串长度.
   * 
   * @param str 待检查字符串
   * @param max 最大长度
   * @return
   * @throws OpenException
   */
  private String checkLength(String str, int max) throws OpenException {
    if (str.length() > max) {
      throw new OpenException(str + "长度大于" + max);
    } else {
      return str;
    }
  }
}
