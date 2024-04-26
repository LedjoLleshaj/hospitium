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
        return "secretary/register";
    }

    @PostMapping("/secretary/register")
    public String register(
            @RequestParam(name = "first_name", required = true) String firstName,
            @RequestParam(name = "last_name", required = true) String lastName,
            @RequestParam(name = "email", required = true) String email,
            @RequestParam(name = "codice_fiscale", required = true) String codice_fiscale,
            @RequestParam(name = "data_di_nascita", required = true) String data_di_nascita,
            @RequestParam(name = "luogo_di_nascita", required = true) String luogo_di_nascita,
            @RequestParam(name = "role", required = true) String stringRole,
            @RequestParam(name = "codice_sanitario", required = false) String codice_sanitario,
            Model model,
            HttpServletRequest request
    ) {

        System.out.println("data di nascita:"+data_di_nascita);

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
            user = new User(firstName, lastName, email,"", codice_fiscale,data_di_nascita,luogo_di_nascita,role);
            System.out.println(user);
            repoUser.save(user);
        } catch (Exception exc) {
            if (Utils.IsCause(exc, DataIntegrityViolationException.class)) {
                Utils.addError(model, "Email already taken");
                return "secretary/register";
            }
            // Unhandled exception
            throw exc;
        }

        return "redirect:/secretary/home";
    }
}
