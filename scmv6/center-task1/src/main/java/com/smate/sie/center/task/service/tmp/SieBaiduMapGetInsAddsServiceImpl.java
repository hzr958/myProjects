package com.smate.sie.center.task.service.tmp;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.smate.center.task.exception.TaskDupRecordException;
import com.smate.center.task.utils.HttpUtil;
import com.smate.core.base.utils.common.HashUtils;
import com.smate.core.base.utils.dao.consts.SieConstRegionDao;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.consts.SieConstRegion;
import com.smate.sie.center.task.dao.tmp.SieTaskInsBaiDuGetAddrDao;
import com.smate.sie.center.task.model.tmp.SieTaskInsBaiDuGetAddr;

/**
 * 通过百度地图api获取单位地址业务处理实现类
 * 
 * @author ztg
 * @date 2018-12-28
 */
@Service("sieBaiduMapGetInsAddsService")
@Transactional(rollbackFor = Exception.class)
public class SieBaiduMapGetInsAddsServiceImpl implements SieBaiduMapGetInsAddsService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private static String scmak = "ZRwruSYOitq8p9KPuwoxLZxgCdrNFF06";//
  // private static String sk = "9u1cuRm3s9sLKx9eupXFutygHhaaGjI9";
  private static String searchApi = "http://api.map.baidu.com/place/v2/search?";
  private static String decodeApi = "http://api.map.baidu.com/geocoder/v2/?";
  @Autowired
  private SieTaskInsBaiDuGetAddrDao sieInsBaiDuGetAddrDao;
  @Autowired
  private SieConstRegionDao sieConstRegionDao;

  @Override
  public List<Long> batchGetProcessedData(Integer size) {
    return sieInsBaiDuGetAddrDao.batchGetData(size);

  }


  /**
   * 开始处理
   */
  @Override
  public void startProcessing(Long Id, List<SieConstRegion> cnZhname, List<SieConstRegion> allName) throws Exception {

    SieTaskInsBaiDuGetAddr ins = sieInsBaiDuGetAddrDao.get(Id);
    String tmpInsName = ins.getTmpInsName();
    ins.setLanguage(1);
    if (!XmlUtil.containZhChar(tmpInsName)) {
      ins.setLanguage(2);
    }
    ins.setTmpInsNameHash(this.cleanPubAddrHash(tmpInsName));
    if (!containZhChar(tmpInsName)) {// 英文单位处理
      this.matchEnInsName(ins, allName);
      return;
    }

    try {
      this.getBaiduResult(tmpInsName, ins);
    } catch (TaskDupRecordException e) {
      sieInsBaiDuGetAddrDao.updateStatusById(Id, 5);// 存在多个地址
      return;
    } catch (Exception e) {
      sieInsBaiDuGetAddrDao.updateStatusById(Id, 4);// 接口状态异常
      return;
    }

    if (StringUtils.isBlank(ins.getFullAddress()) && StringUtils.isBlank(ins.getProvince())
        && StringUtils.isBlank(ins.getCity())) {
      this.insAddrStringMatch(ins, tmpInsName, cnZhname);
    } else {
      ins.setStatus(1);// api获取到地址
    }

  }

  /**
   * 根据单位名中的地址拆分获取
   * 
   * @param ins
   * @param tmpInsName
   * @param cnZhname
   * @author ztg
   * @date 2018-12-28
   */

  public void insAddrStringMatch(SieTaskInsBaiDuGetAddr ins, String tmpInsName, List<SieConstRegion> cnZhname) {

    tmpInsName = tmpInsName.replace("）", ")").replace("（", "(");
    // baidu map api获取不到地址则根据单位名中地址信息获取，排除含（） 分公司 字符的单位
    if (!tmpInsName.contains("(") && !tmpInsName.contains(")") && !tmpInsName.contains("分公司")) {
      for (SieConstRegion constRegion : cnZhname) {

        // 先不清理省市地区名称
        if (tmpInsName.contains(constRegion.getZhName())) {// 单位名中含有省市
          if (constRegion.getSuperRegionId() != 156) {
            ins.setCity(constRegion.getZhName());
            // 根据市superRegionid获取省信息
            ins.setProvince(sieConstRegionDao.getConstRegionById(constRegion.getSuperRegionId()).getZhName());
          } else {// 省或者直辖市
            ins.setProvince(constRegion.getZhName());
          }
          ins.setCountry("中国");
          ins.setStatus(6);// 机构名中拆分获取到地址
          break;
        } else {
          String sconstr = constRegion.getZhName().replace("市", "").replace("省", "").replace("地区", "");
          if (tmpInsName.contains(sconstr)) {// 单位名中含有省市
            if (constRegion.getSuperRegionId() != 156) {// 市
              ins.setCity(constRegion.getZhName());
              // 根据市superRegionid获取省信息
              ins.setProvince(sieConstRegionDao.getConstRegionById(constRegion.getSuperRegionId()).getZhName());
            } else {// 省或者直辖市

              ins.setProvince(constRegion.getZhName());
            }
            ins.setCountry("中国");
            ins.setStatus(6);// 机构名中拆分获取到地址
            break;
          }
        } ;

      }
    }
    if (StringUtils.isEmpty(ins.getProvince())) {
      ins.setStatus(3);// 获取不到地址信息
    }
    return;

  }

  /**
   * 
   * 对英文单位名进行处理
   */
  public void matchEnInsName(SieTaskInsBaiDuGetAddr ins, List<SieConstRegion> allName) {
    String tmpInsName = ins.getTmpInsName();
    ins.setStatus(3);
    // 英文地址中是否包含地址信息，对比常量表进行匹配
    for (SieConstRegion constRegion : allName) {
      String addrName;
      /**
       * 国内几个自治区的英文名需要特殊处理
       */
      try {
        addrName = constRegion.getEnName().toLowerCase();
      } catch (NullPointerException e) {
        logger.error("constRegion地址常量，该地址没有英文名,id:" + constRegion.getId());
        continue;
      }
      switch (constRegion.getRegionCode()) {
        case "640000":
          addrName = "ningxia";
          break;
        case "650000":
          addrName = "xinjiang";
          break;
        case "450000":
          addrName = "guangxi";
          break;
        case "540000":
          addrName = "tibet";
          break;
        case "150000":
          addrName = "innermongolia";
          break;

        default:
          break;
      }
      if (tmpInsName.contains(addrName)) {
        if (constRegion.getSuperRegionId() == null) {// 国外
          ins.setCountry(constRegion.getZhName());
          ins.setStatus(6);
          break;
        } else if (156 == constRegion.getSuperRegionId()) {// 省级地址
          ins.setCountry("中国");
          ins.setProvince(constRegion.getZhName());
          ins.setStatus(6);
          break;
        } else {// 市级地址
          // 根据市superRegionid获取省信息
          ins.setProvince(sieConstRegionDao.getConstRegionById(constRegion.getSuperRegionId()).getZhName());
          ins.setCountry("中国");
          ins.setCity(constRegion.getZhName());
          ins.setStatus(6);
          break;
        }
      }

    }

  }

  /**
   * 判断字符串是否有中文
   * 
   * @param name
   * @return
   */
  public static boolean containZhChar(String name) {
    if (StringUtils.isNotBlank(name)) {
      Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
      Matcher matcher = p.matcher(name);
      if (matcher.find()) {
        return true;
      }
    }
    return false;
  }

  @Override
  /**
   * 获取中国省市，省排序在前面
   */
  public List<SieConstRegion> getAllCNZhname() {
    return sieConstRegionDao.getAllCNZhname();

  }

  /**
   * 获取所有
   * 
   * @return
   * @author ztg
   * @date 2018年3月14日
   */
  @Override
  public List<SieConstRegion> getAllName() {
    return sieConstRegionDao.getAllName();
  }

  /**
   * 调用百度地图API接口， 获得单位 对应的 地址信息
   * 
   * @param tmpInsName
   * @param ins
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public void getBaiduResult(String tmpInsName, SieTaskInsBaiDuGetAddr ins) throws Exception {

    while (true) {
      String ak = scmak;
      String address = URLEncoder.encode(tmpInsName, "UTF-8");
      String resultType = "json";
      String region = URLEncoder.encode("中国", "UTF-8");
      String params = "query=" + address + "&output=" + resultType + "&ak=" + ak + "&region=" + region;
      String jsonStr = HttpUtil.sendGet(searchApi + params, "UTF-8");
      Map<String, Object> jsonToMap = JacksonUtils.jsonToMap(jsonStr);
      Integer status = (Integer) jsonToMap.get("status");
      if (status != 0) {
        if (status == 302) {
          logger.info("百度地址检索接口天配额超限！ak:" + ak);
          continue;
          // throw new Exception("百度地址检索接口天配额超限");
        } else if (status == 401) {
          logger.info("百度地址检索接口并发量超限！ak:" + ak);
          continue;
          // throw new Exception("百度地址检索接口并发量超限");
        } else {
          logger.info("百度地址检索接口返回错误！");
          throw new Exception("百度地址检索接口返回错误");
        }
      }
      List<Object> object = (List<Object>) jsonToMap.get("results");
      if (CollectionUtils.isEmpty(object)) {
        return;
      }
      Map<String, Object> result = (Map<String, Object>) object.get(0);

      Integer num = (Integer) result.get("num");
      if (num != null && num > 0) {
        String city = (String) result.get("name");
        ins.setCountry("中国");
        ins.setCity(city);
        ins.setProvince(sieConstRegionDao
            .getConstRegionById(sieConstRegionDao.getConstRegionByName(city).getSuperRegionId()).getZhName());
        throw new TaskDupRecordException("检索到多个地域信息");
      }

      // String fullAddr = (String) result.get("address");
      Map<String, Double> location = (Map<String, Double>) result.get("location");

      Double lat = location.get("lat");
      Double lng = location.get("lng");
      // http://api.map.baidu.com/geocoder/v2/?callback=renderReverse&location=39.934,116.329&output=json&pois=1&ak=您的ak
      // //GET请求

      String dparams = "location=" + lat + "," + lng + "&output=" + resultType + "&ak=" + ak;
      String djsonStr = HttpUtil.sendGet(decodeApi + dparams, "UTF-8");
      Map<String, Object> djsonToMap = JacksonUtils.jsonToMap(djsonStr);
      Integer dstatus = (Integer) jsonToMap.get("status");
      if (dstatus != 0) {
        logger.info("百度逆地址编码接口返回错误！");
        throw new Exception("百度逆地址编码接口返回错误");
      }
      Map<String, Object> dObject = (Map<String, Object>) djsonToMap.get("result");
      if (dObject.isEmpty()) {
        return;
      }
      String formattedAddress = (String) dObject.get("formatted_address");
      Map<String, Object> addressComponent = (Map<String, Object>) dObject.get("addressComponent");
      String country = (String) addressComponent.get("country");
      String province = (String) addressComponent.get("province");
      String city = (String) addressComponent.get("city");
      ins.setCountry(country);
      ins.setCity(city);
      ins.setProvince(province);
      ins.setFullAddress(formattedAddress);
      break;
    }

  }

  @Override
  public void updateStatusById(Long id, int status) {
    sieInsBaiDuGetAddrDao.updateStatusById(id, status);

  }

  public static Long cleanPubAddrHash(String pubAddr) {
    if (StringUtils.isBlank(pubAddr)) {
      return null;
    }
    // html解码
    pubAddr = HtmlUtils.htmlUnescape(pubAddr);
    pubAddr = XmlUtil.trimAllHtml(pubAddr);
    pubAddr = XmlUtil.filterForCompare(pubAddr);
    pubAddr = pubAddr.trim().toLowerCase();
    if (StringUtils.isBlank(pubAddr)) {
      return null;
    }
    return HashUtils.getStrHashCode(XmlUtil.getTrimBlankLower(pubAddr));
  }
}
