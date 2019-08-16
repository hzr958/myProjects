package com.smate.center.open.service.data.keywords;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.center.open.service.keyword.PdwhPubkeywordsService;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 翻译基准库的关键词
 * 
 * @author aijiangbin
 * @date 2018年4月25日
 */
@Transactional(rollbackFor = Exception.class)
public class TranslatePdwhPubKeywordsServiceImpl extends ThirdDataTypeBase {

  private final static Integer DEAL_NUM = 200; // 处理的条数
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PdwhPubkeywordsService pdwhPubkeywordsService;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> parameter) {
    Map<String, Object> temp = new HashMap<String, Object>();
    Object data = parameter.get(OpenConsts.MAP_DATA);
    if (data != null && data.toString().length() > 0) {
      // 先把关键词中的特殊字符，转译回来
      data = StringEscapeUtils.unescapeHtml4(data.toString());
      Map<String, Object> dataMap = JacksonUtils.jsonToMap(data.toString());
      if (dataMap != null) {
        parameter.putAll(dataMap);
      }
    }
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> parameter) {
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    Object object = parameter.get("keywordData");
    if (object != null && JacksonUtils.isJsonString(object.toString())) {
      List list = JacksonUtils.jsonToList(object.toString());
      if (list != null && list.size() > 0) {
        try {
          pdwhPubkeywordsService.updatePdwhPubkeywordsList(list);
        } catch (Exception e) {
          logger.error("回写成果关键词翻译，异常", e);
        }
      } else {
        return successMap("关键词解析失败", parameter);
      }

    } else {
      return successMap("关键词解析失败", parameter);
    }
    return successMap("成功回写关键词翻译", dataList);

  }

}
