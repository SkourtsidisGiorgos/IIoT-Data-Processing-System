package ntua.dblab.gskourts.streamingiot.controller;

@org.springframework.web.bind.annotation.RestController
public class HelloWorldController {

        @org.springframework.web.bind.annotation.GetMapping("/hello")
        public String hello() {
            return "Hello World!";
        }
}
