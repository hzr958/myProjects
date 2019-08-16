package com.smate.test.file;

import java.io.IOException;

import org.junit.Test;

import net.coobird.thumbnailator.Thumbnails;

/**
 * Thumbnailator测试
 * 
 * @author houchuanjie
 * @date 2018年1月2日 下午2:22:32
 */
public class ThumbnailatorTest {

    @Test
    public void test() throws IOException {
        Thumbnails.of(
                "//filedev.scholarmate.com/data/scm_dev/scholarfile/upfile/b2/27/ab/3dc034d70847435487a3195500231e35.jpg")
                .size(72, 92).keepAspectRatio(false)
                .toFile("//filedev.scholarmate.com/data/scm_dev/scholarfile/upfile/b2/27/ab/3dc034d70847435487a3195500231e35_thumb.jpg");
        ;
    }

    @Test
    public void testSplit() {
        String fileName = "timg.jpg";
        String[] split = fileName.split("\\.");
        for (String string : split) {
            System.out.println(string);

        }
    }

}
