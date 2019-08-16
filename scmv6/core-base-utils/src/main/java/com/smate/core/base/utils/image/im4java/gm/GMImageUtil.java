package com.smate.core.base.utils.image.im4java.gm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.gm4java.engine.GMException;
import org.gm4java.engine.GMService;
import org.gm4java.engine.GMServiceException;
import org.gm4java.engine.support.SimpleGMService;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.process.ProcessExecutor;
import org.im4java.process.ProcessStarter;
import org.im4java.process.ProcessTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smate.core.base.utils.exception.ImageInfoException;
import com.smate.core.base.utils.image.im4java.gm.GMOperation.DensityUnitEnum;
import com.smate.core.base.utils.image.im4java.gm.GMOperation.GeometryEnum;

/**
 * GraphicsMagick图片处理工具类
 *
 * @author houchuanjie
 * @date 2018年1月9日 下午4:46:23
 */
public class GMImageUtil {
  private static final Logger logger = LoggerFactory.getLogger(GMImageUtil.class);
  private static String searchPath = "/usr/local/graphicsmagick/bin";
  private static boolean available = false;
  static {
    try {
      // searchPath = System.getenv("GM_HOME");
      // ProcessStarter.setGlobalSearchPath(searchPath);
      Runtime.getRuntime().exec("gm");
      available = true;
    } catch (IOException e) {
      logger.warn("Invalid IM4JAVA_TOOLPATH: '{}'", ProcessStarter.getGlobalSearchPath());
      // 解决Linux不重启获取不到环境变量IM4JAVA_TOOLPATH的问题
      ProcessStarter.setGlobalSearchPath(searchPath);
      try {
        Runtime.getRuntime().exec(searchPath + "/gm");
        available = true;
      } catch (IOException e1) {
        logger.error("'{}'路径下未找到可执行程序'gm'，GraphicsMagick未安装或者环境变量未正确配置, 所以GMImageUtil工具类将不会正常工作!", searchPath, e1);
      }
    }
  }

  /**
   * 重绘图片大小
   * 
   * @param width 目标宽度
   * @param height 目标高度
   * @param force 是否强制缩放或拉伸至目标大小
   * @param srcPath 原图路径
   * @param destPath 目标路径
   * @return
   */
  public static boolean resize(final int width, final int height, boolean force, String srcPath, String destPath) {
    try {
      GMOperation op = new GMOperation();
      createDirIfNotExist(destPath);
      if (force) {
        op.resize(width, height, Collections.singletonList(GMOperation.GeometryAnnotation.ForceDimensions));
      } else {
        op.resize(width, height);
      }
      op.addImage(srcPath).addImage(destPath);
      ConvertCmd convertCmd = new ConvertCmd(true);
      convertCmd.run(op);
      return true;
    } catch (IOException | InterruptedException | IM4JavaException e) {
      logger.error("转换图片-调整大小时出错！图片地址：{}", srcPath, e);
      return false;
    }
  }

  /**
   * 生成缩略图
   * 
   * @param width 缩略图宽度
   * @param height 缩略图高度
   * @param force 是否强制拉伸
   * @param srcPath 源文件路径
   * @param destPath 目标文件路径
   * @throws Exception
   */
  public static void thumbnail(final int width, final int height, boolean force, String srcPath, String destPath)
      throws Exception {
    try {
      GMOperation op = new GMOperation();
      createDirIfNotExist(destPath);
      op.thumbnail(width, height, force).addImage(srcPath).addImage(destPath);
      ConvertCmd cmd = new ConvertCmd(true);
      cmd.run(op);
    } catch (IOException | InterruptedException | IM4JavaException | IllegalArgumentException e) {
      logger.error("生成缩略图出错！图片地址：{}", srcPath, e);
      throw e;
    }
  }

  /**
   * 生成缩略图
   * 
   * @param width 缩略图宽度
   * @param height 缩略图高度
   * @param force 是否强制拉伸
   * @param srcPath 源文件路径
   * @param destPath 目标文件路径
   * @throws Exception
   */
  public static void sample(final int width, final int height, boolean force, String srcPath, String destPath)
      throws Exception {
    try {
      GMOperation op = new GMOperation();
      createDirIfNotExist(destPath);
      if (force) {
        op.sample(width, height, (int) GeometryEnum.ForceDimensions.getValue());
      } else {
        op.sample(width, height);
      }
      op.quality(80);
      op.addImage(srcPath).addImage(destPath);
      ConvertCmd cmd = new ConvertCmd(true);
      cmd.run(op);
    } catch (IOException | InterruptedException | IM4JavaException e) {
      logger.error("生成缩略图出错！图片地址：{}", srcPath, e);
      throw e;
    }
  }

  /**
   * 获取图片基本信息
   *
   * @author houchuanjie
   * @date 2018年1月15日 上午11:42:43
   * @param filepath
   * @return
   * @throws ImageInfoException
   */
  public static ImageInfo getImageInfo(String filepath) throws ImageInfoException {
    try {
      ImageInfo info = new ImageInfo(filepath);
      return info;
    } catch (ImageInfoException e) {
      logger.error("获取图片信息出错！文件不存在或已损坏！", e);
      throw e;
    }
  }

