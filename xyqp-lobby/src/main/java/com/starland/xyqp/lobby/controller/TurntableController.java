package com.starland.xyqp.lobby.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.starland.xyqp.lobby.domain.Prize;
import com.starland.xyqp.lobby.domain.Turntable;
import com.starland.xyqp.lobby.domain.User;
import com.starland.xyqp.lobby.lottery.LotteryHelper;
import com.starland.xyqp.lobby.service.PrizeService;
import com.starland.xyqp.lobby.service.TurntableService;
import com.starland.xyqp.lobby.service.UserService;
import com.starland.xyqp.lobby.vo.AjaxResponse;

@Controller
@RequestMapping("/turntable")
public class TurntableController {

	private static final Logger LOGGER = LogManager.getLogger(TurntableController.class);
	
	@Resource
	private TurntableService turntableService;
	
	@Resource
	private UserService userService;
	
	@Resource
	private PrizeService prizeService;
	
	/**
	 * 传入 token 字符串   大转盘分享  
	 * @param token
	 * @return 
	 */
	@ResponseBody
	@RequestMapping(value="/share", method=RequestMethod.POST)
	public Object share(@RequestParam("token") String token) {
		if(null == token || "" == token){
			LOGGER.error("token错误");
			return new AjaxResponse(501,"token错误");
		}
		User user = userService.getByToken(token);
		if(null == user){
			LOGGER.error("获取信息失败");
			return new AjaxResponse(502,"信息获取失败");
		}
		if((user.getTokenTime().getTime()) < System.currentTimeMillis()){
			LOGGER.error("token已经过期");
			return new AjaxResponse(503,"token已经过期");
		}
		Integer user_id = user.getId();
		Turntable turntable = turntableService.getByUserId(user_id);
		Map<String,Object> map = new HashMap<String,Object>();
		if(null == turntable){
			Turntable tt = new Turntable();
			tt.setUserId(user_id);
			tt.setShare(1);
			tt.setShareNum(1);
			tt.setLottery(1);
			tt.setSparrowNum(1);
			turntableService.add(tt);
			map.put("share", tt.getShare());
			map.put("lottery", tt.getLottery());
			//雀神是否抽过奖
			map.put("sparrowShare", 0);
			map.put("sparrowNum", 1);
			return new AjaxResponse(map);
		}
		if(turntable.getShare() > 0){
			LOGGER.error("今日已经分享过");
			return new AjaxResponse(504,"今日已经分享过不能获得抽奖机会且雀神次数不能加1");
		}
		turntable.setLottery(turntable.getLottery()+1);
		turntable.setShare(turntable.getShare()+1);
		//判断雀神次数小余3 加一 否则不加一
		if(turntable.getSparrowNum() < 3){
				turntable.setSparrowNum(turntable.getSparrowNum()+1);
		}
		turntableService.update(turntable);
		map.put("share",turntable.getShare());
		map.put("lottery", turntable.getLottery());
		map.put("sparrowShare", turntable.getSparrowShare());
		map.put("sparrowNum", turntable.getSparrowNum());
		return new AjaxResponse(map);
	}
	
	
	/**
	 * 大转盘抽奖
	 * @param token 传入用户的token
	 * @return 
	 * @author 爱龙
	 */
	@ResponseBody
	@RequestMapping(value="/lottery", method=RequestMethod.POST)
	public Object lottery(@RequestParam("token") String token){
		if(null == token || "" == token){
			LOGGER.error("token错误");
			return new AjaxResponse(501,"token错误");
		}
		User user = userService.getByToken(token);
		if(null == user){
			LOGGER.error("获取信息失败");
			return new AjaxResponse(502,"信息获取失败");
		}
		if((user.getTokenTime().getTime()) < System.currentTimeMillis()){
			LOGGER.error("token已经过期");
			return new AjaxResponse(503,"token已经过期");
		}
		Integer user_id = user.getId();
		Turntable turntable = turntableService.getByUserId(user_id);
		Map<String,Object> map = new HashMap<String,Object>();
		if(null == turntable){
			LOGGER.error("此用户无分享记录");
			return new AjaxResponse(504,"此用户无分享记录");
		}
		if(0 == turntable.getLottery() || null == turntable.getLottery()){
			LOGGER.error("已无抽奖次数");
			return new AjaxResponse(505,"已无抽奖次数");
		}
		List<Prize> list = prizeService.list();
		LotteryHelper<Integer> lotteryHelper = new LotteryHelper<>();
		for(Prize prize : list){
			lotteryHelper.add(prize.getPrizeId(), prize.getWeight());
		}
		Integer prize_id = lotteryHelper.take();
		turntable.setPrize(prize_id);
		turntable.setLottery(turntable.getLottery()-1);
		turntableService.update(turntable);
		if(prize_id == 1){
			if(null == user.getIntegral()){
				user.setIntegral(20);
				userService.update(user);
			}else{
				user.setIntegral(user.getIntegral() + 20);
				userService.update(user);
			}
		}
		if(prize_id == 5){
			if(null == user.getDiamond()){
				user.setDiamond(4);
				userService.update(user);
			}else{
				user.setDiamond(user.getDiamond() + 4);
				userService.update(user);
			}
		}
		if(prize_id == 3){
			if(null == user.getDiamond()){
				user.setDiamond(1);
				userService.update(user);
			}else{
				user.setDiamond(user.getDiamond() + 1);
				userService.update(user);
			}
		}
		map.put("share", turntable.getShare());
		map.put("lottery", turntable.getLottery());
		map.put("prizeid",prize_id);
		if(null == user.getDiamond()){
			map.put("diamond", 0);
		}
		map.put("diamond", user.getDiamond());
		if( null == user.getIntegral()){
			map.put("integral", 0);
		}else{
			map.put("integral", user.getIntegral());
		}
		return new AjaxResponse(map);
	}
	
	
	
