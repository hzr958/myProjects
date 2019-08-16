package com.smate.web.v8pub.service.restful;

import java.util.Set;

import com.smate.web.v8pub.dom.PubSituationBean;

public interface PublicImportAndConfirmPubService {
  /**
   * 基准库成果导入到个人库(成果认领，检索导入，基准库成果详情这是我的成果)
   * 
   * @param pubId
   * @param psnId
   * @return
   */
  String importAndConfirmPdwhPub(Long pubId, Long psnId, Integer pubType, Set<PubSituationBean> situationList,
      Integer isPubConfirm);

}
