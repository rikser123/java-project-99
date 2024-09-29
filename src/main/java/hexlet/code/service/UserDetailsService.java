package hexlet.code.service;

import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService implements UserDetailsManager {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user;
    }

    @Override
    public void createUser(UserDetails user) {
        throw new UnsupportedOperationException("Unimplemented");
    }

    @Override
    public void updateUser(UserDetails user) {
        throw new UnsupportedOperationException("Unimplemented");
    }

    @Override
    public void deleteUser(String username) {
        throw new UnsupportedOperationException("Unimplemented");
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        throw new UnsupportedOperationException("Unimplemented");
    }

    @Override
    public boolean userExists(String username) {
        throw new UnsupportedOperationException("Unimplemented");
    }
}
