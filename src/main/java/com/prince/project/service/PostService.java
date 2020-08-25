package com.prince.project.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prince.project.model.Post;
import com.prince.project.repository.PostRepository;

@Service
public class PostService {
	@Autowired
	private PostRepository repository;
	public List<Post> listPayment = new ArrayList<Post>();
	
	public void updateListPayment(HttpServletRequest request, Post post) {
		listPayment.add(post);
	}
	public void deletePresentPost(HttpServletRequest request) {
		request.getSession().removeAttribute("post");
	}
	public void setPresentPost(HttpServletRequest request, Post post) {
		request.getSession().setAttribute("post", post);
	}
	public Post getPresentPost(HttpServletRequest request) {
		return (Post)request.getSession().getAttribute("post");
	}
	public void deleteOrder(int id) {
		listPayment.remove(id);
	}
	public void initListPayment() {
		listPayment.clear();
	}
	public Post getOrder(HttpServletRequest request, int id) {
		Post post = listPayment.get(id);
		request.getSession().setAttribute("post", post);
		return post;
	}
	public Post getPost(int id) throws EntityNotFoundException{
		return repository.getOne(id);
		
	}
	public void PostWatched(Post post) {
		post.setClick(post.getClick() + 1);
		repository.save(post);
	}
	public void PostVisited(Post post) {
		post.setVisiter(post.getVisiter() + 1);
		repository.save(post);
	}
	
	

}
