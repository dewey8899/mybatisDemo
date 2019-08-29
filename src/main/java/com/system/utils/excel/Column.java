package com.system.utils.excel;

import java.lang.annotation.*;

@Target({ElementType.FIELD})  
@Retention(RetentionPolicy.RUNTIME)  
@Documented 
public @interface Column {
	
	public int index();
	
	public int type();
	
	public String title();
	
	public String format() default "";
	
	public int width() default 256;
}
