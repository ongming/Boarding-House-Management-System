package com.example.house.config;

import com.example.house.controller.admin.AdminController;
import com.example.house.controller.staff.StaffFeatureController;
import com.example.house.controller.staff.home.StaffHomeCatalog;
import com.example.house.repository.impl.JpaAdminDataStore;
import com.example.house.repository.impl.JpaRoomRepository;
import com.example.house.repository.impl.JpaStaffDataStore;
import com.example.house.repository.RoomRepository;
import com.example.house.service.admin.AdminDataStore;
import com.example.house.service.admin.AdminService;
import com.example.house.service.impl.admin.AdminServiceImpl;
import com.example.house.service.impl.staff.StaffServiceImpl;
import com.example.house.service.staff.StaffDataStore;
import com.example.house.service.staff.StaffService;
import com.example.house.view.admin.AdminDashboardView;
import com.example.house.view.admin.AdminContentFactory;
import com.example.house.view.admin.DefaultAdminContentFactory;
import com.example.house.view.staff.StaffDashboardView;
import com.example.house.view.staff.StaffContentFactory;
import com.example.house.view.staff.DefaultStaffContentFactory;

import java.util.function.Consumer;
import java.util.function.Function;

public class ViewComposition {
    public AdminDashboardView buildAdminDashboard(String fullName, Runnable onLogout) {
        AdminDataStore dataStore = new JpaAdminDataStore();
        AdminService service = new AdminServiceImpl(dataStore);
        AdminController controller = new AdminController(service);
        AdminContentFactory contentFactory = new DefaultAdminContentFactory(controller);
        return new AdminDashboardView(fullName, onLogout, contentFactory);
    }

    public StaffDashboardView buildStaffDashboard(String fullName, Runnable onLogout) {
        StaffDataStore dataStore = new JpaStaffDataStore();
        StaffService service = new StaffServiceImpl(dataStore);
        StaffFeatureController controller = new StaffFeatureController(service);
        RoomRepository roomRepository = new JpaRoomRepository();
        StaffHomeCatalog homeCatalog = new StaffHomeCatalog(roomRepository);
        Function<Consumer<String>, StaffContentFactory> provider = onCreateContract ->
                new DefaultStaffContentFactory(controller, homeCatalog, onCreateContract);
        return new StaffDashboardView(fullName, onLogout, provider);
    }
}
