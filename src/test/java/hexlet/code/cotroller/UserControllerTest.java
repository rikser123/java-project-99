package hexlet.code.cotroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;



@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    private User user;

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    @BeforeEach
    public void setUp() {
        var faker = new Faker();
        user = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .supply(Select.field(User::getPassword), () -> faker.internet().password())
                .supply(Select.field(User::getFirstName), () -> faker.name().firstName())
                .supply(Select.field(User::getLastName), () -> faker.name().lastName())
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .create();

        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
    }

    @Test
    public void testIndex() throws Exception {
        userRepository.save(user);
        var result = mockMvc.perform(get("/api/users").with(token))
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {
        userRepository.save(user);
        var request = get("/api/users/{id}", user.getId()).with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
            v -> v.node("firstName").isEqualTo(user.getFirstName()),
            v -> v.node("lastName").isEqualTo(user.getLastName()),
          v -> v.node("email").isEqualTo(user.getEmail())
        );
    }

    @Test
    public void testCreate() throws Exception {
        var request = post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(user))
                .with(token);
        mockMvc.perform(request).andExpect(status().isCreated());

        var createdUser = userRepository.findByEmail(user.getEmail()).get();
        assertThat(createdUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(createdUser.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(createdUser.getLastName()).isEqualTo(user.getLastName());
    }

    @Test()
    public void updateTest() throws Exception {
        var newUser = userRepository.save(user);
        var dto = userMapper.map(user);
        dto.setFirstName("burna");
        var request = put("/api/users/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto))
                .with(token);

        mockMvc.perform(request).andExpect(status().isForbidden());

        token = jwt().jwt(builder -> builder.subject(newUser.getEmail()));
        var request2 = put("/api/users/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto))
                .with(token);
        mockMvc.perform(request2).andExpect(status().isOk());

        var changedUser = userRepository.findByEmail(dto.getEmail()).get();
        assertThat(changedUser.getEmail()).isEqualTo(dto.getEmail());
    }

    @Test
    public void deleteTest() throws Exception {
        var newUser = userRepository.save(user);
        var request = delete("/api/users/{id}", user.getId()).with(token);
        mockMvc.perform(request).andExpect(status().isForbidden());

        token = jwt().jwt(builder -> builder.subject(newUser.getEmail()));
        var request2 = delete("/api/users/{id}", user.getId()).with(token);
        mockMvc.perform(request2).andExpect(status().isNoContent());

        assertThat(userRepository.existsById(user.getId())).isEqualTo(false);
    }

}
