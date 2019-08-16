package com.smate.center.task.service.publicpub;

import com.google.gdata.util.ServiceException;
import com.smate.center.task.model.publicpub.PubFulltextUploadLog;
import com.smate.center.task.model.sns.quartz.PubFulltextPsnRcmd;
import com.smate.center.task.v8pub.sns.po.PubSnsPO;

import java.util.List;

public interface PubfulltextRcmdService {

  List<PubFulltextUploadLog> getNeedRcmdData();

  Long getPdwhPubId(Long snsPubId);

  List<Long> getSnsPubId(Long pdwhPubId, Long snsPsnId);

  void savePubFulltextPsnRcmd(List<Long> snsPubIds, Long srcPubId, int dbid, PubFulltextUploadLog uploadLog)
      throws ServiceException;

  void updateLogStatus(Long id, int status);

  void deleteFulltextPsnRcmd(Long srcPubId);

  PubFulltextPsnRcmd getFulltextRcmd(Long snsPubId);

  void deleteFulltextRcmd(Long snsPubId) throws Exception;

  PubSnsPO getSnsPubById(Long snsPubId);
}
