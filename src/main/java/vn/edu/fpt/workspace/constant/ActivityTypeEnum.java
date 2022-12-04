package vn.edu.fpt.workspace.constant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 05/11/2022 - 22:25
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Getter
@RequiredArgsConstructor
public enum ActivityTypeEnum {

    COMMENT("COMMENT"),
    HISTORY("HISTORY");

    private final String type;

}
