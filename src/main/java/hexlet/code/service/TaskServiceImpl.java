package hexlet.code.service;

import hexlet.code.dto.TaskDto;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    /**
     * The method of adding new task.
     * @param       taskDto - user is being created
     * @return       new user
     */
    @Override
    public Task createNewTask(final TaskDto taskDto) {
        final Task newpost = fromDto(taskDto);
        return taskRepository.save(newpost);
    }

    /**
     * The base method of updating of tasks.
     * @param       id - task id
     * @param       taskDto - new task data
     * @return       updated task
     */
    @Override
    public Task updateTask(long id, TaskDto taskDto) {
        final Task post = taskRepository.findById(id).get();
        merge(post, taskDto);
        return taskRepository.save(post);
    }

    private void merge(final Task post, final TaskDto postDto) {
        final Task newPost = fromDto(postDto);
        post.setName(newPost.getName());
        post.setDescription(newPost.getDescription());
        post.setTaskStatus(newPost.getTaskStatus());
        post.setExecutor(newPost.getExecutor());
        post.setLabels(newPost.getLabels());
    }
    private Task fromDto(final TaskDto dto) {
        final User author = userService.getCurrentUser();

        return Task.builder()
                .author(author)
                .description(dto.getDescription())
                .name(dto.getName())
                .executor(dto.getExecutorId() > 0L ? Optional.ofNullable(dto.getExecutorId())
                        .map(User::new)
                        .orElse(null) : null)
                .taskStatus(Optional.ofNullable(dto.getTaskStatusId())
                        .map(TaskStatus::new)
                        .orElse(null))
                .labels(dto.getLabelIds().stream()
                        .map(labelId -> Optional.ofNullable(labelId)
                                .map(Label::new)
                                .orElse(null))
                        .collect(Collectors.toSet())
                )
                .build();
    }
}
