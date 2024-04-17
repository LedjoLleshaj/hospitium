package it.hospitium.controller.patient;

import it.hospitium.model.*;
import it.hospitium.utils.Breadcrumb;
import it.hospitium.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class PatientController {
    @Autowired
    private UserRepository repoUser;
    @Autowired
    private VisitaRepository visitaRepository;
    @Autowired
    private PatientRepository patientRepository;

    @GetMapping("patient/home")
    public String home(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        User user = Utils.loggedUser(request);
        Optional<Patient> maybe_patient = patientRepository.findByUser(user);
        if (maybe_patient.isEmpty()) {
            Utils.addRedirectionError(redirectAttributes, "No such patient");
            return "redirect:/login";
        }
        Patient patient = maybe_patient.get();
        // Retrieve all visits associated with the patient
        List<Visita> visits = (List<Visita>) visitaRepository.findByPatient(patient);


        // Add attributes
        model.addAllAttributes(
                Map.of(
                        "user", patient,
                        "breadcrumbs", List.of(new Breadcrumb("home", "/patient/home")),
                        "visits", visits,
                        "categories", Visita.getVisitCategories()
                        )
        );

        return "/patient/home";
    }

    @GetMapping("patient/new_appointment")
    public String new_appointment() { return "/patient/new_appointment"; }
}
