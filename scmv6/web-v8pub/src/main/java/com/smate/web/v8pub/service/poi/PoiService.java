package com.smate.web.v8pub.service.poi;

import java.util.Collection;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * 导出excel.
 * 
 * @author chenxiangrong
 * 
 * @param <T>
 */
public interface PoiService<T> {
	HSSFWorkbook exportExcel(String title, Collection<T> dataset, int flag, String pattern) throws Exception;

	HSSFWorkbook exportExcelTable(String title, Collection<T> dataset, String pattern) throws Exception;

	HSSFWorkbook exportExcelByTemp(String fileTempPath, Collection<T> dataset, String pattern) throws Exception;

	String getFileRoot();
}
