package com.umpay.hfmng.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.operamasks.data.Ip;
import org.operamasks.data.IpService;
import org.operamasks.model.GridDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.umpay.hfmng.model.DemoInfo;
import com.umpay.hfmng.service.DemoService;
import com.umpay.sso.client.SSOConst;
import com.umpay.sso.org.User;
import com.umpay.hfmng.common.DivisionValue;
import com.umpay.hfmng.common.DivisionName;;
/**
 * ******************  类说明  *********************
 * class       :  DemoAction
 * @author     :  Administrator
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ***********************************************
 */
@Controller
@RequestMapping("/demo")

public class DemoAction extends BaseAction{
	private static final String MYSTOREDATA="MYSTOREDATA";
	@Autowired
	private DemoService service;
	
	@RequestMapping(value = "/index")
	public String index() throws DataAccessException{
	
		log.info("===============mer index =======");
		User u = (User)getHttpSession().getAttribute(SSOConst.PORTAL_SESSION_NAME);
		log.info("user.name"+u.getId());
		log.info("user.name"+u.getName());
		return "demo/index";
	}
	@RequestMapping(value = "/add")
	public String add() throws DataAccessException{
		log.info("===============mer add =======");
		return "demo/addMer";
	}
	
	@RequestMapping(value = "/modify")
	public String load(String id,ModelMap modeMap) throws DataAccessException{
		log.info("===============mer load =======");
		DemoInfo mer = service.load(id);
		modeMap.addAttribute("mer",mer);
		modeMap.addAttribute("myCountry","the United Kingdom/UK");
		return "demo/modifyMer";
	}
	/**
	 * ********************************************
	 * method name   : test1 
	 * description   : 
	 * @return       : String
	 * @see          : 
	 * *******************************************
	 */
	@RequestMapping(value = "/test.do")
	public String test1() throws DataAccessException{
		log.info("===============mer index =======");
		//PageBean<DemoPO> pageBean = new PageUtil<DemoPO>().getPageBean(getHttpRequest());
		//srvMerInfo.findMerInfo(pageBean,new HashMap());
		//super.setPageBean2view(pageBean);
		getHttpRequest().setAttribute("key1", "value1");
		return "demo/combox";
	}
	
	@RequestMapping(value = "/testJson")
	public ModelAndView test2(String id) throws DataAccessException{
		//String json = "{total:4,rows:[{ROWNUM_:1,id:'001',city:'中国',address:'地址1'}," +
				//"{ROWNUM_:2,id:'002',city:'美国',address:'地址2'}, " +
				//"{ROWNUM_:3,id:'003',city:'英国',address:'地址3'}, " +
				//"{ROWNUM_:4,id:'004',city:'日本',address:'地址4'}]}";
		String json = "{\"total\":2,\"rows\":[";
		json += "{\"ROWNUM_\":1,\"id\":\"001\",\"city\":\"中国\",\"address\":\"地址1\"}";
		if(!"001".equals(id)){
			json += ",{\"ROWNUM_\":2,\"id\":\"002\",\"city\":\"美国\",\"address\":\"地址2\"}";
		}
		json +=	"]}";
		return new ModelAndView("jsonView","ajax_json",json);
	}
	
	
	@RequestMapping(value = "/index2.action")
	public String indexTest(HttpServletRequest request, HttpServletResponse response){
//		org.springframework.web.context.request.RequestContextListener;
//		org.springframework.web.context.request.
		ServletRequestAttributes sra = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		HttpServletRequest request1 = sra.getRequest();
		
		System.out.println("=======RequestContextHolder=====1"+(request==request1));

		System.out.println("===============access demo page=======");
		return "demo/page";
	}

	@RequestMapping(value = "/{id}.action")
	public String indexTest(@PathVariable String id,HttpServletRequest request, HttpServletResponse response){
		System.out.println("===============access demo page====id==="+id);
		return "demo/page";
	}
	
