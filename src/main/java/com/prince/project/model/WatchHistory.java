package com.prince.project.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity  
@Table(name = "watch_history", uniqueConstraints={
	    @UniqueConstraint(columnNames = {"user_id", "post_id"})
	})
public class WatchHistory {  
  @Id  
  @GeneratedValue(strategy = GenerationType.AUTO) 
  @Column(name = "watch_id")  
  private Integer id;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", unique = true)
  private AppUser user;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id", unique = true)
  private Post post;
  
  @Column(name = "isclick")  
  private Boolean isClick;
  
  @Column(name = "created_time")  
  private Timestamp createdTime;

public Integer getId() {
	return id;
}

public void setId(Integer id) {
	this.id = id;
}

public AppUser getUser() {
	return user;
}

public void setUser(AppUser user) {
	this.user = user;
}

public Post getPost() {
	return post;
}

public void setPost(Post post) {
	this.post = post;
}

public Boolean getIsClick() {
	return isClick;
}

public void setIsClick(Boolean isClick) {
	this.isClick = isClick;
}

public Timestamp getCreatedTime() {
	return createdTime;
}

public void setCreatedTime(Timestamp createdTime) {
	this.createdTime = createdTime;
}

@Override
public String toString() {
	return "WatchHistory [id=" + id + ", user=" + user + ", post=" + post + ", isClick=" + isClick + ", createdTime="
			+ createdTime + "]";
}
  
  
  
  
}
