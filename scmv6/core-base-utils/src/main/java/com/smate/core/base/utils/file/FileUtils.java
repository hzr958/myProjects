package com.smate.core.base.utils.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.EnumSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.smate.core.base.utils.string.StringUtils;

/**
 * 文件相关的常量
 * 
 * @author houchuanjie
 * @date 2018年1月12日 上午9:42:58
 */
public class FileUtils extends org.apache.commons.io.FileUtils {
  /**
   * 文件扩展名枚举
   *
   * @author houchuanjie
   * @date 2018年1月12日 上午9:45:56
   */
  public enum FileNameExtension {
    // 图片类型
    JPG("jpg"), JPEG("jpeg"), BMP("bmp"), PNG("png"), GIF("gif"), TIF("tif"), TIFF("tiff"), SVG("svg"), PSD("psd"), PCX(
        "pcx"), DIB("dib"), JFIF("jfif"),
    // 文档类型
    PDF("pdf"), TXT("txt"), DOC("doc"), DOCX("docx"), XLS("xls"), XLSX("xlsx"), PPT("ppt"), PPTX("pptx"),
    // 压缩类型
    RAR("rar"), ZIP("zip"), GZIP("gzip"), TAR("tar"), _7Z("7z"),
    // 音频类型
    MP3("mp3"), WAV("wav"),
    // 视频类型
    FLV("flv"), WMV("wmv"), WMA("wma"), AVI("avi"), RMVB("rmvb"), RM("rm"), MKV("mkv"), MP4("mp4"),
    // 网页类型
    HTM("htm"), HTML("html");

    private String value;

    private FileNameExtension(String value) {
      this.value = value;
    }

    public String getValue() {
      return this.value;
    }

    @Override
    public String toString() {
      return getValue();
    }

    /**
     * 获取FileNameExtension枚举中所有图片类型的文件后缀名的枚举集合
     *
     * @return 图片类型后缀的FileNameExtension枚举集合
     */
    public static EnumSet<FileNameExtension> getImagesFileNameExtension() {
      return EnumSet.of(JPG, JPEG, BMP, PNG, GIF, TIF, TIFF, SVG, PSD, PCX, DIB, JFIF);
    }

    /**
     * 判断给定的FileNameExtension枚举是否是图片类型， 判断的依据是看ext是否存在于图片类型的集合中，
     * 图片类型集合见方法{@link #getImagesFileNameExtension()}，如有新图片类型，可扩展
     *
     * @param ext
     * @return
     */
    public static boolean isImageFileNameExtension(FileNameExtension ext) {
      EnumSet<FileNameExtension> matched = getImagesFileNameExtension();
      return matched.contains(ext);
    }

    /**
     * 将字符串文件后缀名（忽略大小写）转换成FileNameExtension中对应值得枚举类型。
     * 如果ext为null，空白字符串，或者在FileNameExtension中无法匹配到对应的枚举，则返回null
     *
     * @param ext
     * @return 当且仅当ext不为空白字符串，不为null，能够匹配FileNameExtension枚举中的任意一个时，返回对应的枚举，否则返回null
     */
    public static FileNameExtension parseFileNameExtension(String ext) {
      if (isBlank(ext)) {
        return null;
      }
      EnumSet<FileNameExtension> fileNameExtensions = EnumSet.allOf(FileNameExtension.class);
      for (FileNameExtension e : fileNameExtensions) {
        if (e.getValue().equalsIgnoreCase(ext)) {
          return e;
        }
      }
      return null;
    }
  }

  public static final char SYMBOL_VIRGULE_CHAR = '/';
  public static final String SYMBOL_VIRGULE = "/";
  public static final char SYMBOL_DOT_CHAR = '.';
  public static final String SYMBOL_DOT = ".";
  public static final char SYMBOL_COLON_CHAR = ':';
  public static final String SYMBOL_COLON = ":";

  /**
   * <p>
   * 获取文件名的前缀，不包括文件扩展名，如果{@code fileName}值为{@code null}，返回空字符串{@code ""}。
   * </p>
   * <p>
   * 文件名前缀和扩展名分隔使用{@code "."}，不检查文件名是否符合规范，只要文件名字符串中有{@code "."}，那么就会默认最后一个{@code "."}为扩展名起始位置，返回{@code
   * "."}之前的字符串。
   * </p>
   * <b>举例：</b>
   * 
   * <pre>
   *  null        return ""
   *  ""          return ""
   *  "abcdef"    return "abcdef"
   *  "abcd.ef"   return "abdc"
   *  "ab.cd.ef"  return "ab.cd"
   *  ".ef"       return ""
   * </pre>
   * 
   * @param fileName 文件名
   * @return 返回{@code fileName}中最后一个{@code "."}之后的字符串。
   */
  public static String getFileNamePrefix(final String fileName) {
    return fileName == null ? "" : StringUtils.splitPreserveAllTokensByReverse(fileName, SYMBOL_DOT, 2)[0];
  }

  /**
   * <p>
   * 获取文件扩展名字符串，不包括{@code "."}。
   * </p>
   * <p>
   * 文件名前缀和扩展名分隔使用{@code "."}，不检查文件名是否符合规范，只要文件名字符串中有{@code "."}，
   * 那么就会默认最后一个{@code "."}为扩展名起始位置，返回{@code "."}之后的字符串。
   * </p>
   * <b>举例：</b>
   * 
   * <pre>
   *  null            return ""
   *  ""              return ""
   *  "abcdefgh"      return ""
   *  "abcdef.jpg"    return "jpg"
   *  "ghijk.gif"     return "gif"
   *  "abc.efgh"      return "efgh"
   *  "abc.efg.h"     return "h"
   *  "abc."          return ""
   * </pre>
   * 
   * @param fileName 文件名
   * @return 当且仅当有文件扩展名时返回文件扩展名（不包括“.”）；否则返回“”。
   */
  public static String getFileNameExtensionStr(final String fileName) {
    String[] tokens = StringUtils.splitPreserveAllTokensByReverse(fileName, SYMBOL_DOT, 2);
    if (tokens != null && tokens.length == 2) {
      return tokens[1];
    }
    return "";
  }

