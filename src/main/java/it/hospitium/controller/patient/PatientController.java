package it.hospitium.controller.patient;

import it.hospitium.controller.EmailService;
import it.hospitium.model.*;
import it.hospitium.utils.Breadcrumb;
import it.hospitium.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private EmailService emailService;

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
    public String new_appointment(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        User user = Utils.loggedUser(request);
        Optional<Patient> maybe_patient = patientRepository.findByUser(user);
        if (maybe_patient.isEmpty()) {
            Utils.addRedirectionError(redirectAttributes, "No such patient");
            return "redirect:/login";
        }
        Patient patient = maybe_patient.get();
        Medico medico_di_base = patient.getMedico();

        // Retrieve all medics
        List<Medico> medici = (List<Medico>) medicoRepository.findAll();
        // add medico di base in top of medici in a set
        medici.add(0, medico_di_base);

        // All visit types
        List<String> categories = Visita.getVisitCategories();

        model.addAttribute("medici", medici);
        model.addAttribute("visitTypes", categories);

        return "/patient/new_appointment";
    }

    // New API endpoint to retrieve only the 'orari' data
    @GetMapping("/patient/orari")
    public ResponseEntity<?> getOrari(HttpServletRequest request, RedirectAttributes redirectAttributes) {

        List<Medico> medici = (List<Medico>) medicoRepository.findAll();

        Set<Medico> medici_set = Set.copyOf(medici);

        // Your logic to fetch 'orari' data
        // This will return only the 'orari' data in JSON format
        // You can reuse the logic used in the previous endpoint to fetch 'orari' data

        Map<Medico, Map<String, ArrayList<String>>> orari = new HashMap<>();
        for (Medico medico : medici_set) {
            List<String> allDates = appointmentRepository.findDataByMedico(medico);
            Map<String, ArrayList<String>> date_time = new HashMap<>();
            for (String date_time_str : allDates) {
                String[] date_time_arr = date_time_str.split("T");
                String date = date_time_arr[0];
                String time = date_time_arr[1];
                date_time.computeIfAbsent(date, k -> new ArrayList<>()).add(time);
            }
            orari.put(medico, date_time);
        }

        return ResponseEntity.ok(orari);
    }

    // Post the new appointment
    @PostMapping("patient/new_appointment")
    public String submitAppointment(
            // get all the parameters from the form
            @RequestParam("date") String date,
            @RequestParam("time") String time,
            @RequestParam("note") String note,
            @RequestParam("visitType") String visitType,
            @RequestParam("urgency") int urgency,
            @RequestParam("medico") long medicoId,
            HttpServletRequest request, RedirectAttributes redirectAttributes) {

        User user = Utils.loggedUser(request);
        Optional<Patient> maybe_patient = patientRepository.findByUser(user);
        if (maybe_patient.isEmpty()) {
            Utils.addRedirectionError(redirectAttributes, "No such patient");
            return "redirect:/login";
        }
        Patient patient = maybe_patient.get();
        Optional<Medico> maybe_medico = medicoRepository.findById(medicoId);
        if (maybe_medico.isEmpty()) {
            Utils.addRedirectionError(redirectAttributes, "No such medico");
            return "redirect:/login";
        }
        Medico medico = maybe_medico.get();

        date = date + "T" + time;

        // add the new appointment to the database
        Appointment appointment = new Appointment(date, time, note, Visita.fromString(visitType), urgency, medico,
                patient);

        // Save the appointment
        appointmentRepository.save(appointment);
        String emailSubject = "Appointment Registered";
        String emailText = "Dear " + user.fullName() + ",\n\nYour appointment was successfully registered.\n\nBest regards,\nHospitium Team";
        emailService.sendSimpleMessage("ledjo.lleshaj@gmail.com", emailSubject, emailText);

        // Go to the profile page
        return "redirect:/patient/profile";
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
        // Get the logged user
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

        // Get the patient
        Patient patient = optionalPatient.get();
        List<Appointment> appointments = appointmentRepository.findByPatient(patient);

        // Patient Data and Appointments
        model.addAttribute("patient", patient);
        model.addAttribute("appointments", appointments);

        // Return the profile page
        return "/patient/profile";
    }

}
