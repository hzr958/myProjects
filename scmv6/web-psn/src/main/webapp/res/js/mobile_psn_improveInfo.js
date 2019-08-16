/**
 * 比较起止时间
 * 
 * @returns {Boolean}
 */
function compareEduTime() {
  var startYear = parseInt($('#edu_from_year option:selected').val());
  var startMonth = parseInt($('#edu_from_month option:selected').val());
  var toYear = parseInt($('#edu_to_year option:selected').val());
  var toMonth = parseInt($('#edu_to_month option:selected').val());
  if (startYear > toYear) {
    $("#edu_to_year").parent().find("p.eduMsg").html("请选择正确的起止时间");
    return false;
  } else if (startYear == toYear) {
    if (toMonth <= startMonth) {
      $("#edu_to_year").parent().find("p.eduMsg").html("请选择正确的起止时间");
      return false;
    }
  }
  return true;
}

/**
 * 构造下拉年份
 */
function setYear() {
  var start = 1990;
  var nowDate = new Date();
  var end = nowDate.getFullYear();
  var years = "";
  for (var i = start; i <= end; i++) {
    years += "<option value='" + i + "'>" + i + "年</option>";
  }
  $("#work_from_year").html(years);
  $("#edu_from_year").html(years);
  $("#edu_to_year").html(years);
}

/**
 * 构造下拉月份
 */
function setMonth() {
  var months = "";
  for (var i = 1; i < 13; i++) {
    months += "<option value='" + i + "'>" + i + "月</option>";
  }
  $(".month").html(months);
}