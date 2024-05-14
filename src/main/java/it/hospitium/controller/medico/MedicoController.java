package it.hospitium.controller.medico;

import it.hospitium.App;
import it.hospitium.controller.EmailService;
import it.hospitium.model.*;
import it.hospitium.utils.Utils;
import it.hospitium.utils.Breadcrumb;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    @Autowired
    private EmailService emailService;

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
        model.addAttribute("user", medico);
        model.addAttribute("breadcrumbs", List.of(new Breadcrumb("home", "/medico/home")));
        model.addAttribute("appointments", appointments);
        model.addAttribute("categories", Visita.getVisitCategories());

        return "/medico/home";
    }

    @GetMapping("/medico/appointment/{id}")
    public String viewAppointmentDetails(@PathVariable Long id, Model model) {
        // Retrieve the appointment by id
        Optional<Appointment> maybeAppointment = appointmentRepository.findById(id);
        if (maybeAppointment.isEmpty()) {
            return "redirect:/medico/home";
        }

        // Get the appointment
        Appointment appointment = maybeAppointment.get();

        // Add attributes
        model.addAttribute("appointment", appointment);

        return "/medico/appointment-details";
    }

    @PostMapping("/medico/appointment/saveResult/{id}")
    public String saveResult(@PathVariable Long id,
            @RequestParam("resultNote") String resultNote,
            RedirectAttributes redirectAttributes) {
        // Retrieve the appointment by id
        Optional<Appointment> maybeAppointment = appointmentRepository.findById(id);
        if (maybeAppointment.isEmpty()) {
            return "redirect:/medico/home";
        }

        // Get the appointment
        Appointment appointment = maybeAppointment.get();
        // get the visit type
        Visita.VisitType visitType = Visita.VisitType.valueOf(appointment.getVisitType().name());

        // Create a new visit details
        Visita newVisit = new Visita(appointment.getData(),
                resultNote,
                visitType,
                appointment.getUrgenza(),
                appointment.getMedico(),
                appointment.getPatient(),
                null); // Nurse is set to null

        // Save the new visit
        visitaRepository.save(newVisit);
        String emailSubject = "Registration Confirmation";
        String emailText = "Dear " + appointment.getPatient().fullName() + ",\n\nThe result of your visit have been saved on your hospitium profile by clicking the link below.\n http://localhost:8080/patient/visit/"+newVisit.getId() + "\n\nBest regards,\nHospitium Team";
        emailService.sendSimpleMessage("ledjo.lleshaj@gmail.com", emailSubject, emailText);

        return "redirect:/medico/home";
    }
}