package io.github.hossensyedriadh.cryptography.controller;

import io.github.hossensyedriadh.cryptography.model.AsymmetricKeyGenerator;
import io.github.hossensyedriadh.cryptography.service.encryption.AsymmetricEncryptionService;
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
@RequestMapping("/asymmetric-encryption")
public class AsymmetricEncryptionController {
    private final ObjectFactory<AsymmetricEncryptionService> asymmetricEncryptionServiceObjectFactory;

    @Autowired
    public AsymmetricEncryptionController(ObjectFactory<AsymmetricEncryptionService> asymmetricEncryptionServiceObjectFactory) {
        this.asymmetricEncryptionServiceObjectFactory = asymmetricEncryptionServiceObjectFactory;
    }

    @GetMapping("/")
    public ModelAndView asymmetricEncryption() {
        return new ModelAndView("asymmetric-encryption");
    }

    @GetMapping("/key-generator/")
    public ModelAndView keyGenerationForm(Model model) {
        AsymmetricKeyGenerator generator = new AsymmetricKeyGenerator();
        model.addAttribute("Generator", generator);

        return new ModelAndView("asymmetric-key-generator");
    }

    @PostMapping("/generate-key-pair/")
    public ModelAndView generateKey(@ModelAttribute AsymmetricKeyGenerator generator, Model model) {
        AsymmetricKeyGenerator keyGenerator = new AsymmetricKeyGenerator(this.asymmetricEncryptionServiceObjectFactory
                .getObject().generateKeyPair(generator.getKeySize()));
        keyGenerator.setKeySize(generator.getKeySize());
        model.addAttribute("Generator", keyGenerator);

        return new ModelAndView("asymmetric-key-generator");
    }
}
