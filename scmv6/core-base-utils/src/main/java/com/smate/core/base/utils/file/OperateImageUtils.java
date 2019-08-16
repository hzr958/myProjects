package com.smate.core.base.utils.file;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// import com.sun.image.codec.jpeg.JPEGCodec;
// import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 图片裁切.
 * 
 * @author pwl
 * 
 */
@SuppressWarnings("restriction")
public class OperateImageUtils {

  protected static Logger logger = LoggerFactory.getLogger(OperateImageUtils.class);

  /**
   * 复制图片.
   * 
   * @param srcPath 读取源图片路径.
   * @param toRootPath 写入图片根目录.
   * @throws IOException
   */
  public static List<String> copyImage(String srcPath, String toRootPath, String srcFileName) throws IOException {
    List<String> imageNameList = new ArrayList<String>();
    BufferedInputStream inBuff = null;
    BufferedOutputStream outBuff = null;
    try {
      // 新建文件输入流并对它进行缓冲
      inBuff = new BufferedInputStream(new FileInputStream(srcPath));
      File imageDir = new File(toRootPath);
      if (!imageDir.exists()) {
        imageDir.mkdirs();
      }
      String fileType = srcFileName.substring(srcFileName.lastIndexOf(".") + 1, srcFileName.length());
      String targetFileName = "1." + fileType;
      File imageFile = new File(toRootPath + "/" + targetFileName);
      if (!imageFile.exists()) {
        imageFile.createNewFile();
      }
      // 新建文件输出流并对它进行缓冲
      outBuff = new BufferedOutputStream(new FileOutputStream(imageFile));
      // 缓冲数组
      byte[] b = new byte[1024 * 5];
      int len;
      while ((len = inBuff.read(b)) != -1) {
        outBuff.write(b, 0, len);
      }
      // 刷新此缓冲的输出流
      outBuff.flush();
      imageNameList.add(targetFileName);
    } finally {
      // 关闭流
      if (inBuff != null)
        inBuff.close();
      if (outBuff != null)
        outBuff.close();
    }
    return imageNameList;
  }

  /**
   * 长高等比例缩小图片
   * 
   * @param srcFilePath 读取源图片路径.
   * @param desFilePath 写入图片目录.
   * @param ratio 缩放比例.
   * 
   * @throws IOException
   */
  public static void reduceImageEqualProportion(String srcFilePath, String desFilePath, float ratio) throws Exception {
    FileOutputStream out = null;
    BufferedImage src = null;
    BufferedImage tag = null;
    try {
      // 读入文件
      File file = new File(srcFilePath);
      // 构造Image对象
      src = javax.imageio.ImageIO.read(file);
      int width = src.getWidth();
      int height = src.getHeight();
      // 缩小边长
      int newWidth = new Float(width * ratio).intValue();
      int newHeight = new Float(height * ratio).intValue();
      tag = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
      // 绘制缩小后的图片
      tag.getGraphics().drawImage(src, 0, 0, newWidth, newHeight, null);
      File imageFile = new File(desFilePath);
      if (!imageFile.getParentFile().exists()) {
        imageFile.getParentFile().mkdirs();
      }
      out = new FileOutputStream(imageFile);
      // JPEGImageEncoder在jdk1.7以上的版本已经删除，编译报错
      // JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
      // encoder.encode(tag);
      javax.imageio.ImageIO.write(tag, "JPEG", out);
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception(e);
    } finally {
      if (src != null) {
        src.flush();
      }

      if (tag != null) {
        tag.flush();
      }

      if (out != null) {
        try {
          out.close();
        } catch (IOException e) {
          throw new Exception(e);
        }
      }
    }
  }
}
