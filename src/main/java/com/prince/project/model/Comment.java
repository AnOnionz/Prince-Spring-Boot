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
import javax.persistence.Transient;

@Entity  
@Table(name = "comment")
public class Comment {  
  @Id  
  @GeneratedValue(strategy = GenerationType.IDENTITY) 
  @Column(name = "comment_id")  
  private Integer id;
  
  @Transient
  private Integer user_id;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private AppUser user;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id")
  private Post post;
  
  @Transient
  private Integer post_id;
  
  @Column(name = "root_id")  
  private String rootID;
  
  @Column(name = "reply_id")  
  private String replyID;
  

  
  @Column(name = "content")  
  private String content;
  
  @Column(name = "created_time")  
  private Timestamp createdTime;

public Integer getId() {
	return id;
}

public void setId(Integer id) {
	this.id = id;
}

public Integer getUser_id() {
	return user_id;
}

public void setUser_id(Integer user_id) {
	this.user_id = user_id;
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

public Integer getPost_id() {
	return post_id;
}

public void setPost_id(Integer post_id) {
	this.post_id = post_id;
}

public String getRootID() {
	return rootID;
}

public void setRootID(String rootID) {
	this.rootID = rootID;
}

public String getReplyID() {
	return replyID;
}

public void setReplyID(String replyID) {
	this.replyID = replyID;
}

public String getContent() {
	return content;
}

public void setContent(String content) {
	this.content = content;
}

public Timestamp getCreatedTime() {
	return createdTime;
}

public void setCreatedTime(Timestamp createdTime) {
	this.createdTime = createdTime;
}

@Override
public String toString() {
	return "Comment [id=" + id + ", user_id=" + user_id + ", user=" + user + ", post=" + post + ", post_id=" + post_id
			+ ", rootID=" + rootID + ", replyID=" + replyID + ", content=" + content + ", createdTime=" + createdTime
			+ "]";
}
  

  
  
  
  
}
