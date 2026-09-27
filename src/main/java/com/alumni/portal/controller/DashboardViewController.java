package com.alumni.portal.controller;

import com.alumni.portal.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardViewController {

    private final AlumniService alumniService;
    private final CampaignService campaignService;
    private final DonationService donationService;
    private final ReportService reportService;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signin")
    public String signin() {
        return "redirect:/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("alumniList", alumniService.getAllAlumni());
        model.addAttribute("campaigns", campaignService.getAllCampaigns());
        model.addAttribute("recentDonations", donationService.getAllDonations());
        model.addAttribute("balanceSheet", reportService.getBalanceSheet());
        model.addAttribute("profitLoss", reportService.getProfitLoss());
        model.addAttribute("budgetReport", reportService.getBudgetReport());
        return "dashboard";
    }
}
