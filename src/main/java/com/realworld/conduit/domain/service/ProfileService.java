package com.realworld.conduit.domain.service;

import com.realworld.conduit.domain.object.Profile;
import com.realworld.conduit.domain.object.User;
import com.realworld.conduit.infrastructure.mybatis.mapper.FollowMapper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {
  private final UserService userService;
  private final FollowMapper followMapper;

  public Optional<Profile> findByUsername(String username, User currentUser) {
    final var user = userService.findByName(username);
    if (user == null) {
      return Optional.empty();
    } else {
      Profile profile =
        new Profile(
          user.getId(),
          user.getUsername(),
          user.getBio(),
          user.getImage(),
          (currentUser != null)
            && followMapper.isUserFollowing(currentUser.getId(), user.getId())
        );
      return Optional.of(profile);
    }
  }

  public Profile findByUserId(String userId, User currentUser) {
    final var user = userService.findById(userId);
    return
      new Profile(
        user.getId(),
        user.getUsername(),
        user.getBio(),
        user.getImage(),
        (currentUser != null)
          && followMapper.isUserFollowing(currentUser.getId(), user.getId())
      );
  }
}
