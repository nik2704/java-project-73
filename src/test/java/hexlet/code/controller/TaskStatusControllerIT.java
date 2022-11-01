package hexlet.code.controller;

import hexlet.code.AppApplication;
import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;
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


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles(SpringConfigIT.TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = AppApplication.class)
public final class TaskStatusControllerIT {

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
    public void crudTaskStatus() throws Exception {
        utils.perform(MockMvcRequestBuilders.get(TestUtils.STATUS_CONTROLLER_PATH))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        utils.regDefaultUser();
        final User expectedUser = userRepository.findAll().get(0);

        long initialCount = taskStatusRepository.count();

        final TaskStatus taskStatus = utils.postNewStatus("test", expectedUser.getEmail());

        final var newStatusDto = new TaskStatusDto("test2");
        final var updateRequest = put(
                TestUtils.STATUS_CONTROLLER_PATH + "/{id}", taskStatus.getId()
        )
                .content(TestUtils.asJson(newStatusDto))
                .contentType(APPLICATION_JSON);

        utils.perform(updateRequest, expectedUser.getEmail()).andExpect(status().isOk());

        utils.perform(delete(TestUtils.STATUS_CONTROLLER_PATH + "/{id}", taskStatus.getId()),
                        expectedUser.getEmail())
                .andExpect(status().isOk());

        assertEquals(initialCount, taskStatusRepository.count());
    }

}
