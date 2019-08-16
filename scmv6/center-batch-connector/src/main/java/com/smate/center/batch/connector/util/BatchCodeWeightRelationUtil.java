package com.smate.center.batch.connector.util;

import java.util.HashMap;
import java.util.Map;

import com.smate.center.batch.connector.enums.BatchOpenCodeEnum;
import com.smate.center.batch.connector.enums.BatchWeightEnum;

/**
 * 服务码&权重关系
 * 
 * @author LXZ
 * 
 * @since 6.0.1
 * @version 6.0.1
 * @param job
 */
public class BatchCodeWeightRelationUtil {
  public static Map<String, String> CODE_WEIGHT_RELATION_MAP;
  static {
    CODE_WEIGHT_RELATION_MAP = new HashMap<String, String>();
    CODE_WEIGHT_RELATION_MAP.put(BatchOpenCodeEnum.THIRD_PSN_BASE_INFO.toString(), BatchWeightEnum.A.toString());
    CODE_WEIGHT_RELATION_MAP.put(BatchOpenCodeEnum.THIRD_PUSH_PROJECT_INFO.toString(), BatchWeightEnum.A.toString());
    CODE_WEIGHT_RELATION_MAP.put(BatchOpenCodeEnum.THIRD_WECHAT_PSN_MSG.toString(), BatchWeightEnum.A.toString());
    CODE_WEIGHT_RELATION_MAP.put(BatchOpenCodeEnum.THIRD_WECHAT_PUBLIC_MSG.toString(), BatchWeightEnum.A.toString());
    CODE_WEIGHT_RELATION_MAP.put(BatchOpenCodeEnum.WECHAT_ACCESS_TOKEN.toString(), BatchWeightEnum.A.toString());
    CODE_WEIGHT_RELATION_MAP.put(BatchOpenCodeEnum.INPUT_FULLTEXT_TASK.toString(), BatchWeightEnum.A.toString());
    CODE_WEIGHT_RELATION_MAP.put(BatchOpenCodeEnum.SIMPLE_PUB_ADD_SAVE.toString(), BatchWeightEnum.A.toString());
    CODE_WEIGHT_RELATION_MAP.put(BatchOpenCodeEnum.SIMPLE_PUB_EDIT_SAVE.toString(), BatchWeightEnum.A.toString());
    CODE_WEIGHT_RELATION_MAP.put(BatchOpenCodeEnum.SIMPLE_PUB_DELETE.toString(), BatchWeightEnum.A.toString());
    CODE_WEIGHT_RELATION_MAP.put(BatchOpenCodeEnum.SIMPLE_PUB_IMPORT.toString(), BatchWeightEnum.A.toString());
    CODE_WEIGHT_RELATION_MAP.put(BatchOpenCodeEnum.SIMPLE_PSN_REGISTER.toString(), BatchWeightEnum.A.toString());
    CODE_WEIGHT_RELATION_MAP.put(BatchOpenCodeEnum.SIMPLE_PUB_LOW_SAVE.toString(), BatchWeightEnum.B.toString());
    CODE_WEIGHT_RELATION_MAP.put(BatchOpenCodeEnum.SIMPLE_PUB_LOW_DELETE.toString(), BatchWeightEnum.B.toString());
    CODE_WEIGHT_RELATION_MAP.put(BatchOpenCodeEnum.PUB_ASSIGN_FOR_ROL.toString(), BatchWeightEnum.D.toString());
    CODE_WEIGHT_RELATION_MAP.put(BatchOpenCodeEnum.PUB_ASSIGN_FOR_ROL1.toString(), BatchWeightEnum.D.toString());
    CODE_WEIGHT_RELATION_MAP.put(BatchOpenCodeEnum.REGISTER_PDWH_PUB_MATCH.toString(), BatchWeightEnum.A.toString());
    CODE_WEIGHT_RELATION_MAP.put(BatchOpenCodeEnum.REGISTER_RCMD_SYN_PSN_INFO.toString(), BatchWeightEnum.A.toString());
    CODE_WEIGHT_RELATION_MAP.put(BatchOpenCodeEnum.REGISTER_INVITATION_HANDLE.toString(), BatchWeightEnum.A.toString());
    CODE_WEIGHT_RELATION_MAP.put(BatchOpenCodeEnum.REGISTER_COMPLETENESS_REFRESH.toString(),
        BatchWeightEnum.A.toString());
    CODE_WEIGHT_RELATION_MAP.put(BatchOpenCodeEnum.REGISTER_PSN_HTML.toString(), BatchWeightEnum.A.toString());
    // 群组后台任务
    CODE_WEIGHT_RELATION_MAP.put(BatchOpenCodeEnum.SYSNC_GROUP_INFO.toString(), BatchWeightEnum.B.toString());
    CODE_WEIGHT_RELATION_MAP.put(BatchOpenCodeEnum.SYSNC_GROUP_PSN_TO_SNS.toString(), BatchWeightEnum.B.toString());
    CODE_WEIGHT_RELATION_MAP.put(BatchOpenCodeEnum.SYSNC_GROUP_INVITE_PSN_TO_SNS.toString(),
        BatchWeightEnum.B.toString());
    CODE_WEIGHT_RELATION_MAP.put(BatchOpenCodeEnum.SYSNC_FOR_ALL_GROUP_UPDATE_TO_ROL.toString(),
        BatchWeightEnum.B.toString());
    CODE_WEIGHT_RELATION_MAP.put(BatchOpenCodeEnum.SYSNC_GROUP_STATISTICS.toString(), BatchWeightEnum.B.toString());
    CODE_WEIGHT_RELATION_MAP.put(BatchOpenCodeEnum.SYSNC_RCMD_SYNC_INFO.toString(), BatchWeightEnum.B.toString());

    CODE_WEIGHT_RELATION_MAP.put(BatchOpenCodeEnum.GROUP_PUB_RECALCULATE.toString(), BatchWeightEnum.B.toString());

    CODE_WEIGHT_RELATION_MAP.put(BatchOpenCodeEnum.PSN_AVATARS_SYNC.toString(), BatchWeightEnum.B.toString());
    CODE_WEIGHT_RELATION_MAP.put(BatchOpenCodeEnum.ATEENTION_DYNAMIC.toString(), BatchWeightEnum.A.toString());
    // 更新人员solr信息
    CODE_WEIGHT_RELATION_MAP.put(BatchOpenCodeEnum.REFRESH_PSN_SOLR_INFO.toString(), BatchWeightEnum.A.toString());
    // 生成缩略图
    CODE_WEIGHT_RELATION_MAP.put(BatchOpenCodeEnum.PRODUCE_THUMBNAIL_IMAGE_STRATEGY.toString(),
        BatchWeightEnum.B.toString());
    // 更新人员配置信息
    CODE_WEIGHT_RELATION_MAP.put(BatchOpenCodeEnum.REFRESH_PSN_CONFIG_INFO.toString(), BatchWeightEnum.A.toString());
    // 基准库成果地址和人员匹配
    CODE_WEIGHT_RELATION_MAP.put(BatchOpenCodeEnum.MATCH_PDWHPUB_ADRR_AUTHOR.toString(), BatchWeightEnum.C.toString());
    CODE_WEIGHT_RELATION_MAP.put(BatchOpenCodeEnum.SAVE_PUB_FROM_CROSSREF.toString(), BatchWeightEnum.C.toString());

  }
}
