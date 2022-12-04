package net.uselesscode.selenidepoi.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

	@RequestMapping("/")
	public String index(HttpServletRequest request, Model model) {
		if (request.isUserInRole("GENERAL")) {
			return "redirect:/general/";
		} else if (request.isUserInRole("ADMIN")) {
			return "redirect:/admin/";
		}
		return "redirect:login";
	}

	@RequestMapping("/login")
	public String login(@AuthenticationPrincipal UserDetails userDetails, Model model,
			@RequestParam(value = "error", required = false) String error) {
		if (userDetails != null) {
			return "redirect:/";
		}

		if (error != null) {
			model.addAttribute("messageType", "error");
			model.addAttribute("message", "メールアドレスまたはパスワードに誤りがあります。");
		}
		return "index";
	}

	@RequestMapping("/logout")
	public String logout(@AuthenticationPrincipal UserDetails userDetails, Model model) {
		if (userDetails != null) {
			return "redirect:/";
		}
		model.addAttribute("messageType", "info");
		model.addAttribute("message", "ログアウトしました。");
		return "index";
	}
}
