package com.smate.sie.center.open.service.dept;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.center.open.cache.OpenCacheService;
import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.dao.data.OpenTokenServiceConstDao;
import com.smate.center.open.exception.OpenException;
import com.smate.center.open.model.OpenTokenServiceConst;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.sie.center.open.dao.dept.ImportThirdPsnsDao;
import com.smate.sie.center.open.dao.dept.ImportThirdPsnsErrorDao;
import com.smate.sie.center.open.model.dept.ImportThirdPsns;
import com.smate.sie.center.open.model.dept.ImportThirdPsnsError;
import com.smate.sie.center.open.model.dept.ImportThirdPsnsPK;

@Transactional(rollbackFor = Exception.class)
public class SyncPsnServiceImpl extends ThirdDataTypeBase {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Value("${initOpen.restful.url}")
  private String SERVER_URL;
  @Autowired
  private OpenCacheService openCacheService;
  @Autowired
  private OpenTokenServiceConstDao openTokenServiceConstDao;
  @Autowired
  private ImportThirdPsnsDao importThirdPsnsDao;
  @Autowired
  private ImportThirdPsnsErrorDao importThirdPsnsErrorDao;

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
    List<Map> dataList = JacksonUtils.jsonToList(mapData.toString());
    String serviceType = paramet.get(OpenConsts.MAP_TYPE).toString();
    OpenTokenServiceConst openTokenServiceConst =
        openTokenServiceConstDao.findObjBytokenAndServiceType(token, serviceType);
    if (openTokenServiceConst.getInsId() == null || openTokenServiceConst.getInsId() == 0) {
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
      temp.put(OpenConsts.RESULT_MSG, "单位id不能为空，请检查是否已配置单位id");
      return temp;
    }
    Long insId = openTokenServiceConst.getInsId();
    int totalCount = 0;// 总记录数
    int successCount = 0;// 成功数
    if (CollectionUtils.isNotEmpty(dataList)) {
      try {
        importThirdPsnsDao.deleteImportThirdPsnsByInsId(insId);
      } catch (Exception e) {
        logger.error("删除单位第三方人员信息失败了喔,insId={}", insId, e);
      }
      totalCount = dataList.size();
      for (Map<String, Object> dataMap : dataList) {
        String zhName = ObjectUtils.toString(dataMap.get("zh_name"));
        String email = ObjectUtils.toString(dataMap.get("email"));
        String unitId = ObjectUtils.toString(dataMap.get("unit_id"));
        String position = ObjectUtils.toString(dataMap.get("position"));
        try {
          this.isBlank("email", email);// email必填
          this.isEmail(email);
          this.isBlank("zh_name", zhName);// zh_name必填
          ImportThirdPsns importThirdPsns = new ImportThirdPsns();
          importThirdPsns.setPk(new ImportThirdPsnsPK(this.checkLength(email, 50), insId));
          importThirdPsns.setZhName(this.checkLength(zhName, 50));
          if (StringUtils.isNotBlank(unitId)) {
            importThirdPsns.setUnitId(this.checkLength(unitId, 20));
          }
          importThirdPsns.setPosition(this.checkLength(position, 50));
          importThirdPsns.setCreateDate(new Date());
          importThirdPsnsDao.save(importThirdPsns);
          successCount++;
        } catch (Exception e) {
          logger.error("保存第三方人员信息出错了喔,insId:{},email:{},zhName:{}", new Object[] {insId, email, zhName, e});
          ImportThirdPsnsError importThirdPsnsError = new ImportThirdPsnsError();
          importThirdPsnsError.setInsId(insId);
          importThirdPsnsError.setParams(paramet.toString());
          importThirdPsnsError.setErrorMsg("insId:" + insId + ",email:" + email + ",zhName:" + zhName + ",msg:"
              + e.getMessage() + "######" + e.toString());
          importThirdPsnsError.setCreateDate(new Date());
          importThirdPsnsErrorDao.save(importThirdPsnsError);
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
    if (null != openCacheService.get("ipCount", ip)) {
      count = (Integer) openCacheService.get("ipCount", ip);
    }
    // ip限制判断
    if (count == 0) {
      openCacheService.put("ipCount", 10 * 60, ip, 1);
    } else if (count >= ipCount) {
      return false;
    } else {
      openCacheService.put("ipCount", 10 * 60, ip, count + 1);
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
   * 判断是否为合法email.
   * 
   * @param email
   * @return
   * @throws OpenException
   */
  private boolean isEmail(String email) throws OpenException {
    if (email.matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")) {
      return true;
    } else {
      throw new OpenException(email + "不是合法的email");
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
