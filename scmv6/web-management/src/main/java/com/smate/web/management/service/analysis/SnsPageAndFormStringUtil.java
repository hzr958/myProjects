package com.smate.web.management.service.analysis;

import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.Page;

/**
 * 根据Page ,Form 条件 生成MD5加密的字符串(lxz专用)
 * 
 * @author lvxingzhi
 *
 */
public class SnsPageAndFormStringUtil {

  /**
   * page & form
   * 
   * @param page
   * @param obj
   * @return
   */
  public static final String get(Page page, Object obj) {
    String pageStr = "";
    String formStr = "";
    if (page != null) {
      pageStr = page.getPageString();
      if (pageStr != null) {
        pageStr = JacksonUtils.jsonObjectSerializer(pageStr);
      }
    }
    if (formStr != null) {
      formStr = JacksonUtils.jsonObjectSerializer(obj);
    }
    return DigestUtils.md5Hex((pageStr == null ? "" : pageStr) + (formStr == null ? "" : formStr)
        + LocaleContextHolder.getLocale().getLanguage());
  }

  /**
   * page & form & psnid
   * 
   * @param page
   * @param obj
   * @param psnId
   * @return
   */
  public static final String get(Page page, Object obj, Long psnId) {
    String pageStr = "";
    String formStr = "";
    if (page != null) {
      pageStr = page.getPageString();
      if (pageStr != null) {
        pageStr = JacksonUtils.jsonObjectSerializer(pageStr);
      }
    }
    if (formStr != null) {
      formStr = JacksonUtils.jsonObjectSerializer(obj);
    }
    return DigestUtils.md5Hex((pageStr == null ? "" : pageStr) + (formStr == null ? "" : formStr) + psnId
        + LocaleContextHolder.getLocale().getLanguage());
  }

  /**
   * page & psnid
   * 
   * @param page
   * @param obj
   * @param psnId
   * @return
   */
  public static final String get(String key, Page page, Long psnId) {
    String pageStr = "";
    if (page != null) {
      pageStr = page.getPageString();
      if (pageStr != null) {
        pageStr = JacksonUtils.jsonObjectSerializer(pageStr);
      }
    }
    return DigestUtils
        .md5Hex(key + (pageStr == null ? "" : pageStr) + psnId + LocaleContextHolder.getLocale().getLanguage());
  }

  /**
   * key & page & form & psnid
   * 
   * @param key
   * @param page
   * @param obj
   * @param psnId
   * @return
   */
  public static final String get(String key, Page page, Object obj, Long psnId) {
    String pageStr = "";
    String formStr = "";
    if (page != null) {
      pageStr = page.getPageString();
      if (pageStr != null) {
        pageStr = JacksonUtils.jsonObjectSerializer(pageStr);
      }
    }
    if (formStr != null) {
      formStr = JacksonUtils.jsonObjectSerializer(obj);
    }
    return DigestUtils.md5Hex(key + (pageStr == null ? "" : pageStr) + (formStr == null ? "" : formStr) + psnId
        + LocaleContextHolder.getLocale().getLanguage());
  }

  /**
   * key & page & form & psnid & isNew
   * 
   * @param key
   * @param page
   * @param obj
   * @param psnId
   * @return
   * @throws Exception
   */
  public static final String get(String key, Page page, Object obj, Long psnId, boolean isNew) {
    String pageStr = "";
    String formStr = "";
    if (page != null) {
      pageStr = page.getPageString();
      if (pageStr != null) {
        pageStr = JacksonUtils.jsonObjectSerializer(pageStr);
      }
    }
    if (formStr != null) {
      formStr = JacksonUtils.jsonObjectSerializer(obj);
    }
    return DigestUtils.md5Hex(key + (pageStr == null ? "" : pageStr) + (formStr == null ? "" : formStr) + psnId
        + (isNew ? 1 : 2) + LocaleContextHolder.getLocale().getLanguage());
  }


  /**
   * key & page & form & sessionId
   * 
   * @param key
   * @param page
   * @param obj
   * @param psnId
   * @return
   */
  public static final String get(String key, Page page, Object obj, String sessionId) {
    String pageStr = "";
    String formStr = "";
    if (page != null) {
      pageStr = page.getPageString();
      if (pageStr != null) {
        pageStr = JacksonUtils.jsonObjectSerializer(pageStr);
      }
    }
    if (formStr != null) {
      formStr = JacksonUtils.jsonObjectSerializer(obj);
    }
    return DigestUtils.md5Hex(key + (pageStr == null ? "" : pageStr) + (formStr == null ? "" : formStr) + sessionId
        + LocaleContextHolder.getLocale().getLanguage());
  }

  /**
   * key & page & form
   * 
   * @param key
   * @param page
   * @param obj
   * @return
   */
  public static final String get(String key, Page page, Object obj) {
    String pageStr = "";
    String formStr = "";
    if (page != null) {
      pageStr = page.getPageString();
      if (pageStr != null) {
        pageStr = JacksonUtils.jsonObjectSerializer(pageStr);
      }
    }
    if (formStr != null) {
      formStr = JacksonUtils.jsonObjectSerializer(obj);
    }
    return DigestUtils.md5Hex(key + (pageStr == null ? "" : pageStr) + (formStr == null ? "" : formStr)
        + LocaleContextHolder.getLocale().getLanguage());
  }

  /**
   * key & page & list<String> & psnId
   * 
   * @param key
   * @param page
   * @param list
   * @param psnId
   * @return
   */
  public static final String get(String key, Page page, List list, Long psnId, boolean isNew) {
    String pageStr = "";
    String listStr = "";
    if (page != null) {
      pageStr = page.getPageString();
      if (pageStr != null) {
        pageStr = JacksonUtils.jsonObjectSerializer(pageStr);
      }
    }
    if (listStr != null) {
      listStr = JacksonUtils.listToJsonStr(list);
    }
    return DigestUtils.md5Hex(key + (pageStr == null ? "" : pageStr) + (listStr == null ? "" : listStr) + psnId
        + (isNew ? 1 : 2) + LocaleContextHolder.getLocale().getLanguage());
  }

  /**
   * key & psnid & count
   */
  public static final String get(String key, Long psnId, Integer count) {
    return DigestUtils.md5Hex(key + psnId + count + LocaleContextHolder.getLocale().getLanguage());
  }


}
