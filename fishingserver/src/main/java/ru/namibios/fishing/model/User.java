package ru.namibios.fishing.model;

import java.security.Timestamp;
import java.sql.Date;

public class User {

	private int id;
	private byte endabled;
	private String licence_key;
	private Timestamp date_create;
	private Date date_valid;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte getEndabled() {
		return endabled;
	}

	public void setEndabled(byte endabled) {
		this.endabled = endabled;
	}

	public String getLicence_key() {
		return licence_key;
	}

	public void setLicence_key(String licence_key) {
		this.licence_key = licence_key;
	}

	public Timestamp getDate_create() {
		return date_create;
	}

	public void setDate_create(Timestamp date_create) {
		this.date_create = date_create;
	}

	public Date getDate_valid() {
		return date_valid;
	}

	public void setDate_valid(Date date_valid) {
		this.date_valid = date_valid;
	}
}