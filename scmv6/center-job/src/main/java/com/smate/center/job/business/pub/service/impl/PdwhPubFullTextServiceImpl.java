package com.smate.center.job.business.pub.service.impl;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.job.business.log.dao.TmpTaskInfoRecordDao;
import com.smate.center.job.business.pdwhpub.dao.PdwhFullTextFileDao;
import com.smate.center.job.business.pub.service.PdwhPubFullTextService;
import com.smate.core.base.file.model.ArchiveFile;
import com.smate.core.base.file.service.ArchiveFileService;
import com.smate.core.base.pub.dao.pdwh.PdwhFullTextImageDao;
import com.smate.core.base.pub.model.pdwh.PdwhFullTextImage;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.file.ArchiveFileUtil;
import com.smate.core.base.utils.file.FileUtils;
import com.smate.core.base.utils.image.im4java.gm.GMImageUtil;
import com.smate.core.base.utils.image.im4java.gm.GMOperation.DensityUnitEnum;

@Service
@Transactional(rollbackFor = Exception.class)
public class PdwhPubFullTextServiceImpl implements PdwhPubFullTextService {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private TmpTaskInfoRecordDao tmpTaskInfoRecordDao;
	@Autowired
	private PdwhFullTextImageDao pdwhFullTextImagedao;
	@Autowired
	private ArchiveFileService archiveFileService;
	@Autowired
	private PdwhFullTextFileDao pdwhFullTextFileDao;
	@Value("${file.root}")
	private String fileRoot;

	@Override
	public void ConvertPubFulltextPdfToimage(Long pdwhPubId) throws Exception {
		// 获取文件id
		Long fileId = pdwhFullTextFileDao.getFileIdByPubId(pdwhPubId);
		ArchiveFile archiveFile = archiveFileService.getArchiveFileById(fileId);
		if (archiveFile == null) {
			this.updateTaskStatus(pdwhPubId, 2, "文件不存在");
			return;
		}
		String fileName = archiveFile.getFileName();
		String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
		if (!"pdf".equalsIgnoreCase(fileType)) {
			logger.error("转换pdwh成果的全文包含非pdf文件，不处理!pdwhPubId" + pdwhPubId);
			this.updateTaskStatus(pdwhPubId, 2, "转换pdwh成果的全文包含非pdf文件");
			return;
		}
		String filePath = getSrcRelativePath(archiveFile);
		String srcFilePath = fileRoot + "/" + filePath;
		File file = new File(srcFilePath);
		if (!file.exists()) {
			this.updateTaskStatus(pdwhPubId, 2, "pdf文件不存在");
			return;
		}
		String imageExt = ".jpeg";
		String destImgpath = ArchiveFileUtil.getFilePath(fileId.toString());// 图片路径
		// /b1/b7/64/1000000265915
		// 原图url /pubFulltextImage/b1/b7/64/1000000265915.jpeg
		String desImageUrl = "/" + ServiceConstants.DIR_PUB_FULLTEXT_IMAGE + destImgpath + imageExt;

		String desImagePath = fileRoot + desImageUrl;// 原图图片路径

		// /b1/b7/64/1000000265915_img_1.jpeg
		String destThumbImgName = destImgpath + "_img_1" + imageExt;// 缩略图名

		// 缩略图url /pubFulltextImage/b1/b7/64/1000000265915_img_1.jpeg
		String destThumbUrl = "/" + ServiceConstants.DIR_PUB_FULLTEXT_IMAGE + destThumbImgName;// 缩略图url

		String destThumbPath = fileRoot + "/" + ServiceConstants.DIR_PUB_FULLTEXT_IMAGE + destThumbImgName;// 缩略图路径
		// 先pdf生成原图然后用原图转缩略图
		GMImageUtil.convert(100, 300, DensityUnitEnum.PixelsPerInch, srcFilePath, desImagePath);
		File srcimg = new File(desImagePath);
		if (!srcimg.exists()) {
			this.updateTaskStatus(pdwhPubId, 2, "pdf生成原图出错，无法继续生成缩略图！");
			return;
		}

		GMImageUtil.sample(ArchiveFileUtil.PDWH_THUMBNAIL_WIDTH_178, ArchiveFileUtil.PDWH_THUMBNAIL_HEIGHT_239, false,
				desImagePath, destThumbPath);

		this.savePdwhPubFulltextImage(pdwhPubId, 1, destThumbUrl, fileId, ".pdf");// 默认保存为缩略图
		// 处理完毕更新状态
		this.updateTaskStatus(pdwhPubId, 1, "pdf全文转换图片成功");
	}

