package it.hospitium.controller.secretary;

import it.hospitium.model.User;
import it.hospitium.model.UserRepository;
import it.hospitium.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class SecretaryController {
    @Autowired
    private UserRepository repoUser;

    @GetMapping("/secretary/home")
    public String home(Model model) {
        model.addAttribute("users", repoUser.findAll());
        return "secretary/home";
    }

    @GetMapping("/secretary/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/secretary/register")
    public String register(
            @RequestParam(name = "first_name", required = true) String firstName,
            @RequestParam(name = "last_name", required = true) String lastName,
            @RequestParam(name = "email", required = true) String email,
            @RequestParam(name = "password", required = true) String password,
            @RequestParam(name = "role", required = true) String stringRole,
            Model model,
            HttpServletRequest request
    ) {
        User.Role role = null;
        switch (stringRole) {
            case "patient":
                role = User.Role.PATIENT;
                break;
            case "medico":
                role = User.Role.MEDICO;
                break;
            case "nurse":
                role = User.Role.NURSE;
                break;
            case "secretary":
                role = User.Role.SECRETARY;
        }

        User user = null;
        try {
            user = new User(firstName, lastName, email, password,"CODICE_FISCALE","00/00/0000" ,role);
            repoUser.save(user);
        } catch (Exception exc) {
            if (Utils.IsCause(exc, DataIntegrityViolationException.class)) {
                Utils.addError(model, "Email already taken");
                return "register";
            }
            // Unhandled exception
            throw exc;
        }

        // return secretary to homepage
        request.getSession().setAttribute("HOSPITIUM_EMAIL", user.getEmail());
        request.getSession().setAttribute("HOSPITIUM_PSW_HASH", user.getPswHash());
        model.addAttribute("user", user);
        return "redirect:/secretary/home";
    }
}
