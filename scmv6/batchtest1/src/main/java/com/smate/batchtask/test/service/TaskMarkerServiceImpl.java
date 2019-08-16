package com.smate.batchtask.test.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.batchtask.test.model.TaskMessageList;
import com.smate.batchtask.test.model.dao.TaskMessageListDao;
import com.smate.core.base.utils.exception.BatchTaskException;

@Service("TaskMarkerService")
@Transactional(rollbackFor = Exception.class)
public class TaskMarkerServiceImpl implements TaskMarkerService{

	@Autowired
	TaskMessageListDao taskMessageListDao;
	


	@Override
	public Long getTaskMsgIdCountByStatus(Integer status)
			throws BatchTaskException {
		
		Long num = taskMessageListDao.getCountByStatus(status);
		
		return num;
	}



	@Override
	public List<TaskMessageList> getTaskMsgIdListByWeightAndNum(Integer num,
			String weight) throws BatchTaskException {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public List<TaskMessageList> getTaskMsgIdListByWeight(String weight)
			throws BatchTaskException {
		List<TaskMessageList> resultList = new ArrayList<TaskMessageList>();
		if(StringUtils.isBlank(weight)){
			return null;
		}else if(weight.equalsIgnoreCase("all")){
			
			resultList = taskMessageListDao.getAllTaskWithStatus0();
			
		}else if(weight.equalsIgnoreCase("A")){
			
			resultList = taskMessageListDao.getTaskByWeight("A");
			
		}else if(weight.equalsIgnoreCase("B")){
			
			resultList = taskMessageListDao.getTaskByWeight("B");
			
		}else if(weight.equalsIgnoreCase("C")){
			
			resultList = taskMessageListDao.getTaskByWeight("C");
			
		}else if(weight.equalsIgnoreCase("D")){
			
			resultList = taskMessageListDao.getTaskByWeight("D");
		}
		
		return resultList;
	}



	@Override
	public Long getTaskMsgIdListCountByWeight(String weight)
			throws BatchTaskException {
		Long count;
		
		count = taskMessageListDao.getTaskMsgIdCountByWeight(weight);
		
		return count;
	}



	@Override
	public List<TaskMessageList> arrangeTask() throws BatchTaskException {
		List<TaskMessageList> resultList = new ArrayList<TaskMessageList>();
		Integer numA;
		Integer numB = 30;
		Integer numC = 12;
		Integer numD = 2;
		List<TaskMessageList> listA = new ArrayList<TaskMessageList>();
		List<TaskMessageList> listB = new ArrayList<TaskMessageList>();
		List<TaskMessageList> listC = new ArrayList<TaskMessageList>();
		List<TaskMessageList> listD = new ArrayList<TaskMessageList>();
		
		//每次查询到需要查询A、B、C和D这4种数据，数据补充规则，A不足，B补上，依次类推...，如连D都不足，则有多少取多少
		listA = taskMessageListDao.getTaskMsgIdsByWeightAndNum("A", 156);
		numA = listA.size();
		
		if(numA < 156){
			numB = 30+156-numA;
		}
		listB = taskMessageListDao.getTaskMsgIdsByWeightAndNum("B", numB);
		numB = listB.size();
		
		if(numB < 30){
			numC = 12+30+156-numB-numA;
		}
		listC = taskMessageListDao.getTaskMsgIdsByWeightAndNum("C", numC);
		numC = listC.size();
		
		if(numC < 12){
			numD = 2+12+30+156-numC-numB-numA;
		}
		listD = taskMessageListDao.getTaskMsgIdsByWeightAndNum("D", numD);
		
		if(CollectionUtils.isNotEmpty(listA)){
			resultList.addAll(listA);
		}
		if(CollectionUtils.isNotEmpty(listB)){
			resultList.addAll(listB);
		}
		if(CollectionUtils.isNotEmpty(listC)){
			resultList.addAll(listC);
		}
		resultList.addAll(listD);
		
		return resultList;
	}



	@Override
	public void updateTaskStatus(TaskMessageList item) throws BatchTaskException {
			taskMessageListDao.save(item);
		
	}
	
	
	
}