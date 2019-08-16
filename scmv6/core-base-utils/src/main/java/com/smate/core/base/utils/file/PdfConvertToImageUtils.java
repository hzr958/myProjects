package com.smate.core.base.utils.file;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

/*
 * import org.icepdf.core.exceptions.PDFException; import
 * org.icepdf.core.exceptions.PDFSecurityException; import org.icepdf.core.pobjects.Document; import
 * org.icepdf.core.pobjects.Page; import org.icepdf.core.util.GraphicsRenderingHints;
 */
import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smate.core.base.utils.exception.BatchTaskException;


/**
 * pdf转换成图片工具类.
 * 
 * @author pwl
 * 
 */
public class PdfConvertToImageUtils {

  protected static Logger logger = LoggerFactory.getLogger(PdfConvertToImageUtils.class);

  /**
   * 
   * @param filePath 原文件路径，例如d:/test.pdf
   * @param imagePath 图片生成的根目录路径，例如 d:/fulltext_image
   * @param scale 缩略图显示倍数，1.0表示不缩放，0.3则缩小到30%
   * @param startIndex 转换起始页，页码从0开始算起
   * @param maxSize 需要转换多少页，0转换到最后页
   * @throws BatchTaskException，FileNotFoundException
   */
  public static List<String> convertToImage(String filePath, String imageRootPath, float scale, int startIndex,
      int maxSize) throws BatchTaskException, FileNotFoundException {
    Document document = new Document();
    BufferedImage image = null;
    try {
      document.setFile(filePath);
      float rotation = 0f;
      int totalPage = document.getNumberOfPages();
      // 如果起始页大于总页数则转换最后一页
      startIndex = startIndex >= totalPage ? totalPage - 1 : startIndex;
      // 转换最大页数为0则从开始页转换到最后一页
      maxSize = maxSize == 0 ? totalPage : maxSize;
      List<String> imageNameList = new ArrayList<String>();
      for (int i = startIndex; i < totalPage && i < maxSize; i++) {
        image = (BufferedImage) document.getPageImage(i, GraphicsRenderingHints.PRINT, Page.BOUNDARY_CROPBOX, rotation,
            scale);
        RenderedImage rendImage = image;
        File imageDir = new File(imageRootPath);
        if (!imageDir.exists()) {
          imageDir.mkdirs();
        }
        File file = new File(imageRootPath + "/" + (i + 1) + ".png");
        ImageIO.write(rendImage, "png", file);
        image.flush();
        imageNameList.add((i + 1) + ".png");
      }
      return imageNameList;
    } catch (PDFException ex) {
      String errorMsg = "文件{}转换成图片出现PDFException异常：" + filePath + "  |||  " + ex.getMessage();
      throw new BatchTaskException(errorMsg);
    } catch (PDFSecurityException ex) {
      String errorMsg = "文件{}转换成图片出现PDFSecurityException异常：" + filePath + "  |||  " + ex.getMessage();
      throw new BatchTaskException(errorMsg);
    } catch (FileNotFoundException ex) {
      String errorMsg = "文件{}转换成图片出现FileNotFoundException异常：" + filePath + "  |||  " + ex.getMessage();
      throw new FileNotFoundException(errorMsg);
    } catch (IOException ex) {
      String errorMsg = "文件{}转换成图片出现IOException异常, 文件可能已损坏:  " + filePath + "  |||  " + ex.getMessage();
      throw new BatchTaskException(errorMsg);
    } finally {
      document.dispose();

      if (image != null) {
        image.flush();
      }
    }
  }

  /**
   * 转换全文的那一页为图片.
   * 
   * @param filePath
   * @param imagePath
   * @param scale
   * @param pageIndex
   * @throws BatchTaskException，FileNotFoundException
   */
  public static void convertToImage(String filePath, String imagePath, String imageExt, float scale, int pageIndex)
      throws Exception, FileNotFoundException {
    Document document = new Document();
    BufferedImage image = null;
    try {
      document.setFile(filePath);
      float rotation = 0f;
      int totalPage = document.getNumberOfPages();
      if (totalPage > 0) {
        pageIndex = pageIndex >= totalPage ? totalPage - 1 : pageIndex;
        image = (BufferedImage) document.getPageImage(pageIndex, GraphicsRenderingHints.PRINT, Page.BOUNDARY_CROPBOX,
            rotation, scale);
        RenderedImage rendImage = image;
        File imageFile = new File(imagePath);
        // 目录是否存在
        File parentFile = imageFile.getParentFile();
        if (!parentFile.exists()) {
          parentFile.mkdirs();
        }
        ImageIO.write(rendImage, imageExt, imageFile);
      }
    } catch (PDFException ex) {
      String errorMsg = "文件{}转换成图片出现PDFException异常：" + filePath + "  |||  " + ex.getMessage();
      throw new Exception(errorMsg);
    } catch (PDFSecurityException ex) {
      String errorMsg = "文件{}转换成图片出现PDFSecurityException异常：" + filePath + "  |||  " + ex.getMessage();
      throw new Exception(errorMsg);
    } catch (FileNotFoundException ex) {
      String errorMsg = "文件{}转换成图片出现FileNotFoundException异常：" + filePath + "  |||  " + ex.getMessage();
      throw new FileNotFoundException(errorMsg);
    } catch (IOException ex) {
      String errorMsg = "文件{}转换成图片出现IOException异常, 文件可能已损坏:  " + filePath + "  |||  " + ex.getMessage();
      throw new Exception(errorMsg);
    } finally {
      document.dispose();

      if (image != null) {
        image.flush();
      }
    }
  }
}
