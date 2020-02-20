package hu.zeletrik.example.actionmonitor.service;

import hu.zeletrik.example.actionmonitor.service.dto.ServiceResponse;
import hu.zeletrik.example.actionmonitor.service.dto.UserDTO;

public interface AuthService {

    /**
     * Retrieve the User based on the name.
     *
     * @param username the nem of the user
     * @return the given user
     */
    ServiceResponse<UserDTO> login(String username, String password);
}
