package com.smate.batchtask.test.model.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.batchtask.test.model.TaskMessageList;
import com.smate.core.base.utils.data.HibernateDao;

@Repository
public class TaskMessageListDao extends HibernateDao<TaskMessageList, Long>{
	
	public void updateStatus(Long msgId){
		String hql = "update TaskMessageList t set t.status = 4 where t.messageId = ?";
		
		super.createQuery(hql, msgId).executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public TaskMessageList getOneMsg(){
		Integer fixedSize = 200;
	    List<TaskMessageList> result = new ArrayList<TaskMessageList>();
		String hql = "from TaskMessageList t where t.status = 1";
		result = (List<TaskMessageList>) super.createQuery(hql).setMaxResults(fixedSize).list();
		if(CollectionUtils.isNotEmpty(result)){
			return result.get(0);
		}else{
			return null;
		}
		
	}
	
	
	public Long getCountByStatus(Integer status){
		String hql = "select count(1) from TaskMessageList t where t.status = ?";
		Long count = super.findUnique(hql,status);
		return count;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<TaskMessageList> getTaskMsgIdsByWeightAndNum(String weight, Integer num){
		
		String hql = "from TaskMessageList t where t.weight = ? and t.status=0";
		
		return super.createQuery(hql, weight).setMaxResults(num).list();
		
	}
	
	//获取status为0的所有任务
	@SuppressWarnings("unchecked")
	public List<TaskMessageList> getAllTaskWithStatus0(){
		String hql = "from TaskMessageList t where t.status=0";
		return super.createQuery(hql).list();
	}
	
	//获取status为1的所有任务
	@SuppressWarnings("unchecked")
	public List<TaskMessageList> getAllTaskWithStatus1(){
		String hql = "from TaskMessageList t where t.status=1";
		return super.createQuery(hql).list();
	}
	
	//获取status为0的不同权重任务数
	public Long getTaskMsgIdCountByWeight(String weight){
		
		String hql = "select count(1) from TaskMessageList t where t.status = 0 and t.weight = ?";
		Long count = super.findUnique(hql, weight);
		return count;
	}
	
	
	public List<TaskMessageList> getTaskByWeight(String weight){
		
		String hql = "from TaskMessageList t where t.status = 0 and t.weight = ?";
		return super.findUnique(hql, weight);
		
	}
	
}