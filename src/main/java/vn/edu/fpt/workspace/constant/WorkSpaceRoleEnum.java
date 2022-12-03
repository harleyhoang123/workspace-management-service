package vn.edu.fpt.workspace.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 03/12/2022 - 22:20
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@RequiredArgsConstructor
@Getter
public enum WorkSpaceRoleEnum {

    OWNER("OWNER"),
    MANAGER("MANAGER"),
    LEADER("LEADER"),
    MEMBER("MEMBER");

    private final String role;
}
