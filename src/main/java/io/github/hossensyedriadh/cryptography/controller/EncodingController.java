package io.github.hossensyedriadh.cryptography.controller;

import io.github.hossensyedriadh.cryptography.model.EncodingDecodingModel;
import io.github.hossensyedriadh.cryptography.service.encoding.EncodingService;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/encoding")
public class EncodingController {
    private final ObjectFactory<EncodingService> encodingServiceObjectFactory;

    @Autowired
    public EncodingController(ObjectFactory<EncodingService> encodingServiceObjectFactory) {
        this.encodingServiceObjectFactory = encodingServiceObjectFactory;
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        webDataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/")
    public ModelAndView encodingForm(Model model) {
        model.addAttribute("Encoder", new EncodingDecodingModel());
        return new ModelAndView("encoding");
    }

    @PostMapping("/encode/")
    public ModelAndView decode(@ModelAttribute EncodingDecodingModel encodingModel, Model model) {
        EncodingDecodingModel encModel = new EncodingDecodingModel();
        encModel.setPlainText(encodingModel.getPlainText());
        encModel.setFormat(encodingModel.getFormat());
        encModel.setEncodedText(this.encodingServiceObjectFactory.getObject()
                .encode(encModel.getPlainText(), encModel.getFormat()));
        model.addAttribute("Encoder", encModel);

        return new ModelAndView("encoding");
    }
}
