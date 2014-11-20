/**
 * @ClassName: ExpAnnotation
 * @Description: TODO
 * @author panyouliang
 * @date 2013-3-28 下午2:16:38
 */
package com.umpay.hfmng.common;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @author MARCO.PAN
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExpTypeAnnotation {
	public String[] fieldStr();
	public int[] fieldWidth();
	public String fileName();
	public String sheetName() default "";
}
