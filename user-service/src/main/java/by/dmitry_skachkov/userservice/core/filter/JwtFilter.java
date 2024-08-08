package by.dmitry_skachkov.userservice.core.filter;

import by.dmitry_skachkov.userservice.core.utils.JwtTokenHandler;
import by.dmitry_skachkov.userservice.core.utils.UserAuth;
import by.dmitryskachkov.exception.exceptions.token.TokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenHandler jwtTokenHandler;

    public JwtFilter(JwtTokenHandler jwtTokenHandler) {
        this.jwtTokenHandler = jwtTokenHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);


        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        final String token = header.split(" ")[1].trim();
        try {
            if (!jwtTokenHandler.validate(token)) {
                chain.doFilter(request, response);
                return;
            }

            UserAuth userAuth = jwtTokenHandler.getUser(token);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userAuth, null, null
            );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } catch (TokenException e) {
            handleVerificationError(response, e);
        }
    }

    /**
     * This method is necessary in the filter to handle and respond to verification errors.
     * It is called before the GlobalHandlerController can be invoked, so it directly writes
     * the error response to the HttpServletResponse.
     *
     * @param response used to write the response
     * @param e        contains information about the Exception and  http status
     * @throws IOException
     **/
    private void handleVerificationError(HttpServletResponse response, TokenException e) throws IOException {
        response.setStatus(e.getHttpStatusCode().value());
        response.setContentType("application/json");
        String jsonError = "{\"error\": \"" + e.getMessage() + "\"}";
        response.getWriter().write(jsonError);
        response.getWriter().flush();
    }

}
