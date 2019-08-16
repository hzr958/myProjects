package com.smate.core.base.utils.person.avatars;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import com.smate.core.base.utils.file.ArchiveFileUtil;
import com.smate.core.base.utils.random.RandomNumber;
import com.smate.core.base.utils.string.IrisStringUtils;

/**
 * 
 * 生成随随机颜色图片 这里保存的图片文件不保存到 archver_file 表中
 * 
 * 
 * @author tsz
 *
 */
public class PersonAvatarsUtils {

  /**
   * Constructor of the object.Arial Black.
   */
  private static Font font = new Font("Arial", Font.PLAIN, 60);
  private static Font font1 = new Font("微软雅黑", Font.PLAIN, 50);

  /**
   * 随机颜色
   */
  private static int defaultRGB[][] =
      {{145, 113, 176}, {121, 133, 193}, {224, 187, 106}, {107, 156, 212}, {137, 189, 91}, {84, 171, 224}};

  /**
   * 
   * @param content 图片内容
   * @param id 图片保存位置，关键id 跟主页头像上传相同
   * @param filePath 文件初始根路径
   * @return
   * @throws Exception
   */
  public static String personAvatars(String content, Long id, String filePath) throws Exception {

    Font currentFont = null;
    if (IrisStringUtils.hasChineseWord(content)) {
      currentFont = font1; // 有中文
    } else {
      currentFont = font; // 没中文
    }

    int width = 120, height = 120;
    Random random = new Random();
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    Graphics2D g = image.createGraphics();
    // 背景
    int temp = random.nextInt(6);
    g.setColor(new Color(defaultRGB[temp][0], defaultRGB[temp][1], defaultRGB[temp][2]));
    g.fillRect(0, 0, width - 1, height - 1);
    // 边框
    g.setColor(new Color(defaultRGB[temp][0], defaultRGB[temp][1], defaultRGB[temp][2]));
    g.drawRect(0, 0, width - 1, height - 1);
    g.setFont(currentFont);
    g.setColor(new Color(255, 255, 255));
    FontMetrics fm = g.getFontMetrics(currentFont);
    int textWidth = fm.stringWidth(content.toUpperCase());
    int widthX = (width - textWidth) / 2;
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g.drawString(content.toUpperCase(), widthX, 80);
    g.dispose();
    // 写图片到指定路径
    String fileName = id + ".png";
    String fileDir = ArchiveFileUtil.getFileDir(fileName);
    String fileAllPath = filePath + fileDir + fileName;
    ImageIO.write(image, "png", createFile(fileAllPath));
    // 返回图片路径
    // A=DXXX 默认头像地址 D开头
    return fileDir + fileName + "?A=D" + RandomNumber.getRandomStr(3);

  }

  private static File createFile(String path) throws IOException {
    File file = new File(path);
    // 目录是否存在
    File parentFile = file.getParentFile();
    if (!parentFile.exists()) {
      boolean b = false;
      try {
        b = parentFile.mkdirs();
      } catch (Throwable e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      if (!b) {
        throw new IOException("文件夹" + parentFile.getPath() + "创建失败");
      }
      if (!parentFile.exists()) {
        throw new IOException("文件夹" + parentFile.getPath() + "创建创建后目录不存在");
      }
    }
    return file;
  }

  public static void main(String[] args) {

    try {
      personAvatars("中文", 0L, "C:\\Users\\Administrator\\Desktop\\test\\");
      personAvatars("字体", 1L, "C:\\Users\\Administrator\\Desktop\\test\\");
      personAvatars("中文", 2L, "C:\\Users\\Administrator\\Desktop\\test\\");
      personAvatars("HR", 4L, "C:\\Users\\Administrator\\Desktop\\test\\");
      personAvatars("IG", 3L, "C:\\Users\\Administrator\\Desktop\\test\\");
    } catch (Exception e) {
      // "C:\\Users\\Administrator\\Desktop\\" + id + ".png")
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

}
