package hexlet.code.app;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.app.dto.LoginDto;
import hexlet.code.app.dto.TaskDto;
import hexlet.code.app.dto.LabelDto;
import hexlet.code.app.dto.TaskStatusDto;
import hexlet.code.app.dto.UserDto;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.Label;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;

import static hexlet.code.app.config.SpringConfig.TEST_PROFILE;
import static hexlet.code.app.controller.UserController.ID;
import static hexlet.code.app.utils.TestUtils.LOGIN;
import static hexlet.code.app.utils.TestUtils.STATUS_CONTROLLER_PATH;
import static hexlet.code.app.utils.TestUtils.USER_CONTROLLER_PATH;
import static hexlet.code.app.utils.TestUtils.TASK_CONTROLLER_PATH;
import static hexlet.code.app.utils.TestUtils.LABEL_CONTROLLER_PATH;
import static hexlet.code.app.utils.TestUtils.TEST_USERNAME;
import static hexlet.code.app.utils.TestUtils.TEST_USERNAME_2;
import static hexlet.code.app.utils.TestUtils.asJson;
import static hexlet.code.app.utils.TestUtils.fromJson;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = AppApplication.class)
public final class ControllersTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    private LabelRepository labelRepository;

    @Autowired
    private TestUtils utils;

    /**
     * Clear the repositories.
     */
    @AfterEach
    public void clear() {
        utils.tearDown();
    }

    @Test
    public void regDefaultUser() throws Exception {
        assertEquals(0, userRepository.count());
        utils.regDefaultUser().andExpect(status().isCreated());
        assertEquals(1, userRepository.count());
    }

    @Test
    public void login() throws Exception {
        utils.regDefaultUser();
        final LoginDto loginDto = new LoginDto(
                utils.getTestRegistrationDto().getEmail(),
                utils.getTestRegistrationDto().getPassword()
        );
        final var loginRequest = post(LOGIN).content(asJson(loginDto)).contentType(APPLICATION_JSON);
        utils.perform(loginRequest).andExpect(status().isOk());
    }

    @Test
    public void getUserById() throws Exception {
        utils.regDefaultUser();
        final User expectedUser = userRepository.findAll().get(0);

        final var response = utils.perform(
                get(USER_CONTROLLER_PATH + ID, expectedUser.getId()),
                expectedUser.getEmail()
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final User user = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expectedUser.getId(), user.getId());
        assertEquals(expectedUser.getEmail(), user.getEmail());
        assertEquals(expectedUser.getFirstName(), user.getFirstName());
        assertEquals(expectedUser.getLastName(), user.getLastName());
    }

    @Test
    public void getUserByIdError() throws Exception {
        utils.regDefaultUser();
        final User expectedUser = userRepository.findAll().get(0);

        Exception exception = assertThrows(
                Exception.class, () -> utils.perform(get(USER_CONTROLLER_PATH + ID, expectedUser.getId()))
        );

        String expectedMessage = "No value present";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void getAllUsers() throws Exception {
        utils.regDefaultUser();
        final var response = utils.perform(get(USER_CONTROLLER_PATH))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<User> users = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(users).hasSize(1);
    }

    @Test
    public void twiceRegTheSameUserFail() throws Exception {
        utils.regDefaultUser().andExpect(status().isCreated());

        Exception exception = assertThrows(
                Exception.class, () -> utils.regDefaultUser().andExpect(status().isBadRequest())
        );

        String expectedMessage = "could not execute statement";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        assertEquals(1, userRepository.count());
    }

    @Test
    public void loginFail() throws Exception {
        final LoginDto loginDto = new LoginDto(
                utils.getTestRegistrationDto().getEmail(),
                "wrongpassword"
        );
        final var loginRequest = post(LOGIN).content(asJson(loginDto))
                .contentType(APPLICATION_JSON);
        utils.perform(loginRequest).andExpect(status().isUnauthorized());
    }

    @Test
    public void updateUser() throws Exception {
        utils.regDefaultUser();

        final Long userId = userRepository.findByEmail(TEST_USERNAME).get().getId();

        final var userDto = new UserDto(TEST_USERNAME_2, "new name", "new last name", "new pwd");

        final var updateRequest = put(USER_CONTROLLER_PATH + ID, userId)
                .content(asJson(userDto))
                .contentType(APPLICATION_JSON);

        utils.perform(updateRequest, TEST_USERNAME).andExpect(status().isOk());

        assertTrue(userRepository.existsById(userId));
        assertNull(userRepository.findByEmail(TEST_USERNAME).orElse(null));
        assertNotNull(userRepository.findByEmail(TEST_USERNAME_2).orElse(null));
    }

    @Test
    public void deleteUser() throws Exception {
        utils.regDefaultUser();

        final Long userId = userRepository.findByEmail(TEST_USERNAME).get().getId();

        utils.perform(delete(USER_CONTROLLER_PATH + ID, userId), TEST_USERNAME)
                .andExpect(status().isOk());

        assertEquals(0, userRepository.count());
    }

    @Test
    public void deleteUserFails() throws Exception {
        utils.regDefaultUser();
        utils.regUser(new UserDto(
                TEST_USERNAME_2,
                "fname",
                "lname",
                "pwd"
        ));

        final Long userId = userRepository.findByEmail(TEST_USERNAME).get().getId();

        utils.perform(delete(USER_CONTROLLER_PATH + ID, userId), TEST_USERNAME_2)
                .andExpect(status().isForbidden());

        assertEquals(2, userRepository.count());
    }

    // TaskStatus Tests
    @Test
    public void crudTaskStatus() throws Exception {
        utils.perform(get(STATUS_CONTROLLER_PATH))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        utils.regDefaultUser();
        final User expectedUser = userRepository.findAll().get(0);

        long initialCount = taskStatusRepository.count();

        final var statusDto = new TaskStatusDto("test");

        final var response = utils.perform(
                        post(STATUS_CONTROLLER_PATH)
                                .content(asJson(statusDto))
                                .contentType(APPLICATION_JSON),
                        expectedUser.getEmail()
                ).andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        final TaskStatus taskStatus = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        final var newStatusDto = new TaskStatusDto("test2");
        final var updateRequest = put(
                STATUS_CONTROLLER_PATH + "/{id}", taskStatus.getId()
        )
                .content(asJson(newStatusDto))
                .contentType(APPLICATION_JSON);

        utils.perform(updateRequest, expectedUser.getEmail()).andExpect(status().isOk());

        utils.perform(delete(STATUS_CONTROLLER_PATH + "/{id}", taskStatus.getId()), expectedUser.getEmail())
                .andExpect(status().isOk());

        assertEquals(initialCount, taskStatusRepository.count());
    }

    // Task Tests

    @Test
    public void crudTaskNotLoggedInError() throws Exception {
        Exception exception = assertThrows(
                NoSuchElementException.class, () -> utils.perform(get(TASK_CONTROLLER_PATH))
        );

        String expectedMessage = "No value present";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

        TaskDto testTaskCreationDto = new TaskDto(
                "Новая задача",
                "Описание новой задачи",
                1,
                1,
                new HashSet<Long>()
        );

        assertThrows(
                NoSuchElementException.class, () -> utils.perform(
                        post(TASK_CONTROLLER_PATH)
                                .content(asJson(testTaskCreationDto))
                                .contentType(APPLICATION_JSON)
                )
        );
    }

    /**
     * Clear a task.
     */
    @Test
    public void createTask() throws Exception {
        utils.regDefaultUser();
        final User expectedUser = userRepository.findAll().get(0);

        final var statusDto = new TaskStatusDto("test");

        utils.perform(
                post(STATUS_CONTROLLER_PATH)
                        .content(asJson(statusDto))
                        .contentType(APPLICATION_JSON),
                expectedUser.getEmail()
        ).andExpect(status().isCreated());

        final TaskStatus status = taskStatusRepository.findAll().get(0);

        TaskDto testTaskDto = new TaskDto(
                "Новая задача",
                "Описание новой задачи",
                expectedUser.getId(),
                status.getId(),
                new HashSet<Long>()
        );

//        utils.perform(
//                post(TASK_CONTROLLER_PATH)
//                        .content(asJson(testTaskDto))
//                        .contentType(APPLICATION_JSON),
//                expectedUser.getEmail()
//        ).andExpect(status().isCreated());

    }

    // Labels test
    @Test
    public void crudLabel() throws Exception {
        utils.regDefaultUser();
        final User expectedUser = userRepository.findAll().get(0);

        final var labelDto = new LabelDto("test");

        final var response = utils.perform(
                        post(LABEL_CONTROLLER_PATH)
                                .content(asJson(labelDto))
                                .contentType(APPLICATION_JSON),
                        expectedUser.getEmail()
                ).andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        final Label label = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        final var newLabelDto = new LabelDto("test2");
        final var updateRequest = put(
                LABEL_CONTROLLER_PATH + "/{id}", label.getId()
        )
                .content(asJson(newLabelDto))
                .contentType(APPLICATION_JSON);

        utils.perform(updateRequest, expectedUser.getEmail()).andExpect(status().isOk());

        utils.perform(delete(LABEL_CONTROLLER_PATH + "/{id}", label.getId()), expectedUser.getEmail())
                .andExpect(status().isOk());

    }

}
