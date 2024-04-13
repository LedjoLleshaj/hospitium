package it.hospitium.controller.patient;

import it.hospitium.model.User;
import it.hospitium.model.UserRepository;
import it.hospitium.utils.Utils;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PatientController {
    @Autowired
    private UserRepository repoUser;

    @GetMapping("patient/home")
    public String home(Model model) {
        model.addAttribute("users", repoUser.findAll());
        return "view/patient/home";
    }
}
