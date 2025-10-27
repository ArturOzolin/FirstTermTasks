package com.mipt.arturozolin;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@AllArgsConstructor
public class TestUser {
  @NotNull(message = "Имя не может быть null")
  @Size(min = 2, max = 50, message = "Имя должно быть от 2 до 50 символов")
  private String name;

  @Email(message = "Некорректный формат email")
  @NotNull(message = "Email не может быть null")
  private String email;

  @Range(min = 0, max = 150, message = "Возраст должен быть от 0 до 150")
  private Integer age;

  @Size(min = 6, max = 20, message = "Пароль должен быть от 6 до 20 символов")
  private String password;
  public TestUser() {

  }
}
