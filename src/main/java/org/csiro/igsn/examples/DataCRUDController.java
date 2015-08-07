package org.csiro.igsn.examples;


import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class DataCRUDController {

    
    @RequestMapping("test2.do")
    public void test2(
            @RequestParam(required = false, value ="batchid") String batchid,          
            HttpServletResponse response)throws Exception {

        response.setContentType("text/html");

        
        OutputStream outputStream = response.getOutputStream();      
        IOUtils.write("<!DOCTYPE html><html><head><title>IGSN</title></head><body><h1>This is a example</h1></body></html>", outputStream);

       
        outputStream.close();
    }

  
}
