package com.ssp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

@Controller
public class VerifyCodeController {

    @GetMapping("/verifyCodeController")
    public void verifyCode(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BufferedImage bi = new BufferedImage(80, 40, BufferedImage.TYPE_INT_RGB);
        Graphics g = bi.getGraphics();
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, 80, 40);

        g2d.setColor(Color.BLUE);
        String codes = "23456789abcdefghijkmnpqrstuvwxyzABCDEFGHIJKLMNPQRSTUVWXYZ";

        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < 4; i++) {
            String str = codes.charAt(random.nextInt(codes.length()))+"";
            sb.append(str);
        }
        g2d.drawString(sb.toString(), 30, 20);
        req.getSession().setAttribute("verifycode", sb.toString());
        ImageIO.write(bi, "JPEG", resp.getOutputStream());
    }
}
