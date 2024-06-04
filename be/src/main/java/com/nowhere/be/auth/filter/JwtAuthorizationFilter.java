package com.nowhere.be.auth.filter;

import com.nowhere.be.auth.model.DetailsUser;
import com.nowhere.be.common.AuthConstants;
import com.nowhere.be.common.utils.TokenUtils;
import com.nowhere.be.user.model.dto.LoginUserDTO;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        List<String> roleLessList = Arrays.asList("/signup");

        if (roleLessList.contains(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        String header = request.getHeader(AuthConstants.AUTH_HEADER);

        if (header != null && !header.equalsIgnoreCase("")) {
            String token = TokenUtils.splitHeader(header);

            if (TokenUtils.isValidToken(token)) {
                Claims claims = TokenUtils.getClaimsFromToken(token);

                DetailsUser authentication = new DetailsUser();
                LoginUserDTO user = new LoginUserDTO();
                user.setUserName(claims.get("userName").toString());
            }
        }
    }
}
