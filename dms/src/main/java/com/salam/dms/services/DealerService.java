package com.salam.dms.services;

import com.salam.dms.config.exception.AppError;
import com.salam.dms.db.entity.Dealer;
import com.salam.dms.model.request.DealerLogin;
import com.salam.dms.repos.DealerRepository;
import eu.fraho.spring.securityJwt.base.service.TotpService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

import static com.salam.dms.config.exception.AppErrors.USER_NOT_FOUND;

@Service
@AllArgsConstructor
public class DealerService {

    final DealerRepository dealerRepository;
    final TotpService totpService;


    public Optional<Dealer> checkLogin(DealerLogin login) {
        return dealerRepository.findActiveDealerByPhone(login.getPhone());
    }

    public void performLoginStep1(DealerLogin login) {
        var dealerOpt = this.checkLogin(login);
        if (dealerOpt.isEmpty()) {
            throw AppError.create(USER_NOT_FOUND);
        }

        var dealer = dealerOpt.get();
        dealer.setTotp(totpService.generateSecret());
        dealer.setTotp("1234");

        dealerRepository.save(dealer);
    }

}
