package hexlet.code.service;

import hexlet.code.dto.TaskStatusCreateDTO;
import hexlet.code.dto.TaskStatusDTO;
import hexlet.code.dto.TaskStatusUpdateDTO;
import hexlet.code.execption.ResourceNotFoundException;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.repository.TaskStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TaskStatusService {
    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskStatusMapper taskStatusMapper;

    public List<TaskStatusDTO> getAll() {
        var statuses = taskStatusRepository.findAll();
        return statuses.stream().map(taskStatusMapper::map).toList();
    }

    public TaskStatusDTO getById(Long id) {
        var status = taskStatusRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Task status with id" + id + " not found!"));
        return taskStatusMapper.map(status);
    }

    public TaskStatusDTO create(TaskStatusCreateDTO taskStatusData) {
        var status = taskStatusMapper.map(taskStatusData);
        var newStatus = taskStatusRepository.save(status);
        return taskStatusMapper.map(newStatus);
    }

    public TaskStatusDTO update(Long id, TaskStatusUpdateDTO taskData) {
        var status = taskStatusRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Task status with id" + id + " not found!"));
        taskStatusMapper.update(taskData, status);
        taskStatusRepository.save(status);
        return taskStatusMapper.map(status);
    }

    public void delete(Long id) {
        var currentStatus = taskStatusRepository.findById(id).get();
        if (currentStatus.getTasks().size() > 0) {
            throw new ResponseStatusException((HttpStatus.INTERNAL_SERVER_ERROR));
        }

        taskStatusRepository.deleteById(id);
    }
}
