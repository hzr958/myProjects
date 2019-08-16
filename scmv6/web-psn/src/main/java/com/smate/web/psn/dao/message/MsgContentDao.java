package com.smate.web.psn.dao.message;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.psn.model.message.MsgContent;


/**
 * 消息内容实体dao
 * 
 * @author zzx
 *
 */

@Repository
public class MsgContentDao extends SnsHibernateDao<MsgContent, Long> {
  /**
   * 
   * @param contentId
   * @return
   */
  public MsgContent getMsgContent(Long contentId) {
    return null;
  }

  /**
   * 获取站内信回话最新的一条消息内容
   * 
   * @param contentId
   * @return
   */
  public String getMsgContentByChat(Long contentId) throws Exception {
    String hql = "select t.content from MsgContent t where t.contentId=:contentId";
    String content = (String) this.createQuery(hql).setParameter("contentId", contentId).uniqueResult();
    String newContent = "";
    if (content != null && JacksonUtils.isJsonString(content)) {
      Map<String, Object> map = JacksonUtils.jsonToMap(content);
      String type = map.get("smateInsideLetterType").toString();
      if ("text".equals(type)) {
        newContent = map.get("content").toString();
      } else if ("file".equals(type)) {
        newContent = map.get("fileName").toString();
      } else if ("pub".equals(type)) {
        newContent = map.get("pubTitleZh").toString() == "" ? map.get("pubTitleEn").toString()
            : map.get("pubTitleZh").toString();
      } else if ("fund".equals(type)) {
        newContent = map.get("fundZhTitle").toString() == "" ? map.get("fundEnTitle").toString()
            : map.get("fundZhTitle").toString();
      } else if ("prj".equals(type)) {
        newContent = map.get("prjTitleZh").toString() == "" ? map.get("prjTitleEn").toString()
            : map.get("prjTitleZh").toString();
      } else if ("agency".equals(type)) {
        newContent = map.get("agencyZhTitle").toString() == "" ? map.get("agencyEnTitle").toString()
            : map.get("agencyZhTitle").toString();
      } else if ("pdwhpub".equals(type)) {
        newContent = map.get("pubTitleZh").toString() == "" ? map.get("pubTitleEn").toString()
            : map.get("pubTitleZh").toString();
      } else if ("fulltext".equals(type)) {
        newContent = map.get("fileName").toString();
      }
    }
    return newContent;
  }
}
