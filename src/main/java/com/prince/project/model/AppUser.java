package com.prince.project.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.DefaultValue;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.UniqueElements;

import com.prince.project.utils.WebUtils;

@Entity
@Table(name = "user", uniqueConstraints = { //
        @UniqueConstraint(name = "USER_UK", columnNames = { "username"})})
public class AppUser {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	private Integer id;

	@Column(name = "username", unique = true)
	@NotEmpty(message = "*user name is null")
	private String name;

	@Column(name = "email", unique = true)
	@Email(message = "*email is invalid")
	@NotEmpty(message = "*please enter your email")
	private String email;

	@Column(name = "password")
	@Length(min = 6, message = "*Your password must be between 6 and 30 characters")
	@NotEmpty(message = "*please enter your password")
	private String password;

	@Column(name = "phone")
	private String phone;

	@Column(name = "avatar")
	private String avatar;
	
	@Column(name = "created_time")
	private Timestamp createdTime;
	
	@Column(name = "email_verification_hash")
	private String emailVerifyHash;

	@Column(name = "email_verification_attempts")
	private Integer emailVerifyAttempts;

	@Column(name = "firstname")
	private String firstName;

	@Column(name = "lastname")
	private String lastName;

	@Column(name = "gender")
	private String gender;

	@Column(name = "age")
	private Integer age;

	@Column(name = "date_of_birth")
	private Date dayofbirth;
	
	@Transient
	private String dayofbirthstr;

	@Column(name = "score")
	private Integer score;
	
	@Column(name = "code")
	private String code;

	@Column(name = "payment")
	private String payment;
	
	@Column(name = "status")
	private Integer status;

	public AppUser() {
		
	}
	

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public String getEmailVerifyHash() {
		return emailVerifyHash;
	}

	public void setEmailVerifyHash(String emailVerifyHash) {
		this.emailVerifyHash = emailVerifyHash;
	}

	public Integer getEmailVerifyAttempts() {
		return emailVerifyAttempts;
	}

	public void setEmailVerifyAttempts(Integer emailVerifyAttempts) {
		this.emailVerifyAttempts = emailVerifyAttempts;
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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Date getDayofbirth() {
		return dayofbirth;
	}

	public void setDayofbirth(Date dayofbirth) {
		this.dayofbirth = dayofbirth;
	}
	

	public String getDayofbirthstr() {
		
		return dayofbirthstr;
	}


	public void setDayofbirthstr(String dayofbirthstr) {
		this.dayofbirthstr = dayofbirthstr;
	}



	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}


	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "AppUser [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", phone="
				+ phone + ", avatar=" + avatar + ", CreatedTime=" + createdTime + ", emailVerifyHash=" + emailVerifyHash
				+ ", emailVerifyAttempts=" + emailVerifyAttempts + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", gender=" + gender + ", age=" + age + ", dayofbirth=" + dayofbirth + ", score=" + score + ", code="
				+ code + ", payment=" + payment + ", status=" + status + "]";
	}
	
	

}