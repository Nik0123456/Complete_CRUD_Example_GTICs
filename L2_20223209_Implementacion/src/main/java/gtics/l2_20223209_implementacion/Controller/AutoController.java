package gtics.l2_20223209_implementacion.Controller;
import gtics.l2_20223209_implementacion.Entity.Auto;
import gtics.l2_20223209_implementacion.Repository.AutoRepository;
import gtics.l2_20223209_implementacion.Repository.SedeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class AutoController {

    final AutoRepository autoRepository;
    final SedeRepository sedeRepository;

    public AutoController(AutoRepository autoRepository, SedeRepository sedeRepository) {
        this.autoRepository = autoRepository;
        this.sedeRepository = sedeRepository;
    }

    //Listar autos
    @GetMapping("/autos/listar")
    public String listarAutos(Model model) {

        List<Auto> listaAutos = autoRepository.findAll();
        model.addAttribute("listaAutos", listaAutos);

        return "ListarAutos";
    }

    //Formulario de crear auto
    @GetMapping("/autos/crear")
    public String crearAuto(Model model) {

        model.addAttribute("listaSedes", sedeRepository.findAll());

        return "CrearAutos";
    }

    //Formulario de edicion de un auto
    @GetMapping("/autos/editar")
    public String editarAuto(Model model, @RequestParam("idAuto") int idAuto) {
        Optional<Auto> optAuto = autoRepository.findById(idAuto);
        if (optAuto.isPresent()) {
            Auto auto = optAuto.get();
            model.addAttribute("auto", auto);
            model.addAttribute("listaSedes", sedeRepository.findAll());
            return "EditarAutos";
        }
        else{
            return "redirect:/autos/listar";
        }
    }

    //Guardar auto (Sirve para crear o actualizar dependiendo de si el primary key esta definido o no)
    @PostMapping("/autos/guardar")
    public String guardarAuto(Auto auto, RedirectAttributes attr) {

        if(auto.getIdAuto() == null){
            attr.addFlashAttribute("mensaje", "Auto creado exitosamente");
        }
        else{
            attr.addFlashAttribute("mensaje", "Auto actualizado exitosamente");
        }

        autoRepository.save(auto);
        return "redirect:/autos/listar";
    }

    //Borrar auto
    @GetMapping("/autos/eliminar")
    public String eliminarAuto(@RequestParam("idAuto") int idAuto, RedirectAttributes attr) {

        Optional<Auto> auto = autoRepository.findById(idAuto);

        if(auto.isPresent()){
            autoRepository.deleteById(idAuto);
            attr.addFlashAttribute("mensaje", "Auto borrado exitosamente");
        }

        return "redirect:/autos/listar";
    }

    //Listar autos filtrando por modelo, color o direccion

    @GetMapping("/autos/BuscarAuto")
    public String buscarAuto(@RequestParam("searchTerm") String searchTerm, Model model) {

        if(searchTerm == null || searchTerm.trim().isEmpty()){
            return "redirect:/autos/listar";
        }

        List<Auto> listaAutos = autoRepository.buscarAutoMulticriterio(searchTerm.trim());
        model.addAttribute("listaAutos", listaAutos);
        return "ListarAutos";
    }

}
