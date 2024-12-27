package br.com.fiap.totem_express;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import static org.mockito.Mockito.*;

public class TestTotemExpressApplication {

	public static void main(String[] args) {
		SpringApplication.from(TotemExpressApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

	@Test
	void should_add_cors_correctly() {
		TotemExpressApplication totemExpressApplication = new TotemExpressApplication();
		CorsRegistry corsRegistry = mock(CorsRegistry.class);
		CorsRegistration corsRegistration = mock(CorsRegistration.class);
		when(corsRegistry.addMapping("/**")).thenReturn(corsRegistration);
		when(corsRegistration.allowedMethods("*")).thenReturn(corsRegistration);
		when(corsRegistration.allowedOrigins("*")).thenReturn(corsRegistration);
		totemExpressApplication.addCorsMappings(corsRegistry);
		verify(corsRegistry).addMapping("/**");
		verify(corsRegistration).allowedMethods("*");
		verify(corsRegistration).allowedOrigins("*");
	}
}
