package com.smate.web.file.utils;

import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.smate.core.base.utils.string.ServiceUtil;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.file.ArchiveFileUtil;
import com.smate.core.base.utils.file.FileUtils;
import com.smate.core.base.utils.file.FileUtils.FileNameExtension;
import com.smate.web.file.form.DownloadFileRes;

/**
 * 下载文件资源打包工具类
 * 
 * @author houchuanjie
 * @date 2018年3月7日 下午4:31:04
 */
public class ZipUtil {
  private static final Logger logger = LoggerFactory.getLogger(ZipUtil.class);

  /**
   * 将资源文件列表进行打包，返回压缩包文件路径，包含文件名和后缀名
   * 
   * @author houchuanjie
   * @date 2018年3月6日 下午5:50:21
   * @param archiveFileResList
   * @return 压缩包文件路径，包含文件名和后缀名
   * @throws FileNotFoundException
   */
  public static String zipFile(Map<Long, DownloadFileRes> downloadFileResMap, String rootPath) throws IOException {
    if (MapUtils.isEmpty(downloadFileResMap)) {
      logger.error("文件打包失败！文件资源列表为空！");
      return "";
    }
    String zipFileName = ArchiveFileUtil.buildUniqueFileName(FileNameExtension.ZIP.toString());
    String zipFilePath =
        rootPath + FileUtils.SYMBOL_VIRGULE + ServiceConstants.DIR_ZIPFILE + FileUtils.SYMBOL_VIRGULE + zipFileName;

    try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFilePath), Charset.forName("gbk"));) {
      // 暂存连接失败的资源，然后进行一次重试
      List<DownloadFileRes> failedResList = new ArrayList<DownloadFileRes>();
      for (DownloadFileRes res : downloadFileResMap.values()) {
        // 向zip输出流写入一个资源文件
        DownloadFileRes downloadFileRes = zipPutEntry(out, res);
        // 返回resMap不为空说明写入失败，加入失败列表
        if (Objects.nonNull(downloadFileRes)) {
          failedResList.add(downloadFileRes);
        }
      }
      // 对写入失败的资源文件再次尝试写入
      for (DownloadFileRes res : failedResList) {
        // 向zip输出流写入一个资源文件
        DownloadFileRes downloadFileRes = zipPutEntry(out, res);
        if (Objects.nonNull(downloadFileRes)) {
          // 如果还是失败，在此进行处理，目前忽略该资源文件，未做其他处理
          downloadFileResMap.remove(downloadFileRes.getId());
        }
      }
      out.flush();
    } catch (FileNotFoundException e) {
      logger.error("路径找不到，无法打开路径{}!", zipFilePath, e);
      throw e;
    } catch (IOException e) {
      logger.error("写入zip压缩包文件失败！文件路径：{}!", zipFilePath, e);
      throw e;
    }
    return zipFilePath;
  }

  /**
   * 向zip压缩包输出流写一个资源文件，成功将返回null，失败则返回该资源的resMap
   *
   * @author houchuanjie
   * @date 2018年3月7日 上午11:22:03
   * @param out
   * @param downloadFileRes
   * @return 写入成功返回null，失败返回参数resMap
   * @throws IOException
   */
  private static DownloadFileRes zipPutEntry(ZipOutputStream out, DownloadFileRes downloadFileRes) throws IOException {
    URL url = new URL(downloadFileRes.getUrl());
    URLConnection conn = url.openConnection();
    try (DataInputStream in = new DataInputStream(conn.getInputStream());) {
      out.putNextEntry(new ZipEntry(ServiceUtil.getRegularString(downloadFileRes.getName())));
      byte[] buffer = new byte[in.available()];
      int len = 0;
      while ((len = in.read(buffer)) > 0) {
        out.write(buffer, 0, len);
      }
      return null;
    } catch (IOException e) {
      logger.error("对多个资源文件进行打包zip时发生错误，无法连接url：{}，稍后将重试一次！", url.toString(), e);
      return downloadFileRes;
    } finally {
      out.closeEntry();
    }
  }
}
