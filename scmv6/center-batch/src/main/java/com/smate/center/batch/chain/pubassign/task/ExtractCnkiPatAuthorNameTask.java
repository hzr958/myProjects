package com.smate.center.batch.chain.pubassign.task;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.rol.pubassign.PubAssignCnkiPatXmlService;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.string.IrisNumberUtils;

/**
 * 展开CnkiPat成果作者姓名.
 * 
 * @author liqinghua
 * 
 */
public class ExtractCnkiPatAuthorNameTask implements IPubAssignXmlTask {

  private final String name = "ExtractCnkiPatAuthorNameTask";
  @Autowired
  private PubAssignCnkiPatXmlService pubAssignCnkiPatXmlService;

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean can(PubXmlDocument xmlDocument, PubXmlProcessContext context) {
    return context.isImport();
  }

  @SuppressWarnings("rawtypes")
  @Override
  public boolean run(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {

    List authorList = xmlDocument.getPubAuthorList();
    if (authorList != null && authorList.size() > 0) {
      for (int i = 0; i < authorList.size(); i++) {
        Element author = (Element) authorList.get(i);
        String auname = StringUtils.trimToEmpty(author.attributeValue("name"));
        Integer seqNo = IrisNumberUtils.createInteger(author.attributeValue("seq_no"));
        // 清除其他符号
        auname = XmlUtil.getCleanAuthorName(auname);
        if (StringUtils.isBlank(auname)) {
          continue;
        }
        // 查找是否有本单位，如果没有，随机取一个单位
        Long insId = ExtractIsiAuthorNameTask.getAuthorInsId(author, context);
        this.pubAssignCnkiPatXmlService.savePubAuthor(context.getCurrentPubId(), context.getCurrentInsId(), auname,
            insId, seqNo);
      }
    } else {
      return false;
    }
    return true;
  }
}
