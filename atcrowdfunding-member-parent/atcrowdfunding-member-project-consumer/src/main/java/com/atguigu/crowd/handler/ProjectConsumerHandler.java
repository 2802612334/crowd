package com.atguigu.crowd.handler;

import com.atguigu.crowd.api.MySQLRemoteFeignService;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.vo.*;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/project")
public class ProjectConsumerHandler {

    @Value("${crowd.HeaderPictureFileDir}")
    private String headerPictureFileDir;

    @Value("${crowd.DetailPictureFileDir}")
    private String detailPictureFileDir;

    @Value("${crowd.ReturnPictureFileDir}")
    private String returnPictureFileDir;

    @Autowired
    private MySQLRemoteFeignService mySQLRemoteFeignService;

    @RequestMapping("/detail/to/{id}")
    public ModelAndView projectDetail(@PathVariable("id") Integer projectId){
        ModelAndView modelAndView = new ModelAndView();
        ResultEntity<DetailProjectVO> detailProjectReturn = mySQLRemoteFeignService.getDetailProjectReturnRemote(projectId);
        modelAndView.setViewName("project");
        if(detailProjectReturn.getResult().equals(ResultEntity.getFAILED())){
            return modelAndView;
        }
        modelAndView.addObject(CrowdConstant.ATTR_NAME_PROJECT_DETAIL,detailProjectReturn.getData());
        return modelAndView;
    }

