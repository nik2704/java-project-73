package hexlet.code.app.service;

import hexlet.code.app.dto.TaskDto;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.User;


public interface TaskService {
    Task createNewTask(TaskDto taskDto);

    Task updateTask(long id, TaskDto taskDto);

    String getCurrentUserName();

    User getCurrentUser();
}
