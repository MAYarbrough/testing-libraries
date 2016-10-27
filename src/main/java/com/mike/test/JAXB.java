package com.mike.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Paths;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.commons.lang3.builder.EqualsBuilder;

public class JAXB {
	
	public static String write(Object jaxbObject, Boolean... doOpen){
		if(doOpen != null && doOpen.length > 0 && doOpen[0]) {
			return writeAndOpen(jaxbObject);
		} else {
			return write(jaxbObject, Paths.get(System.getenv("TEMP"), UUID.randomUUID() + ".xml").toString(), "UTF-8");
		}
	}
	
	public static String writeAndOpen(Object jaxbObject){
		try {
			String dest = write(jaxbObject);
			Runtime.getRuntime().exec( "cmd /c START " + dest );
			return dest;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String write(Object jaxbObject, String destinationLocation, String encoding) {
		try(FileOutputStream fos = new FileOutputStream(destinationLocation)){
			JAXBContext c = JAXBContext.newInstance(jaxbObject.getClass());
			Marshaller marshaller = c.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);
			marshaller.marshal(jaxbObject, fos);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		javax.xml.bind.JAXB.marshal(jaxbObject, destinationLocation);
		return destinationLocation;
	}

	public static <V> V read(String sourceLocation, Class<V> expectedClass) {
		return javax.xml.bind.JAXB.unmarshal(sourceLocation, expectedClass);
	}
	
	public static <T> T test(T left) {
		@SuppressWarnings("unchecked")
		T right = (T) read(write(left), left.getClass());
		if(!EqualsBuilder.reflectionEquals(left, right)){
			throw new RuntimeException("JSON read/write test failed for class " + left.getClass());
		}
		return right;
	}

	public static <V> V read(String sourceLocation, Class<V> expectedClass, String encoding) {
		try{
			InputStream inputStream = new FileInputStream(sourceLocation);
			Reader reader = new InputStreamReader(inputStream, "UTF-16");
			return javax.xml.bind.JAXB.unmarshal(reader, expectedClass);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
