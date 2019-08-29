package com.system.utils.excel.v3x.annotations;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(MSExcelCells.class)
public @interface MSExcelCell {

    int rowIndex();

    String comment() default "";

    String author() default "";

    String format() default "";

    boolean isFormula() default false;

    String formula() default "";
}
