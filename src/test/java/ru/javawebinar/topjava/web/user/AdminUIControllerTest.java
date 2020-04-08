package ru.javawebinar.topjava.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.AbstractControllerTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

class AdminUIControllerTest extends AbstractControllerTest {
    public static final String BASE_URL = AdminUIController.AJAX_URL + "/";

    @Test
    void enableDisable() throws Exception {
        String urlTemplate = BASE_URL + "enable?id=" + USER_ID + "&enabled=%s";

        perform(MockMvcRequestBuilders.post(String.format(urlTemplate, Boolean.FALSE)))
                .andDo(print())
                .andExpect(status().isNoContent());

        User disabledUser = userService.get(USER_ID);
        assertFalse(disabledUser.isEnabled());

        perform(MockMvcRequestBuilders.post(String.format(urlTemplate, Boolean.TRUE)))
                .andDo(print())
                .andExpect(status().isNoContent());

        User enabledUser = userService.get(USER_ID);
        assertTrue(enabledUser.isEnabled());
    }
}