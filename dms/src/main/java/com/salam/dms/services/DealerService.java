package com.salam.dms.services;

import com.salam.dms.db.entity.Dealer;
import com.salam.dms.model.request.DealerLogin;
import com.salam.dms.repos.DealerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DealerService {

    @Autowired
    DealerRepository dealerRepository;


    public Optional<Dealer> checkLogin(DealerLogin login) {
        return dealerRepository.findActiveDealerByPhone(login.getPhone());
    }

}
