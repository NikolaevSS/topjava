package ru.javawebinar.topjava.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.UserUtil;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.TestUtil.readFromJson;
import static ru.javawebinar.topjava.TestUtil.userHttpBasic;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.web.user.ProfileRestController.REST_URL;

class ProfileRestControllerTest extends AbstractControllerTest {

    @Autowired
    private UserService userService;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(USER));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL)
                .with(userHttpBasic(USER)))
                .andExpect(status().isNoContent());
        USER_MATCHER.assertMatch(userService.getAll(), ADMIN);
    }

    @Test
    void register() throws Exception {
        UserTo newTo = new UserTo(null, "newName", "newemail@ya.ru", "newPassword", 1500);
        User newUser = UserUtil.createNewFromTo(newTo);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isCreated());

        User created = readFromJson(action, User.class);
        int newId = created.getId();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(userService.get(newId), newUser);
    }

    @Test
    void notRegisterEmptyUser() throws Exception {
        String content =
                perform(MockMvcRequestBuilders.post(REST_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(new UserTo())))
                        .andExpect(status().isUnprocessableEntity())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
        assertNotNull(content);
        assertContains(content, NAME_MUST_NOT_BE_BLANK);
        assertContains(content, EMAIL_MUST_NOT_BE_BLANK);
        assertContains(content, PASSWORD_MUST_NOT_BE_BLANK);
    }

    @Test
    void notRegisterWithNotUniqueEmail() throws Exception {
        UserTo newTo = new UserTo();
        newTo.setEmail(USER.getEmail());
        String content =
                perform(MockMvcRequestBuilders.post(REST_URL + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(newTo)))
                        .andExpect(status().isUnprocessableEntity())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
        assertNotNull(content);
        assertContains(content, EMAIL_ALREADY_EXISTS);
    }

    @Test
    void update() throws Exception {
        UserTo updatedTo = new UserTo(null, "newName", "newemail@ya.ru", "newPassword", 1500);
        perform(MockMvcRequestBuilders.put(REST_URL).contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(USER))
                .content(UserTestData.jsonWithPassword(updatedTo, updatedTo.getPassword())))
                .andDo(print())
                .andExpect(status().isNoContent());

        USER_MATCHER.assertMatch(userService.get(USER_ID), UserUtil.updateFromTo(new User(USER), updatedTo));
    }

    @Test
    void notUpdateEmptyUser() throws Exception {
        String content =
                perform(MockMvcRequestBuilders.put(REST_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(userHttpBasic(USER))
                        .content(JsonUtil.writeValue(new UserTo())))
                        .andExpect(status().isUnprocessableEntity())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
        assertNotNull(content);
        assertContains(content, NAME_MUST_NOT_BE_BLANK);
        assertContains(content, EMAIL_MUST_NOT_BE_BLANK);
        assertContains(content, PASSWORD_MUST_NOT_BE_BLANK);
    }
}