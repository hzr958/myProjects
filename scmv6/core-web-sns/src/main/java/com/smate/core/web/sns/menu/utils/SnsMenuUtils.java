package com.smate.core.web.sns.menu.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smate.core.web.sns.menu.model.MenuItemBean;


/**
 * 科研之友菜单构成的工具生成类.
 * 
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
public class SnsMenuUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(SnsMenuUtils.class);
  // 一级菜单是否选中.
  private static boolean level1selected = false;
  // 二级菜单是否选中.
  private static boolean level2selected = false;
  // 三级菜单是否选中.
  private static boolean level3selected = false;

  /**
   * 根据MenuItemBean 构造(新版本)系统菜单.
   * 
   * @author MJG.
   * @param item 菜单元素
   * @param menuId 当前菜单编码
   * @param hostURL 主机域名
   * @return Map (key:menuStr,key:menuNav) menuStr:菜单标签 menuNav:底层导航条
   */
  public static String showSnsHTMLMenu(MenuItemBean item, int menuId, String hostURL, String scmDomain, Long insId) {
    StringBuffer htmlStr = new StringBuffer();// 最终返回字符串.
    StringBuffer subHtmlStr = new StringBuffer();// 二级菜单字符串拼装.
    Map<String, String> tempMap = new HashMap<String, String>();

    List<MenuItemBean> items = item.getMenuItems();// 一级菜单列表.
    if (items != null && items.size() > 0) {
      htmlStr.append("<div class=\"nav-t\">");
      htmlStr.append("<div class=\"nav_wrap-t\">");
      htmlStr.append("<ul>");
      // 一级菜单(Level1)
      for (int i = 0; i < items.size(); i++) {
        MenuItemBean level1Menu = items.get(i);

        if (MenuUtils.isExcludeMenu(insId, level1Menu.getMenuId())) {
          continue;
        }
        Long iNum = level1Menu.getMenuId().longValue();
        htmlStr.append("<li id=\"mm" + i + "\" class=\"top_temp_str_" + iNum + "_" + iNum + "\">");
        // 加载(一级菜单)单条菜单记录的链接字符串.
        String level1AContent = getSnsMenuAUrl(level1Menu, hostURL, 1, scmDomain, insId, 1, level1selected);
        htmlStr.append(level1AContent + level1Menu.getName() + "</a>");

        /**
         * 二级菜单
         */
        // 判断是否为当前选中菜单
        if (level1Menu.getMenuId() == menuId) {
          level1selected = true;
        }
        // 获取当前菜单的ID.
        Long level1MenuId = level1Menu.getMenuId();
        // 如果该一级菜单包含子菜单,则对子菜单(二级菜单)进行加载.
        List<MenuItemBean> level2Items = level1Menu.getMenuItems();
        if (CollectionUtils.isNotEmpty(level2Items)) {
          // 获取构建二级菜单.
          Integer level2Serial = i;
          htmlStr.append(buildSecondMenu(level2Items, insId, menuId, hostURL, scmDomain, level1MenuId, level2Serial));
        }
        // end
        htmlStr.append("</li>");

        // 根据选中状态替换字符"tempStr**"
        if (level1selected) {
          tempMap.put("top_temp_str_" + level1MenuId + "_" + level1MenuId, "cur");
          level1selected = false; // 状态复位
        } else {
          tempMap.put("top_temp_str_" + level1MenuId + "_" + level1MenuId, " ");
        }
      }
      htmlStr.append("</ul>");
      /*
       * htmlStr.append("<div class=\"top_search\"></div>");
       * htmlStr.append("<div class=\"clear\"></div>");
       */
      htmlStr.append("</div></div>");

      // htmlStr.append(subHtmlStr);
    } else {
      LOGGER.warn("install menu failure!menuId=" + menuId + ";hostURL=" + hostURL + ";scmDomain=" + scmDomain
          + ";insId+" + insId);
    }
    /**
     * 返回替换后的字符
     */
    return MenuUtils.replaceStringforFilter(htmlStr.toString(), tempMap);
  }

  /**
   * 构建二级菜单内容.
   * 
   * @param subHtmlStr
   * @param tempMap
   * @param level2Items
   * @param insId
   * @param menuId
   * @param hostURL
   * @param scmDomain
   * @param level1selected
   */
  private static String buildSecondMenu(List<MenuItemBean> level2Items, long insId, int menuId, String hostURL,
      String scmDomain, Long level1MenuId) {
    // 二级菜单字符串拼装.
    StringBuffer subHtmlStr = new StringBuffer();
    String thirdMenuHtmlStr = "";

    // Map<String, String> secondTempMap = new HashMap<String, String>();
    subHtmlStr.append("<div class=\"nav_erji\"    style=\"display:none;\"  > ");
    subHtmlStr.append("<dl>");
    // 如果父级菜单未选中，则不加载其子菜单_MJG_SCM-5869.
    // subHtmlStr.append("<span class=\"tempClas" + level1MenuId + "_" +
    // level1MenuId + "\" id=mb" + level1MenuId
    // + ">");
    // 二级菜单(Level2)
    for (int j = 0; j < level2Items.size(); j++) {

      MenuItemBean level2Item = level2Items.get(j);
      // 当前菜单是否被排除.
      if (MenuUtils.isExcludeMenu(insId, level2Item.getMenuId())) {
        continue;
      }

      // 点击科研之友应用菜单，子级菜单默认不高亮（特殊处理）
      boolean isNotMenuFundapp = true;
      // 如果点击选中的是父菜单(一级菜单)，且当前二级菜单值为其第一个子菜单(二级菜单)且不是科研之友应用菜单;或者当前选中菜单。则默认显示该二级菜单的内容.
      boolean isAutoSelectedFlag = (j == 0 && level1selected && isNotMenuFundapp);
      if (isAutoSelectedFlag || (level2Item.getMenuId() == menuId)) {
        level2selected = true;
      }
      subHtmlStr.append("<dd>");
      /* subHtmlStr.append("<span>|</span>"); */
      // 加载单条菜单记录的链接字符串.
      String level2AContent = getSnsMenuAUrl(level2Item, hostURL, 2, scmDomain, insId, 2, level2selected);
      subHtmlStr.append(level2AContent + level2Item.getName() + "</a>");
      subHtmlStr.append("</dd>");

      /**
       * 目前没有三级菜单，
       */
      // 判断是否有下级菜单(三级菜单)(true-有；false-没有)
      boolean hasChildrens = (level2Item.getMenuItems() != null && level2Item.getMenuItems().size() > 0);
      if (hasChildrens) {
        thirdMenuHtmlStr = buildThirdMenu(insId, menuId, hostURL, scmDomain, level2Item.getMenuItems());
      }

      // 如下级菜单被选中则上级菜单也处于选中状态
      if (level3selected) {
        level2selected = true;
        level1selected = true;
        level3selected = false; // 状态复位
      } else if (level2selected) {
        level1selected = true;
      }
      // 如果父级菜单未选中，则不加载其子菜单_MJG_SCM-5869.
      /*
       * // 如果一级菜单选中，则显示下属二级菜单；否则隐藏. if (level1selected) { secondTempMap.put("tempClas" + level1MenuId +
       * "_" + level1MenuId, ""); } else { secondTempMap.put("tempClas" + level1MenuId + "_" +
       * level1MenuId, "hide"); }
       */
      // 状态复位.
      if (level2selected) {
        level2selected = false; // 状态复位
      }
    }
    subHtmlStr.append("</dl>");
    subHtmlStr.append("</div>");
    if (StringUtils.isNotBlank(thirdMenuHtmlStr)) {
      subHtmlStr.append(thirdMenuHtmlStr);
    }
    // 如果父级菜单未选中，则不加载其子菜单_MJG_SCM-5869.
    /*
     * //返回替换后的字符. return MenuUtils.replaceStringforFilter(subHtmlStr.toString(), secondTempMap);
     */

    /*
     * if (level1selected) { return subHtmlStr.toString(); }
     */
    // 子菜单 全部显示
    return subHtmlStr.toString();
  }

  /**
   * 构建二级菜单内容.
   * 
   * @param subHtmlStr
   * @param tempMap
   * @param level2Items
   * @param insId
   * @param menuId
   * @param hostURL
   * @param scmDomain
   * @param level1selected
   * @param level2Serial //第一个二级菜单
   */
  private static String buildSecondMenu(List<MenuItemBean> level2Items, long insId, int menuId, String hostURL,
      String scmDomain, Long level1MenuId, Integer level2Serial) {
    // 二级菜单字符串拼装.
    StringBuffer subHtmlStr = new StringBuffer();
    String thirdMenuHtmlStr = "";

    // Map<String, String> secondTempMap = new HashMap<String, String>();
    String navId = "nav_erji_" + level2Serial;
    subHtmlStr.append("<div class=\"nav_erji\"  id= " + navId + "  style=\"display:none; z-index:1999; \"  > ");
    subHtmlStr.append("<dl>");
    // 如果父级菜单未选中，则不加载其子菜单_MJG_SCM-5869.
    // subHtmlStr.append("<span class=\"tempClas" + level1MenuId + "_" +
    // level1MenuId + "\" id=mb" + level1MenuId
    // + ">");
    // 二级菜单(Level2)
    for (int j = 0; j < level2Items.size(); j++) {

      MenuItemBean level2Item = level2Items.get(j);
      // 当前菜单是否被排除.
      if (MenuUtils.isExcludeMenu(insId, level2Item.getMenuId())) {
        continue;
      }

      // 点击科研之友应用菜单，子级菜单默认不高亮（特殊处理）
      boolean isNotMenuFundapp = true;
      // 如果点击选中的是父菜单(一级菜单)，且当前二级菜单值为其第一个子菜单(二级菜单)且不是科研之友应用菜单;或者当前选中菜单。则默认显示该二级菜单的内容.
      boolean isAutoSelectedFlag = (j == 0 && level1selected && isNotMenuFundapp);
      if (isAutoSelectedFlag || (level2Item.getMenuId() == menuId)) {
        level2selected = true;
      }
      subHtmlStr.append("<dd>");
      /* subHtmlStr.append("<span>|</span>"); */
      // 加载单条菜单记录的链接字符串.
      String level2AContent = getSnsMenuAUrl(level2Item, hostURL, 2, scmDomain, insId, 2, level2selected);
      subHtmlStr.append(level2AContent + level2Item.getName() + "</a>");
      subHtmlStr.append("</dd>");

      /**
       * 目前没有三级菜单，
       */
      // 判断是否有下级菜单(三级菜单)(true-有；false-没有)
      boolean hasChildrens = (level2Item.getMenuItems() != null && level2Item.getMenuItems().size() > 0);
      if (hasChildrens) {
        thirdMenuHtmlStr = buildThirdMenu(insId, menuId, hostURL, scmDomain, level2Item.getMenuItems());
      }

      // 如下级菜单被选中则上级菜单也处于选中状态
      if (level3selected) {
        level2selected = true;
        level1selected = true;
        level3selected = false; // 状态复位
      } else if (level2selected) {
        level1selected = true;
      }
      // 如果父级菜单未选中，则不加载其子菜单_MJG_SCM-5869.
      /*
       * // 如果一级菜单选中，则显示下属二级菜单；否则隐藏. if (level1selected) { secondTempMap.put("tempClas" + level1MenuId +
       * "_" + level1MenuId, ""); } else { secondTempMap.put("tempClas" + level1MenuId + "_" +
       * level1MenuId, "hide"); }
       */
      // 状态复位.
      if (level2selected) {
        level2selected = false; // 状态复位
      }
    }
    subHtmlStr.append("</dl>");
    subHtmlStr.append("</div>");
    if (StringUtils.isNotBlank(thirdMenuHtmlStr)) {
      subHtmlStr.append(thirdMenuHtmlStr);
    }
    // 如果父级菜单未选中，则不加载其子菜单_MJG_SCM-5869.
    /*
     * //返回替换后的字符. return MenuUtils.replaceStringforFilter(subHtmlStr.toString(), secondTempMap);
     */

    /*
     * if (level1selected) { return subHtmlStr.toString(); }
     */
    // 子菜单 全部显示
    return subHtmlStr.toString();
  }

  /**
   * 构建三级菜单结构.
   * 
   * @param insId
   * @param menuId
   * @param hostURL
   * @param scmDomain
   * @param level3Items
   * @return
   */
  private static String buildThirdMenu(long insId, int menuId, String hostURL, String scmDomain,
      List<MenuItemBean> level3Items) {
    // 二级菜单字符串拼装.
    StringBuffer thirdHtmlStr = new StringBuffer();
    thirdHtmlStr.append("<ul>");
    /*
     * 三级菜单(Level3)
     */
    for (int z = 0; z < level3Items.size(); z++) {
      MenuItemBean level3Item = level3Items.get(z);

      if (MenuUtils.isExcludeMenu(insId, level3Item.getMenuId())) {
        continue;

      }
      // 判断是否为当前选中菜单
      if (level3Item.getMenuId() == menuId) {
        level3selected = true;
      }
      // 加载单条菜单记录的链接字符串.
      String level3AContent = getSnsMenuAUrl(level3Item, hostURL, 3, scmDomain, insId, 3, level3selected);
      thirdHtmlStr.append("<li>");
      thirdHtmlStr.append(level3AContent + level3Item.getName() + "</a>");
      thirdHtmlStr.append("</li>");
    }
    thirdHtmlStr.append("</ul>");
    return thirdHtmlStr.toString();
  }

  /**
   * 
   * 根据MenuItemBean URL 构造菜单 < a >起示标签.
   * 
   * @param menu
   * @param hostURL
   * @param jumpHost 需要跳转到的主机地址
   * @return linkTagStr
   */
  private static String getSnsMenuAUrl(MenuItemBean menu, String hostURL, int level, String scmDomain, long insId,
      int menuLevel, Boolean isMenuSelected) {

    // 新版本菜单样式中一级菜单的 a 链接标签的其他属性.
    String aContent = "";
    // 二级菜单且当前菜单被选中.
    if (isMenuSelected && menuLevel == 2) {
      // 2016-10-08 菜单改造
      // aContent += " id=two_nav ";
    }

    hostURL = MenuUtils.wrapperHostUrl(hostURL);
    scmDomain = MenuUtils.wrapperHostUrl(scmDomain);

    String linkTagStr = null;
    // 判断地址是跳转到外部站点
    if (MenuUtils.isOutUrl(menu.getValue())) {
      String outUrl = menu.getValue();
      linkTagStr = "<a " + aContent + " href=\"" + outUrl + "\"  " + MenuUtils.appendTarget(menu.getTarget()) + ">";
    } else if (MenuUtils.isScmUrl(menu.getValue())) {

      linkTagStr = "<a " + aContent + " href=\"" + MenuUtils.getScmURL(menu.getValue(), scmDomain, insId) + "\" "
          + MenuUtils.appendTarget(menu.getTarget()) + ">";

    } else {
      String localURL = menu.getValue();

      if (StringUtils.isEmpty(localURL)) { // 是否为空
        String firstChildURL = MenuUtils.getFirstChildURL(menu, insId);
        if (MenuUtils.isOutUrl(firstChildURL)) {
          linkTagStr = "<a " + aContent + " href=\"" + firstChildURL + ">";
        } else {
          String childLocalURl = "";

          childLocalURl = firstChildURL;
          linkTagStr =
              "<a " + aContent + " href=\"" + childLocalURl + "\" " + MenuUtils.appendTarget(menu.getTarget()) + ">";
        }
      } else {

        if (menu.getMenuId().longValue() == 1 && (insId == 2565 || insId == 2566)) {
          linkTagStr = "<a " + aContent + " href=\"" + hostURL + "/scmwebsns/rolMain" + "\" "
              + MenuUtils.appendTarget(menu.getTarget()) + ">";
        } else {
          linkTagStr = "<a " + aContent + " href=\"" + hostURL + localURL + "\" "
              + MenuUtils.appendTarget(menu.getTarget()) + ">";
        }

      }
    }
    return linkTagStr;
  }
}
