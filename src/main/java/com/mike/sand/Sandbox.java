package com.mike.sand;

import javax.xml.bind.annotation.XmlRootElement;

import com.mike.test.JAXB;

public class Sandbox {
	public static void main(String[] args) throws InterruptedException {
		JAXB.write(new someClass(), true);
	}
	
	@XmlRootElement
	static class someClass {
		String someString = "someString";
	}
}
