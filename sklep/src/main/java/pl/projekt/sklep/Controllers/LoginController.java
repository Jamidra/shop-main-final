package pl.projekt.sklep.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/api/auth/login")
    public String login() {
        return "login-form"; // Returns the login page view (e.g., login.html or login.jsp)
    }
}
