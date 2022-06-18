package com.backend.IManagers;

import com.tables.entities.Appuser;

public interface IAppuserManager {

    Appuser LoadUserappByUsernameAndPassword(String username, String password) throws Exception;

}
