package it.hospitium.controller;

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
public class AuthController {
    @Autowired
    private UserRepository repoUser;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam(name = "email", required = true) String email,
            @RequestParam(name = "password", required = true) String password,
            Model model,
            HttpServletRequest request
    ) {
        Optional<User> maybeUser = repoUser.findByEmail(email);
        if (maybeUser.isEmpty() || !maybeUser.get().getPswHash().equals(User.hashPsw(password))) { //maybe move hashPsw to Utils?
            Utils.addError(model, "Invalid credentials");
            return "login";
        }
        User user = maybeUser.get();

        // So he/she is logged in for future requests
        request.getSession().setAttribute("HOSPITIUM_EMAIL", user.getEmail());
        request.getSession().setAttribute("HOSPITIUM_PSW_HASH", user.getPswHash());
        model.addAttribute("user", user);

        return redirectToUserHomepage(user);
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().removeAttribute("HOSPITIUM_EMAIL");
        request.getSession().removeAttribute("HOSPITIUM_PSW_HASH");
        return "redirect:/login";
    }

    /**
     * Takes a user and figures out which endpoint represents his/her homepage and redirects to it
     */
    private String redirectToUserHomepage(User user) {
        switch (user.getRole()) {
            case MEDICO:
                return "redirect:/medico/home";
            case PATIENT:
                return "redirect:/patient/home";
            case NURSE:
                return "redirect:/nurse/home";
            case SECRETARY:
                return "redirect:/secretary/home";
            default:
                throw new IllegalStateException("Invalid user role");
        }
    }
}
