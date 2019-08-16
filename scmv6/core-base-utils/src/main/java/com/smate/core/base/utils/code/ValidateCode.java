package com.smate.core.base.utils.code;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.smate.core.base.utils.cache.SnsCacheService;
import com.smate.core.base.utils.constant.security.SecurityConstants;

/**
 * 验证码
 * 
 * @author zk
 *
 */
public class ValidateCode extends HttpServlet {

  /**
   * serialVersionUID
   */
  private static final long serialVersionUID = -8287571585783798921L;

  /**
   * Constructor of the object.Arial Black.
   */
  private Font font = new Font("PMingLiU", Font.PLAIN, 16);

  private char[] codeSequence = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
      'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
      'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

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

  private String getRandomChar() {
    return String.valueOf(codeSequence[new Random().nextInt(62)]);
  }

  protected void service(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, java.io.IOException {
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);
    response.setContentType("image/jpeg");
    int width = 75, height = 18;
    Random random = new Random();
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics g = image.getGraphics();
    g.setColor(getRandColor(200, 250));
    g.fillRect(0, 0, width - 1, height - 1);
    g.setColor(new Color(102, 102, 102));
    g.drawRect(0, 0, width - 1, height - 1);
    g.setFont(font);
    g.setColor(getRandColor(160, 200));
    for (int i = 0; i < 155; i++) {
      int x = random.nextInt(width - 1);
      int y = random.nextInt(height - 1);
      int x1 = random.nextInt(6) + 1;
      int y1 = random.nextInt(12) - 1;
      g.drawLine(x, y, x + x1, y + y1);
    }
    for (int i = 0; i < 70; i++) {
      int x = random.nextInt(width - 1);
      int y = random.nextInt(height - 1);
      int x1 = random.nextInt(6) + 1;
      int y1 = random.nextInt(12) - 1;
      g.drawLine(x, y, y - x1, y - y1);
    }
    String sRand = "";
    for (int i = 0; i < 4; i++) {
      String tmp = getRandomChar();
      sRand += tmp;
      g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
      g.drawString(tmp, 15 * i + 10, 15);
    }
    /*
     * HttpSession session = request.getSession(true); session.removeAttribute("validateCode");
     * session.setAttribute("validateCode", sRand); g.dispose();
     */

    ApplicationContext ac = new ClassPathXmlApplicationContext("spring/applicationContext-sns-server.xml");
    SnsCacheService snsCacheService = (SnsCacheService) ac.getBean("snsCacheService");
    String sessionId = this.findServletRequestSessionId(request);
    snsCacheService.put(SecurityConstants.OAUTH_VALIDATE_CODE, 5 * 60, sessionId, sRand);
    g.dispose();

    ImageIO.write(image, "jpeg", response.getOutputStream());

  }

  /**
   * 优先从请求头中获取sessionId，因为直接用ServletRequest强转成的HttpServletRequest获取到的sessionId会改变
   *
   * @param request
   * @return
   */
  private String findServletRequestSessionId(HttpServletRequest request) {
    String cookieStr = request.getHeader("Cookie");
    String sessionId = "";
    if (StringUtils.isNotBlank(cookieStr)) {
      String[] cookies = cookieStr.split(";");
      if (ArrayUtils.isNotEmpty(cookies)) {
        for (String str : cookies) {
          if (str.contains("JSESSIONID=")) {
            sessionId = str.replace("JSESSIONID=", "").trim();
            break;
          }
        }
      }
      if (StringUtils.isBlank(sessionId)) {
        sessionId = request.getSession().getId();
      }
    }
    return sessionId;
  }
}
