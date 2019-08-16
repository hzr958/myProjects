package com.smate.test.file;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.smate.core.base.file.enums.FileTypeEnum;
import com.smate.core.base.file.service.FileDownloadUrlService;

/**
 *
 * @author houchuanjie
 * @date 2017年11月28日 上午10:06:55
 * 
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring/applicationContext*.xml" })
public class FileDownloadUrlServiceTest {
    @Autowired
    private FileDownloadUrlService fileDownloadService;

    @Test
    public void test() {

        Long psnFileId = 1100000001L;
        String psnFileShortUrl = fileDownloadService.getShortDownloadUrl(FileTypeEnum.PSN, psnFileId);
        String psnFileUrl = fileDownloadService.getDownloadUrl(FileTypeEnum.PSN, psnFileId);

        Long groupFileId = 100000000003807L;
        String grpFileShortUrl = fileDownloadService.getShortDownloadUrl(FileTypeEnum.GROUP, groupFileId);
        String grpFileUrl = fileDownloadService.getDownloadUrl(FileTypeEnum.GROUP, groupFileId);

        Long snsPubId = 1200000039622L;
        String snsFTShortUrl = fileDownloadService.getShortDownloadUrl(FileTypeEnum.SNS_FULLTEXT, snsPubId);
        String snsFTUrl = fileDownloadService.getDownloadUrl(FileTypeEnum.SNS_FULLTEXT, snsPubId);

        Long pdwhPubId = 29611L;
        String pdwhFTShortUrl = fileDownloadService.getShortDownloadUrl(FileTypeEnum.SNS_FULLTEXT, pdwhPubId);
        String pdwhFTUrl = fileDownloadService.getDownloadUrl(FileTypeEnum.SNS_FULLTEXT, pdwhPubId);

        StringBuilder builder = new StringBuilder();
        builder.append("\n+———————————————————————————————————————————————————————————————————");
        builder.append("———————————————————————————————————————————————————————————————————+\n");
        builder.append("| 文件类型\t编号\t\t\t站外下载短地址\t\t\t\t\t\t\t站内下载地址\n");
        builder.append("| PSN\t\t" + idFormat(psnFileId) + "\t" + psnFileShortUrl + "\t" + psnFileUrl + "\n");
        builder.append("| GROUP\t\t" + idFormat(groupFileId) + "\t" + grpFileShortUrl + "\t" + grpFileUrl + "\n");
        builder.append("| SNS_FULLTEXT\t" + idFormat(snsPubId) + "\t" + snsFTShortUrl + "\t" + snsFTUrl + "\n");
        builder.append("| PDWH_FULLTEXT\t" + idFormat(pdwhPubId) + "\t" + pdwhFTShortUrl + "\t" + pdwhFTUrl + "\n");
        builder.append("+———————————————————————————————————————————————————————————————————");
        builder.append("———————————————————————————————————————————————————————————————————+\n");
        System.out.println(builder.toString());
    }

    public String idFormat(Long id) {
        return String.format("%d%" + (18 - (id + "").length()) + "s", id, " ");
    }

    @Test
    public void test1() {

    }

}
