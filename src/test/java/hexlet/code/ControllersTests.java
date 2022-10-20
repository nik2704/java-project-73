package hexlet.code;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.dto.LoginDto;
import hexlet.code.dto.TaskDto;
import hexlet.code.dto.LabelDto;
import hexlet.code.dto.TaskStatusDto;
import hexlet.code.dto.UserDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.Label;
import hexlet.code.model.User;
import hexlet.code.model.Task;
import hexlet.code.repository.UserRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.LabelRepository;
import hexlet.code.utils.TestUtils;
import hexlet.code.config.SpringConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.NoSuchElementException;

import static hexlet.code.controller.UserController.ID;

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
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles(SpringConfig.TEST_PROFILE)
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
        final var loginRequest = MockMvcRequestBuilders.post(TestUtils.LOGIN)
                .content(TestUtils.asJson(loginDto)).contentType(APPLICATION_JSON);
        utils.perform(loginRequest).andExpect(status().isOk());
    }

    @Test
    public void getUserById() throws Exception {
        utils.regDefaultUser();
        final User expectedUser = userRepository.findAll().get(0);

        final var response = utils.perform(
                get(TestUtils.USER_CONTROLLER_PATH + ID, expectedUser.getId()),
                expectedUser.getEmail()
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final User user = TestUtils.fromJson(response.getContentAsString(), new TypeReference<>() {
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
                Exception.class, () -> utils.perform(get(TestUtils.USER_CONTROLLER_PATH + ID, expectedUser.getId()))
        );

        String expectedMessage = "No value present";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void getAllUsers() throws Exception {
        utils.regDefaultUser();
        final var response = utils.perform(MockMvcRequestBuilders.get(TestUtils.USER_CONTROLLER_PATH))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<User> users = TestUtils.fromJson(response.getContentAsString(), new TypeReference<>() {
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
        final var loginRequest = MockMvcRequestBuilders.post(TestUtils.LOGIN).content(TestUtils.asJson(loginDto))
                .contentType(APPLICATION_JSON);
        utils.perform(loginRequest).andExpect(status().isUnauthorized());
    }

    @Test
    public void updateUser() throws Exception {
        utils.regDefaultUser();

        final Long userId = userRepository.findByEmail(TestUtils.TEST_USERNAME).get().getId();

        final var userDto = new UserDto(TestUtils.TEST_USERNAME_2, "new name", "new last name", "new pwd");

        final var updateRequest = put(TestUtils.USER_CONTROLLER_PATH + ID, userId)
                .content(TestUtils.asJson(userDto))
                .contentType(APPLICATION_JSON);

        utils.perform(updateRequest, TestUtils.TEST_USERNAME).andExpect(status().isOk());

        assertTrue(userRepository.existsById(userId));
        assertNull(userRepository.findByEmail(TestUtils.TEST_USERNAME).orElse(null));
        assertNotNull(userRepository.findByEmail(TestUtils.TEST_USERNAME_2).orElse(null));
    }

    @Test
    public void deleteUser() throws Exception {
        utils.regDefaultUser();

        final Long userId = userRepository.findByEmail(TestUtils.TEST_USERNAME).get().getId();

        utils.perform(delete(TestUtils.USER_CONTROLLER_PATH + ID, userId), TestUtils.TEST_USERNAME)
                .andExpect(status().isOk());

        assertEquals(0, userRepository.count());
    }

    @Test
    public void deleteUserFails() throws Exception {
        utils.regDefaultUser();
        utils.regUser(new UserDto(
                TestUtils.TEST_USERNAME_2,
                "fname",
                "lname",
                "pwd"
        ));

        final Long userId = userRepository.findByEmail(TestUtils.TEST_USERNAME).get().getId();

        utils.perform(delete(TestUtils.USER_CONTROLLER_PATH + ID, userId), TestUtils.TEST_USERNAME_2)
                .andExpect(status().isForbidden());

        assertEquals(2, userRepository.count());
    }

    // TaskStatus Tests
    @Test
    public void crudTaskStatus() throws Exception {
        utils.perform(MockMvcRequestBuilders.get(TestUtils.STATUS_CONTROLLER_PATH))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        utils.regDefaultUser();
        final User expectedUser = userRepository.findAll().get(0);

        long initialCount = taskStatusRepository.count();

        final TaskStatus taskStatus = postNewStatus("test", expectedUser.getEmail());

        final var newStatusDto = new TaskStatusDto("test2");
        final var updateRequest = put(
                TestUtils.STATUS_CONTROLLER_PATH + "/{id}", taskStatus.getId()
        )
                .content(TestUtils.asJson(newStatusDto))
                .contentType(APPLICATION_JSON);

        utils.perform(updateRequest, expectedUser.getEmail()).andExpect(status().isOk());

        utils.perform(delete(TestUtils.STATUS_CONTROLLER_PATH + "/{id}", taskStatus.getId()), expectedUser.getEmail())
                .andExpect(status().isOk());

        assertEquals(initialCount, taskStatusRepository.count());
    }

    // Task Tests

    @Test
    public void crudTaskNotLoggedInError() throws Exception {
        Exception exception = assertThrows(
                NoSuchElementException.class, () -> utils
                        .perform(MockMvcRequestBuilders.get(TestUtils.TASK_CONTROLLER_PATH))
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
                        MockMvcRequestBuilders.post(TestUtils.TASK_CONTROLLER_PATH)
                                .content(TestUtils.asJson(testTaskCreationDto))
                                .contentType(APPLICATION_JSON)
                )
        );
    }

    /**
     * Clear a task.
     */
    @Test
    public void checkTask() throws Exception {
        utils.regDefaultUser();
        final User expectedUser = userRepository.findAll().get(0);

        TaskStatus status1 = postNewStatus("test", expectedUser.getEmail());
        Label label = postNewLabel("test1", expectedUser.getEmail());
        Set<Long> labels = new HashSet<>();
        labels.add(label.getId());

        TaskDto testTaskDto = new TaskDto();
        testTaskDto.setTaskStatusId(status1.getId());
        testTaskDto.setName("Новая задача");
        testTaskDto.setDescription("Описание новой задачи");
        testTaskDto.setLabelIds(labels);

        Task task = postNewTask(testTaskDto, expectedUser.getEmail());

        assertEquals(task.getAuthor().getId(), expectedUser.getId());
        assertEquals(task.getTaskStatus().getId(), status1.getId());

        List<Task> taskList = getAllTasks(TestUtils.TASK_CONTROLLER_PATH, expectedUser.getEmail());
        assertThat(taskList).hasSize(1);

        TaskStatus status2 = postNewStatus("test2", expectedUser.getEmail());

        label = postNewLabel("test2", expectedUser.getEmail());
        labels = new HashSet<>();
        labels.add(label.getId());

        testTaskDto.setTaskStatusId(status2.getId());
        testTaskDto.setName("Новая задача2");
        testTaskDto.setDescription("Описание новой задачи2");
        testTaskDto.setLabelIds(labels);

        String newQueryStr = TestUtils.TASK_CONTROLLER_PATH + "?taskStatus=" + status2.getId();

        taskList = getAllTasks(newQueryStr, expectedUser.getEmail());

    }

    // Labels test
    @Test
    public void crudLabel() throws Exception {
        utils.regDefaultUser();
        final User expectedUser = userRepository.findAll().get(0);

        Label label = postNewLabel("test", expectedUser.getEmail());

        final var newLabelDto = new LabelDto("test2");
        final var updateRequest = put(
                TestUtils.LABEL_CONTROLLER_PATH + "/{id}", label.getId()
        )
                .content(TestUtils.asJson(newLabelDto))
                .contentType(APPLICATION_JSON);

//        utils.perform(updateRequest, expectedUser.getEmail()).andExpect(status().isOk());
//
//        utils.perform(delete(LABEL_CONTROLLER_PATH + "/{id}", label.getId()), expectedUser.getEmail())
//                .andExpect(status().isOk());

    }

    private Task postNewTask(TaskDto dto, String userName) throws Exception {
        final var response = utils.perform(
                MockMvcRequestBuilders.post(TestUtils.TASK_CONTROLLER_PATH)
                        .content(TestUtils.asJson(dto))
                        .contentType(APPLICATION_JSON),
                userName
        ).andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        Task task = TestUtils.fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        return task;
    }


    public List<Task> getAllTasks(String queryStr, String userName) throws Exception {
        final var response = utils.perform(get(queryStr), userName)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<Task> tasks = TestUtils.fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        return tasks;
    }
    private Label postNewLabel(String txt, String userName) throws Exception {
        final var labelDto = new LabelDto(txt);

        final var response = utils.perform(
                        MockMvcRequestBuilders.post(TestUtils.LABEL_CONTROLLER_PATH)
                                .content(TestUtils.asJson(labelDto))
                                .contentType(APPLICATION_JSON),
                        userName
                ).andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        Label label = TestUtils.fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        return label;
    }

    private TaskStatus postNewStatus(String txt, String userName) throws Exception {
        final var labelDto = new LabelDto(txt);

        final var response = utils.perform(
                        MockMvcRequestBuilders.post(TestUtils.STATUS_CONTROLLER_PATH)
                                .content(TestUtils.asJson(labelDto))
                                .contentType(APPLICATION_JSON),
                        userName
                ).andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        TaskStatus status = TestUtils.fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        return status;
    }
}
