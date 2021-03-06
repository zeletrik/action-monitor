package hu.zeletrik.example.actionmonitor.service;

import hu.zeletrik.example.actionmonitor.service.dto.ServiceResponse;
import hu.zeletrik.example.actionmonitor.service.dto.UserDTO;

import java.util.List;

public interface UserService {

    /**
     * Retrieve the User based on the name.
     *
     * @param username the nem of the user
     * @return the given user
     */
    ServiceResponse<UserDTO> findByUsername(String username);

    /**
     * Find all user
     *
     * @return all user
     */
    ServiceResponse<List<UserDTO>> findAll();
}
