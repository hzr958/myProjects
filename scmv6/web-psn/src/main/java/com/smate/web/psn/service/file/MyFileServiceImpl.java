package com.smate.web.psn.service.file;

import com.smate.center.batch.connector.service.job.BatchJobsService;
import com.smate.core.base.file.dao.ArchiveFileDao;
import com.smate.core.base.file.enums.FileTypeEnum;
import com.smate.core.base.file.model.ArchiveFile;
import com.smate.core.base.file.service.ArchiveFileService;
import com.smate.core.base.file.service.FileDownloadUrlService;
import com.smate.core.base.psn.dao.PsnFileDao;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.model.PsnFile;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.psn.model.StationFile;
import com.smate.core.base.utils.cache.CacheService;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.web.psn.dao.file.PsnFileShareBaseDao;
import com.smate.web.psn.dao.file.PsnFileShareRecordDao;
import com.smate.web.psn.dao.file.StationFileDao;
import com.smate.web.psn.dao.message.MsgChatRelationDao;
import com.smate.web.psn.dao.message.MsgContentDao;
import com.smate.web.psn.dao.message.MsgRelationDao;
import com.smate.web.psn.form.FileMainForm;
import com.smate.web.psn.form.PsnFileInfo;
import com.smate.web.psn.form.StationFileInfo;
import com.smate.web.psn.model.file.PsnFileShareBase;
import com.smate.web.psn.model.file.PsnFileShareBaseInfo;
import com.smate.web.psn.model.file.PsnFileShareRecord;
import com.smate.web.psn.model.message.MsgRelation;
import com.smate.web.psn.model.psninfo.PsnInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.*;

/**
 * 用户文件服务实现类
 * 
 * @author zk
 *
 */
@Service("myFileService")
@Transactional(rollbackOn = Exception.class)
public class MyFileServiceImpl implements MyFileService {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private BatchJobsService batchJobsService;
  @Autowired
  private StationFileDao stationFileDao;
  @Autowired
  private ArchiveFileDao archiveFileDao;
  @Autowired
  private ArchiveFileService archiveFileService;
  @Autowired
  private PsnFileDao psnFileDao;
  @Autowired
  private PsnFileShareBaseDao psnFileShareBaseDao;
  @Autowired
  private PsnFileShareRecordDao psnFileShareRecordDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private FileDownloadUrlService fileDownUrlService;
  @Resource
  private PsnProfileUrlDao psnProfileUrlDao;
  @Resource
  private MsgContentDao msgContentDao;
  @Value("${domainscm}")
  private String domainscm;
  @Resource(name = "psnCacheService")
  private CacheService cacheService;
  @Autowired
  private MsgRelationDao msgRelationDao;
  @Autowired
  private MsgChatRelationDao msgChatRelationDao;

  @Override
  public List<StationFile> findFileForGroup(Long psnId, Long groupId, Integer pageSize, Integer pageNo) {
    return stationFileDao.findFileForGroup(psnId, groupId, pageSize, pageNo);
  }

  @Override
  public List<StationFileInfo> findFileForGrp(Long psnId, Long groupId, Page<StationFile> page, String searchKey) {
    List<StationFileInfo> list = new ArrayList<StationFileInfo>();
    StationFileInfo stationFileInfo;
    StationFile stationFile;
    stationFileDao.findFileForGrp(psnId, groupId, page, searchKey);
    if (page.getResult() != null && page.getResult().size() > 0) {
      for (int i = 0; i < page.getResult().size(); i++) {
        stationFileInfo = new StationFileInfo();
        stationFile = page.getResult().get(i);
        // sf.fileId,sf.fileName ,sf.filePath ,sf.fileType , sf.fileDesc
        // ,sf.uploadTime
        stationFileInfo.setFileDesc(stationFile.getFileDesc());
        stationFileInfo.setFileId(stationFile.getFileId());
        stationFileInfo.setFileName(stationFile.getFileName());
        stationFileInfo.setFilePath(stationFile.getFilePath());
        stationFileInfo.setUploadTime(stationFile.getUploadTime());
        stationFileInfo.setExistsGrpFile(stationFileDao.existsGrpFile(groupId, stationFile.getArchiveFileId(), psnId));
        stationFileInfo.setFileType(stationFile.getFileType());
        list.add(stationFileInfo);
      }
    }
    return list;
  }

