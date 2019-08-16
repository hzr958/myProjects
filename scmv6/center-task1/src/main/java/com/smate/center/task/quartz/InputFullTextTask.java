package com.smate.center.task.quartz;

import com.smate.center.task.base.TaskAbstract;
import com.smate.center.task.exception.InputFullTextTaskException;
import com.smate.center.task.service.pdwh.quartz.InputPubFulltextService;
import com.smate.core.base.utils.image.im4java.gm.GMImageUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;

/**
 * 基准库成果全文匹配导入任务
 *
 * @author lj
 */
public class InputFullTextTask extends TaskAbstract {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  // 不处理的文件夹
  private String skipDir;
  // 配置各个库的文件目录
  @Value("${source.eiDir.name}")
  private String eiDir;

  @Value("${source.isiDir.name}")
  private String isiDir;

  @Value("${source.rainPatDir.name}")
  private String rainPatDir;

  @Value("${source.cnkiDir.name}")
  private String cnkiDir;

  @Value("${source.doiDir.name}")
  private String doiDir;

  @Value("${source.pubid.cnki.name}")
  private String cnkiIdDir;

  @Value("${source.pubid.wanfang.name}")
  private String wanfangIdDir;

  @Value("${source.oalibDir.name}")
  private String oalibDir;

  public InputFullTextTask() {
    super();
  }

  public InputFullTextTask(String beanName) {
    super(beanName);
  }

  @Autowired
  private InputPubFulltextService inputPubFulltextService;

  public void Execute() throws InputFullTextTaskException {
    try {
      if (!super.isAllowExecution()) {
        return;
      }
      File readFile = inputPubFulltextService.readFile();// 获取根目录
      // 递归读取处理文件
      this.readerFile(readFile);

    } catch (Exception e) {
      logger.error("InputFulltextTask运行异常", e);
    }
  }

  /**
   * 递归方法
   *
   * @param file
   * @throws Exception
   */

  private void readerFile(File file) throws Exception {
    // 方便不重启服务上传文件到临时目录待处理。
    if (file.isDirectory() && file.getName().startsWith(skipDir)) {
      return;
    }
    if (!file.isDirectory()) {
      String fileName = file.getName();
      // 跳过处理过的文件
      if (!file.getName().startsWith("finish_") && !file.getName().startsWith("error_")
          && !file.getName().startsWith("failed_")) {
        if (isCorrectPdfFile(file)) {
          String dir = checkFileDir(file);
          try {
            switch (dir) {
              case "eiDir":
                inputPubFulltextService.dealEiFile(file.getPath());// 如果读取的是ei里面的文件
                break;
              case "isiDir":
                inputPubFulltextService.dealIsiFile(file.getPath());// 读取的是isi文件夹中的文件
                break;
              case "rainPatDir":
                inputPubFulltextService.dealRainPatFile(file.getPath());// 读取rainpat文件夹中的文件
                break;
              case "cnkiDir":
                inputPubFulltextService.dealCnkiFile(file.getPath());// 读取cnki文件夹中的文件
                break;
              case "doiDir":
                inputPubFulltextService.dealDoiNameFile(file.getPath());// 读取doi文件夹中的文件
                break;
              case "oalibDir":
                inputPubFulltextService.dealOalibNameFile(file.getPath());// 读取oalibDir文件夹中的文件
                break;

              default:
                if (!fileName.startsWith("error_")) {
                  changeFailed(file, fileName);// 文件名不匹配执行
                }
                break;
            }
          } catch (Exception e) {
            changeFailed(file, fileName);
          }

        }
      }
    } else {
      String[] list = file.list();
      String path = file.getPath();
      File tmpfile = null;
      for (String string : list) {
        tmpfile = new File(path + "//" + string);
        readerFile(tmpfile);
      }

    }

  }

  /**
   * //检查pdf文件是否完整
   *
   * @param file
   * @return
   */
  public boolean isCorrectPdfFile(File file) {

    // 检查pdf文件是否完整
    try {
      GMImageUtil.getImageInfo(file.getAbsolutePath());
    } catch (Exception e) {
      changeFailed(file, file.getName());
      return false;
    }
    return true;
  }

  /**
   * 判断pdf文件读取来源
   *
   * @param file
   * @return
   */
  public String checkFileDir(File file) {

    String fileName = file.getName();
    String parent = file.getParent();

    if (parent.indexOf(eiDir) > -1 && fileName.matches("^[0-9A-Z]+\\.pdf$")) {
      return "eiDir";
    }
    // 标准的格式 isi_pub_id 全是数字 isi_source_id 大写字母 或数据
    if (parent.indexOf(isiDir) > -1 && fileName.matches("^[0-9A-Z]+\\.pdf$")) {
      return "isiDir";
    }
    if (parent.indexOf(rainPatDir) > -1 && fileName.contains("-")) {
      return "rainPatDir";
    }
    if (parent.indexOf(cnkiDir) > -1 && fileName.endsWith(".pdf")) {
      return "cnkiDir";
    }
    if (parent.indexOf(doiDir) > -1 && fileName.endsWith(".pdf")) {
      return "doiDir";
    }
    if (parent.indexOf(wanfangIdDir) > -1 && fileName.endsWith(".pdf")) {
      return "wanfangIdDir";
    }
    if (parent.indexOf(cnkiIdDir) > -1 && fileName.endsWith(".pdf")) {
      return "cnkiIdDir";
    }
    if (parent.indexOf(oalibDir) > -1 && fileName.endsWith(".pdf")) {
      return "oalibDir";
    }
    return null;
  }

  /**
   * 标记为处理失败
   *
   * @param file
   * @param fileName
   * @throws Exception
   */
  public void changeFailed(File file, String fileName) {
    File newFile = new File(file.getPath().replace(fileName, "") + "failed" + "_" + fileName);
    try {
      FileUtils.moveFile(file, newFile);
    } catch (Exception e) {
      logger.error("文件标记处理失败(存在failed_开头的重名文件，无法重命名，将尝试命名为failed_x2_开头)：" + "path:" + file.getPath(), e);
      retryChangeFailed(file, fileName);
    }
  }

  public void retryChangeFailed(File file, String fileName) {
    File newFile = new File(file.getPath().replace(fileName, "") + "failed_x2" + "_" + fileName);
    try {
      FileUtils.moveFile(file, newFile);
    } catch (Exception e) {
      logger.error("文件命名为failed_x2_开头处理失败,请查找是否存在重复文件：" + "path:" + file.getPath(), e);
    }
  }

  public String getSkipDir() {
    return skipDir;
  }

  public void setSkipDir(String skipDir) {
    this.skipDir = skipDir;
  }

}
