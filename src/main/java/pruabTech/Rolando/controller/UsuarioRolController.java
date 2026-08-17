package pruabTech.Rolando.controller;

import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pruabTech.Rolando.domain.Usuario;
import pruabTech.Rolando.service.UsuarioService;

@Controller
@RequestMapping("/usuario_rol")
public class UsuarioRolController {

    private final UsuarioService usuarioService;

    public UsuarioRolController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // 1. Endpoint para la vista inicial (sin usuario)
    @GetMapping("/mantenimiento")
    public String mantenimiento(Model model) {
        model.addAttribute("usuario", new Usuario());
        // Se inicializan listas vacías para evitar errores de Thymeleaf
        model.addAttribute("rolesAsignados", Collections.emptySet());
        model.addAttribute("rolesDisponibles", Collections.emptyList());
        return "usuario_rol/mantenimiento";
    }

    // 2. Endpoint para buscar y mostrar roles
    @GetMapping("/buscar")
    public String buscarUsuario(@RequestParam("username") String username, Model model) {
        // Usa el método existente en UsuarioService
        Usuario usuario = usuarioService.getUsuarioPorUsername(username).orElse(null);

        //Se guarda lo buscado para poder avisar en la vista si no se encontró nada
        model.addAttribute("usernameBuscado", username);
        model.addAttribute("noEncontrado", usuario == null);

        if (usuario != null) {
            model.addAttribute("usuario", usuario);
            // Obtener todos los nombres de roles
            List<String> todosRolesNombres = usuarioService.getRolesNombres();

            // Filtrar los roles disponibles (los que no tiene el usuario)
            List<String> rolesDisponibles = todosRolesNombres.stream()
                    .filter(rolNombre -> usuario.getRoles().stream()
                    .noneMatch(rolAsignado -> rolAsignado.getRol().equals(rolNombre)))
                    .toList();

            model.addAttribute("rolesAsignados", usuario.getRoles());
            model.addAttribute("rolesDisponibles", rolesDisponibles);
        } else {
            //Si no se encontró, igual hay que dejar un Usuario vacío para que el
            //formulario de búsqueda (th:object/th:field) pueda enlazarse sin fallar.
            Usuario vacio = new Usuario();
            vacio.setUsername(username);
            model.addAttribute("usuario", vacio);
            model.addAttribute("rolesAsignados", Collections.emptySet());
            model.addAttribute("rolesDisponibles", Collections.emptyList());
        }

        return "usuario_rol/mantenimiento"; // Vuelve a la misma página
    }

    // 3. Endpoint para agregar un rol
    @GetMapping("/agregar")
    public String agregarRol(@RequestParam("username") String username,
            @RequestParam("nombreRol") String nombreRol) {

        usuarioService.asignarRolPorUsername(username, nombreRol);

        // Redirige al /buscar para recargar los datos del usuario actualizado
        return "redirect:/usuario_rol/buscar?username=" + username;
    }

    // 4. Endpoint para eliminar un rol
    @GetMapping("/eliminar")
    public String eliminarRol(@RequestParam("username") String username,
            @RequestParam("idRol") Integer idRol) {

        usuarioService.eliminarRol(username, idRol);

        // Redirige al /buscar para recargar los datos del usuario actualizado
        return "redirect:/usuario_rol/buscar?username=" + username;
    }
}
