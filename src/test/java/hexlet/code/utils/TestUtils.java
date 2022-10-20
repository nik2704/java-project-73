package hexlet.code.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.component.JWTHelper;
import hexlet.code.dto.UserDto;
import hexlet.code.model.User;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Component
public class TestUtils {

    public static final String TEST_USERNAME = "email@email.com";
    public static final String TEST_USERNAME_2 = "email2@email.com";
    public static final String USER_CONTROLLER_PATH = "/api/users";
    public static final String STATUS_CONTROLLER_PATH = "/api/statuses";
    public static final String TASK_CONTROLLER_PATH = "/api/tasks";

    public static final String LABEL_CONTROLLER_PATH = "/api/labels";
    public static final String LOGIN = "/api/login";
    public static final String ID = "/{id}";

    private final UserDto testRegistrationDto = new UserDto(
            TEST_USERNAME,
            "fname",
            "lname",
            "pwd"
    );

    /**
     * Get the DTO for testing purposes.
     * @return       UserDto object
     */
    public UserDto getTestRegistrationDto() {
        return testRegistrationDto;
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private JWTHelper jwtHelper;

    /**
     * Delete repositories.
     */
    public void tearDown() {
        userRepository.deleteAll();
        taskRepository.deleteAll();
    }

    /**
     * Get User by email.
     * @param       email - name of a user
     * @return       User object
     */
    public User getUserByEmail(final String email) {
        return userRepository.findByEmail(email).get();
    }

    /**
     * Create the default user.
     * @return       result of creating action
     */
    public ResultActions regDefaultUser() throws Exception {
        return regUser(testRegistrationDto);
    }

    /**
     * Registering the user provided in the param.
     * @param       dto - name of a user
     * @return       result data
     */
    public ResultActions regUser(final UserDto dto) throws Exception {
        final var request = post(USER_CONTROLLER_PATH)
                .content(asJson(dto))
                .contentType(APPLICATION_JSON);

        return perform(request);
    }

    /**
     * The base method of adding resource handlers.
     * @param       request - the request
     * @param       byUser - string data of user
     * @return       system userDetails object
     */
    public ResultActions perform(final MockHttpServletRequestBuilder request, final String byUser) throws Exception {
        final String token = jwtHelper.expiring(Map.of("username", byUser, "password", "pwd"));

        request.header(AUTHORIZATION, "Bearer " + token);

        return perform(request);
    }

    /**
     * The base method of adding resource handlers.
     * @param       request - the request
     * @return       system userDetails object
     */
    public ResultActions perform(final MockHttpServletRequestBuilder request) throws Exception {
        return mockMvc.perform(request);
    }

    private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

    /**
     * Convert Object to Json.
     * @param       object - the object is being converted
     * @return       JSON string
     */
    public static String asJson(final Object object) throws JsonProcessingException {
        return MAPPER.writeValueAsString(object);
    }

    /**
     * Convert json to an Object of a type provided.
     * @param <T> - the type of an object given from JSON
     * @param       json - the object is being converted
     * @param       to - type reference
     * @return       Object of type needed
     */
    public static <T> T fromJson(final String json, final TypeReference<T> to) throws JsonProcessingException {
        return MAPPER.readValue(json, to);
    }
}
