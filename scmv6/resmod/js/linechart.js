linechart=function(target,passdata, kws, years){
    var targetelement = target;
    var data = passdata;
    var myChart = echarts.init(document.getElementById(targetelement)); 
option = {
    title: {
       /* text: '堆叠区域图'*/
    },
    tooltip : {
        trigger: 'axis',
        axisPointer: {
            type: 'cross',
            label: {
                backgroundColor: '#6a7985'
            }
        }
    },
    legend: {
       // data:['科研之友测试部','科研之友人力资源部','科研之友专项资金部','科研之友重大项目部','科研之友个人业务部']
    	data:kws
    },
    toolbox: {
        feature: {
            saveAsImage: {}
        }
    },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
    },
    xAxis : [
        {
            type : 'category',
            boundaryGap : false,
            //data : ['周一','周二','周三','周四','周五','周六','周日']
            data:years
        }
    ],
    yAxis : [
        {
            type : 'value'
        }
    ],
    /*series : [
        {
            name:'科研之友测试部',
            type:'line',
            stack: '总量',
            areaStyle: {normal: {}},
            data:[120, 132, 101, 134, 90, 230, 210]
        },
        {
            name:'科研之友人力资源部',
            type:'line',
            stack: '总量',
            areaStyle: {normal: {}},
            data:[220, 182, 191, 234, 290, 330, 310]
        },
        {
            name:'科研之友专项资金部',
            type:'line',
            stack: '总量',
            areaStyle: {normal: {}},
            data:[150, 232, 201, 154, 190, 330, 410]
        },
        {
            name:'科研之友重大项目部',
            type:'line',
            stack: '总量',
            areaStyle: {normal: {}},
            data:[320, 332, 301, 334, 390, 330, 320]
        },
        {
            name:'科研之友个人业务部',
            type:'line',
            stack: '总量',
            label: {
                normal: {
                    show: true,
                    position: 'top'
                }
            },
            areaStyle: {normal: {}},
            data:[820, 932, 901, 934, 1290, 1330, 1320]
        }
    ]*/
    series : passdata
};
myChart.setOption(option);
}