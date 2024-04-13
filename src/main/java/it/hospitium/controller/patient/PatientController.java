package it.hospitium.controller.patient;

import it.hospitium.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PatientController {
    @Autowired
    private UserRepository repoUser;

    @GetMapping("patient/home")
    public String home(Model model) {
        model.addAttribute("users", repoUser.findAll());
        return "/patient/home";
    }

    @GetMapping("patient/search")
    public String search() { return "/patient/search"; }
}
