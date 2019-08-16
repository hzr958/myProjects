package com.smate.sie.center.task.service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dyn.dao.base.DynamicContentDao;
import com.smate.center.task.dyn.dao.base.DynamicMsgDao;
import com.smate.center.task.dyn.model.base.DynamicMsg;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.xss.XssUtils;
import com.smate.sie.center.task.dao.SieDynMsgDao;
import com.smate.sie.center.task.dao.SieDynReceiverDao;
import com.smate.sie.center.task.dao.SieDynResDetailDao;
import com.smate.sie.center.task.dao.SieDynSyncTimeDao;
import com.smate.sie.center.task.model.SieDynMsg;
import com.smate.sie.center.task.model.SieDynReceiver;
import com.smate.sie.center.task.model.SieDynReceiverPk;
import com.smate.sie.center.task.model.SieDynResDetail;
import com.smate.sie.center.task.model.SieDynSyncTime;
import com.smate.sie.core.base.utils.dao.psn.SieInsPersonDao;

/**
 * 同步动态后台任务
 * 
 * @author yxy
 *
 */
@Service("sieSynSnsDynMsgService")
@Transactional(rollbackFor = Exception.class)
public class SieSynSnsDynMsgServiceImpl implements SieSynSnsDynMsgService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  /*
   * @Autowired private SieSynchronousSnsDynMsgService sieSynchronousSnsDynMsgService;
   */
  @Autowired
  private DynamicMsgDao dynamicMsgDao;
  @Autowired
  private SieDynSyncTimeDao sieDynSyncTimeDao;
  @Autowired
  private DynamicContentDao dynamicContentDao;
  @Autowired
  private SieDynReceiverDao sieDynReceiverDao;
  @Autowired
  private SieDynResDetailDao sieDynResDetailDao;
  @Autowired
  private SieDynMsgDao sieDynMsgDao;
  @Autowired
  private SieInsPersonDao sieInsPersonDao;
  @Autowired
  private PersonDao snsPersonDao;

  @Override
  public Map<String, Long> dealWithBusiness(Map<String, Long> paramMap) {
    if (!judgeParamer(paramMap)) {
      return null;
    }
    // 获取参数中的值
    Long first = paramMap.get("first");
    Long last = paramMap.get("last");
    // 对v_dynamic_msg表中按update_date进行倒序查询的数据进行分页
    SieDynSyncTime sieDynSyncTime = sieDynSyncTimeDao.get(1L);
    Date sieLastTime = sieDynSyncTime != null ? sieDynSyncTime.getUpdateTime() : null;
    List<DynamicMsg> dynList = dynamicMsgDao.getSieBusinessList(first, last, sieLastTime);

    // 如果是第一次插入数据或者是存在需要更新的数据，则更新最新时间。
    if (dynList.size() > 0) {
      // 获取队列中第一条数据中的日期
      Date newItemUpdate = dynList.get(0).getUpdateDate();
      if (sieDynSyncTime == null) {
        sieDynSyncTime = new SieDynSyncTime();
        sieDynSyncTime.setSyncId(1L);
        sieDynSyncTime.setUpdateTime(newItemUpdate);
        sieDynSyncTimeDao.save(sieDynSyncTime);
      } else if (newItemUpdate != null) {
        // 如果存在最新时间比现在时间还新的时候，保存时间
        if (newItemUpdate.after(sieLastTime)) {
          sieDynSyncTime.setUpdateTime(newItemUpdate);
          sieDynSyncTimeDao.save(sieDynSyncTime);
        }
      }
    } else {
      // 集合中没有数据
      return null;
    }

    // 处理动态
    for (DynamicMsg dynamicMsg : dynList) {
      syncDynMsg(dynamicMsg);
    }
    // 处理参数中的值
    first = first + last;
    last = ((Integer) dynList.size()).longValue();
    paramMap.replace("first", first);
    paramMap.replace("last", last);
    return paramMap;
  }

  public void syncDynMsg(DynamicMsg dynamicMsg) {
    Long snsPsnId = dynamicMsg.getProducer();
    // 获取这条记录所在的人员信息
    List<Long> insPsnList = sieInsPersonDao.findPsnInsIds(snsPsnId);
    // 如果该人的动态没加入机构，则不处理
    if (insPsnList != null && insPsnList.size() > 0) {
      // 新增表记录
      SieDynMsg dynMsg = this.addSieDynMsg(dynamicMsg);
      if (dynMsg != null && NumberUtils.isNotNullOrZero(dynMsg.getDynId())) {
        this.addSieDynReceiver(snsPsnId, insPsnList, dynMsg.getDynId());
        // 新增表记录
        this.addSieDynResDetail(dynamicMsg, dynMsg.getDynId());
      }
    }
  }

  /*
   * 获取创建的机构版个人动态id（dyn_msg表dyn_id主键字段）
   */
  private Long getBySnsDynId(DynamicMsg dynamicMsg) {
    Long sieDynId = null;
    if (NumberUtils.isNotNullOrZero(dynamicMsg.getDynId())) {
      SieDynMsg sieDynMsg = sieDynMsgDao.getBySnsDynId(dynamicMsg.getDynId());
      sieDynId = sieDynMsg == null ? null : sieDynMsg.getDynId();
    }
    return sieDynId;
  }

  /**
   * 动态-机构表
   * 
   * @param sieDynId
   */
  public boolean addSieDynReceiver(Long snsPsnId, List<Long> insPsnList, Long sieDynId) {
    for (Long insId : insPsnList) {
      // 更新动态接收者
      SieDynReceiverPk sieDynReceiverPk = new SieDynReceiverPk();
      sieDynReceiverPk.setDynId(sieDynId);
      sieDynReceiverPk.setInsId(insId);
      if (sieDynReceiverDao.get(sieDynReceiverPk) == null) {
        SieDynReceiver sieDynReceiver = new SieDynReceiver();
        sieDynReceiver.setPk(sieDynReceiverPk);
        sieDynReceiverDao.save(sieDynReceiver);
      } else {
        sieDynReceiverPk = null;
      }
    }
    return false;
  }

  /**
   * 动态信息详情表
   */
  public SieDynMsg addSieDynMsg(DynamicMsg dynamicMsg) {
    Long snsdynId = dynamicMsg.getDynId();
    Integer resType = dynamicMsg.getResType();
    SieDynMsg sieDynMsg = sieDynMsgDao.getBySnsDynId(snsdynId);
    if (sieDynMsg == null) {
      sieDynMsg = new SieDynMsg();
      sieDynMsg.setSnsDynId(snsdynId);
      sieDynMsg.setCreateDate(dynamicMsg.getCreateDate());
      sieDynMsg.setUpdateDate(dynamicMsg.getUpdateDate());
      sieDynMsg.setResId(dynamicMsg.getResId());
      sieDynMsg.setResType(dynamicMsg.getResType());
      // 分享基金信息 type=2
      if (resType.equals(25)) {
        sieDynMsg.setDynType(2L);
      }
      // 新增成果信息 type=1
      if (resType.equals(1)) {
        sieDynMsg.setDynType(1L);
      }
      // 获取人员信息:获取个人版中的该psnId所在的表person中的记录
      Long psnId = dynamicMsg.getProducer();
      Person person = snsPersonDao.get(psnId);
      sieDynMsg.setProducerAvatars(person.getAvatars());
      sieDynMsg.setProducerName(person.getName());
      sieDynMsg.setProducerPsnId(person.getPersonId());
      sieDynMsg.setDataFrom(1L);// 默认个人动态
      sieDynMsg.setStatus(1L);// 默认正常状态
      sieDynMsgDao.save(sieDynMsg);
    }
    return sieDynMsg;
  }


  /**
   * 给SieDynResDetail表中添加数据,JSON内容
   * 
   * @description 取值使用个人动态的dynId，存储使用机构动态主键sieDynId(关联)
   * @param sieDynId
   */
  public boolean addSieDynResDetail(DynamicMsg dynamicMsg, Long sieDynId) {
    Long snsdynId = dynamicMsg.getDynId();
    Integer resType = dynamicMsg.getResType();
    SieDynResDetail sieDynResDetail = sieDynResDetailDao.get(sieDynId);
    if (sieDynResDetail == null && resType.equals(25)) {
      // 分享基金信息
      String jsonContent = dynamicContentDao.getDynInfo(snsdynId);
      Map<String, Object> tempMap = synSnsFundingAgenciesContentToSie(StringUtils.isEmpty(jsonContent) ? null
          : JacksonUtils.isJsonString(jsonContent) ? JacksonUtils.jsonToMap(jsonContent) : null);
      String newJsonContent = JacksonUtils.mapToJsonStr(tempMap);
      sieDynResDetail = new SieDynResDetail();
      sieDynResDetail.setDynId(sieDynId);
      sieDynResDetail.setResDetail(newJsonContent);
      sieDynResDetailDao.save(sieDynResDetail);
    } else if (sieDynResDetail == null && resType.equals(1)) {
      // 新增成果
      // 同步新增成果信息
      String jsonContent = dynamicContentDao.getDynInfo(snsdynId);
      Map<String, Object> tempMap = synSnsNewPubContentToSie(StringUtils.isEmpty(jsonContent) ? null
          : JacksonUtils.isJsonString(jsonContent) ? JacksonUtils.jsonToMap(jsonContent) : null);
      String newJsonContent = JacksonUtils.mapToJsonStr(tempMap);
      sieDynResDetail = new SieDynResDetail();
      sieDynResDetail.setDynId(sieDynId);
      sieDynResDetail.setResDetail(newJsonContent);
      sieDynResDetailDao.save(sieDynResDetail);
    }
    return false;
  }



  /**
   * 将SNS库中的新增成果同步至SIE库中
   * 
   * 动态需要以下字段： 部门，人员名字，人员链接，人员头像 职称 ，
   * 
   * 业务名 全文缩略图，全文下载地址 成果名 成果详情页 作者名 成果详情信息
   */
  private Map<String, Object> synSnsNewPubContentToSie(Map<String, Object> map) {
    Map<String, Object> newMap = new LinkedHashMap<String, Object>();
    if (map != null) {
      // 资源ID
      newMap.put("resId", map.get("RES_ID"));
      // 标题
      newMap.put("title", map.get("LINK_TITLE_ZH"));
      // 成果详情
      newMap.put("briefDesc", map.get("PUB_DESCR_ZH"));
      // 成果全文图片
      newMap.put("fileImg", map.get("LINK_IMAGE"));
      // 作者名
      newMap.put("authorNames", xssReplace(map.get("PUB_AUTHORS")));
      // 成果详情页
      newMap.put("pubUrl", map.get("PUB_INDEX_URL"));

    }
    return newMap;
  }

  /**
   * 将SNS库中的分享基金信息同步至SIE库中
   * 
   * 动态需要以下字段： 部门，人员名字，人员链接，人员头像 职称
   * 
   * 业务名 全文缩略图，全文下载地址 成果名 成果详情页 作者名 成果详情信息
   */
  private Map<String, Object> synSnsFundingAgenciesContentToSie(Map<String, Object> map) {
    Map<String, Object> newMap = new LinkedHashMap<String, Object>();
    if (map != null) {
      // 资源ID
      newMap.put("resId", map.get("RES_ID"));
      // 标题
      newMap.put("title", map.get("LINK_TITLE_ZH"));
      // 基金详情信息
      newMap.put("briefDesc", map.get("PUB_DESCR_ZH"));
      // 基金图片
      newMap.put("fileImg", map.get("LINK_IMAGE"));
    }
    return newMap;
  }


  /**
   * 校验分页参数
   */
  private boolean judgeParamer(Map<String, Long> paramMap) {
    Object firstObj = paramMap.get("first");
    Object lastObj = paramMap.get("last");
    if (firstObj instanceof Long && lastObj instanceof Long) {
      return true;
    }
    return false;
  }

  /**
   * XSS过滤
   */
  private String xssReplace(Object obj) {
    String value = obj != null && obj instanceof String ? obj.toString() : "";
    String reslut = "";
    if (JacksonUtils.isJsonObjectOrJsonArray(value)) {
      reslut = XssUtils.transferJson(value);
    } else {
      // 对参数值进行过滤.
      reslut = XssUtils.xssReplace(value);
    }
    return reslut;
  }
}
