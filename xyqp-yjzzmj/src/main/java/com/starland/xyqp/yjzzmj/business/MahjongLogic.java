package com.starland.xyqp.yjzzmj.business;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.starland.tools.network.RouteSession;
import com.starland.tools.shape.ShapeParser;
import com.starland.xyqp.common.exception.UselessRequestException;
import com.starland.xyqp.db.domain.User;
import com.starland.xyqp.yjzzmj.model.CardInfo;
import com.starland.xyqp.yjzzmj.model.CardShape;
import com.starland.xyqp.yjzzmj.model.DataMaps;
import com.starland.xyqp.yjzzmj.model.HearInfo;
import com.starland.xyqp.yjzzmj.model.JudgerParam;
import com.starland.xyqp.yjzzmj.model.PlayWay;
import com.starland.xyqp.yjzzmj.model.Room;
import com.starland.xyqp.yjzzmj.model.Seat;
import com.starland.xyqp.yjzzmj.model.SettleDetail;
import com.starland.xyqp.yjzzmj.model.SettleInfo;

@Component
public class MahjongLogic {

	private static final Logger LOGGER = LogManager.getLogger(MahjongLogic.class);
	
	/**
	 * 数据集合
	 */
	private DataMaps dataMaps = new DataMaps();
	
	/**
	 * 随机对象
	 */
	private Random random = new Random();
	
