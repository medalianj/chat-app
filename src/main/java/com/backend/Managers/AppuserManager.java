package com.backend.Managers;

import com.backend.DAO.AppuserDAO;
import com.backend.IDAO.IAppUserDAO;
import com.backend.IManagers.IAppuserManager;
import com.tables.entities.Appuser;

public class AppuserManager implements IAppuserManager {

    IAppUserDAO AppuserDAO = new AppuserDAO();

    @Override
    public Appuser LoadUserappByUsernameAndPassword(String username, String password) throws Exception {
        try {
            return AppuserDAO.GetAppUserByUsernameAndPassword(username, password);
        }
        catch (Exception exception){
            throw new Exception(exception.getMessage());
        }
    }
}
