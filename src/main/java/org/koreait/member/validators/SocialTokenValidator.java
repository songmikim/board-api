package org.koreait.member.validators;

import lombok.RequiredArgsConstructor;
import org.koreait.member.controllers.RequestSocialToken;
import org.koreait.member.repositories.MemberRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SocialTokenValidator implements Validator {

    private final MemberRepository repository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestSocialToken.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (errors.hasErrors()) {
            return;
        }

        RequestSocialToken form = (RequestSocialToken) target;
        boolean exists = repository.findBySocialChannelAndSocialToken(form.getSocialChannel(), form.getSocialToken()).isPresent();
        if (!exists) {
            errors.reject("NotFound.member");
        }
    }
}
