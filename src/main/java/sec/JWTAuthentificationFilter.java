package sec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import entities.AppUser;

public class JWTAuthentificationFilter extends UsernamePasswordAuthenticationFilter {
private AuthenticationManager authentificationManager;

public JWTAuthentificationFilter(AuthenticationManager authentificationManager) {
	super();
	this.authentificationManager = authentificationManager;
}

@Override
public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
{
	
	try {
		AppUser  appUser=new ObjectMapper().readValue(request.getInputStream(), AppUser.class);
		return authentificationManager.authenticate(new UsernamePasswordAuthenticationToken(appUser.getUsername(), appUser.getPassword()));

	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		throw new RuntimeException(e);
	}
	
	
}
	
@Override
protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
		Authentication authResult) throws IOException, ServletException
{
	User user=(User) authResult.getPrincipal(); 
	List<String> roles=new ArrayList<>();
	user.getAuthorities().forEach(a->{
		roles.add(a.getAuthority());
	});
	String jwt =JWT.create()
			    .withIssuer(request.getRequestURI())
			    .withSubject(user.getUsername())
			    .withArrayClaim("roles",roles.toArray(new String [roles.size()]))
			    .withExpiresAt(new Date(System.currentTimeMillis()+SecurityParams.EXPIRATION))
			    .sign(Algorithm.HMAC256(SecurityParams.SECRET));
	response.addHeader(SecurityParams.HEADER_NAME, jwt);
}
}
