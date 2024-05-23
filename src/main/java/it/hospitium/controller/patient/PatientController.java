package it.hospitium.controller.patient;

import it.hospitium.controller.EmailService;
import it.hospitium.model.*;
import it.hospitium.utils.Breadcrumb;
import it.hospitium.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
    private ChildRepository repoChildren;
    @Autowired
    private PatientRepository repoPatient;
    @Autowired
    private MedicoRepository repoMedico;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private NurseRepository nurseRepository;

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
        medici.remove(medico_di_base);
        // add medico di base in top of medici in a set
        medici.add(0, medico_di_base);

        // Retrive all nurses
        List<Nurse> nurses = (List<Nurse>) nurseRepository.findAll();

        // All visit types
        List<String> categories = Visita.getVisitCategories();

        model.addAttribute("nurses", nurses);
        model.addAttribute("medici", medici);
        model.addAttribute("visitTypes", categories);

        return "/patient/new_appointment";
    }

    @GetMapping("patient/child_list")
    public String child_list(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        User user = Utils.loggedUser(request);
        Optional<Patient> maybe_patient = patientRepository.findByUser(user);
        if (maybe_patient.isEmpty()) {
            Utils.addRedirectionError(redirectAttributes, "No such patient");
            return "redirect:/login";
        }
        Patient patient = maybe_patient.get();
        List<Child> children = repoChildren.findByParent(patient);

        model.addAttribute("children", children);

        return "/patient/child_list";
    }

    @GetMapping("patient/child/{id}")
    public String child(@PathVariable Long id, Model model, HttpServletRequest request,
            RedirectAttributes redirectAttributes) {
        User user = Utils.loggedUser(request);
        Optional<Patient> maybe_patient = patientRepository.findByUser(user);
        if (maybe_patient.isEmpty()) {
            Utils.addRedirectionError(redirectAttributes, "No such patient");
            return "redirect:/login";
        }
        Patient patient = maybe_patient.get();
        Optional<Child> maybe_child = repoChildren.findById(id);
        if (maybe_child.isEmpty()) {
            Utils.addRedirectionError(redirectAttributes, "No such child");
            return "redirect:/patient/child_list";
        }
        Child child = maybe_child.get();
        if (!child.getParent().equals(patient)) {
            Utils.addRedirectionError(redirectAttributes, "No such child");
            return "redirect:/patient/child_list";
        }

        List<Appointment> appointments = appointmentRepository.findByChild(child);

        model.addAttribute("patient", child);
        model.addAttribute("appointments", appointments);

        return "/patient/profile";
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

    @GetMapping("/patient/orari_nurse")
    public ResponseEntity<?> getOrariNurse(HttpServletRequest request, RedirectAttributes redirectAttributes) {

        List<Nurse> nurses = (List<Nurse>) nurseRepository.findAll();

        Set<Nurse> nurses_set = Set.copyOf(nurses);

        // Your logic to fetch 'orari' data
        // This will return only the 'orari' data in JSON format
        // You can reuse the logic used in the previous endpoint to fetch 'orari' data

        Map<Nurse, Map<String, ArrayList<String>>> orari = new HashMap<>();
        for (Nurse nurse : nurses_set) {
            List<String> allDates = appointmentRepository.findDataByNurse(nurse);
            Map<String, ArrayList<String>> date_time = new HashMap<>();
            for (String date_time_str : allDates) {
                String[] date_time_arr = date_time_str.split("T");
                String date = date_time_arr[0];
                String time = date_time_arr[1];
                date_time.computeIfAbsent(date, k -> new ArrayList<>()).add(time);
            }
            orari.put(nurse, date_time);
        }

        return ResponseEntity.ok(orari);
    }

    @GetMapping("/patient/child/register")
    public String registerPage(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        // get all medico
        model.addAttribute("medici", repoMedico.findAll());
        return "patient/child_register";
    }

    @PostMapping("/patient/child/register")
    public String register(
            @RequestParam(name = "first_name", required = true) String firstName,
            @RequestParam(name = "last_name", required = true) String lastName,
            @RequestParam(name = "codice_fiscale", required = true) String codice_fiscale,
            @RequestParam(name = "data_di_nascita", required = true) String data_di_nascita,
            @RequestParam(name = "luogo_di_nascita", required = true) String luogo_di_nascita,
            @RequestParam(name = "medico_di_base", required = false) Long medico_id,
            @RequestParam(name = "codice_sanitario", required = false) String codice_sanitario,
            Model model,
            HttpServletRequest request, RedirectAttributes redirectAttributes) {
        model.addAttribute("medici", repoMedico.findAll());
        System.out.println("data di nascita:" + data_di_nascita);

        // get logged in parent info
        User logged_user = Utils.loggedUser(request);
        Optional<Patient> maybe_patient = patientRepository.findByUser(logged_user);
        if (maybe_patient.isEmpty()) {
            Utils.addRedirectionError(redirectAttributes, "No such patient");
            return "redirect:/login";
        }
        Patient parent = maybe_patient.get();

        User user = null;
        try {
            String tempo_psw = User.generatePsw();
            user = new User(firstName, lastName, "", "", codice_fiscale, data_di_nascita, luogo_di_nascita,
                    User.Role.PATIENT);
            System.out.println(user);
            repoUser.save(user);

            String emailSubject = "Registration Confirmation";
            String emailText = "Dear " + firstName
                    + ",\n\nYour child registration was successful.Please check your child list\n\nBest regards,\nHospitium Team";
            emailService.sendSimpleMessage("ledjo.lleshaj@gmail.com", emailSubject, emailText);

        } catch (Exception exc) {
            if (Utils.IsCause(exc, DataIntegrityViolationException.class)) {
                Utils.addError(model, "The child could not be saved!");
                // pass the data back to the form

                return "patient/child_register";
            }
            // Unhandled exception
            throw exc;
        }

        Optional<Medico> maybeMedico = repoMedico.findById(medico_id);

        if (maybeMedico.isEmpty()) {
            Utils.addError(model, "No such medico");
            return "patient/child_register";
        }
        Child child = new Child(codice_sanitario, user, maybeMedico.get(), parent);
        repoChildren.save(child);
        System.out.println("Child Saved " + child.fullName());

        return "redirect:/patient/child_list";
    }

    @PostMapping("patient/new_appointment")
    public String submitAppointment(
            @RequestParam("date") String date,
            @RequestParam("time") String time,
            @RequestParam("note") String note,
            @RequestParam("visitType") String visitType,
            @RequestParam("urgency") int urgency,
            @RequestParam(value = "medico", required = false) Long medicoId,
            @RequestParam(value = "nurse", required = false) Long nurseId,
            HttpServletRequest request, RedirectAttributes redirectAttributes) {

        User user = Utils.loggedUser(request);
        Optional<Patient> maybe_patient = patientRepository.findByUser(user);
        if (maybe_patient.isEmpty()) {
            Utils.addRedirectionError(redirectAttributes, "No such patient");
            return "redirect:/login";
        }
        Patient patient = maybe_patient.get();
        Medico medico = null;
        Nurse nurse = null;

        // Validate Medico
        if (medicoId != null) {
            Optional<Medico> maybe_medico = medicoRepository.findById(medicoId);
            if (maybe_medico.isEmpty()) {
                Utils.addRedirectionError(redirectAttributes, "No such medico");
                return "redirect:/login";
            }
            medico = maybe_medico.get();
        }

        // Validate Nurse
        if (nurseId != null) {
            Optional<Nurse> maybe_nurse = nurseRepository.findById(nurseId);
            if (maybe_nurse.isEmpty()) {
                Utils.addRedirectionError(redirectAttributes, "No such nurse");
                return "redirect:/login";
            }
            nurse = maybe_nurse.get();
        }

        // Check that at least one of medico or nurse is provided
        if (medico == null && nurse == null) {
            Utils.addRedirectionError(redirectAttributes, "Either medico or nurse must be specified");
            return "redirect:/login";
        }


        System.out.println("--------------------------------");
        System.out.println("medicoId: " + medicoId);
        System.out.println("nurseId: " + nurseId);
        System.out.println("--------------------------------");

        date = date + "T" + time;

        // Create and save the new appointment
        Appointment appointment = new Appointment(date, time, note, Visita.fromString(visitType), urgency, medico,
                nurse, patient, null);
        appointmentRepository.save(appointment);

        /* 
        // Send confirmation email
        String emailSubject = "Appointment Registered";
        String emailText = "Dear " + user.fullName()
                + ",\n\nYour appointment was successfully registered.\n\nBest regards,\nHospitium Team";
        emailService.sendSimpleMessage("ledjo.lleshaj@gmail.com", emailSubject, emailText);
         */

        // Redirect to the profile page
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
