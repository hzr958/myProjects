package com.smate.center.batch.service.pdwh.prj;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface PdwhFulltextDownloadService {

  List<Map<String, Object>> queryNeedDownloadFile(Long lastPubId, int batchSize);

  void downloadFile(String pubId, String filePath, File outDir) throws Exception;


}
