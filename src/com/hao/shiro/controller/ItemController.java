package com.hao.shiro.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.hao.shiro.po.ItemsCustom;
import com.hao.shiro.po.ItemsQueryVo;
import com.hao.shiro.service.ItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/item")
public class ItemController {
    //注入service
    @Autowired
    private ItemsService itemsService;

    //单独将商品类型的方法提出来，将方法返回值填充到request，在页面显示
    @ModelAttribute("itemsType")
    public Map<String, String> getItemsType() throws Exception {

        HashMap<String, String> itemsType = new HashMap<String, String>();
        itemsType.put("001", "数码");
        itemsType.put("002", "服装");
        return itemsType;
    }

    //商品信息方法
    @RequestMapping("/queryItems")
    public ModelAndView queryItems(HttpServletRequest request) throws Exception {

        System.out.println(request.getParameter("id"));

        //调用service查询商品列表
        List<ItemsCustom> itemsList = itemsService.findItemsList(null);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("itemsList", itemsList);

        // 指定逻辑视图名
        modelAndView.setViewName("item/itemsList");
        return modelAndView;
    }

    //批量修改商品查询
    @RequestMapping("/editItemsList")
    public ModelAndView editItemsList(HttpServletRequest request) throws Exception {

        System.out.println(request.getParameter("id"));

        //调用service查询商品列表
        List<ItemsCustom> itemsList = itemsService.findItemsList(null);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("itemsList", itemsList);
        // 指定逻辑视图名
        modelAndView.setViewName("item/editItemsList");

        return modelAndView;
    }

    //批量修改商品提交
    @RequestMapping("/editItemsListSubmit")
    public String editItemsListSubmit(ItemsQueryVo itemsQueryVo) throws Exception {

        return "success";
    }

    //方法返回 字符串，字符串就是逻辑视图名，Model作用是将数据填充到request域，在页面展示
    @RequestMapping(value = "/editItems", method = {RequestMethod.GET})
    public String editItems(Model model, Integer id) throws Exception {

        //将id传到页面
        model.addAttribute("id", id);

        //调用 service查询商品信息
        ItemsCustom itemsCustom = itemsService.findItemsById(id);

        model.addAttribute("itemsCustom", itemsCustom);

        //return "editItem_2";
        return "editItem";

    }

    //根据商品id查看商品信息rest接口
    //@RequestMapping中指定restful方式的url中的参数，参数需要用{}包起来
    //@PathVariable将url中的{}包起参数和形参进行绑定
    @RequestMapping("/viewItems/{id}")
    public @ResponseBody
    ItemsCustom viewItems(@PathVariable("id") Integer id) throws Exception {
        //调用 service查询商品信息
        ItemsCustom itemsCustom = itemsService.findItemsById(id);

        return itemsCustom;

    }


    //商品修改提交
    @RequestMapping("/editItemSubmit")
    public String editItemSubmit(Model model, Integer id, @ModelAttribute(value = "itemsCustom") ItemsCustom itemsCustom,
                                 BindingResult bindingResult,
                                 //上传图片
                                 MultipartFile pictureFile
    ) throws Exception {

        //输出校验错误信息
        //如果参数绑定时有错
        if (bindingResult.hasErrors()) {

            //获取错误
            List<ObjectError> errors = bindingResult.getAllErrors();
            //准备在页面输出errors，页面使用jstl遍历
            model.addAttribute("errors", errors);
            for (ObjectError error : errors) {
                //输出错误信息
                System.out.println(error.getDefaultMessage());
            }
            //如果校验错误，回到商品修改页面
            return "editItem";
        }

        //进行数据回显
        model.addAttribute("id", id);
        //model.addAttribute("item", itemsCustom);
        //进行图片上传
        if (pictureFile != null && pictureFile.getOriginalFilename() != null && pictureFile.getOriginalFilename().length() > 0) {
            //图片上传成功后，将图片的地址写到数据库
            String filePath = "F:\\develop\\upload\\temp\\";
            //上传文件原始名称
            String originalFilename = pictureFile.getOriginalFilename();
            //新的图片名称
            String newFileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
            //新文件
            File file = new java.io.File(filePath + newFileName);

            //将内存中的文件写入磁盘
            pictureFile.transferTo(file);

            //图片上传成功，将新图片地址写入数据库
            itemsCustom.setPic(newFileName);
        }

        //调用service接口更新商品信息
        itemsService.updateItems(id, itemsCustom);

        //提交后回到修改页面
        //return "editItem";
        //请求重定向
        return "redirect:queryItems.action";
        //转发
    }

    //删除 商品
    @RequestMapping("/deleteItems")
    public String deleteItems(Integer[] delete_id) throws Exception {

        //调用service方法删除 商品
        //....

        return "success";
    }
}
