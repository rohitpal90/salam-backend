package com.salam.dms.config.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.Optional;
import java.util.function.Predicate;

import static com.salam.dms.config.exception.AppErrors.DMS_APP_ERROR;

@Getter
public class AppError extends RuntimeException {
    private final AppErrors type;
    private final Object data;
    private final Object[] messageParams;

    @Builder
    private AppError(String message, Object data, AppErrors type, Object... args) {
        super(resolveMessage(message, args, type));
        this.type = type;
        this.data = data;
        this.messageParams = args;
    }

    public static AppError ofType(AppErrors error) {
        return AppError.builder().type(error).build();
    }

    public static AppError create(AppErrors type, Object data, Object... args) {
        return AppError.builder().data(data).args(args).type(type).build();
    }

    public static AppError create(AppErrors type, Object... args) {
        return AppError.builder().args(args).type(type).build();
    }

    public static AppError create(AppErrors type) {
        return AppError.builder().type(type).build();
    }

    public static AppError create(String message, AppErrors type) {
        return AppError.builder().type(type).message(message).build();
    }

    private static String resolveMessage(String defaultMessage, Object[] messageParams, AppErrors type) {
        return Optional.ofNullable(messageParams)
                .map(p -> new MessageFormat(type.message()).format(p))
                .orElseGet(() -> Optional.ofNullable(type.message())
                        .filter(Predicate.not(StringUtils::isEmpty))
                        .orElse(defaultMessage)
                );
    }
}
