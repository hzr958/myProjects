package com.smate.center.task.service.tokenizer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.smate.center.task.dao.sns.psn.TokenizerPersonDao;
import com.smate.center.task.dao.sns.quartz.ConstPositionDao;
import com.smate.center.task.dao.sns.quartz.ConstRegionDao;
import com.smate.center.task.dao.sns.quartz.DiscKeywordDao;
import com.smate.center.task.dao.sns.quartz.KeywordsDicDao;
import com.smate.center.task.dao.sns.quartz.KeywordsHotDao;
import com.smate.center.task.exception.TokenizerException;
import com.smate.core.base.utils.dao.security.InsPortalDao;
import com.smate.core.base.utils.file.FileUtils;

/**
 * 分词器词库服务实现
 * 
 * @author zk
 *
 */
@Service("tokenizerThesauriService")
@Transactional(rollbackOn = Exception.class)
public class TokenizerThesauriServiceImpl implements TokenizerThesauriService {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  private Integer BATCHSIZE = 1000;
  private String tokenizerFolder = "/tokenizer/";
  private String tokenizerFile = "snsThesauri.txt";
  @Value("${file.root}")
  private String baseFolder;
  @Autowired
  private TokenizerPersonDao tokenizerPersonDao;
  @Autowired
  private InsPortalDao insPortalDao;
  @Autowired
  private KeywordsDicDao keywordsDicDao;
  @Autowired
  private DiscKeywordDao discKeywordDao;
  @Autowired
  private KeywordsHotDao keywordsHotDao;
  @Autowired
  private ConstRegionDao constRegionDao;
  @Autowired
  private ConstPositionDao constPositionDao;

  @Override
  public void createThesauri() {
    if (StringUtils.isBlank(baseFolder)) {
      logger.error("创建分词器词库出错，无基础目录");
      return;
    }
    try {
      if (FileUtils.makeFolder(this.baseFolder + this.tokenizerFolder)) {
        if (!FileUtils.makeFile(this.baseFolder + this.tokenizerFolder, this.tokenizerFile)) {
          return;
        }
      } else {
        return;
      }
      this.handlePerson();
      this.handleInsPortal();
      this.handleKeywordDic();
      this.handleDiscKeyword();
      this.handleKwHot();
      this.handleConstRegion();
      this.handleConstPosition();
    } catch (Exception e) {
      logger.error("分词器额外词库出错", e);
    }
  }

  /**
   * 处理人员
   */
  private void handlePerson() throws TokenizerException {
    FileWriter fileWriter = null;
    try {
      fileWriter = new FileWriter(new File(this.baseFolder + this.tokenizerFolder + this.tokenizerFile), true);
      Integer pageNo = 1;
      while (true) {
        List<String> psnNameList = tokenizerPersonDao.findUserNameByBatchSize(pageNo, BATCHSIZE);
        if (CollectionUtils.isEmpty(psnNameList)) {
          break;
        }
        fileWriter.write(ThesauriStringUtils.toString(psnNameList));
        fileWriter.flush();
        pageNo++;
      }
      logger.info("分词器额外词库处理人员---" + pageNo * BATCHSIZE);
    } catch (Exception e) {
      logger.error("分词器额外词库处理人员时出错", e);
    } finally {
      if (fileWriter != null) {
        try {
          fileWriter.close();
        } catch (IOException e) {
          fileWriter = null;
        }
      }
    }
  }

  /**
   * 处理单位名称
   * 
   * @throws TokenizerException
   */
  private void handleInsPortal() throws TokenizerException {
    FileWriter fileWriter = null;
    try {
      fileWriter = new FileWriter(new File(this.baseFolder + this.tokenizerFolder + this.tokenizerFile), true);
      Integer pageNo = 1;
      while (true) {
        List<String> insZhTitleList = insPortalDao.findZhTitle(pageNo, BATCHSIZE);
        if (CollectionUtils.isEmpty(insZhTitleList)) {
          break;
        }

        fileWriter.write(ThesauriStringUtils.toString(insZhTitleList));
        fileWriter.flush();
        pageNo++;
      }
      logger.info("分词器额外词库处理单位名称---" + pageNo * BATCHSIZE);
    } catch (Exception e) {
      logger.error("分词器额外词库处理单位名称时出错", e);
    } finally {
      if (fileWriter != null) {
        try {
          fileWriter.close();
        } catch (IOException e) {
          fileWriter = null;
        }
      }
    }
  }

