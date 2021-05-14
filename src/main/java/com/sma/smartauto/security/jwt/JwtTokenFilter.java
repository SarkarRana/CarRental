package com.sma.smartauto.security.jwt;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sma.smartauto.exception.ApiErrorCode;
import com.sma.smartauto.exception.InvalidJwtAuthenticationException;
import com.sma.smartauto.model.ExceptionResponse;

public class JwtTokenFilter extends GenericFilterBean {

	private JwtTokenProvider jwtTokenProvider;
    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }
    
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
		String token = jwtTokenProvider.resolveToken((HttpServletRequest) req);
		try {
			if (token != null && jwtTokenProvider.validateToken(token)) {
				Authentication auth = token != null ? jwtTokenProvider.getAuthentication(token) : null;
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
			filterChain.doFilter(req, res);
		} catch (InvalidJwtAuthenticationException e) {
			ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ApiErrorCode.INVALID_API_TOKEN.name(), e.getMessage(), null);
			HttpServletResponse response = (HttpServletResponse)res;
	        response.setStatus(HttpStatus.UNAUTHORIZED.value());
	        response.setContentType("application/json");		       
	        try {
	        	String jsonStr = new ObjectMapper().writeValueAsString(exceptionResponse);
	            response.getOutputStream().write((jsonStr!=null?jsonStr.getBytes():"{}".getBytes()));
	            response.getOutputStream().flush();
//		            message.getInterceptorChain().abort();           
	        } catch (Exception ex) {
	            throw new RuntimeException("Error in throwing InvalidJwtAuthenticationException");
	        }
		} catch (Exception e) {
			logger.error("Exception at JwtTokenFilter: ", e);
			throw e; 
		}
	}

}
