package com.example.demo;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Controller
public class HomeController {

    @Autowired
    DirectorRepository directorRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    ActorRepository actorRepository;

    @Autowired
    CloudinaryConfig cloudc;

    @RequestMapping("/")
    public String listActors(Model model) {
        model.addAttribute("actors", actorRepository.findAll());
        return "list";
    }
//    @RequestMapping("/")
//    public String index() {
//        return "index";
//    }

    @RequestMapping("/add")
    public String newActor(Model model) {
        model.addAttribute("actor", new Actor());
        return "form";
    }

    @PostMapping("/add")
    public String processActor(@ModelAttribute Actor actor,
                               @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "redirect:/add";
        }
        try {
            Map uploadResult = cloudc.upload(file.getBytes(),
                    ObjectUtils.asMap("resourcetype", "auto"));
            actor.setHeadshot((uploadResult.get("url").toString()));
            actorRepository.save(actor);
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:add/";
        }
        return "redirect:/";
    }

    @RequestMapping("/2")
    public String page2() {
        return "page2";
    }

    @RequestMapping("/3")
    public String page3() {
        return "page3";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/admin")
    public String admin() {
        return "admin";
    }

    @RequestMapping("/secure")
    public String secure(Principal principal, Model model) {
        String username = principal.getName();
        model.addAttribute("user", userRepository.findByUsername(username));
        return "secure";
    }

    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/register")
    public String processRegistrationPage(@Valid
                                          @ModelAttribute("user") User user, BindingResult result, Model model) {
        model.addAttribute("user", user);
        if (result.hasErrors()) {
            return "registration";
        } else {
            userService.saveUser(user);
            model.addAttribute("message", "User Account Created");
            return "index";
        }
    }

    @RequestMapping("/movie")
    public String movie(Model model) {
        Director director = new Director();

        director.setName("Matus Jordan");
        director.setGenre("Sci Fi");

        Movie movie = new Movie();
        movie.setTitle("Star Move");
        movie.setYear(2001);
        movie.setDescription("About Star");

        Set<Movie> movies = new HashSet<Movie>();
        movies.add(movie);

        movie = new Movie();
        movie.setTitle("BattleShip");
        movie.setYear(2011);
        movie.setDescription("Fight together wwww");
        movies.add(movie);

        director.setMovies(movies);

        directorRepository.save(director);
        model.addAttribute("directors", directorRepository.findAll());
        return "movie";
    }
}
