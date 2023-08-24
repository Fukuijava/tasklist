package jp.gihyo.projava.tasklist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class HomeRestController {

    private final TaskListDao dao;

    @Autowired
    HomeRestController(TaskListDao dao){
        this.dao = dao;
    }

    @PostMapping("/rest_add")
    List<HomeController.TaskItem> addItem(@RequestParam("task") String task,
                                          @RequestParam("deadline") String deadLine,
                                          @RequestParam("memo") String memo){
        String id = UUID.randomUUID().toString().substring(0, 8);
        HomeController.TaskItem item = new HomeController.TaskItem(id, task, deadLine, memo, false);
        dao.add(item);
        return dao.findAll();
    }

    @PostMapping("/rest_update")
    List<HomeController.TaskItem> updateItem(@RequestParam("id") String id,
                                             @RequestParam("task") String task,
                                             @RequestParam("deadLine") String deadLine,
                                             @RequestParam("memo") String memo,
                                             @RequestParam("done") Boolean done
                                             ){
        HomeController.TaskItem item = new HomeController.TaskItem(id, task, deadLine, memo, done);
        this.dao.update(item);
        return this.dao.findAll();
    }
}
