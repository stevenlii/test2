/** *****************  JAVA头文件说明  ****************
 * file name  :  HuaDanAction.java
 * owner      :  zhaojunbao
 * copyright  :  UMPAY
 * description:  
 * modified   :  2013-1-14
 * *************************************************/ 

package com.umpay.hfmng.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.umpay.hfmng.model.Huadan;
import com.umpay.hfmng.service.HuadanService;


/** ******************  类说明  *********************
 * class       :  HuaDanAction
 * @author     :  zhaojunbao
 * @version    :  1.0  
 * description :  
 * @see        :                        
 * ************************************************/
@Controller
@RequestMapping("/huadan")
public class HuaDanAction extends BaseAction{
	@Autowired
	private HuadanService huadanService;
	@RequestMapping(value = "/add")
	public String list(ModelMap modeMap) throws DataAccessException {
		return "huadan/add";
	}
	/**
	 * ********************************************
	 * method name   : save 
	 * description   : 保存话单信息
	 * @return       : ModelAndView
	 * @param        : @param huadan
	 * @param        : @return
	 * modified      : zhaojunbao ,  2013-1-15  下午03:57:26
	 * @see          : 
	 * *******************************************
	 */
	@RequestMapping(value = "/save")
	public ModelAndView save(Huadan huadan)  {
		String string="0";
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式
		SimpleDateFormat df2 = new SimpleDateFormat("HHmmss");//设置日期格式
		Date date=new Date();
		String platDate=df.format(date);// new Date()为获取当前系统时间
		String platTime=df2.format(date);
		try {
			if(huadan!=null){
				huadan.setPlatDate(platDate);
				huadan.setPlatTime(platTime);
			}
			string = huadanService.saveHuadan(huadan);
		} catch (Exception e) {
			log.error("添加话单操作失败",e);	
		} 
		return new ModelAndView("jsonView", "ajax_json", string);
	}
	
}
