package com.smate.center.open.service.nsfc;


import com.smate.center.open.model.nsfc.NsfcPubOtherInfo;

/**
 * 成果其他信息服务接口.
 * 
 * @author zhanglingling
 *
 */
public interface NsfcPubOtherInfoService {

  NsfcPubOtherInfo getNsfcPubOtherInfo(Long pubId);

  String getNsfcPubSource(Long pubId);

}
