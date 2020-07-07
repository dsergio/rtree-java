package co.dsergio.rtree_web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
	
	Client client;
	
	public HomeController() {
		client = new Client();
	}
	
	@RequestMapping("/app")
	public ModelAndView treeList(HttpServletRequest request, HttpServletResponse response) {
		
		JSONArray arr = client.getArray("/rtree/get");
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("treeList");
		
		for (int i = 0; i < arr.size(); i++) {
			JSONObject obj = (JSONObject) arr.get(i);
		}
		mv.addObject("result", arr);
		
		return mv;
	}
	
	@RequestMapping("/get/{treeName}")
	public ModelAndView get(@PathVariable String treeName) {
		
		JSONObject obj = client.getObject("/rtree/get/" + treeName);
		
		int N = 0;
		
		if (obj.get("N") != null) {
			N = Integer.parseInt(obj.get("N").toString());
		}
		
		ModelAndView mv = new ModelAndView();
		
		if (N == 3) {
			mv.setViewName("treeDetail3D");
		} else if (N == 2) {
			mv.setViewName("treeDetail2D");
		} else {
			mv.setViewName("treeDetailND");
		}
		
		
		mv.addObject("treeName", treeName);
		mv.addObject("points", obj.get("points"));
		mv.addObject("rectangles", obj.get("rectangles"));
		
		return mv;
	}

}
