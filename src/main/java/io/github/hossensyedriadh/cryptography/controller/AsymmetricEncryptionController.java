package io.github.hossensyedriadh.cryptography.controller;

import io.github.hossensyedriadh.cryptography.service.encryption.AsymmetricEncryptionService;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/asymmetric-encryption")
public class AsymmetricEncryptionController {
    private final ObjectFactory<AsymmetricEncryptionService> asymmetricEncryptionServiceObjectFactory;

    @Autowired
    public AsymmetricEncryptionController(ObjectFactory<AsymmetricEncryptionService> asymmetricEncryptionServiceObjectFactory) {
        this.asymmetricEncryptionServiceObjectFactory = asymmetricEncryptionServiceObjectFactory;
    }

    @GetMapping
    public ModelAndView asymmetricEncryption(Model model) {
        return new ModelAndView("asymmetric-encryption");
    }
}
