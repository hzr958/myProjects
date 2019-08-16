package com.smate.web.v8pub.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import com.smate.core.base.pub.enums.PubSnsRecordFromEnum;
import com.smate.core.base.pub.util.PubParamUtils;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.v8pub.consts.V8pubConst;
import com.smate.web.v8pub.dto.PubFulltextDTO;
import com.smate.web.v8pub.dto.PubMemberDTO;
import com.smate.web.v8pub.service.match.PubAuthorMatchService;
import com.smate.web.v8pub.service.repeatpub.PubRepeatService;
import com.smate.web.v8pub.service.sns.PubEnterEditService;
import com.smate.web.v8pub.vo.PubDuplicateVO;
import com.smate.web.v8pub.vo.RepeatPubInfo;

/**
 * 成果录入
 * 
 * @author wsn
 * @date 2018年7月24日
 */
@Controller
public class PubEnterController {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubEnterEditService pubEnterEditService;
  @Autowired
  private PubRepeatService pubRepeatSercice;
  @Autowired
  private PubAuthorMatchService pubAuthorMatchService;
  @Value("${domainscm}")
  private String domainscm;

  /**
   * 进入成果编辑页面的入口
   * 
   * @param pubJson
   * @return
   */
  @SuppressWarnings("rawtypes")
  @RequestMapping("/pub/enter")
  public ModelAndView pubEnter(String pubJson, String des3PubId, Integer changType, String des3GrpId,
      String isProjectPub) {
    ModelAndView view = new ModelAndView();
    try {
      // demo页面跳转的des3PubId = 5358|1000001000786
      if (StringUtils.isNotBlank(des3PubId)) {
        String pubId = Des3Utils.decodeFromDes3(des3PubId);
        String[] pubIdParam = pubId.split("\\|");
        if (pubIdParam.length == 2) {
          des3PubId = Des3Utils.encodeToDes3(pubIdParam[1]);
        }
      }
      PubDetailVO pubShow = null;
      pubShow = pubEnterEditService.getBuilPubDetailVO(pubJson, des3PubId, des3GrpId, changType);
      if (des3GrpId != null && ("1".equals(isProjectPub) || "0".equals(isProjectPub))) {
        pubShow.setIsProjectPub(Integer.parseInt(isProjectPub));
        pubShow.setDes3GrpId(des3GrpId);
      }
      view.addObject("pubVo", pubShow);
      view.setViewName(
          V8pubConst.PUB_ENTER_OR_EDITOR_VIEW_PREFIX + "pub_" + V8pubConst.PUB_TYPE_MAP.get(pubShow.getPubType()));
    } catch (Exception e) {
      logger.error("进入成果编辑页面出错，des3PubId={}, pubJson={}", des3PubId, pubJson, e);
    }
    return view;
  }

