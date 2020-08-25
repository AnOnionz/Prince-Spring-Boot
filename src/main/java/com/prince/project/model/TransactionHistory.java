package com.prince.project.model;

import java.sql.Timestamp;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity  
@Table(name = "history_transaction")
public class TransactionHistory {  
  @Id  
  @GeneratedValue(strategy = GenerationType.AUTO) 
  @Column(name = "transaction_id")  
  private Integer id;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private AppUser author;
  
  @Column(name = "created_time")  
  private Timestamp createdTime;
  
  @Column(name = "score")  
  private Integer score;
  
  @Column(name = "status")  
  private Integer status;

public Integer getId() {
	return id;
}

public void setId(Integer id) {
	this.id = id;
}

public AppUser getAuthor() {
	return author;
}

public void setAuthor(AppUser author) {
	this.author = author;
}

public Timestamp getCreatedTime() {
	return createdTime;
}

public void setCreatedTime(Timestamp createdTime) {
	this.createdTime = createdTime;
}

public Integer getScore() {
	return score;
}

public void setScore(Integer score) {
	this.score = score;
}

public Integer getStatus() {
	return status;
}

public void setStatus(Integer status) {
	this.status = status;
}
public String createTimeToString() {
	String dateTime = "";
	Timestamp createtime = this.createdTime;
	java.util.GregorianCalendar cal = (java.util.GregorianCalendar) Calendar.getInstance();
	cal.setTime(createtime);
	int day = cal.get(java.util.Calendar.DAY_OF_WEEK);
	int date = cal.get(java.util.Calendar.DAY_OF_MONTH);
	int month = cal.get(java.util.Calendar.MONTH) + 1;
	int year = cal.get(java.util.Calendar.YEAR);
	int hours = cal.get(java.util.Calendar.HOUR);
	int minute = cal.get(java.util.Calendar.MINUTE);
	int a = cal.get(java.util.Calendar.AM_PM);

	dateTime = "ngày " + date + " tháng " + month + ", " + year + " lúc " + hours + ":" + minute;
	if (cal.get(Calendar.AM_PM) == 0) {
		dateTime = dateTime + " AM";
	} else {
		dateTime = dateTime + " PM";
	}

	return dateTime;
}
  
  

}
