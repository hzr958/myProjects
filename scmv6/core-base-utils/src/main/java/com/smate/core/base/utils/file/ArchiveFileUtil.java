package com.smate.core.base.utils.file;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smate.core.base.utils.file.FileUtils.FileNameExtension;
import com.smate.core.base.utils.string.ServiceUtil;

import sun.misc.BASE64Decoder;

/**
 * 附件工具类
 * 
 * @author lxz
 * @since 6.0.1-snapshot
 * @version 6.0.1-snapshot
 */
public class ArchiveFileUtil {

  protected static final Logger logger = LoggerFactory.getLogger(ArchiveFileUtil.class);

  public static final String DOWNLOAD_ACTION_URL = "/archiveFiles/fileDownload.action?fdesId=";
  public static final String DOWNLOAD_ACTION_URL_NO_VER = "/archiveFiles/downLoadNoVer.action?fdesId=";
  public static final String DOWNLOAD_ACTION_URL_PERMISSION = "/archiveFiles/downLoadWithPermission.action?fdesId=";
  public static final String PERMISSION_DOWNLOAD_ACTION_URL = "/archiveFiles/permissionDownLoad.action?fdesId=";
  public static final String DOWNLOAD_STATION_URL = "/file/download?des3Id=";

  /**
   * ArchiveFile表fileUrl列默认值
   */
  public static final String FILE_URL_DEFAULT_VALUE = "0";
  public static final String FILE_TYPE_IMG = "imgIc";
  public static final String FILE_TYPE_TXT = "txt";
  public static final String FILE_TYPE_PDF = "pdf";
  public static final String FILE_TYPE_PPT = "ppt";
  public static final String FILE_TYPE_DOC = "doc";
  public static final String FILE_TYPE_XLS = "xls";
  public static final String FILE_TYPE_RAR = "rar";
  public static final String FILE_TYPE_MUSIC = "music";
  public static final String FILE_TYPE_MOVIE = "movie";
  public static final String FILE_TYPE_HTML = "html";
  public static final String FILE_TYPE_OTHER = "file";

  public static final int THUMBNAIL_WIDTH_72 = 72;
  public static final int THUMBNAIL_HEIGHT_92 = 92;
  public static final int PDWH_THUMBNAIL_WIDTH_178 = 178;
  public static final int PDWH_THUMBNAIL_HEIGHT_239 = 239;
  public static final String THUMBNAIL_SUFFIX = "_thumb.png";
  public static final String PDWH_THUMBNAIL_SUFFIX = "_img_1";
  public static final String PDWH_THUMBNAIL_SUFFIX0 = "_img_0";
  // ID匹配fileid_nodeid_insid
  public static final String ID_PATTEM = "%s_%s_%s";

  /**
   * <p>
   * 根据指定的文件扩展名{@code fileExt}（不包含符号{@code "."}），使用UUID构造一个唯一的文件路径。
   * 返回一个包含两个字符串的数组，第一个字符串是目录路径，第二个字符串是文件名（含给定的扩展名）。
   * </p>
   * <b>例子：</b>
   * 
   * <pre>
   *  "txt" return ["/ce/25/8a/", "a1f7ef8a1d4c4ea99fb0ee663d4ba878.txt"]
   * </pre>
   * <p>
   * 文件目录是通过方法{@link ArchiveFileUtil#getFileDir(String)}来生成的。
   * </p>
   * 
   * @param fileExt 文件扩展名
   * @return 包含文件目录、文件名的字符串数组
   */
  public static String[] buildUniqueFilePathWithName(String fileExt) {

    String newFileName = UUID.randomUUID().toString().replace("-", "");
    if (fileExt != null) {
      newFileName += FileUtils.SYMBOL_DOT + fileExt;
    }
    String dir = getFileDir(newFileName);
    String[] dirName = new String[] {dir, newFileName};
    return dirName;
  }

  /**
   * <p>
   * 根据指定的文件扩展名{@code fileExt}（不包含符号{@code "."}），使用UUID构造一个唯一的文件名。
   * </p>
   * <b>例子：</b>
   * 
   * <pre>
   *  "txt" return "a1f7ef8a1d4c4ea99fb0ee663d4ba878.txt"
   * </pre>
   * 
   * @param fileExt 文件扩展名
   * @return 生成的文件名
   */
  public static String buildUniqueFileName(String fileExt) {
    String newFileName = UUID.randomUUID().toString().replace("-", "");
    if (fileExt != null) {
      newFileName += FileUtils.SYMBOL_DOT + fileExt;
    }
    return newFileName;
  }

  /**
   * <p>
   * 根据文件名（带扩展名）的hash值，获取一个目录路径，返回返回带有文件名的文件路径
   * </p>
   * <b>例子:</b>
   * 
   * <pre>
   * "abcd.txt" return "/5a/cf/94/abcd.txt"
   * </pre>
   * 
   * @see ArchiveFileUtil#getFileDir(String)
   * @author houchuanjie
   * @date 2018年1月19日 下午4:34:13
   * @param fileName 文件名称
   * @return 文件路径
   */
  public static String getFilePath(String fileName) {
    return getFileDir(fileName) + fileName;
  }

