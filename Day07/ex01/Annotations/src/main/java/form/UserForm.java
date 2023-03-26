package form;

import annotation.HtmlForm;
import annotation.HtmlInput;

@HtmlForm(fileName = "qwe123.html", action = "/users", method = "post")
public class UserForm {
    @HtmlInput(type = "text", name = "smth first", placeholder = "Enter First Name")
    private String firstName;

    @HtmlInput(type = "text", name = "smth second", placeholder = "Enter Last Name")
    private String lastName;

    @HtmlInput(type = "password", name = "password", placeholder = "Enter Password")
    private String password;
}
