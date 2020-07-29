package com.stuel.babysitterkata.webformcontrollers;

import com.stuel.babysitterkata.processors.BabySitterWageProcessor;
import com.stuel.babysitterkata.submissionforms.BabySitterWorkSubmissionForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@SessionAttributes
/**
 * Class defines what html pages to load when navigating between the menus and forms.
 */
public class BabySitterWorkFormController {

    // a given WebFormProcessor by spring(created at runtime)
    @Autowired
    BabySitterWageProcessor webFormProcessor;

    /**
     * Defines a html page that will load a java POJO from
     * "some" user input
     * @param model
     *      Spring Model(java type that holds html attributes) object to be method injected
     * @return
     *      the name of the html page to be loaded when
     */
    @GetMapping("/workform")
    public String showForm(Model model) {
        model.addAttribute("babySitterWorkSubmissionForm", new BabySitterWorkSubmissionForm());
        return "work_form";
    }

    /**
     * Defines an html page that will process and display the total amount
     * earned for the shift of a babysitter!
     * @param submissionForm
     *      a BabySitterWorkSubmissionForm object that is received from user input processed
     *      and passed by spring
     * @return
     */
    @PostMapping("/workform")
    public String submitForm(@ModelAttribute("babySitterWorkSubmissionForm")
                             BabySitterWorkSubmissionForm submissionForm) {
        submissionForm = webFormProcessor.processTotalPay(submissionForm);
        return "shift_total";
    }
}
