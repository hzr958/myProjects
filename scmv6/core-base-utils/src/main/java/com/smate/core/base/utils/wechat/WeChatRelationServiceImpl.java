package com.smate.core.base.utils.wechat;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.dao.wechat.WeChatRelationDao;
import com.smate.core.base.utils.model.wechat.WeChatRelation;

/**
 * 微信和科研之友关联服务类
 * 
 * @author zk
 *
 */
@Service("weChatRelationService")
@Transactional(noRollbackFor = Exception.class)
public class WeChatRelationServiceImpl implements WeChatRelationService {

  @Autowired
  private WeChatRelationDao weChatRelationDao;

  @Override
  public Long querypsnIdByWeChatOpenid(String webChatOpenId) {
    return weChatRelationDao.queryPsnIdByWeChatOpenid(webChatOpenId);
  }

  @Override
  public List<String> findWeChatNoUnionIdList() {
    return weChatRelationDao.findWeChatNoUnionIdList();
  }

  @Override
  public void refreshUnionId(String openId, String unionId) {
    weChatRelationDao.refreshUnionId(openId, unionId);
  }

  @Override
  public Long querypsnIdByWeChatUnionid(String webChatUnionId) {
    return weChatRelationDao.queryPsnIdByWeChatUnionid(webChatUnionId);
  }

  /**
   * 
   * WX：绑定成功 wxOccupy ：被占用
   */
  @Override
  public String bindWeChat(String webChatOpenId, String webChatUnionId, Integer bindType, Long psnId, String nickName)
      throws Exception {
    // 先判断，该unionId是否被绑定
    Long bindPsnId = querypsnIdByWeChatUnionid(webChatUnionId);
    if (bindPsnId == null || bindPsnId == 0L) {
      Long openId = weChatRelationDao.findSmateOpenIdByPsnId(psnId);
      // 在判断当前的openId是否 已经绑定
      WeChatRelation chatRelation = weChatRelationDao.getBySmateOpenId(openId);
      if (chatRelation != null) {
        return "repeatBind";
      } else {
        WeChatRelation weChatRelation = new WeChatRelation();
        weChatRelation.setCreateTime(new Date());
        weChatRelation.setBindType(bindType);
        weChatRelation.setNickName(nickName);
        weChatRelation.setSmateOpenId(openId);
        weChatRelation.setWebChatOpenId(webChatOpenId);
        weChatRelation.setWeChatUnionId(webChatUnionId);
        weChatRelationDao.save(weChatRelation);
        return "WX";
      }

    } else if (bindPsnId.longValue() == psnId.longValue()) {
      return "WX";
    }
    return "wxOccupy";

  }

  @Override
  public Long findSmateOpenIdByWxUnionId(String wxUnionId) throws Exception {
    return weChatRelationDao.findSmateOpenIdByWxUnionId(wxUnionId);
  }
}
