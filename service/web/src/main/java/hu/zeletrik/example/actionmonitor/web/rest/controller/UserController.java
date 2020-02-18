package hu.zeletrik.example.actionmonitor.web.rest.controller;

import hu.zeletrik.example.actionmonitor.service.UserService;
import hu.zeletrik.example.actionmonitor.web.rest.response.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final ConversionService conversionService;

    public UserController(UserService userService, ConversionService conversionService) {
        this.userService = userService;
        this.conversionService = conversionService;
    }

    @RequestMapping("/all")
    public ResponseEntity<List<UserResponse>> findAllUser() {
        LOGGER.info("Find all user flow  started");
        var users = userService.findAll()
                .stream()
                .map(dto -> conversionService.convert(dto, UserResponse.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);

    }
}
