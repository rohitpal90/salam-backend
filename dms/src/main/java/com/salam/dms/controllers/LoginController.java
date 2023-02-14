package com.salam.dms.controllers;

import com.salam.dms.model.request.DealerLogin;
import com.salam.dms.services.DealerService;
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

    @PostMapping("/login/1")
    public Object checkLogin(@RequestBody DealerLogin login) {
        dealerService.performLoginStep1(login);
        return Map.of("message", "success");
    }

}