  @Override
  public List<StationFileInfo> findFileForPsn(Long psnId, Page<StationFile> page, String searchKey) throws Exception {
    List<StationFileInfo> list = null;
    StationFileInfo stationFileInfo = null;
    StationFile stationFile = null;
    stationFileDao.findFileForPsn(psnId, page, searchKey);
    if (page.getResult() != null && page.getResult().size() > 0) {
      list = new ArrayList<StationFileInfo>();
      for (int i = 0; i < page.getResult().size(); i++) {
        stationFileInfo = new StationFileInfo();
        stationFile = page.getResult().get(i);
        stationFileInfo.setFileDesc(stationFile.getFileDesc());
        stationFileInfo.setFileId(stationFile.getFileId());
        stationFileInfo.setFileName(stationFile.getFileName());
        stationFileInfo.setFilePath(stationFile.getFilePath());
        stationFileInfo.setUploadTime(stationFile.getUploadTime());
        stationFileInfo.setFileType(stationFile.getFileType());
        list.add(stationFileInfo);
      }
    }
    return list;
  }

  @Override
  public void collectionStationFile(Long psnId, Long fileId) throws Exception {
    PsnFile otherPsnFile = psnFileDao.get(fileId);
    if (otherPsnFile == null) {
      throw new Exception("查询不到你要收藏的文件fileId=" + fileId);
    }
    // 判断是否重复收藏
    PsnFile psnFile = psnFileDao.findPsnFileByPsnIdArchiveId(psnId, otherPsnFile.getArchiveFileId());
    if (psnFile == null) {
      psnFile = new PsnFile();
    }
    psnFile.setArchiveFileId(otherPsnFile.getArchiveFileId());
    psnFile.setUpdateDate(new Date());
    psnFile.setUploadDate(psnFile.getUpdateDate());
    psnFile.setFileDesc(otherPsnFile.getFileDesc());
    psnFile.setFileName(otherPsnFile.getFileName());
    psnFile.setFileType(otherPsnFile.getFileType());
    psnFile.setStatus(0);
    psnFile.setOwnerPsnId(psnId);
    psnFile.setPermission(0);
    psnFileDao.save(psnFile);
  }

  @Override
  public void getFileListForPsn(FileMainForm form) throws Exception {
    List<PsnFile> psnFilelist =
        psnFileDao.getFileListForPsn(form.getPage(), form.getPsnId(), form.getSearchKey(), form.getFileTypeNum());
    List<PsnFileInfo> psnFileInfoList = null;
    PsnFileInfo psnFileInfo = null;
    if (psnFilelist != null && psnFilelist.size() > 0) {
      psnFileInfoList = new ArrayList<PsnFileInfo>();
      String grpFileId = new String();
      if (form.getGrpId() != null && form.getGrpId() != 0L) {
        Long grpId = form.getGrpId();
        List<Long> grpPsnFileIdList = archiveFileService.getGrpPsnFileIdList(grpId);
        for (Long fileId : grpPsnFileIdList) {
          grpFileId += fileId + ",";
        }
      }
      for (PsnFile sf : psnFilelist) {
        psnFileInfo = new PsnFileInfo();
        psnFileInfo.setFileDesc(sf.getFileDesc());
        psnFileInfo.setFileId(sf.getId());
        psnFileInfo.setFileName(sf.getFileName());
        psnFileInfo.setUploadDate(sf.getUploadDate());
        psnFileInfo.setUpdateDate(sf.getUpdateDate());
        psnFileInfo.setFileType(sf.getFileType());
        // SCM-14409 hcj
        // 下载地址
        psnFileInfo.setDownloadUrl(fileDownUrlService.getDownloadUrl(FileTypeEnum.PSN, sf.getId(), 0L));
        // 缩略图地址
        String imgFileThumbUrl = archiveFileService.getImgFileThumbUrl(sf.getArchiveFileId());
        psnFileInfo.setImgThumbUrl(imgFileThumbUrl);
        if (grpFileId.indexOf(sf.getArchiveFileId().toString()) != -1) {
          psnFileInfo.setIsGrpFile(true);
        }
        psnFileInfoList.add(psnFileInfo);
      }
      form.setPsnFileInfoList(psnFileInfoList);
    }
  }

