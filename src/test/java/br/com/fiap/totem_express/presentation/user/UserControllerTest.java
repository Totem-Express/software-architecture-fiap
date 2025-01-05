package br.com.fiap.totem_express.presentation.user;

import br.com.fiap.totem_express.application.user.CreateUserUseCase;
import br.com.fiap.totem_express.application.user.RetrieveUserUseCase;
import br.com.fiap.totem_express.application.user.output.DefaultUserView;
import br.com.fiap.totem_express.infrastructure.jwt.JWTService;
import br.com.fiap.totem_express.presentation.user.requests.CreateUserRequest;
import br.com.fiap.totem_express.presentation.user.validators.UniqueUserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JWTService jwtService;

    @MockBean
    private RetrieveUserUseCase retrieveUserUseCase;

    @MockBean
    private CreateUserUseCase createUserUseCase;

    @MockBean
    private UniqueUserValidator uniqueUserValidator;

    @Autowired
    private JacksonTester<CreateUserRequest> createUserRequest;
    private DefaultUserView userView;


    @BeforeEach
    void setUp() {
        jwtService = mock(JWTService.class);
        when(uniqueUserValidator.supports(any(Class.class))).thenReturn(true);
        doNothing().when(uniqueUserValidator).validate(any(), any());
    }

    @Test
    void should_return_http_200_when_user_is_created() throws Exception {
        var createUser = new CreateUserRequest("Hilary OBrian", "hilary@hotmail.com", "114.974.750-15");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createUserRequest
                                .write(createUser)
                                .getJson()
                        )
                )
                .andExpect(status().isOk());
    }

    @Test
    void should_return_http_400_when_request_is_invalid() throws Exception {
        var createUser = new CreateUserRequest("", "invalid", "invalid");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createUserRequest
                                .write(createUser)
                                .getJson()
                        )
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_http_200_when_user_is_found() throws Exception {
        userView = new DefaultUserView(1L, "Jolie Pitt");
        when(retrieveUserUseCase.execute("123.456.789-00")).thenReturn(Optional.of(userView));

        mockMvc.perform(get("/api/users")
                        .param("document", "123.456.789-00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Jolie Pitt"));
    }

    @Test
    void should_return_http_404_when_user_is_not_found() throws Exception {
        when(retrieveUserUseCase.execute("123.456.789-00")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users")
                        .param("document", "123.456.789-00"))
                .andExpect(status().isNotFound());
    }
}