  /**
   * 自动填充 isChangeType 是否是切换成果类型 1 切换
   * 
   * @param des3pdwhPubId
   * @param savePubType
   * @author hht
   * @return
   */
  @SuppressWarnings({"rawtypes"})
  @RequestMapping("/pub/autoFillPub")
  public ModelAndView pubAutoFill(String pubJson, String des3pdwhPubId, String isChangeType, String des3GrpId,
      String isProjectPub, String des3PubId, String membersJsonStr, String des3FileId) {
    ModelAndView view = new ModelAndView();
    // 假如用户直接通过地址栏提交表单 跳到pub/enter页面
    if (StringUtils.isEmpty(pubJson) && StringUtils.isEmpty(des3pdwhPubId) && StringUtils.isEmpty(des3PubId)) {
      view.setViewName("redirect:" + domainscm + "/pub/enter");
      return view;
    }
    try {
      PubDetailVO pdwhPubShow = null;
      // 保留的一些字段 如pubid 还有全文 作者 个人库
      PubDetailVO snsPubShow = null;
      // 查找基准库对应成果详情
      if (des3pdwhPubId != null || (StringUtils.isNotBlank(pubJson) && des3pdwhPubId == null)) {
        pdwhPubShow = pubEnterEditService.getBuilPubDetailVOByPdwh(pubJson, des3pdwhPubId, des3GrpId);
      }
      // 通过基准库authorNames 匹配member 防止出现为空的
      Long psnId = SecurityUtils.getCurrentUserId();
      if (pdwhPubShow != null) {
        // 设置全文permission 如果为空那么则为0 因为基准库没有permission
        this.handleFulltextPermission(pdwhPubShow);
        // 处理成果日期有时分 不需要的情况
        pdwhPubShow.setPublishDate(subDate(pdwhPubShow.getPublishDate()));
        // 不是切换类型 匹配当前人
        if (!"1".equals(isChangeType)) {
          pubAuthorMatchService.authorMatch(pdwhPubShow.getMembers(), psnId);
        }
        this.trimKeyWordHtml(pdwhPubShow);
        // 设置成果权限 authorName匹配当前人 匹配成功成果公开
        Integer isMathch = pubAuthorMatchService.isMatch(pdwhPubShow.getAuthorNames(), psnId);
        if (isMathch == 1) {
          pdwhPubShow.setPermission(7);
        } else {
          pdwhPubShow.setPermission(4);
        }
      }
      if (StringUtils.isNotBlank(des3PubId)) {
        String pubId = Des3Utils.decodeFromDes3(des3PubId);
        String[] pubIdParam = pubId.split("\\|");
        if (pubIdParam.length == 2) {
          des3PubId = Des3Utils.encodeToDes3(pubIdParam[1]);
        }
        snsPubShow = pubEnterEditService.getBuilPubDetailVO(pubJson, des3PubId, des3GrpId, null);
      }

      if (snsPubShow != null && pdwhPubShow == null) {
        if (des3GrpId != null && ("1".equals(isProjectPub) || "0".equals(isProjectPub))) {
          snsPubShow.setIsProjectPub(Integer.parseInt(isProjectPub));
          snsPubShow.setDes3GrpId(des3GrpId);
          snsPubShow.setPublishDate(subDate(snsPubShow.getPublishDate()));
        }
        this.trimKeyWordHtml(snsPubShow);
        view.addObject("pubVo", snsPubShow);
        view.setViewName(
            V8pubConst.PUB_ENTER_OR_EDITOR_VIEW_PREFIX + "pub_" + V8pubConst.PUB_TYPE_MAP.get(snsPubShow.getPubType()));
      } else {
        if (des3GrpId != null && ("1".equals(isProjectPub) || "0".equals(isProjectPub))) {
          pdwhPubShow.setIsProjectPub(Integer.parseInt(isProjectPub));
          pdwhPubShow.setDes3GrpId(des3GrpId);
        }
        // 复制属性
        this.CopyProperty(snsPubShow, pdwhPubShow, membersJsonStr);
        this.trimKeyWordHtml(pdwhPubShow);

        // 20190104 构造成果全文逻辑
        pubEnterEditService.buildPubFulltext(pdwhPubShow, des3FileId);
        // des3FileId 当前的逻辑中只有pdf导入的时候才会传进来，设置RecordFrom 类型为pdf导入
        if (StringUtils.isNotBlank(des3FileId)) {
          pdwhPubShow.setRecordFrom(PubSnsRecordFromEnum.IMPORT_FORM_PDF);
        }

        view.addObject("pubVo", pdwhPubShow);
        view.addObject("des3pdwhPubId", des3pdwhPubId);
        view.setViewName(V8pubConst.PUB_ENTER_OR_EDITOR_VIEW_PREFIX + "pub_"
            + V8pubConst.PUB_TYPE_MAP.get(pdwhPubShow.getPubType()));
      }
    } catch (Exception e) {
      logger.error("自动填充成果出错，des3pdwhPubId={}", des3pdwhPubId, e);
    }

    return view;
  }

  /**
   * 只取年月日
   * 
   * @param publishDate
   * @return
   */
  private String subDate(String publishDate) {
    return publishDate.length() > 10 ? publishDate.substring(0, 10) : publishDate;
  }

