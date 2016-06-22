package com.procergs.rsp.list;

import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.procergs.rsp.list.ed.ListED;
import com.procergs.rsp.user.ed.FollowED;
import com.procergs.rsp.user.ed.UserEd;
import com.procergs.rsp.user.ed.UserRequestED;

@Stateless
@Named
@Path("list")
public class ListService {
	
	@PersistenceContext(unitName = "RSP_PU")
	EntityManager em;

	ListBD listBD;
	
	@PostConstruct
	public void init() {
		listBD = new ListBD(em);
	}
	  
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
	public ListED post(@Context HttpServletRequest httpRequest, @FormParam("ln") String name){
    	ListED listED = new ListED();
    	listED.setName(name);
    	listBD.insert(listED);
    	
    	UserEd follower = ((UserRequestED) httpRequest.getAttribute(UserRequestED.ATRIBUTO_REQ_USER)).getUserEd();
    	FollowED followED = new FollowED(follower, listED);
    	listBD.insertFollow(followED);
    	
    	return listED;
	}
    
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
	public List<ListED> list(@Context HttpServletRequest httpRequest){
    	UserEd userED = ((UserRequestED) httpRequest.getAttribute(UserRequestED.ATRIBUTO_REQ_USER)).getUserEd();
    	return listBD.list(userED);
	}
    
    
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("l")
	public Collection<ListED> listUser(@QueryParam("ln") String listname){
		return listBD.listList(listname);
	}
    
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/{idlist}")
	public ListED find(@PathParam("idlist") Long idlist){
		return listBD.find(idlist);
	}
	
	

	@GET
	@Path("/f/{idList}")
	public void follow(@PathParam("idList") Long idlist, @Context HttpServletRequest httpRequest){
		ListED followed = new ListED(idlist) ;
		UserEd follower = ((UserRequestED) httpRequest.getAttribute(UserRequestED.ATRIBUTO_REQ_USER)).getUserEd();
		
		FollowED f = new FollowED(follower, followed);
		listBD.insertFollow(f);
	}

	@GET
	@Path("/uf/{idList}")
	public void unfollow(@PathParam("idList") Long idlist, @Context HttpServletRequest httpRequest) {
		ListED followed = new ListED(idlist) ;
		UserEd follower = ((UserRequestED) httpRequest.getAttribute(UserRequestED.ATRIBUTO_REQ_USER)).getUserEd();
		FollowED f = new FollowED(follower, followed);
		listBD.deleteFollow(f);
	}

	public ListED findByName(String name) {
		return  listBD.findByName(name);
	}

	public void insert(ListED listED) {
		listBD.insert(listED);
	}
}
