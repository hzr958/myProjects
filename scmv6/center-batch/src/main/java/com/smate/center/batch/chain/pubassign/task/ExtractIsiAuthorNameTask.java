package com.smate.center.batch.chain.pubassign.task;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.rol.pubassign.PubAssignXmlService;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.string.IrisNumberUtils;

/**
 * 展开isi成果作者姓名.
 * 
 * @author liqinghua
 * 
 */
public class ExtractIsiAuthorNameTask implements IPubAssignXmlTask {

  private final String name = "ExtractIsiAuthorNameTask";
  @Autowired
  private PubAssignXmlService pubAssignXmlService;

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean can(PubXmlDocument xmlDocument, PubXmlProcessContext context) {
    return context.isImport() && context.isIsiImport();
  }

  @SuppressWarnings("rawtypes")
  @Override
  public boolean run(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {

    List authorList = xmlDocument.getPubAuthorList();
    if (authorList != null && authorList.size() > 0) {
      for (int i = 0; i < authorList.size(); i++) {
        Element author = (Element) authorList.get(i);
        String abbrName = StringUtils.trimToEmpty(author.attributeValue("abbr_name"));
        String fullName = StringUtils.trimToEmpty(author.attributeValue("full_name"));
        Integer seqNo = IrisNumberUtils.createInteger(author.attributeValue("seq_no"));

        // 清除其他符号
        abbrName = XmlUtil.getCleanAuthorName(abbrName);
        fullName = XmlUtil.getCleanAuthorName(fullName);
        if (StringUtils.isBlank(abbrName)) {
          continue;
        }
        if (abbrName.equalsIgnoreCase(fullName)) {
          fullName = null;
        } else if (StringUtils.isNotBlank(fullName)) {
          // 去除所有空格，简称如果等于全称，则设置全称为空
          String wordAbbrName = abbrName.replaceAll("\\s+", "");
          String wordFullName = fullName.replaceAll("\\s+", "");
          if (wordAbbrName.equalsIgnoreCase(wordFullName)) {
            fullName = null;
          }
        }
        // 查找是否有本单位，如果没有，随机取一个单位
        Long insId = ExtractIsiAuthorNameTask.getAuthorInsId(author, context);
        this.pubAssignXmlService.savePubAuthor(context.getCurrentPubId(), context.getCurrentInsId(), abbrName, fullName,
            insId, seqNo);
      }
    } else {
      return false;
    }
    return true;
  }

  /**
   * 查找是否有本单位，如果没有，随机取一个单位；如果存在地址，但是地址未匹配到任何机构，则返回1；如果不存在地址，则返回2.
   * 
   * @param author
   * @param context
   * @return
   * @throws Exception
   */
  public static Long getAuthorInsId(Element author, PubXmlProcessContext context) throws Exception {
    // 查找是否有本单位，如果没有，随机取一个单位
    Long insId = IrisNumberUtils.createLong(author.attributeValue("ins_id"));

    // Integer insCount =
    // IrisNumberUtils.createInteger(StringUtils.trimToEmpty(author.attributeValue("ins_count")));
    // insCount = insCount == null ? 0 : insCount;
    // for (int j = 0; j < insCount; j++) {
    // insId =
    // IrisNumberUtils.createLong(StringUtils.trimToEmpty(author.attributeValue("ins_id"
    // + (j + 1))));
    // if (context.getCurrentInsId().equals(insId)) {
    // break;
    // }
    // }
    if (insId == null && StringUtils.isNotBlank(author.attributeValue("addr"))) {
      return 1L;
    } else if (insId == null) {
      return 2L;
    }
    return insId;
  }
}
