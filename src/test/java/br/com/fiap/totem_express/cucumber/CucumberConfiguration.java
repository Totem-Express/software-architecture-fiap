package br.com.fiap.totem_express.cucumber;

import br.com.fiap.totem_express.TestcontainersConfiguration;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;


@Import(TestcontainersConfiguration.class)
@CucumberContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class CucumberConfiguration {
}