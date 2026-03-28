package ma.ens.security.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/public")
    public String publicAccess() {
        return "Contenu public";
    }

    @GetMapping("/user/profile")
    public String userAccess() {
        return "Espace utilisateur accessible à USER ou ADMIN";
    }

    @GetMapping("/admin/dashboard")
    public String adminAccess() {
        return "Espace administrateur accessible uniquement à ADMIN";
    }
}