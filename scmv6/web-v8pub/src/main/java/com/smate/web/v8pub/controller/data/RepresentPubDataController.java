package com.smate.web.v8pub.controller.data;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.service.sns.homepage.RepresentPubService;
import com.smate.web.v8pub.vo.PubListVO;
import com.smate.web.v8pub.vo.PubQueryModel;
import com.smate.web.v8pub.vo.RepresentPubVO;

@Controller
public class RepresentPubDataController {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Resource
  private RepresentPubService pepresentPubService;

  /**
   * 获取代表成果列表
   * 
   * @param representPubVO
   * @return
   */
  @RequestMapping(value = "/data/pub/saverepresentpub", method = RequestMethod.POST,
      produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String savePsnRepresentPub(RepresentPubVO representPubVO) {
    Map<String, String> data = new HashMap<String, String>();
    try {
      pepresentPubService.savePsnRepresentPub(representPubVO.getPsnId(), representPubVO.getAddToRepresentPubIds());
      data.put("result", "success");
    } catch (Exception e) {
      logger.error("保存人员代表性成果出错， psnId = " + representPubVO.getPsnId() + ", pubIds = "
          + representPubVO.getAddToRepresentPubIds(), e);
      data.put("result", "error");
    }
    String jsonString = JacksonUtils.jsonObjectSerializer(data);
    return jsonString;
  }

  /**
   * 获取添加代表成果个人公开成果列表
   * 
   * @param pubQueryModel
   * @return
   */
  @RequestMapping(value = "/data/pub/getOpenPubList", method = RequestMethod.POST,
      produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Object getOpenPubList(PubQueryModel pubQueryModel) {
    Map<String, String> data = new HashMap<String, String>();
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      PubQueryDTO pubQueryDTO = new PubQueryDTO();
      try {
        BeanUtils.copyProperties(pubQueryDTO, pubQueryModel);
        pubQueryDTO.setPageNo(pubQueryModel.getPage().getPageNo());
      } catch (Exception e) {
        logger.error("复制属性异常", e);
      }
      PubListVO pubListVO = new PubListVO();
      pubListVO.setPubQueryDTO(pubQueryDTO);
      pubListVO.getPubQueryDTO().setServiceType("pubList");
      pubListVO.getPubQueryDTO().setSearchPsnId(pubListVO.getPsnId());
      pepresentPubService.findPsnOpenPubList(pubListVO);
      result.put("resultList", pubListVO.getResultList());
      result.put("totalCount", pubListVO.getTotalCount());
      result.put("status", "success");

    } catch (Exception e) {
      logger.error("添加代表成果获取个人公开的成果出错， psnId = " + pubQueryModel.getDes3SearchPsnId(), e);
      data.put("result", "error");
    }
    return result;
  }
}