  /**
   * 这个进到测试入口
   * 
   * @param pubVo
   * @return
   */
  @RequestMapping(value = "/pub/enter/savepub", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String entersavepub(@ModelAttribute("jsonData") String jsonData) {
    String result = "";
    try {
      result = pubEnterEditService.saveOrUpdatePubJson(jsonData);
    } catch (Exception e) {
      logger.error("保存成果json出错 pubJson={}", jsonData, e);
    }
    return result;
  }

  @RequestMapping(value = "/pub/enter/ajaxcheckrepeat", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public ModelAndView pubCheckDup(@ModelAttribute PubDuplicateVO dupVO) {
    ModelAndView view = new ModelAndView();
    boolean isEditPub = false;
    if (StringUtils.isNotBlank(dupVO.getDes3PubId())) {
      isEditPub = true;
    } else {
      isEditPub = false;
    }
    // 群组成果不查重，为空则说明不是群组成果
    if (StringUtils.isBlank(dupVO.getDes3GrpId())) {
      // 先调查重接口进行获取重复的成果pubId
      List<Long> dupPubIds = pubRepeatSercice.listPubIdByCheckDup(dupVO);
      if (!CollectionUtils.isEmpty(dupPubIds)) {
        // 有重复成果数据，调获取成果列表接口，并返回重复成果选择界面
        List<RepeatPubInfo> repeatPubInfoList = pubRepeatSercice.listRepeatPubDetail(dupPubIds);
        if (!CollectionUtils.isEmpty(repeatPubInfoList)) {
          dupVO.setRepeatPubInfoList(repeatPubInfoList);
          view.addObject("repeatPubVO", dupVO);
          view.addObject("isEditPub", isEditPub);
        }
      }
    }
    view.setViewName("pub/main/pub_enter_repeat");
    return view;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public void CopyProperty(PubDetailVO snsPubShow, PubDetailVO pdwhPubShow, String membersJsonStr) {
    if (snsPubShow != null && pdwhPubShow != null) {
      // 1如果个人库有全文 则填充全文 srcFulltextUrl 全文链接
      if (snsPubShow.getFullText() != null && StringUtils.isNotBlank(snsPubShow.getSrcFulltextUrl())) {
        pdwhPubShow.setFullText(snsPubShow.getFullText());
        pdwhPubShow.setSrcFulltextUrl(snsPubShow.getSrcFulltextUrl());
      }
      List<PubMemberDTO> members = new ArrayList<PubMemberDTO>();
      // 2如果作者列表为空 那么填充作者 防止在用户编辑的时候 删除了作者 todo 这个目前没有做
      if (StringUtils.isNotBlank(membersJsonStr)) {
        members = JacksonUtils.jsonToList(membersJsonStr);
      }
      // 如果作者列表为空 那么先填补个人库的作者 如果个人库没有则填补基准库的
      if (members.size() == 0) {
        if (snsPubShow.getMembers().size() > 0) {
          pdwhPubShow.setMembers(snsPubShow.getMembers());
        }
      } else {
        pdwhPubShow.setMembers(members);
      }
      // 3填充 des3pubId 编辑保存的时候使用
      pdwhPubShow.setDes3PubId(snsPubShow.getDes3PubId());
      // 4如果个人库有附件 那么填充附件
      if (snsPubShow.getAccessorys() != null) {
        pdwhPubShow.setAccessorys(snsPubShow.getAccessorys());
      }
    }
  }

  /**
   * 去除关键词中的html标签
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public void trimKeyWordHtml(PubDetailVO pdwhPubShow) {
    if (pdwhPubShow != null) {
      List<String> keyWordsListHtml = pdwhPubShow.getKeyWordsList();
      pdwhPubShow.setKeywords(PubParamUtils.trimAllHtml(pdwhPubShow.getKeywords()));
      if (keyWordsListHtml != null) {
        List<String> keyWordsList = new ArrayList<>();
        String keyWordhtml = "";
        for (String keyWord : keyWordsListHtml) {
          keyWordhtml = HtmlUtils.htmlUnescape(keyWord);
          keyWordsList.add(PubParamUtils.trimAllHtml(keyWordhtml));
        }
        pdwhPubShow.setKeyWordsList(keyWordsList);
      }
    }
  }

  // 设置全文permission 如果为空那么则为0
  public void handleFulltextPermission(PubDetailVO pdwhPubShow) {
    PubFulltextDTO fullText = pdwhPubShow.getFullText();
    if (fullText != null) {
      if (fullText.getPermission() == null) {
        fullText.setPermission(0);
      }
    }
  }

  @RequestMapping(value = "/pub/enter/importpubbypdf", produces = "application/json;charset=UTF-8")
  @ResponseBody
  public String importPubByPDF(String des3FileId) {
    HashMap<String, Object> resultMap = new HashMap<>();
    try {
      if (StringUtils.isNotBlank(des3FileId)) {
        // 解密文件id
        Long fileId = NumberUtils.toLong(Des3Utils.decodeFromDes3(des3FileId));
        // 处理解析pdf逻辑，返回一个基准库pubId
        resultMap = pubEnterEditService.resolverPDF(fileId);
      }
    } catch (Exception e) {
      logger.error("解析pdf文件填充成果信息出错！des3FileId={}", des3FileId, e);
    }
    return JacksonUtils.mapToJsonStr(resultMap);
  }
}
