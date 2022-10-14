package hexlet.code.app.controller;

import hexlet.code.app.dto.TaskDto;
import hexlet.code.app.model.Task;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.service.TaskService;
import hexlet.code.app.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;

import javax.validation.Valid;
import java.util.List;

import static hexlet.code.app.controller.TaskController.TASK_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.CREATED;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + TASK_CONTROLLER_PATH)
public class TaskController {
    public static final String TASK_CONTROLLER_PATH = "/tasks";
    public static final String ID = "/{id}";
    private static final String ONLY_OWNER_BY_ID = """
            @taskRepository.findById(#id).get().getAuthor().getEmail() == authentication.getName()
        """;

    private final UserService userService;
    private final TaskService taskService;
    private final TaskRepository taskRepository;

    /**
     * Registration of a new task.
     * @param       dto task is being added
     * @return      new Task
     */
    @Operation(summary = "Create new task")
    @ApiResponse(responseCode = "201", description = "Task created")
    @PostMapping
    @ResponseStatus(CREATED)
    public Task registerNew(@RequestBody @Valid final TaskDto dto) {
        return taskService.createNewTask(dto);
    }

    /**
     * Get lis of tasks.
     * @return      List of Tasks
     */
    @ApiResponses(@ApiResponse(responseCode = "200", content =
        @Content(schema = @Schema(implementation = Task.class))
        ))
    @GetMapping
    public List<Task> getAll() {
        return taskRepository.findAll()
                .stream()
                .toList();
    }

    /**
     * Get Task by Id.
     * @param       id id of a Task
     * @return      object Task
     */
    @ApiResponses(@ApiResponse(responseCode = "200"))
    @GetMapping(ID)
    public Task getTaskById(@PathVariable final Long id) {
        return taskRepository.findById(id).get();
    }

    /**
     * Update of a task.
     * @param       id task id
     * @param       dto new task data
     * @return      new Task
     */
    @PutMapping(ID)
    public Task update(@PathVariable final long id, @RequestBody @Valid final TaskDto dto) {
        return taskService.updateTask(id, dto);
    }

    /**
     * Delete of a user.
     * @param       id user id
     */
    @DeleteMapping(ID)
    @PreAuthorize(ONLY_OWNER_BY_ID)
    public void delete(@PathVariable final long id) {
        taskRepository.deleteById(id);
    }
}
