package com.smate.core.base.utils.common;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

/**
 * spring el表达式组件.
 * 
 * @author chenxiangrong
 * 
 */
@Component
public class SpringElCompnent {
  public static boolean getExpResult(String expression, Map<String, Object> param) {
    boolean result = false;
    if (param != null) {
      Pattern pattern = Pattern.compile("@[\\w]+");
      Matcher matcher = pattern.matcher(expression);
      while (matcher.find()) {
        String rpparam = matcher.group();
        String paramName = rpparam.substring(1);
        expression = expression.replace(rpparam, param.get(paramName).toString());
      }
    }

    ExpressionParser parser = new SpelExpressionParser();
    result = parser.parseExpression(expression).getValue(Boolean.class);
    return result;
  }
}
