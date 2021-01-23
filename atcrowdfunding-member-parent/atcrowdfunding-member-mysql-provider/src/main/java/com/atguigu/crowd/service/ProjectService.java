package com.atguigu.crowd.service;

import com.atguigu.crowd.entity.vo.DetailProjectVO;
import com.atguigu.crowd.entity.vo.ProjectVO;

import java.text.ParseException;

public interface ProjectService {

    void saveProjectVORemote(ProjectVO projectVO, Integer memberId);

    DetailProjectVO getDetailProjectReturn(Integer projectId) throws Exception;
}
