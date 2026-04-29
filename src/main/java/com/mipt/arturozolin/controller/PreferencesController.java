package com.mipt.arturozolin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/preferences")
@Tag(name = "User Preferences", description = "Управление пользовательскими настройками интерфейса (на основе кук)")
public class PreferencesController {

  @Operation(summary = "Получить настройку отображения", description = "Читает значение из куки 'viewPreference'")
  @ApiResponse(responseCode = "200", description = "Настройка успешно получена")
  @GetMapping("/view")
  public ResponseEntity<String> getViewPreference(
          @Parameter(description = "Значение куки режима отображения")
          @CookieValue(value = "viewPreference", defaultValue = "detailed") String viewMode) {
    return ResponseEntity.ok("{\"mode\":\"" + viewMode + "\"}");
  }

  @Operation(summary = "Установить настройку отображения", description = "Записывает выбранный режим в HttpOnly куку")
  @ApiResponse(responseCode = "200", description = "Кука успешно установлена")
  @PostMapping("/view")
  public ResponseEntity<Void> setViewPreference(@RequestParam String mode) {
    ResponseCookie cookie = ResponseCookie.from("viewPreference", mode)
            .httpOnly(true)
            .path("/")
            .maxAge(7 * 24 * 60 * 60)
            .build();
    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
  }
}