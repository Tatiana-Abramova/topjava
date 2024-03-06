package ru.javawebinar.topjava.web.user;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.validation.Valid;

import static ru.javawebinar.topjava.util.ValidationUtil.*;

@Controller
@RequestMapping("/profile")
public class ProfileUIController extends AbstractUserController {

    @GetMapping
    public String profile() {
        return "profile";
    }

    @PostMapping
    public String updateProfile(@Valid UserTo userTo, BindingResult result, SessionStatus status) {
        if (result.hasErrors()) {
            return "profile";
        } else {
            try {
                super.update(userTo, SecurityUtil.authUserId());
            } catch (DataIntegrityViolationException e) {
                return handleUniqueEmailError(e, result);
            }
            SecurityUtil.get().setTo(userTo);
            status.setComplete();
            return "redirect:/meals";
        }
    }

    @GetMapping("/register")
    public String register(ModelMap model) {
        model.addAttribute("userTo", new UserTo());
        model.addAttribute("register", true);
        return "profile";
    }

    @PostMapping("/register")
    public String saveRegister(@Valid UserTo userTo, BindingResult result, SessionStatus status, ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("register", true);
            return "profile";
        } else {
            try {
                super.create(userTo);
            } catch (DataIntegrityViolationException e) {
                return handleUniqueEmailError(e, result, model);
            }
            status.setComplete();
            return "redirect:/login?message=app.registered&username=" + userTo.getEmail();
        }
    }

    private String handleUniqueEmailError(DataIntegrityViolationException e, BindingResult result, ModelMap model) {
        String rootMsg = getRootCause(e).getMessage();
        if (rootMsg != null) {
            String lowerCaseMsg = rootMsg.toLowerCase();
            if (lowerCaseMsg.contains(USER_UNIQUE_EMAIL_ERROR)) {
                result.rejectValue("email", CONSTRAINS_I18N_MAP.get(USER_UNIQUE_EMAIL_ERROR), "User with this email already exists");
                if (model != null) {
                    model.addAttribute("register", true);
                }
                return "profile";
            }
        }
        throw e;
    }

    private String handleUniqueEmailError(DataIntegrityViolationException e, BindingResult result) {
        return handleUniqueEmailError(e, result, null);
    }
}