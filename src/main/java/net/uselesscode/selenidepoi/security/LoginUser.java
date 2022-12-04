package net.uselesscode.selenidepoi.security;

import java.util.List;

public record LoginUser(Integer id, String name, String mail, String password, List<String> roles) {

}
