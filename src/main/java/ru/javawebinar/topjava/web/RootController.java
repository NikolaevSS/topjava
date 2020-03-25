package ru.javawebinar.topjava.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.util.ControllerUtil;

import javax.servlet.http.HttpServletRequest;

@Controller
public class RootController {
    private final UserService service;

    public RootController(UserService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String root() {
        return "index";
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute("users", service.getAll());
        return "users";
    }

    @PostMapping("/users")
    public String setUser(HttpServletRequest request) {
        SecurityUtil.setAuthUserId(ControllerUtil.getRequireParameter(request, "userId"));
        return "redirect:meals";
    }
}
