package net.bounceme.chronos.inteligenciaartificial.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import jakarta.faces.webapp.FacesServlet;

@Configuration
public class JsfConfig {
	
	@Bean
    ServletRegistrationBean<FacesServlet> facesServletRegistration() {
        // Crea el ServletRegistrationBean para el FacesServlet
        ServletRegistrationBean<FacesServlet> registration = 
            new ServletRegistrationBean<>(new FacesServlet(), "*.xhtml");
        
        // Establece el orden de carga (opcional pero recomendado)
        registration.setLoadOnStartup(1);
        
        return registration;
    }
}
