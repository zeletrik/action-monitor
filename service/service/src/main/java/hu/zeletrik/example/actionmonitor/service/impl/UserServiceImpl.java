package hu.zeletrik.example.actionmonitor.service.impl;

import hu.zeletrik.example.actionmonitor.data.repository.UserRepository;
import hu.zeletrik.example.actionmonitor.service.dto.UserDTO;
import hu.zeletrik.example.actionmonitor.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
    public UserDTO findByUsername(String username) {
        LOGGER.info("Retrieve user with name={}", username);
        var entity = userRepository.findByUsername(username);
        return conversionService.convert(entity, UserDTO.class);
    }

    @Override
    public List<UserDTO> findAll() {
        LOGGER.info("Retrieve all user");
        var iterable = userRepository.findAll();
        return StreamSupport.stream(iterable.spliterator(), false)
                .map(entity -> conversionService.convert(entity, UserDTO.class))
                .collect(Collectors.toList());
    }
}
