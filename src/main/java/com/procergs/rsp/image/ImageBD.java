package com.procergs.rsp.image;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.procergs.rsp.image.ed.ImageED;
import com.procergs.rsp.list.ed.ListED;
import com.procergs.rsp.post.ed.PostED;
import com.procergs.rsp.user.ed.FollowED;
import com.procergs.rsp.user.ed.UserEd;

public class ImageBD {
	EntityManager em;

	public ImageBD(EntityManager em) {
		this.em = em;
	}

	public ImageED findImage(Long idImage) {
		Query q = em.createQuery("SELECT i FROM ImageED i WHERE i.id = :idimage");
		q.setParameter("idimage", idImage);
		
		return (ImageED) q.getSingleResult();
	}

	public void save(ImageED imageED) {
		em.persist(imageED);
	}


	public Collection<ImageED> listImages(PostED postED) {
		Query q = em.createQuery("SELECT i FROM ImageED i WHERE i.postED = :p ");
		q.setParameter("p", postED);
		return q.getResultList();

	}
}
