package com.smate.center.oauth.service.security.validatecode;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import com.smate.center.oauth.cache.OauthCacheService;
import com.smate.core.base.utils.constant.security.SecurityConstants;

/**
 * 验证码
 * 
 * @author zk
 *
 */
public class ValidateCode implements Filter {

  protected static Logger LOG = LoggerFactory.getLogger(ValidateCode.class);

  /**
   * Constructor of the object.Arial Black.
   */
  private Font font = new Font("PMingLiU", Font.PLAIN, 30);

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
    int width = 100, height = 35;
    Random random = new Random();
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics g = image.getGraphics();
    // 背景
    g.setColor(new Color(255, 255, 255));
    g.fillRect(0, 0, width - 1, height - 1);
    // 边框
    g.setColor(new Color(255, 255, 255));
    g.drawRect(0, 0, width - 1, height - 1);
    g.setFont(font);
    // 设置噪点
    /*
     * for (int i = 0; i < 155; i++) { int x = random.nextInt(width - 1); int y = random.nextInt(height
     * - 1); int x1 = random.nextInt(6) + 1; int y1 = random.nextInt(12) - 1; g.drawLine(x, y, x + x1, y
     * + y1); } for (int i = 0; i < 70; i++) { int x = random.nextInt(width - 1); int y =
     * random.nextInt(height - 1); int x1 = random.nextInt(6) + 1; int y1 = random.nextInt(12) - 1;
     * g.drawLine(x, y, y - x1, y - y1); }
     */
    String sRand = "";
    for (int i = 0; i < 4; i++) {
      String tmp = getRandomChar();
      sRand += tmp;
      g.setColor(new Color(20 + random.nextInt(110), 200 + random.nextInt(49), 20 + random.nextInt(110)));
      g.drawString(tmp, 20 * i + 10, 25);
    }
    ApplicationContext ac = new ClassPathXmlApplicationContext("spring/applicationContext-oauthcache.xml");
    OauthCacheService oauthCacheService = (OauthCacheService) ac.getBean("oauthCacheService");
    String sessionId = this.findServletRequestSessionId(request);
    oauthCacheService.put(SecurityConstants.OAUTH_VALIDATE_CODE, 5 * 60, sessionId, sRand);
    g.dispose();
    ImageIO.write(image, "jpeg", response.getOutputStream());
  }

  @Override
  public void destroy() {
    // TODO Auto-generated method stub

  }

  @Override
  public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
      throws IOException, ServletException {

    HttpServletRequest request = (HttpServletRequest) arg0;
    HttpServletResponse response = (HttpServletResponse) arg1;
    service(request, response);

    return;
  }

  @Override
  public void init(FilterConfig arg0) throws ServletException {
    // TODO Auto-generated method stub

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
