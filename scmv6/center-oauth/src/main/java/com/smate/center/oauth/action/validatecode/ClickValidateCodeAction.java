package com.smate.center.oauth.action.validatecode;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.opensymphony.xwork2.ActionSupport;
import com.smate.center.oauth.cache.OauthCacheService;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;

/**
 * test the validatecode
 * 
 * @author tsz
 *
 */
@Results({@Result(name = "main", location = "/WEB-INF/jsp/clickcode/main.jsp")})
public class ClickValidateCodeAction extends ActionSupport {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  private Font font = new Font("PMingLiU", Font.PLAIN, 20);
  @Value("${file.root}")
  private String fileroot;
  @Autowired
  private OauthCacheService oauthCacheService;

  public int getClickx() {
    return clickx;
  }

  public void setClickx(int clickx) {
    this.clickx = clickx;
  }

  public int getClicky() {
    return clicky;
  }

  public void setClicky(int clicky) {
    this.clicky = clicky;
  }

  private int clickx;

  private int clicky;

  private String token;

  @Action("/oauth/clickcheck")
  public String checkValidateCode() throws IOException {

    return "main";
  }

  /**
   * 按既定规则检查
   * 
   * @return
   * @throws Exception
   */
  @Action("/oauth/clickcheck/ajaxcode")
  public String checkValidateCode1() throws Exception {
    Object obj = oauthCacheService.get("clickCode", Struts2Utils.getSession().getId());
    List<Map<String, Object>> list = JacksonUtils.jsonListObjUnSerializer(obj.toString());
    // 主要是点击规则
    // 现在定点击小于5的数字
    Map<String, Object> map = new HashMap<String, Object>();
    for (int i = 0; i < list.size(); i++) {
      Mark mark = mapToBean(list.get(i), Mark.class);
      boolean xc = clickx > mark.getX() && clickx < mark.getX() + 20;
      boolean yc = clicky > mark.getY() - 20 && clicky < mark.getY();
      boolean cc = false;
      if (NumberUtils.isNumber(mark.getContent())) {
        cc = Integer.parseInt(mark.getContent()) < 5;
      }
      if (xc && yc && cc) {
        map.put("result", true);
        break;
      } else {
        map.put("result", false);
      }
    }
    map.put("status", "success");
    Struts2Utils.renderJson(map, "encoding:UTF-8");
    return null;
  }



  /**
   * 要有token 一次图片一次token
   * 
   * @return
   */
  @Action("/oauth/clickcheck/img")
  public String getCheckCode() {
    HttpServletResponse response = Struts2Utils.getResponse();
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);
    response.setContentType("image/jpeg");
    int width = 300, height = 180;
    Random random = new Random();
    // 读取列表然后随机取 (需要预先准备好图片以及图片对应字体颜色)

    String pathroot = fileroot + "/validatecodebg/";
    File rootFile = new File(pathroot);
    String[] files = rootFile.list();
    int ranImg = random.nextInt(files.length);
    // System.out.println(files[ranImg]);
    String path = pathroot + files[ranImg];
    String[] mainRBG = null;
    try {
      mainRBG = MainColor.getImagePixel(path);
    } catch (IOException e2) {
      // TODO Auto-generated catch block
      logger.error("读取文件失败", e2);
    }

    BufferedImage image1 = null;
    try {
      image1 = ImageIO.read(new FileInputStream(path));
    } catch (FileNotFoundException e1) {
      // TODO Auto-generated catch block
      logger.error("读取文件失败", e1);
    } catch (IOException e1) {
      // TODO Auto-generated catch block
      logger.error("读取文件失败", e1);
    }
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics g1 = image.getGraphics();
    Graphics2D g = (Graphics2D) g1;
    g.drawImage(image1, 0, 0, width, height, null);
    g.setFont(font);
    g.shear(0.0, 0.0);// 倾斜画布
    // 需要预先随机选好规则，然后按规则 随机生成内容
    String[] strarray = new String[] {"中", "2", "锅", "5"};
    List<Integer> xints = new ArrayList<Integer>();
    List<Integer> yints = new ArrayList<Integer>();
    List<Mark> listMark = new ArrayList<Mark>();
    for (int i = 0; i < 4; i++) {
      String tmp = strarray[i];
      g.setColor(new Color(Integer.parseInt(mainRBG[0]) + 50, Integer.parseInt(mainRBG[1]) + 50,
          Integer.parseInt(mainRBG[2]) + 50));
      int t = 0;
      int t1 = 0;
      int xcontinue = 0;
      int ycontinue = 0;
      aaa: do {
        t = random.nextInt(width - 50) + 20;
        for (int y = 0; y < xints.size(); y++) {
          if ((t + 20) < xints.get(y) || (t - 20) > xints.get(y)) {
            continue;
          } else {
            xcontinue++;
            continue aaa;
          }
        }
        xints.add(t);
        // 30次后 重新计算?
        break;
      } while (true);
      bbb: do {
        t1 = random.nextInt(height - 50) + 20;
        for (int y = 0; y < yints.size(); y++) {
          if ((t1 + 20) < yints.get(y) || (t1 - 20) > yints.get(y)) {
            continue;
          } else {
            ycontinue++;
            continue bbb;
          }
        }
        yints.add(t1);
        // 30次后 重新计算?
        break;
      } while (true);
      Mark m = new Mark(tmp, t, t1);
      listMark.add(m);
      g.drawString(tmp, t, t1);
    }
    oauthCacheService.put("clickCode", Struts2Utils.getSession().getId(), JacksonUtils.jsonObjectSerializer(listMark));
    g.dispose();

