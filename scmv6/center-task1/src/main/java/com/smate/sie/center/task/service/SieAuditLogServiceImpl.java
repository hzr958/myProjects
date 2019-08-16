package com.smate.sie.center.task.service;

import java.sql.Clob;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.sie.core.base.utils.dao.auditlog.SieAuditActionTypeDao;
import com.smate.sie.core.base.utils.dao.auditlog.SieAuditLogDao;
import com.smate.sie.core.base.utils.date.IrisStringUtils;
import com.smate.sie.core.base.utils.model.auditlog.SieComAuditLog;
import com.smate.sie.core.base.utils.model.auditlog.SieComAuditType;

/***
 * 
 * @author 叶星源
 * @Date 201903
 */
@Service("sieAuditLogService")
@Transactional(rollbackOn = Exception.class)
public class SieAuditLogServiceImpl implements SieAuditLogService {

  public static Logger logger = LoggerFactory.getLogger(SieAuditLogServiceImpl.class);

  @Autowired
  private SieAuditLogDao auditLogDao;

  @Autowired
  private SieAuditActionTypeDao auditLogTypeDao;

  @Autowired
  private PersonDao personDao;

  @Override
  public void parseAuditLog(Map<String, Object> auditTrail) throws Exception {
    if (auditTrail == null) {
      return;
    }
    String audUser = Objects.toString(auditTrail.get("AUD_USER"), "");
    String audResource = Objects.toString(auditTrail.get("AUD_RESOURCE"), "");
    String audAction = Objects.toString(auditTrail.get("AUD_ACTION"), "");
    Date audDate = (Date) (auditTrail.get("AUD_DATE"));
    String audId = Objects.toString(auditTrail.get("AUD_ID"), "");
    // 处理AUD_CONTENT的内容
    String audCotent = IrisStringUtils.oracleClob2Str((Clob) auditTrail.get("AUD_CONTENT"));
    audCotent = new StringBuilder(StringUtils.isBlank(audCotent) ? audResource : audCotent).toString();

    // 获取解析的日志的模板
    List<SieComAuditType> auditTypes = auditLogTypeDao.getActionTypeList();
    SieComAuditType logTemplate = null;
    for (SieComAuditType auditActionType : auditTypes) {
      if (audAction.equals(auditActionType.getAudAction())) {
        logTemplate = auditActionType;
        break;
      }
    }

    // 解析日志并保存
    if (logTemplate != null) {
      // 类型
      String paramType = logTemplate.getParamType();
      SieComAuditType auditParentType = auditLogTypeDao.getParentType(logTemplate.getParentId());
      SieComAuditLog log = new SieComAuditLog();
      log.setLogName(auditParentType.getName());
      log.setActionName(logTemplate.getName());
      log.setDate(audDate);
      log.setAudId(Long.parseLong(audId));
      log.setUser(Long.parseLong(audUser));
      log.setUsername(personDao.get(log.getUser()).getZhName());
      Map<String, Object> returnParams = new HashMap<String, Object>();
      if ("array".equals(paramType)) {// 解析参数类型为数组
        log = this.arrayResovler(audCotent, logTemplate, log, returnParams);
      } else if ("map".equals(paramType)) {// 解析参数类型为map
        log = this.mapResovler(audCotent, logTemplate, log, returnParams);
      } else {
        log = this.normalResovler(audCotent, logTemplate, log, returnParams);
      }
      if (log != null) {
        auditLogDao.save(log);
      }
    }
  }

  /**
   * 解析参数为数组
   */
  @SuppressWarnings("rawtypes")
  private SieComAuditLog arrayResovler(String content, SieComAuditType logTemplate, SieComAuditLog log,
      Map<String, Object> returnParams) throws Exception {
    Map<String, Object> keyValues = new HashMap<String, Object>();
    // 解析日志需要将英文key替换为中文的参数
    String replaceKeys = logTemplate.getReplaceKeys();
    if (StringUtils.isNotBlank(replaceKeys)) {
      keyValues.putAll(JacksonUtils.json2Map(replaceKeys));
    }

    Object data = JacksonUtils.jsonListUnSerializer(content);
    if (data != null) {
      List list = (List) data;
      for (String key : keyValues.keySet()) {
        String value = keyValues.get(key).toString();
        if (value.startsWith("/")) {
          // 解析xml串
          returnParams.put(value.substring(value.lastIndexOf("/") + 1, value.length()),
              getNodeValueFromXML(Objects.toString(list.get(Integer.valueOf(key))), value));
        } else {
          returnParams.put(keyValues.get(key).toString(), Objects.toString(list.get(Integer.valueOf(key)), ""));
        }
      }
    }

    String template = logTemplate.getTemplate();
    this.sqlResovler(logTemplate.getTemplateSql(), returnParams);
    template = IrisStringUtils.str2str(template, returnParams);
    log.setContent(template);
    return log;
  }

