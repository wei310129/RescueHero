package tw.com.aidenmade.rescuehero.domain.base.definition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tw.com.aidenmade.rescuehero.domain.base.enums.Role;
import tw.com.aidenmade.rescuehero.domain.base.enums.RoleType;

@Getter
@AllArgsConstructor
public enum RoleDefinition {
    ACCOUNT_ROLE_ADMIN(RoleType.ACCOUNT, Role.ROLE_ADMIN),
    ACCOUNT_ROLE_ORGAN(RoleType.ACCOUNT, Role.ROLE_ORGAN),
    ACCOUNT_ROLE_USER(RoleType.ACCOUNT, Role.ROLE_USER),
    ;

    private final RoleType type;
    private final Role role;

    public String getTypeName() {
        return this.type.name();
    }

    public String getRoleName() {
        return this.role.name();
    }
}
