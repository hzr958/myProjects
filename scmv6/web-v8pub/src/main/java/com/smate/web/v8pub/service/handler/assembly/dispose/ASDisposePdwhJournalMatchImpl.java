package com.smate.web.v8pub.service.handler.assembly.dispose;

import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.web.v8pub.dom.JournalInfoBean;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.journal.BaseJournal2;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.journal.BaseJournalService;
import com.smate.web.v8pub.service.journal.BaseJournalTitleService;
import com.smate.web.v8pub.utils.PubLocaleUtils;

/**
 * pdwh库成果期刊匹配处理
 * 
 * @author YJ
 *
 *         2018年8月28日
 */
@Transactional(rollbackFor = Exception.class)
public class ASDisposePdwhJournalMatchImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private BaseJournalTitleService baseJournalTitleService;
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
        Long baseJnlId = null;
        // 是期刊类型
        JournalInfoBean journal = JacksonUtils.jsonObject(pub.pubTypeInfo.toJSONString(), JournalInfoBean.class);
        if (journal == null) {
          return null;
        }
        // 参数的处理
        String jname = XmlUtil.changeSBCChar(journal.getName());
        String jissn = XmlUtil.buildStandardIssn(XmlUtil.changeSBCChar(journal.getISSN()));
        if (StringUtils.isBlank(jname) || StringUtils.isBlank(jissn)) {
          return null;
        }
        // jname和jissn均不为空时才进行基准库期刊匹配
        baseJnlId = baseJournalTitleService.searchJournalMatchBaseJnlId(jname, jissn);
        if (NumberUtils.isNullOrZero(baseJnlId)) {
          return null;
        }
        BaseJournal2 b2 = baseJournalService.getBaseJournal2Title(baseJnlId);
        if (b2 == null) {
          return null;
        }
        boolean isChina = PubLocaleUtils.getLocale(journal.getName()).equals(Locale.CHINA);
        String name = isChina ? StringUtils.isNotBlank(b2.getTitleXx()) ? b2.getTitleXx() : b2.getTitleEn()
            : StringUtils.isNotBlank(b2.getTitleEn()) ? b2.getTitleEn() : b2.getTitleXx();
        journal.setName(name);
        journal.setJid(baseJnlId);
        pub.pubTypeInfo = JSONObject.parseObject(JacksonUtils.jsonObjectSerializer(journal));
      }
    } catch (Exception e) {
      logger.error("pdwh库成果匹配期刊出错！title=" + pub.title + ",insId=" + pub.insId, e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "pdwh库成果匹配期刊出错！title=" + pub.title, e);
    }
    return null;
  }

}
