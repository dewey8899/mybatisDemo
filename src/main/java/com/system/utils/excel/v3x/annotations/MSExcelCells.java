package com.system.utils.excel.v3x.annotations;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MSExcelCells {
    MSExcelCell[] value();
}
