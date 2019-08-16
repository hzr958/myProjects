package com.smate.center.job.business.psn.dao;

import com.smate.center.job.business.psn.model.PersonPmName;
import com.smate.core.base.utils.data.SnsHibernateDao;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * PERSON人员姓名变换记录表
 * 
 * @author LIJUN
 * @date 2018年3月20日
 */
@Repository
public class PersonPmNameDao extends SnsHibernateDao<PersonPmName, Long> {

	@SuppressWarnings("unchecked")
	public List<PersonPmName> getPsnNameRecByPsnId(Long psnId) {
		String hql = "from PersonPmName  where psnId=:psnId";
		return super.createQuery(hql).setParameter("psnId", psnId).list();

	}

	/**
	 * 删除系统生成的别名
	 * 
	 * @param psnId
	 * @author LIJUN
	 * @date 2018年5月7日
	 */
	public void deleteName(Long psnId) {
		String hql = "delete from PersonPmName  where psnId=:psnId and nameSource=1";
		super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();
	}

	/**
	 * 根据人名获取psnId
	 * 
	 * @param name
	 * @return
	 * @author LIJUN
	 * @date 2018年3月20日
	 */
	@SuppressWarnings("unchecked")
	public List<PersonPmName> getPsnByName(String name) {
		String hql = "from PersonPmName where name=:name";
		return super.createQuery(hql).setParameter("name", name).list();

	}

	/**
	 * 保存系统生成用户名称前缀
	 * 
	 * @param prefix
	 * @param psnId
	 */
	public void savePrefixName(Set<String> prefixs, Long psnId, Long insId) {
		if (prefixs == null || prefixs.size() == 0) {
			return;
		}
		super.createQuery("delete from PersonPmName where psnId = ? and type = 2 and nameSource=1 ", psnId)
				.executeUpdate();
		for (String prefix : prefixs) {
			PersonPmName name = new PersonPmName(psnId, insId, prefix, 2, 1);
			super.save(name);
		}
	}

	/**
	 * 保存系统生成用户名称简写
	 * 
	 * @param initName
	 * @param psnId
	 */
	public void saveInitName(Set<String> initNames, Long psnId, Long insId) {

		if (initNames == null || initNames.size() == 0) {
			return;
		}
		super.createQuery("delete from PersonPmName where psnId = ? and type = 5 and nameSource=1", psnId)
				.executeUpdate();
		for (String initName : initNames) {
			PersonPmName name = new PersonPmName(psnId, insId, initName, 5, 1);
			super.save(name);
		}
	}

	/**
	 * 保存系统生成用户全称.
	 * 
	 * @param fullNames
	 * @param psnId
	 */
	public void saveFullName(Set<String> fullNames, Long psnId, Long insId) {

		if (CollectionUtils.isEmpty(fullNames)) {
			return;
		}
		super.createQuery("delete from PersonPmName where psnId = ? and type = 1 and nameSource=1", psnId)
				.executeUpdate();
		for (String fullName : fullNames) {
			PersonPmName name = new PersonPmName(psnId, insId, fullName, 1, 1);
			super.save(name);
		}
	}

	@SuppressWarnings("unchecked")
	public List<String> batchGetName(int batchSize, int index) {
		String hql = "select distinct(name) from PersonPmName ";
		return super.createQuery(hql).setMaxResults(batchSize).setFirstResult(batchSize * (index - 1)).list();

	}

	/**
	 * 保存用户输入的姓名 中文和英文
	 * 
	 * @param zhname
	 * @param ename
	 * @param personId
	 * @param insId
	 * @author LIJUN
	 * @date 2018年5月24日
	 */
	public void saveUserInputName(String zhname, String ename, Long psnId, Long insId) {
		super.createQuery("delete from PersonPmName where psnId = ?  and nameSource=3", psnId).executeUpdate();
		if (StringUtils.isNotBlank(zhname)) {
			super.save(new PersonPmName(psnId, insId, zhname.toLowerCase(), 1, 3));
		}
		if (StringUtils.isNotBlank(ename)) {
			super.save(new PersonPmName(psnId, insId, ename.toLowerCase(), 1, 3));
		}

	}

	/**
	 * 获取用户别名(去重复)
	 * 
	 * @param personId
	 * @return
	 * @author LIJUN
	 * @date 2018年5月28日
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAllPsnName(Long personId) {
		String hql = "select distinct(name) from PersonPmName  where psnId =:psnId";
		return super.createQuery(hql).setParameter("psnId", personId).list();
	}

	/**
	 * 根据人名和insId获取记录（联合人员工作经历和教育经历查询）
	 *
	 * @param name
	 * @return
	 * @author LIJUN
	 * @date 2018年3月20日
	 */
	@SuppressWarnings("unchecked")
	public List<PersonPmName> getPsnByNameAndInsId(String name,Long insId) {
		String hql = "from PersonPmName t where  t.name=:name and (t.insId=:insId or exists (select 1 from WorkHistory t2 where t2.psnId=t.psnId and t2.insId=:insId) or exists(select 1 from EducationHistory t3 where t3.psnId=t.psnId and t3.insId=:insId))";
		return super.createQuery(hql).setParameter("name", name).setParameter("insId",insId).list();

	}
}
