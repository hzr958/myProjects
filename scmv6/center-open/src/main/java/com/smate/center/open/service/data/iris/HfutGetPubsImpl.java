package com.smate.center.open.service.data.iris;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.model.sie.publication.PublicationRol;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.center.open.service.iris.HfutPublicationService;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.Page;

/**
 * 合肥工业大学获取成果WS服务.
 * 
 * @author xys
 *
 */
@Transactional(rollbackFor = Exception.class)
public class HfutGetPubsImpl extends ThirdDataTypeBase {

  @Autowired
  HfutPublicationService hfutPublicationService;

  @SuppressWarnings("unchecked")
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
      logger.error("合肥工业大学获取成果WS服务 参数 格式不正确 不能转换成map");
      temp = super.errorMap("合肥工业大学获取成果WS服务 参数 格式不正确 不能转换成map", paramet, "");
      return temp;
    }
    if (dataMap.get("typeId") == null || !NumberUtils.isDigits(dataMap.get("typeId").toString())) {
      logger.error("合肥工业大学获取成果WS服务 参数 成果类型(typeId)不能为空或非数字");
      temp = super.errorMap("合肥工业大学获取成果WS服务 参数 成果类型(typeId)不能为空或非数字", paramet, "");
      return temp;
    }
    if (!StringUtils.isBlank(ObjectUtils.toString(dataMap.get("publishYear")))
        && (!NumberUtils.isDigits(dataMap.get("publishYear").toString())
            || StringUtils.length(dataMap.get("publishYear").toString()) != 4)) {
      logger.error("合肥工业大学获取成果WS服务 参数 发表年份 格式不正确,正确格式为yyyy");
      temp = super.errorMap("合肥工业大学获取成果WS服务 参数 发表年份 格式不正确,正确格式为yyyy", paramet, "");
      return temp;
    }
    if (dataMap.get("all") == null || !"1".equals(dataMap.get("all").toString())) {
      if (dataMap.get("pageNo") == null || !NumberUtils.isDigits(dataMap.get("pageNo").toString())) {
        logger.error("合肥工业大学获取成果WS服务 参数 pageNo 不能为空或非数字");
        temp = super.errorMap("合肥工业大学获取成果WS服务 参数 pageNo 不能为空或非数字", paramet, "");
        return temp;
      }
      if (dataMap.get("pageSize") == null || !NumberUtils.isDigits(dataMap.get("pageSize").toString())) {
        logger.error("合肥工业大学获取成果WS服务 参数 pageSize 不能为空或非数字");
        temp = super.errorMap("合肥工业大学获取成果WS服务 参数 pageSize 不能为空或非数字", paramet, "");
        return temp;
      }
    }
    if (!StringUtils.isBlank(ObjectUtils.toString(dataMap.get("orderType")))
        && (!"desc".equalsIgnoreCase(dataMap.get("orderType").toString()))
        && (!"asc".equals(dataMap.get("orderType").toString()))) {
      logger.error("合肥工业大学获取成果WS服务 参数 排序方式(orderType)错误，正确排序方式为'asc'或'desc'");
      temp = super.errorMap("合肥工业大学获取成果WS服务 参数  排序方式(orderType)错误，正确排序方式为'asc'或'desc'", paramet, "");
      return temp;
    }

    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    Object data = paramet.get(OpenConsts.MAP_DATA);

    Map<String, Object> dataMap = JacksonUtils.jsonToMap(data.toString());
    Page<PublicationRol> page = new Page<PublicationRol>();
    boolean isAll = false;
    if (dataMap.get("all") != null && "1".equals(dataMap.get("all").toString())) {
      isAll = true;
    }
    if (!isAll) {
      page.setPageNo(Integer.valueOf(dataMap.get("pageNo").toString()));
      page.setPageSize(Integer.valueOf(dataMap.get("pageSize").toString()));
    }
    page = hfutPublicationService.getPubs(dataMap, page);
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

    Document document = DocumentHelper.createDocument();
    Element publications = document.addElement("publications");
    if (!isAll) {
      publications.addAttribute("page_no", ObjectUtils.toString(page.getPageNo()));
      publications.addAttribute("page_size", ObjectUtils.toString(page.getPageSize()));
      publications.addAttribute("total_pages", ObjectUtils.toString(page.getTotalPages()));
    }
    publications.addAttribute("total_count", ObjectUtils.toString(page.getTotalCount()));

    if (!CollectionUtils.isEmpty(page.getResult())) {
      for (PublicationRol pub : page.getResult()) {
        Element publication = publications.addElement("publication");
        publication.addElement("pub_id").addText(ObjectUtils.toString(pub.getId()));
        publication.addElement("type_id").addText(ObjectUtils.toString(pub.getTypeId()));
        publication.addElement("zh_title").addText(ObjectUtils.toString(pub.getZhTitle()));
        publication.addElement("en_title").addText(ObjectUtils.toString(pub.getEnTitle()));
        publication.addElement("author_names").addText(ObjectUtils.toString(pub.getAuthorNames()));
        publication.addElement("publish_year").addText(ObjectUtils.toString(pub.getPublishYear()));
        publication.addElement("zh_brief_desc").addText(ObjectUtils.toString(pub.getBriefDesc()));
        publication.addElement("en_brief_desc").addText(ObjectUtils.toString(pub.getBriefDescEn()));
        publication.addElement("doi").addText(ObjectUtils.toString(pub.getDoi()));
        publication.addElement("jnl_zh_name").addText(ObjectUtils.toString(pub.getJnlZhName()));
        publication.addElement("jnl_en_name").addText(ObjectUtils.toString(pub.getJnlEnName()));
        publication.addElement("cited_times")
            .addText(ObjectUtils.toString(pub.getCitedTimes() == null ? 0 : pub.getCitedTimes()));
        publication.addElement("update_date").addText(ObjectUtils.toString(pub.getUpdateDate()));
        publication.addElement("paper_ins").addText(ObjectUtils.toString(pub.getPaperIns()));
        publication.addElement("first_ins_hfut").addText(pub.isFirstAuthorIns() ? "是" : "否");
        publication.addElement("list_ei").addText(ObjectUtils.toString(pub.getListEi() == null ? 0 : pub.getListEi()));
        publication.addElement("list_sci")
            .addText(ObjectUtils.toString(pub.getListSci() == null ? 0 : pub.getListSci()));
        publication.addElement("list_istp")
            .addText(ObjectUtils.toString(pub.getListIstp() == null ? 0 : pub.getListIstp()));
        publication.addElement("list_ssci")
            .addText(ObjectUtils.toString(pub.getListSsci() == null ? 0 : pub.getListSsci()));
      }
    }
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("data", document.asXML());
    dataList.add(map);
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    temp.put(OpenConsts.RESULT_MSG, "scm-0000");// 响应成功
    temp.put(OpenConsts.RESULT_DATA, dataList);
    return temp;
  }

}
