package io.github.hossensyedriadh.cryptography.controller;

import io.github.hossensyedriadh.cryptography.service.encoding.EncodingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/encoding")
public class EncodingController {
    private final EncodingService encodingService;

    @Autowired
    public EncodingController(EncodingService encodingService) {
        this.encodingService = encodingService;
    }

    @GetMapping("/")
    public ModelAndView encoding() {
        return new ModelAndView("encoding");
    }
}
