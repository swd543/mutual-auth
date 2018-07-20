package com.buga.boxes.server.to;

/**
 * @author Swapneel
 * A wrapper class to always encapsulate our response object in a JSON
 * @param <T>
 */
public class ResponseObject<T> {
	private long timestamp;
	private T value;
	
	public ResponseObject() {
		timestamp=System.currentTimeMillis();
	}
	public ResponseObject(T value) {
		timestamp=System.currentTimeMillis();
		this.value=value;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public T getValue() {
		return value;
	}
	public void setValue(T value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "ResponseObject [timestamp=" + timestamp + ", value=" + value + "]";
	}
	
	public static ResponseObject<String> ok(){
		return new ResponseObject<String>("OK");
	}
}
