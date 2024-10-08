package by.dmitry_skachkov.userservice.core.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserAuth implements GrantedAuthority {

    private String UUID;

    @Override
    public String getAuthority() {
        return null; //по ТЗ нет ролей
    }

}
