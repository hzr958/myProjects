package com.smate.core.base.utils.string;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.smate.core.base.utils.model.security.Person;

public class CompareToTest {
	
	public static void main(String[] args){
		
		CompareToTest t = new CompareToTest();
		
		List<Person> psnList = new LinkedList<Person>();
		psnList.add(new Person(1L, "AA", "BB", "阿一", "DD"));
		psnList.add(new Person(1L, "BB", "BB", "必须", "DD"));
		psnList.add(new Person(1L, "FF", "qq", "彩虹", "DD"));
		psnList.add(new Person(1L, "DD", "HH", "校服", "DD"));
		psnList.add(new Person(1L, "呵呵", "BB", "小红", "DD"));
		psnList.add(new Person(1L, "XX", "BB", "", "吃货"));
		psnList.add(new Person(1L, "SS", "BB", "大丁", "DD"));
		psnList.add(new Person(1L, "EE", "BB", "前后", "DD"));
		psnList.add(new Person(1L, "小明", "BB", "馒头", "DD"));
		psnList.add(new Person(1L, "GG", "BB", "开心", "DD"));
		psnList.add(new Person(1L, "GG", "BB", "1A", "DD"));
		psnList.add(new Person(1L, "GG", "BB", "$sd", "DD"));
		psnList.add(new Person(1L, "GG", "BB", "2C", "DD"));
		psnList.add(new Person(1L, "GG", "BB", "开h", "DD"));
		psnList.add(new Person(1L, "GG", "BB", "开C", "DD"));
		psnList.add(new Person(1L, "GG", "BB", "AA", "DD"));
		psnList.add(new Person(1L, "GG", "BB", "Ab", "DD"));
		psnList.add(new Person(1L, "GG", "BB", "dd", "DD"));
		psnList.add(new Person(1L, "GG", "BB", "bb", "DD"));
		psnList.add(new Person(1L, "GG", "BB", "GG", "DD"));
		
		
		List<String> strList = new ArrayList<String>();
		strList.add(t.parseName("阿一"));
		strList.add(t.parseName("必须"));
		strList.add(t.parseName("校服"));
		strList.add(t.parseName("彩虹"));
		strList.add(t.parseName("大丁"));
		strList.add(t.parseName("吃货"));
		strList.add(t.parseName("馒头"));
		strList.add(t.parseName("开心"));
		strList.add(t.parseName("前后"));
		Collections.sort(strList);
		for(String str : strList){
			System.out.print(str + ", ");
		}
		
		Collections.sort(psnList);
		
		for(Person p : psnList){
			System.out.println(p.getName() + ", " + p.getLastName() + ", " + p.getFirstName());
		}
	}
	
	
	public String parseName(String name){
		String regexStr = "[\u4E00-\u9FA5]";
		Pattern p = Pattern.compile(regexStr);
		StringBuffer newName = new StringBuffer();
		if(StringUtils.isNotBlank(name)){
			char[] nameChar = name.toCharArray();
			for(int i=0; i<nameChar.length; i++){
				if(p.matches(regexStr, nameChar[i]+"")){
					newName.append(ServiceUtil.parseWordPinyin(nameChar[i]));
				}else{
					newName.append(nameChar[i]);
				}
			}
		}
		return newName.toString().toUpperCase();
	}
}
