package sec;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import entities.AppUser;
import services.AccountService;
@Service
public class UserDetailsServiceImpl  implements UserDetailsService{

	@Autowired
	AccountService accountService;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser appUser=accountService.loadUserByUsername(username);
		if (appUser==null) throw new UsernameNotFoundException("Invalid User");
		Collection<GrantedAuthority> authorities=new ArrayList<>();
		appUser.getRoles().forEach(role->{
			authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
			System.out.println("role est :"+role);
		} ) ;
		
		System.out.println("role est :");
	
		return new User(appUser.getUsername(), appUser.getPassword(), authorities);
	}

}
