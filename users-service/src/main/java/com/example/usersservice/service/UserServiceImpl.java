package com.example.usersservice.service;

import com.example.bookingcommonabstractservice.service.AbstractDataService;
import com.example.commondto.common.UserDto;
import com.example.usersservice.domain.User;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * User service implementation.
 */
@Service
public class UserServiceImpl extends AbstractDataService<Integer, User, UserDto> implements UserService{

  @Override
  public UserDto create( @NotNull UserDto target) {
    Assert.notNull(target,"User dto cannot be null");
    return super.create(target);
  }

  @Override
  public UserDto update(@NotNull UserDto target, final @NotNull  Integer integer) {
    Assert.notNull(target,"Update dto cannot be null");
    return super.update(target, integer);
  }
}
