package it.hospitium.controller.medico;

import it.hospitium.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MedicoController {
    @Autowired
    private UserRepository repoUser;

    @GetMapping("medico/home")
    public String home(Model model) {
        model.addAttribute("users", repoUser.findAll());
        return "/medico/home";
    }

    @GetMapping("medico/search")
    public String search() { return "/patient/search"; }
}
