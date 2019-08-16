package com.smate.web.mobile.dyn.vo;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.dyn.model.DynamicMsg;
import com.smate.core.base.enums.LikeStatusEnum;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 动态Form
 * 
 * @author zk
 *
 */
public class DynamicForm {

  private Long dynId; // 主键
  private String des3DynId;
  private Long parentDynId; // 父动态id（被分享，被赞，被评论的那个动态id）
  private String des3ParentDynId;
  private Long producer; // 动态创建人
  private String dynType; // 动态类型 A 类型B1 \B2\ C \ D
  private String nextDynType;
  private Integer dynTmp; // 动态模版
  private Integer permission; // 动态权限
  private Integer status; // 状态,0:正常,1:删除

  private String dynText; // 原始动态文本
  private String paramJson; // 参数json
  private List<Long> dynIds; // 动态id列表
  private List<DynamicMsg> dynMsgs; // 动态信息列表
  private Map<String, Object> dynMap; // 动态实时数据
  private List<String> dynLists; // 动态列表
  private String forwardUrl; // 跳转url
  private Integer queryDynType; // 查询动态类型，用于页面选择类型
  private Long lastDynId; // 最后一个动态id,用于查询

  private Integer resType; // 资源 类型 ， 包括 成果类型等。。
  private String resTypeStr; // 资源 类型 ， 包括 成果类型等。。
  private Integer operatorType; // 操作类型 ， 1："评论了", 2"赞了", 3"分享了", 5"关注了"
  private Long resId; // 资源 Id
  private String des3ResId;// 加密的资源id
  private Integer resNode; // 资源 节点
  private Integer databaseType = 1;// 1: 个人库 2 基准库
  private String locale = "zh_CN"; // 语言版本,zh_CN,en_US
  private Page<DynamicMsg> page = new Page<DynamicMsg>();

  // 人员信息
  private Long psnId; // 人员id
  private String userId;
  private String resOwnerDes3Id;
  private String des3psnId; // 人员id
  private String psnInsAndPos; // 人员单位及职称
  private String psnAvatars; // 人员头像
  private String psnName; // 人员姓名
  private int pageNumber = 1;// 开始
  private Long resPsnId; // 资源所有人
  private Integer action; // 动作 数据库中0取消，1赞
  private String attId; // 关注id
  private String replyContent; // 评论内容
  private String replyPubTitle; // 评论成果标题
  private String dynStatisticsIds; // 加载动态列表id
  private String dynResIdResTypes; // 加载动态列表资源Id 和资源类型
  private int totalCount;// 评论总数

  private Integer dbId;// 网站Id

  private String des3PubId;

  private Long pubId;

  public Integer pageSize = 6; // 批量6条，外加两条推广动态

  private String platform; // 平台，pc或移动端，移动端(mobile)

  private String resInfoJson; // 资源信息json串
  private boolean shareFund = false; // 分享基金

  private LikeStatusEnum hasAward;// 当前赞动作
  private Long awardTimes;
  private String imgUrl;

  private Long resGrpId;// 引用的群组id
  private String title;// 资源的标题
  private Long resInfoId;// 资源相关信息id
  private Long fundId;// 基金id


  public Long getResGrpId() {
    return resGrpId;
  }

