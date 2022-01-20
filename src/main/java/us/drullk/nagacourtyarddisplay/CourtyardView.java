package us.drullk.nagacourtyarddisplay;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.validation.Valid;
import java.util.Random;

@Controller
public class CourtyardView implements WebMvcConfigurer {
    @GetMapping("/form")
    public String getSettings(Model model) {
        model.addAttribute("primer", new Connectome(new Random().nextInt()));
        return "form";
    }

    @PostMapping("/form")
    public String generateMaze(@Valid Connectome primer, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "form";
        }

        model.addAttribute("result", primer.toString());
        return "result";
    }
}
