package gtics.l2_20223209_implementacion.Controller;

import gtics.l2_20223209_implementacion.Entity.Auto;
import gtics.l2_20223209_implementacion.Entity.Sede;
import gtics.l2_20223209_implementacion.Repository.SedeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class SedeController {

    final SedeRepository sedeRepository;

    public SedeController(SedeRepository sedeRepository) {
        this.sedeRepository = sedeRepository;
    }

    //Listar sedes
    @GetMapping("/sedes/listar")
    public String listarSedes(Model model) {

        List<Sede> listaSedes = sedeRepository.findAll();
        model.addAttribute("listaSedes", listaSedes);

        return "ListarSedes";
    }

}
