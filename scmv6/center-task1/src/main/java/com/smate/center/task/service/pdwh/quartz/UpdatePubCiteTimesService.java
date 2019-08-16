package com.smate.center.task.service.pdwh.quartz;

import java.io.File;

public interface UpdatePubCiteTimesService {

  public File getfile() throws Exception;

  public void handleXML(File file) throws Exception;


}
