package com.rep.book.bookrepboot.service;

import com.rep.book.bookrepboot.dao.TradeMsgDao;
import com.rep.book.bookrepboot.dto.MsgDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
@Slf4j
public class TradeMsgService {
    @Autowired
    TradeMsgDao tradeMsgDao;

    public String sendTradeMsg(MsgDTO msgDTO, RedirectAttributes rttr) {
        log.info("sendTradMsg() - service");
        String msg = null;
        String view = null;

        try{
            tradeMsgDao.setTradeMsg(msgDTO);
            msg = "신청 메시지 전송 완료";
        }catch (Exception e){
            e.printStackTrace();
            msg = "신청 메시지 전송 실패";
        }
        view = "redirect:share-house";
        rttr.addFlashAttribute("msg", msg);

        return view;
    }
}
