package io.github.hossensyedriadh.cryptography.controller;

import io.github.hossensyedriadh.cryptography.model.HashModel;
import io.github.hossensyedriadh.cryptography.service.hashing.HashingService;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/hashing")
public class HashingController {
    private final ObjectFactory<HashingService> hashingServiceObjectFactory;

    @Autowired
    public HashingController(ObjectFactory<HashingService> hashingServiceObjectFactory) {
        this.hashingServiceObjectFactory = hashingServiceObjectFactory;
    }

    @GetMapping("/")
    public ModelAndView hashForm(Model model) {
        model.addAttribute("HashRequest", new HashModel());

        return new ModelAndView("hashing");
    }

    @PostMapping("/generate-hash/")
    public ModelAndView generateHash(@ModelAttribute("HashRequest") HashModel hashModel, Model model) {
        hashModel.setHash(hashingServiceObjectFactory.getObject().generateHash(hashModel.getText(), hashModel.getAlgorithm()));
        model.addAttribute("HashRequest", hashModel);

        return new ModelAndView("hashing");
    }
}
