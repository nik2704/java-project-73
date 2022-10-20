package hexlet.code.app.service;

import hexlet.code.app.dto.TaskDto;
import hexlet.code.app.model.Task;


public interface TaskService {
    Task createNewTask(TaskDto taskDto);

    Task updateTask(long id, TaskDto taskDto);
}
