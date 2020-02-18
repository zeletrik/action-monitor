package hu.zeletrik.example.actionmonitor.web.rest.controller;

import hu.zeletrik.example.actionmonitor.service.AuthService;
import hu.zeletrik.example.actionmonitor.service.UserService;
import hu.zeletrik.example.actionmonitor.web.rest.request.LoginRequest;
import hu.zeletrik.example.actionmonitor.web.rest.response.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> findAllUser() {
        LOGGER.info("Find all user flow  started");
        var users = userService.findAll().getBody()
                .stream()
                .map(dto -> conversionService.convert(dto, UserResponse.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest loginRequest) {
        var serviceResponse = authService.login(loginRequest.getUsername(), loginRequest.getPassword());
        var response = conversionService.convert(serviceResponse.getBody(), UserResponse.class);

        var status = serviceResponse.isSuccess()
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;

        return ResponseEntity
                .status(status)
                .body(response);
    }

}
