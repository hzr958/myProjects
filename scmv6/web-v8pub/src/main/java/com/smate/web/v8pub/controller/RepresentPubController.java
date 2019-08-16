package com.smate.web.v8pub.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.service.sns.homepage.RepresentPubService;
import com.smate.web.v8pub.vo.PubListVO;
import com.smate.web.v8pub.vo.PubQueryModel;
import com.smate.web.v8pub.vo.RepresentPubVO;

/**
 * 代表公开成果列表控制器
 * 
 * @author aijiangbin
 * @date 2018年8月9日
 */
@Controller
public class RepresentPubController {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Resource
  private RepresentPubService pepresentPubService;

  @RequestMapping("/pub/ajaxgetopenlist")
  @ResponseBody
  public ModelAndView representpubajaxopen(PubQueryModel pubQueryModel) {

    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    try {
      BeanUtils.copyProperties(pubQueryDTO, pubQueryModel);
      pubQueryDTO.setPageNo(pubQueryModel.getPage().getPageNo());
    } catch (Exception e) {
      logger.error("复制属性异常", e);
    }
    PubListVO pubListVO = new PubListVO();
    pubListVO.setPubQueryDTO(pubQueryDTO);
    ModelAndView view = new ModelAndView();
    pubListVO.getPubQueryDTO().setServiceType("openPubList");
    pubListVO.getPubQueryDTO().setSearchPsnId(pubListVO.getPsnId());
    pepresentPubService.findPsnOpenPubList(pubListVO);
    view.addObject(pubListVO);
    view.setViewName("/pub/homepage/representpub/psn_openpub_List");
    return view;
  }

  /**
   * 显示人员代表性成果
   * 
   * @return
   */
  @RequestMapping(value = {"/pub/representpub/ajaxshow", "/pub/outside/ajaxrepresentpub"})
  public ModelAndView showPsnRepresentPub(RepresentPubVO representPubVO) {
    ModelAndView model = new ModelAndView("/pub/homepage/representpub/psn_representpub");
    try {
      if (representPubVO.getPsnId() > 0) {
        isMySelf(representPubVO);
        pepresentPubService.findPsnRepresentPub(representPubVO);
        model.addObject("pubVO", representPubVO);
      }
    } catch (Exception e) {
      logger.error("显示人员代表性成果出错，psnId =" + representPubVO.getPsnId(), e);
    }
    return model;
  }

  /**
   * 编辑代表性成果
   * 
   * @return
   */
  @RequestMapping("/pub/representpub/ajaxedit")
  public ModelAndView editPsnRepresentPub(RepresentPubVO representPubVO) {
    ModelAndView model = new ModelAndView("/pub/homepage/representpub/psn_representpub_edit");
    try {
      if (representPubVO.getPsnId() > 0) {
        isMySelf(representPubVO);
        pepresentPubService.findPsnRepresentPub(representPubVO);
        model.addObject("pubVO", representPubVO);
      }
    } catch (Exception e) {
      logger.error("弹出人员代表性成果编辑框出错， psnId = " + representPubVO.getPsnId(), e);
    }
    return model;
  }

  /**
   * 保存人员代表性成果
   * 
   * @return
   */

  @RequestMapping("/pub/representpub/ajaxsave")
  @ResponseBody
  public String savePsnRepresentPub(RepresentPubVO representPubVO) {
    Map<String, String> data = new HashMap<String, String>();
    try {
      isMySelf(representPubVO);
      if (representPubVO.getIsMySelf()) {

        pepresentPubService.savePsnRepresentPub(representPubVO.getPsnId(), representPubVO.getAddToRepresentPubIds());
        data.put("result", "success");
      } else {
        data.put("result", "notMySelf");
      }
    } catch (Exception e) {
      logger.error("保存人员代表性成果出错， psnId = " + representPubVO.getPsnId() + ", pubIds = "
          + representPubVO.getAddToRepresentPubIds(), e);
      data.put("result", "error");
    }
    String jsonString = JacksonUtils.jsonObjectSerializer(data);
    return jsonString;
  }

  /**
   * 判断是否是本人
   * 
   * @return
   */
  private void isMySelf(RepresentPubVO representPubVO) {
    boolean isSelf = true;
    Long currentPsnId = SecurityUtils.getCurrentUserId();

    if (currentPsnId != null && currentPsnId != 0) {
      representPubVO.setCurrentPsnId(currentPsnId);
      if (currentPsnId.longValue() == representPubVO.getPsnId().longValue()) {
        isSelf = true;
      } else {
        isSelf = false;
      }
    } else {
      isSelf = false;
    }
    representPubVO.setIsMySelf(isSelf);
  }

}
