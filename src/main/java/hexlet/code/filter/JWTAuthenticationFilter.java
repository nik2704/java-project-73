package hexlet.code.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.component.JWTHelper;
import hexlet.code.dto.UserDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public final class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final JWTHelper jwtHelper;

    public JWTAuthenticationFilter(final AuthenticationManager authenticationManager,
                                   final RequestMatcher loginRequest,
                                   final JWTHelper jwtHelperValue) {
        super(authenticationManager);
        super.setRequiresAuthenticationRequestMatcher(loginRequest);
        this.jwtHelper = jwtHelperValue;
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request,
                                                final HttpServletResponse response) throws AuthenticationException {
        final UserDto loginData = getLoginData(request);
        final var authRequest = new UsernamePasswordAuthenticationToken(
                loginData.getEmail(),
                loginData.getPassword()
        );
        setDetails(request, authRequest);
        return getAuthenticationManager().authenticate(authRequest);
    }

    private UserDto getLoginData(final HttpServletRequest request) throws AuthenticationException {
        try {
            final String json = request.getReader()
                    .lines()
                    .collect(Collectors.joining());
            return MAPPER.readValue(json, UserDto.class);
        } catch (IOException e) {
            throw new BadCredentialsException("Can't extract login data from request");
        }
    }

    @Override
    protected void successfulAuthentication(final HttpServletRequest request,
                                            final HttpServletResponse response,
                                            final FilterChain chain,
                                            final Authentication authResult) throws IOException {
        final UserDetails user = (UserDetails) authResult.getPrincipal();
        final String token = jwtHelper.expiring(Map.of(SPRING_SECURITY_FORM_USERNAME_KEY, user.getUsername()));

        response.getWriter().println(token);
    }
}