  /**
   * 处理关键词字典
   * 
   * @throws TokenizerException
   */
  private void handleKeywordDic() throws TokenizerException {
    FileWriter fileWriter = null;
    try {
      fileWriter = new FileWriter(new File(this.baseFolder + this.tokenizerFolder + this.tokenizerFile), true);
      Integer pageNo = 1;
      while (true) {
        List<String> kwDicList = keywordsDicDao.findKeywordTextByTwoType(pageNo, BATCHSIZE);
        if (CollectionUtils.isEmpty(kwDicList)) {
          break;
        }

        fileWriter.write(ThesauriStringUtils.toString(kwDicList));
        fileWriter.flush();
        pageNo++;
      }
      logger.info("分词器额外词库处理关键词字典---" + pageNo * BATCHSIZE);
    } catch (Exception e) {
      logger.error("分词器额外词库处理关键词字典时出错", e);
    } finally {
      if (fileWriter != null) {
        try {
          fileWriter.close();
        } catch (IOException e) {
          fileWriter = null;
        }
      }
    }
  }

  /**
   * 处理关键词
   * 
   * @throws TokenizerException
   */
  private void handleDiscKeyword() throws TokenizerException {
    FileWriter fileWriter = null;
    try {
      fileWriter = new FileWriter(new File(this.baseFolder + this.tokenizerFolder + this.tokenizerFile), true);
      Integer pageNo = 1;
      while (true) {
        List<String> kwDicList = discKeywordDao.findDiscKwZhkey(pageNo, BATCHSIZE);
        if (CollectionUtils.isEmpty(kwDicList)) {
          break;
        }

        fileWriter.write(ThesauriStringUtils.toString(kwDicList));
        fileWriter.flush();
        pageNo++;
      }
      logger.info("分词器额外词库处理关键词---" + pageNo * BATCHSIZE);
    } catch (Exception e) {
      logger.error("分词器额外词库处理关键词时出错", e);
    } finally {
      if (fileWriter != null) {
        try {
          fileWriter.close();
        } catch (IOException e) {
          fileWriter = null;
        }
      }
    }
  }

  /**
   * 处理关键词
   * 
   * @throws TokenizerException
   */
  private void handleKwHot() throws TokenizerException {
    FileWriter fileWriter = null;
    try {
      fileWriter = new FileWriter(new File(this.baseFolder + this.tokenizerFolder + this.tokenizerFile), true);
      Integer pageNo = 1;
      while (true) {
        List<String> hotKwtxtList = keywordsHotDao.findKwtxt(pageNo, BATCHSIZE);
        if (CollectionUtils.isEmpty(hotKwtxtList)) {
          break;
        }
        fileWriter.write(ThesauriStringUtils.toString(hotKwtxtList));
        fileWriter.flush();
        pageNo++;
      }
      logger.info("分词器额外词库处理关键词---" + pageNo * BATCHSIZE);
    } catch (Exception e) {
      logger.error("分词器额外词库处理关键词时出错", e);
    } finally {
      if (fileWriter != null) {
        try {
          fileWriter.close();
        } catch (IOException e) {
          fileWriter = null;
        }
      }
    }
  }

  /**
   * 处理地区名称
   * 
   * @throws TokenizerException
   */
  private void handleConstRegion() throws TokenizerException {
    FileWriter fileWriter = null;
    try {
      fileWriter = new FileWriter(new File(this.baseFolder + this.tokenizerFolder + this.tokenizerFile), true);
      Integer pageNo = 1;
      while (true) {
        List<String> crList = constRegionDao.findZhName(pageNo, BATCHSIZE);
        if (CollectionUtils.isEmpty(crList)) {
          break;
        }
        crList = ThesauriStringUtils.excludeRegionStr(crList);
        fileWriter.write(ThesauriStringUtils.toString(crList));
        fileWriter.flush();
        pageNo++;
      }
      logger.info("分词器额外词库处理地区名称---" + pageNo * BATCHSIZE);
    } catch (Exception e) {
      logger.error("分词器额外词库处理地区名称时出错", e);
    } finally {
      if (fileWriter != null) {
        try {
          fileWriter.close();
        } catch (IOException e) {
          fileWriter = null;
        }
      }
    }
  }

  /**
   * 处理职称名称
   * 
   * @throws TokenizerException
   */
  private void handleConstPosition() throws TokenizerException {
    FileWriter fileWriter = null;
    try {
      fileWriter = new FileWriter(new File(this.baseFolder + this.tokenizerFolder + this.tokenizerFile), true);
      Integer pageNo = 1;
      while (true) {
        List<String> positionNameList = constPositionDao.findPositionName(pageNo, BATCHSIZE);
        if (CollectionUtils.isEmpty(positionNameList)) {
          break;
        }
        fileWriter.write(ThesauriStringUtils.toString(positionNameList));
        fileWriter.flush();
        pageNo++;
      }
      logger.info("分词器额外词库处理职称名称---" + pageNo * BATCHSIZE);
    } catch (Exception e) {
      logger.error("分词器额外词库处理职称名称时出错", e);
    } finally {
      if (fileWriter != null) {
        try {
          fileWriter.close();
        } catch (IOException e) {
          fileWriter = null;
        }
      }
    }
  }

  public String getBaseFolder() {
    return baseFolder;
  }

  public void setBaseFolder(String baseFolder) {
    this.baseFolder = baseFolder;
  }

}
