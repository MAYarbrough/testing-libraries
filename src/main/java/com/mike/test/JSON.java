package com.mike.test;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JSON {
	
	private static ObjectMapper mapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
//		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY);
		return mapper;
	}
	
	public static String write(Object o){
		return write(o, Paths.get(System.getenv("TEMP"), UUID.randomUUID() + ".json").toString());
	}
	
	public static String write(Object o, String destinationLocation, boolean... doOpen) {
		try(FileOutputStream fout = new FileOutputStream(new File(destinationLocation))) {
			mapper().writeValue(fout, o);
			if(null != doOpen && doOpen.length > 0 && doOpen[0]) {
				Runtime.getRuntime().exec( "cmd /c START " + destinationLocation );
			}
			return destinationLocation;
		} catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public static String writeAndOpen(Object o) {
		try {
			String dest = write(o);
			Runtime.getRuntime().exec( "cmd /c START " + dest );
			return dest;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static <T> T read(String sourceLocation, Class<T> expectedClass) {
		try(FileInputStream fin = new FileInputStream(new File(sourceLocation))){
			return mapper().readValue(fin, expectedClass);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static class ObjectWrapper {
		public Object o;
	}
	
	public static <T> T test(T left) {
		@SuppressWarnings("unchecked")
		T right = (T) read(write(left), left.getClass());
		if(!EqualsBuilder.reflectionEquals(left, right)){
			throw new RuntimeException("JSON read/write test failed for class " + left.getClass());
		}
		return right;
	}
	
}
