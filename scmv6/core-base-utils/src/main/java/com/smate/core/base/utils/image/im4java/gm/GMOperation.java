package com.smate.core.base.utils.image.im4java.gm;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

/**
 * 这个类扩展了gm4java版本的GMOperation，并扩展了一些方便的方法来简化GraphicsMagick图像命令的构造
 * 
 * @author houchuanjie
 * @date 2018年1月9日 下午8:32:56
 */
public class GMOperation extends org.gm4java.im4java.GMOperation {
  /**
   * 几何操作符枚举
   */
  public enum GeometryEnum {
    /**
     * 调整图像的大小，同时使用几何规范作为最小值保持宽比。
     */
    FillUsingAspectRatio('^'),
    /**
     * 强制图像大小和几何规格完全匹配
     */
    ForceDimensions('!'),
    /**
     * 将宽度和高度声明为百分数，而不是像素长度。
     */
    DimensionsAsPercentages('%'),
    /**
     * 只有当宽度或高度超过几何规格时才调整图像大小。
     */
    DownsizeOnly('>'),
    /**
     * 只有在宽度和高度小于几何规格的情况下调整图像大小。
     */
    UpsizeOnly('<');
    private char value;

    private GeometryEnum(char val) {
      this.value = val;
    }

    public char getValue() {
      return value;
    }

    public static void validate(final Collection<GeometryEnum> geometryEnums) throws IllegalArgumentException {
      mutuallyExclusiveCheck(geometryEnums,
          EnumSet.of(GeometryEnum.FillUsingAspectRatio, GeometryEnum.ForceDimensions));
      mutuallyExclusiveCheck(geometryEnums, EnumSet.of(GeometryEnum.DownsizeOnly, GeometryEnum.UpsizeOnly));
    }

    /**
     * 互斥检查
     *
     * @param used
     * @param mutuallyExclusive
     */
    private static void mutuallyExclusiveCheck(final Collection<GeometryEnum> used,
        final Collection<GeometryEnum> mutuallyExclusive) throws IllegalArgumentException {
      EnumSet<GeometryEnum> matched = EnumSet.copyOf(used);
      matched.retainAll(mutuallyExclusive);
      if (matched.size() > 1) {
        throw new IllegalArgumentException("GeometryEnums " + join(matched, "与") + "是互斥的！");
      }
    }

    private static String join(Collection<?> set, String andOr) {
      StringBuffer buf = new StringBuffer();
      Iterator<?> i = set.iterator();
      for (int n = set.size() - 2; n > 0; n--) {
        buf.append(i.next()).append(", ");
      }
      buf.append(i.next()).append(' ').append(andOr).append(' ').append(i.next());
      return buf.toString();
    }
  }

  /**
   * 图像分辨率单位枚举
   */
  public enum DensityUnitEnum {
    /**
     * 未定义
     */
    Undefined,
    /**
     * 像素每英寸
     */
    PixelsPerInch,
    /**
     * 像素每厘米
     */
    PixelsPerCentimeter;
  }

  /**
   * 调整图像为指定大小，生成缩略图的快速方法，不关心图像质量。同样可以实现缩略图的方法{@link #resize(Integer, Integer, Character)}
   * 当force=false时，调整图像的大小，同时使用目标宽度作为最大值，并保持宽高比。 当force=true时，调整图像大小，使用目标宽高，当宽度或高度小于目标值时，会拉伸图像。
   * 
   * @param width 缩略图宽度
   * @param height 缩略图高度
   * @param force 是否强制图像大小和目标大小完全匹配，true表示强制，false表示不强制。
   * @return
   */
  public GMOperation thumbnail(final int width, final int height, final boolean force) throws IllegalArgumentException {
    final List<String> cmdArgs = getCmdArgs();
    cmdArgs.add("-thumbnail");
    cmdArgs.add(resample(width, height, force ? GeometryEnum.ForceDimensions : null));
    return this;
  }

  /**
   * 格式化图片输出信息
   *
   * @param fmt 格式化规则
   * @return
   */
  public GMOperation format(String fmt) {
    StringBuffer builder = new StringBuffer();
    final List<String> cmdArgs = getCmdArgs();
    cmdArgs.add("-format");
    if (fmt != null) {
      builder.append(fmt.toString());
    }
    if (builder.length() > 0) {
      cmdArgs.add(builder.toString());
    }
    return this;
  }

  /**
   * 根据宽高和几何枚举信息重新取样
   * 
   * @param width
   * @param height
   * @param geometryEnums
   * @return
   * @throws IllegalArgumentException
   */
  protected static String resample(final int width, final int height, GeometryEnum... geometryEnums)
      throws IllegalArgumentException {
    if ((width < 1) || (height < 1)) {
      throw new IllegalArgumentException("目标宽度和高度应大于0！");
    }
    EnumSet<GeometryEnum> geometryEnumSet = EnumSet.copyOf(Arrays.asList(geometryEnums));
    GeometryEnum.validate(geometryEnumSet);
    StringBuilder buf = new StringBuilder();
    buf.append(width).append('x').append(height);
    for (GeometryEnum geometryEnum : geometryEnumSet) {
      buf.append(geometryEnum.getValue());
    }
    return buf.toString();
  }

}
