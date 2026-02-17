package net.bounceme.chronos.inteligenciaartificial.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
	@GetMapping(path="/")
    public String home() {
        return "forward:/index.xhtml";
    }
}
