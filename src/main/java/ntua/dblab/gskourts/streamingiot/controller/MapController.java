package ntua.dblab.gskourts.streamingiot.controller;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
@RequiredArgsConstructor
@Setter
public class MapController {

    @GetMapping("/data/geographical")
    public String getMap() {
        return "map";
    }
}
