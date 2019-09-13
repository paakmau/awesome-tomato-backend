package com.example.ffbackend.bl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.example.ffbackend.da.UserDaService;
import com.example.ffbackend.entity.User;
import com.example.ffbackend.exception.MyRuntimeException;
import com.example.ffbackend.vo.ResponseEnums;
import com.example.ffbackend.vo.UserVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import lombok.var;

@Service
public class UserService {
    @Autowired
    UserDaService userDaService;

    Map<String, Pair<Date, String>> emailCaptchaDict = new HashMap<>();
    Map<String, Pair<Date, String>> phoneCaptchaDict = new HashMap<>();
    Random rand = new Random();

    public boolean InsertUser(UserVo vo) {
        var oriUser = userDaService.GetUserByUsername(vo.getUsername());
        if (oriUser != null)
            throw new MyRuntimeException(ResponseEnums.REPEAT_REGISTER);
        User user = User.fromVo(vo);
        user.setId(null);
        userDaService.InsertUser(user);
        return true;
    }

    public boolean DeleteUser(String username) {
        userDaService.DeleteUser(username);
        return true;
    }

    public boolean UpdateUser(UserVo vo) {
        User user = User.fromVo(vo);
        if (user.getId() == null)
            return false;
        userDaService.UpdateUser(user);
        return true;
    }

    public UserVo GetUserByUsername(String username) {
        var po = userDaService.GetUserByUsername(username);
        if (po != null)
            return po.getVo();
        return null;
    }

    public boolean InsertEmailCaptcha(String email) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++)
            sb.append(rand.nextInt(10));
        emailCaptchaDict.put(email, Pair.of(new Date(), sb.toString()));
        // TODO: 发邮件
        return true;
    }

    public boolean InsertPhoneCaptcha(String phone) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++)
            sb.append(rand.nextInt(10));
        phoneCaptchaDict.put(phone, Pair.of(new Date(), sb.toString()));
        // TODO: 发短信
        return true;
    }
}