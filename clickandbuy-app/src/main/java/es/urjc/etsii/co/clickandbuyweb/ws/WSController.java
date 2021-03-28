package es.urjc.etsii.co.clickandbuyweb.ws;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/ws")
public class WSController {
	
	@GetMapping("")
	public ModelAndView test(Model model) {
		return new ModelAndView("ws-test");
	}
	
}