  /**
   * 
   * <p>
   * 根据文件名（带扩展名）的hash值，获取一个目录路径。
   * </p>
   * <b>例子:</b>
   * 
   * <pre>
   * "abcd.txt" return "/5a/cf/94/"
   * </pre>
   * 
   * @param fileName 文件名
   * @return 目录路径
   */
  public static String getFileDir(String fileName) {
    String secr = DigestUtils.md5Hex(fileName);
    StringBuilder sb = new StringBuilder(FileUtils.SYMBOL_VIRGULE);
    sb.append(secr.substring(1, 2)).append(secr.substring(3, 4)).append(FileUtils.SYMBOL_VIRGULE);
    sb.append(secr.substring(5, 6)).append(secr.substring(7, 8)).append(FileUtils.SYMBOL_VIRGULE);
    sb.append(secr.substring(9, 10)).append(secr.substring(11, 12)).append(FileUtils.SYMBOL_VIRGULE);
    String dir = sb.toString();
    return dir;
  }

  /**
   * 生成附件下载链接.
   * 
   * @param url
   * @param fileCode
   * @param nodeId
   * @param insId
   * @return
   */
  public static String generateDownloadLink(String url, Long fileCode, Integer nodeId, Long insId) {
    if (StringUtils.isBlank(url)) {
      throw new IllegalArgumentException("url参数不能为空。");
    }
    if (fileCode == null || fileCode <= 0) {
      throw new IllegalArgumentException("fileCode参数不能为空");
    }
    return url + ArchiveFileUtil.DOWNLOAD_ACTION_URL + ArchiveFileUtil.getFDesId(fileCode, nodeId, insId);
  }

  public static String generateDownloadLink(String url, Long fileCode, Integer nodeId, Long insId, Long ownerPsnId,
      Integer permission) {
    if (StringUtils.isBlank(url)) {
      throw new IllegalArgumentException("url参数不能为空。");
    }
    if (fileCode == null || fileCode <= 0) {
      throw new IllegalArgumentException("fileCode参数不能为空");
    }
    return url + ArchiveFileUtil.DOWNLOAD_ACTION_URL_PERMISSION + ArchiveFileUtil.getFDesId(fileCode, nodeId, insId)
        + "&permissionStr="
        + ServiceUtil.encodeToDes3((permission == null ? 0 : permission) + "," + new Date().toString()) + "&ownerPsnId="
        + ServiceUtil.encodeToDes3(ownerPsnId.toString());
  }

  public static String getFDesId(String fileCode, String nodeId, String insId) {

    return URLEncoder.encode(ServiceUtil.encodeToDes3(String.format(ID_PATTEM, fileCode, nodeId, insId)));
  }

  public static String getFDesId(Long fileCode, Integer nodeId, Long insId) {

    return ServiceUtil.encodeToDes3(String.format(ID_PATTEM, fileCode, nodeId, insId));
  }

  /**
   * 获取文件类型图片.
   * 
   * @param contextPath
   * @param fileType
   * @param locale
   * @return
   */
  public static String getFileTypeIco(String contextPath, String fileType, Locale locale) {

    String fulltextIcon = "/images/ico_fulltext_" + locale.toString() + ".gif";
    if (StringUtils.isNotBlank(fileType)) {
      fileType = fileType.toLowerCase();
      if (fileType.indexOf(FileNameExtension.XLS.toString()) > -1
          || fileType.indexOf(FileNameExtension.XLSX.toString()) > -1) {
        fulltextIcon = "/images/filelist/ico_xls.gif";
      } else if (fileType.indexOf(FileNameExtension.PPT.toString()) > -1) {
        fulltextIcon = "/images/filelist/ico_ppt.gif";
      } else if (fileType.indexOf(FileNameExtension.DOC.toString()) > -1
          || fileType.indexOf(FileNameExtension.DOCX.toString()) > -1) {
        fulltextIcon = "/images/filelist/ico_doc.gif";
      } else if (fileType.indexOf(FileNameExtension.PDF.toString()) > -1) {
        fulltextIcon = "/images/filelist/pdf_icon.gif";
      } else if (fileType.indexOf(FileNameExtension.TXT.toString()) > -1 || fileType.indexOf("text") > -1) {
        fulltextIcon = "/images/filelist/ico_txt.gif";
      } else if (fileType.indexOf(FileNameExtension.RAR.toString()) > -1) {
        fulltextIcon = "/images/filelist/ico_rar.gif";
      } else if (fileType.indexOf(FileNameExtension.ZIP.toString()) > -1) {
        fulltextIcon = "/images/filelist/ico_zip.gif";
      } else if (fileType.indexOf("htm") > -1) {
        fulltextIcon = "/images/filelist/ico_htm.gif";
      } else if (fileType.indexOf("html") > -1) {
        fulltextIcon = "/images/filelist/ico_html.gif";
      } else if (fileType.indexOf(FileNameExtension.JPG.toString()) > -1
          || fileType.indexOf(FileNameExtension.JPEG.toString()) > -1) {
        fulltextIcon = "/images/filelist/ico_jpg.gif";
      } else if (fileType.indexOf(FileNameExtension.PNG.toString()) > -1) {
        fulltextIcon = "/images/filelist/ico_png.gif";
      } else if (fileType.indexOf(FileNameExtension.GIF.toString()) > -1) {
        fulltextIcon = "/images/filelist/ico_gif.gif";
      } else if (fileType.indexOf(FileNameExtension.BMP.toString()) > -1
          || fileType.indexOf(FileNameExtension.JFIF.toString()) > -1
          || fileType.indexOf(FileNameExtension.DIB.toString()) > -1
          || fileType.indexOf(FileNameExtension.TIF.toString()) > -1
          || fileType.indexOf(FileNameExtension.TIFF.toString()) > -1) {
        fulltextIcon = "/images/filelist/ico_img.gif";
      }

    }
    return contextPath + fulltextIcon;
  }