	@RequestMapping(value = "/map.action")
	public Map<String, Object> mapTest(String param1){
		///demo/map.action?param1=eeedddrrrakkk&dd=222
		//ModelMap model
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("key1", "value1");
		map.put("key2", param1);
		System.out.println("===============map======="+map);
		return map;
	}
	@RequestMapping(value = "/model.action")
	public void modelTest(String param1,ModelMap model){
		///demo/model.action?param1=eeedddrrrakkk&dd=222
		//ModelMap model
		model.addAttribute("key", param1);
		System.out.println("===============model======="+model);
	}
	@RequestMapping(value = "/bean.action")
	public void javaBeanTest(String param1,DemoInfo po,ModelMap model,HttpServletRequest request){
		///demo/bean.action?param1=eeedddrrrakkk&merId=9996&
		//ModelMap model
		System.out.println(request.getParameter("merId"));
		model.addAttribute("key", po.getMerId());
		model.addAttribute("key2", po.getMerName());
		System.out.println("===============model======="+model);
	}
	@RequestMapping(value = "/gridcrud")
	public void gridCrud(HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		request.setCharacterEncoding("UTF-8");
		
		Object stored=request.getSession().getAttribute(MYSTOREDATA);
        if(stored==null){
            List<Ip> list=new ArrayList<Ip>(IpService.ipInfos);
            Collections.sort(list, new Comparator<Ip>(){
                public int compare(Ip o1, Ip o2) {
                    return o2.getId()-o1.getId();
                }
            });
            stored=list;
            request.getSession().setAttribute(MYSTOREDATA,stored);
        }
        List<Ip> ips=(List<Ip>) stored;
        String operation = request.getParameter("operation");
        if("add".equals(operation)){ //新增
            int newID =ips.get(0).getId()+1;
            String start = request.getParameter("start");
            String end= request.getParameter("end");
            String city=request.getParameter("city");
            String address =request.getParameter("address");
            Ip newIP=new Ip(newID,start,end,city,address);
            ips.add(0,newIP);
        }else if("modify".equals(operation)){ //修改
            int id=Integer.parseInt(request.getParameter("id"));
            String start = request.getParameter("start");
            String end= request.getParameter("end");
            String city=request.getParameter("city");
            String address =request.getParameter("address");
            int index=ips.indexOf(new Ip(id,null,null,null,null));
            Ip old=ips.get(index);
            old.setStart(start);
            old.setEnd(end);
            old.setCity(city);
            old.setAddress(address);
        }else if("delete".equals(operation)){ //删除
            int id=Integer.parseInt(request.getParameter("id"));
            int index=ips.indexOf(new Ip(id,null,null,null,null));
            ips.remove(index);
        }else{ //取数或查询
            GridDataModel<Ip> model = new GridDataModel<Ip>();
            String startStr = request.getParameter("start");
            String limitStr = request.getParameter("limit");
            int start = Integer.parseInt(startStr);
            int limit = Integer.parseInt(limitStr);
            int end = start + limit;
            
            String city=request.getParameter("qCity");
            if(city==null){ //不是查询，返回所有
                int total = ips.size();
                end = end > total ? total : end;
                model.setTotal(total);
                if(start <= end) {
                    model.setRows(ips.subList(start, end));
                }
            }else{ //是查询，返回查询结果
                List<Ip> queryed=new ArrayList<Ip>();
                for(Ip ip:ips){
                    if(ip.getCity().contains(city)){
                        queryed.add(ip);
                    }
                }
                int total = queryed.size();
                end = end > total ? total : end;
                model.setTotal(total);
                if(start <= end) {
                    model.setRows(queryed.subList(start, end));
                }
            }
            PrintWriter writer = response.getWriter();
            writer.write(net.sf.json.JSONObject.fromObject(model).toString());
        }
	}
	/**
	 * ********************************************
	 * method name   : testDivisionValue 
	 * description   : 测试区分值获取
	 */
	private static void testDivisionValue(){
		String name = DivisionName.getCheckStatusName(DivisionValue.CheckStatus.ST_OK);
		System.out.println(name);
	}
	public static void main(String args[]){
		testDivisionValue();
	}
	
	@RequestMapping(value = "/trn")
	public ModelAndView testTransaction(){
		String json = "yes";
		try{
			service.testTransaction();
		}catch(DataAccessException e){
			e.printStackTrace();
			json = "no";
		}
		return new ModelAndView("jsonView","ajax_json",json); 
	}
}
