package com.rep.book.bookrepboot.service;

import com.google.gson.Gson;
import com.rep.book.bookrepboot.dao.AdminDao;
import com.rep.book.bookrepboot.dao.PathDao;
import com.rep.book.bookrepboot.dao.UserDao;
import com.rep.book.bookrepboot.dto.DeliveryDTO;
import com.rep.book.bookrepboot.dto.PathDTO;
import com.rep.book.bookrepboot.dto.TradeDTO;
import com.rep.book.bookrepboot.dto.UserDTO;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SuperAdminService {

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

    public List<PathDTO> getPathToSuperAdmin() {
        log.info("getPathToSuperAdmin()");

        return pathDao.getPathToSuperAdmin();
    }

    public void matchDriver(Long pathId, Long adminId) {
        log.info("matchDriver()");

        PathDTO pathDTO = new PathDTO();
        pathDTO.setPath_id(pathId);
        pathDTO.setDriver_admin_id(adminId);


        pathDao.matchDriver(pathDTO);

        adminDao.appointDriver(adminId);
    }

    public void getPathDetail(Long pathId, Model model) {
        log.info("getPathDetail()");

        PathDTO pathDTO = pathDao.getPathDetail(pathId);
        List<TradeDTO> tradeList = new Gson().fromJson(pathDTO.getTrade_json(), List.class);
        Map<String, DeliveryDTO> deliveryMap = new HashMap<>();

        if (tradeList != null){
            for (TradeDTO tradeDTO : tradeList){
                updateDeliveryMap(deliveryMap, tradeDTO.getFir_user_email(), tradeDTO.getFir_book_isbn(), true);
                updateDeliveryMap(deliveryMap, tradeDTO.getSec_user_email(), tradeDTO.getSec_book_isbn(), false);
            }
        }

        model.addAttribute("path", pathDTO);
        model.addAttribute("deliveryMap", deliveryMap);
    }

    private DeliveryDTO createDeliveryDTO(String userEmail, double x, double y, String isbn) {
        DeliveryDTO deliveryDTO = new DeliveryDTO();
        deliveryDTO.setUserEmail(userEmail);
        deliveryDTO.setX(x);
        deliveryDTO.setY(y);
        List<String> list = new ArrayList<>();
        list.add(isbn);
        return deliveryDTO;
    }

    private void updateDeliveryMap(Map<String, DeliveryDTO> deliveryMap, String userEmail, String isbn, boolean isPickup) {
        UserDTO userDTO = isPickup ? userDao.getFirstUser(userEmail) : userDao.getSecondUser(userEmail);
        DeliveryDTO deliveryDTO = deliveryMap.getOrDefault(userEmail, createDeliveryDTO(userEmail, userDTO.getLongitude(), userDTO.getLatitude(), isbn));
        List<String> list = isPickup ? deliveryDTO.getPickupList() : deliveryDTO.getDeliveryList();
        list.add(isbn);
        deliveryMap.put(userEmail, deliveryDTO);
    }

}
