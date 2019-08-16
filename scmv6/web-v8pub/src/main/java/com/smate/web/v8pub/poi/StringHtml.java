package com.smate.web.v8pub.poi;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *html字符串处理.
 * 
 * @author chenxiangrong
 * 
 */
public class StringHtml {
	/**
	 * 功能：去掉所有的<*>标记,去除html标签.
	 * 
	 * @param content
	 * @return
	 */
	public static String removeTagFromText(String content) {
		Pattern p = null;
		Matcher m = null;
		String value = null;

		// 去掉<>标签
		p = Pattern.compile("(<[^>]*>)");
		m = p.matcher(content);
		String temp = content;
		while (m.find()) {
			value = m.group(0);
			temp = temp.replace(value, "");
		}

		// 去掉换行或回车符号
		p = Pattern.compile("(\r+|\n+)");
		m = p.matcher(temp);
		while (m.find()) {
			value = m.group(0);
			temp = temp.replace(value, " ");
		}
		return temp;
	}

	/**
	 * 转换成html格式.
	 * 
	 * @param str
	 * @return
	 */
	public static String toHtmlInput(String str) {
		if (str == null)
			return null;
		String html = new String(str);
		html = replace(html, "&", "&amp;");
		html = replace(html, "<", "&lt;");
		html = replace(html, ">", "&gt;");

		return html;
	}

	public static String replace(String source, String oldString, String newString) {
		StringBuffer output = new StringBuffer();
		int lengthOfSource = source.length();
		int lengthOfOld = oldString.length();
		int posStart = 0;
		int pos; // 
		while ((pos = source.indexOf(oldString, posStart)) >= 0) {
			output.append(source.substring(posStart, pos));
			output.append(newString);
			posStart = pos + lengthOfOld;
		}
		if (posStart < lengthOfSource) {
			output.append(source.substring(posStart));
		}
		return output.toString();
	}

	/**
	 * 针对word中特殊处理.
	 * 
	 * @param content
	 * @return
	 */
	public static String wordHandler(String content) {
		if (content == null) {
			return "";
		}

		content = content.replaceAll("&alpha;", "α");
		content = content.replaceAll("&Alpha;", "Α");

		content = content.replaceAll("&beta;", "β");
		content = content.replaceAll("&Beta;", "Β");

		content = content.replace("&gamma;", "γ");
		content = content.replace("&Gamma;", "Γ");

		content = content.replace("&delta;", "δ");
		content = content.replace("&Delta;", "Δ");

		content = content.replace("&kappa;", "κ");
		content = content.replace("&μ;", "μ");
		content = content.replace("&Mu;", "Μ");
		content = content.replace("&infin;", "∞");
		content = content.replace("&deg;", "°");

		content = content.replace("&quot;", "\"");
		content = content.replace("&ldquo;", "“");
		content = content.replace("&rdquo;", "”");

		content = content.replace("&mdash;", "—");
		content = content.replace("&hellip;", "…");

		// 换行符
		content = content.replace("<br>", "#n#");
		// 空格
		content = content.replace("&nbsp;", " ");
		// 加粗
		content = content.replace("&lt;strong&gt;", "");
		content = content.replace("&lt;/strong&gt;", "");
		content = content.replace("<strong>", "");
		content = content.replace("</strong>", "");
		// 尖括号
		content = content.replace("<", "#<#");
		content = content.replace(">", "#>#");
		content = content.replace("&lt;", "#<#");
		content = content.replace("&gt;", "#>#");
		content = content.replace("&amp;", "#11#");
		// &
		content = content.replace("&", "&amp;");
		// 回改尖括号
		content = content.replace("#<#", "&lt;");
		content = content.replace("#>#", "&gt;");
		content = content.replace("#11#", "&amp;");
		// 换行符
		content = content.replace("#n#", "<w:br/>");

		return content;
	}

	/**
	 * 针对word中特殊处理.
	 * 
	 * @param content
	 * @return
	 */
	public static String txtHandler(String content) {
		if (content == null) {
			return "";
		}

		content = content.replaceAll("&alpha;", "α");
		content = content.replaceAll("&Alpha;", "Α");

		content = content.replaceAll("&beta;", "β");
		content = content.replaceAll("&Beta;", "Β");

		content = content.replace("&gamma;", "γ");
		content = content.replace("&Gamma;", "Γ");

		content = content.replace("&delta;", "δ");
		content = content.replace("&Delta;", "Δ");

		content = content.replace("&kappa;", "κ");
		content = content.replace("&μ;", "μ");
		content = content.replace("&Mu;", "Μ");
		content = content.replace("&infin;", "∞");
		content = content.replace("&deg;", "°");

		content = content.replace("&quot;", "\"");
		content = content.replace("&ldquo;", "“");
		content = content.replace("&rdquo;", "”");

		content = content.replace("&mdash;", "—");
		content = content.replace("&hellip;", "…");

		// 换行符
		content = content.replace("<br>", "#n#");
		// 空格
		content = content.replace("&nbsp;", " ");
		// 加粗
		content = content.replace("&lt;strong&gt;", "");
		content = content.replace("&lt;/strong&gt;", "");
		content = content.replace("<strong>", "");
		content = content.replace("</strong>", "");

		content = content.replace("&lt;", "<");
		content = content.replace("&gt;", ">");
		content = content.replace("&amp;", "&");

		return content;
	}

	public static String splitString(String content, int length) {
		StringBuffer newContent = new StringBuffer();
		while (content.length() > length) {
			newContent.append(content.substring(0, length) + "<w:br/>");
			content = content.substring(length);
		}
		if ("".equals(newContent.toString())) {
			return content;
		} else {
			newContent.append(content);
			return newContent.toString();
		}
	}

	public static String symbolHandler(String str, String symbol) {
		if (str != null && !"".equals(str))
			return str + symbol;
		else
			return "";
	}

	public static String getPinyin(String str) {
		return "";
	}
}