  /**
   * 返回文件扩展名字符串匹配到的枚举类型；根据{@link #getFileNameExtensionStr(String)}获取到的文件扩展名字符串去匹配{@link FileNameExtension#parseFileNameExtension(String)}对应的{@link FileNameExtension}
   *
   * @see #getFileNameExtensionStr(String)
   * @see FileNameExtension#parseFileNameExtension(String)
   * @see FileNameExtension
   * @param fileName
   * @return 当且仅当fileName不为null，有文件扩展名，而且能够匹配FileNameExtension枚举中的任意一个时，返回对应的枚举，否则返回null
   */
  public static FileNameExtension getFileNameExtension(final String fileName) {
    return FileNameExtension.parseFileNameExtension(getFileNameExtensionStr(fileName));
  }

  /**
   * <p>
   * 给定一个文件路径或者目录路径，检查是否存在，不存在则创建目录。如果给定的{@code filePath}是文件路径，则检查其父目录是否已经存在，不存在则创建；
   * 如果给定的{@code filePath}是个目录路径，则检查是否已经存在，不存在则创建。
   * </p>
   * <p>
   * {@code filePath}是文件路径还是目录路径的判定依据：<br>
   * 看是否有文件扩展名，扩展名通过方法{@link #getFileNameExtensionStr(String)}获得，
   * 得到扩展名后，会检查是否是合法扩展名，只要不包含目录分隔符的，都是合法扩展名，如果包含目录分隔符，则认为是不合法的扩展名。
   * 有合法扩展名的，被认为是文件路径；没有合法扩展名的，认为是目录路径，检查并创建该路径。
   * </p>
   * 
   *
   * @param filePath 文件或目录路径
   * @return 当且仅当发生创建目录的行为并成功创建后，返回true，否则返回false。注意：目录已经存在的话，返回false
   */
  public static boolean mkdirIfNotExist(String filePath) {
    boolean flag = false;
    String ext = getFileNameExtensionStr(filePath);
    File file = new File(filePath);
    if (StringUtils.isBlank(ext) || ext.contains(SYMBOL_VIRGULE) || ext.contains(File.pathSeparator)) {
      if (!file.exists()) {
        flag = file.mkdirs();
      }
    } else {
      File parentFile = file.getParentFile();
      if (!parentFile.exists()) {
        flag = parentFile.mkdirs();
      }
    }
    return flag;
  }

  private static boolean isBlank(String str) {
    if (str == null) {
      return true;
    } else {
      if (str.trim().length() == 0) {
        return true;
      } else {
        return false;
      }
    }
  }

  public static String openFile(String filePath) throws Exception {
    int HttpResult; // 服务器返回的状态
    String content = new String();
    try {
      URL url = new URL(filePath); // 创建URL
      URLConnection urlconn = url.openConnection(); // 试图连接并取得返回状态码
      urlconn.connect();
      HttpURLConnection httpconn = (HttpURLConnection) urlconn;
      HttpResult = httpconn.getResponseCode();
      if (HttpResult != HttpURLConnection.HTTP_OK) {
        System.out.print("无法连接到");
      } else {
        int filesize = urlconn.getContentLength(); // 取数据长度
        InputStreamReader isReader = new InputStreamReader(urlconn.getInputStream(), "UTF-8");
        BufferedReader reader = new BufferedReader(isReader);
        StringBuffer buffer = new StringBuffer();
        String line; // 用来保存每行读取的内容
        line = reader.readLine(); // 读取第一行
        while (line != null) { // 如果 line 为空说明读完了
          buffer.append(line); // 将读到的内容添加到 buffer 中
          buffer.append(System.lineSeparator()); // 添加换行符
          line = reader.readLine(); // 读取下一行
        }
        content = buffer.toString();
      }
    } catch (FileNotFoundException e) {
      throw e;
    } catch (IOException e) {
      throw e;
    }
    return content;
  }

  /**
   * 清理非法文件名并截取
   * 
   * @return
   */
  public static String cleanArcFileName(String fileName) {
    if (StringUtils.isBlank(fileName)) {
      return null;
    }
    // 文件名在操作系统中不允许出现 / \ " : | * ? < > 故将其以空替代
    Pattern pattern = Pattern.compile("[\\\\/:\\*\\?\\\"<>\\|]");
    Matcher matcher = pattern.matcher(fileName);
    fileName = matcher.replaceAll("").trim();
    if (fileName.length() > 255) {
      fileName = fileName.substring(0, 250);
    }
    return fileName;
  }

  /**
   * 创建文件夹
   * 
   * @param folderName
   * @return
   */
  public static boolean makeFolder(String folderName) {
    if (StringUtils.isBlank(folderName)) {
      return false;
    }
    File folder = new File(folderName);
    if (!folder.exists()) {
      return folder.mkdir();
    } else {
      return true;
    }
  }

  /**
   * 创建文件
   * 
   * @param folderName
   * @param fileName
   * @return
   */
  public static boolean makeFile(String folderName, String fileName) {
    try {
      if (StringUtils.isBlank(fileName)) {
        return false;
      }
      File file = new File(folderName + fileName);
      if (!file.exists()) {
        return file.createNewFile();
      } else {
        return true;
      }
    } catch (Exception e) {
      return false;
    }
  }
}
