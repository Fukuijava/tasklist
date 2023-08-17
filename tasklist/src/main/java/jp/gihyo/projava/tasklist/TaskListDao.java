package jp.gihyo.projava.tasklist;

import jp.gihyo.projava.tasklist.HomeController.TaskItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import java.util.*;
import java.lang.*;

//import static org.springframework.http.HttpHeaders.FROM;

@Service
public class TaskListDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    TaskListDao(JdbcTemplate jdbcTemplate) {//フィールド地の初期化
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(TaskItem taskItem) {//タスクリストテーブルに登録するメソッド
        SqlParameterSource param = new BeanPropertySqlParameterSource(taskItem);
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate).withTableName("tasklist");
        insert.execute(param);
    }

    public List<TaskItem> findAll() {//現在登録されているすべての情報を取得してListオブジェクトに格納して返す
        String query = "SELECT * FROM tasklist";

        List<Map<String,Object>> result =jdbcTemplate.queryForList(query);
        List<TaskItem> taskItems = result.stream()
                .map((Map<String , Object> row) -> new TaskItem(
                        row.get("id").toString(),
                        row.get("task").toString(),
                        row.get("deadline").toString(),
                        (Boolean)row.get("done")))
                .toList();

        return taskItems;
    }

    public int delete(String id){
        int number = jdbcTemplate.update("DELETE FROM tasklist WHERE id = ?",id);
        return number;
    }

    public int update(TaskItem taskItem){
        int number = jdbcTemplate.update(
                "UPDATE tasklist SET task = ?, deadline = ?, done = ? WHERE id = ?",
                taskItem.task(),
                taskItem.deadline(),
                taskItem.done(),
                taskItem.id());
        return number;
    }
}