	@Override
	public void updateTaskStatus(Long pdwhPubId, int status, String errMsg) {
		tmpTaskInfoRecordDao.updateTaskStatus(pdwhPubId, status, errMsg, 3);

	}

	/**
	 * 获取图片文件的源路径（相对地址）
	 *
	 * @author houchuanjie
	 * @date 2018年1月11日 下午3:36:24
	 */
	private String getSrcRelativePath(ArchiveFile archiveFile) {
		// 文件相对路径
		String relativeFilePath = archiveFile.getFileUrl();
		// 如果relativeFilePath是空的，那么根据filePath获取文件路径
		if (ArchiveFileUtil.FILE_URL_DEFAULT_VALUE.equals(relativeFilePath)) {
			relativeFilePath = FileUtils.SYMBOL_VIRGULE + ServiceConstants.DIR_UPFILE
					+ ArchiveFileUtil.getFilePath(archiveFile.getFilePath());
		}
		if (relativeFilePath.charAt(0) != FileUtils.SYMBOL_VIRGULE_CHAR) {
			relativeFilePath = FileUtils.SYMBOL_VIRGULE + relativeFilePath;
		}
		archiveFile.setFileUrl(relativeFilePath);
		return relativeFilePath;
	}

	/**
	 * 删除pdwh fulltext image
	 */
	@Override
	public void delPdwhPubFulltextByPubId(Long pubId) throws Exception {
		try {
			PdwhFullTextImage pubFulltext = this.pdwhFullTextImagedao.get(pubId);
			if (pubFulltext != null) {
				String fulltextImagePath = pubFulltext.getImagePath();
				this.pdwhFullTextImagedao.delete(pubId);
				if (StringUtils.isNotBlank(pubFulltext.getImagePath())) {
					archiveFileService.deleteFileByPath(fulltextImagePath);
				}
			}
		} catch (Exception e) {
			logger.error("删除pdwh成果pubId={}的全文转换后的图片出现异常：{}", pubId, e);
		}

	}

	/**
	 * 保存pdwh成果全文图片信息.
	 */
	private void savePdwhPubFulltextImage(Long pubId, Integer fulltextImagePageIndex, String fulltextImagePath,
			Long fileId, String fileExtend) throws Exception {
		PdwhFullTextImage pubFulltext;
		try {
			pubFulltext = pdwhFullTextImagedao.get(pubId);
			if (pubFulltext == null) {
				pubFulltext = new PdwhFullTextImage();
				pubFulltext.setPubId(pubId);
				pubFulltext.setFileId(fileId);
				pubFulltext.setImagePageIndex(fulltextImagePageIndex);
				pubFulltext.setImagePath(fulltextImagePath);
				pubFulltext.setFileExtend(fileExtend);
				pubFulltext.setUpdateTime(new Date());
				pdwhFullTextImagedao.save(pubFulltext);
			} else {
				pubFulltext.setImagePageIndex(fulltextImagePageIndex);
				pubFulltext.setImagePath(fulltextImagePath);
				pubFulltext.setFileExtend(fileExtend);
				pubFulltext.setUpdateTime(new Date());
				pdwhFullTextImagedao.save(pubFulltext);
			}
		} catch (Exception e) {
			logger.error("保存pdwh成果pubId={}全文图片信息出现异常：{}", pubId, e);
			throw new Exception(e);
		}
	}

	@Override
	public List<Long> getbatchhandleIdList(long begin, long end, int jobType) {
		return tmpTaskInfoRecordDao.getbatchhandleIdList(begin, end, jobType);
	}
}
