package com.procergs.rsp.image;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.procergs.rsp.image.ed.ImageED;
import com.procergs.rsp.post.ed.PostED;

import java.util.Collection;

@Stateless
@Named
@Path("image")
public class ImageService {
	
	@PersistenceContext(unitName = "RSP_PU")
	EntityManager em;

	ImageBD imageBD;
	
	@PostConstruct
	public void init() {
		imageBD = new ImageBD(em);
	}
	
	
	@GET
	@Path("/{idUser}/{idImage}.jpg")
	@Produces({"image/jpeg"})
	public Response getImage(@PathParam("idUser")Long idUsuario,@PathParam("idImage") Long idImage){
		ImageED imageEd = imageBD.findImage(idImage);
		
		CacheControl cc = new CacheControl();
		
		cc.setNoCache(false);
		cc.setNoStore(false);
		cc.setPrivate(false);
		cc.setMaxAge(Integer.MAX_VALUE);
		return Response.ok(imageEd.getImage(), "image/jpeg").cacheControl(cc).build();
	}
	
	
	@GET
	@Path("/{idImage}.{type}")
	public Response getImage(@PathParam("idImage") Long idImage, @PathParam("type") String type){
		ImageED imageEd = imageBD.findImage(idImage);
		
		CacheControl cc = new CacheControl();
		
		cc.setNoCache(false);
		cc.setNoStore(false);
		cc.setPrivate(false);
		cc.setMaxAge(Integer.MAX_VALUE);
		return Response.ok(imageEd.getImage(), "image/"+imageEd.getType()).cacheControl(cc).build();
	}
	
	
	
	public void insert(ImageED imageED) {
		imageBD.save(imageED);
	}

	public Collection<ImageED> listImages(PostED postED) {
		return imageBD.listImages(postED);
	}
}
