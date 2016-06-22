package com.procergs.rsp.profile;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.procergs.rsp.profile.ed.ProfileField;
import com.procergs.rsp.profile.ed.ProfileFieldValue;
import com.procergs.rsp.user.ed.UserEd;

@Stateless
@Named
@Path("profile")
public class ProfileService {
	
	@PersistenceContext(unitName = "RSP_PU")
	EntityManager em;

	ProfileBD profileBD;
	
	@PostConstruct
	public void init() {
		profileBD = new ProfileBD(em);
	}

	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/f")
	public List<ProfileField> listaFields(){
		return profileBD.listaFields();
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/{idUser}")
	public List<ProfileFieldValue> loadFieldValues(@PathParam("idUser") Long idUser){
		return profileBD.loadFieldValues(idUser);
	}


	public void removeProfileFieldValue(UserEd userED, ProfileField profileField) {
		profileBD.removeProfileFieldValue(userED, profileField);
	}


	public void insertProfileFieldValue(ProfileFieldValue profileFieldValue) {
		profileBD.profileFieldValue(profileFieldValue);
	}


	public void updateFieldValue(ProfileFieldValue profileValue) {
		profileBD.updateFieldValue(profileValue);
	}


}
