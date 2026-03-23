package com.mipt.arturozolin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/favorites")
@Tag(name = "Favorites Management", description = "Управление списком избранных задач пользователя (на основе сессий)")
public class FavoritesController {

  @Operation(summary = "Добавить задачу в избранное", description = "Сохраняет ID задачи в текущей сессии пользователя")
  @ApiResponse(responseCode = "200", description = "Задача добавлена в избранное")
  @PostMapping("/{taskId}")
  public ResponseEntity<Void> addFavorite(@PathVariable String taskId, HttpSession session) {
    Set<String> favorites = (Set<String>) session.getAttribute("favoriteTaskIds");
    if (favorites == null) favorites = new HashSet<>();
    favorites.add(taskId);
    session.setAttribute("favoriteTaskIds", favorites);
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "Удалить задачу из избранного", description = "Удаляет ID задачи из текущей сессии пользователя")
  @ApiResponse(responseCode = "204", description = "Задача удалена из избранного")
  @DeleteMapping("/{taskId}")
  public ResponseEntity<Void> removeFavorite(@PathVariable String taskId, HttpSession session) {
    Set<String> favorites = (Set<String>) session.getAttribute("favoriteTaskIds");
    if (favorites != null) {
      favorites.remove(taskId);
      session.setAttribute("favoriteTaskIds", favorites);
    }
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Получить список избранного", description = "Возвращает набор ID задач, сохраненных в сессии")
  @ApiResponse(responseCode = "200", description = "Список избранного успешно получен")
  @GetMapping
  public ResponseEntity<Set<String>> getFavorites(HttpSession session) {
    Set<String> favorites = (Set<String>) session.getAttribute("favoriteTaskIds");
    return ResponseEntity.ok(favorites != null ? favorites : new HashSet<>());
  }
}