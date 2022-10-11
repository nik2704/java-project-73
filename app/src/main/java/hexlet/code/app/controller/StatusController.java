package hexlet.code.app.controller;


import hexlet.code.app.dto.StatusDto;
import hexlet.code.app.model.Status;
import hexlet.code.app.repository.StatusRepository;
import hexlet.code.app.service.StatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
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


import static hexlet.code.app.controller.StatusController.STATUS_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.CREATED;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + STATUS_CONTROLLER_PATH)
public class StatusController {
    public static final String STATUS_CONTROLLER_PATH = "/statuses";
    public static final String ID = "/{id}";

    private final StatusService statusService;
    private final StatusRepository statusRepository;

    /**
     * Registration of a new status.
     * @param       dto status is being added
     * @return      new Status
     */
    @Operation(summary = "Create new status")
    @ApiResponse(responseCode = "201", description = "Status created")
    @PostMapping
    @ResponseStatus(CREATED)
    public Status registerNew(@RequestBody @Valid final StatusDto dto) {
        return statusService.createNewStatus(dto);
    }

    /**
     * Get lis of statuses.
     * @return      List of Statuses
     */
    @ApiResponses(@ApiResponse(responseCode = "200", content =
        @Content(schema = @Schema(implementation = Status.class))
        ))
    @GetMapping
    public List<Status> getAll() {
        return statusRepository.findAll()
                .stream()
                .toList();
    }

    /**
     * Get Status by Id.
     * @param       id id of a Status
     * @return      object Status
     */
    @ApiResponses(@ApiResponse(responseCode = "200"))
    @GetMapping(ID)
    public Status getStatusById(@PathVariable final Long id) {
        return statusRepository.findById(id).get();
    }

    /**
     * Update of a status.
     * @param       id status id
     * @param       dto new status data
     * @return      new Status
     */
    @PutMapping(ID)
    public Status update(@PathVariable final long id, @RequestBody @Valid final StatusDto dto) {
        return statusService.updateStatus(id, dto);
    }

    /**
     * Delete of a status.
     * @param       id status id
     */
    @DeleteMapping(ID)
    public void delete(@PathVariable final long id) {
        statusRepository.deleteById(id);
    }

}

