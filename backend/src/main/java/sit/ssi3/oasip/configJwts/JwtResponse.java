package sit.ssi3.oasip.configJwts;


import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String refreshToken;
    private String type = "Bearer";
    private String username;
    private List<String> roles;

    public JwtResponse(String accessToken, String refreshToken, String username,  List<String> roles) {
        this.token = accessToken;
        this.refreshToken = refreshToken;
        this.username = username;
        this.roles = roles;
    }

    // getters and setters
}