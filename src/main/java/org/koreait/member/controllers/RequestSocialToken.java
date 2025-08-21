package org.koreait.member.controllers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.koreait.member.constants.SocialChannel;

@Data
public class RequestSocialToken {
    @NotNull
    private SocialChannel socialChannel;

    @NotBlank
    private String socialToken;
}
