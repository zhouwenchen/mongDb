package com.example.demo.controller;import com.example.demo.core.repository.DemoInfoRepository;import com.example.demo.core.util.Pagination;import com.example.demo.entity.DemoInfo;import com.example.demo.util.ChineseName;import net.sf.json.JSONObject;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.data.mongodb.core.query.Criteria;import org.springframework.data.mongodb.core.query.Query;import org.springframework.data.mongodb.core.query.Update;import org.springframework.web.bind.annotation.*;import javax.servlet.http.HttpServletRequest;import java.util.HashMap;import java.util.List;import java.util.Map;import java.util.Random;/** * @Author: jensen * @Description: mongoDb 服务 * @Date: Created 16:45 2018/3/14 */@RequestMapping(value = "/mogondb")@RestControllerpublic class mogondbController {    @Autowired    private DemoInfoRepository demoInfoRepository;    @RequestMapping(value = "/helloWorld")    public String helloWorld(){        return "helloWorld";    }    @RequestMapping("/save")    public String save(){       DemoInfo demoInfo = new DemoInfo();       demoInfo.setName(ChineseName.getRandomName());       demoInfo.setAge(new Random().nextInt(100));       String jsonString = net.sf.json.JSONObject.fromObject(demoInfoRepository.save(demoInfo)).toString();        return jsonString;    }    @RequestMapping("/find")    public Map<String,Object> find(){        Map<String,Object> data = new HashMap<>();        long startTime = System.currentTimeMillis();        List<DemoInfo> list = demoInfoRepository.findAll();        long endTime = System.currentTimeMillis();        long seconds = (endTime-startTime)/1000;        //data.put("data",list);        data.put("size",list.size());        data.put("time",seconds);        return data;    }    @RequestMapping("/findByName")    public Map<String,Object> findByName(HttpServletRequest request){        Map<String,Object> data = new HashMap<>();        String name = request.getParameter("name");        List<DemoInfo> list = demoInfoRepository.findByName(name);        data.put("data",list);        data.put("size",list.size());        return data;    }    /**     *     * @author jensen     * @description 根据Id查询     * @date 2018/3/19 16:36     * @param [request]     * @return java.util.Map<java.lang.String,java.lang.Object>     */    @RequestMapping("/findById")    public Map<String,Object> findById(HttpServletRequest request){        Map<String,Object> data = new HashMap<>();        String id = request.getParameter("id");        DemoInfo list = demoInfoRepository.findById(id);        data.put("data",list);        return data;    }    /**     *     * @author jensen     * @description 根据id删除     * @date 2018/3/19 16:32     * @param [id]     * @return void     */    @RequestMapping("deleteById")    public void  deleteById(@RequestParam(name = "id") String id){        demoInfoRepository.deleteObject(id);    }    @RequestMapping(value = "/getDemoInfoByPage",method =  RequestMethod.GET)    public String getDemoInfoByPage(@RequestParam(name ="name" ,required = false) String name,                                    @RequestParam(name = "pageSize",defaultValue = "10") String pageSize,                                    @RequestParam(name = "pageNum",defaultValue = "1") String pageNum){        Query query = new Query(Criteria.where("name").is(name));        Pagination<DemoInfo> pageDemoInfo = demoInfoRepository.getPage(Integer.valueOf(pageNum),Integer.valueOf(pageSize),query);        return JSONObject.fromObject(pageDemoInfo).toString();    }    /**     *     * @author jensen     * @description 更新     * @date 2018/3/19 16:46     * @param [name, id]     * @return void     */    @RequestMapping(value = "/updateDemoInfoByName")    public Map<String,Object> updateDemoInfoByName(@RequestParam(name ="name") String name,                                                   @RequestParam(name = "id") String id){        Map<String,Object> data = new HashMap<>();        Query query = new Query(Criteria.where("id").is(id));        Update update = Update.update("name",name);        data.put("oldObject",demoInfoRepository.findById(id));        demoInfoRepository.updateFirst(query,update);        data.put("newObject",demoInfoRepository.findById(id));        return data;    }}