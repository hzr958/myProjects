medicineIns=function(target,passdata){
	var targetelement = target;
  var data = passdata;
    var myChart = echarts.init(document.getElementById(targetelement));
    option = {
        title:{
	        text: "医学科学-机构",
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
            data: ['医学科学-机构']
        }],
        animationDuration: 3000,  
        animationEasingUpdate: 'quinticInOut', 
        series: [{ 
            name: '医学科学-机构',
            type: 'graph',
            layout: 'force',  
            force: { 
                repulsion: 350  
            },

        data:[{"name": "医学科学-机构","value": 6,"symbolSize": 18,"category": "医学科学-机构","draggable": "true"},
              {"name": "AMER CANC SOC","symbolSize": 3,"category": "医学科学-机构","draggable": "true","value": 2},
              {"name": "INT AGENCY RES CANCER (IARC)","symbolSize": 3,"category": "医学科学-机构","draggable": "true","value": 2},
              {"name": "CTR DIS CONTROL PREVENT - USA","symbolSize": 3,"category": "医学科学-机构","draggable": "true","value": 2},
              {"name": "E SURREY HOSP","symbolSize": 3,"category": "医学科学-机构","draggable": "true","value": 2},
              {"name": "BOSTON UNIV","symbolSize": 3,"category": "医学科学-机构","draggable": "true","value": 2},
              {"name": "CLEVELAND CLIN FDN","symbolSize": 3,"category": "医学科学-机构","draggable": "true","value": 2},
              {"name": "ANGELES CLIN & RES INST","symbolSize": 3,"category": "医学科学-机构","draggable": "true","value": 2},
              {"name": "ASSIST PUBL HOSP MARSEILLE","symbolSize": 3,"category": "医学科学-机构","draggable": "true","value": 2},
              {"name": "ARUP LABS","symbolSize": 3,"category": "医学科学-机构","draggable": "true","value": 2},
              {"name": "YALE UNIV","symbolSize": 3,"category": "医学科学-机构","draggable": "true","value": 2},
              {"name": "BOEHRINGER INGELHEIM PHARMA GMBH CO KG","symbolSize": 3,"category": "医学科学-机构","draggable": "true","value": 2},
              {"name": "UNIV CALIF LOS ANGELES","symbolSize": 3,"category": "医学科学-机构","draggable": "true","value": 2},
              {"name": "OHIO STATE UNIV","symbolSize": 3,"category": "医学科学-机构","draggable": "true","value": 2},
              {"name": "COLUMBIA UNIV","symbolSize": 3,"category": "医学科学-机构","draggable": "true","value": 2},
              {"name": "MARIO NEGRI INST PHARMACOL RES","symbolSize": 3,"category": "医学科学-机构","draggable": "true","value": 2},
              {"name": "ASTRA ZENECA","symbolSize": 3,"category": "医学科学-机构","draggable": "true","value": 2},
              {"name": "CTR CANC & INFLAMMAT","symbolSize": 3,"category": "医学科学-机构","draggable": "true","value": 2},
              {"name": "DANA FARBER CANC CTR","symbolSize": 3,"category": "医学科学-机构","draggable": "true","value": 2},
              {"name": "GHENT UNIV","symbolSize": 3,"category": "医学科学-机构","draggable": "true","value": 2},
              {"name": "BERMAN CTR OUTCOMES & CLIN RES","symbolSize": 3,"category": "医学科学-机构","draggable": "true","value": 2}],
links:[{"source": "医学科学-机构","target": "AMER CANC SOC"}, 
       {"source": "医学科学-机构","target": "INT AGENCY RES CANCER (IARC)"}, 
       {"source": "医学科学-机构","target": "CTR DIS CONTROL PREVENT - USA"}, 
       {"source": "医学科学-机构","target": "E SURREY HOSP"}, 
       {"source": "医学科学-机构","target": "BOSTON UNIV"}, 
       {"source": "医学科学-机构","target": "CLEVELAND CLIN FDN"}, 
       {"source": "医学科学-机构","target": "ANGELES CLIN & RES INST"},
       {"source": "医学科学-机构","target": "ASSIST PUBL HOSP MARSEILLE"},
       {"source": "医学科学-机构","target": "ARUP LABS"},
       {"source": "医学科学-机构","target": "YALE UNIV"},
       {"source": "医学科学-机构","target": "BOEHRINGER INGELHEIM PHARMA GMBH CO KG"}, 
       {"source": "医学科学-机构","target": "UNIV CALIF LOS ANGELES"}, 
       {"source": "医学科学-机构","target": "OHIO STATE UNIV"}, 
       {"source": "医学科学-机构","target": "COLUMBIA UNIV"}, 
       {"source": "医学科学-机构","target": "MARIO NEGRI INST PHARMACOL RES"}, 
       {"source": "医学科学-机构","target": "ASTRA ZENECA"}, 
       {"source": "医学科学-机构","target": "CTR CANC & INFLAMMAT"},
       {"source": "医学科学-机构","target": "DANA FARBER CANC CTR"},
       {"source": "医学科学-机构","target": "GHENT UNIV"},
       {"source": "医学科学-机构","target": "BERMAN CTR OUTCOMES & CLIN RES"}],
       
   categories: [{'name':'AMER CANC SOC'},
                {'name':'INT AGENCY RES CANCER (IARC)'},
                {'name':'CTR DIS CONTROL PREVENT - USA'},
                {'name':'E SURREY HOSP'},
                {'name':'BOSTON UNIV'},
                {'name':'CLEVELAND CLIN FDN'},
                {'name':'ANGELES CLIN & RES INST'},
                {'name':'ASSIST PUBL HOSP MARSEILLE'},
                {'name':'ARUP LABS'},
                {'name':'YALE UNIV'},
                {'name':'BOEHRINGER INGELHEIM PHARMA GMBH CO KG'},
                {'name':'UNIV CALIF LOS ANGELES'},
                {'name':'OHIO STATE UNIV'},
                {'name':'COLUMBIA UNIV'},
                {'name':'MARIO NEGRI INST PHARMACOL RES'},
                {'name':'ASTRA ZENECA'},
                {'name':'CTR CANC & INFLAMMAT'},
                {'name':'DANA FARBER CANC CTR'},
                {'name':'GHENT UNIV'},
                {'name':'BERMAN CTR OUTCOMES & CLIN RES'}],
 
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