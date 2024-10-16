package com.baranbasaran.cheaperbook.service;

import com.baranbasaran.cheaperbook.controller.request.User.CreateUserRequest;
import com.baranbasaran.cheaperbook.controller.request.User.UpdateUserRequest;
import com.baranbasaran.cheaperbook.controller.request.User.UserRequest;
import com.baranbasaran.cheaperbook.dto.UserDto;
import com.baranbasaran.cheaperbook.exception.InvalidRequestException;
import com.baranbasaran.cheaperbook.exception.UserAlreadyExistsException;
import com.baranbasaran.cheaperbook.exception.UserNotFoundException;
import com.baranbasaran.cheaperbook.model.User;
import com.baranbasaran.cheaperbook.repository.UserRepository;
import com.baranbasaran.cheaperbook.security.JwtTokenUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidRequestException("Invalid email or password");
        }

        // Return JWT token after successful authentication
        return jwtTokenUtil.generateToken(user.getEmail());
    }

    public String signUp(CreateUserRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }

        User user = User.builder()
                .name(request.getName())
                .username(request.getUsername())
                .email(request.getEmail())
                .birthDate(request.getBirthDate())
                .password(passwordEncoder.encode(request.getPassword())) // Encrypt password
                .phoneNumber(request.getPhoneNumber() != null ? request.getPhoneNumber() : null) // Handle phone number
                .address(request.getAddress() != null ? request.getAddress().to() : null) // Use the 'to' method of AddressRequest
                .build();

        userRepository.save(user);

        // Return JWT token after successful signup
        return jwtTokenUtil.generateToken(user.getEmail());
    }
    @CachePut(value = "users", key = "'user_' + #email")
    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        return UserDto.from(user);  // Convert to DTO
    }


    private boolean userExistsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    private boolean userExistsWithEmailAndPassword(String email, String password) {
        return userRepository.findByEmail(email)
                .map(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElse(false);
    }

    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(UserDto::from)
                .toList();
    }

    public UserDto findById(Long id) {
        return UserDto.from(userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id)));
    }

    public UserDto create(CreateUserRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }

        User user = User.builder()
                .name(request.getName())
                .username(request.getUsername())
                .email(request.getEmail())
                .birthDate(request.getBirthDate())
                .password(passwordEncoder.encode(request.getPassword())) // Encrypt password
                .phoneNumber(request.getPhoneNumber() != null ? request.getPhoneNumber() : null) // Handle phone number
                .address(request.getAddress() != null ? request.getAddress().to() : null) // Use the 'to' method of AddressRequest
                .build();

        userRepository.save(user);

        return UserDto.from(user);
    }

    public UserDto update(Long id, UpdateUserRequest userRequest) {
        userRequest.setId(id);
        return UserDto.from(userRepository.save(updateUser(userRequest)));
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional
    public User updateUser(UpdateUserRequest request) {
        User user = userRepository.findById(request.getId())
                .orElseThrow(() -> new UserNotFoundException(request.getId()));

        if (request.getName() != null) {
            user.setName(request.getName());
        }

        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getUsername() != null) {
            user.setUsername(request.getUsername());
        }
        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getAddress() != null) {
            user.setAddress(request.getAddress().to());
        }
        if (request.getBirthDate() != null) {
            user.setBirthDate(request.getBirthDate());
        }
        return user;
    }
    @Transactional
    public User createOrGetExistingUserFromRequest(UserRequest request) {
        if (request == null) {
            throw new InvalidRequestException("Invalid request");
        }

        User user = null;
        if (request.getId() != null) {
            user = userRepository.findById(request.getId())
                    .orElseThrow(() -> new UserNotFoundException(request.getId()));
            throw new UserAlreadyExistsException("User already exists");
        }

        // Start building the user
        User.UserBuilder userBuilder = User.builder()
                .name(request.getName())
                .username(request.getUsername())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .birthDate(request.getBirthDate());

        // Set address if available
        if (request.getAddress() != null) {
            userBuilder.address(request.getAddress().to());
        }

        // Set password if request has it
        if (request instanceof CreateUserRequest) {
            String rawPassword = ((CreateUserRequest) request).getPassword();
            if (rawPassword != null && !rawPassword.isEmpty()) {
                String encodedPassword = passwordEncoder.encode(rawPassword);
                userBuilder.password(encodedPassword);
            } else {
                throw new InvalidRequestException("Password is required for creating a new user.");
            }
        } else if (request instanceof UpdateUserRequest) {
            String rawPassword = ((UpdateUserRequest) request).getPassword();
            if (rawPassword != null && !rawPassword.isEmpty()) {
                String encodedPassword = passwordEncoder.encode(rawPassword);
                userBuilder.password(encodedPassword);
            }
            // Password is optional for updates
        }

        user = userBuilder.build();
        return user;
    }

    @Caching(evict = {
            @CacheEvict(value = "followers", key = "'followers_' + #userToFollowId"),
            @CacheEvict(value = "followings", key = "'followings_' + #userId")
    })
    public void followUser(Long userId, Long userToFollowId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        User userToFollow = userRepository.findById(userToFollowId).orElseThrow(() -> new UserNotFoundException(userToFollowId));

        if (userToFollow.getFollowers().contains(user)) {
            return;
        }

        user.follow(userToFollow);
        userRepository.save(user);
    }

    @Caching(evict = {
            @CacheEvict(value = "followers", key = "'followers_' + #userToUnfollowId"),
            @CacheEvict(value = "followings", key = "'followings_' + #userId")
    })
    public void unfollowUser(Long userId, Long userToUnfollowId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        User userToUnfollow = userRepository.findById(userToUnfollowId).orElseThrow(() -> new UserNotFoundException(userToUnfollowId));

        if (!userToUnfollow.getFollowers().contains(user)) {
            return;
        }

        user.unfollow(userToUnfollow);
        userRepository.save(user);
    }

    @CachePut(value = "followers", key = "'followers_' + #userId", unless = "#result.size() == 0")
    public List<UserDto> getFollowers(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        return user.getFollowers().stream()
                .map(UserDto::from)
                .toList();
    }

    @CachePut(value = "followings", key = "'followings_' + #userId", unless = "#result.size() == 0")
    public List<UserDto> getFollowings(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        return user.getFollowing().stream()
                .map(UserDto::from)
                .toList();
    }
}
