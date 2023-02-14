package com.salam.dms.controllers;

import com.salam.dms.config.exception.AppError;
import com.salam.dms.model.request.DealerLogin;
import com.salam.dms.services.DealerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.salam.dms.config.exception.AppErrors.USER_NOT_FOUND;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    DealerService dealerService;

    @PostMapping("/check")
    public Object checkLogin(@RequestBody DealerLogin login) {
        var dealerOpt = dealerService.checkLogin(login);
        dealerOpt.ifPresent(dealer -> {
            // send otp request
        });

        return dealerOpt.map(dealer -> Map.of("message", "success"))
                .orElseThrow(() -> AppError.create(USER_NOT_FOUND));
    }

}
