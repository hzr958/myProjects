package com.smate.core.base.enums;

import com.smate.core.base.utils.string.StringUtils;
import com.sun.istack.internal.NotNull;
import java.util.Objects;
import java.util.function.Function;

/**
 * 基础枚举抽象类
 *
 * @param <T> value值的类型。value存储枚举值
 * @author houchuanjie
 * @date 2017年7月26日
 */
public interface IBaseEnum<T> {

  /**
   * 将给定任意类型的值转换为实现{@link IBaseEnum}接口的枚举类型
   *
   * @param value 要进行转换的任意类型的值
   * @param enumClazz 转换后的目标枚举类型Class
   * @param parser 值转换器，用于将{@code value}转换为目标枚举类型的{@link IBaseEnum#getValue()}返回值等价的类型， 如果此参数为{@code
   * null}，则使用{@code value}的实际类型
   * @param <E> 实现了{@link IBaseEnum}接口的枚举类型
   * @param <S> 任意类型
   * @param <T> 值转换器转换后的目标类型，与 {@code enumClazz#getValue()} 返回类型相同
   * @return 转换后的目标枚举类型，可能为 {@code null}
   * @throws IllegalArgumentException
   */
  static <E extends IBaseEnum, S, T> E parse(S value, @NotNull Class<E> enumClazz, Function<S, T> parser)
      throws IllegalArgumentException {
    if (!Enum.class.isAssignableFrom(enumClazz)) {
      throw new IllegalArgumentException("给定类型不是枚举类型！");
    }

    if (!IBaseEnum.class.isAssignableFrom(enumClazz)) {
      throw new IllegalArgumentException("给定类型" + enumClazz + "不是IBaseEnum接口的实现类");
    }

    if (value == null) {
      return null;
    }

    // 如果有指定的parser（用于将value转换为与枚举类型enumClazz的方法getValue()返回值相同类型的值），则转换
    Object targetValue = parser == null ? value : parser.apply(value);
    if (targetValue == null) {
      return null;
    }
    // 遍历所有枚举值，取出枚举中getValue()值与targetValue相同的枚举返回
    E[] enumConstants = enumClazz.getEnumConstants();
    for (E enumConstant : enumConstants) {
      Object enumValue = enumConstant.getValue();
      // 如果都是String类型，忽略大小写比较
      if (targetValue.getClass() == String.class && enumValue.getClass() == String.class) {
        if (StringUtils.equalsIgnoreCase((String) targetValue, (String) enumValue)) {
          return enumConstant;
        }
      } else if (Objects.equals(targetValue, enumValue)) {
        return enumConstant;
      }
    }
    return null;
  }

  /**
   * 获取枚举的值
   *
   * @return
   * @author ChuanjieHou
   * @date 2017年9月26日
   */
  T getValue();

  /**
   * 获取枚举的描述
   *
   * @return
   * @author ChuanjieHou
   * @date 2017年9月26日
   */
  String getDescription();
}
