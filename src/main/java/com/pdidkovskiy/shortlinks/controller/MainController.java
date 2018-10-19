package com.pdidkovskiy.shortlinks.controller;

import com.pdidkovskiy.shortlinks.service.LinkService;
import com.pdidkovskiy.shortlinks.service.UserService;
import domain.Link;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private LinkService linkService;

    @ApiOperation(value = "Login")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void login(@RequestParam("login") String login) {
        userService.login(login);
    }

    @ApiOperation(value = "Create short link object")
    @RequestMapping(value = "/link", method = RequestMethod.POST)
    public Link createLink(@RequestParam("url") String url) {
        return linkService.create(url);
    }

    @ApiOperation(value = "Get all short link objects", notes = "Only available for admin")
    @RequestMapping(value = "/link/all-links", method = RequestMethod.GET)
    public Collection<Link> getAllLinks() {
        return linkService.getAllLinks();
    }

    @ApiOperation(value = "Get short link objects for current user")
    @RequestMapping(value = "/link/user-links", method = RequestMethod.GET)
    public Collection<Link> getUserLinks() {
        return linkService.getUserLinks();
    }

    @ApiOperation(value = "Delete short link")
    @RequestMapping(value = "/link", method = RequestMethod.DELETE)
    public void deleteLink(@RequestParam("shortLink") String shortLink,
                           @RequestParam("userLogin") String userLogin) {
         linkService.deleteLink(shortLink, userLogin);
    }

    @ApiOperation(value = "Get original link", notes = "If implement UI, this should redirect to original page")
    @RequestMapping(value = "/link/hit-link", method = RequestMethod.GET)
    public String hitLink(@RequestParam("shortLink") String shortLink) {
        return linkService.hitLink(shortLink);
    }

}
