package hu.zeletrik.example.actionmonitor.web.rest.controller;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import hu.zeletrik.example.actionmonitor.service.security.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.zeletrik.example.actionmonitor.service.AuthService;
import hu.zeletrik.example.actionmonitor.service.UserService;
import hu.zeletrik.example.actionmonitor.web.rest.request.LoginRequest;
import hu.zeletrik.example.actionmonitor.web.rest.response.UserResponse;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final AuthService authService;
    private final ConversionService conversionService;

    public UserController(UserService userService, AuthService authService, ConversionService conversionService) {
        this.userService = userService;
        this.authService = authService;
        this.conversionService = conversionService;
    }

    @GetMapping("/others")
    public ResponseEntity<List<UserResponse>> findAllOtherUser(Principal currentUser) {
        LOGGER.info("Find all user flow  started");
        var users = userService.findAll().getBody()
                .stream()
                .filter(user -> !user.getUsername().equals(currentUser.getName()))
                .map(dto -> conversionService.convert(dto, UserResponse.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    @GetMapping("/current")
    public ResponseEntity<UserResponse> retrieveCurrentUser(Principal currentUser) {
        var serviceResponse = userService.findByUsername(currentUser.getName());
        var user =  conversionService.convert(serviceResponse.getBody(), UserResponse.class);

        var status = serviceResponse.isSuccess()
                ? HttpStatus.OK
                : HttpStatus.NO_CONTENT;

        return ResponseEntity
                .status(status)
                .body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest loginRequest) {
        var serviceResponse = authService.login(loginRequest.getUsername(), loginRequest.getPassword());
        var user = conversionService.convert(serviceResponse.getBody(), UserResponse.class);

        var status = serviceResponse.isSuccess()
                ? HttpStatus.OK
                : HttpStatus.UNAUTHORIZED;

        return ResponseEntity
                .status(status)
                .body(user);
    }
}
