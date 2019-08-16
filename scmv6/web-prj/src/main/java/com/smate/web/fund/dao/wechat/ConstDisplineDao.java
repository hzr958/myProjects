package com.smate.web.fund.dao.wechat;

import com.smate.core.base.consts.model.ConstDiscipline;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.prj.form.wechat.FundWeChatForm;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ConstDisplineDao extends SnsHibernateDao<ConstDiscipline, Long> {
  /*
   * 查找fundId对应的学科
   */

  @SuppressWarnings("unchecked")
  public String querydiscipline(FundWeChatForm form) throws Exception {
    String hql =
        "select cf.zhName as zhName,cf.enName as enName from ConstDiscipline cf,ConstFundCategoryDis cd where  cd.categoryId=:categoryId  and cd.disId=cf.id";
    List<Object> list = super.createQuery(hql)
        .setParameter("categoryId", NumberUtils.toLong(ServiceUtil.decodeFromDes3(form.getDes3FundId())))
        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    Map<Object, Object> m = new HashMap<Object, Object>();
    String zhName = "";
    String enName = "";
    for (int i = 0; i < list.size(); i++) {
      m = (Map<Object, Object>) list.get(i);
      ConstDiscipline f = new ConstDiscipline();
      zhName = zhName + (String) m.get("zhName");
      enName = enName + (String) m.get("enName");
      if (list.size() > i + 1)
        zhName = zhName + ",";
      enName = enName + ",";
    }
    return zhName;
  }
}
