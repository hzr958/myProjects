package com.smate.core.base.utils.security;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class OauthsAuthenticationToken extends AbstractAuthenticationToken {

  /**
   * 
   */
  private static final long serialVersionUID = -1620826307559516075L;


  private final Object userDetails;

  public OauthsAuthenticationToken(final Collection<? extends GrantedAuthority> authorities, final Object userDetails) {
    super(authorities);

    if ((authorities == null) || (userDetails == null)) {
      throw new IllegalArgumentException("Cannot pass null or empty values to constructor");
    }

    this.userDetails = userDetails;
    setAuthenticated(true);
  }

  public Object getUserDetails() {
    return userDetails;
  }

  @Override
  public Object getCredentials() {

    return null;
  }

  @Override
  public Object getPrincipal() {
    return userDetails;
  }
}
