package vn.edu.fpt.workspace.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 04/12/2022 - 08:15
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@RequiredArgsConstructor
@Getter
public enum WorkflowStatusEnum {

    TO_DO("TO_DO"),
    IN_PROGRESS("IN_PROGRESS"),
    DONE("DONE");

    private final String status;
}
