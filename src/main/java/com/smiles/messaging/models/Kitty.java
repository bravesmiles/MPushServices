/**
 * 
 */
package com.smiles.messaging.models;

/**
 * @author yaojliu
 *
 */
public class Kitty {
	private long id;
	private String content;
	public Kitty(long incrementAndGet, String content) {
		// TODO Auto-generated constructor stub
		this.setId(incrementAndGet);
		this.setContent(content);
	}
	
	public Kitty(String content) {
		// TODO Auto-generated constructor stub
		this.content = content;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

}
