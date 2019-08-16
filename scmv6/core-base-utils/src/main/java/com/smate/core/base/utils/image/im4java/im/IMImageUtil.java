package com.smate.core.base.utils.image.im4java.im;

import java.io.File;
import java.io.IOException;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.process.ProcessStarter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smate.core.base.utils.image.im4java.gm.GMOperation.GeometryEnum;

/**
 * ImageMagick图片处理工具类
 * 
 * @author houchuanjie
 * @date 2018/05/08 17:34
 */
public class IMImageUtil {
  private static final Logger logger = LoggerFactory.getLogger(IMImageUtil.class);
  private static String searchPath = "/usr/local/imagemagick/bin";
  // 是否可用
  private static boolean available = false;
  static {
    try {
      searchPath = System.getenv("IM_HOME");
      ProcessStarter.setGlobalSearchPath(searchPath);
      Runtime.getRuntime().exec("convert -version");
      available = true;
    } catch (IOException e) {
      logger.error("Invalid IM_HOME: '{}'", ProcessStarter.getGlobalSearchPath());
      // 解决Linux不重启获取不到环境变量IM4JAVA_TOOLPATH的问题
      ProcessStarter.setGlobalSearchPath("/usr/local/imagemagick/bin");
      try {
        Runtime.getRuntime().exec("convert -version");
        available = true;
      } catch (IOException e1) {
        logger.error("ImageMagick未安装或者环境变量未正确配置, 所以IMImageUtil工具类将不会正常工作!", e);
      }
    }
  }

  /**
   * 生成缩略图
   *
   * @param width
   * @param height
   * @param force
   * @param srcPath
   * @param destPath
   * @throws Exception
   */
  public static void thumbnail(final int width, final int height, boolean force, String srcPath, String destPath)
      throws Exception {
    try {
      IMOperation op = new IMOperation();
      File destFileDir = (new File(destPath)).getParentFile();
      if (!destFileDir.exists()) {
        destFileDir.mkdirs();
      }
      op.thumbnail(width, height, GeometryEnum.ForceDimensions.getValue()).addImage(srcPath).addImage(destPath);
      ConvertCmd cmd = new ConvertCmd(false);
      cmd.run(op);
    } catch (IOException | InterruptedException | IM4JavaException | IllegalArgumentException e) {
      logger.error("生成缩略图出错！图片地址：{}", srcPath, e);
      throw e;
    }
  }

  /**
   * 是否可用
   * 
   * @return
   */
  public static boolean isAvailable() {
    return available;
  }
}
