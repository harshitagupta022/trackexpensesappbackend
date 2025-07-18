package pl.harshita_gupta.trackexpensesappbackend.auth.api;


import pl.harshita_gupta.trackexpensesappbackend.auth.usermodel.User;
import pl.harshita_gupta.trackexpensesappbackend.auth.api.dto.AuthLoginDTO;
import pl.harshita_gupta.trackexpensesappbackend.auth.api.dto.AuthRegisterDTO;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

public interface AuthService {

    void registerUser(@Valid AuthRegisterDTO authRegisterDTO);

    String hashPassword(String password);

    String loginUser(AuthLoginDTO authLoginDTO, HttpServletResponse response);

    User getUserFromToken(String token);

    String createAccessToken(User user);

    Cookie createRefreshTokenCookie(User user);

    String refreshToken(HttpServletRequest request, HttpServletResponse response,
                        String refreshToken, String accessToken);

    void deleteRefreshTokenCookie(HttpServletResponse response);
}
