package com.rep.book.bookrepboot.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.Type;
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

    public void showDelivery(Model model, Long pathId) throws IOException, ParserConfigurationException, InterruptedException {
        log.info("showDelivery()");

        Long adminId = Long.parseLong(Objects.requireNonNull(SecurityUtil.getCurrentUserEmail()));
        log.info("adminId: {}", adminId);

        PathDTO pathDTO = pathDao.getPathDetail(pathId);
        Map<String, DeliveryDTO> deliveryMap = superAdminService.packageDeliveryMap(pathDTO);

        model.addAttribute("path", pathDTO);
        model.addAttribute("deliveryMap", deliveryMap);
    }

    public void completeDelivery(Long pathId) {
        log.info("completeDelivery()");
        PathDTO pathDTO = pathDao.getPathDetail(pathId);
        pathDao.completeDelivery(pathId);

        Type listType = new TypeToken<List<TradeDTO>>(){}.getType();
        List<TradeDTO> tradeList = new Gson().fromJson(pathDTO.getTrade_json(), listType);
        for (TradeDTO tradeDTO : tradeList){
            tradeDao.completeTrade(tradeDTO);
        }
    }

    public void getDeliveryAmount(Model model) {
        log.info("getDeliveryAmount()");

        Long adminId = Long.parseLong(Objects.requireNonNull(SecurityUtil.getCurrentUserEmail()));
        log.info("adminId: {}", adminId);

        List<PathDTO> pathList = pathDao.getPathByAdminId(adminId);
        model.addAttribute("pathList", pathList);
    }
}
