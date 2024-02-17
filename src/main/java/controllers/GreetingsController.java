package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GreetingsController {

    @GetMapping("/greet")
    @ResponseBody
    public String greet() {
        return "greet";
    }

}
