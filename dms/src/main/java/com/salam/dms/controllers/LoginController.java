package com.salam.dms.controllers;

import com.google.i18n.phonenumbers.NumberParseException;
import com.salam.dms.model.request.DealerEmailLogin;
import com.salam.dms.model.request.DealerLogin;
import com.salam.dms.repos.UserRepository;
import com.salam.dms.services.DealerService;
import com.salam.dms.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class LoginController {

    final DealerService dealerService;
    final UserService userService;

  /* @PostMapping("/login/1")
    public Object checkLogin( @RequestBody  @Valid DealerLogin login) {
        dealerService.performLoginStep1(login);
        return Map.of("message", "success");
    }*/

    @PostMapping("/login/1")
    public Object checkEmailLogin(@RequestBody  @Valid DealerEmailLogin login) throws NumberParseException {
        userService.LoginCheck(login);
        return Map.of("message", "success");
    }

}
