package hexlet.code.app.controller;


//import hexlet.code.app.dto.TaskStatusDto;
//import hexlet.code.app.model.TaskStatus;
//import hexlet.code.app.repository.TaskStatusRepository;
//import hexlet.code.app.service.TaskStatusService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.DeleteMapping;
//
//import javax.validation.Valid;
//import java.util.List;


import static hexlet.code.app.controller.TaskStatusController.STATUS_CONTROLLER_PATH;
//import static org.springframework.http.HttpStatus.CREATED;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + STATUS_CONTROLLER_PATH)
public class TaskStatusController {
    public static final String STATUS_CONTROLLER_PATH = "/statuses";
    public static final String ID = "/{id}";
//
//    private final TaskStatusService statusService;
//    private final TaskStatusRepository taskStatusRepository;
//
//    /**
//     * Registration of a new status.
//     * @param       dto status is being added
//     * @return      new Status
//     */
//    @Operation(summary = "Create new status")
//    @ApiResponse(responseCode = "201", description = "Status created")
//    @PostMapping
//    @ResponseStatus(CREATED)
//    public TaskStatus registerNew(@RequestBody @Valid final TaskStatusDto dto) {
//        return statusService.createNewStatus(dto);
//    }
//
//    /**
//     * Get lis of statuses.
//     * @return      List of Statuses
//     */
//    @ApiResponses(@ApiResponse(responseCode = "200", content =
//        @Content(schema = @Schema(implementation = TaskStatus.class))
//        ))
//    @GetMapping
//    public List<TaskStatus> getAll() {
//        return taskStatusRepository.findAll()
//                .stream()
//                .toList();
//    }
//
//    /**
//     * Get Status by Id.
//     * @param       id id of a Status
//     * @return      object Status
//     */
//    @ApiResponses(@ApiResponse(responseCode = "200"))
//    @GetMapping(ID)
//    public TaskStatus getStatusById(@PathVariable final Long id) {
//        return taskStatusRepository.findById(id).get();
//    }
//
//    /**
//     * Update of a status.
//     * @param       id status id
//     * @param       dto new status data
//     * @return      new Status
//     */
//    @PutMapping(ID)
//    public TaskStatus update(@PathVariable final long id, @RequestBody @Valid final TaskStatusDto dto) {
//        return statusService.updateStatus(id, dto);
//    }
//
//    /**
//     * Delete of a status.
//     * @param       id status id
//     */
//    @DeleteMapping(ID)
//    public void delete(@PathVariable final long id) {
//        taskStatusRepository.deleteById(id);
//    }
//
}

