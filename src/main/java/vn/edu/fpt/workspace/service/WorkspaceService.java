package vn.edu.fpt.workspace.service;

import vn.edu.fpt.workspace.dto.event.ModifyMembersToWorkspaceEvent;
import vn.edu.fpt.workspace.dto.event.CreateWorkspaceEvent;
import vn.edu.fpt.workspace.dto.response.workspace._CreateWorkspaceResponse;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 03/12/2022 - 22:13
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
public interface WorkspaceService {
    _CreateWorkspaceResponse createWorkspace(CreateWorkspaceEvent event);

    void modifyMembersToWorkspace(ModifyMembersToWorkspaceEvent event);
}
