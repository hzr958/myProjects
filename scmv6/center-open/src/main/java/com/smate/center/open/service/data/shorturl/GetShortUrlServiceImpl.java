package com.smate.center.open.service.data.shorturl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.center.open.service.shorturl.BuildShortUrlService;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.dao.shorturl.OpenShortUrlDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.shorturl.OpenShortUrl;

/**
 * 短地址生成服务实现
 * 
 * @author ajb
 *
 */

@Transactional(rollbackFor = Exception.class)
public class GetShortUrlServiceImpl extends ThirdDataTypeBase {

  private Map<String, BuildShortUrlService> shortUrlServiceMap;

  public Map<String, BuildShortUrlService> getShortUrlServiceMap() {
    return shortUrlServiceMap;
  }

  public void setShortUrlServiceMap(Map<String, BuildShortUrlService> shortUrlServiceMap) {
    this.shortUrlServiceMap = shortUrlServiceMap;
  }

  @Autowired
  private OpenShortUrlDao openShortUrlDao;

  /**
   * 
   * 校验参数 业务参数格式 data={"type":"X","shortUrlParamet":"XXX","createPsnId":""}
   * 
   * type类型 有一个枚举类 定义了 类型与 真实连接的关系 必要 shortUrlParamet 访问真实地址的参数 ，可无 ,如果有 则必须是 json字符串格式 createPsnId
   * 短地址创建人 必要 0 为系统参数，1 为匿名用户 产生 这个参数可以扩展
   * 
   * 
   */
  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {

    Map<String, Object> temp = new HashMap<String, Object>();

    Object data = paramet.get(OpenConsts.MAP_DATA);
    if (data != null && data.toString().length() > 0) {
      Map<String, Object> dataMap = JacksonUtils.jsonToMap(data.toString());
      if (dataMap != null) {
        paramet.putAll(dataMap);
      }
    }

    if (paramet.get(ShortUrlConst.CREATE_PSN_ID) == null) {
      logger.error("获取短地址，服务参数  createPsnId 类型有误或不能为空 createPsnId为：  " + paramet.get("createPsnId"));
      temp = super.errorMap(OpenMsgCodeConsts.SCM_286, paramet, "SCM_286  获取短地址,服务参数  createPsnId 不能为空");
      return temp;
    }
    if (paramet.get(ShortUrlConst.SHORT_URL_PARAMET) != null
        && !JacksonUtils.isJsonString(paramet.get(ShortUrlConst.SHORT_URL_PARAMET).toString())) {
      logger.error("获取短地址，服务参数  shortUrlParamet 必须是json格式 ");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_287, paramet, "scm-287  获取短地址，服务参数  shortUrlParamet 必须是json格式");
      return temp;
    }

    BuildShortUrlService shortUrlService = shortUrlServiceMap.get(paramet.get(ShortUrlConst.TYPE));
    if (shortUrlService == null) {
      logger.error("获取短地址，服务参数  type 类型不能为空或者类型出错 type为：  " + paramet.get("type"));
      temp = super.errorMap(OpenMsgCodeConsts.SCM_288, paramet, "scm-288  获取短地址，服务参数  type不能为空或者类型出错   ");
      return temp;
    }

    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    BuildShortUrlService shortUrlService = shortUrlServiceMap.get(paramet.get(ShortUrlConst.TYPE).toString());
    String shortUrl = shortUrlService.buildShortUrl(paramet);
    OpenShortUrl openShortUrl = new OpenShortUrl();
    openShortUrl.setCreateDate(new Date());
    openShortUrl.setCreatePsnId(NumberUtils.toLong(paramet.get(ShortUrlConst.CREATE_PSN_ID).toString()));
    openShortUrl.setRealUrlHash(paramet.get(ShortUrlConst.SHORT_URL_PARAMET).toString().hashCode());
    openShortUrl.setRealUrlParamet(paramet.get(ShortUrlConst.SHORT_URL_PARAMET).toString());
    openShortUrl.setShortUrl(shortUrl);
    openShortUrl.setType(paramet.get(ShortUrlConst.TYPE).toString());
    shortUrlService.buildShortUrlParam(openShortUrl);
    openShortUrlDao.save(openShortUrl);
    Map<String, Object> data = new HashMap<String, Object>();
    data.put(ShortUrlConst.SHORT_URL, shortUrl);
    dataList.add(data);
    return successMap("获取短地址数据成功", dataList);
  }

}
