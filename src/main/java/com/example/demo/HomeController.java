package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashSet;
import java.util.Set;

@Controller
public class HomeController {

    @Autowired
    DirectorRepository directorRepository;

    @RequestMapping("/")
    public String index() {
        return "index";
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
    public String secure() {
        return "secure";
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
