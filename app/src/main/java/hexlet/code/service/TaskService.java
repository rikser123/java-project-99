package hexlet.code.service;

import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskFIlterDTO;
import hexlet.code.dto.TaskUpdateDTO;
import hexlet.code.execption.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.repository.TaskRepository;
import hexlet.code.specification.TaskSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskSpecification taskSpecification;

    public List<TaskDTO> getAll(TaskFIlterDTO fIlterDTO) {
        var filter = taskSpecification.build(fIlterDTO);
        var list = taskRepository.findAll(filter);
        return list.stream().map(taskMapper::map).toList();
    }

    public TaskDTO getById(Long id) {
        var task = taskRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Task with id " + id + " not found!"));
        return taskMapper.map(task);
    }

    public TaskDTO create(TaskCreateDTO taskData) {
        var task = taskMapper.map(taskData);
        var newTask = taskRepository.save(task);
        return taskMapper.map(newTask);
    }

    public TaskDTO update(Long id, TaskUpdateDTO taskData) {
        var task = taskRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Task with id " + id + " not found!"));
        taskMapper.update(taskData, task);
        var newTask = taskRepository.save(task);
        return taskMapper.map(newTask);
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }
}
