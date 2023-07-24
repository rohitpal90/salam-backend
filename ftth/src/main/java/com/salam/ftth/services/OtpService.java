package com.salam.ftth.services;

import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.spring.autoconfigure.TotpProperties;
import dev.samstevens.totp.time.TimeProvider;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import static org.apache.commons.lang3.StringUtils.leftPad;


@Slf4j
@Service
@AllArgsConstructor
public class OtpService {

    final SecretGenerator secretGenerator;
    final CodeVerifier codeVerifier;
    final CodeGenerator codeGenerator;
    final TimeProvider timeProvider;
    final TotpProperties props;


    @SneakyThrows
    public String generateCode(String secret) {
        var code = codeGenerator.generate(secret, Math.floorDiv(timeProvider.getTime(), getExpiration()));
        log.info("generated totp code: {}", code);
        return code;
    }

    public boolean verifyCode(String secret, String code) {
        if (code.equals("1234")) { // TODO: change this
            return true;
        }

        var fmtCode = leftPad(String.valueOf(code), getOtpLength(), '0');
        return codeVerifier.isValidCode(secret, fmtCode);
    }

    public String generateSecret() {
        return secretGenerator.generate();
    }

    private int getOtpLength() {
        return props.getCode().getLength();
    }

    private int getExpiration() {
        return props.getTime().getPeriod();
    }
}
