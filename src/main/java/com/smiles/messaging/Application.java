package com.smiles.messaging;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.FileSystemXmlApplicationContext;

@ComponentScan
@EnableAutoConfiguration
public class Application {
	
	private static ApplicationContext context = new FileSystemXmlApplicationContext("beans.xml");

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);       
    }
    
    public static ApplicationContext getContext(){
    	return context;
    }
}
