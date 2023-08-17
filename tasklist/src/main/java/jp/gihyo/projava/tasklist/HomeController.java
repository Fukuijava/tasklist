package jp.gihyo.projava.tasklist;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class HomeController {
    private final TaskListDao dao;
    @Autowired
    HomeController(TaskListDao dao){
        this.dao = dao;
    }
//    @RequestMapping(value="/hello")
//    @ResponseBody
//    String hello(){
//        return """
//                <html>
//                    <head><title><Hello</title></head>
//                    <body>
//                        <h1>Hello</h1>
//                        It works!<br>
//                        現在時刻は%sです。
//                    </body>
//                </html>
//                """.formatted(LocalDateTime.now());
//    }


    @RequestMapping(value="/hello")
    String hello(Model model){
        model.addAttribute("time",LocalDateTime.now());
        return "hello";
    }


    record TaskItem(String id, String task, String deadline, boolean done){}
    private List<TaskItem> taskItems = new ArrayList<>();

    @GetMapping("/list")
    String listItems(Model model){
        List<TaskItem> taskItems = dao.findAll();
        model.addAttribute("taskList", taskItems);
        return "home";
    }

    @GetMapping("/add")
    String addItem(@RequestParam("task") String task,
                   @RequestParam("deadline") String deadline){
        String id = UUID.randomUUID().toString().substring(0,8);
        TaskItem item = new TaskItem(id, task, deadline, false);
       dao.add(item);


        return "redirect:/list";
    }

    @GetMapping("/delete")
    String deleteItem(@RequestParam("id") String id){
        dao.delete(id);
        return "redirect:/list";
    }

    @GetMapping("/update")
    String updateItem(@RequestParam("id") String id,
                      @RequestParam("task") String task,
                      @RequestParam("deadline") String deadline,
                      @RequestParam("done") boolean done){
        TaskItem taskItem = new TaskItem(id, task, deadline, done);
        dao.update(taskItem);
        return "redirect:/list";
    }
}