  @Override
  public void getFileList(FileMainForm form) throws Exception {
    List<Map<Object, Object>> psnFilelist = psnFileDao.getFileList(form.getPage(), form.getPsnId());
    List<PsnFileInfo> psnFileInfoList = new ArrayList<PsnFileInfo>();
    if (psnFilelist != null && psnFilelist.size() > 0) {
      for (Map<Object, Object> map : psnFilelist) {
        PsnFileInfo psnFileInfo = new PsnFileInfo();
        if (map.get("fileName") != null) {
          psnFileInfo.setFileName(map.get("fileName").toString());
        }
        if (map.get("fileDesc") != null) {
          psnFileInfo.setFileDesc(map.get("fileDesc").toString());
        }
        if (map.get("fileType") != null) {
          psnFileInfo.setFileType(map.get("fileType").toString());
        }
        if (map.get("uploadDate") != null) {
          psnFileInfo.setUploadDate((Date) map.get("uploadDate"));
        }
        if (map.get("fileSize") != null) {
          psnFileInfo.setFileSize(getPrintSize((Long) map.get("fileSize")));
        }
        if (map.get("id") != null) {
          // SCM-14409 hcj
          /*
           * String downloadUrl = fileDownUrlService.getDownloadUrl(FileTypeEnum.PSN, (Long) map.get("id"));
           */
          String downloadUrl = fileDownUrlService.getShortDownloadUrl(FileTypeEnum.PSN, (Long) map.get("id"));
          psnFileInfo.setDownloadUrl(downloadUrl);
          psnFileInfo.setDes3FileId(Des3Utils.encodeToDes3(map.get("id").toString()));// 添加des3fileid
          // 方便app端作缓存标识
          psnFileInfo.setAppDownloadUrl(downloadUrl);// app下载不需要权限，修改当前web下载URl时请注意保持app端不变
        }
        if (map.get("archiveFileId") != null) {
          String imgFileThumbUrl = archiveFileService.getImgFileThumbUrl((Long) map.get("archiveFileId"));
          psnFileInfo.setImgThumbUrl(imgFileThumbUrl);
          psnFileInfo.setAppImgThumbUrl(this.getAppDefaultFileIcon(psnFileInfo.getFileType(), imgFileThumbUrl));
        }
        psnFileInfoList.add(psnFileInfo);
      }
      form.setPsnFileInfoList(psnFileInfoList);
    }
  }

  /**
   * 根据文件类型获取默认app文件图标
   * 
   * @param fileType
   * @param url 图片文件缩略图
   * @return
   */
  public String getAppDefaultFileIcon(String fileType, String url) {
    String fileIconPath = "/resmod/smate-pc/img/fileicon_default.png";
    // 添加文件预览图片地址，方便app端直接获取
    if ("pdf".equals(fileType)) {
      fileIconPath = "/resmod/smate-pc/img/fileicon_pdf.png";
    }
    if ("ppt".equals(fileType) || "pptx".equals(fileType)) {
      fileIconPath = "/resmod/smate-pc/img/fileicon_ppt.png";
    } else if ("docx".equals(fileType) || "doc".equals(fileType)) {
      fileIconPath = "/resmod//smate-pc/img/fileicon_doc.png";
    } else if ("zip".equals(fileType) || "rar".equals(fileType)) {
      fileIconPath = "/resmod/smate-pc/img/fileicon_zip.png";
    } else if ("xlsx".equals(fileType) || "xls".equals(fileType)) {
      fileIconPath = "/resmod/smate-pc/img/fileicon_xls.png";
    } else if ("txt".equals(fileType)) {
      fileIconPath = "/resmod/smate-pc/img/fileicon_txt.png";
    } else if ("imgIc".equals(fileType) || "jpg".equals(fileType) || "png".equals(fileType)) {
      // 图片类型则下载地址为缩略图
      fileIconPath = url;
    }
    return fileIconPath;
  }

