package hexlet.code.app;

import hexlet.code.app.config.SpringConfig;
import hexlet.code.app.dto.TaskStatusDto;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static hexlet.code.app.config.SpringConfig.TEST_PROFILE;
import static hexlet.code.app.utils.TestUtils.asJson;
import static hexlet.code.app.utils.TestUtils.STATUS_CONTROLLER_PATH;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringConfig.class)
public class TaskStatusControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TestUtils utils;


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

        utils.perform(
                post(STATUS_CONTROLLER_PATH)
                        .content(asJson(statusDto))
                        .contentType(APPLICATION_JSON),
                expectedUser.getEmail()
        ).andExpect(status().isCreated())
        .andReturn()
        .getResponse();

        final var newStatusDto = new TaskStatusDto("test2");
        final var updateRequest = put(STATUS_CONTROLLER_PATH + "/1")
                .content(asJson(newStatusDto))
                .contentType(APPLICATION_JSON);

        utils.perform(updateRequest, expectedUser.getEmail()).andExpect(status().isOk());

        utils.perform(delete(STATUS_CONTROLLER_PATH + "/1"), expectedUser.getEmail())
                .andExpect(status().isOk());

        assertEquals(initialCount, taskStatusRepository.count());
    }

}
