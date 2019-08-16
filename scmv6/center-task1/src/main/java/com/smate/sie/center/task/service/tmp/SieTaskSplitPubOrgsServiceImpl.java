package com.smate.sie.center.task.service.tmp;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.task.exception.ServiceException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.sie.center.task.dao.tmp.SieTaskSplitPubOrgsDao;
import com.smate.sie.center.task.model.tmp.SieTaskSplitPubOrgs;

/**
 * 拆分task_split_pub_orgs 表pub_json 服务层 接口实现
 * 
 * @author ztg
 *
 */
@Service("tmpWustPubsService")
@Transactional(rollbackOn = Exception.class)
public class SieTaskSplitPubOrgsServiceImpl implements SieTaskSplitPubOrgsService {

  private Logger logger = LoggerFactory.getLogger(getClass());


  @Autowired
  private SieTaskSplitPubOrgsDao sieTaskSplitPubOrgsDao;

  @Override
  public Long countNeedHandleKeyId() {
    try {
      return sieTaskSplitPubOrgsDao.countNeedHandleKeyId();
    } catch (Exception e) {
      logger.error("读取task_split_pub_orgs表中需要处理的总数出错 ！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<SieTaskSplitPubOrgs> loadNeedHandleKeyId(int maxSize) {
    List<SieTaskSplitPubOrgs> tmpWustPubsList = null;
    try {
      tmpWustPubsList = sieTaskSplitPubOrgsDao.loadNeedHandleKeyId(maxSize);
    } catch (Exception e) {
      logger.error("读取task_split_pub_orgs表中需要处理的数据出错！", e);
      throw new ServiceException(e);
    }
    return tmpWustPubsList;
  }


  /**
   * { "seqNo": 1, "name": "Gong, Rong Zhou", "email": "chengyz@wust.edu.cn", "dept": "School of
   * Information Science and Engineering, Wuhan University of Science and Technology, Wuhan; 430081,
   * China", "communicable": true, "insNames": [ ], "firstAuthor": true },
   */
  @SuppressWarnings({"deprecation", "unchecked"})
  @Override
  public void doSplit(SieTaskSplitPubOrgs splitPubOrgs) {
    String pubJson = splitPubOrgs.getPubJson();
    List<String> orgs = new LinkedList<>();
    List<String> emailAuthors = new LinkedList<>();
    try {
      if (pubJson != null) {
        Map<String, Object> pubJsonMap = JacksonUtils.json2HashMap(pubJson);
        List<Map<String, Object>> members = (List<Map<String, Object>>) pubJsonMap.get("members");
        if (members != null && !members.isEmpty()) {
          for (Map<String, Object> member : members) {
            String org = ""; // 单位信息
            String emailAuthor = "";// 通讯作者
            Integer seqNo = (Integer) member.get("seqNo");
            String dept = (String) member.get("dept");
            Boolean communicable = (Boolean) member.get("communicable");
            String name = (String) member.get("name");

            org = "[" + seqNo + "]" + dept;
            if (communicable) {
              emailAuthor = name;
            }

            if (StringUtils.isNotBlank(org)) {
              orgs.add(org);
            }

            if (StringUtils.isNotBlank(emailAuthor)) {
              emailAuthors.add(emailAuthor);
            }
          }

          if (!orgs.isEmpty() || orgs.size() > 0) {
            String tmpOrgsStr = StringUtils.join(orgs.toArray(), " ");

            splitPubOrgs.setOrgs(StringEscapeUtils.unescapeHtml4(tmpOrgsStr));
          }

          if (!emailAuthors.isEmpty() || emailAuthors.size() > 0) {
            splitPubOrgs.setEmailAuthor(StringUtils.join(emailAuthors.toArray(), "; "));
          }
        }
      }
      splitPubOrgs.setStatus(1);
      sieTaskSplitPubOrgsDao.saveOrUpdate(splitPubOrgs);
    } catch (Exception e) {
      logger.error("读取表中pubJson，进行拆分出错！", e);
      throw new ServiceException(e);
    }
  }

}
