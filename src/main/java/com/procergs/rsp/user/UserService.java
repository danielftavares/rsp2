package com.procergs.rsp.user;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Named;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.procergs.rsp.user.ed.*;
import com.procergs.rsp.util.Propriedades;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import com.procergs.rsp.image.ImageService;
import com.procergs.rsp.image.ed.ImageED;
import com.procergs.rsp.profile.ProfileService;
import com.procergs.rsp.profile.ed.ProfileField;
import com.procergs.rsp.profile.ed.ProfileFieldValue;
import com.procergs.rsp.util.RSPUtil;

@Stateless
@Named
@Path("user")
@TransactionManagement(TransactionManagementType.CONTAINER)
public class UserService {

	@PersistenceContext(unitName = "RSP_PU")
	EntityManager em;

	@EJB
	UserLoginService userLoginService;

	UserBD userBd;

	@EJB
	ImageService imageService;

	@EJB
	ProfileService profileService;

	@PostConstruct
	public void init() {
		userBd = new UserBD(em);
	}

	@POST
	@Produces({ MediaType.APPLICATION_JSON })
    @Consumes(MediaType.APPLICATION_JSON)
	@Path("login")
	public UserLoginED login(@Context HttpHeaders httpHeaders, LoginED loginED, @Context final HttpServletResponse response) {
		try {
			UserEd userPath = userBd.find(loginED.getUsername());
			if (userPath != null && userPath.getLdap() == false) {
				UserEd userEd = login(loginED.getUsername(), loginED.getPassword());
				UserLoginED userLoginED = userLoginService.gerarLogin(userEd);
				return userLoginED;

			} else {
				// autentica por ldap
				Hashtable env = new Hashtable();
				env.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
				env.put(javax.naming.Context.PROVIDER_URL, Propriedades.getInstance("DOL").getValor("RSP.LDAP"));
				LdapContext ctx = new InitialLdapContext(env, null);

				SearchControls ctls = new SearchControls();
				ctls.setSearchScope(2);
				String[] atrib = { "uid" , "cn", "jpegPhoto"};
				ctls.setReturningAttributes(atrib);
				ctls.setCountLimit(100L);

				NamingEnumeration<SearchResult> sr = ctx.search("ou=procergs,o=estado,c=br", "uid="+loginED.getUsername(), ctls);
				String uds = null;
				Attributes userAtributes = null;
				while (sr.hasMore()) {
					SearchResult sre = sr.next();
					uds = sre.getNameInNamespace();
					userAtributes = sre.getAttributes();
				}

				if(userAtributes == null){
					throw new Exception("Loguin Fail");
				}
				env = new Hashtable();
				env.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
				env.put(javax.naming.Context.PROVIDER_URL, Propriedades.getInstance("DOL").getValor("RSP.LDAP"));
				env.put(javax.naming.Context.SECURITY_AUTHENTICATION, "simple");
		//
//				// Authenticate as S. User and password "mysecret"
//				env.put(Context.SECURITY_PRINCIPAL, "procergs-daniel-tavares");
				env.put(javax.naming.Context.SECURITY_PRINCIPAL, uds);
				env.put(javax.naming.Context.SECURITY_CREDENTIALS, loginED.getPassword());
				ctx = new InitialLdapContext(env, null);

				// autenticação bem sucedida!
				if(userPath == null){
					// cria Usuario
					UserEd userEd = new UserEd();
					userEd.setLogin(loginED.getUsername());
					userEd.setNome((String)userAtributes.get("cn").get());
					userEd.setLdap(true);
					
					ImageED imageED = new ImageED();
					imageED.setImage((byte[]) userAtributes.get("jpegPhoto").get());
					imageED.setDate(Calendar.getInstance());
					imageED.setType("jpeg");
					imageService.insert(imageED);
					
					userEd.setProfileImage(imageED);
					userBd.insert(userEd);
					UserLoginED userLoginED = userLoginService.gerarLogin(userEd);
					return userLoginED;
				} else {
					UserLoginED userLoginED = userLoginService.gerarLogin(userPath);
					return userLoginED;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		}
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("l")
	public Collection<UserEd> listUser(@QueryParam("un") String username) {
		return userBd.listUser(username);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/{idUser}")
	public UserEd find(@PathParam("idUser") Long idUser) {
		return userBd.find(idUser);
	}

	@GET
	@Path("/f/{idUser}")
	public void follow(@PathParam("idUser") Long idUser, @Context HttpServletRequest httpRequest) {
		UserEd followed = userBd.find(idUser);
		UserEd follower = ((UserRequestED) httpRequest.getAttribute(UserRequestED.ATRIBUTO_REQ_USER)).getUserEd();

		FollowED f = new FollowED(follower, followed);
		userBd.insertFollow(f);
	}

	@GET
	@Path("/uf/{idUser}")
	public void unfollow(@PathParam("idUser") Long idUser, @Context HttpServletRequest httpRequest) {
		UserEd followed = userBd.find(idUser);
		UserEd follower = ((UserRequestED) httpRequest.getAttribute(UserRequestED.ATRIBUTO_REQ_USER)).getUserEd();
		FollowED f = new FollowED(follower, followed);
		userBd.deleteFollow(f);
	}

	public UserEd login(String username, String password) {
		return userBd.login(username, password);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("lf/{idUser}")
	public FollowingFollowersED listFollowingAndFollowers(@PathParam("idUser") Long idUser) {
		FollowingFollowersED followingFollowersED = new FollowingFollowersED();
		followingFollowersED.setFollowers(userBd.listFollowers(idUser));
		followingFollowersED.setFollowing(userBd.listFollowing(idUser));
		return followingFollowersED;
	}

	@POST
	@Path("/profile")
	@Consumes("multipart/form-data")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void updateProfile(MultipartFormDataInput input, @Context HttpServletRequest httpRequest) {
		UserEd userED = ((UserRequestED) httpRequest.getAttribute(UserRequestED.ATRIBUTO_REQ_USER)).getUserEd();
		userED = userBd.find(userED.getIdUsuario());
		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		try {
			List<ImageED> limages = RSPUtil.getImages(input, "pi");
			for (ImageED imageED : limages) {
				userED.setProfileImage(imageED);
				imageService.insert(imageED);
			}

			List<ProfileFieldValue> values = profileService.loadFieldValues(userED.getIdUsuario());
			List<ProfileField> fields = profileService.listaFields();
			for (ProfileField profileField : fields) {
				if (uploadForm.containsKey("f" + profileField.getIdProfileField())) {
					InputPart inputPart = uploadForm.get("f" + profileField.getIdProfileField()).get(0);
					boolean haveValue = false;
					for (ProfileFieldValue profileValue : values) {
						if (profileValue.getProfileField().equals(profileField)) {
							profileValue.setValue(RSPUtil.getInputPartValue(inputPart));
							profileService.updateFieldValue(profileValue);
							haveValue = true;
						}
					}
					if (!haveValue) {
						ProfileFieldValue profileFieldValue = new ProfileFieldValue();
						profileFieldValue.setProfile(userED);
						profileFieldValue.setProfileField(profileField);
						profileFieldValue.setValue(RSPUtil.getInputPartValue(inputPart));
						profileService.insertProfileFieldValue(profileFieldValue);
					}
				} else {
					profileService.removeProfileFieldValue(userED, profileField);
				}
			}

			// for(String key: uploadForm.keySet()){
			// if(key.matches("[f](\\d*)")){
			// List<InputPart> inputPartsf = uploadForm.get(key);
			// for (InputPart inputPart : inputPartsf) {
			// System.out.println(inputPart.getBodyAsString());
			// }
			// }
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}

		userBd.update(userED);
		System.out.println("TESTE!!");

	}
}
