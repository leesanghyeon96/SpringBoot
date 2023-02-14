package com.mysite.sbb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class indexController {
	
	@GetMapping("/")	// http://localhost:9898 = /
	public String index() {
		
		return "redirect:/question/list";
	}
	// 사용자가 localhost:9898/라고 요청하면 index라고 리턴 ->index.html열림
	
}
