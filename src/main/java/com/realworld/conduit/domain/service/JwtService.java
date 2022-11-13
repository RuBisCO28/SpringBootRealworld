package com.realworld.conduit.domain.service;

import com.realworld.conduit.domain.object.User;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public interface JwtService {
  String toToken(User user);
  Optional<String> getSubFromToken(String token);
}
