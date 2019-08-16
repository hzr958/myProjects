package com.smate.center.batch.dao.tmppdwh;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.tmppdwh.NsfcKwsCotfTwo;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;

@Repository
public class NsfcKwsCotfTwoDao extends HibernateDao<NsfcKwsCotfTwo, Long> {

    @Override
    public DBSessionEnum getDbSession() {
        return DBSessionEnum.TMPPDWH;
    }

    public void insertIntoNsfcCotfLength2(String discode, String kwsStr, String kw1, String kw2, Integer cotf,
            Integer language) {
        String sql = "insert into nsfc_kw_cotf_two values (seq_nsfc_kw_wtf.nextval, ?,?,?,?,?,?)";
        super.update(sql, new Object[] {discode, kwsStr, kw1, kw2, cotf, language});
    }

    public void insertIntoNsfcCotfLength3(String discode, String kwsStr, String kw1, String kw2, String kw3,
            Integer cotf, Integer language) {
        String sql = "insert into nsfc_kw_cotf_three values (seq_nsfc_kw_wtf.nextval, ?,?,?,?,?,?,?)";
        super.update(sql, new Object[] {discode, kwsStr, kw1, kw2, kw3, cotf, language});
    }

    public void insertIntoNsfcCotfLength4(String discode, String kwsStr, String kw1, String kw2, String kw3, String kw4,
            Integer cotf, Integer language) {
        String sql = "insert into nsfc_kw_cotf_four values (seq_nsfc_kw_wtf.nextval, ?,?,?,?,?,?,?,?)";
        super.update(sql, new Object[] {discode, kwsStr, kw1, kw2, kw3, kw4, cotf, language});
    }

    public void insertIntoNsfcCotfLength5(String discode, String kwsStr, String kw1, String kw2, String kw3, String kw4,
            String kw5, Integer cotf, Integer language) {
        String sql = "insert into nsfc_kw_cotf_five values (seq_nsfc_kw_wtf.nextval, ?,?,?,?,?,?,?,?,?)";
        super.update(sql, new Object[] {discode, kwsStr, kw1, kw2, kw3, kw4, kw5, cotf, language});
    }

}
