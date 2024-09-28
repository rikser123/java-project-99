package hexlet.code.service;

import hexlet.code.dto.LabelDTO;
import hexlet.code.dto.LabelUpdateDTO;
import hexlet.code.execption.ResourceNotFoundException;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelService {
    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private LabelMapper labelMapper;

    public List<LabelDTO> getLabels() {
        return labelRepository.findAll().stream().map(labelMapper::map).toList();
    }

    public LabelDTO getById(Long id) {
        var label = labelRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Label with id " + id + " not found"));
        return labelMapper.map(label);
    }

    public LabelDTO create(LabelUpdateDTO labelData) {
        var label = labelMapper.map(labelData);
        var newLabel = labelRepository.save(label);
        return labelMapper.map(newLabel);
    }

    public LabelDTO update(Long id, LabelUpdateDTO labelData) {
        var label = labelRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Label with id " + id + " not found"));
        labelMapper.update(labelData, label);
        labelRepository.save(label);
        return labelMapper.map(label);
    }

    public void delete(Long id) {
        labelRepository.deleteById(id);
    }
}