  public static String getPrintSize(long size) {
    // 如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
    if (size < 1024) {
      return String.valueOf(size) + "B";
    } else {
      size = size / 1024;
    }
    // 如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
    // 因为还没有到达要使用另一个单位的时候
    // 接下去以此类推
    if (size < 1024) {
      return String.valueOf(size) + "KB";
    } else {
      size = size / 1024;
    }
    if (size < 1024) {
      // 因为如果以MB为单位的话，要保留最后1位小数，
      // 因此，把此数乘以100之后再取余
      size = size * 100;
      return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "MB";
    } else {
      // 否则如果要以GB为单位的，先除于1024再作同样的处理
      size = size * 100 / 1024;
      return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "GB";
    }
  }

  @Override
  public void getFileListCallBack(FileMainForm form) throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, Object> fileListTypeCount = psnFileDao.getFileListTypeCount(form.getPsnId(), form.getSearchKey());
    map.put("fileTypeNum", fileListTypeCount);
    form.setResultMap(map);
  }

  @Override
  public Long saveMyUploadFile(FileMainForm form) throws Exception {
    PsnFile psnFile = new PsnFile();
    ArchiveFile archiveFile = archiveFileDao.get(form.getArchiveFileId());
    if (archiveFile == null) {
      throw new Exception("保存上传文件时， archiveFile为空+ archiveFileId=" + form.getArchiveFileId());
    }
    if (archiveFile.getCreatePsnId().equals(form.getPsnId())) {
      psnFile.setOwnerPsnId(form.getPsnId());
      psnFile.setFileName(archiveFile.getFileName());
      psnFile.setFileType(archiveFile.getFileType());
      psnFile.setFileDesc(form.getFileDesc());
      psnFile.setStatus(0);
      psnFile.setUploadDate(archiveFile.getCreateTime());
      psnFile.setUpdateDate(archiveFile.getCreateTime());
      psnFile.setArchiveFileId(archiveFile.getFileId());
      psnFile.setPermission(0);
      psnFileDao.save(psnFile);
      // 图片类型的文件创建缩略图生成任务
      if (archiveFileService.isImageFile(archiveFile)) {
        batchJobsService.createAndSaveThumbnailBatchJob(archiveFile, FileTypeEnum.PSN);
      }
    }
    return psnFile.getId();
  }

  @Override
  public void delMyFile(FileMainForm form) throws Exception {
    int result = psnFileDao.delFile(form.getFileId(), +SecurityUtils.getCurrentUserId());
    if (result <= 0) {
      throw new Exception("你没有权限删除该文件： psnId = " + SecurityUtils.getCurrentUserId() + "文件id=" + form.getFileId());
    }
  }

  @Override
  public void saveFileDesc(FileMainForm form) throws Exception {
    psnFileDao.saveFileDesc(form.getFileId(), SecurityUtils.getCurrentUserId(), form.getFileDesc());
  }

  @Override
  public void getShareRecordsList(FileMainForm form) throws Exception {
    List<PsnFileShareBase> psnFileShareBaseList = psnFileShareBaseDao.findListByPsnId(form.getPsnId(), form.getPage());
    PsnFileShareBaseInfo pfsb = null;
    Person sender = null;
    if (psnFileShareBaseList != null && psnFileShareBaseList.size() > 0) {
      form.setPsnFileShareBaseInfos(new ArrayList<PsnFileShareBaseInfo>());
      for (PsnFileShareBase p : psnFileShareBaseList) {
        pfsb = new PsnFileShareBaseInfo();
        pfsb.setPsnFileShareBase(p);
        builePsnFileShareBaseListInfo(pfsb, p.getId());
        sender = personDao.findPersonBaseIncludeIns(pfsb.getSenderId());
        buildSenderInfo(sender, pfsb);
        form.getPsnFileShareBaseInfos().add(pfsb);
      }
    }
  }

  /**
   * 构建分享记录列表显示信息
   * 
   * @param pfsb
   * @param shareBaseId
   * @throws Exception
   */
  private void builePsnFileShareBaseListInfo(PsnFileShareBaseInfo pfsb, Long shareBaseId) throws Exception {
    List<Long> fileIdList = psnFileShareRecordDao.findFileIdByShareBaseId(shareBaseId);
    List<PsnFileShareRecord> psnFileRecordList = psnFileShareRecordDao.findPsnIdByShareBaseId(shareBaseId);
    if (fileIdList != null && fileIdList.size() > 0) {
      pfsb.setPsnFileInfoList(new ArrayList<PsnFileInfo>());
      PsnFile psnFile = null;
      for (Long fileId : fileIdList) {
        psnFile = psnFileDao.get(fileId);
        pfsb.getPsnFileInfoList().add(buildPsnFileInfo(psnFile));
      }
    }
    if (psnFileRecordList != null && psnFileRecordList.size() > 0) {
      pfsb.setPsnInfoList(new ArrayList<PsnInfo>());
      Person person = null;
      for (PsnFileShareRecord psnFileShareRecord : psnFileRecordList) {
        Long psnId = psnFileShareRecord.getReveiverId();
        // 站外人员
        if (psnId == 0) {
          PsnInfo p = new PsnInfo();
          p.setPsnId(psnId);
          p.setEmail(psnFileShareRecord.getOutsideEmail());
          pfsb.getPsnInfoList().add(p);
        } else {
          person = personDao.findPersonBaseIncludeIns(psnId);
          pfsb.getPsnInfoList().add(buildPsnInfo(person));
        }
      }
      List<PsnInfo> newPsnInfoList = removeDuplicatePsn(pfsb.getPsnInfoList());
      pfsb.setPsnInfoList(newPsnInfoList);
    }
  }

  /**
   * 构建文件接收人信息
   * 
   * @param person
   * @return
   * @throws Exception
   */
  private PsnInfo buildPsnInfo(Person person) throws Exception {
    if (person == null) {
      return null;
    }
    PsnInfo p = new PsnInfo();
    List<String> psnName = buildPsnName(person);
    p.setZhName(psnName.get(0));
    p.setEnName(psnName.get(1));
    p.setEmail(person.getEmail());
    PsnProfileUrl profileUrl = psnProfileUrlDao.get(person.getPersonId());
    if (profileUrl != null && StringUtils.isNotBlank(profileUrl.getPsnIndexUrl())) {
      p.setPsnShortUrl(domainscm + "/" + ShortUrlConst.P_TYPE + "/" + profileUrl.getPsnIndexUrl());
    } else {
      String psnUrl =
          domainscm + "/psnweb/homepage/show?des3PsnId=" + Des3Utils.encodeToDes3(person.getPersonId().toString());
      p.setPsnShortUrl(psnUrl);
    }
    p.setPsnId(person.getPersonId());
    return p;
  }

  /**
   * 构建分享文件信息
   * 
   * @param psnFile
   * @return
   */
  private PsnFileInfo buildPsnFileInfo(PsnFile psnFile) {
    if (psnFile == null) {
      return null;
    }
    PsnFileInfo p = new PsnFileInfo();
    p.setFileId(psnFile.getId());
    p.setArchiveFileId(psnFile.getArchiveFileId());
    p.setFileDesc(psnFile.getFileDesc());
    p.setFileName(psnFile.getFileName());
    p.setFileType(psnFile.getFileType());
    // SCM-14409 hcj
    String downloadUrl = fileDownUrlService.getDownloadUrl(FileTypeEnum.PSN, psnFile.getId(), 0L);
    p.setDownloadUrl(downloadUrl);
    return p;
  }

  /**
   * 构造发送人的信息
   * 
   * @param sender
   * @param pfsb
   */
  private void buildSenderInfo(Person sender, PsnFileShareBaseInfo pfsb) throws Exception {
    if (sender != null) {
      List<String> psnName = buildPsnName(sender);
      pfsb.setSenderZhName(psnName.get(0));
      pfsb.setSenderEnName(psnName.get(0));
      pfsb.setSenderAvatars(sender.getAvatars());
      PsnProfileUrl profileUrl = psnProfileUrlDao.get(sender.getPersonId());
      if (profileUrl != null && StringUtils.isNotBlank(profileUrl.getPsnIndexUrl())) {
        pfsb.setSenderShortUrl(domainscm + "/" + ShortUrlConst.P_TYPE + "/" + profileUrl.getPsnIndexUrl());
      } else {
        String psnUrl =
            domainscm + "/psnweb/homepage/show?des3PsnId=" + Des3Utils.encodeToDes3(sender.getPersonId().toString());
        pfsb.setSenderShortUrl(psnUrl);
      }
    }
  }

  /**
   * 构建人员姓名
   * 
   * @param p
   * @return
   */
  private List<String> buildPsnName(Person p) throws Exception {
    List<String> listName = new ArrayList<String>(2);
    String zhName = p.getName();
    String enName = p.getEname();
    if (StringUtils.isBlank(zhName)) {
      zhName =
          (p.getFirstName() != null ? p.getFirstName() : "") + " " + (p.getLastName() != null ? p.getLastName() : "");
    }
    if (StringUtils.isBlank(zhName)) {
      zhName = p.getEname();
    }
    if (StringUtils.isBlank(enName)) {
      enName = zhName;
    }
    listName.add(zhName != null ? zhName : "");
    listName.add(enName != null ? enName : "");
    return listName;
  }

  @Override
  public void cancelFileShare(FileMainForm form) throws Exception {
    if (form.getPsnId() == null || form.getPsnId() == 0L) {
      return;
    }
    PsnFileShareBase psnFileShareBase = psnFileShareBaseDao.get(form.getShareBaseId());
    if (psnFileShareBase != null && psnFileShareBase.getSharerId().equals(form.getPsnId())) {
      psnFileShareBase.setStatus(1);
      psnFileShareBase.setUpdateDate(new Date());
      psnFileShareBaseDao.save(psnFileShareBase);
      // 删除文字内容站内信 tsz
      boolean shareText = false;
      if (StringUtils.isNoneBlank(psnFileShareBase.getShareContentRel())) {
        shareText = true;
        String[] str = psnFileShareBase.getShareContentRel().split(",");
        for (String dynRelIdStr : str) {
          Long dynRelId = NumberUtils.toLong(dynRelIdStr, 0L);
          MsgRelation msgRelation = msgRelationDao.get(dynRelId);
          if (msgRelation != null) {
            msgRelation.setStatus(2);
            msgRelation.setDealDate(new Date());
            msgRelationDao.save(msgRelation);
          }
        }
      }
      List<PsnFileShareRecord> shareRecordList = psnFileShareRecordDao.findListByShareBaseId(psnFileShareBase.getId());
      PsnFileShareRecord psnFileShareRecord = null;
      if (shareRecordList != null && shareRecordList.size() > 0) {
        for (int i = 0; i < shareRecordList.size(); i++) {
          psnFileShareRecord = shareRecordList.get(i);
          psnFileShareRecord.setUpdateDate(new Date());
          psnFileShareRecord.setStatus(1);
          psnFileShareRecordDao.save(psnFileShareRecord);
          // 删除站内信
          MsgRelation msgRelation = msgRelationDao.get(psnFileShareRecord.getMsgRelationId());
          if (msgRelation != null) {
            msgRelation.setStatus(2);
            msgRelation.setDealDate(new Date());
            msgRelationDao.save(msgRelation);
            // 将站内信的的上一条聊天记录作为两人最新聊天记录
            Long senderId = msgRelation.getSenderId();
            Long receiverId = msgRelation.getReceiverId();
            //更新消息
            form.getUpdateContentPsnIds().add(senderId.toString()+"|"+receiverId.toString());
          }
        }
      }
    } else {
      throw new Exception("你没有权限取消该分享文件：psnId=" + form.getPsnId() + " 分享文件shareBaseId=" + form.getShareBaseId());
    }
  }

  public  void updateContent(FileMainForm form){
    if(CollectionUtils.isEmpty(form.getUpdateContentPsnIds())) return;
    for(String s : form.getUpdateContentPsnIds()){
      long senderId = Long.parseLong(s.split("\\|")[0]);
      long receiverId =Long.parseLong(s.split("\\|")[1]);;
      List<Long> allContentIds = msgRelationDao.getAllContentId(senderId, receiverId);
      if(CollectionUtils.isNotEmpty(allContentIds)){
        try{
          String contentByChat = msgContentDao.getMsgContentByChat(allContentIds.get(0));
          msgChatRelationDao.updateContent(senderId, receiverId, contentByChat);
        }catch (Exception e){
          logger.error("更新站内信消息异常，senderId="+senderId ,e);
        }
      }
    }



  }

  public boolean checkContentId(String ids, Long contentId) {
    if(StringUtils.isBlank(ids)){
      return  false ;
    }
    String[] split = ids.split(",");
    for(String id : split){
      MsgRelation msgRelation = msgRelationDao.get(Long.parseLong(id));
      if (msgRelation != null && msgRelation.getContentId().longValue() == contentId.longValue()) {
        return true;
      }
    }

    return false;
  }

  @Override
  public Page getPsnFileListInGroup(Page<StationFile> page) throws Exception {
    List<StationFile> list = new ArrayList<StationFile>();
    List<PsnFile> psnFilelist = new ArrayList<PsnFile>();
    Long psnId = SecurityUtils.getCurrentUserId();
    psnFilelist = psnFileDao.getFileListForPsn(page, psnId, null, null);
    // list = stationFileDao.getFileListForPsn(page, psnId, null, null);
    for (int i = 0; i < psnFilelist.size(); i++) {
      PsnFile psnFile = psnFilelist.get(i);
      if (psnFile == null || psnFile.getArchiveFileId() == null || psnFile.getArchiveFileId() == 0L) {
        continue;
      }
      StationFile stationFile = new StationFile();
      ArchiveFile sf = this.archiveFileDao.get(psnFile.getArchiveFileId());
      if (sf != null) {
        stationFile.setFileSize(sf.getFileSize());
        stationFile.setFileDesc(sf.getFileDesc());
        stationFile.setFileId(sf.getFileId());
        stationFile.setFileName(sf.getFileName());
        stationFile.setUploadTime(psnFile.getUploadDate());
        // stationFile.set(psnFile.getUpdateDate());
        stationFile.setFileType(sf.getFileType());
        stationFile.setDownloadUrl(fileDownUrlService.getDownloadUrl(FileTypeEnum.PSN, psnFile.getId(), 0L));
        list.add(stationFile);
      }
    }
    page.setResult(list);
    return page;
  }

  @Override
  public void batchDelMyFile(FileMainForm form) throws Exception {
    String[] fileIds = form.getDes3FileIds().split(",");
    for (String id : fileIds) {
      Long fileId = Long.parseLong(Des3Utils.decodeFromDes3(id));
      if (checkCurrFileIsExist(form.getPsnId(), fileId)) {
        psnFileDao.delFile(fileId, form.getPsnId());
        form.setBatchCount(form.getBatchCount() + 1);
      }
    }
  }

  @Override
  public boolean checkCurrFileIsExist(Long psnId, Long fileId) {
    boolean flag = false;
    PsnFile psnFile = psnFileDao.checkCurrFileIsExist(psnId, fileId);
    if (Objects.nonNull(psnFile)) {
      flag = true;
    }
    return flag;
  }

  /**
   * 去重
   * 
   * @param psnList
   * @return
   */
  private List<PsnInfo> removeDuplicatePsn(List<PsnInfo> psnList) {
    Set<PsnInfo> set = new TreeSet<PsnInfo>(new Comparator<PsnInfo>() {
      @Override
      public int compare(PsnInfo o1, PsnInfo o2) {
        return o1.getEmail().compareTo(o2.getEmail());
      }
    });
    set.addAll(psnList);
    return new ArrayList<PsnInfo>(set);
  }
}
