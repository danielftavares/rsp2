package com.procergs.rsp.user;

import java.util.Calendar;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.procergs.rsp.user.ed.UserEd;
import com.procergs.rsp.user.ed.UserLoginED;

public class UserLoginBD {
	EntityManager em;

	public UserLoginBD(EntityManager em) {
		this.em = em;
	}
	
	public void insere(UserLoginED userLogin){
		em.persist(userLogin);
	}

	public UserLoginED buscaLogin(Long idUser, String token) {
		Query q = em.createQuery("SELECT l FROM UserLoginED l WHERE l.userEd.id = :idUser AND l.token = :token AND l.dataLogout = NULL ");
		q.setParameter("idUser", idUser);
		q.setParameter("token", token);
		return (UserLoginED) q.getSingleResult();
		
	}

	public void geraLogout(UserEd userEd) {
		Query q = em.createQuery("UPDATE UserLoginED l SET l.dataLogout = :data WHERE l.userEd = :userEd AND l.dataLogout = NULL ");
		q.setParameter("data", Calendar.getInstance());
		q.setParameter("userEd", userEd);
		q.executeUpdate();
	}
}