	/**
	 * 雀神转盘抽奖
	 * @param token 參數传入用户token
	 * @return 
	 * @author 愛龍
	 */
	@ResponseBody
	@RequestMapping(value = "/sparrowDraw", method = RequestMethod.POST)
	public Object sparrowDraw(@RequestParam("token") String token) {
		if (null == token || "" == token) {
			LOGGER.error("token错误");
			return new AjaxResponse(501, "token错误");
		}
		User user = userService.getByToken(token);
		if (null == user) {
			LOGGER.error("获取信息失败");
			return new AjaxResponse(502, "信息获取失败");
		}
		if ((user.getTokenTime().getTime()) < System.currentTimeMillis()) {
			LOGGER.error("token已经过期");
			return new AjaxResponse(503, "token已经过期");
		}
		Integer user_id = user.getId();
		Turntable turntable = turntableService.getByUserId(user_id);
		if (null == turntable) {
			LOGGER.error("无相关信息不能抽奖");
			return new AjaxResponse(504, "不能抽奖数据库无相关信息");
		}
		if (turntable.getSparrowNum() < 3) {
			LOGGER.error("雀神次数没有达到");
			return new AjaxResponse(505, "雀神次数没有达到");
		}
		if (null == turntable.getSparrowShare() || turntable.getSparrowShare() == 0) {
			Map<String, Object> map = new HashMap<String, Object>();
			List<Prize> list = prizeService.getList();
			LotteryHelper<Integer> lotteryHelper = new LotteryHelper<>();
			for (Prize prize : list) {
				lotteryHelper.add(prize.getPrizeId(), prize.getWeight());
			}
			Integer prize_id = lotteryHelper.take();
			turntable.setPrize(prize_id);
			turntable.setSparrowShare(1);
			turntableService.update(turntable);

			if (prize_id == 1) {
				if(null == user.getIntegral()){
					user.setIntegral(1);
					userService.update(user);
				}else{
					user.setIntegral(user.getIntegral() + 1);
					userService.update(user);
				}
			}
			if (prize_id == 3) {
				if(null == user.getIntegral()){
					user.setIntegral(10);
					userService.update(user);
				}else{
					user.setIntegral(user.getIntegral() + 10);
					userService.update(user);
				}
			}
			if (prize_id == 4) {
				if(null == user.getDiamond()){
					user.setDiamond(3);
					userService.update(user);
				}else{
					user.setDiamond(user.getDiamond() + 3);
					userService.update(user);
				}
			}
			if (prize_id == 5) {
				if(null == user.getIntegral()){
					user.setIntegral(3);
					userService.update(user);
				}else{
					user.setIntegral(user.getIntegral() + 3);
					userService.update(user);
				}
			}
			if (prize_id == 7) {
				if(null == user.getIntegral()){
					user.setIntegral(30);
					userService.update(user);
				}else{
					user.setIntegral(user.getIntegral() + 30);
					userService.update(user);
				}
			}
			if (prize_id == 8) {
				if(null == user.getDiamond()){
					user.setDiamond(5);
					userService.update(user);
				}else{
					user.setDiamond(user.getDiamond() + 5);
					userService.update(user);
				}
			}
			map.put("prizeid", prize_id);
			if (null == user.getIntegral()) {
				map.put("integral", 0);
			} else {
				map.put("integral", user.getIntegral());
			}
			if (null == user.getDiamond()) {
				map.put("diamond", 0);
			} else {
				map.put("diamond", user.getDiamond());
			}
			map.put("sparrowShare", turntable.getSparrowShare());
			return new AjaxResponse(map);
		}else{
			LOGGER.error("今日已经抽过奖");
			return new AjaxResponse(506,"今日已经抽过奖");
		}
		
	}
	
	
	/**
	 * 该方法为查看周雀神排行榜信息
	 * @param token 传入用户的token
	 * @return  
	 */
	@ResponseBody
	@RequestMapping(value="/sparrowWeekRank", method=RequestMethod.POST)
	public Object sparrowWeekRank(@RequestParam("token") String token){
		if(null == token || "" == token){
			LOGGER.error("token错误");
			return new AjaxResponse(500,"token错误");
		}
		User user = userService.getByToken(token);
		if(null == user){
			LOGGER.error("获取信息失败");
			return new AjaxResponse(500,"信息获取失败");
		}
		if((user.getTokenTime().getTime()) < System.currentTimeMillis()){
			LOGGER.error("token已经过期");
			return new AjaxResponse(500,"token已经过期");
		}
		List<Turntable> list = turntableService.getSparrowWeek();
		if(null == list){
			LOGGER.error("获取信息失败");
			return new AjaxResponse(500,"获取信息失败");
		}
		if(list.size() == 0){
			LOGGER.error("无相关数据");
			return new AjaxResponse(500,"无相关数据");
		}
		return new AjaxResponse(list);
	}
	
	
	/**
	 * 该方法为查看月雀神排行榜信息
	 * @param token  用户的token
	 * @return 
	 */
	@ResponseBody
	@RequestMapping(value="/sparrowMonthRank", method=RequestMethod.POST)
	public Object sparrowMonthRank(@RequestParam("token") String token){
		if(null == token || "" == token){
			LOGGER.error("token错误");
			return new AjaxResponse(500,"token错误");
		}
		User user = userService.getByToken(token);
		if(null == user){
			LOGGER.error("获取信息失败");
			return new AjaxResponse(500,"信息获取失败");
		}
		if((user.getTokenTime().getTime()) < System.currentTimeMillis()){
			LOGGER.error("token已经过期");
			return new AjaxResponse(500,"token已经过期");
		}
		List<Turntable> list = turntableService.getSparrowMonth();
		if(null == list){
			LOGGER.error("获取信息失败");
			return new AjaxResponse("获取信息失败");
		}
		if(0 == list.size()){
			LOGGER.error("无相关数据");
			return new AjaxResponse("无相关数据");
		}
		return new AjaxResponse(list);
	}
	
	
	/**
	 * 获取周衰神榜信息
	 * @param token 用户所传入的token
	 * @return 返回衰神榜集合
	 */
	@ResponseBody
	@RequestMapping(value="/negativeWeekRank", method=RequestMethod.POST)
	public Object negativeWeekRank(@RequestParam("token") String token){
		if(null == token || "" == token){
			LOGGER.error("token错误");
			return new AjaxResponse(500,"token错误");
		}
		User user = userService.getByToken(token);
		if(null == user){
			LOGGER.error("获取信息失败");
			return new AjaxResponse(500,"信息获取失败");
		}
		if((user.getTokenTime().getTime()) < System.currentTimeMillis()){
			LOGGER.error("token已经过期");
			return new AjaxResponse(500,"token已经过期");
		}
		List<Turntable> list = turntableService.getNegativeWeek();
		if(null == list){
			LOGGER.error("获取信息失败");
			return new AjaxResponse("获取信息失败");
		}
		if(0 == list.size()){
			LOGGER.error("无相关数据");
			return new AjaxResponse("无相关数据");
		}
		return new AjaxResponse(list);
	}
	
	
	/**
	 * 查看月衰神排行榜信息
	 * @param token 用户登录的标识
	 * @return 返回月衰神排行榜的集合
	 */
	@ResponseBody
	@RequestMapping(value="/negativeMonthRank", method=RequestMethod.POST)
	public Object negativeMonthRank(@RequestParam("token") String token){
		if(null == token || "" == token){
			LOGGER.error("token错误");
			return new AjaxResponse(500,"token错误");
		}
		User user = userService.getByToken(token);
		if(null == user){
			LOGGER.error("获取信息失败");
			return new AjaxResponse(500,"信息获取失败");
		}
		if((user.getTokenTime().getTime()) < System.currentTimeMillis()){
			LOGGER.error("token已经过期");
			return new AjaxResponse(500,"token已经过期");
		}
		List<Turntable> list = turntableService.getNegativeMonth();
		if(null == list){
			LOGGER.error("获取信息失败");
			return new AjaxResponse("获取信息失败");
		}
		if(0 == list.size()){
			LOGGER.error("无相关数据");
			return new AjaxResponse("无相关数据");
		}
		return new AjaxResponse(list);
	}
	
	
	/**
	 * 查看周消费房卡排行榜信息
	 * @param token 用户传入的token
	 * @return 返回周消费房卡排行榜集合
	 */
	@ResponseBody
	@RequestMapping(value="/roomCardWeekRank", method=RequestMethod.POST)
	public Object roomCardWeekRank(@RequestParam("token") String token){
		if(null == token || "" == token){
			LOGGER.error("token错误");
			return new AjaxResponse(500,"token错误");
		}
		User user = userService.getByToken(token);
		if(null == user){
			LOGGER.error("获取信息失败");
			return new AjaxResponse(500,"信息获取失败");
		}
		if((user.getTokenTime().getTime()) < System.currentTimeMillis()){
			LOGGER.error("token已经过期");
			return new AjaxResponse(500,"token已经过期");
		}
		List<Turntable> list = turntableService.getRoomCardWeek();
		if(null == list){
			LOGGER.error("获取信息失败");
			return new AjaxResponse("获取信息失败");
		}
		if(0 == list.size()){
			LOGGER.error("无相关数据");
			return new AjaxResponse("无相关数据");
		}
		return new AjaxResponse(list);
	}
	
	
	
	/**
	 * 查看月消费房卡的排行榜信息
	 * @param token 用户传入的token
	 * @return 月消费房卡排行榜信息集合
	 */
	@ResponseBody
	@RequestMapping(value="/roomCardMonthRank", method=RequestMethod.POST)
	public Object roomCardMonthRank(@RequestParam("token") String token){
		if(null == token || "" == token){
			LOGGER.error("token错误");
			return new AjaxResponse(500,"token错误");
		}
		User user = userService.getByToken(token);
		if(null == user){
			LOGGER.error("获取信息失败");
			return new AjaxResponse(500,"信息获取失败");
		}
		if((user.getTokenTime().getTime()) < System.currentTimeMillis()){
			LOGGER.error("token已经过期");
			return new AjaxResponse(500,"token已经过期");
		}
		List<Turntable> list = turntableService.getRoomCardMonth();
		if(null == list){
			LOGGER.error("获取信息失败");
			return new AjaxResponse("获取信息失败");
		}
		if(0 == list.size()){
			LOGGER.error("无相关数据");
			return new AjaxResponse("无相关数据");
		}
		return new AjaxResponse(list);
	}
	
	
	
}
