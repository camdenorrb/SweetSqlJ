package me.camdenorrb.sweetsqlj.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


// Defines a variable that shouldn't be stored in SQL, will result in this being null on load
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NonSql { }
