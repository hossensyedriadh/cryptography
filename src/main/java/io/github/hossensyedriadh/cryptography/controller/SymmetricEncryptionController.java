package io.github.hossensyedriadh.cryptography.controller;

import io.github.hossensyedriadh.cryptography.service.encryption.SymmetricEncryptionService;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/symmetric-encryption")
public class SymmetricEncryptionController {
    private final ObjectFactory<SymmetricEncryptionService> symmetricEncryptionServiceObjectFactory;

    @Autowired
    public SymmetricEncryptionController(ObjectFactory<SymmetricEncryptionService> symmetricEncryptionServiceObjectFactory) {
        this.symmetricEncryptionServiceObjectFactory = symmetricEncryptionServiceObjectFactory;
    }

    @GetMapping("/")
    public ModelAndView symmetricEncryption(Model model) {
        return new ModelAndView("symmetric-encryption");
    }
}
