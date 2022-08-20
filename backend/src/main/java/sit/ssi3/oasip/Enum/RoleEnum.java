package sit.ssi3.oasip.Enum;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum RoleEnum {
    admin,
    lecturer,
    student;

    public static RoleEnum convertToEnum(String role) {
        switch (role.toLowerCase()) {
            case "student":
                return student;
            case "lecturer":
                return lecturer;
            case "admin":
                return admin;
            default:
                return null;
        }
    }

    public static boolean checkEnum(String role) {
        return Arrays.stream(RoleEnum.values()).filter(roleEnum -> roleEnum.name().equalsIgnoreCase(role)).collect(Collectors.toList()).size() > 0;
    }
}
