package com.smate.web.v8pub.service.repeatpub;

import com.smate.core.base.file.enums.FileTypeEnum;
import com.smate.core.base.file.service.FileDownloadUrlService;
import com.smate.core.base.pub.dao.PubIndexUrlDao;
import com.smate.core.base.pub.model.PubIndexUrl;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.v8pub.dao.repeatpub.PubSameItemDAO;
import com.smate.web.v8pub.dao.repeatpub.PubSameRecordDAO;
import com.smate.web.v8pub.po.repeatpub.PubSameItemPO;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.service.sns.PubFullTextService;
import com.smate.web.v8pub.service.sns.PubSnsService;
import com.smate.web.v8pub.vo.RepeatPubInfo;
import com.smate.web.v8pub.vo.RepeatPubVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 重复成果 - 显示-实现类
 * 
 * @author zzx
 *
 */
@Service("repeatPubShowService")
@Transactional(rollbackFor = Exception.class)
public class RepeatPubShowServiceImpl implements RepeatPubShowService {
  @Autowired
  private PubSameRecordDAO pubSameRecordDao;
  @Autowired
  private PubSameItemDAO pubSameItemDao;
  @Autowired
  private PubSnsService pubSnsService;
  @Autowired
  private PubFullTextService pubFullTextService;
  @Autowired
  private PubIndexUrlDao pubIndexUrlDao;
  @Autowired
  private FileDownloadUrlService fileDownUrlService;
  @Value("${domainscm}")
  private String domainscm;

  @Override
  public void repeatPubMainTitle(RepeatPubVO repeatPubVO) throws Exception {
    Long userId = SecurityUtils.getCurrentUserId();
    if (userId != null && userId != 0L) {
      Long count = pubSameRecordDao.findPubSameRecordCount(userId);
      if (count > 0) {
        List<Long> recordIdsList = pubSameRecordDao.getPubSameRecords(userId);
        if (recordIdsList != null && recordIdsList.size() > 0) {
          List<String> des3Records = new ArrayList<String>(recordIdsList.size());
          for (Long recordId : recordIdsList) {
            des3Records.add(Des3Utils.encodeToDes3(recordId.toString()));
          }
          repeatPubVO.getResultMap().put("records", des3Records.toArray());
        }
        repeatPubVO.getResultMap().put("count", count);
        repeatPubVO.getResultMap().put("result", "success");
      } else {
        repeatPubVO.getResultMap().put("result", "no_result");
      }
    }
  }

  @Override
  public void showRepeatPubPage(RepeatPubVO repeatPubVO) throws Exception {

    List<RepeatPubInfo> repeatPubs = new ArrayList<RepeatPubInfo>();
    // 根据具体的组id 查找成果id列表
    List<PubSameItemPO> pubList =
        pubSameItemDao.getPubSameItems(Long.parseLong(Des3Utils.decodeFromDes3(repeatPubVO.getDes3RecordId())));
    if (pubList != null && pubList.size() > 0) {
      RepeatPubInfo repeatPub = null;
      // 根据pubId获取成果信息
      for (PubSameItemPO item : pubList) {
        PubSnsPO pubSnsPO = pubSnsService.get(item.getPubId());
        if (pubSnsPO != null) {
          repeatPub = new RepeatPubInfo();
          repeatPub.setTitle(pubSnsPO.getTitle());
          repeatPub.setUpdateDate(item.getUpdateDate());
          repeatPub.setDes3PubId(Des3Utils.encodeToDes3(pubSnsPO.getPubId().toString()));
          repeatPub.setBriefDesc(pubSnsPO.getBriefDesc());
          if (StringUtils.isNotBlank(pubSnsPO.getAuthorNames())) {
            repeatPub.setAuthorNamesNoTag(pubSnsPO.getAuthorNames().replaceAll("<([^>]*)>", ""));
          }
          repeatPub.setDealStatus(item.getDealStatus());
          // 成果全文图像资源地址
          String fullTextImagePath = pubFullTextService.getFulltextImageUrl(pubSnsPO.getPubId());
          if (StringUtils.isBlank(fullTextImagePath)) {
            repeatPub.setFullTextImagePath("/resmod/images_v5/images2016/file_img.jpg");
          } else {
            repeatPub.setFullTextImagePath(fullTextImagePath);
            // 下载地址
            String downloadUrl = fileDownUrlService.getDownloadUrl(FileTypeEnum.SNS_FULLTEXT, pubSnsPO.getPubId());
            repeatPub.setDownloadUrl(downloadUrl);
          }
          // 成果短地址路径，方便用戶查看成果全文
          PubIndexUrl pubIndexUrl = pubIndexUrlDao.get(item.getPubId());
          if (pubIndexUrl != null && StringUtils.isNotBlank(pubIndexUrl.getPubIndexUrl())) {
            repeatPub.setPubIndexUrl(domainscm + "/" + ShortUrlConst.A_TYPE + "/" + pubIndexUrl.getPubIndexUrl());
          }
          repeatPub.setDes3RecordId(Des3Utils.encodeToDes3(item.getRecordId().toString()));
          repeatPub.setDes3pubSameItemId(Des3Utils.encodeToDes3(item.getId().toString()));
          repeatPubs.add(repeatPub);
        } else {
          continue;
        }
      }
      repeatPubVO.setRepeatPubInfoList(repeatPubs);
      repeatPubVO.getResultMap().put("result", "sucess");
    } else {
      repeatPubVO.getResultMap().put("result", "no_result");
    }

  }

}