  /**
   * 按图片或PDF原始比例转换为目标图片
   * 
   * @param quality 目标质量：0 - 100，数字越大，质量越高
   * @param density 目标分辨率，值越高越清晰
   * @param unit 分辨率的单位
   * @param srcPath 源图片文件路径
   * @param destPath 目标图片文件路径
   */
  public static void convert(final int quality, final int density, DensityUnitEnum unit, String srcPath,
      String destPath) throws Exception {
    try {
      GMOperation op = new GMOperation();
      createDirIfNotExist(destPath);
      op.quality(quality).density(density).units(unit.toString()).addImage(srcPath).addImage(destPath);
      ConvertCmd cmd = new ConvertCmd(true);
      cmd.run(op);
    } catch (Exception e) {
      logger.error("转换图片出错！", e);
      throw e;
    }
  }

  private static void createDirIfNotExist(String destPath) {
    File destFileDir = (new File(destPath)).getParentFile();
    if (!destFileDir.exists()) {
      destFileDir.mkdirs();
    }
  }

  /**
   * 执行gm commands
   * 
   * @param commands
   * @return
   */
  private static boolean executeCommands(List<String> commands) {
    assert null != commands && commands.size() > 0;
    String result = "";
    try {
      result = getGMService().execute(commands);
    } catch (GMException | GMServiceException | IOException e) {
      logger.error("处理结果：{}，异常：{}", result, e.toString());
      return false;
    }
    return true;
  }

  private static GMService getGMService() {
    SimpleGMService simpleGMService = new SimpleGMService();
    return simpleGMService;
  }

  /**
   * 功能描述：批量处理pdf首页转图片压缩或者图片压缩，比较耗性能，需要合理配置可执行线程数，默认为6
   * 
   * @param filePathBeans，要处理的图片信息的集合
   * @return Map<String ,List<FilePathBean>> 执行结果集<br>
   *         key:success ,value: 执行成功List<FilePathBean><br>
   *         key:error ,value: 执行失败List<FilePathBean><br>
   * @author LIJUN
   * @date 2018年4月10日
   */
  public static Map<String, List<FilePathBean>> processThumbnailImage(List<FilePathBean> filePathBeans) {
    Map<String, List<FilePathBean>> map = new HashMap<>();
    List<FilePathBean> successList = new ArrayList<>();
    List<FilePathBean> errorList = new ArrayList<>();
    // 可执行线程数8
    ProcessExecutor exec = new ProcessExecutor(6);
    try {
      // FilePathBean封装了图片/pdf处理参数
      for (FilePathBean filePathBean : filePathBeans) {
        // 获取IMOperation操作接口
        IMOperation op = getOpThumbnailImage(filePathBean.getFileFullPath(), filePathBean.getWidth(),
            filePathBean.getHeight(), filePathBean.getQuality());
        File destFileDir = (new File(filePathBean.getFileFullToPath())).getParentFile();
        if (!destFileDir.exists()) {
          destFileDir.mkdirs();
        }
        if (null == op) {
          logger.error(filePathBean.getFileName() + " the file is damaged!");
          errorList.add(filePathBean);// 记录执行失败的记录
          continue;
        }
        ConvertCmd cmd = new ConvertCmd(true);
        // 传入IMOperation操作接口,获取ProcessTask处理任务
        ProcessTask pt = cmd.getProcessTask(op, filePathBean.getFileFullPath(), filePathBean.getFileFullToPath());
        exec.execute(pt);// 无返回,sumbmit带返回值但会阻塞线程
        successList.add(filePathBean);// 记录执行成功的记录
      }
      exec.shutdown();
      while (true) {
        if (exec.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
          logger.debug("Batch processing thumbnail over!");
          break;
        }
      }
    } catch (Exception e) {
      logger.error("Batch processing thumbnail error: " + e.getMessage());

    }
    map.put("success", successList);
    map.put("error", errorList);
    return map;
  }

  /**
   * 功能描述：压缩图片操作的接口
   * 
   * @param filePath 原图路径
   * @param width 压缩图宽度
   * @param height 压缩图高度
   * @param quality 设置图片的质量
   * @return IMOperation，im4java处理图片操作的接口
   */
  public static IMOperation getOpThumbnailImage(String filePath, String width, String height, String quality) {
    String srcWidth;
    String srcHeight;
    try {
      // 这里如果图片是损坏的,则直接返回null
      ImageInfo info = new ImageInfo(filePath);
      srcWidth = String.valueOf(info.getImageWidth());
      srcHeight = String.valueOf(info.getImageHeight());
    } catch (ImageInfoException e) {
      logger.error("Get picture/pdf information error, please ensure image/pdf is complete: " + e.getMessage());
      return null;
    }

    IMOperation op = new IMOperation();
    op.addImage();
    // 没有指定高度则只设置宽
    if (StringUtils.isNotEmpty(width) && StringUtils.isEmpty(height)) {
      op.addRawArgs("-scale", width);

    }
    // 没有指定宽度则只设置高
    else if (StringUtils.isNotEmpty(height) && StringUtils.isEmpty(width)) {
      op.addRawArgs("-scale", "x" + height);
    }
    // 没有指定宽高则使用原始图的宽高
    else if (StringUtils.isEmpty(width) && StringUtils.isEmpty(height)) {
      op.addRawArgs("-scale", srcWidth + "x" + srcHeight + "^");
      op.addRawArgs("-gravity", "center");
      op.addRawArgs("-extent", srcWidth + "x" + srcHeight);
    }
    // 其他用设置的宽高
    else {
      op.addRawArgs("-scale", width + "x" + height + "^");
      op.addRawArgs("-gravity", "center");
      op.addRawArgs("-extent", width + "x" + height);
    }

    if (StringUtils.isEmpty(quality)) {
      op.addRawArgs("-quality", "10");// 没有指定质量则为10%
    } else {
      op.addRawArgs("-quality", quality);
    }
    op.strip();
    op.addImage();
    return op;
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