  /**
   * 从XML字符串中获取XML节点值.
   */
  public static String getNodeValueFromXML(String xml, String nodePath) {
    // 加载原XML
    Document doc = null;
    try {
      doc = DocumentHelper.parseText(xml);
    } catch (DocumentException e) {
    }
    Node node = doc != null ? doc.selectSingleNode(nodePath) : null;
    if (node == null) {
      return "";
    } else {
      return node.getText();
    }
  }



  /**
   * 解析参数为map
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  private SieComAuditLog mapResovler(String content, SieComAuditType logTemplate, SieComAuditLog log,
      Map<String, Object> returnParams) throws Exception {

    Object data = JacksonUtils.jsonMapUnSerializer(content);
    if (data != null) {
      Map<String, Object> dataMap = (Map<String, Object>) ((List) data).get(0);
      returnParams.putAll(dataMap);
    }
    String template = logTemplate.getTemplate();
    this.sqlResovler(logTemplate.getTemplateSql(), returnParams);
    template = IrisStringUtils.str2str(template, returnParams);
    log.setContent(template);
    return log;
  }

  /**
   * 普通解析
   */
  private SieComAuditLog normalResovler(String content, SieComAuditType logTemplate, SieComAuditLog log,
      Map<String, Object> returnParams) throws Exception {
    String template = logTemplate.getTemplate();
    this.sqlResovler(logTemplate.getTemplateSql(), returnParams);
    template = IrisStringUtils.str2str(template, returnParams);
    log.setContent(template);
    return log;
  }

  /**
   * 执行sql结果转换为map
   */
  @SuppressWarnings("unchecked")
  private Map<String, Object> sqlResovler(String sql, Map<String, Object> returnParams) {
    if (StringUtils.isNotBlank(sql)) {
      List<Object> sqlParams = new ArrayList<Object>();
      sql = this.transSql(sql, returnParams, sqlParams);
      List<Map<String, Object>> list = auditLogDao.queryForList(sql, sqlParams.toArray());
      if (list != null && list.size() > 0 && list.get(0) != null) {
        for (String key : list.get(0).keySet()) {
          returnParams.put(key.toLowerCase(), list.get(0).get(key));
        }
      }
    }
    return returnParams;
  }

  @Override
  public void updateAuditTrail(Long audId, Integer status) {
    if (audId == 0L) {
      return;
    }
    auditLogDao.updateAuditTrailStatus(audId, status);
  }

  @Override
  public List<Map<String, Object>> getAuditLogList(Integer fetchSize) {
    return auditLogDao.getAuditLogList(0, fetchSize);
  }

  /**
   * 通过业务类传过来的参数对SQL中对应的参数进行替换,SQL中参数格式为[@参数名@].
   * 
   * @param sql sql语句
   * @param map 用于替换sql语句中特殊字符"[@ @]" 的map
   * @param params 执行sql语句时传入的占位符变量
   * @return
   */
  public String transSql(String sql, Map<String, Object> map, List<Object> params) {
    Map<String, Object> constMap = new HashMap<String, Object>();
    // 对专有值进行特殊处理
    Object zhName = map.get("zh_name");
    if (zhName != null && NumberUtils.isNumber(zhName.toString())) {
      Person person = personDao.get(Long.valueOf(zhName.toString().trim()));
      if (person != null) {
        String psnName = person.getZhName();
        map.replace("zh_name", psnName);
      }
    }
    if (map != null) {
      // 加入常量map，如果有相同的key则会覆盖，以传入进来的map为主
      constMap.putAll(map);
    }

    // 正则表达式，\\S表示去掉空白字符，如空格、回车等，*表示任意符号，值2是表示大小写不限制
    Pattern p = Pattern.compile("\\[@\\S[^@]*@\\]", 2);
    Matcher m = p.matcher(sql);
    String key;
    String paramKey;

    while (m.find()) {
      key = m.group().toLowerCase();
      paramKey = key.substring(2, key.length() - 2);// 去掉[@ @]
      sql = IrisStringUtils.regexReplaceString(sql, "\\[@" + paramKey + "@\\]", "?");
      params.add(constMap.get(paramKey));
    }
    return sql;
  }
}