  public void setResGrpId(Long resGrpId) {
    this.resGrpId = resGrpId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Long getResInfoId() {
    return resInfoId;
  }

  public void setResInfoId(Long resInfoId) {
    this.resInfoId = resInfoId;
  }

  public Long getFundId() {
    return fundId;
  }

  public void setFundId(Long fundId) {
    this.fundId = fundId;
  }

  private Integer resCount = 1;

  private Map<String, Object> resultMap;


  private Integer pageNo;

  public String getDes3DynId() {
    return des3DynId;
  }

  public void setDes3DynId(String des3DynId) {
    this.des3DynId = des3DynId;
  }


  public Long getDynId() {
    if (this.dynId == null && StringUtils.isNotBlank(this.des3DynId)) {
      try {
        dynId = Long.valueOf(Des3Utils.decodeFromDes3(this.des3DynId));
      } catch (Exception e) {
      }
    }
    return dynId;
  }

  public void setDynId(Long dynId) {
    this.dynId = dynId;
  }

  public Long getProducer() {
    return producer;
  }

  public void setProducer(Long producer) {
    this.producer = producer;
  }

  public String getDynType() {
    return dynType;
  }

  public void setDynType(String dynType) {
    this.dynType = dynType;
  }

  public Integer getDynTmp() {
    return dynTmp;
  }

  public void setDynTmp(Integer dynTmp) {
    this.dynTmp = dynTmp;
  }

  public Integer getPermission() {
    return permission;
  }

  public void setPermission(Integer permission) {
    this.permission = permission;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getParamJson() {
    return paramJson;
  }

  public void setParamJson(String paramJson) {
    this.paramJson = paramJson;
  }

  public List<Long> getDynIds() {
    return dynIds;
  }

  public void setDynIds(List<Long> dynIds) {
    this.dynIds = dynIds;
  }

  public List<DynamicMsg> getDynMsgs() {
    return dynMsgs;
  }

  public void setDynMsgs(List<DynamicMsg> dynMsgs) {
    this.dynMsgs = dynMsgs;
  }

  public Map<String, Object> getDynMap() {
    return dynMap;
  }

  public void setDynMap(Map<String, Object> dynMap) {
    this.dynMap = dynMap;
  }

  public String getForwardUrl() {
    return forwardUrl;
  }

  public void setForwardUrl(String forwardUrl) {
    this.forwardUrl = forwardUrl;
  }

  public Integer getQueryDynType() {
    return queryDynType;
  }

  public void setQueryDynType(Integer queryDynType) {
    this.queryDynType = queryDynType;
  }

  public Long getLastDynId() {
    return lastDynId;
  }

  public void setLastDynId(Long lastDynId) {
    this.lastDynId = lastDynId;
  }

  public Long getPsnId() {
    if (psnId == null) {
      psnId = SecurityUtils.getCurrentUserId();
    }
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getLocale() {
    return locale;
  }

  public void setLocale(String locale) {
    this.locale = locale;
  }

  public Long getParentDynId() {
    return Optional.ofNullable(parentDynId)
        .orElseGet(() -> Optional.ofNullable(des3ParentDynId).filter(StringUtils::isNotBlank)
            .map(des3Id -> NumberUtils.parseLong(Des3Utils.decodeFromDes3(des3ParentDynId), null)).orElse(null));
  }

  public void setParentDynId(Long parentDynId) {
    this.parentDynId = parentDynId;
  }

  public Integer getResType() {
    return resType;
  }

  public void setResType(Integer resType) {
    this.resType = resType;
  }

  public Long getResId() {
    return Optional.ofNullable(resId).orElseGet(() -> Optional.ofNullable(des3ResId).filter(StringUtils::isNotBlank)
        .map(des3Id -> NumberUtils.parseLong(Des3Utils.decodeFromDes3(des3Id), null)).orElse(null));
  }

  public void setResId(Long resId) {
    this.resId = resId;
  }

  public Integer getResNode() {
    return resNode;
  }

  public void setResNode(Integer resNode) {
    this.resNode = resNode;
  }

  public String getPsnInsAndPos() {
    return psnInsAndPos;
  }

  public void setPsnInsAndPos(String psnInsAndPos) {
    this.psnInsAndPos = psnInsAndPos;
  }

  public String getPsnAvatars() {
    return psnAvatars;
  }

  public void setPsnAvatars(String psnAvatars) {
    this.psnAvatars = psnAvatars;
  }

  public String getPsnName() {
    return psnName;
  }

  public void setPsnName(String psnName) {
    this.psnName = psnName;
  }

  public Long getResPsnId() {
    return resPsnId;
  }

  public void setResPsnId(Long resPsnId) {
    this.resPsnId = resPsnId;
  }

  public Integer getAction() {
    return action;
  }

  public void setAction(Integer action) {
    this.action = action;
  }

  public String getAttId() {
    return attId;
  }

  public void setAttId(String attId) {
    this.attId = attId;
  }

  public String getReplyContent() {
    return replyContent;
  }

  public void setReplyContent(String replyContent) {
    this.replyContent = replyContent;
  }

  public int getPageNumber() {
    return pageNumber;
  }

  public void setPageNumber(int pageNumber) {
    this.pageNumber = pageNumber;
  }

  public String getDynText() {
    return dynText;
  }

  public void setDynText(String dynText) {
    this.dynText = dynText;
  }

  public String getDynStatisticsIds() {
    return dynStatisticsIds;
  }

  public void setDynStatisticsIds(String dynStatisticsIds) {
    this.dynStatisticsIds = dynStatisticsIds;
  }

  public String getDynResIdResTypes() {
    return dynResIdResTypes;
  }

  public void setDynResIdResTypes(String dynResIdResTypes) {
    this.dynResIdResTypes = dynResIdResTypes;
  }

  public List<String> getDynLists() {
    return dynLists;
  }

  public void setDynLists(List<String> dynLists) {
    this.dynLists = dynLists;
  }

  public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public String getDes3PubId() {
    if (StringUtils.isBlank(des3PubId) && pubId != null) {
      return Des3Utils.encodeToDes3(pubId.toString());
    }
    return des3PubId;
  }

  public void setDes3PubId(String des3PubId) {
    this.des3PubId = des3PubId;
  }

  public Integer getOperatorType() {
    return operatorType;
  }

  public void setOperatorType(Integer operatorType) {
    this.operatorType = operatorType;
  }

  public String getDes3ResId() {
    if (this.des3ResId == null && this.getResId() != null) {
      try {
        return Des3Utils.encodeToDes3(this.getResId() + "");
      } catch (Exception e) {
        return des3ResId;
      }
    }
    return des3ResId;
  }

  public void setDes3ResId(String des3ResId) {

    this.des3ResId = des3ResId;

  }

  public Long getPubId() {
    if (pubId == null && StringUtils.isNoneBlank(des3PubId)) {
      try {
        return Long.valueOf(Des3Utils.decodeFromDes3(des3PubId));
      } catch (Exception e) {
        return pubId;
      }
    }
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }

  public String getDes3psnId() {
    if (StringUtils.isBlank(des3psnId) && this.psnId != null && this.psnId != 0L) {
      des3psnId = Des3Utils.encodeToDes3(psnId.toString());
    }
    return des3psnId;
  }

  public void setDes3psnId(String des3psnId) {
    this.des3psnId = des3psnId;
  }

  public Page<DynamicMsg> getPage() {
    return page;
  }

  public void setPage(Page<DynamicMsg> page) {
    this.page = page;
  }

  public String getReplyPubTitle() {
    return replyPubTitle;
  }

  public void setReplyPubTitle(String replyPubTitle) {
    this.replyPubTitle = replyPubTitle;
  }

  public Integer getDatabaseType() {
    return databaseType;
  }

  public void setDatabaseType(Integer databaseType) {
    this.databaseType = databaseType;
  }

  public Integer getDbId() {
    return dbId;
  }

  public void setDbId(Integer dbId) {
    this.dbId = dbId;
  }

  public String getResInfoJson() {
    return resInfoJson;
  }

  public void setResInfoJson(String resInfoJson) {
    this.resInfoJson = resInfoJson;
  }

  public boolean getShareFund() {
    return shareFund;
  }

  public void setShareFund(boolean shareFund) {
    this.shareFund = shareFund;
  }

  public LikeStatusEnum getHasAward() {
    return hasAward;
  }

  public void setHasAward(LikeStatusEnum hasAward) {
    this.hasAward = hasAward;
  }

  public Long getAwardTimes() {
    return awardTimes;
  }

  public void setAwardTimes(Long awardTimes) {
    this.awardTimes = awardTimes;
  }

  public String getNextDynType() {
    return nextDynType;
  }

  public void setNextDynType(String nextDynType) {
    this.nextDynType = nextDynType;
  }

  public String getDes3ParentDynId() {
    return des3ParentDynId;
  }

  public void setDes3ParentDynId(String des3ParentDynId) {
    this.des3ParentDynId = des3ParentDynId;
  }

  public String getImgUrl() {
    return imgUrl;
  }

  public void setImgUrl(String imgUrl) {
    this.imgUrl = imgUrl;
  }

  public int getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(int totalCount) {
    this.totalCount = totalCount;
  }

  public String getResTypeStr() {
    return resTypeStr;
  }

  public void setResTypeStr(String resTypeStr) {
    this.resTypeStr = resTypeStr;
  }

  public Map<String, Object> getResultMap() {
    return resultMap;
  }

  public void setResultMap(Map<String, Object> resultMap) {
    this.resultMap = resultMap;
  }

  public Integer getResCount() {
    return resCount;
  }

  public void setResCount(Integer resCount) {
    this.resCount = resCount;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getResOwnerDes3Id() {
    return resOwnerDes3Id;
  }

  public void setResOwnerDes3Id(String resOwnerDes3Id) {
    this.resOwnerDes3Id = resOwnerDes3Id;
  }

  public Integer getPageNo() {
    return pageNo;
  }

  public void setPageNo(Integer pageNo) {
    this.pageNo = pageNo;
  }

}
