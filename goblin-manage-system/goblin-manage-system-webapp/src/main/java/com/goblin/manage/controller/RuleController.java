package com.goblin.manage.controller;

import com.goblin.manage.permission.Pass;
import com.goblin.manage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author pijingzhanji
 */
@Pass
@RestController
@RequestMapping( "rules" )
public class RuleController {

    @Autowired
    private UserService userService;

    public static void main ( String[] args ) {



    }


    public void rule () {


    }


}