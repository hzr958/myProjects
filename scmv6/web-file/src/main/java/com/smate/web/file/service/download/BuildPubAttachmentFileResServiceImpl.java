package com.smate.web.file.service.download;

import com.smate.core.base.pub.util.PubDetailVoUtil;
import com.smate.core.base.pub.vo.Accessory;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.url.RestUtils;
import com.smate.web.file.consts.FileConsts;
import com.smate.web.file.exception.FileDownloadNoPermissionException;
import com.smate.web.file.exception.FileNotExistException;
import com.smate.web.file.form.FileDownloadForm;
import com.smate.web.file.utils.FileNamePathParseUtil;
import org.omg.CORBA.Object;
import org.springframework.beans.factory.annotation.Value;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 个人成果附件下载
 * 
 * @author Administrator
 *
 */
@Transactional(rollbackOn = Exception.class)
public class BuildPubAttachmentFileResServiceImpl extends BuildResServiceBase {


  @Value("${domainrol}")
  private String domainrol;


  @Override
  public void check(FileDownloadForm form) throws Exception {
    PubDetailVO pubDetailVO = null;
    super.checkArchiveFile(form.getFileId(), form);
    // 不是短地址
    if (!form.isShortUrl()) {
      if (form.getPubId() != null) {
        String SERVER_URL = domainscm + FileConsts.QUERY_PUB_DETAIL_URL;
        Map<String, String> parmaMap = new HashMap<>();
        parmaMap.put("serviceType", FileConsts.QUERY_SNS_PUB);
        parmaMap.put("des3PubId", Des3Utils.encodeToDes3(form.getPubId().toString()));
        Map<String, Object> remoteInfo =
            (Map<String, Object>) RestUtils.getRemoteInfo(parmaMap, SERVER_URL, restTemplate);
        pubDetailVO = PubDetailVoUtil.getBuilPubDetailVO(JacksonUtils.jsonObjectSerializer(remoteInfo));
      }
      if (pubDetailVO == null) {
        form.setResult(false);
        form.setResultMsg("文件不存在！");
        throw new FileNotExistException();
      }
      int permission = 2;
      List<Accessory> accessoryList = pubDetailVO.getAccessorys();
      for (int i = 0; i < accessoryList.size(); i++) {
        Accessory acc = accessoryList.get(i);
        if (form.getFileId().longValue() == acc.getFileId().longValue()) {
          if (acc.getPermission() != null) {
            permission = acc.getPermission();
          }
        }
      }
      switch (permission) {
        case 0: // 公开 所有人可下载
          break;
        default:// 隐私
        {
          Long pubOwnerId = pubDetailVO.getPubOwnerPsnId();
          Long currPsnId = SecurityUtils.getCurrentUserId();
          // 隐私但是当前用户是成果的拥有者
          boolean flag = currPsnId != 0 && currPsnId.equals(pubOwnerId);
          if (flag) {
            break;
          } else {
            form.setResult(false);
            form.setResultMsg("没有下载该文件的权限！");
            throw new FileDownloadNoPermissionException();
          }
        }
      }

    }

  }

  @Override
  public void buildResUrl(FileDownloadForm form) throws Exception {
    String url = "/" + basicPath + FileNamePathParseUtil.parseFileNameDir(form.getArchiveFile().getFilePath());
    form.setResPath(url);
  }

  @Override
  public void extend(FileDownloadForm form) throws Exception {
    // TODO Auto-generated method stub

  }

}
