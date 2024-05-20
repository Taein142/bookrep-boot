package com.rep.book.bookrepboot.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rep.book.bookrepboot.APIKEY;
import com.rep.book.bookrepboot.dao.AdminDao;
import com.rep.book.bookrepboot.dao.BookDao;
import com.rep.book.bookrepboot.dao.PathDao;
import com.rep.book.bookrepboot.dao.UserDao;
import com.rep.book.bookrepboot.dto.*;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SuperAdminService {
    @Autowired
    private BookDao bookDao;

    @Setter(onMethod_ = {@Autowired})
    private UserDao userDao;

    @Autowired
    private PathDao pathDao;

    @Autowired
    private AdminDao adminDao;

    public List<UserDTO> getUserToSuperAdmin() {
        log.info("getUserToSuperAdmin()");

        return userDao.getUserToSuperAdmin();
    }

    public void getPathToSuperAdmin(Model model) {
        log.info("getPathToSuperAdmin()");

        List<PathDTO> pathList = pathDao.getPathToSuperAdmin();
        model.addAttribute("pathList", pathList);
    }

    public void matchDriver(Long pathId, Long adminId) {
        log.info("matchDriver()");

        PathDTO pathDTO = new PathDTO();
        pathDTO.setPath_id(pathId);
        pathDTO.setDeliver_admin_id(adminId);


        pathDao.matchDriver(pathDTO);

        adminDao.appointDriver(adminId);
    }

    public void getPathDetail(Long pathId, Model model) throws IOException, ParserConfigurationException, InterruptedException {
        log.info("getPathDetail()");

        PathDTO pathDTO = pathDao.getPathDetail(pathId);
        log.info(pathDTO.toString());
        Map<String, DeliveryDTO> deliveryMap = packageDeliveryMap(pathDTO);

        model.addAttribute("path", pathDTO);
        model.addAttribute("deliveryMap", deliveryMap);
    }

    public Map<String, DeliveryDTO> packageDeliveryMap(PathDTO pathDTO) throws IOException, ParserConfigurationException, InterruptedException {
        log.info("packageDeliveryMap()");

        Type tradeListType = new TypeToken<List<TradeDTO>>(){}.getType();
        List<TradeDTO> tradeList = new Gson().fromJson(pathDTO.getTrade_json(), tradeListType);
        Map<String, DeliveryDTO> deliveryMap = new HashMap<>();

        if (tradeList != null){
            for (TradeDTO tradeDTO : tradeList){
                updateDeliveryMap(deliveryMap, tradeDTO.getFir_user_email(), tradeDTO.getFir_book_isbn(), true);
                updateDeliveryMap(deliveryMap, tradeDTO.getSec_user_email(), tradeDTO.getSec_book_isbn(), false);
            }
        }

        return deliveryMap;
    }

    private DeliveryDTO createDeliveryDTO(String userEmail, double x, double y, String isbn){
        DeliveryDTO deliveryDTO = new DeliveryDTO();
        deliveryDTO.setUserEmail(userEmail);
        deliveryDTO.setX(x);
        deliveryDTO.setY(y);
        deliveryDTO.setPickupList(new ArrayList<>());
        deliveryDTO.setDeliveryList(new ArrayList<>());
        List<String> list = new ArrayList<>();
        list.add(convertIsbnToBookName(isbn));
        return deliveryDTO;
    }

    private void updateDeliveryMap(Map<String, DeliveryDTO> deliveryMap, String userEmail, String isbn, boolean isPickup) throws IOException, ParserConfigurationException, InterruptedException {
        UserDTO userDTO = isPickup ? userDao.getFirstUser(userEmail) : userDao.getSecondUser(userEmail);
        DeliveryDTO deliveryDTO = deliveryMap.getOrDefault(userEmail, createDeliveryDTO(userEmail, userDTO.getLongitude(), userDTO.getLatitude(), isbn));
        List<String> list = isPickup ? deliveryDTO.getPickupList() : deliveryDTO.getDeliveryList();
        list.add(convertIsbnToBookName(isbn));
        deliveryMap.put(userEmail, deliveryDTO);
    }

    public void getAllAdminList(Model model) {
        log.info("getAllAdminList()");

        List<AdminDTO> adminList = adminDao.getAllAdminList();
        List<Long> adminIdList = new ArrayList<>();
        for (AdminDTO adminDTO : adminList){
            adminIdList.add(adminDTO.getAdmin_id());
        }
        model.addAttribute("adminList", adminIdList);
    }

    public void rollbackToAdmin(Long currentAdminId) {
        log.info("rollbackToAdmin()");

        adminDao.rollbackToAdmin(currentAdminId);
    }

    public String convertIsbnToBookName(String isbn){
        return bookDao.convertIsbnToBookName(isbn);
    }
}
