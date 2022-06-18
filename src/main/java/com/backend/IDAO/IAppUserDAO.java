package com.backend.IDAO;

import com.tables.entities.Appuser;

public interface IAppUserDAO {

    Appuser GetAppUserByUsernameAndPassword(String username, String password);

}
