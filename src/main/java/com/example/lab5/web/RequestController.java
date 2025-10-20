package com.example.lab5.web;

import com.example.lab5.entity.ApplicationRequest;
import com.example.lab5.entity.Course;
import com.example.lab5.entity.Operator;
import com.example.lab5.repo.ApplicationRequestRepository;
import com.example.lab5.repo.CourseRepository;
import com.example.lab5.repo.OperatorRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class RequestController {
    private final ApplicationRequestRepository requestRepo;
    private final CourseRepository courseRepo;
    private final OperatorRepository operatorRepo;

    public RequestController(ApplicationRequestRepository requestRepo, CourseRepository courseRepo, OperatorRepository operatorRepo) {
        this.requestRepo = requestRepo;
        this.courseRepo = courseRepo;
        this.operatorRepo = operatorRepo;
    }

    @GetMapping("/")
    public String root() { return "redirect:/requests"; }

    @GetMapping("/requests")
    public String list(@RequestParam(required = false) Boolean handled, Model model) {
        List<ApplicationRequest> requests = handled == null ? requestRepo.findAll() : requestRepo.findByHandled(handled);
        model.addAttribute("requests", requests);
        model.addAttribute("handled", handled);
        return "requests";
    }

    @GetMapping("/requests/new")
    public String newForm(Model model) {
        model.addAttribute("courses", courseRepo.findAll());
        return "request_form";
    }

    @PostMapping("/requests")
    public String create(@RequestParam String userName,
                         @RequestParam String phone,
                         @RequestParam String commentary,
                         @RequestParam Long courseId) {
        Course course = courseRepo.findById(courseId).orElseThrow();
        ApplicationRequest req = new ApplicationRequest();
        req.setUserName(userName);
        req.setPhone(phone);
        req.setCommentary(commentary);
        req.setHandled(false);
        req.setCourse(course);
        requestRepo.save(req);
        return "redirect:/requests";
    }

    @GetMapping("/requests/{id}")
    public String details(@PathVariable Long id, Model model) {
        ApplicationRequest req = requestRepo.findById(id).orElseThrow();
        model.addAttribute("req", req);
        model.addAttribute("courses", courseRepo.findAll());
        if (!req.isHandled()) {
            List<Operator> all = operatorRepo.findAll();
            all.removeAll(req.getOperators());
            model.addAttribute("availableOperators", all);
        }
        return "request_details";
    }

    @PostMapping("/requests/{id}/assign")
    public String assign(@PathVariable Long id, @RequestParam(required = false, name = "operatorIds") List<Long> operatorIds) {
        ApplicationRequest req = requestRepo.findById(id).orElseThrow();
        if (req.isHandled()) return "redirect:/requests/" + id;
        if (operatorIds != null) {
            for (Long opId : operatorIds) {
                Operator op = operatorRepo.findById(opId).orElseThrow();
                req.getOperators().add(op);
            }
        }
        req.setHandled(true);
        requestRepo.save(req);
        return "redirect:/requests/" + id;
    }

    @PostMapping("/requests/{id}/operators/{opId}/remove")
    public String removeOperator(@PathVariable Long id, @PathVariable Long opId) {
        ApplicationRequest req = requestRepo.findById(id).orElseThrow();
        operatorRepo.findById(opId).ifPresent(op -> req.getOperators().remove(op));
        requestRepo.save(req);
        return "redirect:/requests/" + id;
    }

    @PostMapping("/requests/{id}/delete")
    public String delete(@PathVariable Long id) {
        requestRepo.deleteById(id);
        return "redirect:/requests";
    }
}
