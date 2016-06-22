package com.procergs.rsp.post;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import com.procergs.rsp.list.ed.ListED;
import com.procergs.rsp.post.ed.LikeED;
import com.procergs.rsp.post.ed.PostED;
import com.procergs.rsp.user.ed.FollowED;
import com.procergs.rsp.user.ed.UserEd;

public class PostBD {
	EntityManager em;

	public PostBD(EntityManager em) {
		this.em = em;
	}

	public void insert(PostED postED) {
		em.persist(postED);
	}

	public Collection<PostED> list(UserEd user, Long idLastPost, Long idFirstPost) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from post ");
		sql.append(" where post.ID_POST_PARENT is null and (post.ID_USUARIO = ? ");
		sql.append(" or exists ( ");
		sql.append("		select 1 from FOLLOW ");
		sql.append(" where FOLLOW.ID_FOLLOWER = ? ");
		sql.append(" and (post.ID_USUARIO  = FOLLOW.ID_FOLLOWED ");
		sql.append(" 		OR EXISTS (select 1 FROM LIST_POST ");
		sql.append(" 					WHERE LIST_POST.ID_POST = post.ID_POST ");
		sql.append(" 					and LIST_POST.ID_LIST = FOLLOW.ID_LIST_FOLLOWED ) ");
		sql.append(" 	 ) ");
		sql.append(" )) ");
		if(idLastPost != null){
			sql.append(" AND post.ID_POST < ? ");
		}
		if(idFirstPost != null){
			sql.append(" AND post.ID_POST > ? ");
		}
		sql.append(" order by post.ID_POST desc ");

		Query query =  em.createNativeQuery(sql.toString(), PostED.class);
		int index = 1;
		query.setParameter(index++, user.getIdUsuario());
		query.setParameter(index++, user.getIdUsuario());
		if(idLastPost != null){
			query.setParameter(index++, idLastPost);
		}
		if(idFirstPost != null){
			query.setParameter(index++, idFirstPost);
		}

		query.setMaxResults(10);

		List<PostED> l = query.getResultList();
		return l;
	}

	public Collection<PostED> listPostList(Long idList, Long idLastPost, Long idFirstPost) {

		StringBuilder sql = new StringBuilder();
		sql.append(" select * from post ");
		sql.append(" where post.ID_POST_PARENT is null and  ");
		sql.append(" EXISTS (select 1 FROM LIST_POST ");
		sql.append(" WHERE LIST_POST.ID_POST = post.ID_POST ");
		sql.append(" and LIST_POST.ID_LIST = ? ) ");
		if(idLastPost != null){
			sql.append(" AND post.ID_POST < ? ");
		}
		if(idFirstPost != null){
			sql.append(" AND post.ID_POST > ? ");
		}
		sql.append(" order by post.ID_POST desc ");

		Query query =  em.createNativeQuery(sql.toString(), PostED.class);
		int index = 1;
		query.setParameter(index++, idList);
		if(idLastPost != null){
			query.setParameter(index++, idLastPost);
		}
		if(idFirstPost != null){
			query.setParameter(index++, idFirstPost);
		}

		query.setMaxResults(10);

		List<PostED> l = query.getResultList();
		return l;
	}

	public Collection<PostED> listPostUser(Long idUser, Long idLastPost, Long idFirstPost) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from post ");
		sql.append(" where post.ID_POST_PARENT is null and post.ID_USUARIO = ? ");
		if(idLastPost != null){
			sql.append(" AND post.ID_POST < ? ");
		}
		if(idFirstPost != null){
			sql.append(" AND post.ID_POST > ? ");
		}
		sql.append(" order by post.ID_POST desc ");

		Query query =  em.createNativeQuery(sql.toString(), PostED.class);
		int index = 1;
		query.setParameter(index++, idUser);
		if(idLastPost != null){
			query.setParameter(index++, idLastPost);
		}
		if(idFirstPost != null){
			query.setParameter(index++, idFirstPost);
		}

		query.setMaxResults(10);

		List<PostED> l = query.getResultList();
		return l;
	}

	public void insertLike(LikeED likeED) {
		em.persist(likeED);
	}

	public void deleteLike(LikeED likeED) {
		Query q = em.createQuery("DELETE FROM LikeED l WHERE l.userEd = :u AND l.postED = :p");
		q.setParameter("u", likeED.getUserEd());
		q.setParameter("p", likeED.getPostED());
		q.executeUpdate();
	}

	public PostED load(Long idPost) {
		return em.find(PostED.class, idPost);
	}

	public void delete(PostED postED) {
		Query qi = em.createQuery("DELETE FROM ImageED i WHERE i.postED = :p");
		qi.setParameter("p", postED);
		qi.executeUpdate();

		Query ql = em.createQuery("DELETE FROM LikeED l WHERE l.postED = :p");
		ql.setParameter("p", postED);
		ql.executeUpdate();

		em.remove(postED);
	}

	public Collection<PostED> listReplies(PostED postED) {
		Query q = em.createQuery("SELECT p FROM PostED p WHERE p.parent = :p ORDER BY p.data");
		q.setParameter("p", postED);
		return q.getResultList();
	}

	public Collection<LikeED> listLikes(PostED postED) {
		Query q = em.createQuery("SELECT l FROM LikeED l WHERE l.postED = :p");
		q.setParameter("p", postED);
		return q.getResultList();
	}
}
