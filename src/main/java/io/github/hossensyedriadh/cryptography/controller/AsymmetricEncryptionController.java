package io.github.hossensyedriadh.cryptography.controller;

import io.github.hossensyedriadh.cryptography.enumerator.Method;
import io.github.hossensyedriadh.cryptography.model.AsymmetricEncryption;
import io.github.hossensyedriadh.cryptography.model.AsymmetricKeyGenerator;
import io.github.hossensyedriadh.cryptography.service.encryption.AsymmetricEncryptionService;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.KeyPair;
import java.util.Base64;

@Controller
@RequestMapping("/asymmetric-encryption")
public class AsymmetricEncryptionController {
    private final ObjectFactory<AsymmetricEncryptionService> asymmetricEncryptionServiceObjectFactory;

    @Autowired
    public AsymmetricEncryptionController(ObjectFactory<AsymmetricEncryptionService> asymmetricEncryptionServiceObjectFactory) {
        this.asymmetricEncryptionServiceObjectFactory = asymmetricEncryptionServiceObjectFactory;
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        webDataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
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

    @GetMapping("/encryption/")
    public ModelAndView encryptionForm(Model model) {
        AsymmetricEncryption encryption = new AsymmetricEncryption();
        model.addAttribute("Encryption", encryption);

        return new ModelAndView("asymmetric-encryption");
    }

    @PostMapping("/encrypt/")
    public ModelAndView encrypt(@ModelAttribute AsymmetricEncryption asymmetricEncryption, Model model) {
        AsymmetricEncryption encryption;
        if (asymmetricEncryption.getMethod().equals(Method.GENERATE_NEW_KEY)) {
            KeyPair keyPair = this.asymmetricEncryptionServiceObjectFactory.getObject()
                    .generateKeyPair(asymmetricEncryption.getKeySize());
            encryption = new AsymmetricEncryption(keyPair);
            encryption.setMethod(asymmetricEncryption.getMethod());
            encryption.setPlainText(asymmetricEncryption.getPlainText());
            encryption.setEncryptedText(this.asymmetricEncryptionServiceObjectFactory.getObject()
                    .encryptMessage(encryption.getPlainText(), Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded())));
            encryption.setKeySize(asymmetricEncryption.getKeySize());
        } else {
            encryption = new AsymmetricEncryption();
            encryption.setMethod(asymmetricEncryption.getMethod());
            encryption.setPlainText(asymmetricEncryption.getPlainText());
            encryption.setExistingKey(asymmetricEncryption.getExistingKey());
            encryption.setEncryptedText(this.asymmetricEncryptionServiceObjectFactory.getObject()
                    .encryptMessage(asymmetricEncryption.getPlainText(), asymmetricEncryption.getExistingKey()));
        }
        model.addAttribute("Encryption", encryption);
        return new ModelAndView("asymmetric-encryption");
    }

    @GetMapping("/decryption/")
    public ModelAndView decryptionForm(Model model) {
        return new ModelAndView("asymmetric-decryption");
    }
}
