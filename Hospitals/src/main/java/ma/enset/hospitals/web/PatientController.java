package ma.enset.hospitals.web;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import ma.enset.hospitals.entities.Patient;
import ma.enset.hospitals.repository.PatientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
public class PatientController {

    private PatientRepository patientRepository;

    @Operation(
            tag = ""
    )
    @GetMapping("/index")
    public String index(Model model, @RequestParam(name = "page",defaultValue = "0") int page ,
                                     @RequestParam(name = "size",defaultValue = "3") int size,
                                     @RequestParam(name = "keyword",defaultValue = "") String key){
        Page<Patient> patientPage=patientRepository.findByNameContains(key,PageRequest.of(page,size));
        /*la vue prendre la liste auprès du model pour qu'il puisse l'afficher : */
        model.addAttribute("listePatients",patientPage.getContent());
        model.addAttribute("pages",new int[patientPage.getTotalPages()]);
        model.addAttribute("currentPage",page);
        model.addAttribute("keyword",key);
        return "patients";

    }
    @GetMapping("/")
    public String home(){
        return "redirect:/user/index";
    }
    @GetMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(@RequestParam(name="id") Long id,
                         @RequestParam(name="keyword", defaultValue = "") String keyword,
                         @RequestParam(name="page", defaultValue = "0") int page){
        patientRepository.deleteById(id);
        return "redirect:/user/index?page="+page+"&keyword="+keyword;
    }

    @GetMapping("/formPatient")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String formPatient(Model model){
        model.addAttribute("patient", new Patient());
        return "formPatient";
    }
    @PostMapping(path = "/save")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String save(Model model, @Valid Patient patient, BindingResult bindingResult,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "") String keyword) {
        if (bindingResult.hasErrors()) return "formPatients";
        patientRepository.save(patient);
        return "redirect:/user/index?page=" + page + "&keyword=" + keyword;
    }
    @GetMapping("/editPatient")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editPatient(Model model, Long id, String keyword, int page){
        Patient patient=patientRepository.findById(id).orElse(null);
        if (patient==null) throw new RuntimeException("patient introuvable");
        model.addAttribute("patient",patient);
        model.addAttribute("page", page);
        model.addAttribute("keyword", keyword);
        return "/editPatient";
    }
}
