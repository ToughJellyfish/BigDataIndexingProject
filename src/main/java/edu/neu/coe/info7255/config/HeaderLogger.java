package edu.neu.coe.info7255.config;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class HeaderLogger implements Filter {

    @Override
    public void init (FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter (ServletRequest request, ServletResponse response,
                          FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse rep = (HttpServletResponse) response;
        System.out.println("----- Request ---------");
        Collections.list(req.getHeaderNames())
                .forEach(n -> System.out.println(n + ": " + req.getHeader(n)));

        chain.doFilter(request, response);

        System.out.println("----- response ---------");
        rep.getHeaderNames()
                .forEach(n -> System.out.println(n + ": " + rep.getHeader(n)));

        System.out.println("response status: " + rep.getStatus());
    }

    @Override
    public void destroy () {
    }
}
