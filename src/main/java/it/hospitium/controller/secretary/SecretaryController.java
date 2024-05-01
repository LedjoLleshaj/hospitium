package it.hospitium.controller.secretary;

import it.hospitium.model.*;
import it.hospitium.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
public class SecretaryController {
    @Autowired
    private UserRepository repoUser;

    @Autowired
    private SecretaryRepository repoSecretary;

    @Autowired
    private MedicoRepository repoMedico;

    @Autowired
    private PatientRepository repoPatient;

    @Autowired
    private NurseRepository repoNurse;


    @GetMapping("/secretary/home")
    public String home(Model model) {
        model.addAttribute("users", repoUser.findAll());
        return "secretary/home";
    }

    @GetMapping("/secretary/register")
    public String registerPage(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        // get all medico
        model.addAttribute("medici", repoMedico.findAll());
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
            @RequestParam(name = "medico_di_base", required = false) Long medico_id,
            @RequestParam(name = "codice_sanitario", required = false) String codice_sanitario,
            Model model,
            HttpServletRequest request,RedirectAttributes redirectAttributes
    ) {
        model.addAttribute("medici", repoMedico.findAll());
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
                Utils.addError(model, "User could not be saved!");
                //pass the data back to the form


                return "secretary/register";
            }
            // Unhandled exception
            throw exc;
        }


        Optional<Medico> maybeMedico = repoMedico.findById(medico_id);

        switch (role) {
            case PATIENT:
                if (maybeMedico.isEmpty()) {
                    Utils.addError(model, "No such medico");
                    return "secretary/register";
                }
                Patient patient = new Patient(codice_sanitario, user, maybeMedico.get());
                repoPatient.save(patient);
                System.out.println("Patient Saved"+patient.fullName());
                break;
            case MEDICO:
                Medico medico = new Medico(user);
                repoMedico.save(medico);
                System.out.println("Medico Saved"+medico.fullName());
                break;
            case NURSE:
                if (maybeMedico.isEmpty()) {
                    Utils.addError(model, "No such medico");
                    return "secretary/register";
                }
                Nurse nurse = new Nurse(user, maybeMedico.get());
                repoNurse.save(nurse);
                System.out.println("Nurse Saved"+nurse.fullName());
                break;

        }

        return "redirect:/secretary/home";
    }
}