  /**
   * 根据文件名获取文件类型
   *
   * @author houchuanjie
   * @date 2018年1月18日 下午2:47:47
   * @param fileName
   * @return
   */
  public static String getFileType(String fileName) {
    String ext = FileUtils.getFileNameExtensionStr(fileName);
    if (StringUtils.isNotBlank(ext)) {
      FileNameExtension fileNameExtension = FileNameExtension.parseFileNameExtension(ext.trim());
      if (Objects.isNull(fileNameExtension)) {
        return ext;
      }
      switch (fileNameExtension) {
        case XLS:
        case XLSX:
          return FILE_TYPE_XLS;
        case DOC:
        case DOCX:
          return FILE_TYPE_DOC;
        case PPT:
        case PPTX:
          return FILE_TYPE_PPT;
        case TXT:
          return FILE_TYPE_TXT;
        case PDF:
          return FILE_TYPE_PDF;
        case RAR:
        case ZIP:
        case GZIP:
        case TAR:
        case _7Z:
          return FILE_TYPE_RAR;
        case JPG:
        case JPEG:
        case PNG:
        case BMP:
        case GIF:
        case JFIF:
        case DIB:
        case TIF:
        case TIFF:
        case PCX:
        case PSD:
          return FILE_TYPE_IMG;
        case FLV:
        case WMV:
        case WMA:
        case AVI:
        case RMVB:
        case RM:
        case MKV:
        case MP4:
          return FILE_TYPE_MOVIE;
        case MP3:
        case WAV:
          return FILE_TYPE_MUSIC;
        case HTM:
        case HTML:
          return FILE_TYPE_HTML;
        default:
          return ext;
      }
    }
    return FILE_TYPE_OTHER;
  }

  /**
   * 计算文件大小.
   * 
   * @param file
   * @return
   * @throws Exception
   */
  public static Long getFileSize(File file) throws Exception {

    if (file.exists()) {
      try {
        return file.length();
      } catch (Exception e) {
        logger.error("计算文件大小时出错了!", e);
        throw new Exception("计算文件大小时出错了!", e);
      }
    } else {
      return 0L;
    }
  }

  /**
   * 计算文件大小.
   * 
   * @param fileBase64
   * @return
   * @throws Exception
   */
  @SuppressWarnings("restriction")
  public static Integer getFileSize(String fileBase64) throws Exception {

    BASE64Decoder decoder = new BASE64Decoder();
    // Base64解码
    byte[] b = decoder.decodeBuffer(fileBase64);
    return b.length;
  }

  /**
   * 获取GB转换成Byte.
   * 
   * @param gbsize
   * @return
   */
  public static long getGBByte(long gbsize) {

    return gbsize * FileUtils.ONE_GB;
  }

  /**
   * 获取MB转换成Byte.
   * 
   * @param mbsize
   * @return
   */
  public static long getMBByte(long mbsize) {

    return mbsize * FileUtils.ONE_MB;
  }

  /**
   * 获取KB转换成Byte.
   * 
   * @param kbsize
   * @return
   */
  public static long getKBByte(long kbsize) {

    return kbsize * FileUtils.ONE_KB;
  }

  /**
   * 将base64编码的图片字符串写入指定文件
   *
   * @author houchuanjie
   * @date 2018年1月19日 下午3:21:14
   * @param destFilePath 目标文件
   * @param base64Str base64图片编码串
   * @throws IOException
   */
  public static void writeBase64Str2File(String destFilePath, String base64Str) throws IOException {
    assert StringUtils.isNotBlank(destFilePath);
    assert base64Str != null;
    byte[] decodedBytes = Base64.getDecoder().decode(base64Str);
    for (int i = 0; i < decodedBytes.length; i++) {
      decodedBytes[i] &= 0xFF;
    }
    FileUtils.writeByteArrayToFile(new File(destFilePath), decodedBytes);
  }

  public static void main(String[] args) {
    String[] path = ArchiveFileUtil.buildUniqueFilePathWithName("txt");
    for (String s : path) {
      System.out.println(s);
    }
  }

}
