package org.example.service;

import org.activiti.engine.task.Task;
import org.example.model.dto.UserDTO;

import java.util.Collection;

public interface TaskService {

    Collection<Task> listTasksByUser(UserDTO userDTO);
}
