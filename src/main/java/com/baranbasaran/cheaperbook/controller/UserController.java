package com.baranbasaran.cheaperbook.controller;

import com.baranbasaran.cheaperbook.common.dto.Response;
import com.baranbasaran.cheaperbook.controller.request.User.CreateUserRequest;
import com.baranbasaran.cheaperbook.controller.request.User.UpdateUserRequest;
import com.baranbasaran.cheaperbook.dto.UserDto;
import com.baranbasaran.cheaperbook.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public Response<List<UserDto>> getUsers() {
        return Response.success(userService.findAll());
    }

    @GetMapping("/{id}")
    public Response<UserDto> getUserById(@PathVariable Long id) {
        return Response.success(userService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Response<UserDto> createUser(@RequestBody @Valid CreateUserRequest userRequest) {
        return Response.success(userService.create(userRequest));
    }

    @PutMapping("/{id}")
    public Response<UserDto> updateUser(@PathVariable Long id, @RequestBody @Valid UpdateUserRequest userRequest) {
        return Response.success(userService.update(id, userRequest));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }

    @PostMapping("{userId}/follow/{userToFollowId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void followUser(@PathVariable Long userId, @PathVariable Long userToFollowId) {
        userService.followUser(userId, userToFollowId);
    }

    @PostMapping("{userId}/unfollow/{userToUnfollowId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unfollowUser(@PathVariable Long userId, @PathVariable Long userToUnfollowId) {
        userService.unfollowUser(userId, userToUnfollowId);
    }

    @GetMapping("{userId}/followers")
    public Response<List<UserDto>> getFollowers(@PathVariable Long userId) {
        return Response.success(userService.getFollowers(userId));
    }

    @GetMapping("{userId}/followings")
    public Response<List<UserDto>> getFollowings(@PathVariable Long userId) {
        return Response.success(userService.getFollowings(userId));
    }

}
