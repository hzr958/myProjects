package com.smate.web.v8pub.service.publics;

import com.smate.web.v8pub.po.PubFulltextUploadLog;

public interface PubfulltextUploadService {

  void saveOrUpdate(PubFulltextUploadLog pubFulltextUploadLog);

  PubFulltextUploadLog getUploadLog(Long pubId);

  PubFulltextUploadLog getSnsUploadLog(Long pubId);

}
