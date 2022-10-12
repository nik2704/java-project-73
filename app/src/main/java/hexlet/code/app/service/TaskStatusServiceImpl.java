//package hexlet.code.app.service;
//
//import hexlet.code.app.dto.TaskStatusDto;
//import hexlet.code.app.model.TaskStatus;
//import hexlet.code.app.repository.TaskStatusRepository;
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@Transactional
//@AllArgsConstructor
//public class TaskStatusServiceImpl implements TaskStatusService {
//
//    private final TaskStatusRepository taskStatusRepository;
//
//    /**
//     * The base method of adding status.
//     * @param       statusDto - status is being created
//     * @return       new status
//     */
//    @Override
//    public TaskStatus createNewStatus(TaskStatusDto statusDto) {
//        final TaskStatus taskStatus = new TaskStatus();
//        taskStatus.setName(statusDto.getName());
//        return taskStatusRepository.save(taskStatus);
//    }
//
//    /**
//     * The base method of updating status.
//     * @param       id - status id
//     * @param       statusDto - new status data
//     * @return       updated status
//     */
//    @Override
//    public TaskStatus updateStatus(long id, TaskStatusDto statusDto) {
//        final TaskStatus taskStatusToUpdate = taskStatusRepository.findById(id).get();
//        taskStatusToUpdate.setName(statusDto.getName());
//        return taskStatusRepository.save(taskStatusToUpdate);
//    }
//}
//
