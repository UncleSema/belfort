import java.util.ArrayList;

public record UserDTO(long id, String token, String strategy,
                      ArrayList<String> figis) {
}
