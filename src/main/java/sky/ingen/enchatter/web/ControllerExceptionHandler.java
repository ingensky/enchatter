package sky.ingen.enchatter.web;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import sky.ingen.enchatter.util.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView handleError(HttpServletRequest request, Exception e) {
        return getModelAndView(request, e, "500");
    }

    @ExceptionHandler({NotFoundException.class, NoHandlerFoundException.class})
    public String handleError404(HttpServletRequest request, Exception e, Model model) {
        model.addAttribute("exception", getMessage(request, e));
        return "error";
    }


    @ResponseStatus(value = HttpStatus.CONFLICT)  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDuplicate(HttpServletRequest request, Exception e, Model model) {
        String msg = ExceptionUtils.getRootCauseMessage(e).split("Detail: ")[1]
                .replace("Ключ", "User with");
        model.addAttribute("exception", msg);
        return "error";
    }

    private ModelAndView getModelAndView(HttpServletRequest request, Exception e, String s) {
        String msg = getMessage(request, e);
        ModelAndView modelAndView = new ModelAndView(s);
        modelAndView.getModelMap().addAttribute("exception", msg);
        return modelAndView;
    }

    private String getMessage(HttpServletRequest request, Exception e) {
        //  https://stackoverflow.com/questions/17747175
        String rootCauseMessage = ExceptionUtils.getRootCauseMessage(e);
        String msg = "Request: " + request.getRequestURL() + " raised " + rootCauseMessage;
        log.error(msg);
        return msg;
    }
}
