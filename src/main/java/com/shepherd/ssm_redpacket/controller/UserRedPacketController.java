package com.shepherd.ssm_redpacket.controller;

import com.shepherd.ssm_redpacket.service.IUserRedPacketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
public class UserRedPacketController {
    @Autowired
    private IUserRedPacketService userRedPacketService = null;

    @RequestMapping("/getRedPacket")
    public ModelAndView getView(){
        ModelAndView mv = new ModelAndView("redPacket");
        return mv;
    }
    @RequestMapping("/grapRedPacket")
    @ResponseBody
    public Map<String ,Object> grapRedPacket(Long redPacketId,Long userId){
        int result = userRedPacketService.grapRedPacket(redPacketId, userId);
        Map<String,Object> map = new HashMap<>();
        Boolean flag = result > 0;
        map.put("success",flag);
        map.put("message",flag?"抢红包成功":"抢红包失败");
        return map;


    }

}
