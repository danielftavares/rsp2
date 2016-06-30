package com.procergs.rsp.user;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletRequest;

import com.procergs.rsp.user.ed.UserEd;
import com.procergs.rsp.user.ed.UserLoginED;
import com.procergs.rsp.user.ed.UserRequestED;
import org.apache.commons.codec.binary.Base64;

@Stateless
@Named
public class UserLoginService {
	
	public static String TIPO_AUTENCTICACAO_USER_TOKEN = "RSPUT";
	public static String TIPO_AUTENCTICACAO_BASIC      = "Basic";

    @PersistenceContext(unitName = "RSP_PU")
	EntityManager em;
	
    UserLoginBD userLoginBd;
    
    @EJB
    UserService userService;
    
    @PostConstruct
    public void init(){
    	userLoginBd = new UserLoginBD(em);
    }
    
    
    public UserLoginED gerarLogin(UserEd userEd){
    	userLoginBd.geraLogout(userEd);
    	UserLoginED userLoginED = new UserLoginED();
    	userLoginED.setUserEd(userEd);
    	userLoginED.setToken(UUID.randomUUID().toString());
    	userLoginED.setDataLogin(Calendar.getInstance());
    	userLoginBd.insere(userLoginED);
    	return userLoginED;
    }


	public boolean autenticar(String authCredentials, ServletRequest request) {
		if(authCredentials == null){
			return false; 
		} else if(authCredentials.startsWith(TIPO_AUTENCTICACAO_USER_TOKEN)){
			return autenticarToken(authCredentials.substring(TIPO_AUTENCTICACAO_USER_TOKEN.length()+1), request);
		} else if(authCredentials.startsWith(TIPO_AUTENCTICACAO_BASIC)){
			return autenticarBasic(authCredentials.substring(TIPO_AUTENCTICACAO_BASIC.length()+1), request);
		} else {
			return false;
		}
	}


	private boolean autenticarBasic(String encodedUserPassword, ServletRequest request) {
		try {
			byte[] decodedBytes = Base64.decodeBase64(encodedUserPassword);
			String usernameAndPassword = new String(decodedBytes, "UTF-8");
		
			final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
			final String username = tokenizer.nextToken();
			final String password = tokenizer.nextToken();
			UserEd userEd = userService.login(username, password);
			request.setAttribute(UserRequestED.ATRIBUTO_REQ_USER, new UserRequestED(userEd));
			return userEd != null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return false;
	}


	private boolean autenticarToken(String userAndToken, ServletRequest request) {
		final StringTokenizer tokenizer = new StringTokenizer(userAndToken, ":");
		final Long idUser = Long.parseLong(tokenizer.nextToken());
		final String token = tokenizer.nextToken();
		UserLoginED userLoginED = userLoginBd.buscaLogin(idUser, token);
		request.setAttribute(UserRequestED.ATRIBUTO_REQ_USER, new UserRequestED(userLoginED.getUserEd()));
		return userLoginED != null;
	}


}
