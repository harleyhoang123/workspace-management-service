package vn.edu.fpt.workspace.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 04/12/2022 - 11:36
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@RequiredArgsConstructor
@Getter
public enum SprintStatusEnum {

    NOT_START("NOT_START"),
    STARTING("STARTING"),
    COMPLETED("COMPLETED");

    private final String status;
}
