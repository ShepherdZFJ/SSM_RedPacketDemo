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
        //第一种方法：不加锁和加悲观锁
        //int result = userRedPacketService.grapRedPacket(redPacketId, userId);

        //第二种方法：加乐观锁，版本控制
        //int result = userRedPacketService.grapRedPacketForVersion(redPacketId, userId);

        //第三种办法：使用redis抢红包
        Long result = userRedPacketService.grapRedPacketByRedis(redPacketId, userId);
        Map<String,Object> map = new HashMap<>();
        Boolean flag = result > 0;
        map.put("success",flag);
        map.put("message",flag?"抢红包成功":"抢红包失败");
        return map;
    }

}
