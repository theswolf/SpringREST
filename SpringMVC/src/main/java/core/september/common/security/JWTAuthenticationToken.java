package core.september.common.security;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JWTAuthenticationToken extends AbstractAuthenticationToken{
    private static final long serialVersionUID = 1L;
    private final Object principal;
    private Object details;
 
    Collection  authorities;
    public JWTAuthenticationToken( String jwtToken) {
        super(null);
        super.setAuthenticated(true); // must use super, as we override
        JWTParser parser = new JWTParser(jwtToken);
         
        this.principal=parser.getSub();
         
        this.setDetailsAuthorities();
 
    }
 
    @Override
    public Object getCredentials() {
        return "";
    }
 
    @Override
    public Object getPrincipal() {
        return principal;
    }
    private void setDetailsAuthorities() {
        String username = principal.toString();
        SpringUserDetailsAdapter adapter = new SpringUserDetailsAdapter(username);
        details=adapter;
        authorities=(Collection) adapter.getAuthorities();
         
    }
 
    @Override
    public Collection getAuthorities() {
        return authorities;
    }
}
