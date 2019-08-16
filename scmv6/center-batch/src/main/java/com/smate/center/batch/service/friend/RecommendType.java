package com.smate.center.batch.service.friend;

/**
 * @author tsz
 */
public interface RecommendType {

  // 工作经历
  static final Integer RECOMMEND_WORK = 1;
  // 教育经历
  static final Integer RECOMMEND_EDU = 2;
  // 好友的好友
  static final Integer RECOMMEND_OF_FRIEND = 3;
  // 成果合作
  static final Integer RECOMMEND_PUBLICATION = 4;
  // 项目合作
  static final Integer RECOMMEND_PROJECT = 5;
  // 领域
  static final Integer RECOMMEND_FIELD = 6;
  // 文献合作
  static final Integer RECOMMEND_REFERENCE = 7;

}
