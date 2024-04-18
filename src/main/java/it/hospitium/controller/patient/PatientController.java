package it.hospitium.controller.patient;

import it.hospitium.model.*;
import it.hospitium.utils.Breadcrumb;
import it.hospitium.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

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
    @Autowired
    private MedicoRepository medicoRepository;

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
                        "categories", Visita.getVisitCategories()));

        return "/patient/home";
    }

    @GetMapping("patient/new_appointment")
    public String new_appointment(Model model) {
        // All medici
        List<Medico> medici = (List<Medico>) medicoRepository.findAll();

        // All visit types
        List<String> categories = Visita.getVisitCategories();

        // Add attributes
        model.addAttribute("medici", medici);
        model.addAttribute("visitTypes", categories);

        return "/patient/new_appointment";
    }

    // Post the new appointment
    @PostMapping("patient/new_appointment")
    public String submitAppointment(
            // get all the parameters from the form
            @RequestParam("date") String date,
            @RequestParam("visitType") String visitType,
            @RequestParam("urgency") int urgency,
            @RequestParam("medico") long medicoId) {

        // Print the parameters
        System.out.println("Date: " + date);
        System.out.println("Visit Type: " + visitType);
        System.out.println("Urgency: " + urgency);
        System.out.println("Medico ID: " + medicoId);

        // Go to the profile page
        return "/patient/profile";
    }

    @GetMapping("patient/visit/{id}")
    public String visit(@PathVariable Long id, Model model) {
        // Retrieve the visit by id
        Optional<Visita> maybeVisit = visitaRepository.findById(id);
        if (maybeVisit.isEmpty()) {
            // if the visit does not exist, redirect to home
            return "redirect:/patient/home";
        }

        // Get the visit
        Visita visit = maybeVisit.get();

        // Add attributes
        model.addAttribute("visit", visit);

        return "/patient/visit";
    }

    @GetMapping("/patient/profile")
    public String viewProfile(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        // Get the logged-in user
        User user = Utils.loggedUser(request);

        // Check authentication
        if (user == null) {
            return "redirect:/login";
        }

        // Get the patient
        Optional<Patient> optionalPatient = patientRepository.findByUser(user);

        if (optionalPatient.isEmpty()) {
            // Error
            Utils.addRedirectionError(redirectAttributes, "No such patient");
            // Redirect to login
            return "redirect:/login";
        }

        // Patient Data
        model.addAttribute("patient", optionalPatient.get());

        // Return the profile page
        return "/patient/profile";
    }

}
