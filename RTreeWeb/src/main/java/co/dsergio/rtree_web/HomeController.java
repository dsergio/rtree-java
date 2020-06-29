package co.dsergio.rtree_web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


public class HomeController {
	
	@RequestMapping("/treeList")
	public ModelAndView add(HttpServletRequest request, HttpServletResponse response) {
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("treeList");
		
		return mv;
	}

}
