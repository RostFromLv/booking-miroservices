package com.example.usersservice.rest;

import com.example.commondto.common.UserDto;
import com.example.usersservice.model.Groups;
import com.example.usersservice.service.UserService;
import com.example.usersservice.service.UserServiceImpl;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for work with user.
 *
 * @author Rostyslav
 * @since 1.0.0-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserControllerV1 {

  private final UserService userService;

  @Autowired
  public UserControllerV1(UserServiceImpl userService) {
    this.userService = userService;
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public UserDto getById(@PathVariable final Integer id) {
    return userService.findById(id).get();
  }


  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Collection<UserDto> getAll() {
    return userService.findAll();
  }


  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public UserDto create(@RequestBody
                        @Validated(value = Groups.Create.class) final UserDto userDto) {
    return userService.create(userDto);
  }

  @PutMapping
  @ResponseStatus(HttpStatus.OK)
  public UserDto update(@RequestBody
                        @Validated(value = Groups.Update.class) final UserDto userDto) {
    return userService.update(userDto, userDto.getId());
  }

  @DeleteMapping("/{userId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteById(@PathVariable Integer userId) {
    userService.deleteById(userId);
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteAllUsers(){
    userService.deleteAll();
  }
}
