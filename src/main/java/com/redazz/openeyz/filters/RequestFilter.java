/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.redazz.openeyz.filters;

import com.redazz.openeyz.beans.JwTokenUtils;
import com.redazz.openeyz.defines.Define;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author kyzer
 */
public class RequestFilter implements Filter {
//    @Autowired
    private final JwTokenUtils jwt = new JwTokenUtils();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        boolean accessBase = req.getRequestURI().equals(Define.ROOT_URL);
        boolean accessDownloadImg = req.getRequestURI().split("\\?")[0].equals(Define.UPLOAD_IMAGE_URL) && req.getMethod().equals("GET");

        if (!(accessBase || accessDownloadImg)) {

            String token = req.getHeader("Authorization").split("Bearer ")[1];
            try {
                jwt.decode(token);

            }
            catch (Exception e) {
                res.sendError(401, e.getMessage());
            }
        }
        chain.doFilter(request, response);
    }
}
