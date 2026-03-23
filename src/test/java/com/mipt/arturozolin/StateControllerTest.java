package com.mipt.arturozolin;

import com.mipt.arturozolin.controller.FavoritesController;
import com.mipt.arturozolin.controller.PreferencesController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.servlet.http.Cookie;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({FavoritesController.class, PreferencesController.class})
class StateControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void shouldAddTaskIdToSessionFavorites() throws Exception {
    mockMvc.perform(post("/api/favorites/task-1")).andExpect(status().
            isOk()).andExpect(request().sessionAttribute("favoriteTaskIds", hasItem("task-1")));
  }

  @Test
  void shouldReturnEmptySetWhenNoFavoritesInSession() throws Exception {
    mockMvc.perform(get("/api/favorites")).andExpect(status().isOk()).
            andExpect(jsonPath("$").isArray()).
            andExpect(jsonPath("$.length()").value(0));
  }


  @Test
  void shouldReturnDefaultPreferenceWhenNoCookiePresent() throws Exception {
    mockMvc.perform(get("/api/preferences/view")).andExpect(status().isOk()).
            andExpect(jsonPath("$.mode").value("detailed"));
  }

  @Test
  void shouldReadPreferenceFromProvidedCookie() throws Exception {
    Cookie viewCookie = new Cookie("viewPreference", "compact");

    mockMvc.perform(get("/api/preferences/view").cookie(viewCookie)).
            andExpect(status().isOk()).andExpect(jsonPath("$.mode").value("compact"));
  }

  @Test
  void shouldSetCookieInResponse() throws Exception {
    mockMvc.perform(post("/api/preferences/view").param("mode", "grid")).
            andExpect(status().isOk()).andExpect(header().exists("Set-Cookie")).andExpect(cookie().
                    value("viewPreference", "grid")).andExpect(cookie().
                    httpOnly("viewPreference", true));
  }
}
