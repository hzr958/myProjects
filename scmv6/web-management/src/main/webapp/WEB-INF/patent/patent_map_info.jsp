<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>专利地图</title>
<style>
.domain {
  display: none;
}
</style>
<svg width="920" height="500"></svg>
<script>
var svg = d3.select("svg"),
    width = +svg.attr("width"),
    height = +svg.attr("height"),
    margin = {top: 20, right: 30, bottom: 30, left: 40};

var x = d3.scaleLinear()
    .rangeRound([margin.left, width - margin.right]);

var y = d3.scaleLinear()
    .rangeRound([height - margin.bottom, margin.top]);

var category='${tsvDataName}';
//alert(category);

//d3.tsv("/resmod/information.tsv", function(d) {
d3.tsv("/resmod/"+category, function(d) {
  d.eruptions = +d.eruptions;
  d.waiting = +d.waiting;
  return d;
}, function(error, faithful) {
  if (error) throw error;

  x.domain(d3.extent(faithful, function(d) { return d.waiting; })).nice();
  y.domain(d3.extent(faithful, function(d) { return d.eruptions; })).nice();

  svg.insert("g", "g")
      .attr("fill", "none")
      .attr("stroke", "steelblue")
      .attr("stroke-linejoin", "round")
    .selectAll("path")
    .data(d3.contourDensity()
        .x(function(d) { return x(d.waiting); })
        .y(function(d) { return y(d.eruptions); })
        .size([width, height])
        .bandwidth(40)
      (faithful))
    .enter().append("path")
      .attr("d", d3.geoPath());

  svg.append("g")
      .attr("stroke", "white")
    .selectAll("circle")
    .data(faithful)
    .enter().append("circle")
      .attr("cx", function(d) { return x(d.waiting); })
      .attr("cy", function(d) { return y(d.eruptions); })
      .attr("r", 2);

  svg.append("g")
      .attr("transform", "translate(0," + (height - margin.bottom) + ")")
      .call(d3.axisBottom(x))
    .select(".tick:last-of-type text")
    .select(function() { return this.parentNode.appendChild(this.cloneNode()); })
      .attr("y", -3)
      .attr("dy", null)
      .attr("font-weight", "bold")
      .text("Idle (min.)");

  svg.append("g")
      .attr("transform", "translate(" + margin.left + ",0)")
      .call(d3.axisLeft(y))
    .select(".tick:last-of-type text")
    .select(function() { return this.parentNode.appendChild(this.cloneNode()); })
      .attr("x", 3)
      .attr("text-anchor", "start")
      .attr("font-weight", "bold")
      .text("Erupting (min.)");
});

</script>
</html>