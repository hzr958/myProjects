package org.springframework.security.web.authentication.switchrole;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.GrantedAuthorityImpl;

/**
 * Custom <code>GrantedAuthority</code> used by
 * {@link org.springframework.security.web.authentication.SwitchRole.SwitchRoleFilter}
 * <p>
 * Stores the <code>Authentication</code> object of the original user to be used later when
 * 'exiting' from a user switch.
 * </p>
 * 
 * @author Mark St.Godard
 * 
 * @see org.springframework.security.web.authentication.SwitchRole.SwitchRoleFilter
 */
public class SwitchRoleGrantedAuthority extends GrantedAuthorityImpl {
  // ~ Instance fields
  // ================================================================================================

  private static final long serialVersionUID = 1L;
  private Authentication source;

  // ~ Constructors
  // ===================================================================================================

  public SwitchRoleGrantedAuthority(String role, Authentication source) {
    super(role);
    this.source = source;
  }

  // ~ Methods
  // ========================================================================================================

  /**
   * Returns the original user associated with a successful user switch.
   * 
   * @return The original <code>Authentication</code> object of the switched user.
   */
  public Authentication getSource() {
    return source;
  }
}