    @RequestMapping("/create/confirm.html")
    public String createConfirm(MemberConfirmInfoVO memberConfirmInfoVO,HttpSession session,HttpServletRequest request){
        // 1.从session域获取当前工程对象和登录用户
        ProjectVO projectVO = (ProjectVO)session.getAttribute(CrowdConstant.ATTR_NAME_PROJECTVO);
        MemberLoginVO memberLoginVO = (MemberLoginVO)session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
        // 2.将MemberConfirmInfoVO信息放入该对象
        projectVO.setMemberConfirmInfoVO(memberConfirmInfoVO);
        // 3.设置创建时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        projectVO.setCreatedate(simpleDateFormat.format(new Date()));
        // 3.进行数据持久化
        ResultEntity<String> resultEntity = mySQLRemoteFeignService.saveProjectVORemote(projectVO, memberLoginVO.getId());
        if(resultEntity.getResult().equals(ResultEntity.getFAILED())){
            log.info("保存" + projectVO.toString() + "失败！" + "     错误信息：" + resultEntity.getMessage());
            request.setAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_SAVE_PROJECT_ERROR);
            return "confirm";
        }
        // 4.清空session
        session.removeAttribute(CrowdConstant.ATTR_NAME_PROJECTVO);
        return "redirect:/project/create/success.html";
    }

    @ResponseBody
    @RequestMapping("/create/save/return.json")
    public ResultEntity saveReturn(ReturnVO returnObj, HttpSession session){

        ProjectVO projectVO = (ProjectVO)session.getAttribute(CrowdConstant.ATTR_NAME_PROJECTVO);

        projectVO.getReturnVOList().add(returnObj);

        // 由于使用了SpringSession，该数据需要重新放入session中，更新redis中的数据
        session.setAttribute(CrowdConstant.ATTR_NAME_PROJECTVO,projectVO);

        return ResultEntity.successWithOutData();
    }

    @ResponseBody
    @RequestMapping("/create/upload/return/picture.json")
    public ResultEntity uploadReturnPicture(MultipartFile returnPicture){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // 1.判断上传图片是否为空
        if(returnPicture == null && returnPicture.getSize() == 0){
            return ResultEntity.failed(CrowdConstant.MESSAGE_RETURNPICTURE_IS_EMPTY);
        }
        // 2.生成上传路径
        String returnDir = returnPictureFileDir + simpleDateFormat.format(new Date()) + "\\";
        File uploadDir = new File(returnDir);
        if(!uploadDir.exists()){
            uploadDir.mkdirs();
        }
        // 3.获取上传文件的扩展名
        String returnPictureExtensionName = CrowdUtil.getFileExtensionName(returnPicture.getOriginalFilename());
        // 4.生成上传图片的文件名
        String realName = UUID.randomUUID().toString().replaceAll("-","") + returnPictureExtensionName;
        // 5.执行存储
        try {
            returnPicture.transferTo(new File(returnDir + realName));
            log.info(realName + "回报图片存储成功" + "----------" + "文件大小为：" + "----------" + returnPicture.getSize() + "字节");
            // 6.将上传成功的路径返回给用户
            return ResultEntity.successWithData(returnDir + realName);
        } catch (IOException e) {
            log.error(realName + "回报图片上传失败");
            // 7.上传失败，返回失败消息
            return ResultEntity.failed(CrowdConstant.MESSAGE_RETURNPICTURE_ERROR);
        }
    }

    @RequestMapping("/create/project/information.html")
    public String createProjectInfo(
            ProjectVO projectVO,
            MultipartFile headerPicture,
            List<MultipartFile> detailPictureList,
            HttpServletRequest request,
            HttpSession session
    ){
        String realName = "";
        File tempDir = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // 一，存储头图
        // 1.判断头图是否为空
        if(!(headerPicture != null && headerPicture.getSize() != 0)){
            request.setAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_HEADERPICTURE_IS_EMPTY);
            return "launch";
        }

        // 2.创建头图文件存储路径
        String headDir = headerPictureFileDir + simpleDateFormat.format(new Date()) + "\\";
        tempDir = new File(headDir);
        if(!tempDir.exists()){
            tempDir.mkdirs();
        }

        // 2.获取头图文件的扩展名
        String headerPictureExtensionName = CrowdUtil.getFileExtensionName(headerPicture.getOriginalFilename());

        // 3.生成真实文件名
        realName = UUID.randomUUID().toString().replaceAll("-","") + headerPictureExtensionName;

        // 4.存储头图
        try {
            headerPicture.transferTo(new File(headDir + realName));
            // 5.将成功存储的路径保存到ProjectVO对象中
            projectVO.setHeaderPicturePath(headDir + realName);
            log.info(realName + "头图存储成功" + "----------" + "文件大小为：" + "----------" + headerPicture.getSize() + "字节");
        } catch (IOException e) {
            request.setAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_HEADERPICTURE_ERROR);
            log.error(realName + "头图上传失败");
        }

        // 二，存储详情图
        for (MultipartFile multipartFile : detailPictureList) {
            // 1.判断当前详情图是否为null
            if(multipartFile == null && multipartFile.getSize() == 0){
                request.setAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_DETAILPICTURE_IS_EMPTY);
                return "launch";
            }
            // 2.创建头图文件存储路径
            String detailDir = detailPictureFileDir + simpleDateFormat.format(new Date()) + "\\";
            tempDir = new File(detailDir);
            if(!tempDir.exists()){
                tempDir.mkdirs();
            }
            // 3.获取当前详情图片扩展名
            String detailPictureExtensionName = CrowdUtil.getFileExtensionName(multipartFile.getOriginalFilename());
            // 4.生成当前详情图片的文件名
            realName = UUID.randomUUID().toString().replaceAll("-","") + detailPictureExtensionName;
            // 5.存储当前详情图片
            try {
                multipartFile.transferTo(new File(detailDir + realName));
                // 6.将成功存储的路径保存到ProjectVO对象中
                projectVO.getDetailPicturePathList().add(detailDir + realName);
                log.info(realName + "详情图片存储成功" + "----------" + "文件大小为：" + "----------" + multipartFile.getSize() + "字节");
            } catch (IOException e) {
                request.setAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_DETAILPICTURE_ERROR);
                log.error(realName + "详情图片上传失败");
            }
        }
        // 三，将当前对象存储到Session域中
        session.setAttribute(CrowdConstant.ATTR_NAME_PROJECTVO,projectVO);
        return "redirect:/project/return/protocol/page.html";
    }

}
