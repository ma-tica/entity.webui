package com.mcmatica.login.service;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mcmatica.entity.webui.repository.BaseMongoRepositoryImpl;
import com.mcmatica.login.model.BaseUser;

public abstract class BaseLoginService  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7074468624823470155L;

	private static Logger LOGGER = LogManager.getLogger(BaseMongoRepositoryImpl.class);

	
	private BaseUser currentUser;
	
	public boolean login(String userId, String password) {
        
        this.currentUser = this.findUser(userId, password);

        if (currentUser != null) {
            LOGGER.info("login successful for '{}'", userId);

            return true;
        } else {
            LOGGER.info("login failed for '{}'", userId);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Login failed",
                            "Invalid or unknown credentials."));

            return false;
        }
    }
	
	public boolean logout() {
		
        String identifier = this.currentUser.getUserid();

        // invalidate the session
        LOGGER.debug("invalidating session for '{}'", identifier);
        FacesContext.getCurrentInstance().getExternalContext()
                .invalidateSession();

        LOGGER.info("logout successful for '{}'", identifier);
        return true;
    }
	
	public boolean isLoggedIn() {
        return currentUser != null;
    }
	
	protected abstract BaseUser findUser(String userId, String password);
}
