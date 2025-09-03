package com.api.ersmapi.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.ersmapi.config.DBConnection;
import com.api.ersmapi.services.menu.MenuService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/menu_service")
@Tag(name = "Menu Service", description = "Menu Service for TerraFinder Application")
public class MenuController {

    MenuService menuService = new MenuService();

    @Autowired
    private DBConnection dbConnection;

    @GetMapping("/get_all_menus")
    public ResponseEntity<?> getAllMenus()  throws Exception {
        menuService.con = dbConnection.getConnection();
        String result = menuService.getAllMenus();
        menuService.con.close();
        System.out.println("get_all_menus: "+result);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/get_user_menus")
    public ResponseEntity<?> getUserMenus(@RequestBody String jsonReq)  throws Exception {
        menuService.con = dbConnection.getConnection();
        String result = menuService.getUserMenus(jsonReq);
        menuService.con.close();
        System.out.println("get_user_menus: "+result);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/save_user_menus")
    public ResponseEntity<?> saveUserMenus(@RequestBody String jsonReq)  throws Exception {
        menuService.con = dbConnection.getConnection();
        String result = menuService.saveUserMenus(jsonReq);
        menuService.con.close();
        return ResponseEntity.ok(result);
    }
    
}
