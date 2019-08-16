package com.smate.core.base.utils;

import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import com.smate.core.base.utils.exception.ImageInfoException;
import com.smate.core.base.utils.file.ArchiveFileUtil;
import com.smate.core.base.utils.image.im4java.gm.GMImageUtil;
import com.smate.core.base.utils.image.im4java.gm.GMOperation.DensityUnitEnum;
import com.smate.core.base.utils.image.im4java.gm.ImageInfo;
import com.smate.core.base.utils.image.im4java.im.IMImageUtil;

/**
 * @author houchuanjie
 * @date 2018年1月15日 下午2:16:53
 */
public class TestGMImageUtil {

    @Test
    public void testGMImageUtil() {
        try {
            ImageInfo info = GMImageUtil.getImageInfo(
                    "//filedev.scholarmate.com/data/scm_dev/scholarfile/upfile/00/7f/4d/6453c3a4c501428aa3ef26abaf91e258");
            /*ImageInfo info = new ImageInfo(
                    "//filedev.scholarmate.com/data/scm_dev/scholarfile/upfile/b5/9e/ec/e853195e095547f796492c66adbc147d.jpg");*/
            System.out.println("Type: " + info.getImageType());
            System.out.println("format: " + info.getImageFormat());
            System.out.println("geometry: " + info.getImageGeometry());
            System.out.println("width: " + info.getImageWidth());
            System.out.println("height: " + info.getImageHeight());
            System.out.println("depth: " + info.getImageDepth());
        } catch (ImageInfoException e) {
            System.err.println(e.getMessage());
        }
    }

    @Test
    public void testThumbnail() {
        if (!IMImageUtil.isAvailable()) {
            return;
        }
        LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue(100);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 200, 3000, TimeUnit.MILLISECONDS,
                linkedBlockingQueue);
        String srcPath = "D:/pdf/test.pdf[0]";
        String destPath = "D:/testimgs/test-";
        AtomicInteger result = new AtomicInteger(0);
        Instant start = Instant.now();
        for (int i = 0; i < 100; i++) {
            String dest = destPath + i + ".jpg";
            threadPoolExecutor.execute(() -> {
                try {
                    GMImageUtil.sample(72, 92, true, srcPath, dest);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                result.incrementAndGet();
            });
        }
        while (result.get() < 100) {
            ;
        }
        System.out.println("时间：" + Instant.now().minusMillis(start.toEpochMilli()).getEpochSecond() + "s");
    }

    @Test
    public void testIM() {
        try {
            String srcPath = "D:/pdf/test.pdf[0]";
            String dest = "D:/testimgs/test.jpg";
            GMImageUtil.thumbnail(72, 92, true, srcPath, dest);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConvert() throws Exception {
        if (!GMImageUtil.isAvailable()) {
            return;
        }
        LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue(100);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 200, 3000, TimeUnit.MILLISECONDS,
            linkedBlockingQueue);
        String srcPath = "D:/pdf/test.pdf[0]";
        String destPath = "D:/testimgs/test0.png";
        String destThumbPath = "D:/testimgs/test0_thumb.png";
        GMImageUtil.convert(100, 300, DensityUnitEnum.PixelsPerInch, srcPath, destPath);
        GMImageUtil.sample(ArchiveFileUtil.THUMBNAIL_WIDTH_72, ArchiveFileUtil.THUMBNAIL_HEIGHT_92, false, destPath,
            destThumbPath);
    }
}
