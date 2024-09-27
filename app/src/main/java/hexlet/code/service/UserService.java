package hexlet.code.service;

import hexlet.code.dto.UserCreateDTO;
import hexlet.code.dto.UserDTO;
import hexlet.code.dto.UserUpdateDTO;
import hexlet.code.execption.ResourceNotFoundException;
import hexlet.code.mapper.UserMapper;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserUtils userUtils;

    public List<UserDTO> getUsers() {
        var users = userRepository.findAll();
        return users.stream().map(userMapper::map).toList();
    }

    public UserDTO getUser(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found!"));
        return userMapper.map(user);
    }

    public UserDTO createUser(UserCreateDTO userData) {
        var user = userMapper.map(userData);
        var newUser = userRepository.save(user);
        return userMapper.map(newUser);
    }

    public UserDTO updateUser(Long id, UserUpdateDTO userData) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found!"));
        var currentUser = userUtils.getCurrentUser();
        if (!user.getEmail().equals(currentUser.getEmail())) {
           throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        userMapper.update(userData, user);
        var newUser = userRepository.save(user);
        return userMapper.map(newUser);
    }

    public void deleteUser(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found!"));
        var currentUser = userUtils.getCurrentUser();

        if (!user.getEmail().equals(currentUser.getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        userRepository.deleteById(id);
    }
}

// Свободный путь до индекса
