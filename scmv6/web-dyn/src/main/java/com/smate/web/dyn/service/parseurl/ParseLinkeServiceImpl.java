package com.smate.web.dyn.service.parseurl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.smate.core.base.utils.url.URLParseUtils;

/**
 * 
 * @author zzx
 *
 */
@Service("parseLinkeService")
public class ParseLinkeServiceImpl implements ParseLinkeService {

  @Override
  public Map<String, String> resolveUrl(String shareUrl) throws IOException {
    if (!URLParseUtils.isUrl(shareUrl)) {
      return null;
    }
    Map<String, String> map = new HashMap<String, String>();
    Document doc = URLParseUtils.getDocByURL(shareUrl);
    // 获取title
    Elements ogTitleElement = doc.select("meta[og:title]");
    if (ogTitleElement == null || ogTitleElement.size() == 0) {// meta中不存在,则在title里取
      Elements titleElement = doc.select("title");
      map.put("title", titleElement != null ? titleElement.get(0).html() : "");
    } else {
      map.put("title", ogTitleElement.get(0).attr("og:title"));
    }
    // 获取image
    Elements ogImageElements = doc.select("meta[og:image]");
    map.put("image",
        ogImageElements != null && ogImageElements.size() > 0 ? ogImageElements.get(0).attr("og:image") : "");

    return map;
  }

}
