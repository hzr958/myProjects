package com.smate.center.open.service.wechat.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.constant.wechat.UserConstant;
import com.smate.center.open.service.wechat.AccessTokenService;
import com.smate.core.base.utils.constant.wechat.WeChatConstant;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.wechat.MessageUtil;

/**
 * 微信用户服务.
 * 
 * @author xys
 *
 */
@Service("weChatUserService")
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private AccessTokenService accessTokenService;

  @SuppressWarnings("rawtypes")
  @Override
  public List<Map> getUserList(List<Map> userList, int curCount, String nextOpenid) {
    String fullReqUrl = getFullReqUrl(accessTokenService.getAccessToken(), nextOpenid);
    Map map = MessageUtil.httpRequest(fullReqUrl, WeChatConstant.REQ_METHOD_GET, nextOpenid);
    try {
      int total = (int) map.get(UserConstant.TOTAL);
      int count = (int) map.get(UserConstant.COUNT);
      if (userList == null) {
        userList = new ArrayList<Map>();
      }
      userList.add(map);
      curCount += count;
      if (total > curCount) {
        nextOpenid = map.get(UserConstant.NEXT_OPENID).toString();
        userList = getUserList(userList, curCount, nextOpenid);
      }
    } catch (Exception e) {
      logger.info("The result from weChat:" + JacksonUtils.jsonObjectSerializer(map));
    }
    return userList;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public List<String> getOpenIdList() {
    List<String> openIdList = new ArrayList<String>();
    List<Map> userList = this.getUserList(null, 0, "");
    if (!CollectionUtils.isEmpty(userList)) {
      for (Map user : userList) {
        Map data = (Map) user.get(UserConstant.DATA);
        List<String> subOpenIds = (List<String>) data.get(UserConstant.OPENID);
        openIdList.addAll(subOpenIds);
      }
    }
    return openIdList;
  }

  private static String getFullReqUrl(String accessToken, String nextOpenid) {
    StringBuffer sb = new StringBuffer();
    sb.append(UserConstant.REQ_URL_USER_LIST);
    sb.append("?access_token=");
    sb.append(accessToken);
    sb.append("&next_openid=");
    sb.append(nextOpenid);
    return sb.toString();
  }

}
