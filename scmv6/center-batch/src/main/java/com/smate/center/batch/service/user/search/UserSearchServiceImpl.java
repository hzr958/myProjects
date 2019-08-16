package com.smate.center.batch.service.user.search;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 检索用户.
 * 
 * @author liqinghua
 * 
 */
@Service("userSearchService")
@Transactional(rollbackFor = Exception.class)
public class UserSearchServiceImpl implements UserSearchService {

  private static final String EN_WORD = "[a-zA-Z_0-9]{1,}";
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  /*
   * @Autowired private UserSearchDao userSearchDao;
   * 
   * 
   * 
   * 
   * @Override public void saveUserSearch(Long psnId, String zhName, String enName, Integer pubFlag,
   * Integer scmFlag, Integer nodeId, Integer isPrivate,Integer selfLogin) throws Exception { try {
   * UserSearch user = userSearchDao.get(psnId); if (user == null) { user = new UserSearch();
   * user.setPsnId(psnId); } if (isPrivate == null) { isPrivate = 0; } user.setIsPrivate(isPrivate);
   * // 英文名字不存在，则将汉语拼音作为其英文名 if (StringUtils.isBlank(enName)) { Map<String, String> userMap =
   * UserSearchServiceImpl.parseZhInfo(zhName); if (userMap != null) { zhName = userMap.get("zhInfo");
   * enName = userMap.get("enInfo"); } else { enName = ""; } }
   * 
   * // 中文转拼音 Map<String, String> map = ServiceUtil.parsePinYin(zhName); String zhInfoIndex = enName;
   * if (map != null) { zhInfoIndex = map.get("lastName") + " " + map.get("firstName"); }
   * 
   * if (StringUtils.isBlank(zhName)) { zhName = ""; }
   * 
   * // 如果英文名称中存在非英文单词外的字符，将其拼入中文名称 if (enName != null && !"".equals(enName)) { zhName = zhName +
   * enName.replaceAll(EN_WORD, "").replaceAll("\\s+?", ""); enName = enName.replaceAll("[\\s]{2,}?",
   * " "); }
   * 
   * // 如果不想改变原先的配置，则传入-1 if (pubFlag == null || pubFlag == -1) { pubFlag = user.getPubFlag() == null
   * ? 0 : user.getPubFlag(); } if (scmFlag == null || scmFlag == -1) { scmFlag = user.getScmFlag() ==
   * null ? 1 : user.getScmFlag(); } user.setNodeId(nodeId); user.setZhInfo(zhName);
   * user.setEnInfo(StringUtils.substring(enName, 0, 100)); user.setPubFlag(pubFlag);
   * user.setScmFlag(scmFlag); user.setIndexFlag(1); user.setZhInfoIndex(zhInfoIndex);
   * user.setSelfLogin(selfLogin!=null?selfLogin:0); userSearchDao.save(user); } catch (Exception e) {
   * logger.error("保存用户检索信息", e); throw new Exception(e); } }
   */

  /**
   * 拆分中文信息中的中英文信息.
   * 
   * @param zhInfo
   * @return
   */
  private static Map<String, String> parseZhInfo(String zhInfo) {
    // 解决报空指针问题
    if (zhInfo == null) {
      zhInfo = "";
    }
    Pattern pattern = Pattern.compile(EN_WORD);
    Matcher match = pattern.matcher(zhInfo);
    StringBuilder sb = new StringBuilder();
    while (match.find()) {
      String info = match.group();
      sb.append(info).append(" ");
    }
    zhInfo = zhInfo.replaceAll(EN_WORD, "").replaceAll("\\s+?", "");
    Map<String, String> userMap = new HashMap<String, String>();
    userMap.put("zhInfo", zhInfo);
    userMap.put("enInfo", sb.toString());
    return userMap;
  }

}
