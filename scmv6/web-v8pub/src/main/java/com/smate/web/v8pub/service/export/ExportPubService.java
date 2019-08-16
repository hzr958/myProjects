package com.smate.web.v8pub.service.export;

import java.util.List;

import com.smate.web.v8pub.vo.exportfile.ExportPubTemp;

public interface ExportPubService {

  public List<ExportPubTemp> queryExportPubTemp(List<Long> pubIds) throws Exception;

}
