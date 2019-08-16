piechart=function(target,passdata){
    var targetelement = target;
    var myChart = echarts.init(document.getElementById(targetelement)); 
    var data = passdata;
    myChart.setOption({
        series:[{
        	name:"公司贡献",
        	type:"pie",
        	radius:"50%",
        	/*data:[
                    {value:235, name:"科研之友测试部"},
                    {value:274, name:"科研之友人力资源部"},
                    {value:310, name:"科研之友专项资金部"},
                    {value:335, name:"科研之友重大项目部"},
                    {value:490, name:"科研之友个人业务部"},
        		]*/
        	data:passdata
        	}]
        });	
}