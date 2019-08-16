heatmap=function(target,passdata,measures,planning,cmax){
	var max=cmax;
	var targetelement = target;
    var data = passdata;
    var myChart = echarts.init(document.getElementById(targetelement));
    data = data.map(function (item) {
        return [item[1], item[0], item[2] || '-'];
    });
    
    option = {
        tooltip: {
            position: 'top'
        },
        animation: false,
        grid: {
            height: '50%',
            y: '10%'
        },
        xAxis: {
            type: 'category',
            data: measures
        },
        yAxis: {
            type: 'category', 
            data: planning
        },
        visualMap: {
            min: 1,
            max: max,
            calculable: true,
            orient: 'horizontal',
            left: 'center',
            bottom: '15%'
        },
        series: [{
            name: '专利矩阵',
            type: 'heatmap',
            data: data,
            label: {
                normal: {
                    show: true
                }
            },
            itemStyle: {
                emphasis: {
                    shadowBlur: 10,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
            }
        }]
    };
    myChart.setOption(option);
}


