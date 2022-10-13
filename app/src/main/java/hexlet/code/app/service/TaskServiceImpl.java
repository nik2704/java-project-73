package hexlet.code.app.service;

import hexlet.code.app.dto.TaskDto;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


@Service
@Transactional
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final TaskStatusRepository taskStatusRepository;

    /**
     * The base method of adding resource handlers.
     * @return       name of a user
     */
    @Override
    public String getCurrentUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /**
     * The base method of adding resource handlers.
     * @return       current user
     */
    @Override
    public User getCurrentUser() {
        return userRepository.findByEmail(getCurrentUserName()).get();
    }

    /**
     * The method of adding new task.
     * @param       taskDto - user is being created
     * @return       new user
     */
    @Override
    public Task createNewTask(final TaskDto taskDto) {
        final Task task = new Task();
        task.setAuthor(getCurrentUser());
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setTaskStatus(taskStatusRepository
                .getById(taskDto.getTaskStatusId()));

        if (!Objects.isNull(taskDto.getExecutorId())) {
            task.setExecutor(userRepository
                    .getById(taskDto.getExecutorId()));
        }

        return taskRepository.save(task);
    }

    /**
     * The base method of updating of tasks.
     * @param       id - task id
     * @param       taskDto - new task data
     * @return       updated task
     */
    @Override
    public Task updateTask(long id, TaskDto taskDto) {
        final Task taskToUpdate = taskRepository.findById(id).get();
        taskToUpdate.setDescription(taskDto.getDescription());
        taskToUpdate.setName(taskDto.getName());

        if (!Objects.isNull(taskDto.getTaskStatusId())) {
            taskToUpdate.setTaskStatus(taskStatusRepository
                    .getById(taskDto.getTaskStatusId()));
        }

        if (!Objects.isNull(taskDto.getExecutorId())) {
            taskToUpdate.setExecutor(userRepository
                    .getById(taskDto.getExecutorId()));
        }
        return taskRepository.save(taskToUpdate);
    }

}
