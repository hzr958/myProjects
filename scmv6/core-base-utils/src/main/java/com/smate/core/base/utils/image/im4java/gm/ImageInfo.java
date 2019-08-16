package com.smate.core.base.utils.image.im4java.gm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

import org.im4java.core.IM4JavaException;
import org.im4java.core.IdentifyCmd;
import org.im4java.process.ArrayListOutputConsumer;
import org.im4java.process.Pipe;

import com.smate.core.base.utils.exception.ImageInfoException;

/**
 * GraphicsMagick/ImageMagick的图片信息类
 * 
 * @author houchuanjie
 * @date 2018年1月13日 上午11:11:14
 */
public class ImageInfo {
  private Hashtable<String, String> iAttributes = null;
  private LinkedList<Hashtable<String, String>> iAttribList = new LinkedList<>();

  /**
   * 新建ImageInfo，使用GraphicsMagick命令获取图片基本信息
   * 
   * @param fileName 图片文件名（全名称路径）
   * @throws ImageInfoException
   */
  public ImageInfo(String fileName) throws ImageInfoException {
    this.getBaseInfo(fileName, (InputStream) null, false);
  }

  /**
   * 新建ImageInfo，使用GraphicsMagick命令获取图片基本信息
   * 
   * @param args 参数
   * @param imageInput 图片文件的输入流
   * @throws ImageInfoException
   */
  public ImageInfo(String args, InputStream imageInput) throws ImageInfoException {
    if (imageInput != null && !args.equals("-") && !args.endsWith(":-")) {
      throw new IllegalArgumentException("illegal filename for piped input");
    } else {
      this.getBaseInfo(args, imageInput, false);
    }
  }

  /**
   * 新建ImageInfo，获取图片基本信息； useIM为true时，使用使用ImageMagick命令，否则使用GraphicsMagick命令
   * 
   * @param fileName 图片文件名（全名称路径）
   * @param useIM true使用ImageMagick，false使用GraphicsMagick
   * @throws ImageInfoException
   */
  public ImageInfo(String fileName, boolean useIM) throws ImageInfoException {
    this.getBaseInfo(fileName, (InputStream) null, useIM);
  }

  /**
   * 新建ImageInfo，获取图片基本信息； useIM为true时，使用使用ImageMagick命令，否则使用GraphicsMagick命令
   * 
   * @param args 参数
   * @param imageInput 图片文件输入流
   * @param useIM true使用ImageMagick，false使用GraphicsMagick
   * @throws ImageInfoException
   */
  public ImageInfo(String args, InputStream imageInput, boolean useIM) throws ImageInfoException {
    if (imageInput != null && !args.equals("-") && !args.endsWith(":-")) {
      throw new IllegalArgumentException("illegal filename for piped input");
    } else {
      this.getBaseInfo(args, imageInput, useIM);
    }
  }

  private void getBaseInfo(String imageFileArgs, InputStream imageInput, boolean useIM) throws ImageInfoException {
    GMOperation option = new GMOperation();
    option.ping();
    option.format("%m\n%w\n%h\n%g\n%W\n%H\n%G\n%q\n%r");
    option.addImage(new String[] {imageFileArgs});

    try {
      IdentifyCmd cmd = new IdentifyCmd(!useIM);
      ArrayListOutputConsumer outConsumer = new ArrayListOutputConsumer();
      cmd.setOutputConsumer(outConsumer);
      if (imageInput != null) {
        Pipe pipe = new Pipe(imageInput, (OutputStream) null);
        cmd.setInputProvider(pipe);
      }

      cmd.run(option, new Object[0]);
      ArrayList<String> outList = outConsumer.getOutput();
      Iterator<String> iter = outList.iterator();
      this.iAttributes = new Hashtable<>();
      this.iAttributes.put("Format", iter.next());
      this.iAttributes.put("Width", iter.next());
      this.iAttributes.put("Height", iter.next());
      this.iAttributes.put("Geometry", iter.next());
      this.iAttributes.put("PageWidth", iter.next());
      this.iAttributes.put("PageHeight", iter.next());
      this.iAttributes.put("PageGeometry", iter.next());
      this.iAttributes.put("Depth", iter.next());
      this.iAttributes.put("Type", iter.next());
      this.iAttribList.add(this.iAttributes);
    } catch (IOException | InterruptedException | IM4JavaException e) {
      throw new ImageInfoException(e.getMessage());
    }
  }

  public String getImageFormat() {
    return this.iAttributes.get("Format");
  }

  public String getImageFormat(int index) {
    return this.iAttribList.get(index).get("Format");
  }

  public int getImageWidth() throws ImageInfoException {
    return this.getImageWidth(this.iAttribList.size() - 1);
  }

  public int getImageWidth(int index) throws ImageInfoException {
    try {
      return Integer.parseInt(this.iAttribList.get(index).get("Width"));
    } catch (NumberFormatException arg2) {
      throw new ImageInfoException(arg2);
    }
  }

  public int getImageHeight() throws ImageInfoException {
    return this.getImageHeight(this.iAttribList.size() - 1);
  }

  public int getImageHeight(int index) throws ImageInfoException {
    try {
      return Integer.parseInt(this.iAttribList.get(index).get("Height"));
    } catch (NumberFormatException e) {
      throw new ImageInfoException(e);
    }
  }

  public String getImageGeometry() {
    return this.iAttributes.get("Geometry");
  }

  public String getImageGeometry(int index) {
    return this.iAttribList.get(index).get("Geometry");
  }

  public int getImageDepth() throws ImageInfoException {
    return this.getImageDepth(this.iAttribList.size() - 1);
  }

  public int getImageDepth(int index) throws ImageInfoException {
    String[] depths = this.iAttribList.get(index).get("Depth").split("-|/", 2);

    try {
      return Integer.parseInt(depths[0]);
    } catch (NumberFormatException e) {
      throw new ImageInfoException(e);
    }
  }

  public String getImageType() {
    return this.iAttributes.get("Type");
  }

  public String getImageType(int index) {
    return this.iAttribList.get(index).get("Type");
  }

  public int getPageWidth() throws ImageInfoException {
    return this.getPageWidth(this.iAttribList.size() - 1);
  }

  public int getPageWidth(int index) throws ImageInfoException {
    try {
      return Integer.parseInt(this.iAttribList.get(index).get("PageWidth"));
    } catch (NumberFormatException e) {
      throw new ImageInfoException(e);
    }
  }

  public int getPageHeight() throws ImageInfoException {
    return this.getPageHeight(this.iAttribList.size() - 1);
  }

  public int getPageHeight(int index) throws ImageInfoException {
    try {
      return Integer.parseInt(this.iAttribList.get(index).get("PageHeight"));
    } catch (NumberFormatException e) {
      throw new ImageInfoException(e);
    }
  }

  public String getPageGeometry() {
    return this.iAttributes.get("PageGeometry");
  }

  public String getPageGeometry(int index) {
    return this.iAttribList.get(index).get("PageGeometry");
  }

  public String getProperty(String prop) {
    return this.iAttributes.get(prop);
  }

  public String getProperty(String prop, int index) {
    return this.iAttribList.get(index).get(prop);
  }

  public int getSceneCount() {
    return this.iAttribList.size();
  }

  public Enumeration<String> getPropertyNames() {
    return this.iAttributes.keys();
  }
}
