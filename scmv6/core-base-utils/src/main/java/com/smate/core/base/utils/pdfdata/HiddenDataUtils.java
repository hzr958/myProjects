package com.smate.core.base.utils.pdfdata;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;

import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;

public class HiddenDataUtils {

  @Value("${pdfDir}")
  private static String pdfFileRootDir;// 生成PDF文件的路径

  private static final String APPNAME = "myapp";// pdf隐藏数据appName
  private static final String DATANAME = "xmldata";// pdf隐藏数据pdfName

  /**
   * 生成序列化的临时文件名
   */
  public static String getTempName(String fileDir, String type) {
    String fileStr = fileDir + UUID.randomUUID().toString() + "." + type;
    return fileStr;
  }

  /**
   * 删除临时文件
   */
  public static void deleteFile(String fileName) {
    File file = new File(fileName);
    file.delete();
  }

  /**
   * 读取PDF隐藏数据
   */
  public static String parseHiddenData(String pdfFile) throws IOException {
    PdfName appName = new PdfName(APPNAME);
    PdfName dataName = new PdfName(DATANAME);

    DocumentPieceInfo dpi = new DocumentPieceInfo();
    InputStream in = new FileInputStream(pdfFile);
    PdfReader reader = new PdfReader(in);

    PdfObject data = dpi.getPieceInfo(reader, appName, dataName);
    return data != null ? EscapeUnescape.unescape(data.toString()) : "";
  }

}
