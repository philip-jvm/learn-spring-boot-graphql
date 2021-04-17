package com.learn.graphql.config.security;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GrantedAuthorityFactory {

  public static List<GrantedAuthority> getAuthoritiesFrom(String userRoles) {
    if (StringUtils.isBlank(userRoles)) {
      return List.of();
    }

    return Set.of(userRoles.split(","))
        .stream()
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());
  }

}
