package com.rep.book.bookrepboot.service;

import com.google.gson.Gson;
import com.rep.book.bookrepboot.dao.PathDao;
import com.rep.book.bookrepboot.dao.TradeDao;
import com.rep.book.bookrepboot.dto.DeliveryDTO;
import com.rep.book.bookrepboot.dto.PathDTO;
import com.rep.book.bookrepboot.dto.TradeDTO;
import com.rep.book.bookrepboot.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class DriverService {

    @Autowired
    private PathDao pathDao;

    @Autowired
    private TradeDao tradeDao;

    @Autowired
    private SuperAdminService superAdminService;

    public void showDelivery(Model model) {
        log.info("showDelivery()");

        Long adminId = Long.parseLong(Objects.requireNonNull(SecurityUtil.getCurrentUserEmail()));
        log.info("adminId: {}", adminId);

        PathDTO pathDTO = pathDao.getPathByAdminId(adminId);
        Map<String, DeliveryDTO> deliveryMap = superAdminService.packageDeliveryMap(pathDTO);

        model.addAttribute("path", pathDTO);
        model.addAttribute("deliveryMap", deliveryMap);
    }

    public void completeDelivery(Long pathId) {
        log.info("completeDelivery()");
        PathDTO pathDTO = pathDao.getPathDetail(pathId);
        pathDao.completeDelivery(pathId);

        List<TradeDTO> tradeList = new Gson().fromJson(pathDTO.getTrade_json(), List.class);
        for (TradeDTO tradeDTO : tradeList){
            tradeDao.completeTrade(tradeDTO);
        }
    }

}
