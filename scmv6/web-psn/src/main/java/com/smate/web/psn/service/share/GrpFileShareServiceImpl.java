package com.smate.web.psn.service.share;

import java.net.URLEncoder;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.psn.model.share.FileShareForm;

/**
 * 群组文件文件分享
 * 
 * @author aijiangbin
 *
 */
@Service("grpFileShareService")
@Transactional(rollbackFor = Exception.class)
public class GrpFileShareServiceImpl implements GrpFileShareService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private FileShareQueryService fileShareQueryService;

  /**
   * 查找文件分享列表
   * 
   * @param from
   */
  @Override
  public void findEmailVileFiles(FileShareForm form, Page page) throws Exception {

    // 新文件分享逻辑
    if (StringUtils.isNotBlank(form.getA()) && StringUtils.isNotBlank(form.getB())) {
      Long resSendId = NumberUtils.toLong(ServiceUtil.decodeFromDes3(form.getA()), 0L);
      Long resReveiverId = NumberUtils.toLong(ServiceUtil.decodeFromDes3(form.getB()), 0L);
      Long baseId = NumberUtils.toLong(ServiceUtil.decodeFromDes3(form.getC()), 0L);
      // resReveiverId ==0 为匿名用户 ， 2018-02-26
      if (resSendId != 0L && resReveiverId != null) {
        int status = fileShareQueryService.checkGrpFileShareStatus(baseId);
        if (status == -1) {
          form.setStatus(-1);
          return;
        }
        page = fileShareQueryService.getGrpFileShareDataInSendSide(resSendId, resReveiverId, baseId, page);
        if (SecurityUtils.getCurrentUserId() == null || SecurityUtils.getCurrentUserId() == 0l) {
          form.setStatus(0);
          // 未登录
          String A = "", B = "", C = "", des3GrpId = "";
          if (StringUtils.isNotEmpty(form.getA())) {
            A = form.getA().replace("+", "%2B");
          }
          if (StringUtils.isNotEmpty(form.getB())) {
            B = form.getB().replace("+", "%2B");
          }
          if (StringUtils.isNotEmpty(form.getC())) {
            C = form.getC().replace("+", "%2B");
          }
          if (StringUtils.isNotEmpty(form.getDes3GrpId())) {
            des3GrpId = form.getDes3GrpId().replace("+", "%2B");
          }
          String service =
              "/psnweb/fileshare/emailviewfiles?des3GrpId=" + des3GrpId + "&A=" + A + "&B=" + B + "&C=" + C;
          service = URLEncoder.encode(Des3Utils.encodeToDes3(service), "utf-8");
          form.setLoginUrl("/oauth/index?service=" + service);
          form.setRegisterUrl("/oauth/pc/register?service=" + service);

        } else if (SecurityUtils.getCurrentUserId().equals(resReveiverId)) {
          form.setStatus(1);
        } else {
          form.setStatus(2);
        }
        return;
      }
    }

  }
}
