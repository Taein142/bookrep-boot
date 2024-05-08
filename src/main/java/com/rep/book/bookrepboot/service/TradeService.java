package com.rep.book.bookrepboot.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.rep.book.bookrepboot.dao.BookDao;
import com.rep.book.bookrepboot.dao.BookTradeDao;
import com.rep.book.bookrepboot.dao.TradeDao;
import com.rep.book.bookrepboot.dao.TradeMsgDao;
import com.rep.book.bookrepboot.dao.UserDao;
import com.rep.book.bookrepboot.dto.*;
import com.rep.book.bookrepboot.util.KakaoApiUtil;
import com.rep.book.bookrepboot.util.MainUtil;
import com.rep.book.bookrepboot.util.kakao.KakaoDirections;
import com.rep.book.bookrepboot.vrp.VrpResult;
import com.rep.book.bookrepboot.vrp.VrpService;
import com.rep.book.bookrepboot.vrp.VrpVehicleRoute;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class TradeService {
    @Autowired
    TradeDao tradeDao;
    @Autowired
    BookDao bookDao;
    @Autowired
    private TradeMsgDao tradeMsgDao;
    @Autowired
    private PlatformTransactionManager manager;
    @Autowired
    private TransactionDefinition definition;
    @Autowired
    private BookTradeDao bookTradeDao;

    @Autowired
    private UserDao userDao;


    // 유저 이메일에 따라 교환중인 리스트를 가져오는 메서드(trade 테이블)
    public List<PageDTO> getTradeListByEmail(String email) {
        log.info("getTradeListByEmail()");
        List<TradeDTO> tradeDTOList = tradeDao.getTradeListByEmail(email);
        List<Object> tradeList = new ArrayList<>();
        for (TradeDTO tradeDTO : tradeDTOList) {
            Map<String, Object> map = new HashMap<>();
            map.put("tradeInfo", tradeDTO);
            BookDTO firstBook = bookDao.getBookByIsbn(tradeDTO.getFir_book_isbn());
            BookDTO secondBook = bookDao.getBookByIsbn(tradeDTO.getSec_book_isbn());
            map.put("firstBook", firstBook);
            map.put("secondBook", secondBook);
            tradeList.add(map);
        }
        log.info("tradeList {}", tradeList);
        return MainUtil.setPaging(tradeList, 10);
    }

    // 교환 신청 보내는 서비스 메서드
    public String saveTradeMsg(MsgDTO msgDTO, RedirectAttributes rttr) {
        log.info("saveTradMsg() - service");
        String msg = null;
        String view = null;

        try {
            tradeMsgDao.setTradeMsg(msgDTO);
            msg = "신청 메시지 전송 완료";
            view = "redirect:my-share";
            bookTradeDao.updateBookTradeStatus(msgDTO.getBook_trade_id());
            log.info("bookTrade 상태 수정 완료");
        } catch (Exception e) {
            e.printStackTrace();
            msg = "신청 메시지 전송 실패";
            view = "redirect:share-house";
        }
        rttr.addFlashAttribute("msg", msg);

        return view;
    }

    // id를 참고하여 교환 메시지 정보를 가져오는 메서드
    public MsgDTO getMsgByID(Long msgId) {
        log.info("getMsgById()");
        MsgDTO msgDTO = tradeMsgDao.getMsgById(msgId);
        log.info("msgDTO {}", msgDTO);

        return msgDTO;
    }

    // msg status 업테이트 메서드
    // 0: 메시지만 보낸 상태  1: 수락  2: 거절  3: 취소
    public boolean updateTradeMsgStatus(Long msgId, String userEmail, int status) throws Exception {
        log.info("updateTradeMsgStatus()");
        TransactionStatus transactionStatus = manager.getTransaction(definition);

        MsgDTO msgDTO = tradeMsgDao.getMsgById(msgId);
        boolean result = false;

        if (msgDTO != null && (msgDTO.getReceived_user_email().equals(userEmail) || msgDTO.getSent_user_email().equals(userEmail))) {
            try {
                Map<String, Object> map = new HashMap<>();
                map.put("msgId", msgId);
                map.put("status", status);
                result = tradeMsgDao.updateMsgStatus(map);
                log.info("update msg_status");

                if (status == 1) { // 승낙하였다면 trade 테이블에 정보 저장
                    TradeDTO tradeDTO = new TradeDTO();
                    tradeDTO.setFir_user_email(msgDTO.getSent_user_email());
                    tradeDTO.setSec_user_email(msgDTO.getReceived_user_email());
                    tradeDTO.setFir_book_isbn(msgDTO.getSent_book_isbn());
                    tradeDTO.setSec_book_isbn(msgDTO.getReceived_book_isbn());
                    tradeDTO.setTrade_status(0);
                    tradeDao.setTrade(tradeDTO);
                }
                manager.commit(transactionStatus); // 상태 변경, trade 저장 둘 다 성공하면 진행되도록 함
            } catch (Exception e) {
                e.printStackTrace();
                manager.rollback(transactionStatus); // 둘 중 하나라도 안되면 롤백
                result = false;
            }
        }
        try (AutoCloseableExecutorService executorService = new AutoCloseableExecutorService(10)) {
            executorService.getExecutorService().submit(() -> {
                try {
                    packageTradesForDeliver(1);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        return result;
    }

    private void packageTradesForDeliver(int quantity) throws IOException, InterruptedException {
        log.info("packageTradesForDeliver()");
        Long lastId = tradeDao.getLastId();
        if (lastId % quantity != 0) {
            return;
        }
        List<TradeDTO> tradeList = tradeDao.getTradeRemainder();
        System.out.println("tradeList=" + tradeList);
        VrpService vrpService = new VrpService();

        NodeDTO start = new NodeDTO();
        start.setEmail("출발지");
        start.setX(126.711779438);
        start.setY(37.413179094);
        start.setAddress("104동 1701호");

        vrpService.addVehicle("대충 차량", start.getEmail());

        Map<String, NodeDTO> nodeMap = new HashMap<>();
        nodeMap.put(start.getEmail(), start);
        Map<String, Map<String, NodeCostDTO>> nodeCostMap = new HashMap<>();
        List<NodeDTO> nodeList = new ArrayList<>();
        nodeList.add(start);
        for (TradeDTO trade : tradeList) {
            UserDTO firstUser = userDao.getFirstUser(trade.getFir_user_email());
            NodeDTO firstNode = new NodeDTO();
            firstNode.setEmail(firstUser.getEmail());
            firstNode.setX(firstUser.getLongitude());
            firstNode.setY(firstUser.getLatitude());
            firstNode.setAddress(firstUser.getDetail());

            UserDTO secondUser = userDao.getSecondUser(trade.getSec_user_email());
            NodeDTO secondNode = new NodeDTO();
            secondNode.setEmail(secondUser.getEmail());
            secondNode.setX(secondUser.getLongitude());
            secondNode.setY(secondUser.getLatitude());
            secondNode.setAddress(secondUser.getDetail());

            String shipmentNameFir = firstNode.getEmail() + "_" + secondNode.getEmail();
            String shipmentNameSec = secondNode.getEmail() + "_" + firstNode.getEmail();
            vrpService.addShipement(shipmentNameFir, firstNode.getEmail(), secondNode.getEmail());
            vrpService.addShipement(shipmentNameSec, secondNode.getEmail(), firstNode.getEmail());

            nodeMap.put(firstNode.getEmail(), firstNode);
            nodeMap.put(secondNode.getEmail(), secondNode);

            nodeList.add(firstNode);
            nodeList.add(secondNode);
        }


        for (int i = 0; i < nodeList.size(); i++) {
            NodeDTO startNode = nodeList.get(i);
            for (int j = 0; j < nodeList.size(); j++) {
                NodeDTO endNode = nodeList.get(j);
                // 출발지와 도착지 노드 사이의 비용을 계산
                NodeCostDTO nodeCost = getNodeCost(startNode, endNode);
                // 동일한 노드 쌍에 대해서는 비용 계산을 수행하지 않고 넘어감.
                if (i == j) {
                    continue;
                }
                if (nodeCost == null) {
                    // 출발지와 도착지 사이에 경로가 없는 경우, 기본값으로 거리와 시간을 0l으로 설정
                    nodeCost = new NodeCostDTO();
                    nodeCost.setDistanceMeter(0l);
                    nodeCost.setDurationSecond(0l);
                }
                Long distanceMeter = nodeCost.getDistanceMeter(); // 계산된 비용중 거리를 가져옴
                log.info("distanceMeter={}", distanceMeter);
                Long durationSecond = nodeCost.getDurationSecond(); // 소요시간을 가져옴
                log.info("durationSecond={}", durationSecond);
                String startNodeId = String.valueOf(startNode.getEmail()); // 출발지 노드의 id를 문자열로 변환
                String endNodeId = String.valueOf(endNode.getEmail()); // 도착지 노드의 id를 문자열로 변환
                // 비용 등록
                vrpService.addCost(startNodeId, endNodeId, durationSecond, distanceMeter);
                // 출발지 노드가 nodeCostMap에 없는 경우, 출발지와 연결된 도착지 노드를 저장할 맵을 생성
                if (!nodeCostMap.containsKey(startNodeId)) {
                    nodeCostMap.put(startNodeId, new HashMap<>());
                }
                // 출발지와 도착지 노드 간의 비용을 nodeCostMap에 저장
                nodeCostMap.get(startNodeId).put(endNodeId, nodeCost);
            }
        }
        List<NodeDTO> vrpNodeList = new ArrayList<>();
        // vrp 서비스를 통해 결과를 가져옴
        VrpResult vrpResult = vrpService.getVrpResult();
        // vrp 결과에서 각 차량 경로 반복
        for (VrpVehicleRoute vrpVehicleRoute : vrpResult.getVrpVehicleRouteList()) {
            // 해당 경로가 화물을 나타내는지 확인
            if ("deliverShipment".equals(vrpVehicleRoute.getActivityName())) {
                // 해당 경로의 위치 id 가져옴
                String locationId = vrpVehicleRoute.getLocationId();
                // 위치 ID를 사용하여 노드 맵에서 해당 노드를 가져와 VRP 노드 리스트에 추가
                vrpNodeList.add(nodeMap.get(locationId));
            }
            System.out.println(vrpVehicleRoute);
        }
        int totalDistance = 0; // 총 거리를 담을 변수
        int totalDuration = 0; // 총 소요시간을 담을 변수
        List<KakaoApiUtil.Point> totalPathPointList = new ArrayList<>();
        for (int i = 1; i < vrpNodeList.size(); i++) {
            NodeDTO prev = vrpNodeList.get(i - 1);
            NodeDTO next = vrpNodeList.get(i);
            // 이전 노드에서 현재 노드로 이동하는 데 필요한 비용 정보를 가져옴.
            NodeCostDTO nodeCost = nodeCostMap.get(String.valueOf(prev.getEmail())).get(String.valueOf(next.getEmail()));
            // 만약 비용 정보가 없으면 다음 반복으로 넘어감.
            if (nodeCost == null) {
                continue;
            }
            totalDistance += nodeCost.getDistanceMeter(); // 이동거리 누적
            totalDuration += nodeCost.getDurationSecond(); //소요시간 누적
            String pathJson = nodeCost.getPathJson(); // 경로 문자열을 가져옴.

            // 경로 JSON을 파싱하여 경로 포인트 리스트에 추가
            if (pathJson != null) {
                totalPathPointList.addAll(new ObjectMapper().readValue(pathJson, new TypeReference<List<KakaoApiUtil.Point>>() {
                }));
            }
        }
        String totalJson = new Gson().toJson(totalPathPointList);
        String tradeJson = new Gson().toJson(tradeList);
        System.out.println("totalJson=" + totalJson);
        System.out.println("tradeJson=" + tradeJson);
        if (totalJson != null && tradeJson != null) {
            PathDTO pathDTO = new PathDTO();
            pathDTO.setTrade_json(tradeJson);
            pathDTO.setPath_json(totalJson);
            pathDTO.setTotal_distance(totalDistance);
            pathDTO.setTotal_duration(totalDuration);

            tradeDao.insertPath(pathDTO);
        }
        for (TradeDTO tradeDTO : tradeList) {
            tradeDTO.setTrade_status(1);
            tradeDao.updateTrade(tradeDTO);
        }
    }

    private NodeCostDTO getNodeCost(NodeDTO prev, NodeDTO next) throws IOException, InterruptedException {
        NodeCostDTO nodeCostDTO = new NodeCostDTO();
        nodeCostDTO.setStartNodeId(prev.getEmail());
        nodeCostDTO.setEndNodeId(next.getEmail());
        NodeCostDTO nodeCost = tradeDao.getOneByParam(nodeCostDTO);
        if (nodeCost == null) {//카카오 API를 사용하여 두 지점 간의 경로를 가져옴
            KakaoDirections kakaoDirections = KakaoApiUtil.getKakaoDirections(new KakaoApiUtil.Point(prev.getX(), prev.getY()),
                    new KakaoApiUtil.Point(next.getX(), next.getY()));
            // 경로 목록을 가져옴
            List<KakaoDirections.Route> routes = kakaoDirections.getRoutes();
            // 첫번째 경로 선택(지금은 연습이라 단일경로지만, 프로젝트에서는 복수의 경로로 해야 됨)
            KakaoDirections.Route route = routes.get(0);
            // 경로를 저장할 리스트를 생성
            List<KakaoApiUtil.Point> pathPointList = new ArrayList<KakaoApiUtil.Point>();
            // 경로의 섹션 목록 가져옴
            List<KakaoDirections.Route.Section> sections = route.getSections();
            if (sections == null) { // 섹션이 없는 경우 경로가 직선으로 구성된 것이므로 예외 처리함
                // {"trans_id":"018e3d7f7526771d9332cb717909be8f","routes":[{"result_code":104,"result_msg":"출발지와
                // 도착지가 5 m 이내로 설정된 경우 경로를 탐색할 수 없음"}]}
                pathPointList.add(new KakaoApiUtil.Point(prev.getX(), prev.getY())); // 경로에 시작지점 추가
                pathPointList.add(new KakaoApiUtil.Point(next.getX(), next.getY())); // 도착지점 추가
                nodeCost = new NodeCostDTO(); // NodeCost를 생성하고 설정한 후에는, 반환하기 전에 먼저 DB에 추가
                nodeCost.setStartNodeId(prev.getEmail());// 시작노드id
                nodeCost.setEndNodeId(next.getEmail());// 종료노드id
                nodeCost.setDistanceMeter(0l);// 이동거리(미터)
                nodeCost.setDurationSecond(0l);// 이동시간(초)
                nodeCost.setTollFare(0);// 통행 요금(톨게이트)
                nodeCost.setTaxiFare(0);// 택시 요금(지자체별, 심야, 시경계, 복합, 콜비 감안)
                nodeCost.setPathJson(new ObjectMapper().writeValueAsString(pathPointList));// 이동경로json [[x,y],[x,y]]
                nodeCost.setRegDt(new Date());// 등록일시
                nodeCost.setModDt(new Date());// 수정일시
                tradeDao.add(nodeCost); // 여기가 DB에 추가하는 부분
                return null;
            }

            // 첫 번째 섹션의 도로 목록을 가져옴
            List<KakaoDirections.Route.Section.Road> roads = sections.get(0).getRoads();
            for (KakaoDirections.Route.Section.Road road : roads) {
                // 도로의 좌표 목록을 가져옴.
                List<Double> vertexes = road.getVertexes();
                for (int q = 0; q < vertexes.size(); q++) {
                    pathPointList.add(new KakaoApiUtil.Point(vertexes.get(q), vertexes.get(++q)));
                }
            }
            // 요약정보 객체 생성하고 거리, 소요시간, 요금, 택시요금, 톨비를 가져와 담음.
            KakaoDirections.Route.Summary summary = route.getSummary();
            Integer distance = summary.getDistance();
            Integer duration = summary.getDuration();
            KakaoDirections.Route.Summary.Fare fare = summary.getFare();
            Integer taxi = fare.getTaxi();
            Integer toll = fare.getToll();

            // NodeCost를 생성하고 이동 비용과 경로 정보를 설정
            nodeCost = new NodeCostDTO();
            nodeCost.setStartNodeId(prev.getEmail());// 시작노드id
            nodeCost.setEndNodeId(next.getEmail());// 종료노드id
            nodeCost.setDistanceMeter(distance.longValue());// 이동거리(미터)
            nodeCost.setDurationSecond(duration.longValue());// 이동시간(초)
            nodeCost.setTollFare(toll);// 통행 요금(톨게이트)
            nodeCost.setTaxiFare(taxi);// 택시 요금(지자체별, 심야, 시경계, 복합, 콜비 감안)
            nodeCost.setPathJson(new ObjectMapper().writeValueAsString(pathPointList));// 이동경로json [[x,y],[x,y]]
            nodeCost.setRegDt(new Date());// 등록일시
            nodeCost.setModDt(new Date());// 수정일시
            tradeDao.add(nodeCost); // DB에 추가
        }
        return nodeCost;
    }

    // 책 정보를 조합하여 리턴하는 메서드(리스트)
    public List<Object> addBookInfoList(List<MsgDTO> msglist) {
        log.info("addBookInfoList()");
        List<Object> mList = new ArrayList<>();

        try {
            for (MsgDTO msgDTO : msglist) {
                Map<String, Object> map = new HashMap<>();
                BookDTO book = bookDao.getBookByIsbn(msgDTO.getReceived_book_isbn());
                map.put("msgId", msgDTO.getMsg_id());
                map.put("book", book);
                mList.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mList;
    }

    // 책 정보를 조합하여 리턴하는 메서드(dto)
    public Map<String, Object> addBookInfo(MsgDTO msgDTO) {
        log.info("addBookInfo");
        Map<String, Object> resultMap = new HashMap<>();

        try {
            BookDTO sentBook = bookDao.getBookByIsbn(msgDTO.getSent_book_isbn());
            BookDTO receivedBook = bookDao.getBookByIsbn(msgDTO.getReceived_book_isbn());
            resultMap.put("msgDTO", msgDTO);
            resultMap.put("sentBook", sentBook);
            resultMap.put("receivedBook", receivedBook);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultMap;
    }
}
