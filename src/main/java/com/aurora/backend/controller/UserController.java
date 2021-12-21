package com.aurora.backend.controller;

import com.aurora.backend.entity.User;
import com.aurora.backend.services.BaseEntityService;
import com.aurora.backend.services.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
@RequestMapping(value = "/user")
public class UserController extends BaseController<User, Long> {

    @Autowired
    private UserService userService;

    @Override
    protected BaseEntityService<User, Long> getService() {
        return userService;
    }


}
