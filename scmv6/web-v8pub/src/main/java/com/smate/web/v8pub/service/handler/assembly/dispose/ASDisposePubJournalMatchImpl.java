package com.smate.web.v8pub.service.handler.assembly.dispose;

import java.util.Locale;
import java.util.Map;

import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.smate.core.base.pub.enums.PubSnsRecordFromEnum;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.v8pub.dom.JournalInfoBean;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.journal.BaseJournalPO;
import com.smate.web.v8pub.po.journal.JournalPO;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.journal.BaseJournalService;
import com.smate.web.v8pub.service.journal.JournalService;
import com.smate.web.v8pub.utils.PubLocaleUtils;

/**
 * sns库成果期刊匹配处理
 * 
 * @author YJ
 *
 *         2018年8月6日
 */
@Transactional(rollbackFor = Exception.class)
public class ASDisposePubJournalMatchImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private JournalService journalService;
  @Autowired
  private BaseJournalService baseJournalService;

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    try {
      if (pub.pubType.intValue() == PublicationTypeEnum.JOURNAL_ARTICLE) {
        JournalInfoBean journal = JacksonUtils.jsonObject(pub.pubTypeInfo.toJSONString(), JournalInfoBean.class);
        if (journal == null) {
          return null;
        }
        // 匹配sns库的journal普通期刊表，journal表，获取JournalSnsPO对象
        JournalPO journalPO = getJournalPO(pub, journal);
        // 根据普通期刊表对象中的matchBaseJnlId字段，可以知道该期刊是否匹配到标准期刊，有值则匹配到
        if (journalPO != null) {
          // 将标准期刊的数据替换当前的期刊数据
          if (journalPO.getMatchBaseJnlId() != null) {
            BaseJournalPO b = baseJournalService.getById(journalPO.getMatchBaseJnlId());
            if (b != null) {
              if (pub.recordFrom == PubSnsRecordFromEnum.IMPORT_FORM_FILE
                  || pub.recordFrom == PubSnsRecordFromEnum.IMPORT_FROM_PDWH
                  || pub.recordFrom == PubSnsRecordFromEnum.IMPORT_FROM_DATABASE) {
                // 联邦检索，文件导入，基准库导入匹配基准期刊，匹配上则用标准期刊的信息
                String name = Locale.CHINA.equals(PubLocaleUtils.getLocale(journal.getName()))
                    ? StringUtils.isNotBlank(b.getTitleXx()) ? b.getTitleXx() : b.getTitleEn()
                    : StringUtils.isNotBlank(b.getTitleEn()) ? b.getTitleEn() : b.getTitleXx();
                journal.setName(name);
              }
              journal.setISSN(b.getPissn());
            }
          }
          journal.setJid(journalPO.getId());
        }
        pub.pubTypeInfo = JSONObject.parseObject(JacksonUtils.jsonObjectSerializer(journal));
        logger.debug("sns库期刊匹配完成");
      }
    } catch (Exception e) {
      logger.error("sns库成果匹配期刊出错！", e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "sns库成果匹配期刊出错！", e);
    }
    return null;
  }

  private JournalPO getJournalPO(PubDTO pub, JournalInfoBean j) {
    JournalPO journalPO = null;
    if (StringUtils.isEmpty(j.getName())) {
      return null;
    }
    // 进行长度的处理
    j.setName(StringUtils.substring(j.getName(), 0, 250));
    j.setISSN(StringUtils.substring(j.getISSN(), 0, 100));
    // 参数的处理
    String jname = XmlUtil.changeSBCChar(j.getName());
    String jissn = XmlUtil.buildStandardIssn(XmlUtil.changeSBCChar(j.getISSN()));
    journalPO = journalService.findJournalByNameIssn(jname, jissn, pub.psnId);

    if (journalPO == null) {
      // 1.3 都没期刊对象信息，说明这条期刊是新的期刊，那么新增期刊记录
      // 1.3.1 进行标准期刊的匹配
      Long baseJid = baseJournalService.searchJnlMatchBaseJnlId(jname, jissn);
      // 1.3.2 保存期刊对象数据
      journalPO = new JournalPO();
      journalPO.setAddPsnId(pub.psnId);
      journalPO.setIssn(j.getISSN());
      if (Locale.CHINA.equals(PubLocaleUtils.getLocale(j.getName()))) {
        // 中文
        journalPO.setZhName(j.getName());
        journalPO.setZhNameAlias(j.getName().toLowerCase(Locale.CHINA));
      } else {
        journalPO.setEnName(j.getName());
        journalPO.setEnameAlias(j.getName().toLowerCase(Locale.US));
      }
      journalPO.setMatchBaseJnlId(baseJid);
      journalPO.setMatchBaseStatus(1);
      journalService.save(journalPO);
    }
    return journalPO;
  }

}
