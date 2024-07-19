package com.kitcat.likelion.controller;

import com.kitcat.likelion.requestDTO.LoginDTO;
import com.kitcat.likelion.security.custom.CustomUserDetails;
import com.kitcat.likelion.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Tag(name = "User API", description = "user 도메인 관련 API")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "회원 로그인 기능", description = "로그인에 사용되는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "로그인 실패", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<String> login(@RequestBody LoginDTO dto) {
        String token = userService.login(dto);

        if(token.equals("user not found") || token.equals("password error")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(token);
        }

        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    @GetMapping("/test")
    @PreAuthorize("isAuthenticated()")
    public Long test(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return userDetails.getUserId();
    }
}
