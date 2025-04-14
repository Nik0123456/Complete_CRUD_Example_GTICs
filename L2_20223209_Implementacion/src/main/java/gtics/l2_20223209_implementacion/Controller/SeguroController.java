package gtics.l2_20223209_implementacion.Controller;
import gtics.l2_20223209_implementacion.Entity.Seguro;
import gtics.l2_20223209_implementacion.Repository.SeguroRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class SeguroController {

    final SeguroRepository seguroRepository;

    public SeguroController(SeguroRepository seguroRepository) {
        this.seguroRepository = seguroRepository;
    }

    //Listar seguros

    @GetMapping("/seguros/listar")
    public String listarSeguros(Model model) {

        List<Seguro> listaSeguros = seguroRepository.findAll();
        model.addAttribute("listaSeguros", listaSeguros);

        return "ListarSeguros";
    }

}

