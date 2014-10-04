package com.tutorialspoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.smiles.messaging.Application;

public class MainApp {

	// @Autowired
	// private static HelloWorld helloWorld;

	public static void main(String[] args) {

		ApplicationContext context = Application.getContext();

		HelloWorld helloWorld = (HelloWorld) context.getBean("helloWorld");
		helloWorld.getMessage();

		TextEditor te = (TextEditor) context.getBean("textEditor");

		te.spellCheck();
	}

//	/**
//	 * @return the helloWorld
//	 */
//	public static HelloWorld getHelloWorld() {
//		return helloWorld;
//	}
//
//	/**
//	 * @param helloWorld
//	 *            the helloWorld to set
//	 */
//	public static void setHelloWorld(HelloWorld helloWorld) {
//		MainApp.helloWorld = helloWorld;
//	}
}