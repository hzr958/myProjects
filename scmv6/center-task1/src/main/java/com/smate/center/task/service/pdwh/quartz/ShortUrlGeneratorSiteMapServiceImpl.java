package com.smate.center.task.service.pdwh.quartz;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.pdwh.quartz.PdwhPubIndexUrlDao;

@Service
@Transactional
public class ShortUrlGeneratorSiteMapServiceImpl implements ShortUrlGeneratorSiteMapService {

  @Autowired
  private PdwhPubIndexUrlDao pdwhPubIndexUrlDao;
  @Value("${file.root}")
  private String fileRoot;
  @Value("${domainscm}")
  private String domain;

  @Override
  public Integer generatorSiteMap(Integer startIndex, Integer size) throws Exception {
    List<String> listUrl = pdwhPubIndexUrlDao.getAllUrl(startIndex, size);
    // 遍历这个list
    if (listUrl.size() == 0) {
      return 0;// 循环终止
    }
    // startIndex = startIndex + listUrl.size();
    int num = startIndex / size + 1;
    String date = DateFormatUtils.ISO_DATE_FORMAT.format(new Date());
    String dirXmlPath = fileRoot + File.separator + "sitemap" + File.separator + date + "-xml" + File.separator
        + "sitemap" + num + ".xml";
    String dirGzPath = fileRoot + File.separator + "sitemap" + File.separator + date + "-gz" + File.separator
        + "sitemap" + num + ".xml.gz";
    File dirXmlFile = new File(dirXmlPath);
    File dirGzFile = new File(dirGzPath);
    // 删除原有的文件
    if (dirXmlFile.delete() && dirGzFile.delete()) {
      // 重新创建
      dirXmlFile = new File(dirXmlPath);
      dirGzFile = new File(dirGzPath);
    }
    // 没有目录则创建
    if (!dirXmlFile.getParentFile().exists()) {
      dirXmlFile.getParentFile().mkdirs();
    }
    if (!dirGzFile.getParentFile().exists()) {
      dirGzFile.getParentFile().mkdirs();
    }
    // 写入xml文件
    writeXmlFile(dirXmlFile, listUrl);
    // 压缩gzip文件
    gzipFile(dirGzFile, dirXmlFile);

    return startIndex + listUrl.size();
  }

  @Override
  public void writeIndexFile() throws Exception {
    String date = DateFormatUtils.ISO_DATE_FORMAT.format(new Date());
    File file = new File(fileRoot + File.separator + "sitemap" + File.separator + date + "-xml");
    // 算出该文件夹下有多少个文件
    int length = file.listFiles().length;
    String dirIndexPath = fileRoot + File.separator + "sitemap" + File.separator + date + "-sitemap_index.xml";
    File dirIndexFile = new File(dirIndexPath);
    // 删除原有的文件
    if (dirIndexFile.delete()) {
      // 新建
      dirIndexFile = new File(dirIndexPath);
    }

    FileOutputStream fileOutputStream = new FileOutputStream(dirIndexFile);
    StringBuilder content = new StringBuilder("<sitemapindex xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");
    for (int i = 1; i <= length; i++) {
      content.append("  <sitemap>\n" + "\t<loc>" + domain + "/sitemap" + i + ".xml.gz</loc>\n" + "\t<lastmod>"
          + DateFormatUtils.ISO_DATETIME_FORMAT.format(new Date()) + "</lastmod>\n" + "  </sitemap>\n");
    }
    content.append("</sitemapindex>");
    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
    writer.write(content.toString());
    writer.close();
    fileOutputStream.close();
  }

  /**
   * 压缩为gzip文件
   * 
   * @param dirGzFile
   * @param dirXmlFile
   * @throws Exception
   */
  private void gzipFile(File dirGzFile, File dirXmlFile) throws Exception {
    FileOutputStream dirGzStream = new FileOutputStream(dirGzFile, true);
    FileInputStream fis = new FileInputStream(dirXmlFile);
    BufferedInputStream bis = new BufferedInputStream(fis);
    GZIPOutputStream gzp = new GZIPOutputStream(dirGzStream);
    int count = 0;
    byte[] data = new byte[2048];
    while ((count = bis.read(data, 0, 2048)) != -1) {
      gzp.write(data, 0, count);
    }
    fis.close();
    bis.close();
    gzp.close();
    dirGzStream.close();

  }

  /**
   * 写入xml文件
   * 
   * @param dirXmlFile
   * @param listUrl
   * @throws Exception
   */
  private void writeXmlFile(File dirXmlFile, List<String> listUrl) throws Exception {
    FileOutputStream dirXmlStream = new FileOutputStream(dirXmlFile, true);
    // 写文件
    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(dirXmlStream, "utf-8"));
    StringBuilder xml = new StringBuilder();
    out.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n"
        + "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");
    for (int i = 0; i < listUrl.size(); i++) {
      String url = "https://www.scholarmate.com/S/" + listUrl.get(i);
      // 拼凑sitemap格式
      xml.append("  <url>\r\n" + "    <loc>").append(url).append("</loc>\r\n" + "    <priority>0.6</priority>\r\n"
          + "    <changefreq>monthly</changefreq>\r\n  " + "</url>\n");
    }
    xml.append("</urlset>");
    // 写入文件中
    out.write(xml.toString());
    // 关闭资源
    out.flush();
    out.close();
    dirXmlStream.close();
  }

}
