package app.domains.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.sql.DataSource;
import java.sql.Connection;

@Controller
public class TestController {

    @Autowired
    private DataSource dataSource;

    @RequestMapping("/dbtest")
    public String dbTest(Model model) {
        String message;
        try (Connection conn = dataSource.getConnection()) {
            message = "DB 연결 성공!<br/>"
                    + "URL = " + conn.getMetaData().getURL() + "<br/>"
                    + "User = " + conn.getMetaData().getUserName();
        } catch (Exception e) {
            message = "DB 연결 실패: " + e.getMessage();
        }

        model.addAttribute("dbMessage", message);
        return "dbtest"; 
    }
}
