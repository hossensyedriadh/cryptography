package io.github.hossensyedriadh.cryptography.controller;

import io.github.hossensyedriadh.cryptography.enumerator.Method;
import io.github.hossensyedriadh.cryptography.model.SymmetricDecryption;
import io.github.hossensyedriadh.cryptography.model.SymmetricEncryption;
import io.github.hossensyedriadh.cryptography.model.SymmetricKeyGenerator;
import io.github.hossensyedriadh.cryptography.service.encryption.SymmetricEncryptionService;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Base64;

@Controller
@RequestMapping("/symmetric-encryption")
public class SymmetricEncryptionController {
    private final ObjectFactory<SymmetricEncryptionService> symmetricEncryptionServiceObjectFactory;

    @Autowired
    public SymmetricEncryptionController(ObjectFactory<SymmetricEncryptionService> symmetricEncryptionServiceObjectFactory) {
        this.symmetricEncryptionServiceObjectFactory = symmetricEncryptionServiceObjectFactory;
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        webDataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/key-generator/")
    public ModelAndView keyGeneratorForm(Model model) {
        model.addAttribute("Generator", new SymmetricKeyGenerator());
        return new ModelAndView("symmetric-key-generator");
    }

    @PostMapping("/generate-key/")
    public ModelAndView generateKey(@ModelAttribute SymmetricKeyGenerator keyGenerator, Model model) {
        model.addAttribute("Generator", new SymmetricKeyGenerator(keyGenerator.getAlgorithm(), this
                .symmetricEncryptionServiceObjectFactory.getObject().generateKey(keyGenerator.getAlgorithm())));

        return new ModelAndView("symmetric-key-generator");
    }

    @GetMapping("/encryption/")
    public ModelAndView encryption(Model model) {
        model.addAttribute("Encryption", new SymmetricEncryption());
        return new ModelAndView("symmetric-encryption");
    }

    @PostMapping("/encrypt/")
    public ModelAndView encrypt(@ModelAttribute SymmetricEncryption symmetricEncryption, Model model) {
        SymmetricEncryption encryption = new SymmetricEncryption();
        String encrypted;

        encryption.setPlainTextMessage(symmetricEncryption.getPlainTextMessage());
        if (symmetricEncryption.getMethod().equals(Method.USE_EXISTING_KEY)) {
            encrypted = this.symmetricEncryptionServiceObjectFactory.getObject()
                    .encryptMessage(symmetricEncryption.getPlainTextMessage(), symmetricEncryption.getKey());
            encryption.setKey(symmetricEncryption.getKey());
        } else {
            String key = Base64.getEncoder().encodeToString(this.symmetricEncryptionServiceObjectFactory.getObject()
                    .generateKey(symmetricEncryption.getAlgorithm()).getEncoded());

            encrypted = this.symmetricEncryptionServiceObjectFactory.getObject()
                    .encryptMessage(symmetricEncryption.getPlainTextMessage(), key);
            encryption.setKey(key);
        }

        encryption.setEncryptedMessage(encrypted);

        model.addAttribute("Encryption", encryption);

        return new ModelAndView("symmetric-encryption");
    }

    @GetMapping("/decryption/")
    public ModelAndView decryption(Model model) {
        model.addAttribute("Decryption", new SymmetricDecryption());
        return new ModelAndView("symmetric-decryption");
    }

    @PostMapping("/decrypt/")
    public ModelAndView decrypt(@ModelAttribute SymmetricDecryption symmetricDecryption, Model model) {
        SymmetricDecryption decryption = new SymmetricDecryption();
        decryption.setEncryptedText(symmetricDecryption.getEncryptedText());
        decryption.setKey(symmetricDecryption.getKey());
        decryption.setDecryptedText(this.symmetricEncryptionServiceObjectFactory.getObject()
                .decryptMessage(decryption.getEncryptedText(), decryption.getKey()));

        model.addAttribute("Decryption", decryption);

        return new ModelAndView("symmetric-decryption");
    }
}
