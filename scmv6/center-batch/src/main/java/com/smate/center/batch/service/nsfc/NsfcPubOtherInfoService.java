package com.smate.center.batch.service.nsfc;

import java.util.List;

import com.smate.center.batch.model.sns.nsfc.NsfcPubMember;
import com.smate.center.batch.model.sns.pub.PubSimple;


/**
 * 成果其他信息服务接口.
 * 
 * @author zhanglingling
 *
 */
public interface NsfcPubOtherInfoService {

  void saveNsfcPubOtherInfo(PubSimple pub, String xmlData, List<NsfcPubMember> nsfcPubMemberList) throws Exception;

}