	private ShapeParser shapeParser = new ShapeParser();
	
//	@PostConstruct
	@Deprecated
	public void initDataMaps() {
		File temp = new File("temp");
		if (!temp.exists()) {
			temp.mkdirs();
		}
		File file = new File("temp" + File.separator + "dataMaps.obj");
		if (file.exists()) {
			try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
				dataMaps = (DataMaps) ois.readObject();
				LOGGER.info("服务器启动，成功读取缓存中的房间信息！");
			} catch (IOException | ClassNotFoundException e) {
				LOGGER.error("", e);
			}
			file.delete();
		}
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
				oos.writeObject(dataMaps);
				LOGGER.info("服务器关闭，成功保存房间信息到缓存中！");
			} catch (IOException e) {
				LOGGER.error("", e);
			}
		}));
	}
	
	/**
	 * 创建一个房间
	 * @param session
	 * @return
	 */
	public Room createRoom(PlayWay playWay, String roomId) {
		Room room = new Room(roomId, 4);
		room.setPlayWay(playWay);
		dataMaps.getRoomMap().put(roomId, room);
		return room;
	}
	
	/**
	 * 解散房间
	 */
	public void dissolveRoom(Room room) {
		List<Seat> seatList = room.getSeatList();
		for (Seat seat : seatList) {
			User user = seat.getUser();
			if (null == user) {
				continue;
			}
			Integer userId = user.getId();
			dataMaps.getSeatMap().remove(userId);
		}
		String roomId = room.getId();
		dataMaps.getRoomMap().remove(roomId);
	}
	
	/**
	 * 加入到座位
	 * @param session
	 * @param seat
	 */
	public void joinSeat(RouteSession session, User user, Seat seat) {
		Integer userId = user.getId();
		if (dataMaps.getSeatMap().containsKey(userId)) {
			throw new UselessRequestException("已经有用户在座位上！");
		}
		dataMaps.getSeatMap().put(userId, seat);
		seat.setEmpty(false);
		seat.setOnline(true);
		seat.setRouteSession(session);
		seat.setUser(user);
	}
	
	/**
	 * 根据位置获取一个座位
	 * @param room
	 * @param position
	 * @return
	 */
	public Seat getSeat(Room room, int position) {
		List<Seat> seats = room.getSeatList();
		if (position <= 0 || position > seats.size()) {
			return null;
		}
		return seats.get(position - 1);
	}
	
	/**
	 * 根据用户编号获取座位
	 * @param userId
	 * @return
	 */
	public Seat getSeatByUserId(Integer userId) {
		return dataMaps.getSeatMap().get(userId);
	}
	
	/**
	 * 根据房间号获取房间
	 * @param roomId
	 * @return
	 */
	public Room getRoom(String roomId) {
		return dataMaps.getRoomMap().get(roomId);
	}
	
	/**
	 * 查找一个空座位
	 * @param room
	 * @return
	 */
	public Seat findEmptySeat(Room room) {
		List<Seat> seats = room.getSeatList();
		for (Seat seat : seats) {
			if (seat.isEmpty()) {
				return seat;
			}
		}
		return null;
	}
	
	/**
	 * 是否全部准备好了
	 * @param room
	 * @return
	 */
	public boolean isAllReady(Room room) {
		List<Seat> seats = room.getSeatList();
		for (Seat seat : seats) {
			if (!seat.isReady()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 座位是否都坐满了
	 * @param room
	 * @return
	 */
	public boolean isFull(Room room) {
		List<Seat> seats = room.getSeatList();
		for (Seat seat : seats) {
			if (seat.isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 洗牌
	 * @param room
	 */
	public void shuffle(Room room) {
		List<Integer> cards = new ArrayList<>();
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 4; j++) {
				// 1-9 万
				cards.add(i + 1);
				// 11-19 条
				cards.add(i + 11);
				// 21-29 筒
				cards.add(i + 21);
			}
		}
		Deque<Integer> cardList = room.getCardList();
		cardList.clear();
		while(!cards.isEmpty()) {
			int index = random.nextInt(cards.size());
//			index = 0;
			Integer card = cards.remove(index);
			cardList.push(card);
		}
	}
	
	/**
	 * 给每个座位发牌
	 * @param room
	 */
	public void allotCards(Room room) {
		List<Seat> seats = room.getSeatList();
		for (Seat seat : seats) {
			Deque<Integer> cardList = room.getCardList();
			List<Integer> seatCards = seat.getCardList();
			for (int i = 0; i < 13; i++) {
				Integer card = cardList.removeFirst();
				seatCards.add(card);
			}
			if (seat.getPosition() == room.getBankerPosition()) {
				Integer card = cardList.removeFirst();
				seat.setCard(card);
			}
		}
	}
	
	/**
	 * 出牌
	 * @param seat
	 * @param card
	 */
	public void outCard(Seat seat, Integer card) {
		if (null == seat.getCard()) {
			throw new UselessRequestException("该玩家不能出牌！");
		}
		if (!seat.getHearList().isEmpty() && !seat.getCard().equals(card)) {
			throw new UselessRequestException("不能出这张牌！");
		}
		List<Integer> cardList = seat.getCardList();
		if (!seat.getCard().equals(card)) {
			if (!cardList.remove(card)) {
				throw new UselessRequestException("该玩家没有[" + card + "]这张牌！");
			}
			cardList.add(seat.getCard());
		}
		seat.setFirstTakeCard(false);
		seat.setJustBridge(false);
		seat.setGrabBridgeCard(null);
		seat.setCard(null);
		seat.getOutList().add(card);
		Room room = seat.getRoom();
		room.setLastCard(card);
		room.setLastPosition(seat.getPosition());
	}
	
	/**
	 * 接一张牌
	 * @param seat
	 * @return
	 */
	public Integer takeCard(Seat seat) {
		if (null != seat.getCard()) {
			throw new UselessRequestException("该玩家有牌未打出去！");
		}
		Room room = seat.getRoom();
		Deque<Integer> cardList = room.getCardList();
		if (cardList.isEmpty()) {
			throw new UselessRequestException("牌墙中已经没有牌了！");
		}
		room.setSkyWin(false);
		Integer card = cardList.removeFirst();
//		card = 6;
		seat.setCard(card);
		return card;
	}
	
	/**
	 * 更新牌的分析信息，即吃、碰、杠、胡、听等信息
	 * @param seat
	 */
	public void updateCardInfo(Seat seat) {
		CardInfo cardInfo = new CardInfo();
		checkBump(seat, cardInfo);
		checkBridge(seat, cardInfo);
		checkWin(seat, cardInfo);
		checkHear(seat, cardInfo);
		boolean flag = false;
		if (cardInfo.isCanBridge()) {
			flag = true;
		}
		if (cardInfo.isCanBump()) {
			flag = true;
		}
		if (cardInfo.isCanWin()) {
			flag = true;
		}
		if (cardInfo.isCanHear()) {
			flag = true;
		}
		if (flag) {
			seat.setCardInfo(cardInfo);
		} else {
			seat.setCardInfo(null);
		}
	}
	
	/**
	 * 检测抢杠胡
	 * @param seat
	 * @param card
	 */
	public void checkGrapBridgeWin(Seat seat, Integer card) {
		Room room = seat.getRoom();
		List<Seat> seatList = room.getSeatList();
		for (Seat pseat : seatList) {
			if (pseat.equals(seat)) {
				continue;
			}
			JudgerParam judgerParam = getJudgerParam(pseat);
			judgerParam.setGrabBridge(true);
			judgerParam.setCard(card);
			if (canWin(judgerParam)) {
				seat.setGrabBridgeCard(card);
				CardInfo cardInfo = new CardInfo();
				cardInfo.setCanWin(true);
				pseat.setCardInfo(cardInfo);
			}
		}
	}
	
	/**
	 * 上家的座位
	 * @param seat
	 * @return
	 */
	public Seat preSeat(Seat seat) {
		Room room = seat.getRoom();
		int size = room.getSeatList().size();
		int position = seat.getPosition() - 1;
		if (position <= 0) {
			position = size;
		}
		return getSeat(room, position);
	}
	
	/**
	 * 下家的座位
	 * @param seat
	 * @return
	 */
	public Seat nextSeat(Seat seat) {
		Room room = seat.getRoom();
		int size = room.getSeatList().size();
		int position = seat.getPosition() + 1;
		if (position > size) {
			position = 1;
		}
		return getSeat(room, position);
	}
	
	
	/**
	 * 碰牌
	 * @param seat
	 */
	public void bumpCard(Seat seat) {
		Room room = seat.getRoom();
		Integer card = seat.getCard();
		List<Integer> cardList = seat.getCardList();
		int lastCard = room.getLastCard();
		int lastPosition = room.getLastPosition();
		// 自己打出的牌不能碰
		if (seat.getPosition() == lastPosition) {
			throw new UselessRequestException();
		}
		// 手上接了牌的情况下，不能碰
		if (null != card) {
			throw new UselessRequestException();
		}
		int count = findCardCount(cardList, lastCard);
		if (count < 2) {
			throw new UselessRequestException();
		}
		Seat lastSeat = getSeat(room, lastPosition);
		List<Integer> outList = lastSeat.getOutList();
		outList.remove(outList.size() - 1);
		seat.setFirstTakeCard(false);
		removeCard(cardList, lastCard, 2);
		seat.getBumpList().add(lastCard);
		Integer pai = cardList.remove(cardList.size() - 1);
		seat.setCard(pai);
	}
	
	/**
	 * 胡牌
	 * @param seats
	 */
	public void winCard(List<Seat> seats) {
		if (seats.size() == 1) {
			Seat seat = seats.get(0);
			if (seat.getCard() != null) {
				winCardSelf(seat);
				return;
			}
		}
		Room room = seats.get(0).getRoom();
		List<Seat> seatList = room.getSeatList();
		for (Seat seat : seatList) {
			if (seat.getGrabBridgeCard() != null) {
				winGrabBridge(seats, seat);
				return;
			}
		}
		winCardOther(seats);
	}
	
	/**
	 * 杠牌
	 * @param seat
	 * @param card
	 */
	public String bridgeCard(Seat seat, Integer card) {
		Integer myCard = seat.getCard();
		// 自己刚打完牌不能碰
		Room room = seat.getRoom();
		int lastPosition = room.getLastPosition();
		if (seat.getPosition() == lastPosition) {
			throw new UselessRequestException();
		}
		Integer lastCard = room.getLastCard();
		List<Integer> cardList = seat.getCardList();
		if (null == myCard && null == lastCard) {
			throw new UselessRequestException();
		}
		seat.setFirstTakeCard(false);
		// 明杠
		if (null == myCard && null != lastCard && lastCard.equals(card)) {
			int count = findCardCount(cardList, lastCard);
			if (count >= 3) {
				Seat lastSeat = getSeat(room, lastPosition);
				lastSeat.setSendBridgeNum(lastSeat.getSendBridgeNum() + 1);
				showBridgeCard(seat, card);
				seat.setJustBridge(true);
				return "showBridge";
			}
		}
		if (null != myCard) {
			List<Integer> list = new ArrayList<>();
			list.addAll(cardList);
			list.add(myCard);
			int count = findCardCount(list, card);
			if (count >= 3) {
				hideBridgeCard(seat, card);
				seat.setJustBridge(true);
				return "hideBridge";
			}
			List<Integer> bumpList = seat.getBumpList();
			if (count >= 1 && bumpList.contains(card)) {
				passBridgeCard(seat, card);
				seat.setJustBridge(true);
				return "passBridge";
			}
		}
		throw new UselessRequestException();
	}
	
	/**
	 * 检测是否为海底
	 * @return
	 */
	public boolean checkSeafloor(Seat seat) {
		Room room = seat.getRoom();
		Deque<Integer> cardList = room.getCardList();
		if (cardList.size() > 4) {
			return false;
		}
		cardList.add(13);
		cardList.add(13);
		cardList.add(13);
		cardList.add(13);
		Seat thisSeat = seat;
		for (Integer card : cardList) {
			JudgerParam judgerParam = getJudgerParam(seat);
			judgerParam.setCard(card);
			judgerParam.setSeafloor(true);
			judgerParam.setTakeSelf(true);
			WinJudger winJudger = new WinJudger();
			List<CardShape> shapes = shapeParser.parse(winJudger, judgerParam);
			if (!shapes.isEmpty()) {
				thisSeat.setCard(card);
				winCardSeafloor(thisSeat, shapes);
				return true;
			}
			thisSeat = nextSeat(thisSeat);
		}
		winFlowRound(room);
		return true;
	}
	
	/**
	 * 听牌
	 * @param seat
	 * @param card
	 */
	public void hearCard(Seat seat, Integer card) {
		CardInfo cardInfo = seat.getCardInfo();
		if (null == cardInfo) {
			throw new UselessRequestException();
		}
		seat.setCardInfo(null);
		if (!cardInfo.isCanHear()) {
			throw new UselessRequestException();
		}
		if (!seat.getHearList().isEmpty()) {
			throw new UselessRequestException();
		}
		List<HearInfo> hearInfoList = cardInfo.getHearInfoList();
		for (HearInfo hearInfo : hearInfoList) {
			if (card.equals(hearInfo.getCard())) {
				List<Integer> cardList = hearInfo.getCardList();
				seat.getHearList().addAll(cardList);
				return;
			}
		}
	}
	
	/**
	 * 离开房间
	 * @param seat
	 */
	public void leaveRoom(Seat seat, Integer userId) {
		Room room = seat.getRoom();
		if (room.getRoundCount() != 0) {
			throw new UselessRequestException("游戏已经开始，不能离开房间！");
		}
		dataMaps.getSeatMap().remove(userId);
		seat.setEmpty(true);
		seat.setOnline(false);
		seat.setRouteSession(null);
		seat.setUser(null);
	}
	
	/**
	 * 检测碰
	 * @param seat
	 * @param cardInfo
	 */
	private void checkBump(Seat seat, CardInfo cardInfo) {
		if (!seat.getHearList().isEmpty()) {
			return;
		}
		Room room = seat.getRoom();
		Integer card = seat.getCard();
		List<Integer> cardList = seat.getCardList();
		Integer lastCard = room.getLastCard();
		if (null == lastCard) {
			return;
		}
		int lastPosition = room.getLastPosition();
		// 自己打出的牌不能碰
		if (seat.getPosition() == lastPosition) {
			return;
		}
		// 手上接了牌的情况下，不能碰
		if (null != card) {
			return;
		}
		int count = findCardCount(cardList, lastCard);
		if (count >= 2) {
			cardInfo.setCanBump(true);
		}
	}
	
	/**
	 * 检测杠
	 * @param seat
	 * @param cardInfo
	 */
	private void checkBridge(Seat seat, CardInfo cardInfo) {
		if (!seat.getHearList().isEmpty()) {
			return;
		}
		Room room = seat.getRoom();
		Integer card = seat.getCard();
		List<Integer> cardList = seat.getCardList();
		List<Integer> bumpList = seat.getBumpList();
		Integer lastCard = room.getLastCard();
		int lastPosition = room.getLastPosition();
		// 自己刚打完牌不能碰
		if (seat.getPosition() == lastPosition) {
			return;
		}
		List<Integer> bridgeList = new ArrayList<>();
		// 明杠
		if (null == card && null != lastCard) {
			int count = findCardCount(cardList, lastCard);
			if (count >= 3) {
				cardInfo.setCanBridge(true);
				bridgeList.add(lastCard);
			}
		}
		// 暗杠
		if (null != card) {
			List<Integer> cards = new ArrayList<>();
			cards.addAll(cardList);
			cards.add(card);
			for (Integer pai : cards) {
				if (bridgeList.contains(pai)) {
					continue;
				}
				int count = findCardCount(cards, pai);
				if (count >= 4) {
					cardInfo.setCanBridge(true);
					bridgeList.add(pai);
				}
			}
		}
		// 过路杠
		if (null != card) {
//			List<Integer> cards = new ArrayList<>();
//			cards.addAll(cardList);
//			cards.add(card);
//			for (Integer pai : cards) {
//				if (bridgeList.contains(pai)) {
//					continue;
//				}
//				if (bumpList.contains(pai)) {
//					cardInfo.setCanBridge(true);
//					bridgeList.add(pai);
//				}
//			}
			if (bumpList.contains(card)) {
				cardInfo.setCanBridge(true);
				bridgeList.add(card);
			}
		}
		if (cardInfo.isCanBridge()) {
			cardInfo.setBridgeList(bridgeList);
		}
	}
	
	/**
	 * 检测胡
	 * @param seat
	 * @param cardInfo
	 */
	private void checkWin(Seat seat, CardInfo cardInfo) {
		Room room = seat.getRoom();
		Integer card = seat.getCard();
		Integer lastCard = room.getLastCard();
		int lastPosition = room.getLastPosition();
		// 刚打完牌不能胡
		if (seat.getPosition() == lastPosition) {
			return;
		}
		JudgerParam judgerParam = getJudgerParam(seat);
		if (null != card) {
			// 自摸
			judgerParam.setTakeSelf(true);
			judgerParam.setCard(card);
		} else {
			// 点炮
			if (seat.getWinPassCards().contains(lastCard)) {
				return;
			}
			judgerParam.setCard(lastCard);
		}
		if (canWin(judgerParam)) {
			cardInfo.setCanWin(true);
		}
	}
	
	/**
	 * 检测听
	 * @param seat
	 * @param cardInfo
	 */
	private void checkHear(Seat seat, CardInfo cardInfo) {
		if (seat.getCard() == null) {
			return;
		}
		if (!seat.isFirstTakeCard()) {
			return;
		}
		if (!seat.getHearList().isEmpty()) {
			return;
		}
		List<HearInfo> hearInfoList = findHearInfo(seat);
		if (!hearInfoList.isEmpty()) {
			cardInfo.setCanHear(true);
			cardInfo.setHearInfoList(hearInfoList);
		}
	}
	
	/**
	 * 查找听列表
	 * @param seat
	 * @return
	 */
	private List<HearInfo> findHearInfo(Seat seat) {
		List<HearInfo> result = new ArrayList<>();
		List<Integer> cardList = new LinkedList<>();
		cardList.addAll(seat.getCardList());
		cardList.add(seat.getCard());
		Set<Integer> set = new HashSet<>();
		set.addAll(cardList);
		Iterator<Integer> iter = set.iterator();
		while(iter.hasNext()) {
			Integer card = iter.next();
			List<Integer> cards = new LinkedList<>();
			cards.addAll(cardList);
			cards.remove(card);
			HearJudger hearJudger = new HearJudger();
			List<List<Integer>> groups = shapeParser.parse(hearJudger, cards);
			List<Integer> list = new ArrayList<>();
			for (List<Integer> group : groups) {
				list.addAll(group);
			}
			if (list.isEmpty()) {
				continue;
			}
			HearInfo hearInfo = new HearInfo();
			hearInfo.setCard(card);
			hearInfo.setCardList(list);
			result.add(hearInfo);
		}
		return result;
	}
	
	/**
	 * 是否能胡
	 * @param cards
	 * @return
	 */
	private boolean canWin(JudgerParam judgerParam) {
		WinJudger winJudger = new WinJudger();
		List<CardShape> shapes = shapeParser.parse(winJudger, judgerParam);
		return !shapes.isEmpty();
	}
	
	/**
	 * 查找牌的数量
	 * @param cardList
	 * @param card
	 * @return
	 */
	private int findCardCount(List<Integer> cardList, Integer card) {
		int count = 0;
		for (Integer integer : cardList) {
			if (integer.equals(card)) {
				count++;
			}
		}
		return count;
	}
	
	/**
	 * 自摸
	 */
	private void winCardSelf(Seat seat) {
		Room room = seat.getRoom();
		room.setBankerPosition(seat.getPosition());
		List<Seat> seatList = room.getSeatList();
		JudgerParam judgerParam = getJudgerParam(seat);
		judgerParam.setTakeSelf(true);
		judgerParam.setCard(seat.getCard());
		WinJudger winJudger = new WinJudger();
		List<CardShape> shapes = shapeParser.parse(winJudger, judgerParam);
		StringBuilder buf = new StringBuilder();
		buf.append("自摸");
		for (CardShape cardShape : shapes) {
			buf.append("," + cardShape.getName());
		}
		int score = getScore(shapes);
		for (Seat pseat : seatList) {
			SettleInfo settleInfo = pseat.getSettleInfo();
			SettleDetail settleDetail = new SettleDetail();
			int offsetScore = 0;
			if (seat.equals(pseat)) {
				settleInfo.setWinSelfNum(settleInfo.getWinSelfNum() + 1);
				offsetScore = (seatList.size() - 1) * score;
				settleDetail.setDescription(buf.toString());
				settleDetail.setWin(true);
			} else {
				offsetScore = -1 * score;
			}
			settleDetail.setOffsetScore(offsetScore);
			settleInfo.setCurrentScore(settleInfo.getCurrentScore() + offsetScore);
			settleDetail.setCurrentScore(settleInfo.getCurrentScore());
			settleInfo.getSettleDetails().add(settleDetail);
		}
		settleZhaniao(seat);
		amendMaxScore(room);
		settleExtra(room);
	}
	
	private void settleZhaniao(Seat seat) {
		Room room = seat.getRoom();
		PlayWay playWay = room.getPlayWay();
		if (!playWay.isZhaniao()) { 
			return;
		}
		Seat zhaniaoSeat = seat;
		Integer zhaniaoCard = room.getCardList().removeLast();
		room.setZhaniaoCard(zhaniaoCard);
		int length = zhaniaoCard % 10 - 1;
		for (int i = 0; i < length; i++) {
			zhaniaoSeat = nextSeat(zhaniaoSeat);
		}
		room.setZhaniaoPosition(zhaniaoSeat.getPosition());
		List<Seat> seatList = room.getSeatList();
		if (zhaniaoSeat.equals(seat)) {
			//扎中自己全翻倍
			for (Seat pseat : seatList) {
				SettleInfo settleInfo = pseat.getSettleInfo();
				List<SettleDetail> settleDetails = settleInfo.getSettleDetails();
				SettleDetail settleDetail = settleDetails.get(settleDetails.size() - 1);
				int offsetScore = settleDetail.getOffsetScore();
				settleDetail.setOffsetScore(offsetScore * 2);
				settleInfo.setCurrentScore(settleInfo.getCurrentScore() + offsetScore);
				settleDetail.setCurrentScore(settleInfo.getCurrentScore());
			}
		} else {
			SettleInfo settleInfo = zhaniaoSeat.getSettleInfo();
			List<SettleDetail> settleDetails = settleInfo.getSettleDetails();
			SettleDetail settleDetail = settleDetails.get(settleDetails.size() - 1);
			int offsetScore = settleDetail.getOffsetScore();
			settleDetail.setOffsetScore(offsetScore * 2);
			settleInfo.setCurrentScore(settleInfo.getCurrentScore() + offsetScore);
			settleDetail.setCurrentScore(settleInfo.getCurrentScore());
			
			SettleInfo settleInfo2 = seat.getSettleInfo();
			List<SettleDetail> settleDetails2 = settleInfo2.getSettleDetails();
			SettleDetail settleDetail2 = settleDetails2.get(settleDetails2.size() - 1);
			settleDetail2.setOffsetScore(settleDetail2.getOffsetScore() - offsetScore);
			settleInfo2.setCurrentScore(settleInfo2.getCurrentScore() - offsetScore);
			settleDetail2.setCurrentScore(settleInfo2.getCurrentScore());
		}
		SettleInfo settleInfo = zhaniaoSeat.getSettleInfo();
		List<SettleDetail> settleDetails = settleInfo.getSettleDetails();
		SettleDetail settleDetail = settleDetails.get(settleDetails.size() - 1);
		String description = settleDetail.getDescription();
		if (null == description || "".equals(description)) {
			settleDetail.setDescription("扎鸟");
		} else {
			settleDetail.setDescription(description + ",扎鸟");
		}
	}
	
	private int getScore(List<CardShape> shapes) {
		int multiply = 0;
		for (CardShape cardShape : shapes) {
			multiply += cardShape.getCount();
		}
		int score = 1;
		for (int i = 0; i < multiply; i++) {
			if (i == 0) {
				score = 3;
			} else {
				score *= 2;
			}
		}
		return score;
	}
	
	/**
	 * 点炮
	 * @param seats
	 */
	private void winCardOther(List<Seat> seats) {
		Room room = seats.get(0).getRoom();
		int lastPosition = room.getLastPosition();
		Seat lastSeat = getSeat(room, lastPosition);
		List<Seat> seatList = room.getSeatList();
		Integer lastCard = room.getLastCard();
		int totalScore = 0;
		PlayWay playWay = room.getPlayWay();
		for (Seat seat : seats) {
			JudgerParam judgerParam = getJudgerParam(seat);
			judgerParam.setCard(lastCard);
			WinJudger winJudger = new WinJudger();
			List<CardShape> shapes = shapeParser.parse(winJudger, judgerParam);
			int score = getScore(shapes);
			if (playWay.isZhaniao()) {
				score *= 2;
			}
			totalScore += score;
			StringBuilder buf = new StringBuilder();
			for (CardShape cardShape : shapes) {
				buf.append("," + cardShape.getName());
			}
			if (seats.size() > 1) {
				buf.append(",一炮多响");
				room.setBankerPosition(lastSeat.getPosition());
			} else {
				room.setBankerPosition(seat.getPosition());
			}
			if (buf.length() > 0) {
				buf.deleteCharAt(0);
			}
			SettleInfo settleInfo = seat.getSettleInfo();
			SettleDetail settleDetail = new SettleDetail();
			settleInfo.setWinOtherNum(settleInfo.getWinOtherNum() + 1);
			settleDetail.setWin(true);
			settleDetail.setOffsetScore(score);
			settleInfo.setCurrentScore(settleInfo.getCurrentScore() + score);
			settleDetail.setCurrentScore(settleInfo.getCurrentScore());
			settleInfo.getSettleDetails().add(settleDetail);
			settleDetail.setDescription(buf.toString());
		}
		
		for (Seat seat : seatList) {
			SettleInfo settleInfo = seat.getSettleInfo();
			SettleDetail settleDetail = new SettleDetail();
			int score = 0;
			if (seat.equals(lastSeat)) {
				settleInfo.setPointCannonNum(settleInfo.getPointCannonNum() + 1);
				settleDetail.setDescription("放炮");
				score = - totalScore;
			} else if (seats.contains(seat)) {
				continue;
			} else {
				score = 0;
			}
			settleDetail.setOffsetScore(score);
			settleInfo.setCurrentScore(settleInfo.getCurrentScore() + score);
			settleDetail.setCurrentScore(settleInfo.getCurrentScore());
			settleInfo.getSettleDetails().add(settleDetail);
		}
		for (Seat seat : seats) {
			seat.setCard(room.getLastCard());
		}
		amendMaxScore(room);
		settleExtra(room);
	}
	
	/**
	 * 抢杠胡
	 * @param seats
	 */
	private void winGrabBridge(List<Seat> seats, Seat loseSeat) {
		Room room = seats.get(0).getRoom();
		List<Seat> seatList = room.getSeatList();
		Integer grabBridgeCard = loseSeat.getGrabBridgeCard();
		int totalScore = 0;
		for (Seat seat : seats) {
			JudgerParam judgerParam = getJudgerParam(seat);
			judgerParam.setCard(grabBridgeCard);
			judgerParam.setGrabBridge(true);
			WinJudger winJudger = new WinJudger();
			List<CardShape> shapes = shapeParser.parse(winJudger, judgerParam);
			int score = getScore(shapes);
			totalScore += score;
			StringBuilder buf = new StringBuilder();
			for (CardShape cardShape : shapes) {
				buf.append("," + cardShape.getName());
			}
			if (seats.size() > 1) {
				buf.append(",一炮多响");
				room.setBankerPosition(loseSeat.getPosition());
			} else {
				room.setBankerPosition(seat.getPosition());
			}
			if (buf.length() > 0) {
				buf.deleteCharAt(0);
			}
			SettleInfo settleInfo = seat.getSettleInfo();
			SettleDetail settleDetail = new SettleDetail();
			settleInfo.setWinOtherNum(settleInfo.getWinOtherNum() + 1);
			settleDetail.setWin(true);
			settleDetail.setOffsetScore(score);
			settleInfo.setCurrentScore(settleInfo.getCurrentScore() + score);
			settleDetail.setCurrentScore(settleInfo.getCurrentScore());
			settleInfo.getSettleDetails().add(settleDetail);
			settleDetail.setDescription(buf.toString());
		}
		
		
		for (Seat seat : seatList) {
			SettleInfo settleInfo = seat.getSettleInfo();
			SettleDetail settleDetail = new SettleDetail();
			int score = 0;
			if (seat.equals(loseSeat)) {
				settleInfo.setPointCannonNum(settleInfo.getPointCannonNum() + 1);
				settleDetail.setDescription("放炮");
				score = - totalScore;
			} else if (seats.contains(seat)) {
				continue;
			} else {
				score = 0;
			}
			settleDetail.setOffsetScore(score);
			settleInfo.setCurrentScore(settleInfo.getCurrentScore() + score);
			settleDetail.setCurrentScore(settleInfo.getCurrentScore());
			settleInfo.getSettleDetails().add(settleDetail);
		}
		for (Seat seat : seats) {
			seat.setCard(grabBridgeCard);
		}
		amendMaxScore(room);
		settleExtra(room);
	}
	
	/**
	 * 从一个集合中移除几张牌
	 * @param cardList
	 * @param card
	 * @param count
	 */
	private void removeCard(List<Integer> cardList, Integer card, int count) {
		int num = 0;
		Iterator<Integer> iter = cardList.iterator();
		while(iter.hasNext()) {
			Integer pai = iter.next();
			if (pai.equals(card)) {
				iter.remove();
				num++;
			}
			if (num == count) {
				return;
			}
		}
		throw new IllegalArgumentException("删除失败！");
	}
	
	/**
	 * 明杠
	 * @param seat
	 * @param card
	 */
	private void showBridgeCard(Seat seat, Integer card) {
		SettleInfo settleInfo = seat.getSettleInfo();
		settleInfo.setShowBridgeNum(settleInfo.getShowBridgeNum() + 1);
		List<Integer> cardList = seat.getCardList();
		removeCard(cardList, card, 3);
		seat.getShowBridgeList().add(card);
		Room room = seat.getRoom();
		int lastPosition = room.getLastPosition();
		Seat lastSeat = getSeat(room, lastPosition);
		List<Integer> outList = lastSeat.getOutList();
		outList.remove(outList.size() - 1);
	}
	
	/**
	 * 暗杠
	 * @param seat
	 * @param card
	 */
	private void hideBridgeCard(Seat seat, Integer card) {
		SettleInfo settleInfo = seat.getSettleInfo();
		settleInfo.setHideBridgeNum(settleInfo.getHideBridgeNum() + 1);
		List<Integer> cardList = seat.getCardList();
		Integer myCard = seat.getCard();
		if (myCard.equals(card)) {
			removeCard(cardList, card, 3);
			seat.setCard(null);
		} else {
			removeCard(cardList, card, 4);
			cardList.add(myCard);
			seat.setCard(null);
		}
		seat.getHideBridgeList().add(card);
	}
	
	/**
	 * 过路杠
	 * @param seat
	 * @param card
	 */
	private void passBridgeCard(Seat seat, Integer card) {
		SettleInfo settleInfo = seat.getSettleInfo();
		settleInfo.setShowBridgeNum(settleInfo.getShowBridgeNum() + 1);
		Integer myCard = seat.getCard();
//		if (myCard.equals(card)) {
//			seat.setCard(null);
//		} else {
//			List<Integer> cardList = seat.getCardList();
//			removeCard(cardList, card, 1);
//			cardList.add(myCard);
//			seat.setCard(null);
//		}
		if (!myCard.equals(card)) {
			throw new UselessRequestException();
		}
		seat.setCard(null);
		List<Integer> bumpList = seat.getBumpList();
		removeCard(bumpList, card, 1);
		seat.getPassBridgeList().add(card);
	}
	
	/**
	 * 结算海底
	 * @param seat
	 * @param shapes
	 */
	private void winCardSeafloor(Seat seat, List<CardShape> shapes) {
		int score = getScore(shapes);
		StringBuilder buf = new StringBuilder();
		for (CardShape cardShape : shapes) {
			buf.append(",");
			buf.append(cardShape.getName());
		}
		if (buf.length() > 0) {
			buf.deleteCharAt(0);
		}
		Room room = seat.getRoom();
		room.setBankerPosition(seat.getPosition());
		List<Seat> seatList = room.getSeatList();
		for (Seat pseat : seatList) {
			SettleInfo settleInfo = pseat.getSettleInfo();
			SettleDetail settleDetail = new SettleDetail();
			int offsetScore = 0;
			if (seat.equals(pseat)) {
				settleInfo.setWinSelfNum(settleInfo.getWinSelfNum() + 1);
				offsetScore = (seatList.size() - 1) * score;
				settleDetail.setDescription(buf.toString());
				settleDetail.setWin(true);
			} else {
				offsetScore = -1 * score;
			}
			settleDetail.setOffsetScore(offsetScore);
			settleInfo.setCurrentScore(settleInfo.getCurrentScore() + offsetScore);
			settleDetail.setCurrentScore(settleInfo.getCurrentScore());
			settleInfo.getSettleDetails().add(settleDetail);
		}
		amendMaxScore(room);
		settleExtra(room);
	}
	
	
	/**
	 * 结算流局
	 * @param room
	 */
	private void winFlowRound(Room room) {
		List<Seat> seatList = room.getSeatList();
		for (Seat pseat : seatList) {
			SettleInfo settleInfo = pseat.getSettleInfo();
			SettleDetail settleDetail = new SettleDetail();
			settleInfo.getSettleDetails().add(settleDetail);
		}
		settleExtra(room);
	}
	
	/**
	 * 修正最大分数上限
	 * @param room
	 */
	private void amendMaxScore(Room room) {
		PlayWay playWay = room.getPlayWay();
		if (!playWay.isHasMultipleMax()) {
			return;
		}
		List<Seat> seatList = room.getSeatList();
		List<Seat> winSeats = new LinkedList<>();
		for (Seat seat : seatList) {
			SettleInfo settleInfo = seat.getSettleInfo();
			List<SettleDetail> settleDetails = settleInfo.getSettleDetails();
			SettleDetail settleDetail = settleDetails.get(settleDetails.size() - 1);
			if (settleDetail.isWin()) {
				winSeats.add(seat);
			}
		}
		if (winSeats.size() == 1) {
			amendMaxScoreSingle(room, winSeats.get(0));
		} else if (winSeats.size() > 1) {
			amendMaxScoreMany(room, winSeats);
		}
	}
	
	/**
	 * 修正最大分数， 一炮多响
	 * @param room
	 * @param winSeats
	 */
	private void amendMaxScoreMany(Room room, List<Seat> winSeats) {
		int overflow = 0;
		for (Seat seat : winSeats) {
			SettleInfo settleInfo = seat.getSettleInfo();
			List<SettleDetail> settleDetails = settleInfo.getSettleDetails();
			SettleDetail settleDetail = settleDetails.get(settleDetails.size() - 1);
			int offsetScore = settleDetail.getOffsetScore();
			if (offsetScore > 24) {
				int over = offsetScore - 24;
				settleDetail.setOffsetScore(24);
				settleInfo.setCurrentScore(settleInfo.getCurrentScore() - over);
				settleDetail.setCurrentScore(settleInfo.getCurrentScore());
				overflow += over;
			}
		}
		List<Seat> seatList = room.getSeatList();
		for (Seat seat : seatList) {
			SettleInfo settleInfo = seat.getSettleInfo();
			List<SettleDetail> settleDetails = settleInfo.getSettleDetails();
			SettleDetail settleDetail = settleDetails.get(settleDetails.size() - 1);
			int offsetScore = settleDetail.getOffsetScore();
			if (offsetScore < 0) {
				settleDetail.setOffsetScore(offsetScore + overflow);
				settleInfo.setCurrentScore(settleInfo.getCurrentScore() + overflow);
				settleDetail.setCurrentScore(settleInfo.getCurrentScore());
				break;
			}
		}
	}
	
	/**
	 * 修正最大分数， 非一炮多响
	 * @param room
	 * @param seat
	 */
	private void amendMaxScoreSingle(Room room, Seat winSeat) {
		List<Seat> seatList = room.getSeatList();
		int overflow = 0;
		for (Seat seat : seatList) {
			SettleInfo settleInfo = seat.getSettleInfo();
			List<SettleDetail> settleDetails = settleInfo.getSettleDetails();
			SettleDetail settleDetail = settleDetails.get(settleDetails.size() - 1);
			if (settleDetail.isWin()) {
				continue;
			}
			int offsetScore = settleDetail.getOffsetScore();
			if (offsetScore < -24) {
				int over = -24 - offsetScore;
				settleDetail.setOffsetScore(-24); 
				settleInfo.setCurrentScore(settleInfo.getCurrentScore() + over);
				settleDetail.setCurrentScore(settleInfo.getCurrentScore());
				overflow += over;
			}
		}
		if (overflow > 0) {
			SettleInfo settleInfo = winSeat.getSettleInfo();
			List<SettleDetail> settleDetails = settleInfo.getSettleDetails();
			SettleDetail settleDetail = settleDetails.get(settleDetails.size() - 1);
			settleDetail.setOffsetScore(settleDetail.getOffsetScore() - overflow);
			settleInfo.setCurrentScore(settleInfo.getCurrentScore() - overflow);
			settleDetail.setCurrentScore(settleInfo.getCurrentScore());
		}
	}
	
	/**
	 * 结算额外的分数
	 * @param room
	 */
	private void settleExtra(Room room) {
		List<Seat> seatList = room.getSeatList();
		for (Seat seat : seatList) {
			SettleInfo settleInfo = seat.getSettleInfo();
			List<SettleDetail> settleDetails = settleInfo.getSettleDetails();
			SettleDetail settleDetail = settleDetails.get(settleDetails.size() - 1);
			int extraScore = getExtraScore(seat);
			String extraDiscription = getExtraDiscription(seat);
			String description = settleDetail.getDescription();
			settleDetail.setWinScroe(settleDetail.getOffsetScore());
			settleDetail.setBridgeScore(extraScore);
			settleDetail.setOffsetScore(settleDetail.getOffsetScore() + extraScore);
			settleInfo.setCurrentScore(settleInfo.getCurrentScore() + extraScore);
			settleDetail.setCurrentScore(settleInfo.getCurrentScore());
			StringBuffer buf = new StringBuffer();
			if (!"".equals(extraDiscription)) {
				buf.append(extraDiscription);
			}
			if (null != description && !"".equals(description)) {
				if (buf.length() != 0) {
					buf.append(",");
				}
				buf.append(description);
			}
			settleDetail.setDescription(buf.toString());
		}
	}
	
	/**
	 * 获取额外的得分，杠，一字撬报喜
	 * @param seat
	 * @return
	 */
	private int getExtraScore(Seat seat) {
		Room room = seat.getRoom();
		PlayWay playWay = room.getPlayWay();
		List<Seat> seatList = room.getSeatList();
		int score = 0;
		for (Seat pseat : seatList) {
			List<Integer> showBridgeList = pseat.getShowBridgeList();
			List<Integer> hideBridgeList = pseat.getHideBridgeList();
			List<Integer> passBridgeList = pseat.getPassBridgeList();
			if (pseat.equals(seat)) {
				score += showBridgeList.size() * 3;
				score += hideBridgeList.size() * 6;
				score += passBridgeList.size() * 3;
				score -= pseat.getSendBridgeNum() * 3;
				if (playWay.isYiziqiao() && pseat.getCardList().size() == 1) {
					score += 3;
				}
			} else {
				score -= hideBridgeList.size() * 2;
				score -= passBridgeList.size() * 1;
				if (playWay.isYiziqiao() && pseat.getCardList().size() == 1) {
					score -= 1;
				}
			}
		}
		return score;
	}
	
	/**
	 * 获取额外的结算描述，杠，一字撬报喜
	 * @param seat
	 * @return
	 */
	private String getExtraDiscription(Seat seat) {
		Room room = seat.getRoom();
		PlayWay playWay = room.getPlayWay();
		StringBuilder buf = new StringBuilder();
		List<Integer> showBridgeList = seat.getShowBridgeList();
		List<Integer> hideBridgeList = seat.getHideBridgeList();
		List<Integer> passBridgeList = seat.getPassBridgeList();
		int sendBridgeNum = seat.getSendBridgeNum();
		if (!showBridgeList.isEmpty()) {
			buf.append(",明杠x");
			buf.append(showBridgeList.size());
		}
		if (!hideBridgeList.isEmpty()) {
			buf.append(",暗杠x");
			buf.append(hideBridgeList.size());
		}
		if (!passBridgeList.isEmpty()) {
			buf.append(",过路杠x");
			buf.append(passBridgeList.size());
		}
		if (sendBridgeNum != 0) {
			buf.append(",放杠x");
			buf.append(sendBridgeNum);
		}
		if (playWay.isYiziqiao() && seat.getCardList().size() == 1) {
			buf.append(",报喜");
		}
		if (buf.length() > 0) {
			buf.deleteCharAt(0);
		}
		return buf.toString();
	}
	
	private JudgerParam getJudgerParam(Seat seat) {
		Room room = seat.getRoom();
		JudgerParam judgerParam = new JudgerParam();
		judgerParam.setSkyWin(room.isSkyWin());
		judgerParam.setBridgeWin(seat.isJustBridge());
		judgerParam.setBumpList(seat.getBumpList());
		judgerParam.setCardList(seat.getCardList());
		judgerParam.setHideBridgeList(seat.getHideBridgeList());
		judgerParam.setPassBridgeList(seat.getPassBridgeList());
		judgerParam.setPlayWay(room.getPlayWay());
		judgerParam.setShowBridgeList(seat.getShowBridgeList());
		if (!seat.getHearList().isEmpty()) {
			judgerParam.setHear(true);
		}
		return judgerParam;
	}
	
}
