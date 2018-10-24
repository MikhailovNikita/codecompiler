package tsystems.tchallenge.codecompiler.api.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tsystems.tchallenge.codecompiler.api.dto.ContestInvoice;
import tsystems.tchallenge.codecompiler.api.dto.IdAware;
import tsystems.tchallenge.codecompiler.managers.contest.ContestManager;

@RestController
@RequestMapping("/contests/")
public class ContestController {

    private final ContestManager contestManager;

    @Autowired
    public ContestController(ContestManager contestManager) {
        this.contestManager = contestManager;
    }

    @PostMapping
    public IdAware create(@RequestBody  ContestInvoice invoice) {
        return contestManager.save(invoice);
    }
}
