package ntua.dblab.gskourts.streamingiot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutController {

    @GetMapping("/info/about")
    public String about() {
        return "about";
    }
}
