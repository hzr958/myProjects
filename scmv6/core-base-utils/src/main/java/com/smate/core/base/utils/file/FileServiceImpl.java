package com.smate.core.base.utils.file;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * 根据{@link:com.iris.scm.system.file.LocaleFileService}文件解析策略读写文件.
 * 
 * @author liqinghua
 * 
 */
@Service("fileService")
public class FileServiceImpl implements FileService {

  /**
   * 
   */
  private static final long serialVersionUID = 1340865380802722576L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private FileNameParseService fileNameParseService;

  @Override
  public String getFilePath(String fileName) {

    String filePath = fileNameParseService.parseFileNameDir(fileName);
    return filePath;
  }

  @Override
  public File readFile(String fileName, String baseDir) throws IOException {

    String filePath = baseDir + this.getFilePath(fileName);
    logger.info("##读取文件路径:" + filePath);
    File file = new File(filePath);
    if (file.exists()) {
      return file;
    }
    throw new FileNotFoundException(filePath);
  }

  @Override
  public String readText(String fileName, String baseDir, String encoding) throws IOException {

    File file = this.readFile(fileName, baseDir);
    if (file != null) {
      return FileUtils.readFileToString(file, encoding);
    }
    return null;
  }

  @Override
  public String readTextTrimEmpty(String fileName, String baseDir, String encoding) throws IOException {
    String filePath = baseDir + this.getFilePath(fileName);
    logger.info("##读取文件路径:" + filePath);
    File file = new File(filePath);
    if (file.exists()) {
      return FileUtils.readFileToString(file, encoding);
    }
    return "";
  }

  @Override
  public String writeText(String text, String baseDir, String fileName, String encoding) throws IOException {

    String[] fileDir = fileNameParseService.generalDirWithFileName(fileName);
    String filePath = baseDir + fileDir[0] + fileDir[1];
    logger.debug("##写文件路径:" + filePath);
    File file = new File(filePath);
    FileUtils.writeStringToFile(file, text, encoding);
    return fileDir[1];
  }

  @Override
  public String writeUniqueText(String text, String baseDir, String fileExt, String encoding) throws IOException {
    String[] fileDir = fileNameParseService.generalUniqueDirWithFileName(fileExt);
    String filePath = baseDir + fileDir[0] + fileDir[1];
    logger.debug("##写文件路径:" + filePath);
    File file = new File(filePath);
    FileUtils.writeStringToFile(file, text, encoding);
    return fileDir[1];
  }

  @Override
  public String writeFile(File fileData, String baseDir, String fileName) throws IOException {

    String[] fileDir = fileNameParseService.generalDirWithFileName(fileName);
    String filePath = baseDir + fileDir[0] + fileDir[1];
    logger.info("##写文件路径:" + filePath);
    this.writeFile(fileData, filePath);
    return fileDir[1];
  }

  @Override
  public String writeUniqueFile(File fileData, String baseDir, String fileExt) throws IOException {
    String[] fileDir = fileNameParseService.generalUniqueDirWithFileName(fileExt);
    String filePath = baseDir + fileDir[0] + fileDir[1];
    logger.debug("##写文件路径:" + filePath);
    this.writeFile(fileData, filePath);
    return fileDir[1];
  }

  public String copyUniqueFile(String url, String baseDir, String fileExt) throws IOException {
    String[] fileDir = fileNameParseService.generalUniqueDirWithFileName(fileExt);
    String filePath = baseDir + fileDir[0] + fileDir[1];
    logger.debug("##写文件路径:" + filePath);
    this.copyFile(url, filePath);
    return fileDir[1];
  }

  private void writeFile(File fileData, String filePath) throws IOException {

    File file = new File(filePath);
    // 目录是否存在
    File parentFile = file.getParentFile();
    if (!parentFile.exists()) {
      parentFile.mkdirs();
    }

    logger.info("##写文件路径:" + filePath);
    FileOutputStream fos = new FileOutputStream(filePath);
    FileInputStream fis = new FileInputStream(fileData);
    byte[] buffer = new byte[1024];
    int len = 0;
    while ((len = fis.read(buffer)) > 0) {
      fos.write(buffer, 0, len);
    }
    fos.close();
    fis.close();
  }

  private void copyFile(String url, String filePath) throws IOException {

    File file = new File(filePath);
    // 目录是否存在
    File parentFile = file.getParentFile();
    if (!parentFile.exists()) {
      parentFile.mkdirs();
    }

    logger.info("##写文件路径:" + filePath);
    FileOutputStream out = new FileOutputStream(filePath);
    URL fileUrl = new URL(url);
    HttpURLConnection connection = (HttpURLConnection) fileUrl.openConnection();
    DataInputStream in = new DataInputStream(connection.getInputStream());
    BufferedInputStream buf = new BufferedInputStream(in);
    int len = 0;
    byte[] buffer = new byte[in.available()];
    while ((len = buf.read(buffer)) > 0) {
      out.write(buffer, 0, len);
    }
    out.flush();
    out.close();
    buf.close();
  }

  public FileNameParseService getFileNameParseService() {
    return fileNameParseService;
  }

  public void setFileNameParseService(FileNameParseService fileNameParseService) {
    this.fileNameParseService = fileNameParseService;
  }
}
