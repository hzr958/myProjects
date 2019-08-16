package com.smate.core.web.sns.menu.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;

import com.smate.core.web.sns.consts.ExcludeMenuConst;
import com.smate.core.web.sns.menu.model.MenuItemBean;


/**
 * 
 * 菜单工具类（用于处理生成菜单，导航条）.
 * 
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
public class MenuUtils {

  // ~ Static fields/initializers
  // ========================================================
  /**
   * 
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(MenuUtils.class);
  /**
     * 
     */
  private static final String HTTP_MARK = "http://";

  /**
   * 
   */
  public MenuUtils() {

  }

  /**
   * 
   * 根据MenuItemBean 构造系统菜单.
   * 
   * @param item 菜单元素
   * @param menuId 当前菜单编码
   * @param hostURL 主机域名
   * @return Map (key:menuStr,key:menuNav) menuStr:菜单标签 menuNav:底层导航条
   */
  public static String showHTMLMenu(MenuItemBean item, int menuId, String hostURL, String scmDomain, long insId) {

    final String MENU_VERSION = "old";// 菜单版本(调用getMenuLinkStartTagForURL方法时用到此参数)_MJG_2012-08-11

    StringBuffer htmlStr = new StringBuffer();
    List<MenuItemBean> items = item.getMenuItems();
    Map<String, String> tempMap = new HashMap<String, String>();

    if (items != null && items.size() > 0) {
      // <ul id="main_nav">
      htmlStr.append("<ul id=\"main_nav\">");

      // 菜单是否选中
      boolean level1selected = false;
      // 一级菜单(Level1)
      for (int i = 0; i < items.size(); i++) {
        MenuItemBean level1Menu = items.get(i);

        if ((insId == 2565 || insId == 2566) && ExcludeMenuConst.nsfcExcludeMenuIds.contains(level1Menu.getMenuId())) {

          continue;
        }

        // 判断是否为当前选中菜单
        if (level1Menu.getMenuId() == menuId) {
          level1selected = true;
        }
        // <a href="#">首页</a>
        htmlStr.append("<li id=\"navItem" + i + "\" tempStr1_" + i + ">");
        // lqh add一级菜单可自由伸缩
        htmlStr.append("<div class=\"nav_btn_left_" + i + "\"></div><div class=\"nav_btn_bg_" + i + "\">");
        htmlStr.append(getMenuLinkStartTagForURL(level1Menu, hostURL, 1, scmDomain, insId, false, MENU_VERSION)
            + "&nbsp;" + level1Menu.getName() + "</a>");

        if (level1Menu.getMenuItems() != null && level1Menu.getMenuItems().size() > 0) {
          List<MenuItemBean> level2Items = level1Menu.getMenuItems();
          htmlStr.append("<ul>");
          // 菜单是否选中
          boolean level2selected = false;
          // 二级菜单(Level2)
          for (int j = 0; j < level2Items.size(); j++) {
            MenuItemBean level2Item = level2Items.get(j);

            if ((insId == 2565 || insId == 2566)
                && ExcludeMenuConst.nsfcExcludeMenuIds.contains(level2Item.getMenuId())) {

              continue;
            }

            if ((insId != 2565 && insId != 2566)
                && ExcludeMenuConst.scmExcludeMenuIds.contains(level2Item.getMenuId())) {

              continue;

            }
            // 判断是否为当前选中菜单
            if (level2Item.getMenuId() == menuId) {
              level2selected = true;
            }
            // 判断是否有下级菜单
            boolean hasChildrens = (level2Item.getMenuItems() != null && level2Item.getMenuItems().size() > 0);
            // 当选中一级菜单时默认第一个子菜单选中 <li class="highlight"><a
            // href="#">首页</a></li>
            // <li id="navItem2_SubNavItem1"
            // onmouseover="navShowLevel3Section(this.id)"
            // onmouseout="navClearLevel3Nav(this.id)">
            boolean isNotMenuFundapp = true;
            if (i == 3) {// 点击科研之友应用菜单，子级菜单默认不高亮（特殊处理）
              isNotMenuFundapp = !("/scmwebsns/fundapp/main".equalsIgnoreCase(level1Menu.getValue()) && menuId == 4);
            }
            if (j == 0 && level1selected && isNotMenuFundapp) {
              if (hasChildrens) {
                htmlStr.append("<li class=\"highlight\" id=\"navItem" + i + "_SubNavItem" + j
                    + "\" onmouseover=\"navShowLevel3Section(this.id)\" onmouseout=\"navClearLevel3Nav(this.id)\">");
              } else {
                htmlStr.append("<li class=\"highlight\">");
              }
            } else {
              if (hasChildrens) {
                htmlStr.append("<li id=\"navItem" + i + "_SubNavItem" + j
                    + "\" onmouseover=\"navShowLevel3Section(this.id)\" onmouseout=\"navClearLevel3Nav(this.id)\" tempStr2_"
                    + i + "_" + j + ">");
              } else {
                htmlStr.append("<li  tempStr2_" + i + "_" + j + ">");
              }
            }
            htmlStr
                .append(getMenuLinkStartTagForURL(level2Item, hostURL, 2, scmDomain, insId, hasChildrens, MENU_VERSION)
                    + level2Item.getName() + "</a>");
            // 菜单是否选中
            boolean level3selected = false;
            if (hasChildrens) {
              List<MenuItemBean> level3Items = level2Item.getMenuItems();
              htmlStr.append("<ul id=\"navItem" + i + "_SubNavItem" + j + "_SubNav\" style=\"display: none\">");
              /*
               * 三级菜单(Level3) <ul id="navItem2_SubNavItem1_SubNav" style="display: none"> <li
               * onmouseover="navL3ItemHover(this)" onmouseout="navL3ItemOut(this)"><a href="#">三级菜单1</a></li> <li
               * onmouseover="navL3ItemHover(this)" onmouseout="navL3ItemOut(this)"><a href="#">三级菜单2</a></li>
               * </ul>
               */
              for (int z = 0; z < level3Items.size(); z++) {
                MenuItemBean level3Item = level3Items.get(z);

                if ((insId == 2565 || insId == 2566)
                    && ExcludeMenuConst.nsfcExcludeMenuIds.contains(level3Item.getMenuId())) {

                  continue;
                }

                if ((insId != 2565 && insId != 2566)
                    && ExcludeMenuConst.scmExcludeMenuIds.contains(level3Item.getMenuId())) {

                  continue;

                }
                if (level3Item.getMenuId() == menuId) {
                  level3selected = true;
                }
                htmlStr.append("<li onmouseover=\"navL3ItemHover(this)\" onmouseout=\"navL3ItemOut(this)\">"
                    + getMenuLinkStartTagForURL(level3Item, hostURL, 3, scmDomain, insId, false, MENU_VERSION)
                    + level3Item.getName() + "</a></li>");
              }
              htmlStr.append("</ul>");
            }

            // 如下级菜单被选中则上级菜单也处于选中状态
            if (level3selected) {
              level2selected = true;
              level1selected = true;
              level3selected = false; // 状态复位
            } else if (level2selected) {
              level1selected = true;
            }

            /**
             * 根据选中状态位,为填充字符匹配替换模式 把填充字符和对应的替换匹配模式保存Map<String,String>中;
             */
            if (level2selected) {
              tempMap.put("tempStr2_" + i + "_" + j, " class=\"highlight\" ");
              level2selected = false; // 状态复位
            } else {
              tempMap.put("tempStr2_" + i + "_" + j, " ");
            }
            htmlStr.append("</li>");
          }
          htmlStr.append("</ul>");
        }

        // 根据选中状态替换字符"tempStr**"
        if (level1selected) {
          tempMap.put("tempStr1_" + i, " class=\"highlight\" ");
          // lqh add一级菜单可自由伸缩
          tempMap.put("nav_btn_left_" + i, "nav_bluebtn_left");
          tempMap.put("nav_btn_bg_" + i, "nav_bluebtn_bg");
          tempMap.put("nav_btn_right_" + i, "nav_bluebtn_right");
          level1selected = false; // 状态复位
        } else {
          tempMap.put("tempStr1_" + i, " ");
          // lqh add一级菜单可自由伸缩
          tempMap.put("nav_btn_left_" + i, "nav_graybtn_left");
          tempMap.put("nav_btn_bg_" + i, "nav_graybtn_bg");
          tempMap.put("nav_btn_right_" + i, "nav_graybtn_right");
        }
        // lqh add一级菜单可自由伸缩
        htmlStr.append("</div><div class=\"nav_btn_right_" + i + "\"></div>");

        htmlStr.append("</li>");
      }
      htmlStr.append("</ul>");
    } else {
      LOGGER.warn("install menu failure!menuId=" + menuId + ";hostURL=" + hostURL + ";scmDomain=" + scmDomain
          + ";insId+" + insId);
    }
    /**
     * 返回替换后的字符
     */
    return replaceStringforFilter(htmlStr.toString(), tempMap);
  }

  /**
   * 根据MenuItemBean 构造(新版本)系统菜单.
   * 
   * @author MJG.
   * @param item 菜单元素
   * @param menuId 当前菜单编码
   * @param hostURL 主机域名
   * @return Map (key:menuStr,key:menuNav) menuStr:菜单标签 menuNav:底层导航条
   */
  public static String showNewHTMLMenu(MenuItemBean item, int menuId, String hostURL, String scmDomain, long insId) {
    StringBuffer htmlStr = new StringBuffer();// 最终返回字符串.
    StringBuffer subHtmlStr = new StringBuffer();// 二级菜单字符串拼装.

    final String MENU_VERSION = "new";// 菜单版本(调用getMenuLinkStartTagForURL方法时用到此参数)_MJG_2012-08-11

    List<MenuItemBean> items = item.getMenuItems();// 一级菜单列表.
    // 获取当前语言版本.
    String language = LocaleContextHolder.getLocale().toString();
    if (language == null) {
      language = "zh_CN";
    }
    Map<String, String> tempMap = new HashMap<String, String>();
    if (items != null && items.size() > 0) {
      // htmlStr.append("<div id=mainmenu_body>");
      htmlStr.append("<div id=\"mainmenu_top\"><ul>");
      subHtmlStr.append("<div id=mainmenu_bottom><div class=\"mainmenu_rbg\">");
      // 菜单是否选中
      boolean level1selected = false;
      // 一级菜单(Level1)
      for (int i = 0; i < items.size(); i++) {
        MenuItemBean level1Menu = items.get(i);

        if ((insId == 2565 || insId == 2566) && ExcludeMenuConst.nsfcExcludeMenuIds.contains(level1Menu.getMenuId())) {
          continue;
        }
        if ((insId != 2565 && insId != 2566) && ExcludeMenuConst.scmExcludeMenuIds.contains(level1Menu.getMenuId())) {
          continue;
        }
        htmlStr.append("<li>");
        // 判断是否为当前选中菜单
        if (level1Menu.getMenuId() == menuId) {
          level1selected = true;
        }
        // 获取当前菜单的ID.
        Long level1MenuId = level1Menu.getMenuId();
        // String spanInfo = level1selected ?
        // "<span id=\"nav01-hover\">" : "<span>";
        String spanInfo = level1selected ? "<span>" : "<span>";
        // 加载(一级菜单)单条菜单记录的链接字符串.
        String level1AContent =
            getMenuLinkStartTagForURL(level1Menu, hostURL, 1, scmDomain, insId, false, MENU_VERSION);
        htmlStr.append(level1AContent + "&nbsp;" + spanInfo + level1Menu.getName() + "</span>" + "</a>");
        // 如果该一级菜单包含子菜单,则对子菜单(二级菜单)进行加载.
        if (level1Menu.getMenuItems() != null && level1Menu.getMenuItems().size() > 0) {
          // subHtmlStr.append("<ul class=hide id=mb1>");
          subHtmlStr
              .append("<ul class=\"tempClas" + level1MenuId + "_" + level1MenuId + "\" id=mb" + level1MenuId + ">");
          // 获取二级菜单列表.
          List<MenuItemBean> level2Items = level1Menu.getMenuItems();

          boolean level2selected = false;// 菜单是否选中
          // 二级菜单(Level2)
          for (int j = 0; j < level2Items.size(); j++) {

            MenuItemBean level2Item = level2Items.get(j);

            if ((insId == 2565 || insId == 2566)
                && ExcludeMenuConst.nsfcExcludeMenuIds.contains(level2Item.getMenuId())) {
              continue;
            }
            if ((insId != 2565 && insId != 2566)
                && ExcludeMenuConst.scmExcludeMenuIds.contains(level2Item.getMenuId())) {
              continue;
            }

            // 点击科研之友应用菜单，子级菜单默认不高亮（特殊处理）
            boolean isNotMenuFundapp = true;
            // if (i == 3) {
            // isNotMenuFundapp =
            // !("/scmwebsns/fundapp/main".equalsIgnoreCase(level1Menu.getValue())
            // &&
            // menuId == 4);
            // }
            // 如果点击选中的是父菜单(一级菜单)，且当前二级菜单值为其第一个子菜单(二级菜单)且不是科研之友应用菜单，则默认显示该二级菜单的内容.
            if (j == 0 && level1selected && isNotMenuFundapp) {
              level2selected = true;
            }
            subHtmlStr.append("<li>");

            // 判断是否为当前选中菜单
            if (level2Item.getMenuId() == menuId) {
              level2selected = true;
            }

            Long level2MenuId = level2Item.getMenuId();// 获取当前菜单ID.
            // 判断是否有下级菜单(三级菜单)(true-有；false-没有)
            boolean hasChildrens = (level2Item.getMenuItems() != null && level2Item.getMenuItems().size() > 0);
            // 加载单条菜单记录的链接字符串.
            String level2AContent =
                getMenuLinkStartTagForURL(level2Item, hostURL, 2, scmDomain, insId, hasChildrens, MENU_VERSION);
            String level2Span = hasChildrens ? "<span class=\"arrow_more_gray\">" + level2Item.getName() + "</span>"
                : level2Item.getName();
            subHtmlStr.append(level2AContent + level2Span + "</a>");

            // 菜单是否选中
            boolean level3selected = false;
            // 如果有三级菜单，则对三级菜单进行加载.
            if (hasChildrens) {
              List<MenuItemBean> level3Items = level2Item.getMenuItems();
              subHtmlStr.append("<ul class=\"nav_third nav_third_width_" + language + "\">");
              /*
               * 三级菜单(Level3)
               */
              for (int z = 0; z < level3Items.size(); z++) {
                MenuItemBean level3Item = level3Items.get(z);

                if ((insId == 2565 || insId == 2566)
                    && ExcludeMenuConst.nsfcExcludeMenuIds.contains(level3Item.getMenuId())) {

                  continue;
                }

                if ((insId != 2565 && insId != 2566)
                    && ExcludeMenuConst.scmExcludeMenuIds.contains(level3Item.getMenuId())) {

                  continue;

                }
                // 判断是否为当前选中菜单
                if (level3Item.getMenuId() == menuId) {
                  level3selected = true;
                }
                // 加载单条菜单记录的链接字符串.
                String level3AContent =
                    getMenuLinkStartTagForURL(level3Item, hostURL, 3, scmDomain, insId, false, MENU_VERSION);
                subHtmlStr.append("<li>" + level3AContent + level3Item.getName() + "</a></li>");
              }
              subHtmlStr.append("</ul>");
            }

            // 如下级菜单被选中则上级菜单也处于选中状态
            if (level3selected) {
              level2selected = true;
              level1selected = true;
              level3selected = false; // 状态复位
            } else if (level2selected) {
              level1selected = true;
            }
            /**
             * 根据选中状态位,为填充字符匹配替换模式 把填充字符和对应的替换匹配模式保存Map<String,String>中;
             */
            if (level2selected) {
              tempMap.put("tempStr2" + level2MenuId + "_" + level2MenuId, "highlight");
              level2selected = false; // 状态复位
            } else {
              tempMap.put("tempStr2" + level2MenuId + "_" + level2MenuId, " ");
            }
            subHtmlStr.append("</li>");
          }
          subHtmlStr.append("</ul>");
        }
        // 根据选中状态替换字符"tempStr**"
        if (level1selected) {
          tempMap.put("tempStr1" + level1MenuId + "_" + level1MenuId, "menuhover");
          tempMap.put("tempClas" + level1MenuId + "_" + level1MenuId, "");
          level1selected = false; // 状态复位
        } else {
          tempMap.put("tempStr1" + level1MenuId + "_" + level1MenuId, " ");
          tempMap.put("tempClas" + level1MenuId + "_" + level1MenuId, "hide");
        }
        htmlStr.append("</li>");
      }
      htmlStr.append("</ul></div>");

      subHtmlStr.append("</div><div class=\"Online-Service\"><input name=\"\" class=\"onlinehelp_" + language
          + "\" type=\"button\" /></div></div>");
      htmlStr.append(subHtmlStr);
      // htmlStr.append("</div>");
    } else {
      LOGGER.warn("install menu failure!menuId=" + menuId + ";hostURL=" + hostURL + ";scmDomain=" + scmDomain
          + ";insId+" + insId);
    }
    /**
     * 返回替换后的字符
     */
    return replaceStringforFilter(htmlStr.toString(), tempMap);
  }

  /**
   * 检查菜单是否已被过滤.
   * 
   * @param menuId
   * @return
   */
  public static Boolean isExcludeMenu(Long insId, Long menuId) {
    if ((insId == 2565 || insId == 2566) && ExcludeMenuConst.nsfcExcludeMenuIds.contains(menuId)) {
      return true;
    }
    if ((insId != 2565 && insId != 2566) && ExcludeMenuConst.scmExcludeMenuIds.contains(menuId)) {
      return true;
    }
    return false;
  }

  /**
   * 
   * 根据MenuItemBean构造导航条.
   * 
   * @param item 需构造的MenuItemBean
   * @param menuId当前选中MenuItemBean编号
   * @param hostURL 主机域名
   * @return
   */
  public static String showHTMLNav(MenuItemBean item, int menuId, String hostURL, String scmDomain, long insId) {
    StringBuffer htmlStr = new StringBuffer();
    List<MenuItemBean> items = item.getMenuItems();

    if (items != null && items.size() > 0) {
      // 菜单是否选中
      boolean level1selected = false;
      // 一菜单
      for (int i = 0; i < items.size(); i++) {
        MenuItemBean level1Menu = items.get(i);
        level1selected = (level1Menu.getMenuId() == menuId);
        List<MenuItemBean> level2Items = level1Menu.getMenuItems();
        if (level2Items != null && level2Items.size() > 0) {
          // 判断是否选中一级菜单如果选中则默认选择下级菜单的第一项作为导航条
          // <div class="nav4_div_hover"><ul><li class="left"></li><li
          // class="bg">我的活动列表</li><li
          // class="right"></li></ul></div>
          if (level1selected) {
            MenuItemBean level2Item = level2Items.get(0);
            if (level2Item.getMenuItems() != null && level2Item.getMenuItems().size() > 0) {
              List<MenuItemBean> level3Items = level2Item.getMenuItems();
              for (int z = 0; z < level3Items.size(); z++) {
                MenuItemBean level3Item = level3Items.get(z);
                String linkURL = getlinkNavStartTagForURL(level3Item, hostURL, scmDomain, insId, "old", false);

                if (z == 0) {
                  htmlStr.append("<div class=\"nav4_div_hover\">");
                } else {
                  htmlStr.append("<div class=\"nav4_div_on\">");
                }

                htmlStr.append("<ul><li>" + linkURL + "<B class=\"left\"></B><B class=\"middle\">"
                    + level3Item.getName() + "</B><B class=\"right\" ></B></a></li></ul></div>");
              }
            }
            // 跳出循环
            break;
          } else {
            // 二级菜单
            for (int j = 0; j < level2Items.size(); j++) {
              MenuItemBean level2Item = level2Items.get(j);
              // 新成果系统，成果菜单需要加上switchInsId参数
              if (level2Item.getMenuId() == 1300 && level2Item.getValue() != null
                  && !level2Item.getValue().contains("?switchInsId=")) {
                level2Item.setValue(level2Item.getValue() + "?switchInsId=" + insId);
              }
              // 菜单是否选中
              if (level2Item.getMenuId() == menuId) {
                if (level2Item.getMenuItems() != null && level2Item.getMenuItems().size() > 0) {
                  List<MenuItemBean> level3Items = level2Item.getMenuItems();
                  // 三级菜单
                  for (int z = 0; z < level3Items.size(); z++) {
                    MenuItemBean level3Item = level3Items.get(z);
                    String linkURL = getlinkNavStartTagForURL(level3Item, hostURL, scmDomain, insId, "old", false);
                    if (z == 0) {
                      htmlStr.append("<div class=\"nav4_div_hover\">");
                    } else {
                      htmlStr.append("<div class=\"nav4_div_on\">");
                    }
                    htmlStr.append("<ul><li>" + linkURL + "<B class=\"left\"></B><B class=\"middle\">"
                        + level3Item.getName() + "</B><B class=\"right\"></B></a></li></ul></div>");
                  }
                }
              } else {
                // 菜单是否选中
                boolean level3selected = false;
                List<MenuItemBean> level3Items = level2Item.getMenuItems();
                if (level2Item.getMenuItems() != null && level2Item.getMenuItems().size() > 0) {
                  // 三级菜单
                  for (int z = 0; z < level3Items.size(); z++) {
                    MenuItemBean level3Item = level3Items.get(z);
                    // 若存在匹配的菜单,选中状态为真,跳出当前循环
                    if (level3Item.getMenuId() == menuId) {
                      level3selected = true;
                      break;
                    }
                  }
                }
                if (level3selected) {
                  // 三级菜单
                  for (int z = 0; z < level3Items.size(); z++) {
                    MenuItemBean level3Item = level3Items.get(z);
                    String linkURL = getlinkNavStartTagForURL(level3Item, hostURL, scmDomain, insId, "old", false);

                    if (level3Item.getMenuId() == menuId) {
                      htmlStr.append("<div class=\"nav4_div_hover\">");
                    } else {
                      htmlStr.append("<div class=\"nav4_div_on\">");
                    }
                    htmlStr.append("<ul><li>" + linkURL + "<B class=\"left\" ></B><B class=\"middle\">"
                        + level3Item.getName() + "</B><B class=\"right\" ></B></a></li></ul></div>");
                  }
                }
              }
            }
          }
        }
      }
    } else {
      LOGGER.debug("install Nav failure!");
    }

    if (StringUtils.isNotBlank(htmlStr.toString())) {
      htmlStr.insert(0, "<div class=\"box_nav3tab\" >");
      htmlStr.append("</div>");
    }
    return htmlStr.toString();
  }

  /**
   * 
   * 根据MenuItemBean构造导航条.
   * 
   * @param item 需构造的MenuItemBean
   * @param menuId当前选中MenuItemBean编号
   * @param hostURL 主机域名
   * @return
   */
  public static String showNewHTMLNav(MenuItemBean item, int menuId, String hostURL, String scmDomain, long insId) {
    StringBuffer htmlStr = new StringBuffer();
    List<MenuItemBean> items = item.getMenuItems();

    if (items != null && items.size() > 0) {
      // 菜单是否选中
      boolean level1selected = false;
      // 一菜单
      for (int i = 0; i < items.size(); i++) {
        MenuItemBean level1Menu = items.get(i);
        level1selected = (level1Menu.getMenuId() == menuId);
        List<MenuItemBean> level2Items = level1Menu.getMenuItems();// 获取一级菜单下的二级菜单列表.
        if (level2Items != null && level2Items.size() > 0) {
          // 判断是否选中一级菜单如果选中则默认选择下级菜单的第一项作为导航条.
          if (level1selected) {
            MenuItemBean level2Item = level2Items.get(0);
            if (level2Item.getMenuItems() != null && level2Item.getMenuItems().size() > 0) {
              List<MenuItemBean> level3Items = level2Item.getMenuItems();// 获取二级菜单下的三级菜单列表.
              htmlStr.append("<div class=\"nav-three\">");
              htmlStr.append("<ul>");
              for (int z = 0; z < level3Items.size(); z++) {
                MenuItemBean level3Item = level3Items.get(z);
                boolean isBold = (z == 0);// 是否突出显示.
                String linkURL = getlinkNavStartTagForURL(level3Item, hostURL, scmDomain, insId, "new", isBold);
                htmlStr.append("<li>" + linkURL + level3Item.getName() + "</a></li>");
              }
              htmlStr.append("</ul></div>");
            }
            // 跳出循环
            break;
          }
          // 一级菜单未选中.
          else {
            // 二级菜单
            for (int j = 0; j < level2Items.size(); j++) {
              MenuItemBean level2Item = level2Items.get(j);
              // 新成果系统，成果菜单需要加上switchInsId参数
              if (level2Item.getMenuId() == 1300 && level2Item.getValue() != null
                  && !level2Item.getValue().contains("?switchInsId=")) {
                level2Item.setValue(level2Item.getValue() + "?switchInsId=" + insId);
              }
              // 当前二级菜单被选中.
              if (level2Item.getMenuId() == menuId) {
                if (level2Item.getMenuItems() != null && level2Item.getMenuItems().size() > 0) {
                  List<MenuItemBean> level3Items = level2Item.getMenuItems();// 获取二级菜单下的三级菜单列表.
                  // 三级菜单
                  htmlStr.append("<div class=\"nav-three\">");
                  htmlStr.append("<ul>");
                  for (int z = 0; z < level3Items.size(); z++) {
                    MenuItemBean level3Item = level3Items.get(z);
                    boolean isBold = (z == 0);// 是否突出显示.
                    String linkURL = getlinkNavStartTagForURL(level3Item, hostURL, scmDomain, insId, "new", isBold);
                    htmlStr.append("<li>" + linkURL + level3Item.getName() + "</a></li>");
                  }
                  htmlStr.append("</ul></div>");
                }
              }
              // 当前二级菜单未被选中.
              else {
                // 当前三级菜单是否选中.
                boolean level3selected = false;
                List<MenuItemBean> level3Items = level2Item.getMenuItems();// 获取二级菜单下的三级菜单列表.
                if (level3Items != null && level3Items.size() > 0) {
                  // 三级菜单
                  for (int z = 0; z < level3Items.size(); z++) {
                    MenuItemBean level3Item = level3Items.get(z);
                    // 若存在匹配的菜单,选中状态为真,跳出当前循环
                    if (level3Item.getMenuId() == menuId) {
                      level3selected = true;
                      break;
                    }
                  }
                  if (level3selected) {
                    // 三级菜单
                    htmlStr.append("<div class=\"nav-three\">");
                    htmlStr.append("<ul>");
                    for (int z = 0; z < level3Items.size(); z++) {
                      MenuItemBean level3Item = level3Items.get(z);
                      boolean isBold = (level3Item.getMenuId() == menuId);// 是否突出显示.
                      String linkURL = getlinkNavStartTagForURL(level3Item, hostURL, scmDomain, insId, "new", isBold);
                      htmlStr.append("<li>" + linkURL + level3Item.getName() + "</a></li>");
                    }
                    htmlStr.append("</ul></div>");
                  }
                }
              }
            }
          }
        }
      }
    } else {
      LOGGER.debug("install Nav failure!");
    }
    return htmlStr.toString();
  }

  /**
   * 
   * 根据MenuItemBean的URL构造导航条的A起示标签.
   * 
   * @param menu MenuItemBean 元素.
   * @param hostURL 域名.
   * @return .
   * 
   */
  /**
   * 增加了以下两个参数，以兼容v5版本的菜单导航条样式.
   * 
   * @author MJG.
   * @since 2012-08-01
   * @param menuType 菜单样式类型(new-新版本(v5)；old-旧版本)
   * @param isDisabled v5版本的菜单导航条标签是否突出显示.
   * @return
   */
  public static String getlinkNavStartTagForURL(MenuItemBean menu, String hostURL, String scmDomain, long insId,
      String menuType, boolean isDisabled) {
    String linkTarStr = null;
    hostURL = wrapperHostUrl(hostURL);
    scmDomain = wrapperHostUrl(scmDomain);
    // 如果导航条是新版本且需突出显示的标签，则设置其ID为 threenav .
    boolean flag = isDisabled && "new".equals(menuType);
    String a_id = flag ? " id=\"threenav\"" : "";
    // 判断地址是跳转到外部站点
    if (isOutUrl(menu.getValue())) {
      // 按跳转业务处理
      String outUrl = menu.getValue();
      linkTarStr = "<a " + a_id + "href=\"" + outUrl + "\" " + appendTarget(menu.getTarget()) + " >";
    } else if (isScmUrl(menu.getValue())) {
      linkTarStr = "<a " + a_id + "href=\"" + getScmURL(menu.getValue(), scmDomain, insId) + "\" "
          + appendTarget(menu.getTarget()) + " >";

    } else {
      // 如果没有跳转则按普通菜单处理
      String localURL = menu.getValue();// addParameterForURL(menu.getValue(),
      // "menuId", "" +
      // menu.getMenuId());

      // 是否为空
      if (StringUtils.isEmpty(localURL)) {
        // 如果菜单URL为空则尝试取第一个子菜单的URL
        String firstChildURL = MenuUtils.getFirstChildURL(menu, insId);
        if (isOutUrl(firstChildURL)) {
          linkTarStr = "<a " + a_id + "href=\"" + firstChildURL + "\" " + appendTarget(menu.getTarget()) + " >";
        } else {
          String childLocalURl = firstChildURL;// addParameterForURL(firstChildURL,
          // "menuId", "" +
          // menu.getMenuId());
          linkTarStr =
              "<a " + a_id + "href=\"" + hostURL + childLocalURl + "\"  " + appendTarget(menu.getTarget()) + " >";
        }
      } else {
        linkTarStr = "<a " + a_id + "href=\"" + hostURL + localURL + "\" " + appendTarget(menu.getTarget()) + " >";
      }
    }

    return linkTarStr;
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
  private static String getMenuLinkStartTagForURL(MenuItemBean menu, String hostURL, int level, String scmDomain,
      long insId, boolean hasChildrens, String menuType) {

    // 新版本菜单样式中一级菜单的 a 链接标签的其他属性.
    String aContent = "";
    if ("new".equals(menuType)) {
      Long iNum = menu.getMenuId().longValue();
      // 一级菜单.
      if (level == 1) {
        // 取消一级菜单的鼠标响应事件(即还原为点击一级菜单后才显示其子菜单)_MaoJianGuo_2012-11-10_应fzq要求.
        // // 标签中的 Menu.showM(this,iNum)方法是 js_v5/menu.js文件中的JS事件,需对应匹配.
        // aContent = "id=mm" + iNum + " onmouseover=\"Menu.showM(this,"
        // + iNum
        // + ");\" onmouseout=\"Menu.OnMouseLeft();\"";
        aContent += " class=\"tempStr1" + iNum + "_" + iNum + "\" ";
      }
      // 二级菜单且当前菜单被选中.
      else if (level == 2) {
        aContent += " class=\"tempStr2" + iNum + "_" + iNum + "\" ";
      }
    }

    hostURL = wrapperHostUrl(hostURL);
    scmDomain = wrapperHostUrl(scmDomain);

    String linkTagStr = null;
    // 判断地址是跳转到外部站点
    if (isOutUrl(menu.getValue())) {
      String outUrl = menu.getValue();
      linkTagStr = "<a " + aContent + " href=\"" + outUrl + "\"  " + appendTarget(menu.getTarget());
    } else if (isScmUrl(menu.getValue())) {

      linkTagStr = "<a " + aContent + " href=\"" + getScmURL(menu.getValue(), scmDomain, insId) + "\" "
          + appendTarget(menu.getTarget());

    } else {
      String localURL = menu.getValue();

      if (StringUtils.isEmpty(localURL)) { // 是否为空
        String firstChildURL = MenuUtils.getFirstChildURL(menu, insId);
        if (MenuUtils.isOutUrl(firstChildURL)) {
          linkTagStr = "<a " + aContent + " href=\"" + firstChildURL;
        } else {
          String childLocalURl = "";

          childLocalURl = firstChildURL;
          linkTagStr = "<a " + aContent + " href=\"" + childLocalURl + "\" " + appendTarget(menu.getTarget());
        }
      } else {

        if (menu.getMenuId().longValue() == 1 && (insId == 2565 || insId == 2566)) {
          linkTagStr =
              "<a " + aContent + " href=\"" + hostURL + "/scmwebsns/rolMain" + "\" " + appendTarget(menu.getTarget());
        } else {
          linkTagStr = "<a " + aContent + " href=\"" + hostURL + localURL + "\" " + appendTarget(menu.getTarget());
        }

      }
    }
    if (StringUtils.isNotBlank(menuType) && !"new".equals(menuType)) {
      linkTagStr = linkTagStr + (hasChildrens ? " class=\"arrow_more_gray\"> " : " class=\"menu_a_default\"> ");
    } else {
      linkTagStr += ">";
    }
    return linkTagStr;
  }

  /**
   * 
   * 为URL追加参数.
   * 
   * @param url需要追加参数的URL
   * @param parameterName需要追加的参数名
   * @param parameterValue需要追加的参数值
   * @return 追加参数后的URL
   * 
   */
  public static String addParameterForURL(String url, String parameterName, String parameterValue) {

    if (StringUtils.isEmpty(url) || StringUtils.equals(url, "#")) {
      return "";
    }
    if (StringUtils.isEmpty(parameterName)) {
      return url;
    }

    if (url.indexOf("?") >= 0) {
      if (url.indexOf("menuId") >= 0) {
        return url;
      } else {
        return url + "&" + parameterName + "=" + parameterValue;
      }
    } else {
      return url + "?" + parameterName + "=" + parameterValue;
    }
  }

  /**
   * 
   * 取得主机域名.
   * 
   * @param url
   * @return host
   */
  public static String getHostURLForRequestURL(String url) {
    int hostStart = MenuUtils.HTTP_MARK.length();
    int hostEnd = url.indexOf("/", hostStart);
    return url.substring(hostStart, hostEnd);
  }

  /**
   * 
   * 判断是否跳出到外部站点的URL.
   * 
   * @param url
   * @return
   * 
   */
  public static boolean isOutUrl(final String url) {

    return StringUtils.startsWith(url, "http://");

  }

  public static String appendTarget(final String target) {
    if (StringUtils.isNotEmpty(target)) {
      return " target=\"" + target + "\" ";
    } else {
      return "";
    }
  }

  /**
   * 
   * 替换字符中的标志位.
   * 
   * @param str 需要替换的字符
   * @param filter 过滤模式MapM<key,value> key:str字符中需替换的标示符,value要替换的的字符
   * @return temp
   * 
   */
  public static String replaceStringforFilter(final String str, final Map<String, String> filter) {
    /**
     * 执行替换
     */
    String temp = null;

    if (StringUtils.isNotEmpty(str)) {
      temp = str;
      Iterator<String> iter = filter.keySet().iterator();
      while (iter.hasNext()) {
        String key = iter.next();
        temp = org.apache.commons.lang.StringUtils.replace(temp, key, filter.get(key));
      }
    }
    return temp;
  }

  /**
   * 
   * 取得子菜单URL.
   * 
   * @param menu
   * @return string
   * 
   */
  public static String getFirstChildURL(final MenuItemBean menu, final long insId) {
    String value = "";
    List<MenuItemBean> itemBeans = menu.getMenuItems();
    if (itemBeans != null && itemBeans.size() > 0) {
      // 过滤成果在线系统中不存在的菜单.
      if ((insId == 2565 || insId == 2566)) {
        for (int i = 0; i < itemBeans.size(); i++) {
          MenuItemBean itemBean = itemBeans.get(i);
          if (!ExcludeMenuConst.nsfcExcludeMenuIds.contains(itemBean.getMenuId())) {
            value = itemBean.getValue();
            break;
          }
        }
      }
      // 过滤科研之友系统中不存在的菜单.
      if ((insId != 2565 && insId != 2566)) {
        for (int i = 0; i < itemBeans.size(); i++) {
          MenuItemBean itemBean = itemBeans.get(i);
          if (!ExcludeMenuConst.scmExcludeMenuIds.contains(itemBean.getMenuId())) {
            value = itemBean.getValue();
            break;
          }
        }
      }
    }
    return value;
  }

  /**
   * 判断是否有跳转业务.
   * 
   * @param url
   * @return
   */
  public static boolean isScmUrl(String url) {
    if (url == null || url.equals(""))
      return false;
    int start = url.indexOf("?scm=");
    // 判断是否有参数
    if (start == 0) {
      String paraStr = url.substring(start + 1, url.length() - 1);
      String[] paraEntity = paraStr.split("&");
      if (paraEntity != null && paraEntity.length > 0) {
        for (int i = 0; i < paraEntity.length; i++) {
          String[] para = paraEntity[i].split("=");
          String key = para[0];
          if ("scm".equals(key))
            return true;
        }
      } else {
        return false;
      }
    }

    return false;
  }

  /**
   * 取得SCM路径.
   * 
   * @param url
   * @param scmHost 需要跳转到的SCM主机地址
   * @return
   */
  public static String getScmURL(String url, String scmHost, long insId) {
    String scmURL = url;
    int start = url.indexOf("?scm=");
    // 判断是否有参数
    if (start == 0) {
      String paraStr = url.substring(start + 1, url.length());
      String[] paraEntity = paraStr.split("&");
      if (paraEntity != null && paraEntity.length > 0) {
        for (int i = 0; i < paraEntity.length; i++) {
          String[] para = paraEntity[i].split("=");
          String key = para[0];
          if ("scm".equals(key)) {
            scmURL = scmHost + url.substring(url.indexOf("scm=") + "scm=".length());
            if (insId > 0) {
              scmURL = addParameterForURL(scmURL, "rolInsId", String.valueOf(insId));
            }
          }
        }
      }
    }
    return scmURL;
  }

  public static String wrapperHostUrl(String hostUrl) {
    if (hostUrl.toLowerCase().indexOf("http://") != -1) {
      hostUrl = hostUrl.replace("http://", "https://");
    }
    if (hostUrl.toLowerCase().indexOf("https://") == -1) {
      hostUrl = "https://" + hostUrl;
    }
    return hostUrl;

  }

  /**
   * 将当前菜单及其子菜单整理为list.
   * 
   * @param item
   */
  public static List<MenuItemBean> getMenuItemList(MenuItemBean item) {
    List<MenuItemBean> itemList = new ArrayList<MenuItemBean>();

    if (item != null) {
      itemList.add(item);
      // 如果该菜单包含子菜单.
      if (CollectionUtils.isNotEmpty(item.getMenuItems())) {
        // 遍历子菜单.
        for (int i = 0; i < item.getMenuItems().size(); i++) {
          MenuItemBean subMenuItem = item.getMenuItems().get(i);
          if (subMenuItem.getMenuItems().size() > 0) {
            // 递归获取子菜单.
            List<MenuItemBean> subItemList = getMenuItemList(subMenuItem);
            if (subItemList != null && subItemList.size() > 0) {
              itemList.addAll(subItemList);
            }
          } else {
            itemList.add(subMenuItem);
          }
        }
      }
    }
    return itemList;
  }

  /**
   * 根据访问的URL路径反向获得当前菜单id
   * 
   * @param url
   * @param menuItem
   * @return
   */
  public static Integer getMenuIdByRequestURL(String url, MenuItemBean menuItem) {

    Integer menuId = null;
    if (menuItem != null) {
      if (menuItem.getValue() != null && menuItem.getValue().indexOf(url) > -1) {
        menuId = Integer.valueOf(String.valueOf(menuItem.getMenuId()));
      } else {
        if (menuItem.getMenuItems().size() > 0) {
          // 遍历子菜单.
          for (int i = 0; i < menuItem.getMenuItems().size(); i++) {
            MenuItemBean subMenuItem = menuItem.getMenuItems().get(i);
            if (subMenuItem.getValue() != null && subMenuItem.getValue().indexOf(url) > -1) {
              menuId = Integer.valueOf(String.valueOf(subMenuItem.getMenuId()));
              break;
            } else {
              // 递归获取菜单ID.
              Integer subMenuId = getMenuIdByRequestURL(url, subMenuItem);
              if (subMenuId != null) {
                menuId = subMenuId;
                break;
              }
            }
          }
        }
      }
    }
    return menuId;

  }
}
