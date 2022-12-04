package net.uselesscode.selenidepoi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/general")
public class GeneralController {

	@RequestMapping("/")
	public String index(Model model) {
		// HTMLに埋め込むパラメータを設定
		model.addAttribute("message", "こんにちは");

		// レンダリングさせたいHTMLファイルのパスを指定
		return "general/mypage";
	}
}
