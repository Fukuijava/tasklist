package jp.gihyo.projava.tasklist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.*;
import java.util.List;
import java.util.Map;

@Service
public class TaskListDao {
    private final static String TABLE_NAME = "tasklist";
    private  final JdbcTemplate jdbcTemplate;

    @Autowired
    TaskListDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * レコードを追加し、追加した件数を返す
     *
     * @param item HomeRestController.TaskItem
     * @return 追加件数
     */
    public int add(HomeController.TaskItem item) {
        String url = "jdbc:mysql://localhost:3306/taskdb";
        String user = "root";
        String password = "NIKUnikufy44";

        int count;
        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement preStatement = con.prepareStatement("insert into tasklist(id, task, deadline, memo,done) values(?, ?, ?, ?, ?);")) {
            preStatement.setString(1, item.id());
            preStatement.setString(2, item.task());
            preStatement.setString(3, item.deadline());
            preStatement.setString(4, item.memo());
            preStatement.setBoolean(5, item.done());
            count = preStatement.executeUpdate();
        } catch (SQLException e) {
            count = 0;
            e.printStackTrace();
        }
        return count;
    }

    public <LIst> List<HomeController.TaskItem> findAll(){
        String query = "SELECT * FROM " + TABLE_NAME;
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
        List<HomeController.TaskItem> list = result.stream().map(
                (Map<String, Object> row) -> new HomeController.TaskItem(
                        row.get("id").toString(),
                        row.get("task").toString(),
                        row.get("deadline").toString(),
                        (String)row.get("memo"),
                        (Boolean)row.get("done")
                )).toList();
        return list;
    }

    public int delete(String id){
        int num = jdbcTemplate.update("DELETE FROM " + TABLE_NAME + " WHERE id = ?", id);
        return num;
    }

    public int update(HomeController.TaskItem taskItem){
        int number = jdbcTemplate.update("update tasklist set task=?, deadline=?, memo=?, done=? where id = ?",
                taskItem.task(),
                taskItem.deadline(),
                taskItem.memo(),
                taskItem.done(),
                taskItem.id());
        return number;
    }

    public List<HomeController.TaskItem> search(String  month){
        String  entered_month = "SELECT * FROM tasklist WHERE deadline like '" + month + "%'";
        List<Map<String, Object>> result = this.jdbcTemplate.queryForList(entered_month);
        List<HomeController.TaskItem> list = result.stream().map(
                (Map<String, Object> row) -> new HomeController.TaskItem(
                        row.get("id").toString(),
                        row.get("task").toString(),
                        row.get("deadline").toString(),
                        (String)row.get("memo"),
                        (Boolean)row.get("done")
                )).toList();
        return list;
    }

    public List<HomeController.TaskItem> judgment(Boolean complete){
        String judgment_result = "SELECT * FROM tasklist WHERE done = " + complete;
        List<Map<String, Object>> result = this.jdbcTemplate.queryForList(judgment_result);
        List<HomeController.TaskItem> list = result.stream().map(
                (Map<String, Object> row) -> new HomeController.TaskItem(
                        row.get("id").toString(),
                        row.get("task").toString(),
                        row.get("deadline").toString(),
                        (String)row.get("memo"),
                        (Boolean)row.get("done")
                )).toList();
        return list;
    }
}
