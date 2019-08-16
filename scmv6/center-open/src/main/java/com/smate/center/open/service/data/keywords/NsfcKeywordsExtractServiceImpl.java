package com.smate.center.open.service.data.keywords;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.center.open.service.keyword.NsfcKeywordsService;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 为isis指派系统提取关键词
 * 
 * 
 */
public class NsfcKeywordsExtractServiceImpl extends ThirdDataTypeBase {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private NsfcKeywordsService nsfcKeywordsService;

    @Override
    public Map<String, Object> doVerify(Map<String, Object> parameter) {

        Map<String, Object> temp = new HashMap<String, Object>();
        Object data = parameter.get(OpenConsts.MAP_DATA);
        if (data != null && data.toString().length() > 0) {
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
        List<HashMap<String, Object>> dataMap = new ArrayList<HashMap<String, Object>>();
        String title = "";
        String abstractStr = "";
        String keywords = "";
        String category = "";
        // 标题
        if (parameter.get("title") != null) {
            title = parameter.get("title").toString();
        }
        // 摘要
        if (parameter.get("abstract") != null) {
            abstractStr = parameter.get("abstract").toString();
        }
        // 关键词
        if (parameter.get("keywords") != null) {
            keywords = parameter.get("keywords").toString();
        }
        // 关键词
        if (parameter.get("category") != null) {
            keywords = parameter.get("category").toString();
        }
        Integer rs = 1;
        try {
            rs = this.nsfcKeywordsService.extractKwsForIsis(title, abstractStr, keywords, category, dataMap);
        } catch (Exception e) {
            logger.error("计算项目关键词错误", e);
        }
        if (rs == 1) {
            return successMap("计算对应关键词完成", dataMap);
        } else {
            return successMap("计算对应关键词完成, 返回结果为空", dataMap);
        }
    }

}
