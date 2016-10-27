package com.mike.test;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

public abstract class FileManager {
	private boolean doOpen = true;
	private String lastDestination;
	private String nextDestination;
	protected String fileExtension = ".txt";
	
	public abstract String write(Object o);

	public abstract <V> V read(String sourceLocation, Class<V> expectedClass);
	
	protected String getNextDestination() {
		if(null == nextDestination){
			return Paths.get(System.getenv("TEMP"), UUID.randomUUID() + fileExtension).toString();
		} else {
			return nextDestination;
		}
	}
	
	public String getLastDestination() {
		return lastDestination;
	}
	
	public void setNextDestination(String destinationLocation) {
		nextDestination = destinationLocation;
	}
	
	protected void saveLastDestination(String destinationLocation) {
		lastDestination = destinationLocation;
		if(doOpen){
			try {
				Runtime.getRuntime().exec( "cmd /c START " + destinationLocation );
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	public void setToOpen(boolean doOpen){
		this.doOpen = doOpen;
	}
	
	public void setDestination(String destinationLocation){
		nextDestination = destinationLocation;
	}
}
