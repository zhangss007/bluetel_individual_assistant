package com.bluetel.android.app.individual_assistant;

import android.graphics.Bitmap;

/**
 * ��ϵ�����װ
 */
public class Contact {

	private String name ;
	private String number ;
	private transient Bitmap photo;
	public Contact(String name, String number, Bitmap photo) {
		super();
		this.name = name;
		this.number = number;
		this.photo = photo;
	}
	
	public Contact(){
		
		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public Bitmap getPhoto() {
		return photo;
	}
	public void setPhoto(Bitmap photo) {
		this.photo = photo;
	}
	
	
	
}
