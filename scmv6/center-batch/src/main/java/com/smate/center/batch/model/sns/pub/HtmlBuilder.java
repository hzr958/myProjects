package com.smate.center.batch.model.sns.pub;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.commons.lang.StringUtils;

/**
 * Will build up an Html String piece by piece.
 * 
 * Note: unless otherwise noted if the parameter used for any of the methods is null or empty then
 * that html element will not be appended. The advantage of this is you do not have to do any null
 * or empty string checking to use this class.
 * 
 * @author Jeff Johnston
 */
public class HtmlBuilder {
  private Writer writer;

  /**
   * Default constructor using a StringWriter, which is really just a StringBuffer.
   */
  public HtmlBuilder() {
    this.writer = new StringWriter();
  }

  /**
   * A builder with the specified Writer.
   * 
   * @param writer The Writer to use.
   */
  public HtmlBuilder(Writer writer) {
    this.writer = writer;
  }

  /**
   * Flush the writer.
   */
  public void flushBuilder() {
    try {
      writer.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Close the writer.
   */
  public void closeBuilder() {
    try {
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Write out the content to the internal writer.
   */
  private HtmlBuilder write(String text) {
    try {
      writer.write(text);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return this;
  }

  /**
   * The length of the internal Writer.
   */
  public int length() {
    return writer.toString().length();
  }

  /**
   * The Object to append. Will call the Object's toString() method.
   */
  public HtmlBuilder append(Object text) {
    if (text != null) {
      write(text.toString());
    }

    return this;
  }

  /**
   * The String to append.
   */
  public HtmlBuilder append(String text) {
    write(text);

    return this;
  }

  /**
   * <p>
   * Append tabs [\t] and newlines [\n].
   * </p>
   * 
   * @param tabs The number of tab spaces \t to put in.
   * @param newlines The number of newlines \n to put in.
   */
  public HtmlBuilder format(int tabs, int newlines) {
    tabs(tabs);
    newlines(newlines);

    return this;
  }

  /**
   * <p>
   * Append tabs.
   * </p>
   * 
   * @param tabs The number of tab spaces [\t] to put in.
   */
  public HtmlBuilder tabs(int tabs) {
    for (int i = 0; i < tabs; i++) {
      tab();
    }

    return this;
  }

  /**
   * <p>
   * Append newlines [\n].
   * </p>
   * 
   * @param newlines The number of newlines \n to put in.
   */
  public HtmlBuilder newlines(int newlines) {
    for (int i = 0; i < newlines; i++) {
      newline();
    }

    return this;
  }

  /**
   * <p>
   * Append tab [\t].
   * </p>
   */
  public HtmlBuilder tab() {
    write("\t");

    return this;
  }

  /**
   * <p>
   * Append newline [\n].
   * </p>
   */
  public HtmlBuilder newline() {
    write("\n");

    return this;
  }

  /**
   * <p>
   * Close the element [>]
   * </p>
   */
  public HtmlBuilder close() {
    write(">");

    return this;
  }

  /**
   * <p>
   * Close the element with a slash to comply with xhtml [/>]
   * </p>
   */
  public HtmlBuilder xclose() {
    write("/>");

    return this;
  }

  /**
   * <p>
   * The start of the table element [ <table ].
   * </p>
   * 
   * <p>
   * Also appends a newline [\n] and the specified number of tabs [\t] before the table.
   * </p>
   * 
   * @param tabs The number of tab spaces [\t] to put in.
   */
  public HtmlBuilder table(int tabs) {
    newline();
    tabs(tabs);
    write("<table");

    return this;
  }

  /**
   * <p>
   * The close tag of the table element [
   * </table>
   * ].
   * </p>
   * 
   * <p>
   * Also appends a newline [\n] and the specified number of tabs [\t] before the table ends.
   * </p>
   * 
   * @param tabs The number of tab spaces [\t] to put in.
   */
  public HtmlBuilder tableEnd(int tabs) {
    newline();
    tabs(tabs);
    write("</table>");

    return this;
  }

  /**
   * <p>
   * The start of the button element [<button].
   * </p>
   */
  public HtmlBuilder button() {
    write("<button");
    return this;
  }

  /**
   * <p>
   * The close tag of the button element [</button>].
   * </p>
   */
  public HtmlBuilder buttonEnd() {
    write("</button>");

    return this;
  }

  /**
   * <p>
   * The start of the tr element [ <tr ].
   * </p>
   * 
   * <p>
   * Also appends a newline [\n] and the specified number of tabs [\t] before the tr.
   * </p>
   * 
   * @param tabs The number of tab spaces [\t] to put in.
   */
  public HtmlBuilder tr(int tabs) {
    newline();
    tabs(tabs);
    write("<tr");

    return this;
  }

  /**
   * <p>
   * The close tag of the tr element [
   * </tr>
   * ].
   * </p>
   * 
   * <p>
   * Also appends a newline [\n] and the specified number of tabs [\t] before the tr ends.
   * </p>
   * 
   * @param tabs The number of tab spaces [\t] to put in.
   */
  public HtmlBuilder trEnd(int tabs) {
    newline();
    tabs(tabs);
    write("</tr>");

    return this;
  }

  /**
   * <p>
   * The start of the th element [ <th].
   * </p>
   * 
   * <p>
   * Also appends a newline [\n] and the specified number of tabs [\t] before the th.
   * </p>
   * 
   * @param tabs The number of tab spaces [\t] to put in.
   */
  public HtmlBuilder th(int tabs) {
    newline();
    tabs(tabs);
    write("<th");

    return this;
  }

  /**
   * <p>
   * The close tag of the th element [</th>].
   * </p>
   */
  public HtmlBuilder thEnd() {
    write("</th>");

    return this;
  }

  /**
   * <p>
   * The start of the td element [ <td].
   * </p>
   * 
   * <p>
   * Also appends a newline [\n] and the specified number of tabs [\t] before the td.
   * </p>
   * 
   * @param tabs The number of tab spaces [\t] to put in.
   */
  public HtmlBuilder td(int tabs) {
    newline();
    tabs(tabs);
    write("<td");

    return this;
  }

  /**
   * <p>
   * The close tag of the td element [</td>].
   * </p>
   */
  public HtmlBuilder tdEnd() {
    write("</td>");

    return this;
  }

  /**
   * <p>
   * The start of the input element [<input].
   * </p>
   */
  public HtmlBuilder input() {
    write("<input");

    return this;
  }

  /**
   * <p>
   * The type attribute [type=].
   * </p>
   */
  public HtmlBuilder type(String type) {
    if (StringUtils.isNotBlank(type)) {
      write(" type=\"").write(type).write("\" ");
    }

    return this;
  }

  /**
   * <p>
   * Combines the start of the input element and the type attribute [<input type=].
   * </p>
   */
  public HtmlBuilder input(String type) {
    write("<input type=\"").write(type).write("\" ");

    return this;
  }

  /**
   * <p>
   * The start of the select element [<select].
   * </p>
   */
  public HtmlBuilder select() {
    write("<select");

    return this;
  }

  /**
   * <p>
   * The close tag of the select element [</select>].
   * </p>
   */
  public HtmlBuilder selectEnd() {
    write("</select>");

    return this;
  }

  /**
   * <p>
   * The start of the option element [<option].
   * </p>
   */
  public HtmlBuilder option() {
    write("<option");

    return this;
  }

  /**
   * <p>
   * The close tag of the option element [</option>].
   * </p>
   */
  public HtmlBuilder optionEnd() {
    write("</option>");

    return this;
  }

  /**
   * <p>
   * The start of the form element [<form].
   * </p>
   * 
   * <p>
   * Also appends a newline [\n] before the form.
   * </p>
   */
  public HtmlBuilder form() {
    newline();
    write("<form");

    return this;
  }

  /**
   * <p>
   * The close tag of the form element [</form>].
   * </p>
   * 
   * <p>
   * Also appends a newline [\n] before the end.
   * </p>
   */
  public HtmlBuilder formEnd() {
    newline();
    write("</form>");

    return this;
  }

  /**
   * <p>
   * The name attribute [name=].
   * </p>
   */
  public HtmlBuilder name(String name) {
    if (StringUtils.isNotBlank(name)) {
      write(" name=\"").write(name).write("\" ");
    }

    return this;
  }

  /**
   * <p>
   * The value attribute [value=].
   * </p>
   * 
   * <p>
   * If the value parameter is null or empty then will append a empty value element.
   * </p>
   */
  public HtmlBuilder value(String value) {
    if (StringUtils.isNotBlank(value)) {
      write(" value=\"").write(value).write("\" ");
    } else {
      write(" value=\"").write("\" ");
    }

    return this;
  }

  /**
   * <p>
   * The title attribute [title=].
   * </p>
   */
  public HtmlBuilder title(String title) {
    if (StringUtils.isNotBlank(title)) {
      write(" title=\"").write(title).write("\" ");
    }

    return this;
  }

  /**
   * <p>
   * The action attribute [action=].
   * </p>
   */
  public HtmlBuilder action(String action) {
    write(" action=\"");
    if (StringUtils.isNotBlank(action)) {
      write(action);
    }
    write("\" ");

    return this;
  }

  /**
   * <p>
   * The method attribute [method=].
   * </p>
   */
  public HtmlBuilder method(String method) {
    if (StringUtils.isNotBlank(method)) {
      write(" method=\"").write(method).write("\" ");
    }

    return this;
  }

  /**
   * <p>
   * The enctype attribute [enctype=].
   * </p>
   */
  public HtmlBuilder enctype(String enctype) {
    if (StringUtils.isNotBlank(enctype)) {
      write(" enctype=\"").write(enctype).write("\" ");
    }

    return this;
  }

  /**
   * <p>
   * The onchange attribute [onchange=].
   * </p>
   */
  public HtmlBuilder onchange(String onchange) {
    if (StringUtils.isNotBlank(onchange)) {
      write(" onchange=\"").write(onchange).write("\" ");
    }

    return this;
  }

  /**
   * <p>
   * The onsubmit attribute [onsubmit=].
   * </p>
   */
  public HtmlBuilder onsubmit(String onsubmit) {
    if (StringUtils.isNotBlank(onsubmit)) {
      write(" onsubmit=\"").write(onsubmit).write("\" ");
    }

    return this;
  }

  /**
   * <p>
   * The onclick attribute [onclick=].
   * </p>
   */
  public HtmlBuilder onclick(String onclick) {
    if (StringUtils.isNotBlank(onclick)) {
      write(" onclick=\"").write(onclick).write("\" ");
    }

    return this;
  }

  /**
   * <p>
   * The onmouseover attribute [onmouseover=].
   * </p>
   */
  public HtmlBuilder onmouseover(String onmouseover) {
    if (StringUtils.isNotBlank(onmouseover)) {
      write(" onmouseover=\"").write(onmouseover).write("\" ");
    }

    return this;
  }

  /**
   * <p>
   * The onmouseout attribute [onmouseout=].
   * </p>
   */
  public HtmlBuilder onmouseout(String onmouseout) {
    if (StringUtils.isNotBlank(onmouseout)) {
      write(" onmouseout=\"").write(onmouseout).write("\" ");
    }

    return this;
  }

  /**
   * <p>
   * The onkeypress attribute [onkeypress=].
   * </p>
   */
  public HtmlBuilder onkeypress(String onkeypress) {
    if (StringUtils.isNotBlank(onkeypress)) {
      write(" onkeypress=\"").write(onkeypress).write("\" ");
    }

    return this;
  }

  /**
   * <p>
   * The id attribute [id=].
   * </p>
   */
  public HtmlBuilder id(String id) {
    if (StringUtils.isNotBlank(id)) {
      write(" id=\"").write(id).write("\" ");
    }

    return this;
  }

  /**
   * <p>
   * The class attribute [class=].
   * </p>
   */
  public HtmlBuilder styleClass(String styleClass) {
    if (StringUtils.isNotBlank(styleClass)) {
      write(" class=\"").write(styleClass).write("\" ");
    }

    return this;
  }

  /**
   * <p>
   * The style attribute [style=].
   * </p>
   */
  public HtmlBuilder style(String style) {
    if (StringUtils.isNotBlank(style)) {
      write(" style=\"").write(style).write("\" ");
    }

    return this;
  }

  /**
   * <p>
   * The width attribute [width=].
   * </p>
   */
  public HtmlBuilder width(String width) {
    if (StringUtils.isNotBlank(width)) {
      write(" width=\"").write(width).write("\" ");
    }

    return this;
  }

  /**
   * <p>
   * The align attribute [align=].
   * </p>
   */
  public HtmlBuilder align(String align) {
    if (StringUtils.isNotBlank(align)) {
      write(" align=\"").write(align).write("\" ");
    }

    return this;
  }

  /**
   * <p>
   * The valign attribute [valign=].
   * </p>
   */
  public HtmlBuilder valign(String valign) {
    if (StringUtils.isNotBlank(valign)) {
      write(" valign=\"").write(valign).write("\" ");
    }

    return this;
  }

  /**
   * <p>
   * The border attribute [border=].
   * </p>
   */
  public HtmlBuilder border(String border) {
    if (StringUtils.isNotBlank(border)) {
      write(" border=\"").write(border).write("\" ");
    }

    return this;
  }

  /**
   * <p>
   * The cellpadding attribute [cellpadding=].
   * </p>
   */
  public HtmlBuilder cellPadding(String cellPadding) {
    if (StringUtils.isNotBlank(cellPadding)) {
      write(" cellpadding=\"").write(cellPadding).write("\" ");
    }

    return this;
  }

  /**
   * <p>
   * The cellspacing attribute [cellspacing=].
   * </p>
   */
  public HtmlBuilder cellSpacing(String cellSpacing) {
    if (StringUtils.isNotBlank(cellSpacing)) {
      write(" cellspacing=\"").write(cellSpacing).write("\" ");
    }

    return this;
  }

  /**
   * <p>
   * The colspan attribute [colspan=].
   * </p>
   */
  public HtmlBuilder colSpan(String colspan) {
    if (StringUtils.isNotBlank(colspan)) {
      write(" colspan=\"").write(colspan).write("\" ");
    }

    return this;
  }

  /**
   * <p>
   * The rowspan attribute [rowspan=].
   * </p>
   */
  public HtmlBuilder rowSpan(String rowspan) {
    if (StringUtils.isNotBlank(rowspan)) {
      write(" rowspan=\"").write(rowspan).write("\" ");
    }

    return this;
  }

  /**
   * <p>
   * The size attribute [size=].
   * </p>
   */
  public HtmlBuilder size(String size) {
    if (StringUtils.isNotBlank(size)) {
      write(" size=\"").write(size).write("\" ");
    }

    return this;
  }

  /**
   * <p>
   * The start of the span element [<span].
   * </p>
   */
  public HtmlBuilder span() {
    write("<span");

    return this;
  }

  /**
   * <p>
   * The close tag of the span element [</span>].
   * </p>
   */
  public HtmlBuilder spanEnd() {
    write("</span>");

    return this;
  }

  /**
   * <p>
   * The start of the div element [<div].
   * </p>
   */
  public HtmlBuilder div() {
    write("<div");

    return this;
  }

  /**
   * <p>
   * The close tag of the div element [</div>].
   * </p>
   */
  public HtmlBuilder divEnd() {
    write("</div>");

    return this;
  }

  /**
   * <p>
   * A URL parameter name/value [name=value]
   * </p>
   */
  public HtmlBuilder param(String name, String value) {
    append(name);
    equals();
    append(value);

    return this;
  }

  /**
   * <p>
   * The start of the a element plus the href attribute [<a href=].
   * </p>
   */
  public HtmlBuilder a(String href) {
    append("<a href=");
    quote();
    append(href);
    quote();

    return this;
  }

  /**
   * <p>
   * The start of the a element plus the href attribute [<a href=].
   * </p>
   */
  public HtmlBuilder a() {
    write("<a href=");

    return this;
  }

  /**
   * <p>
   * The close tag of the a element [</div>].
   * </p>
   */
  public HtmlBuilder aEnd() {
    write("</a>");

    return this;
  }

  /**
   * <p>
   * The bold element [<b>].
   * </p>
   */
  public HtmlBuilder bold() {
    write("<b>");

    return this;
  }

  /**
   * <p>
   * The close tag of the bold element [</b>].
   * </p>
   */
  public HtmlBuilder boldEnd() {
    write("</b>");

    return this;
  }

  /**
   * <p>
   * A single quote ["].
   * </p>
   */
  public HtmlBuilder quote() {
    write("\"");

    return this;
  }

  /**
   * <p>
   * A single question mark [?].
   * </p>
   */
  public HtmlBuilder question() {
    write("?");

    return this;
  }

  /**
   * <p>
   * A single equals [=].
   * </p>
   */
  public HtmlBuilder equals() {
    write("=");

    return this;
  }

  /**
   * <p>
   * A single ampersand [&].
   * </p>
   */
  public HtmlBuilder ampersand() {
    write("&");

    return this;
  }

  /**
   * <p>
   * The start of the img element [<img].
   * </p>
   */
  public HtmlBuilder img() {
    write("<img");

    return this;
  }

  /**
   * <p>
   * The src attribute [src=].
   * </p>
   */
  public HtmlBuilder src(String src) {
    if (StringUtils.isNotBlank(src)) {
      write(" src=\"").write(src).write("\" ");
    }

    return this;
  }

  /**
   * <p>
   * The alt attribute [alt=].
   * </p>
   */
  public HtmlBuilder alt(String alt) {
    if (StringUtils.isNotBlank(alt)) {
      write(" alt=\"").write(alt).write("\" ");
    }

    return this;
  }

  /**
   * <p>
   * The start of the src element plus the src attribute [<img src= style=border:0/>].
   * </p>
   */
  public HtmlBuilder img(String src) {
    write("<img src=\"").write(src).write("\" style=\"border:0\"/> ");

    return this;
  }

  /**
   * <p>
   * The start of the src element plus the src attribute [<img src="" tooltip="" style="border:0">].
   * </p>
   */
  public HtmlBuilder img(String img, String tooltip) {
    write("<img src=\"").write(img).write("\" style=\"border:0;vertical-align: text-bottom\"");

    if (tooltip != null) {
      write(" title=\"").write(tooltip).write("\">");
    }

    return this;
  }

  /**
   * <p>
   * The start of the textarea element [<textarea].
   * </p>
   */
  public HtmlBuilder textarea() {
    write("<textarea");

    return this;
  }

  /**
   * <p>
   * The close tag of the textarea element [</textarea>].
   * </p>
   */
  public HtmlBuilder textareaEnd() {
    write("</textarea>");

    return this;
  }

  /**
   * <p>
   * The cols attribute [cols=].
   * </p>
   */
  public HtmlBuilder cols(String cols) {
    if (StringUtils.isNotBlank(cols)) {
      write(" cols=\"").write(cols).write("\" ");
    }

    return this;
  }

  /**
   * <p>
   * The rows attribute [rows=].
   * </p>
   */
  public HtmlBuilder rows(String rows) {
    if (StringUtils.isNotBlank(rows)) {
      write(" rows=\"").write(rows).write("\" ");
    }

    return this;
  }

  /**
   * <p>
   * The checked attribute [checked="checked"].
   * </p>
   */
  public HtmlBuilder checked() {
    write(" checked=\"checked\"");

    return this;
  }

  /**
   * <p>
   * The selected attribute [selected="selected"].
   * </p>
   */
  public HtmlBuilder selected() {
    write(" selected=\"selected\"");

    return this;
  }

  /**
   * <p>
   * The readonly attribute [readonly="readonly"].
   * </p>
   */
  public HtmlBuilder readonly() {
    write(" readonly=\"readonly\"");

    return this;
  }

  /**
   * <p>
   * The non-breaking space [&nbsp;].
   * </p>
   */
  public HtmlBuilder nbsp() {
    write("&#160;");

    return this;
  }

  /**
   * <p>
   * The comment [<!-- -->].
   * </p>
   */
  public HtmlBuilder comment(String comment) {
    if (StringUtils.isNotBlank(comment)) {
      write(" <!-- ").write(comment).write(" -->");
    }

    return this;
  }

  /**
   * <p>
   * The ul element [
   * <ul>
   * ].
   * </p>
   */
  public HtmlBuilder ul() {
    write("<ul>");

    return this;
  }

  /**
   * <p>
   * The close tag of the ul element [
   * </ul>
   * ].
   * </p>
   */
  public HtmlBuilder ulEnd() {
    write("</ul>");

    return this;
  }

  /**
   * <p>
   * The li element [
   * <li></li>].
   * </p>
   */
  public HtmlBuilder li(String text) {
    if (StringUtils.isNotBlank(text)) {
      write("<li>").write(text).write("</li>");
    }

    return this;
  }

  /**
   * <p>
   * The br element [<br/>
   * ].
   * </p>
   */
  public HtmlBuilder br() {
    write("<br/>");

    return this;
  }

  /**
   * <p>
   * The disabled attribute [disabled="disabled"].
   * </p>
   */
  public HtmlBuilder disabled() {
    write(" disabled=\"disabled\" ");

    return this;
  }

  /**
   * <p>
   * The nowrap attribute [nowrap="nowrap"].
   * </p>
   */
  public HtmlBuilder nowrap() {
    write(" nowrap=\"nowrap\" ");

    return this;
  }

  /**
   * <p>
   * The maxlength attribute [maxlength=].
   * </p>
   */
  public HtmlBuilder maxlength(String maxlength) {
    if (StringUtils.isNotBlank(maxlength)) {
      write(" maxlength=\"").write(maxlength).write("\" ");
    }

    return this;
  }

  /**
   * <p>
   * The start of the tbody element [<tbody].
   * </p>
   */
  public HtmlBuilder tbody(int tabs) {
    newline();
    tabs(tabs);
    write("<tbody");

    return this;
  }

  /**
   * <p>
   * The end tag of the tbody element [</tbody>].
   * </p>
   * 
   * <p>
   * Also appends a newline [\n] and the specified number of tabs [\t] before the </tbody>.
   * </p>
   * 
   * @param tabs The number of tab spaces [\t] to put in.
   */
  public HtmlBuilder tbodyEnd(int tabs) {
    newline();
    tabs(tabs);
    write("</tbody>");

    return this;
  }

  /**
   * <p>
   * The start of the thead element [<thead].
   * </p>
   */
  public HtmlBuilder thead(int tabs) {
    newline();
    tabs(tabs);
    write("<thead");

    return this;
  }

  /**
   * <p>
   * The end tag of the thead element [</thead>].
   * </p>
   * 
   * <p>
   * Also appends a newline [\n] and the specified number of tabs [\t] before the </thead>.
   * </p>
   * 
   * @param tabs The number of tab spaces [\t] to put in.
   */
  public HtmlBuilder theadEnd(int tabs) {
    newline();
    tabs(tabs);
    write("</thead>");

    return this;
  }

  /**
   * <p>
   * The start of the p element [ <p ].
   * </p>
   */
  public HtmlBuilder p() {
    write("<p");

    return this;
  }

  /**
   * <p>
   * The close tag of the p element [
   * </p>
   * ].
   * </p>
   */
  public HtmlBuilder pEnd() {
    write("</p>");

    return this;
  }

  /**
   * <p>
   * The start of the h1 element [ <h1].
   * </p>
   */
  public HtmlBuilder h1() {
    write("<h1");

    return this;
  }

  /**
   * <p>
   * The close tag of the h1 element [</h1>].
   * </p>
   */
  public HtmlBuilder h1End() {
    write("</h1>");

    return this;
  }

  /**
   * <p>
   * The start of the h2 element [ <h2].
   * </p>
   */
  public HtmlBuilder h2() {
    write("<h2");

    return this;
  }

  /**
   * <p>
   * The close tag of the h2 element [</h2>].
   * </p>
   */
  public HtmlBuilder h2End() {
    write("</h2>");

    return this;
  }

  /**
   * <p>
   * The start of the h3 element [ <h3].
   * </p>
   */
  public HtmlBuilder h3() {
    write("<h3");

    return this;
  }

  /**
   * <p>
   * The close tag of the h3 element [</h3>].
   * </p>
   */
  public HtmlBuilder h3End() {
    write("</h3>");

    return this;
  }

  /**
   * <p>
   * The start of the h4 element [ <h4].
   * </p>
   */
  public HtmlBuilder h4() {
    write("<h4");

    return this;
  }

  /**
   * <p>
   * The close tag of the h4 element [</h4>].
   * </p>
   */
  public HtmlBuilder h4End() {
    write("</h4>");

    return this;
  }

  /**
   * <p>
   * The start of the h5 element [ <h5].
   * </p>
   */
  public HtmlBuilder h5() {
    write("<h5");

    return this;
  }

  /**
   * <p>
   * The close tag of the h5 element [</h5>].
   * </p>
   */
  public HtmlBuilder h5End() {
    write("</h5>");

    return this;
  }

  public String toString() {
    return writer.toString();
  }
}
