bioSciIns=function(target,passdata){
	var targetelement = target;
  var data = passdata;
    var myChart = echarts.init(document.getElementById(targetelement));
    option = {
        title:{
	        text: "生命科学-机构",
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
            data: ['生命科学-机构']
        }],
        animationDuration: 3000,  
        animationEasingUpdate: 'quinticInOut', 
        series: [{ 
            name: '生命科学-机构',
            type: 'graph',
            layout: 'force',  
            force: { 
                repulsion: 350  
            },

        data:[{"name": "生命科学-机构","value": 6,"symbolSize": 18,"category": "生命科学-机构","draggable": "true"},
              {"name": "ARIZONA STATE UNIV","symbolSize": 3,"category": "生命科学-机构","draggable": "true","value": 5},
              {"name": "MIT","symbolSize": 3,"category": "生命科学-机构","draggable": "true","value": 5},
              {"name": "JST","symbolSize": 3,"category": "生命科学-机构","draggable": "true","value": 5},
              {"name": "BROAD INST","symbolSize": 3,"category": "生命科学-机构","draggable": "true","value": 5},
              {"name": "UNIV UTRECHT","symbolSize": 3,"category": "生命科学-机构","draggable": "true","value": 5},
              {"name": "J DAVID GLADSTONE INST","symbolSize": 3,"category": "生命科学-机构","draggable": "true","value": 5},
              {"name": "HOWARD HUGHES MED INST","symbolSize": 3,"category": "生命科学-机构","draggable": "true","value": 5},
              {"name": "DUKE UNIV","symbolSize": 3,"category": "生命科学-机构","draggable": "true","value": 5},
              {"name": "UNIV MARYLAND COLLEGE PARK","symbolSize": 3,"category": "生命科学-机构","draggable": "true","value": 5},
              {"name": "JOHNS HOPKINS UNIV","symbolSize": 3,"category": "生命科学-机构","draggable": "true","value": 5},
              {"name": "KAROLINSKA INST","symbolSize": 3,"category": "生命科学-机构","draggable": "true","value": 5},
              {"name": "UNIV VIGO","symbolSize": 3,"category": "生命科学-机构","draggable": "true","value": 5},
              {"name": "NA-GENOME CTR WISCONSIN","symbolSize": 3,"category": "生命科学-机构","draggable": "true","value": 5},
              {"name": "UNIV PENN","symbolSize": 3,"category": "生命科学-机构","draggable": "true","value": 5},
              {"name": "UNIV CAMBRIDGE","symbolSize": 3,"category": "生命科学-机构","draggable": "true","value": 5},
              {"name": "MAX PLANCK SOCIETY","symbolSize": 3,"category": "生命科学-机构","draggable": "true","value": 5},
              {"name": "ATOMIC ENER ALT ENER COMMISSION","symbolSize": 3,"category": "生命科学-机构","draggable": "true","value": 5},
              {"name": "LEIDEN UNIV","symbolSize": 3,"category": "生命科学-机构","draggable": "true","value": 5},
              {"name": "UNIV COLL LONDON","symbolSize": 3,"category": "生命科学-机构","draggable": "true","value": 5},
              {"name": "AFFYMETRIX","symbolSize": 3,"category": "生命科学-机构","draggable": "true","value": 5}],
links:[{"source": "生命科学-机构","target": "ARIZONA STATE UNIV"}, 
       {"source": "生命科学-机构","target": "MIT"}, 
       {"source": "生命科学-机构","target": "JST"}, 
       {"source": "生命科学-机构","target": "BROAD INST"}, 
       {"source": "生命科学-机构","target": "UNIV UTRECHT"}, 
       {"source": "生命科学-机构","target": "J DAVID GLADSTONE INST"}, 
       {"source": "生命科学-机构","target": "HOWARD HUGHES MED INST"},
       {"source": "生命科学-机构","target": "DUKE UNIV"},
       {"source": "生命科学-机构","target": "UNIV MARYLAND COLLEGE PARK"},
       {"source": "生命科学-机构","target": "JOHNS HOPKINS UNIV"},
       {"source": "生命科学-机构","target": "KAROLINSKA INST"}, 
       {"source": "生命科学-机构","target": "UNIV VIGO"}, 
       {"source": "生命科学-机构","target": "NA-GENOME CTR WISCONSIN"}, 
       {"source": "生命科学-机构","target": "UNIV PENN"}, 
       {"source": "生命科学-机构","target": "UNIV CAMBRIDGE"}, 
       {"source": "生命科学-机构","target": "MAX PLANCK SOCIETY"}, 
       {"source": "生命科学-机构","target": "ATOMIC ENER ALT ENER COMMISSION"},
       {"source": "生命科学-机构","target": "LEIDEN UNIV"},
       {"source": "生命科学-机构","target": "UNIV COLL LONDON"},
       {"source": "生命科学-机构","target": "AFFYMETRIX"}],
       
   categories: [{'name':'ARIZONA STATE UNIV'},
                {'name':'MIT'},
                {'name':'JST'},
                {'name':'BROAD INST'},
                {'name':'UNIV UTRECHT'},
                {'name':'J DAVID GLADSTONE INST'},
                {'name':'HOWARD HUGHES MED INST'},
                {'name':'DUKE UNIV'},
                {'name':'UNIV MARYLAND COLLEGE PARK'},
                {'name':'JOHNS HOPKINS UNIV'},
                {'name':'KAROLINSKA INST'},
                {'name':'UNIV VIGO'},
                {'name':'NA-GENOME CTR WISCONSIN'},
                {'name':'UNIV PENN'},
                {'name':'UNIV CAMBRIDGE'},
                {'name':'MAX PLANCK SOCIETY'},
                {'name':'ATOMIC ENER ALT ENER COMMISSION'},
                {'name':'LEIDEN UNIV'},
                {'name':'UNIV COLL LONDON'},
                {'name':'AFFYMETRIX'}],
 
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