package hexlet.code.controller;


import hexlet.code.AppApplication;
import hexlet.code.dto.TaskDto;
import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.utils.TestUtils;
import hexlet.code.config.SpringConfigIT;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static hexlet.code.utils.TestUtils.LABEL_CONTROLLER_PATH;


import java.util.HashSet;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles(SpringConfigIT.TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = AppApplication.class)
public final class LabelControllerIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

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

    @Test
    public void crudLabel() throws Exception {
        utils.regDefaultUser();
        final User expectedUser = userRepository.findAll().get(0);

        Label label = utils.postNewLabel("test", expectedUser.getEmail());

        final var newLabelDto = new LabelDto("test2");
        final var updateRequest = put(
                TestUtils.LABEL_CONTROLLER_PATH + "/{id}", label.getId()
        )
                .content(TestUtils.asJson(newLabelDto))
                .contentType(APPLICATION_JSON);

        utils.perform(updateRequest, expectedUser.getEmail()).andExpect(status().isOk());

        utils.perform(delete(LABEL_CONTROLLER_PATH + "/{id}", label.getId()), expectedUser.getEmail())
                .andExpect(status().isOk());

    }

}
