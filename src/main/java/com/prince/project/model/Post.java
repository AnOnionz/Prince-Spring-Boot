package com.prince.project.model;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import org.springframework.web.util.WebUtils;

import com.prince.project.repository.CategoryRepository;

@Entity  
@Table(name = "post")
public class Post {  
  @Id  
  @GeneratedValue(strategy = GenerationType.AUTO) 
  @Column(name = "post_id")  
  private Integer id;  
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  private Category category;
  
  @Transient
  private Integer selectedCat;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id")
  private AppUser author;
  
  @Column(name = "created_time")  
  private Timestamp createdTime;
  
  @Column(name = "title")  
  private String title;
  
  @Column(name = "score")  
  private Integer score;
  
  @Column(name = "subtitle1")  
  private String subTitle1;
  
  @Column(name = "subtitle2")  
  private String subTitle2;
  
  @Column(name = "content1")  
  private String content1;
  
  @Column(name = "content2")  
  private String content2;
  
  @Column(name = "image")  
  private String image;
  
  @Column(name = "image1")  
  private String image1;
  
  @Column(name = "image2")  
  private String image2;
  
  @Column(name = "figure1")  
  private String figure1;
  
  @Column(name = "figure2")  
  private String figure2;
  
  @Column(name = "startdate")  
  private Date startDate;
  
  @Column(name = "enddate")  
  private Date endDate;
  
  @Column(name = "cost")  
  private Integer cost;
  
  @Column(name = "cost_per_click")  
  private double costperclick;
  
  @Column(name = "click")  
  private Integer click;
  
  @Column(name = "visiter")  
  private Integer visiter;
  
  @Column(name = "type")  
  private Integer type;
  
  @Transient
  private Integer extend;
  
  @Column(name = "status")  
  private Integer status;

public Integer getId() {
	return id;
}

public void setId(Integer id) {
	this.id = id;
}

public Category getCategory() {
	return category;
}

public void setCategory(Category category) {
	this.category = category;
}

public void setCostperclick(double costperclick) {
	this.costperclick = costperclick;
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

public String getTitle() {
	return title;
}
public String getTitleLimited(int index) {
	StringBuffer bf = new StringBuffer();
	String[] titles =  title.split(" ");
	for(int i = 0 ; i< titles.length; i++) {
		if(i==index) {
			bf.append("...");
			break;
		}
		bf.append(titles[i]+" ");
		
	}
	return new String(bf);
}

public void setTitle(String title) {
	this.title = title;
}
public Integer getSelectedCat() {
	return selectedCat;
}

public void setSelectedCat(Integer selectedCat) {
	this.selectedCat = selectedCat;
}

public Integer getScore() {
	return score;
}

public void setScore(Integer score) {
	this.score = score;
}

public String getSubTitle1() {
	return subTitle1;
}

public void setSubTitle1(String subTitle1) {
	this.subTitle1 = subTitle1;
}

public String getSubTitle2() {
	return subTitle2;
}

public void setSubTitle2(String subTitle2) {
	this.subTitle2 = subTitle2;
}

public String getContent1() {
	return content1;
}

public void setContent1(String content1) {
	this.content1 = content1;
}

public String getContent2() {
	return content2;
}

public void setContent2(String content2) {
	this.content2 = content2;
}

public String getImage() {
	return image;
}

public void setImage(String image) {
	this.image = image;
}

public String getImage1() {
	return image1;
}

public void setImage1(String image1) {
	this.image1 = image1;
}

public String getImage2() {
	return image2;
}

public void setImage2(String image2) {
	this.image2 = image2;
}

public String getFigure1() {
	return figure1;
}

public void setFigure1(String figure1) {
	this.figure1 = figure1;
}

public String getFigure2() {
	return figure2;
}

public void setFigure2(String figure2) {
	this.figure2 = figure2;
}

public Date getStartDate() {
	return startDate;
}

public String startDateString() {
	return startDate.toString();
}

public void setStartDate(Date startDate) {
	this.startDate = startDate;
}

public Date getEndDate() {
	return endDate;
}

public void setEndDate(Date endDate) {
	this.endDate = endDate;
}

public Integer getCost() {
	return cost;
}

public void setCost(Integer cost) {
	this.cost = cost;
}

public double getCostperclick() {
	return costperclick;
}

public Integer getClick() {
	return click;
}

public void setClick(Integer click) {
	this.click = click;
}

public Integer getVisiter() {
	return visiter;
}

public void setVisiter(Integer visiter) {
	this.visiter = visiter;
}

public Integer getType() {
	return type;
}

public void setType(Integer type) {
	this.type = type;
}

public Integer getExtend() {
	return extend;
}

public void setExtend(Integer extend) {
	this.extend = extend;
}

public Integer getStatus() {
	return status;
}

public void setStatus(Integer status) {
	this.status = status;
}
public int getClickGuess() {
	return (int) (cost / costperclick);

}

public int costPerClick() {
	// nguoi dung chi nhan 35%
	final double rate = 0.35;
	return (int) (((rate) * (this.costperclick))*23000/100);
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
@Override
public String toString() {
	return "Post [id=" + id + ", category=" + category + ", author=" + author + ", createdTime=" + createdTime
			+ ", title=" + title + ", score=" + score + ", subTitle1=" + subTitle1 + ", subTitle2=" + subTitle2
			+ ", content1=" + content1 + ", content2=" + content2 + ", image=" + image + ", image1=" + image1
			+ ", image2=" + image2 + ", figure1=" + figure1 + ", figure2=" + figure2
			+ ", startDate=" + startDate + ", endDate=" + endDate + ", cost=" + cost + ", costperclick=" + costperclick
			+ ", click=" + click + ", visiter=" + visiter + ", type=" + type +  ", extend=" + extend + ", status=" + status + "]";
}


  
  
  
  
  
  
  

}
