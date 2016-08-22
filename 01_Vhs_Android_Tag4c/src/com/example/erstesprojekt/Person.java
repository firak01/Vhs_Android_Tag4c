package com.example.erstesprojekt;

import java.io.Serializable;

public class Person implements Serializable {
 /**
	 * //MErke: Serializierbar, damit es im bundle persistiert werden kann.	 
	 */
	private static final long serialVersionUID = 1L;
public Person(String vorname, String nachname, int alter) {
	 
		this.vorname = vorname;
		this.nachname = nachname;
		this.alter = alter;
//		super(); !!! Anders als bei normalen Methoden müsste bei Konstruktoren der Aufruf der Superklasse als erster Aufruf hier sein...
	}
private String vorname;
 public String getVorname() {
	return vorname;
}
public void setVorname(String vorname) {
	this.vorname = vorname;
}
public String getNachname() {
	return nachname;
}
public void setNachname(String nachname) {
	this.nachname = nachname;
}
public int getAlter() {
	return alter;
}
public void setAlter(int alter) {
	this.alter = alter;
}
private String nachname;
 private int alter;
}
