package it.hospitium.controller.medico;

import it.hospitium.App;
import it.hospitium.model.*;
import it.hospitium.utils.Utils;
import it.hospitium.utils.Breadcrumb;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
public class MedicoController {
    @Autowired
    private UserRepository repoUser;
    @Autowired
    private VisitaRepository visitaRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private MedicoRepository medicoRepository;

    @GetMapping("medico/home")
    public String home(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        User user = Utils.loggedUser(request);
        Optional<Medico> maybe_medico = medicoRepository.findByUser(user);

        if (maybe_medico.isEmpty()) {
            Utils.addRedirectionError(redirectAttributes, "No such medico");
            return "redirect:/login";
        }
        Medico medico = maybe_medico.get();

        // take all the appintments of the medico
        List<Appointment> appointments = (List<Appointment>) appointmentRepository.findByMedico(medico);

        // Add attributes
        model.addAllAttributes(
                Map.of(
                        "user", medico,
                        "breadcrumbs", List.of(new Breadcrumb("home", "/medico/home")),
                        "appointments", appointments));

        return "/medico/home";
    }

}
