package com.salam.dms.config.auth;

import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.spring.autoconfigure.TotpProperties;
import dev.samstevens.totp.time.TimeProvider;
import eu.fraho.spring.securityJwt.base.service.TotpService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang3.StringUtils.leftPad;


@Component
@AllArgsConstructor
public class OtpService implements TotpService {

    final SecretGenerator secretGenerator;
    final CodeVerifier codeVerifier;
    final CodeGenerator codeGenerator;
    final TimeProvider timeProvider;
    final TotpProperties props;


    @SneakyThrows
    public String generateCode(String secret) {
        return codeGenerator.generate(secret, Math.floorDiv(timeProvider.getTime(), getExpiration()));
    }

    @Override
    public boolean verifyCode(String secret, int code) {
        var fmtCode = leftPad(String.valueOf(code), getOtpLength(), '0');
        return codeVerifier.isValidCode(secret, fmtCode);
    }

    @Override
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
