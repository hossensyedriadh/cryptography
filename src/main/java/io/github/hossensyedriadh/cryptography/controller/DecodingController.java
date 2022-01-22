package io.github.hossensyedriadh.cryptography.controller;

import io.github.hossensyedriadh.cryptography.model.EncodingDecodingModel;
import io.github.hossensyedriadh.cryptography.service.decoding.DecodingService;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/decoding")
public class DecodingController {
    private final ObjectFactory<DecodingService> decodingServiceObjectFactory;

    @Autowired
    public DecodingController(ObjectFactory<DecodingService> decodingServiceObjectFactory) {
        this.decodingServiceObjectFactory = decodingServiceObjectFactory;
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        webDataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/")
    public ModelAndView decodingForm(Model model) {
        model.addAttribute("Decoder", new EncodingDecodingModel());
        return new ModelAndView("decoding");
    }

    @PostMapping("/decode/")
    public ModelAndView decode(@ModelAttribute EncodingDecodingModel decodingModel, Model model) {
        EncodingDecodingModel decModel = new EncodingDecodingModel();
        decModel.setEncodedText(decodingModel.getEncodedText());
        decModel.setFormat(decodingModel.getFormat());
        decModel.setPlainText(this.decodingServiceObjectFactory.getObject()
                .decode(decModel.getEncodedText(), decModel.getFormat()));
        model.addAttribute("Decoder", decModel);

        return new ModelAndView("decoding");
    }
}
