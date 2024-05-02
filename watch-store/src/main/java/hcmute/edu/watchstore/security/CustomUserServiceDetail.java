package hcmute.edu.watchstore.security;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hcmute.edu.watchstore.entity.Role;
import hcmute.edu.watchstore.entity.User;
import hcmute.edu.watchstore.repository.RoleRepository;
import hcmute.edu.watchstore.repository.UserRepository;

@Service
public class CustomUserServiceDetail implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = this.userRepository.findByUsername(username);
        if (user.isPresent()){
            Set<Role> roles = new HashSet<>();
            for(Role r : user.get().getRole()){
                roles.add(this.roleRepository.findById(r.getId()).orElse(null));
            }
            user.get().setRole(roles);
            return new CustomUserDetail(user.get());
        }
        else
            throw new UsernameNotFoundException(username);
    }
}
