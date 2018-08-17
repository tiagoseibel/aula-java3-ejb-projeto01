package web;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ControleAcessoFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, 
            ServletResponse response, 
            FilterChain chain) throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        
        String uri = req.getRequestURI();
        String auth = req.getHeader("Authorization");
        
        System.out.println("Passou por: " + uri);
        
        if ( auth == null && !uri.contains("html") ) {
            System.out.println("Acesso negado!");
            resp.sendError(403);
        } else {
            System.out.println("Acesso garantido!");
            chain.doFilter(request, response);
        }
    }
    
}
