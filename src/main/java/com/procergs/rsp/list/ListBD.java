package com.procergs.rsp.list;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.procergs.rsp.list.ed.ListED;
import com.procergs.rsp.user.ed.FollowED;
import com.procergs.rsp.user.ed.UserEd;

public class ListBD {
	EntityManager em;

	public ListBD(EntityManager em) {
		this.em = em;
	}
	
	public void insert(ListED listED){
		em.persist(listED);
	}

	public void insertFollow(FollowED followED) {
		em.persist(followED);
	}

	public List<ListED> list(UserEd userED) {
		Query q = em.createQuery("SELECT l FROM ListED l WHERE EXISTS (SELECT f FROM FollowED f WHERE f.follower = :u AND f.listFollowed = l) ");
		q.setParameter("u", userED);
		return q.getResultList();
	}

	public ListED find(Long idlist) {
		return em.find(ListED.class, idlist);
	}

	public Collection<ListED> listList(String listname) {
		Query query = em.createQuery("SELECT l FROM ListED l WHERE l.name like :u order by l.name");
		query.setParameter("u", "%"+listname+"%");
		return (Collection<ListED>) query.getResultList();
	}

	public ListED findByName(String name) {
		Query query = em.createQuery("SELECT l FROM ListED l WHERE l.name = :u ");
		query.setParameter("u", name);
		List<ListED> lr = (List<ListED>) query.getResultList();
		if(!lr.isEmpty()){
			return lr.get(0);
		} else {
			return null;
		}
	}

	public void deleteFollow(FollowED f) {
		Query q = em.createQuery("DELETE FROM FollowED f WHERE f.follower = :follower AND f.listFollowed = :listFollowed ");
		q.setParameter("follower", f.getFollower());
		q.setParameter("listFollowed", f.getListFollowed());
		q.executeUpdate();
	}
}
