package me.jrl1004.java.blockcollapse.managers;

public @interface BCPermissions {

	boolean reqireOP() default false;

	String[] permissions();

}
