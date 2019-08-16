<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="resmod" value="/resmod" />
<title>前沿分析</title>
<script src="${resmod}/js/clusteringTest2.js"></script>
<script src="${resmod}/js/echarts.min.js"></script>
<script type="text/javascript">
var mId=${mId};//左侧菜单Id
var titleData = "信息科学";
var bottomData = ['计算机科学与技术', '图书情报学', '物理学','信息论','信息科学技术','推荐系统','信息检索','大数据','信息采集','信息融合'];
var categoryData = [{'name': '计算机科学与技术'},
                    {'name': '图书情报学'},
                    {'name': '物理学'},
                    {'name': '信息论'},
                    {'name': '信息科学技术'},
                    {'name': '推荐系统'},
                    {'name': '信息检索'},
                    {'name': '大数据'},
                    {'name': '信息采集'},
                    {'name': '信息融合'}];
                    
var linkData =[{"source": "信息科学","target": "计算机科学与技术"}, 
         		{"source": "信息科学","target": "图书情报学"}, 
          		{"source": "信息科学","target": "物理学"},
          		{"source": "信息科学","target": "信息论"},
          		{"source": "信息科学","target": "信息科学技术"},
          		{"source": "信息科学","target": "推荐系统"},
          		{"source": "信息科学","target": "信息检索"},
          		{"source": "信息科学","target": "大数据"},
          		{"source": "信息科学","target": "信息采集"},
          		{"source": "信息科学","target": "信息融合"},
          		{"source": "计算机科学与技术","target": "人工智能"},
          		{"source": "计算机科学与技术","target": "信息处理"},
          		{"source": "计算机科学与技术","target": "神经网络"},
          		{"source": "计算机科学与技术","target": "自然语言处理"},
          		{"source": "计算机科学与技术","target": "机器学习"},
          		{"source": "计算机科学与技术","target": "数据挖掘"},
          		{"source": "图书情报学","target": "情报科学"},
          		{"source": "图书情报学","target": "图书馆学"},
          		{"source": "图书情报学","target": "信息资源"},
          		{"source": "图书情报学","target": "信息社会"},
          		{"source": "物理学","target": "电子信息科学"},
          		{"source": "物理学","target": "光电子学"},
          		{"source": "物理学","target": "通信技术"},
          		{"source": "物理学","target": "光信息科学与技术"}];
         		
var chartData =[{"name": "信息科学","value": 27,"symbolSize": 20,"draggable": "true"},
                {"name": "计算机科学与技术","symbolSize": 18,"category": "计算机科学与技术","draggable": "true","value": 6},
                {"name": "图书情报学","symbolSize": 18,"category": "图书情报学","draggable": "true","value": 6}, 
                {"name": "物理学","symbolSize": 18,"category": "物理学","draggable": "true","value": 6},
                {"name": "信息论","symbolSize": 18,"category": "信息论","draggable": "true","value": 6},
                {"name": "信息科学技术","symbolSize": 18,"category": "信息科学技术","draggable": "true","value": 6},
                {"name": "推荐系统","symbolSize": 18,"category": "推荐系统","draggable": "true","value": 6},
                {"name": "信息检索","symbolSize": 18,"category": "信息检索","draggable": "true","value": 6},
                {"name": "大数据","symbolSize": 18,"category": "大数据","draggable": "true","value": 6},
                {"name": "信息采集","symbolSize": 18,"category": "信息采集","draggable": "true","value": 6},
                {"name": "信息融合","symbolSize": 18,"category": "信息融合","draggable": "true","value": 6},
                {"name": "人工智能","symbolSize": 3,"category": "计算机科学与技术","draggable": "true","value": 2},
                {"name": "信息处理","symbolSize": 3,"category": "计算机科学与技术","draggable": "true","value": 2},  
                {"name": "神经网络","symbolSize": 3,"category": "计算机科学与技术","draggable": "true","value": 2},
                {"name": "自然语言处理","symbolSize": 3,"category": "计算机科学与技术","draggable": "true","value": 2}, 
                {"name": "机器学习","symbolSize": 3,"category": "计算机科学与技术","draggable": "true","value": 2},  
                {"name": "数据挖掘","symbolSize": 3,"category": "计算机科学与技术","draggable": "true","value": 2},
                {"name": "情报科学","symbolSize": 3,"category": "图书情报学","draggable": "true","value": 2}, 
                {"name": "图书馆学","symbolSize": 3,"category": "图书情报学","draggable": "true","value": 2}, 
                {"name": "信息资源","symbolSize": 3,"category": "图书情报学","draggable": "true","value": 2}, 
                {"name": "信息社会","symbolSize": 3,"category": "图书情报学","draggable": "true","value": 2}, 
                {"name": "电子信息科学","symbolSize": 3,"category": "物理学","draggable": "true","value": 2},
                {"name": "光电子学","symbolSize": 3,"category": "物理学","draggable": "true","value": 2},
                {"name": "通信技术","symbolSize": 3,"category": "物理学","draggable": "true","value": 2},
                {"name": "光信息科学与技术","symbolSize": 3,"category": "物理学","draggable": "true","value": 2}];
                
$(document).ready(function() {
	clusterchart('main4',1,bottomData,categoryData,linkData,chartData,titleData);
});

</script>
