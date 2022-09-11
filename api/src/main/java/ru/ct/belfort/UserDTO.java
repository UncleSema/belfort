package ru.ct.belfort;

import java.util.List;

public record UserDTO(long id, String token, String strategy, List<String> figis) {
}
