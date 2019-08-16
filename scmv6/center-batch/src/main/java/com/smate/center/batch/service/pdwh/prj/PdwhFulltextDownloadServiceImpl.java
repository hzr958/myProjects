package com.smate.center.batch.service.pdwh.prj;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.pdwh.prj.NsfcPrjKeywordsDao;
import com.smate.center.batch.service.utils.FileNamePathParseUtil;
import com.smate.core.base.utils.exception.BatchTaskException;

@Service("PdwhFulltextDownloadService")
@Transactional(rollbackFor = Exception.class)
public class PdwhFulltextDownloadServiceImpl implements PdwhFulltextDownloadService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  /**
   * 文件上下文路径 区分文件要放到哪个文件夹上
   */
  @Value("${file.root}")
  private String fileRoot;

  private String basicPath = "upfile";

  @Autowired
  private NsfcPrjKeywordsDao nsfcPrjKeywordsDao;

  @Override
  public void downloadFile(String pubId, String filePath, File outDirectory) throws Exception {
    String url = fileRoot + "/" + basicPath + FileNamePathParseUtil.parseFileNameDir(filePath);
    File inFile = new File(url);
    if (!inFile.exists() || inFile.isDirectory()) {// 文件不存在，直接跳过
      return;
    }
    String fileName = pubId.toString() + filePath.substring(filePath.lastIndexOf("."));
    if (StringUtils.isEmpty(fileName)) {
      return;
    }
    File outPutFile = new File(outDirectory, fileName);
    if (outPutFile.exists()) {// 目标文件已经存在，直接跳过
      return;
    }
    FileInputStream in = new FileInputStream(inFile);
    FileOutputStream out = new FileOutputStream(outPutFile);
    try {
      byte[] buffer = new byte[1024];
      int num = 0;
      while ((num = in.read(buffer)) > 0) {
        out.write(buffer, 0, num);
        out.flush();
      }
    } catch (Exception e) {
      logger.error("========基准库全文文件下载失败======= pubId:" + pubId, e);
      outPutFile.delete();// 文件复制失败，将失败文件进行删除
      throw new BatchTaskException(e);
    } finally {
      out.close();
      in.close();
    }
  }

  @Override
  public List<Map<String, Object>> queryNeedDownloadFile(Long lastPubId, int batchSize) {
    return nsfcPrjKeywordsDao.queryNeedDownloadFile(lastPubId, batchSize);
  }
}
