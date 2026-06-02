package filter;

import entity.Users;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AdminAuthorizationFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        Object sessionUser = httpRequest.getSession(false) == null
                ? null
                : httpRequest.getSession(false).getAttribute("user");

        if (sessionUser instanceof Users user && user.isAdmin()) {
            chain.doFilter(request, response);
            return;
        }

        httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.xhtml");
    }

    @Override
    public void destroy() {
    }
}
