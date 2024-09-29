package hexlet.code.component;

import hexlet.code.dto.LabelUpdateDTO;
import hexlet.code.dto.TaskStatusCreateDTO;
import hexlet.code.dto.UserCreateDTO;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.mapper.UserMapper;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskStatusMapper taskStatusMapper;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private LabelMapper labelMapper;

    private void createTaskStatus(String name, String slug) {
        var taskStatusData = new TaskStatusCreateDTO();
        taskStatusData.setName(name);
        taskStatusData.setSlug(slug);
        var status = taskStatusMapper.map(taskStatusData);
        taskStatusRepository.save(status);
    }

    private void createAdmin() {
        var userData = new UserCreateDTO();
        userData.setFirstName("sys");
        userData.setLastName("burna");
        userData.setEmail("hexlet@example.com");
        userData.setPassword(passwordEncoder.encode("qwerty"));
        var user = userMapper.map(userData);
        userRepository.save(user);
    }

    @Override
    public void run(ApplicationArguments args) {
       createAdmin();

       var statusData = new ArrayList<>(List.of(
               Map.of("name", "Draft", "slug", "draft"),
               Map.of("name", "ToReview", "slug", "to_review"),
               Map.of("name", "ToBeFixed", "slug", "to_be_fixed"),
               Map.of("name", "ToPublish", "slug", "to_publish"),
               Map.of("name", "Published", "slug", "published")
        ));

       statusData.forEach(status -> {
           var name = status.get("name");
           var slug = status.get("slug");
           this.createTaskStatus(name, slug);
       });

       var labelFeature = new LabelUpdateDTO();
       labelFeature.setName("feature");
       labelRepository.save(labelMapper.map(labelFeature));

       var labelBug = new LabelUpdateDTO();
       labelBug.setName("bug");
       labelRepository.save(labelMapper.map(labelBug));
    }
}
