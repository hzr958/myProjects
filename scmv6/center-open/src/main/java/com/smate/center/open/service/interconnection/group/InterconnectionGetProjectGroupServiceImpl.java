package com.smate.center.open.service.interconnection.group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.open.cache.OpenCacheService;
import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.dao.group.OpenGroupUnionDao;
import com.smate.center.open.dao.grp.GrpBaseInfoDao;
import com.smate.center.open.model.interconnection.UnionGroupCodeCache;
import com.smate.center.open.service.interconnection.ScmUnionDataHandleService;

/**
 * 互联互通 获取 项目群组
 * 
 * @author AiJiangBin
 *
 */
public class InterconnectionGetProjectGroupServiceImpl implements ScmUnionDataHandleService {
  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private GrpBaseInfoDao grpBaseInfoDao;

  @Autowired
  OpenGroupUnionDao openGroupUnionDao;
  @Autowired
  private OpenCacheService openCacheService;

  /**
   * 检查： 参数
   * 
   * @param dataParamet
   * @return
   */
  private String checkParam(Map<String, Object> dataParamet) {


    return null;
  }

  /**
   * 具体业务
   */
  @Override
  public String handleUnionData(Map<String, Object> dataParamet) {

    Long psnId = NumberUtils.toLong(dataParamet.get("psnId").toString());
    List<HashMap<String, Object>> groupBaseInfoList = grpBaseInfoDao.findMyProjectGrp(psnId);
    if (groupBaseInfoList == null || groupBaseInfoList.size() == 0) {
      return buildResultXml("没有项目群组", "3");
    }
    return buildResultXml(groupBaseInfoList, dataParamet);
  }


  /**
   * 构造返回参数
   * 
   * @param groupBaseInfoList
   * @param dataParamet
   * @return
   */
  private String buildResultXml(List<HashMap<String, Object>> groupBaseInfoList, Map<String, Object> dataParamet) {
    Long psnId = NumberUtils.toLong(dataParamet.get("psnId").toString());
    Long openId = NumberUtils.toLong(dataParamet.get("openid").toString());
    List<HashMap<String, Object>> groupBaseInfoListHasUnion = new ArrayList<HashMap<String, Object>>();
    Document doc;
    try {
      doc = DocumentHelper.parseText("<?xml version=\"1.0\" encoding=\"UTF-8\"?><groups></groups>");
      Element rootNode = (Element) doc.selectSingleNode("/groups");
      rootNode.addElement("getGroupListStatus").addText("0");
      if (groupBaseInfoList != null && groupBaseInfoList.size() > 0) {
        for (HashMap<String, Object> groupBaseInfo : groupBaseInfoList) {
          String groupName = groupBaseInfo.get("GRP_NAME").toString();
          String groupId = groupBaseInfo.get("GRP_ID").toString();
          String groupCode = DigestUtils.md5Hex(openId.toString() + "_" + groupId);
          Boolean hasUnion = false;
          hasUnion = isOrNotUnion(psnId, groupCode);
          if (hasUnion) {
            groupBaseInfoListHasUnion.add(groupBaseInfo);
            continue;
          }
          UnionGroupCodeCache groupCodeCache = new UnionGroupCodeCache();
          groupCodeCache.setGroupId(NumberUtils.toLong(groupId));
          groupCodeCache.setOpenId(openId);
          openCacheService.put(OpenConsts.UNION_GROUP_CODE_CACHE, openCacheService.EXP_HOUR_1, groupCode,
              groupCodeCache);
          Element groupElement = rootNode.addElement("group");
          groupElement.addElement("group_code").addText(groupCode);
          groupElement.addElement("group_name").addText(groupName);
          groupElement.addElement("has_union").addText("0");
        }
      }
      // 最后添加已经关联的
      if (groupBaseInfoListHasUnion != null && groupBaseInfoListHasUnion.size() > 0) {
        for (HashMap<String, Object> groupBaseInfo : groupBaseInfoListHasUnion) {
          String groupName = groupBaseInfo.get("GRP_NAME").toString();
          String groupId = groupBaseInfo.get("GRP_ID").toString();
          String groupCode = DigestUtils.md5Hex(openId.toString() + "_" + groupId);
          UnionGroupCodeCache groupCodeCache = new UnionGroupCodeCache();
          groupCodeCache.setGroupId(NumberUtils.toLong(groupId));
          groupCodeCache.setOpenId(openId);
          openCacheService.put(OpenConsts.UNION_GROUP_CODE_CACHE, openCacheService.EXP_HOUR_1, groupCode,
              groupCodeCache);
          Element groupElement = rootNode.addElement("group");
          groupElement.addElement("group_code").addText(groupCode);
          groupElement.addElement("group_name").addText(groupName);
          groupElement.addElement("has_union").addText("1");
        }
      }
      return doc.getRootElement().asXML();
    } catch (DocumentException e) {
      logger.error("构造成果XML列表出现异常", e);
      return buildResultXml("构造成果XML列表出现异常", "5");
    }
  }


  public String buildResultXml(String result, String getGroupStatus) {
    Document doc;
    try {
      doc = DocumentHelper.parseText("<?xml version=\"1.0\" encoding=\"UTF-8\"?><groups></groups>");
      Element rootNode = (Element) doc.selectSingleNode("/groups");
      rootNode.addElement("result").addText(result);
      rootNode.addElement("getGroupListStatus").addText(getGroupStatus);
      return doc.getRootElement().asXML();
    } catch (DocumentException e) {
      logger.error("构造成果XML列表出现异常", e);
    }
    return "";
  }

  /**
   * 判断是否关联
   * 
   * @param ownerPsnId
   * @param groupCode
   * @return
   */
  private Boolean isOrNotUnion(Long ownerPsnId, String groupCode) {
    Long id = openGroupUnionDao.findIdByOwnPsnIdAndGroupCode(ownerPsnId, groupCode);
    if (id != null) {
      return true;
    }
    return false;
  }


  public static void main(String[] args) {
    System.out.println(DigestUtils.md5Hex("54645993" + "_" + "100000002332929"));
  }


}

