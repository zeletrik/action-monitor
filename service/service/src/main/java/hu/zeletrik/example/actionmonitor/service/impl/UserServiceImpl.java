package hu.zeletrik.example.actionmonitor.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import hu.zeletrik.example.actionmonitor.data.repository.UserRepository;
import hu.zeletrik.example.actionmonitor.service.UserService;
import hu.zeletrik.example.actionmonitor.service.dto.ServiceResponse;
import hu.zeletrik.example.actionmonitor.service.dto.UserDTO;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final ConversionService conversionService;

    public UserServiceImpl(UserRepository userRepository, ConversionService conversionService) {
        this.userRepository = userRepository;
        this.conversionService = conversionService;
    }

    @Override
    public ServiceResponse<UserDTO> findByUsername(String username) {
        LOGGER.info("Retrieve user with name={}", username);
        return userRepository.findByUsername(username)
                .map(entity -> conversionService.convert(entity, UserDTO.class))
                .map(dto -> ServiceResponse.<UserDTO>builder()
                                .success(true)
                                .body(dto)
                                .build())
                .orElse(ServiceResponse.<UserDTO>builder()
                                .success(false)
                                .body(UserDTO.builder().build())
                                .build());
    }

    @Override
    public ServiceResponse<List<UserDTO>> findAll() {
        LOGGER.info("Retrieve all user");
        var iterable = userRepository.findAll();
        var list = StreamSupport.stream(iterable.spliterator(), false)
                .map(entity -> conversionService.convert(entity, UserDTO.class))
                .collect(Collectors.toList());

        return ServiceResponse.<List<UserDTO>>builder()
                .success(true)
                .body(list)
                .build();
    }
}
