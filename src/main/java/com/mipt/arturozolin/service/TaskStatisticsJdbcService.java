package com.mipt.arturozolin.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class TaskStatisticsJdbcService {

    private final JdbcTemplate jdbcTemplate;

    public TaskStatisticsJdbcService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Data
    @AllArgsConstructor
    public static class PriorityStat {
        private String priority;
        private long count;
    }

    public List<PriorityStat> getTasksCountByPriority() {
        String sql = "SELECT priority, COUNT(*) as cnt FROM tasks GROUP BY priority";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new PriorityStat(
                rs.getString("priority"),
                rs.getLong("cnt")
        ));
    }

  /*
  private final TaskRepository primaryRepo;
  private final TaskRepository stubRepo;
  public void compareRepositories() {
    log.info("Primary repo size: {}", primaryRepo.findAll().size());
    log.info("Stub repo size: {}", stubRepo.findAll().size());
  }
  */
}