    try {
      ImageIO.write(image, "jpeg", response.getOutputStream());
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }



  Color getRandColor(int fc, int bc) {
    Random random = new Random();
    if (fc > 255)
      fc = 255;
    if (bc > 255)
      bc = 255;
    int r = random.nextInt(bc - fc) + bc;
    int g = random.nextInt(bc - fc) + bc;
    int b = random.nextInt(bc - fc) + bc;
    if (r > 255)
      r = 255;
    if (g > 255)
      g = 255;
    if (b > 255)
      b = 255;
    return new Color(r, g, b);
  }

  /**
   * 利用反射将map集合封装成bean对象
   * 
   * @param params
   * @param clazz
   * @return
   */
  public static <T> T mapToBean(Map<String, Object> map, Class<?> clazz) throws Exception {
    Object obj = clazz.newInstance();
    if (map != null && !map.isEmpty() && map.size() > 0) {
      for (Map.Entry<String, Object> entry : map.entrySet()) {
        String propertyName = entry.getKey(); // 属性名
        Object value = entry.getValue(); // 属性值
        String setMethodName = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
        Field field = getClassField(clazz, propertyName); // 获取和map的key匹配的属性名称
        if (field == null) {
          continue;
        }
        Class<?> fieldTypeClass = field.getType();
        value = convertValType(value, fieldTypeClass);
        try {
          clazz.getMethod(setMethodName, field.getType()).invoke(obj, value);
        } catch (NoSuchMethodException e) {
          e.printStackTrace();
        }
      }
    }
    return (T) obj;
  }

  /**
   * 根据给定对象类匹配对象中的特定字段
   * 
   * @param clazz
   * @param fieldName
   * @return
   */
  private static Field getClassField(Class<?> clazz, String fieldName) {
    if (Object.class.getName().equals(clazz.getName())) {
      return null;
    }
    Field[] declaredFields = clazz.getDeclaredFields();
    for (Field field : declaredFields) {
      if (field.getName().equals(fieldName)) {
        return field;
      }
    }
    Class<?> superClass = clazz.getSuperclass(); // 如果该类还有父类，将父类对象中的字段也取出
    if (superClass != null) { // 递归获取
      return getClassField(superClass, fieldName);
    }
    return null;
  }

  /**
   * 将map的value值转为实体类中字段类型匹配的方法
   * 
   * @param value
   * @param fieldTypeClass
   * @return
   */
  private static Object convertValType(Object value, Class<?> fieldTypeClass) {
    Object retVal = null;

    if (Long.class.getName().equals(fieldTypeClass.getName())
        || long.class.getName().equals(fieldTypeClass.getName())) {
      retVal = Long.parseLong(value.toString());
    } else if (Integer.class.getName().equals(fieldTypeClass.getName())
        || int.class.getName().equals(fieldTypeClass.getName())) {
      retVal = Integer.parseInt(value.toString());
    } else if (Float.class.getName().equals(fieldTypeClass.getName())
        || float.class.getName().equals(fieldTypeClass.getName())) {
      retVal = Float.parseFloat(value.toString());
    } else if (Double.class.getName().equals(fieldTypeClass.getName())
        || double.class.getName().equals(fieldTypeClass.getName())) {
      retVal = Double.parseDouble(value.toString());
    } else {
      retVal = value;
    }
    return retVal;
  }

  public static void main(String[] args) {
    Random random = new Random();
    int ranImg = random.nextInt(1);
    System.out.println(ranImg);
  }


}


/**
 * 检验规则
 * 
 * @author Administrator
 *
 */
class MarkRule {
  private String rule;

}


/**
 * 校验数据
 * 
 * @author Administrator
 *
 */
class Mark {
  private int x = 0;
  private int y = 0;
  private String content = "";

  public Mark() {
    super();
  }

  public Mark(String content, int x, int y) {
    super();
    this.x = x;
    this.y = y;
    this.content = content;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }



}
