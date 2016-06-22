package com.procergs.rsp.profile;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.procergs.rsp.profile.ed.ProfileField;
import com.procergs.rsp.profile.ed.ProfileFieldValue;
import com.procergs.rsp.user.ed.UserEd;

public class ProfileBD {
	EntityManager em;

	public ProfileBD(EntityManager em) {
		this.em = em;
	}

	public List<ProfileField> listaFields() {
		return em.createQuery("SELECT f FROM ProfileField f").getResultList();
	}

	public void removeProfileFieldValue(UserEd userED, ProfileField profileField) {
		Query q = em.createQuery("DELETE FROM ProfileFieldValue fv WHERE fv.profile = :p AND fv.profileField = :pf");
		q.setParameter("p", userED);
		q.setParameter("pf", profileField);
		
		q.executeUpdate();
	}

	public void profileFieldValue(ProfileFieldValue profileFieldValue) {
		em.persist(profileFieldValue);
	}

	public void updateFieldValue(ProfileFieldValue profileValue) {
		em.merge(profileValue);
	}

	public List<ProfileFieldValue> loadFieldValues(Long idUser) {
		Query q = em.createQuery("SELECT fv FROM ProfileFieldValue fv WHERE fv.profile.id = :idu");
		q.setParameter("idu", idUser);
		return q.getResultList();
	}
	

}
