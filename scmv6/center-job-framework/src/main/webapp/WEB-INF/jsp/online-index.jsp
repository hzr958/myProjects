<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="/job"></c:set>

<div class="page-header">
    <h1> 任务列表
        <small>
            <i class="ace-icon fa fa-angle-double-right"></i> 在线任务
        </small>
    </h1>
</div>
<!-- /.page-header -->

<div class="row">
    <div class="col-xs-12">
        <!-- PAGE CONTENT BEGINS -->
        <div class="alert alert-info">
            <button class="close" data-dismiss="alert">
                <i class="ace-icon fa fa-times"></i>
            </button>

            <i class="ace-icon fa fa-hand-o-right"></i>
            注意任务修改必须先停止任务！
        </div>

        <div class="row">
            <div class="col-xs-12">
                <table id="config-grid-table"></table>
                <div id="config-grid-pager"></div>
            </div>
        </div>

        <!-- PAGE CONTENT ENDS -->
    </div><!-- /.col -->
</div>
<!-- /.row -->


<!-- page specific plugin scripts -->

<!-- inline scripts related to this page -->
<script type="text/javascript">
  var config_grid_selector = "#config-grid-table",
      config_pager_selector = "#config-grid-pager";
  jQuery(function ($) {
    var parent_column = $(config_grid_selector).closest('[class*="col-"]');
    //resize to fit page size
    $(window).on('resize.jqGrid', function () {
      $(config_grid_selector).jqGrid('setGridWidth', parent_column.width());
    });

    //resize on sidebar collapse/expand
    $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
      if (event_name === 'sidebar_collapsed' || event_name
          === 'main_container_fixed') {
        //setTimeout is for webkit only to give time for DOM changes and then redraw!!!
        setTimeout(function () {
          $(config_grid_selector).jqGrid('setGridWidth', parent_column.width());
        }, 20);
      }
    });
    var editOptions = $.extend($.jgrid.custom.prmEdit,
        {
          url: '${ctx}/online/config/edit',
          beforeShowForm: beforeShowEditForm,
          afterShowForm: afterShowEditForm
        }),
        deleteOptions = $.extend($.jgrid.custom.prmDelete, {
          url: '${ctx}/online/config/delete'
        });
    jQuery(config_grid_selector).jqGrid({
      url: '${ctx}/online/config/search',
      sortname: 'modifiedTime',
      sortorder: 'desc',
      colNames: ['操作', 'ID', '任务名称', '开关', '权重', '优先级', '已完成', '未完成', '失败的',
        '<i class="ace-icon glyphicon glyphicon-time"></i> 更新时间'],
      colModel: [
        {
          name: 'ac',
          width: 80,
          align: 'center',
          fixed: true,
          sortable: false,
          resize: false,
          search: false,
          formatter: 'actions',
          formatoptions: {
            keys: true,
            delOptions: deleteOptions,
            editformbutton: true,
            editOptions: editOptions
          }
        },
        {
          name: 'id',
          index: 'id',
          width: 50,
          hidden: true,
          sorttype: "string",
          editable: true
        },
        {
          name: 'name',
          index: 'name',
          editable: true,
          edittype: 'text',
          editoptions: {maxlength: "50", style: "width: 80%"},
          editrules: {required: true}
        },
        {
          name: 'enable',
          index: 'enable',
          width: 80,
          editable: true,
          edittype: "checkbox",
          editoptions: {value: "1:0"},
          formatter: 'enableFormatter'
        },
        {
          name: 'weight',
          index: 'weight',
          width: 40,
          editable: true,
          edittype: 'select',
          editoptions: {value: "${jobWeightEnums}"},
          editrules: {required: true}
        },
        {
          name: 'priority',
          index: 'priority',
          width: 60,
          editable: true,
          edittype: 'custom',
          editoptions: {custom_element: createNumberInput, custom_value: inputValue},
          editrules: {
            required: true,
            integer: true,
            minValue: -2147483648,
            maxValue: 2147483647
          }
        },
        {
          name: 'doneCount',
          width: 100,
          sortable: false,
          search: false,
          formatter: doneCountFormatter
        },
        {
          name: 'notDoneCount',
          width: 100,
          sortable: false,
          search: false,
          formatter: notDoneCountFormatter
        },
        {
          name: 'errCount',
          width: 100,
          sortable: false,
          search: false,
          formatter: errCountFormatter
        },
        {
          name: 'modifiedTime',
          index: 'modifiedTime',
          width: 160,
          fixed: true,
          sorttype: "date",
          search: false,
          formatter: 'date',
          formatoptions: {srcformat: 'Y-m-d H:i:s', newformat: 'Y-m-d H:i:s'}
        }
      ],
      pager: config_pager_selector,
      caption: "在线任务配置列表",
      loadComplete: function (result) {
        var table = this;
        if (!!result.success) {
          setTimeout(function () {
            $(table).jqGrid('setLabel', 'rn', '序号', {'text-align': 'center'});
            styleCheckbox(table);
            updateActionIcons(table);
            updatePagerIcons(table);
            enableTooltips(table);
            $(table).hideCol("subgrid");
          }, 0);
        } else {
          var errMsg = result.errMsg;
          layer.alert(errMsg);
        }
      },
      subGrid: true,    //子表格支持
      subGridRowExpanded: buildSubGrid,
      subGridRowColapsed: function (pId, id) {
        $(config_grid_selector + " tr#" + id).attr('sub-data', '');
        return true;
      }
    })  //navButtons
    .jqGrid('navGrid', config_pager_selector,
        $.jgrid.custom.navOptions, editOptions, $.extend($.jgrid.custom.prmAdd, {
          url: '${ctx}/online/config/add',
          beforeShowForm: function (formid) {
            $(this).jqGrid('resetSelection');
            style_edit_form(formid);
          },
          afterShowForm: afterShowEditForm
        }), deleteOptions, $.jgrid.custom.prmSearch,
        $.extend($.jgrid.custom.prmView, {
          beforeShowForm: beforeShowViewForm,
        })
    );

    $(window).triggerHandler('resize.jqGrid');//trigger window resize to make the grid get the correct size

    function errCountFormatter(cellvalue, options, rowdata) {
      var op = '<div class="ui-pg-div sub-grid-expand" title="点击展开" onclick="triggerSubGrid(\'failed\', this)" style="float: left; cursor:pointer;" onmouseover="$(this).addClass(\'ui-state-hover\');" onmouseout="$(this).removeClass(\'ui-state-hover\');"><span class="ui-icon fa fa-plus red" /></div>';
      var span = '<span>' + cellvalue + '个</span>';
      return op + '&nbsp;&nbsp;' + span;
    }

    function doneCountFormatter(cellvalue, options, rowdata) {
      var op = '<div class="ui-pg-div sub-grid-expand" title="点击展开" onclick="triggerSubGrid(\'done\', this)" style="float: left; cursor:pointer;" onmouseover="$(this).addClass(\'ui-state-hover\');" onmouseout="$(this).removeClass(\'ui-state-hover\');"><span class="ui-icon fa fa-plus green" /></div>';
      var span = '<span>' + cellvalue + '个</span>';
      return op + '&nbsp;&nbsp;' + span;
    }

    function notDoneCountFormatter(cellvalue, options, rowdata) {
      var op = '<div class="ui-pg-div sub-grid-expand" title="点击展开" onclick="triggerSubGrid(\'notdone\', this)" style="float: left; cursor:pointer;" onmouseover="$(this).addClass(\'ui-state-hover\');" onmouseout="$(this).removeClass(\'ui-state-hover\');"><span class="ui-icon fa fa-plus blue" /></div>';
      var span = '<span>' + cellvalue + '个</span>';
      return op + '&nbsp;&nbsp;' + span;
    }

    function beforeShowEditForm(formid) {
      selectLastRowOnly(this);
      style_edit_form(formid);
    }

    function afterShowEditForm(formid) {
      var form = $(formid[0]);
      var id = form.find("input#id_g").val();
      initChosenSelect();
      //是否启用的默认值
      var rowData = $(this).jqGrid('getGridRowData', id);
      var checked = !!rowData && rowData.enable;
      form.find('input#enable').attr("checked", checked);

      //子表格增加表单处理
      var $nameInput = form.find('input#name');
      var formId = form.attr('id');
      if ((!id || id === "_empty") && (/.*(_t)$/.test(formId))) {
        $nameInput.val($(config_grid_selector).jqGrid('getGridRowData', formId.split('_')[2]).name);
      }
    }

    function style_edit_form(formid) {
      style_form_header(formid);
      style_edit_form_buttons(formid);
      var form = $(formid[0]);
      //checkboxs 样式修改
      form.find('input[type="checkbox"]').each(function () {
        $(this).addClass('ace ace-switch ace-switch-6').after(
            '<span class="lbl" style="display: inline-block;"></span>');
      });
      //select样式修改
      form.find('select').each(function () {
        $(this).prepend('<option>').addClass("chosen-select").attr("data-placeholder", "请选择");
        if ($(this).attr('rowid') == '_empty') {
          $(this).find('option:first-child').attr('selected', 'selected');
        }
      });
      //重新定位居中
      centeredDialog(form);
    }

    function beforeShowViewForm(formid) {
      selectLastRowOnly(this);
      style_view_form(formid);
      centeredDialog(formid);
    }

    function style_view_form(formid) {
      style_form_header(formid);
      var form = $(formid[0]);
      form.find('table tr:not(:last)').each(function () {
        if ($(this).attr('id') == 'trv_ac') {
          $(this).hide();
        } else {
          $(this).show();
        }
      });
    }

    $(document).one('ajaxloadstart.page', function (e) {
      $.jgrid.gridDestroy(config_grid_selector);
      $('.ui-jqdialog').remove();
    });

    function buildSubGrid(subGridDivId, rowId) {
      var rowdata = $(config_grid_selector).jqGrid('getGridRowData', rowId);
      if (debug) {
        console.log("子表格对应行id: ", rowId);
        console.log("该行原始数据: ", rowdata);
        $(config_grid_selector).jqGrid('getGridData');
      }
      var subGridTableId = subGridDivId + "_t",
          subGridPagerId = subGridDivId + "_pgr",
          subGridTable_selector = "#" + subGridTableId,
          subGridPager_selector = "#" + subGridPagerId;

      //动态增加子表格的table和pager
      $("#" + subGridDivId).html("<table id='" + subGridTableId
          + "' parent-rowid='" + rowId + "'></table><div id='" + subGridPagerId + "'></div>");
      var subData = $(config_grid_selector).find("tr#" + rowId).attr("sub-data");
      var jobName = rowdata["name"];
      var state, editable = true, addable = true, searchable = true,
          colNames = ['ID', '任务名称', '优先级', '状态', '任务参数',
            '<i class="ace-icon glyphicon glyphicon-time"></i> 执行时间',
            '<i class="ace-icon glyphicon glyphicon-time"></i> 总耗时(ms)',
            '<i class="ace-icon glyphicon glyphicon-time"></i> 更新时间'],
          colModel = [
            {
              name: 'id',
              width: 50,
              hidden: true,
              sorttype: "string",
              editable: false
            },
            {
              name: 'name',
              editable: false,
              hidden: true,
              editable: true,
              editoptions: {disabled: "disabled"},
              editrules: {edithidden: true}
            },
            {
              name: 'priority',
              width: 60,
              fixed: true,
              editable: true,
              edittype: 'custom',
              editoptions: {custom_element: createNumberInput, custom_value: inputValue},
              editrules: {
                required: true,
                integer: true,
                minValue: -2147483648,
                maxValue: 2147483647
              }
            },
            {
              name: 'status',
              width: 80,
              fixed: true,
              editable: true,
              edittype: 'select',
              editoptions: {value: "-1:标记为失败;0:标记为未执行"},
              editrules: {required: true},
              formatter: 'statusFormatter'
            },
            {
              name: 'paramsMap',
              width: 200,
              sortable: false,
              editable: true,
              edittype: 'textarea',
              editoptions: {
                cols: "50", rows: "5"
              }
            },
            {
              name: 'startTime',
              index: 'startTime',
              width: 150,
              fixed: true,
              editable: false,
              search: false,
              sorttype: 'date',
              formatter: 'date',
              formatoptions: {srcformat: 'Y-m-d H:i:s', newformat: 'Y-m-d H:i:s'}
            },
            {
              name: 'elapsed',
              index: 'elapsed',
              width: 150,
              fixed: true,
              editable: false,
              search: false,
              defaultVal: 0,
              formatter: 'number',
              formatoptions: {
                thousandsSeparator: " ",
                decimalPlaces: 0,
                defaultValue: 0
              }
            },
            {
              name: 'modifiedTime',
              index: 'modifiedTime',
              width: 160,
              fixed: true,
              search: false,
              sorttype: "date",
              formatter: 'date',
              formatoptions: {srcformat: 'Y-m-d H:i:s', newformat: 'Y-m-d H:i:s'}
            }
          ];
      switch (subData) {
        case "failed":
          state = "failed";
          addable = false;
          colNames.splice(5, 0, '错误信息');
          colModel.splice(5, 0, {
            name: 'errMsg',
            index: "errMsg",
            sortable: false,
            editable: false,
            width: 200
          });
          break;
        case "done":
          state = "processed";
          editable = false;
          addable = false;
          break;
        case "notdone":
          state = "unprocessed";
          break;
      }
      var subEditOptions = $.extend($.jgrid.custom.prmEdit, {
        url: '${ctx}/online/list/save',
        beforeShowForm: beforeShowEditForm,
        afterShowForm: afterShowEditForm
      });

      $(subGridTable_selector).jqGrid({
        url: '${ctx}/online/list/search/' + state,
        sortname: 'modifiedTime',
        sortorder: 'desc',
        postData: {
          "name": jobName
        },
        colNames: colNames,
        colModel: colModel,
        pager: subGridPager_selector
      });
      //navButtons
      $(subGridTable_selector).jqGrid('navGrid', subGridPager_selector,
          $.extend($.jgrid.custom.navOptions, {
            edit: editable,
            search: searchable,
            add: addable
          }),
          subEditOptions,
          $.extend($.jgrid.custom.prmAdd, {
            url: '${ctx}/online/list/save',
            beforeShowForm: function (formid) {
              $(this).jqGrid('resetSelection');
              style_edit_form(formid);
            },
            afterShowForm: afterShowEditForm
          }),
          $.extend($.jgrid.custom.prmDelete, {
            url: '${ctx}/online/list/delete/' + state,
          }),
          $.extend($.jgrid.custom.prmSearch, {}),
          $.extend($.jgrid.custom.prmView, {
            beforeShowForm: beforeShowViewForm
          })
      );
    }
  });

  function triggerSubGrid(dataType, obj) {

    var $tr = $(obj).closest('tr'),
        $span = $(obj).find('span'),
        id = $tr.attr('id');

    if ($span.hasClass('fa-plus')) {
      var sleep = 0;
      //先收起其它子表格，如果有展开的话
      if (!!$tr.attr('sub-data') && $tr.attr('sub-data') != '') {
        $tr.find('div.sub-grid-expand').attr('title', '点击展开');
        $tr.find('div.sub-grid-expand > span').removeClass('fa-minus').addClass('fa-plus');
        $(config_grid_selector).jqGrid('collapseSubGridRow', id);
        sleep = 100;
      }
      setTimeout(function () {
        $span.removeClass('fa-plus').addClass('fa-minus');
        $tr.attr('sub-data', dataType);
        $(obj).attr('title', '点击收起');
        $(config_grid_selector).jqGrid('expandSubGridRow', id);
      }, sleep);

    } else {
      $span.removeClass('fa-minus').addClass('fa-plus');
      $(obj).attr('title', '点击展开');
      $(config_grid_selector).jqGrid('collapseSubGridRow', id);
    }

  }
</script>
