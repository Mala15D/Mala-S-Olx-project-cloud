package com.zensar.olx.bean;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.lang.NonNull;

@Entity
@Table(name = "olx_users_table")
public class OlxUser {

	@NonNull
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int olxUserId;

	@NonNull
	@Column(name ="first_name")
	private String firstName;

	@NonNull
	@Column(name ="last_name")
	private String lastName;

	@NonNull
	@Column(name ="user_name")
	private String userName;

	@NonNull
	@Enumerated(EnumType.STRING)
	private Active active;

	@NonNull
	private String password;

	@NonNull
	@Column(name ="email_id")
	private String emailId;

	@NonNull
	@Column(name ="phone_number")
	private String phoneNumber;

	@NonNull
	private String roles;

	public OlxUser(int olxUserId, String firstName, String lastName, String userName, Active active, String password,
			String emailId, String phoneNumber, String roles) {
		super();
		this.olxUserId = olxUserId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.active = active;
		this.password = password;
		this.emailId = emailId;
		this.phoneNumber = phoneNumber;
		this.roles = roles;
	}

	public OlxUser(String firstName, String lastName, String userName, Active active, String password, String emailId,
			String phoneNumber, String roles) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.active = active;
		this.password = password;
		this.emailId = emailId;
		this.phoneNumber = phoneNumber;
		this.roles = roles;
	}

	public OlxUser() {
		super();
	}

	public int getOlxUserId() {
		return olxUserId;
	}

	public void setOlxUserId(int olxUserId) {
		this.olxUserId = olxUserId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Active getActive() {
		return active;
	}

	public void setActive(Active active) {
		this.active = active;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "OLXUsers [olxUserId=" + olxUserId + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", userName=" + userName + ", active=" + active + ", password=" + password + ", emailId=" + emailId
				+ ", phoneNumber=" + phoneNumber + ", roles=" + roles + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(olxUserId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OlxUser other = (OlxUser) obj;
		return olxUserId == other.olxUserId;
	}

}
