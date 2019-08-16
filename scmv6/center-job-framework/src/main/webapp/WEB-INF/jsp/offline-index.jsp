<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="/job"></c:set>

<div class="page-header">
    <h1> 任务列表
        <small>
            <i class="ace-icon fa fa-angle-double-right"></i> 离线任务
        </small>
    </h1>
</div>
<!-- /.page-header -->

<div class="row">
    <div class="col-xs-12">
        <!-- PAGE CONTENT BEGINS -->
        <div class="alert alert-info">
            <button class="close" data-dismiss="alert">
                <i class="ace-icon fa fa-times text-nowrap"></i>
            </button>

            <i class="ace-icon fa fa-hand-o-right"></i>
            注意任务修改必须先停止任务！
        </div>

        <table id="grid-table"></table>

        <div id="grid-pager"></div>

        <!-- PAGE CONTENT ENDS -->
    </div><!-- /.col -->
</div>
<!-- /.row -->


<!-- page specific plugin scripts -->
<script src="${ctx}/res/js/custom.jqGrid.js"></script>

<!-- inline scripts related to this page -->
<script type="text/javascript">
  var grid_selector = "#grid-table",
      pager_selector = "#grid-pager",
      refreshTimer;
  jQuery(function ($) {
    var parent_column = $(grid_selector).closest('[class*="col-"]');
    //resize to fit page size
    $(window).on('resize.jqGrid', function () {
      $(grid_selector).jqGrid('setGridWidth', parent_column.width());
    })

    //resize on sidebar collapse/expand
    $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
      if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
        //setTimeout is for webkit only to give time for DOM changes and then redraw!!!
        setTimeout(function () {
          $(grid_selector).jqGrid('setGridWidth', parent_column.width());
        }, 20);
      }
    })

    var editOptions = $.extend($.jgrid.custom.prmEdit, {
          url: '${ctx}/offline/edit',
          beforeInitData: beforeInitEditData,
          beforeShowForm: beforeShowEditForm,
          afterShowForm: afterShowEditForm
        }),
        delOptions = $.extend($.jgrid.custom.prmDelete, {
          url: '${ctx}/offline/delete'
        });
    jQuery(grid_selector).jqGrid({
      url: '${ctx}/offline/search',
      sortname: 'modifiedTime',
      sortorder: 'desc',
      colNames: [' ', '操作', 'ID', '任务名称', '开关', '权重', '优先级', '线程数', '状态', '执行进度',
        '<i class="ace-icon glyphicon glyphicon-time"></i> 执行时间',
        '<i class="ace-icon glyphicon glyphicon-time"></i> 总耗时(ms)',
        '数据源', '业务表名', '唯一键名', '起始ID', '截止ID', '筛选条件',
        '<i class="ace-icon glyphicon glyphicon-time"></i> 更新时间', '错误信息'],
      colModel: [
        {
          name: 'ac',
          index: '',
          width: 80,
          fixed: true,
          sortable: false,
          search: false,
          resize: false,
          align: 'center',
          formatter: 'actions',
          formatoptions: {
            keys: true,
            delOptions: delOptions,
            editformbutton: true,
            editOptions: editOptions
          }
        },
        {
          name: 'op',
          index: 'op',
          width: 35,
          fixed: true,
          sortable: false,
          search: false,
          resize: false,
          align: 'center',
          editable: false,
          defval: '',
          formatter: 'opFormatter'
        },
        {name: 'id', index: 'id', width: 50, hidden: true, sorttype: "int"},
        {
          name: 'name',
          index: 'name',
          editable: true,
          editoptions: {maxlength: "50", style: "width: 80%"},
          editrules: {required: true, edithidden: true}
        },
        {
          name: 'enable', index: 'enable', width: 50, editable: true, edittype: "checkbox",
          editoptions: {value: "1:0"}, formatter: 'enableFormatter'
        },
        {
          name: 'weight', index: 'weight', width: 40, editable: true, edittype: 'select',
          editoptions: {value: "${jobWeightEnums}"}, editrules: {required: true}
        },
        {
          name: 'priority',
          index: 'priority',
          width: 60,
          editable: true,
          edittype: 'custom',
          editoptions: {custom_element: createNumberInput, custom_value: inputValue},
          editrules: {required: true, integer: true, minValue: -2147483648, maxValue: 2147483647}
        },
        {
          name: 'threads',
          index: 'threads',
          width: 80,
          editable: true,
          edittype: 'custom',
          editoptions: {custom_element: createNumberInput, custom_value: inputValue},
          editrules: {required: true, integer: true, minValue: 1, maxValue: 2147483647}
        },
        {name: 'status', index: 'status', width: 80, formatter: 'statusFormatter'},
        {
          name: 'percent',
          index: 'percent',
          width: 200,
          search: false,
          formatter: 'progressFormatter'
        },
        {
          name: 'startTime',
          index: 'startTime',
          width: 150,
          search: false,
          sorttype: 'date',
          formatter: 'date',
          formatoptions: {srcformat: 'Y-m-d H:i:s', newformat: 'Y-m-d H:i:s'}
        },
        {
          name: 'elapsed',
          index: 'elapsed',
          width: 150,
          editable: false,
          search: false,
          formatter: 'number',
          formatoptions: {
            thousandsSeparator: " ",
            decimalPlaces: 0,
            defaultValue: 0
          }
        },
        {
          name: 'db',
          index: 'db',
          width: 120,
          hidden: true,
          editable: true,
          edittype: "select",
          editoptions: {value: "${dbSessionEnums}"},
          editrules: {required: true, edithidden: true}
        },
        {
          name: 'table',
          index: 'table',
          width: 300,
          hidden: true,
          editable: true,
          editoptions: {maxlength: "50", style: "width: 80%"},
          editrules: {required: true, edithidden: true}
        },
        {
          name: 'uniKey',
          index: 'uniKey',
          width: 100,
          hidden: true,
          editable: true,
          editoptions: {maxlength: "50", style: "width: 80%"},
          editrules: {required: true, edithidden: true}
        },
        {
          name: 'begin',
          sortable: false,
          width: 100,
          hidden: true,
          editable: true,
          edittype: 'custom',
          editoptions: {custom_element: createNumberInput, custom_value: inputValue},
          editrules: {
            required: true,
            edithidden: true,
            integer: true,
            minValue: 1,
            maxValue: 999999999999999999
          }
        },
        {
          name: 'end',
          sortable: false,
          width: 100,
          hidden: true,
          editable: true,
          edittype: 'custom',
          editoptions: {custom_element: createNumberInput, custom_value: inputValue},
          editrules: {
            required: true,
            edithidden: true,
            integer: true,
            minValue: 1,
            maxValue: 999999999999999999
          }
        },
        {
          name: 'filter',
          sortable: false,
          width: 150,
          hidden: true,
          editable: true,
          edittype: 'textarea',
          editoptions: {cols: "50", rows: "5", maxlength: "255", style: "width: 80%"},
          editrules: {edithidden: true}
        },
        {
          name: 'modifiedTime',
          index: 'modifiedTime',
          width: 150,
          sorttype: "date",
          search: false,
          formatter: 'date',
          formatoptions: {srcformat: 'Y-m-d H:i:s', newformat: 'Y-m-d H:i:s'}
        },
        {name: 'errMsg', sortable: false, hidden: true, defval: ''}
      ],
      pager: pager_selector,
      caption: "离线任务列表",
      gridComplete: refreshUnprocessed
    });

    $(window).triggerHandler('resize.jqGrid');//trigger window resize to make the grid get the correct size

    //navButtons
    jQuery(grid_selector).jqGrid('navGrid', pager_selector,
        $.jgrid.custom.navOptions, editOptions,
        $.extend($.jgrid.custom.prmAdd, {
          url: '${ctx}/offline/add',
          beforeShowForm: function (e) {
            $(this).jqGrid('resetSelection');
            beforeShowEditForm(e);
          },
          afterShowForm: afterShowEditForm
        }), delOptions, $.jgrid.custom.prmSearch,
        $.extend($.jgrid.custom.prmView, {
          beforeShowForm: function (formid) {
            selectLastRowOnly(this);
            style_view_form(formid);
            centeredDialog(formid);
          }
        })
    );

    function style_view_form(formid) {
      style_form_header(formid);
      var form = $(formid[0]);
      form.find('table tr:not(:last)').each(function () {
        if ($(this).attr('id') == 'trv_ac' || $(this).attr('id') == 'trv_op') {
          $(this).hide();
        } else {
          $(this).show();
        }
      });
      var $percent_td = form.find("#v_percent");
      var $percent_div = $percent_td.find("span>div").css("width", "80%");
      $percent_div.find('.progress-bar').css('margin-top', '0px');
      $percent_td.html($percent_div);
    }

    function style_edit_form(formid) {
      style_form_header(formid);
      style_edit_form_buttons(formid);
      var form = $(formid[0]);
      //是否启用控件样式修改
      var enable_input = form.find('input[name=enable]');
      enable_input.addClass('ace ace-switch ace-switch-6').after(
          '<span class="lbl" style="display: inline-block;"></span>');
      //修改选择框的样式
      var db_select = form.find("#db").prepend('<option>').addClass("chosen-select")
      .attr("data-placeholder", "选择一个数据源");
      if (db_select.attr('rowid') == '_empty') {
        form.find("#db").find('option:first-child').attr('selected', 'selected');
      }
      //重新定位居中
      centeredDialog(form);
    }

    function beforeInitEditData(formid) {
      var id = selectLastRowOnly(grid_selector);
      var rowData = $(grid_selector).jqGrid('getGridRowData', id);
      var st = JobStatus.valueOf(rowData.status);
      if (!rowData.enable || st === JobStatus.FAILED || st === JobStatus.PROCESSED) {
        return true;
      }
      layer.confirm('任务还在执行中，如果要进行修改，必须先停止该任务。是否停止该任务？',
          {title: '提示', btn: ['停止并修改', '取消']}, function (index) {
            layer.close(index);
            operateJob(id, 'stop', function (result) {
              if (!result.success) {
                layer.msg('任务停止失败了，等一下再试吧！', {icon: 5});
              } else {
                $(grid_selector).jqGrid('editGridRow', id, formEditOptions);
              }
            });
          });
      return false;   //返回false取消展示编辑框
    }

    function beforeShowEditForm(formid) {
      style_edit_form(formid);
    }

    function afterShowEditForm(formid) {
      var form = $(formid[0]);
      var id = form.find("input#id_g").val();
      initChosenSelect();
      //initNumberSpinner();
      //编辑框是否启用的默认值
      var rowData = $(grid_selector).jqGrid('getGridRowData', id);
      var checked = !!rowData && rowData.enable;
      form.find('input#enable').attr("checked", checked);
    }

    //定时刷新没有处理完毕的任务信息
    function refreshUnprocessed() {
      !!refreshTimer && clearInterval(refreshTimer);
      refreshTimer = setInterval(function () {
        var ids = [];
        $.each($(grid_selector).jqGrid('getGridData'), function (id, v) {
          var st = JobStatus.valueOf(v.status);
          if (v.enable && st !== JobStatus.FAILED && st !== JobStatus.PROCESSED) {
            ids.push(id);
          }
        });
        if (ids.length === 0) {
          return;
        }
        $.post('${ctx}/offline/refresh', {'ids': ids.toString()}, function (result) {
          if (!!result.success) {
            $.each(result.data, function (i, v) {
              $(grid_selector).jqGrid('setCell', v.id, 'status', v.status);
              $(grid_selector).jqGrid('setCell', v.id, 'op', v.op);
              $(grid_selector).jqGrid('setCell', v.id, 'startTime', v.startTime);
              $(grid_selector).jqGrid('setCell', v.id, 'elapsed', v.elapsed);
              $(grid_selector).jqGrid('setCell', v.id, 'modifiedTime', v.modifiedTime);
              var percent = Math.floor(v.percent * 1000)/10 + '%';
              var $progress_bar = $('tr#' + v.id).children(
                  '[aria-describedby="grid-table_percent"]')
              .find(".progress-bar").css('width', percent).text(percent);
              if (v.percent < 1.0) {
                if (!$progress_bar.hasClass('active')) {
                  $progress_bar.addClass("progress-bar-striped progress-bar-animated active");
                }
              } else {
                $progress_bar.removeClass('progress-bar-striped progress-bar-animated active');
              }
              //更新对应的行数据
              $(grid_selector).jqGrid('setGridRowData', v.id, v);
            });
          }
        });
      }, 5000);
    }

    $(document).one('ajaxloadstart.page', function (e) {
      $.jgrid.gridDestroy(grid_selector);
      $('.ui-jqdialog').remove();
    });

  });

  /**
   * 任务操作：启动、停止、重新执行
   * @param id 操作的行id
   * @param op 操作类型
   * @param callback 操作后的回调，callback第一个参数是操作成功与否的布尔值，第二个参数是服务器返回的ajax对象
   */
  function operateJob(id, op, callback) {
    if (!!!id || !!!op) {
      layer.alert('参数不正确，必须指定操作类型和被操作的任务id！', {icon: 5, title: '错误'});
      return;
    }
    //防止重复点击
    var $opBtn = $('#jOpButton_' + id);
    if ($opBtn.attr('clicked')) {
      return;
    } else {
      $opBtn.attr('clicked', true);
    }
    var index = layer.msg('正在提交操作，请稍等...', {icon: 16, shade: 0.01});
    $.post('${ctx}/offline/op-run', {"id": id, "op": op}, function (result) {
      if (!!result.success) {
        layer.close(index);
        layer.msg(result.msg);
        if (!!result.data) {
          $(grid_selector).jqGrid('setRowData', result.data.id, result.data);
          $(grid_selector).jqGrid('setGridRowData', result.data.id, result.data);
        } else {
          $(grid_selector).trigger('reloadGrid');
        }
        if (typeof callback == "function") {
          callback.call(null, result);
        }
      } else {
        if (typeof callback == "function") {
          callback.call(null, result);
        } else {
          layer.alert(result.msg, {icon: 5, title: "失败"});
        }
      }
    });
  }
</script>
