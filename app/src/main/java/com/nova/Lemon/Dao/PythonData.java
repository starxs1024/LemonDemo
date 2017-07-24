package com.nova.Lemon.Dao;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nova.Lemon.bean.Constant;
import com.nova.Lemon.bean.PythonBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Paraselene on 2017/7/19. Email ：15616165649@163.com
 */

public class PythonData {

    private List<PythonBean> ps;

    PythonBean pythonbean = new PythonBean();

    List list = new ArrayList();

    /************************** 获取数据 ***************************************/
    public List<PythonBean> getData() {
        String str = Constant.PYTHON_JSON;
        Gson g = new Gson();
        ps = g.fromJson(str, new TypeToken<List<PythonBean>>() {
        }.getType());
        for (int i = 0; i < ps.size(); i++) {
            PythonBean p = ps.get(i);
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("title", p.getTitle());
//            map.put("time", p.getTime());
//            map.put("videolink", p.getVideolink());
//            map.put("imaglinks", p.getImaglinks());
            list.add(p);
        }
        return list;
    }
}
