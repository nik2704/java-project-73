package hexlet.code.app.service;

import hexlet.code.app.dto.UserDto;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static hexlet.code.app.config.SecurityConfig.DEFAULT_AUTHORITIES;


@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * The base method of adding resource handlers.
     * @param       userDto - user is being created
     * @return       new user
     */
    @Override
    public User createNewUser(final UserDto userDto) {
        final User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        return userRepository.save(user);
    }

    /**
     * The base method of adding resource handlers.
     * @param       id - user id
     * @param       userDto - new user data
     * @return       updated user
     */
    @Override
    public User updateUser(final long id, final UserDto userDto) {
        final User userToUpdate = userRepository.findById(id).get();
        userToUpdate.setEmail(userDto.getEmail());
        userToUpdate.setFirstName(userDto.getFirstName());
        userToUpdate.setLastName(userDto.getLastName());
        userToUpdate.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userRepository.save(userToUpdate);
    }

    /**
     * The base method of adding resource handlers.
     * @return       name of a user
     */
    @Override
    public String getCurrentUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /**
     * The base method of adding resource handlers.
     * @return       current user
     */
    @Override
    public User getCurrentUser() {
        return userRepository.findByEmail(getCurrentUserName()).get();
    }

    /**
     * The base method of adding resource handlers.
     * @param       username - name of a user
     * @return       system userDetails object (the user is found in the repository and mapped to)
     */
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(this::buildSpringUser)
                .orElseThrow(() -> new UsernameNotFoundException("Not found user with 'username': " + username));
    }

    /**
     * The base method of adding resource handlers.
     * @param       user - object User
     * @return       system userDetails object (with authorities defined in DEFAULT_AUTHORITIES)
     */
    private UserDetails buildSpringUser(final User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                DEFAULT_AUTHORITIES
        );
    }
}
