package it.hospitium.controller.nurse;

import it.hospitium.controller.EmailService;
import it.hospitium.model.*;
import it.hospitium.utils.Breadcrumb;
import it.hospitium.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Controller
public class NurseController {
    @Autowired
    private UserRepository repoUser;
    @Autowired
    private NurseRepository nurseRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private VisitaRepository visitaRepository;

    @GetMapping("nurse/home")
    public String home(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        User user = Utils.loggedUser(request);
        Optional<Nurse> maybe_nurse = nurseRepository.findByUser(user);

        if (maybe_nurse.isEmpty()) {
            Utils.addRedirectionError(redirectAttributes, "No such nurse");
            return "redirect:/login";
        }
        Nurse nurse = maybe_nurse.get();
        List<Appointment> appointments = (List<Appointment>) appointmentRepository.findByNurse(nurse);



        // Add attributes
        model.addAttribute("user", nurse);
        model.addAttribute("breadcrumbs", List.of(new Breadcrumb("home", "/nurse/home")));
        model.addAttribute("appointments", appointments);
        model.addAttribute("categories", Visita.getVisitCategories().subList(Visita.getVisitCategories().size()-2, Visita.getVisitCategories().size()));

        return "/nurse/home";
    }

    @GetMapping("/nurse/profile")
    public String viewProfile(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        User user = Utils.loggedUser(request);

        Optional<Nurse> maybeNurse = nurseRepository.findByUser(user);

        if (maybeNurse.isEmpty()) {
            Utils.addRedirectionError(redirectAttributes, "No such nurse");
            return "redirect:/login";
        }

        Nurse nurse = maybeNurse.get();

        // Add attributes
        model.addAttribute("user", nurse.getUser());
        model.addAttribute("medico", nurse.getMedico().fullName());

        return "/nurse/profile";
    }
    @GetMapping("/nurse/appointment/{id}")
    public String viewAppointmentDetails(@PathVariable Long id, Model model) {
        // Retrieve the appointment by id
        Optional<Appointment> maybeAppointment = appointmentRepository.findById(id);
        if (maybeAppointment.isEmpty()) {
            return "redirect:/nurse/home";
        }

        // Get the appointment
        Appointment appointment = maybeAppointment.get();

        // Add attributes
        model.addAttribute("appointment", appointment);

        return "/nurse/appointment-details";
    }

    @PostMapping("/nurse/appointment/saveResult/{id}")
    public String saveResult(@PathVariable Long id,
                             @RequestParam("resultNote") String resultNote,
                             RedirectAttributes redirectAttributes) {
        // Retrieve the appointment by id
        Optional<Appointment> maybeAppointment = appointmentRepository.findById(id);
        if (maybeAppointment.isEmpty()) {
            return "redirect:/nurse/home";
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
                null,
                null); // Nurse is set to null

        // Save the new visit
        visitaRepository.save(newVisit);
        String emailSubject = "Visit Results are Available";
        String emailText = "Dear " + appointment.getPatient().fullName()
                + ",\n\nThe result of your visit have been saved on your hospitium profile by clicking the link below.\n http://localhost:8080/patient/visit/"
                + newVisit.getId() + "\n\nBest regards,\nHospitium Team";
        emailService.sendSimpleMessage("ledjo.lleshaj@gmail.com", emailSubject, emailText);

        // Delete the appointment
        appointmentRepository.deleteById(id);

        return "redirect:/nurse/home";
    }
}