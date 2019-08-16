earthIns=function(target,passdata){
	var targetelement = target;
  var data = passdata;
    var myChart = echarts.init(document.getElementById(targetelement));
    option = {
        title:{
	        text: "地球科学-机构",
	        subtext: "关键分析",
	        top: "top",
	        left: "center"
       },
        legend: [{  
            tooltip: { 
                show: true 
            },
            selectedMode: 'false', 
            bottom: 20, 
            data: ['地球科学-机构']
        }],
        animationDuration: 3000,  
        animationEasingUpdate: 'quinticInOut', 
        series: [{ 
            name: '地球科学-机构',
            type: 'graph',
            layout: 'force',  
            force: { 
                repulsion: 350  
            },

        data:[{
	"name": "地球科学-机构",
	"value": 6,
	"symbolSize": 18,
	"category": "地球科学-机构",
	"draggable": "true"
},{
	"name": "EUROPEAN CTR MEDIUM RANGE WEATHER FORECASTS",
	"symbolSize": 3,
	"category": "地球科学-机构",
	"draggable": "true",
	"value": 2
},{
	"name": "CNRS",
	"symbolSize": 3,
	"category": "地球科学-机构",
	"draggable": "true",
	"value": 2
},{
	"name": "LAWRENCE LIVERMORE NATL LAB",
	"symbolSize": 3,
	"category": "地球科学-机构",
	"draggable": "true",
	"value": 2
},{
	"name": "ATOMIC ENER ALT ENER COMMISSION",
	"symbolSize": 3,
	"category": "地球科学-机构",
	"draggable": "true",
	"value": 2
},{
	"name": "DEPARTMENT OF INTERIOR - USA",
	"symbolSize": 3,
	"category": "地球科学-机构",
	"draggable": "true",
	"value": 2
},{
	"name": "ARIZONA STATE UNIV",
	"symbolSize": 3,
	"category": "地球科学-机构",
	"draggable": "true",
	"value": 2
},{
	"name": "UNIV MELBOURNE",
	"symbolSize": 3,
	"category": "地球科学-机构",
	"draggable": "true",
	"value": 2
},{
	"name": "BUR METEOROL",
	"symbolSize": 3,
	"category": "地球科学-机构",
	"draggable": "true",
	"value": 2
},{
	"name": "NCAR (NATL CTR ATMOSPHERIC RES",
	"symbolSize": 3,
	"category": "地球科学-机构",
	"draggable": "true",
	"value": 2
},{
    "name": "ELECTRIC POWER RES INST (EPRI)",
    "symbolSize": 3,
    "category": "地球科学-机构",
    "draggable": "true",
    "value": 2
},{
	"name": "GODDARD SPACE FLIGHT CTR",
	"symbolSize": 3,
	"category": "地球科学-机构",
	"draggable": "true",
	"value": 2
},{
	"name": "DEUTSCHER WETTERDIENST",
	"symbolSize": 3,
	"category": "地球科学-机构",
	"draggable": "true",
	"value": 2
},{
	"name": "NOAA (NATL OCEANIC ATMOSPHERIC ADMIN)",
	"symbolSize": 3,"category": "地球科学-机构",
	"draggable": "true",
	"value": 2
},{
	"name": "NA-IM SYST GRP INC",
	"symbolSize": 3,
	"category": "地球科学-机构",
	"draggable": "true",
	"value": 2
},{
	"name": "NA-ATMOSPHER CHEM SERV",
	"symbolSize": 3,
	"category": "地球科学-机构",
	"draggable": "true",
	"value": 2
},{
	"name": "CALTECH",
	"symbolSize": 3,
	"category": "地球科学-机构",
	"draggable": "true",
	"value": 2
},{
	"name": "AERODYNE RES INC",
	"symbolSize": 3,
	"category": "地球科学-机构",
	"draggable": "true",
	"value": 2
},{
	"name": "CALIF STATE UNIV",
	"symbolSize": 3,
	"category": "地球科学-机构",
	"draggable": "true",
	"value": 2
},{
	"name": "UNIV MINNESOTA",
	"symbolSize": 3,
	"category": "地球科学-机构",
	"draggable": "true",
	"value": 2
}],
links:[{
	"source": "地球科学-机构",
	"target": "EUROPEAN CTR MEDIUM RANGE WEATHER FORECASTS"
},{
	"source": "地球科学-机构",
	"target": "CNRS"
},{
	"source": "地球科学-机构",
	"target": "LAWRENCE LIVERMORE NATL LAB"
},{
	"source": "地球科学-机构",
	"target": "ATOMIC ENER ALT ENER COMMISSION"
},{
	"source": "地球科学-机构",
	"target": "DEPARTMENT OF INTERIOR - USA"
},{
	"source": "地球科学-机构",
	"target": "ARIZONA STATE UNIV"
},{
	"source": "地球科学-机构",
	"target": "UNIV MELBOURNE"
},{
	"source": "地球科学-机构",
	"target": "BUR METEOROL"
},{
	"source": "地球科学-机构",
	"target": "NCAR (NATL CTR ATMOSPHERIC RES"
},{
	"source": "地球科学-机构",
	"target": "ELECTRIC POWER RES INST (EPRI)"
},{
	"source": "地球科学-机构",
	"target": "GODDARD SPACE FLIGHT CTR"
},{
	"source": "地球科学-机构",
	"target": "DEUTSCHER WETTERDIENST"
},{
	"source": "地球科学-机构",
	"target": "NOAA (NATL OCEANIC ATMOSPHERIC ADMIN)"
},{
	"source": "地球科学-机构",
	"target": "NA-IM SYST GRP INC"
},{
	"source": "地球科学-机构",
	"target": "NA-ATMOSPHER CHEM SERV"
},{
	"source": "地球科学-机构",
	"target": "CALTECH"
},{
	"source": "地球科学-机构",
	"target": "AERODYNE RES INC"
},{
	"source": "地球科学-机构",
	"target": "CALIF STATE UNIV"
},{
	"source": "地球科学-机构",
	"target": "UNIV MINNESOTA"
}],
       
   categories: [{
          'name':'EUROPEAN CTR MEDIUM RANGE WEATHER FORECASTS'
          },{
            'name':'CNRS' 
          },{
            'name':'LAWRENCE LIVERMORE NATL LAB'
          },{
            'name':'ATOMIC ENER ALT ENER COMMISSION'
          },{
            'name':'DEPARTMENT OF INTERIOR - USA'
          },{
            'name':'ARIZONA STATE UNIV'
          },{
            'name':'UNIV MELBOURNE'
          },{
            'name':'BUR METEOROL'
          },{
            'name':'NCAR (NATL CTR ATMOSPHERIC RES'
          },{
            'name':'ELECTRIC POWER RES INST (EPRI)'
          },{
            'name':'GODDARD SPACE FLIGHT CTR'
          },{
            'name':'DEUTSCHER WETTERDIENST'
          },{
            'name':'NOAA (NATL OCEANIC ATMOSPHERIC ADMIN)'
          },{
            'name':'NA-IM SYST GRP INC'
          },{
          	'name':'NA-ATMOSPHER CHEM SERV'
          },{
            'name':'CALTECH'
          },{
            'name':'AERODYNE RES INC'
          },{
            'name':'CALIF STATE UNIV'
          },{
            'name':'UNIV MINNESOTA'
          }],
 
          roam: true,
          label: {
              normal: {
                  show: true,
                  position: 'top',
              }
          },
          lineStyle: {
              normal: {
                  color: 'source',
                  curveness: 0,
                  type: "solid"
              }
          }
      }]
  };
  myChart.setOption(option);
}