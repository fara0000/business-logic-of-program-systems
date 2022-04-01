package backend.entities;

import com.fasterxml.jackson.annotation.JsonCreator;

import static java.lang.String.format;

public enum Role {
    USER,
    ADMIN;

    @JsonCreator
    public static Role forValue(String value) {
        for (Role role : values()) {
            if (role.toString().equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException(format("No role with text %s found.", value));
    }

}
