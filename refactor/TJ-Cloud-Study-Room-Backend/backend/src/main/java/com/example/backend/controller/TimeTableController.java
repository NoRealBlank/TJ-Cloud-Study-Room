package com.example.backend.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.common.Result;
import com.example.backend.entity.TimeTable;
import com.example.backend.mapper.TimeTableMapper;
import com.example.backend.service.TimeTableService;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping()

public class TimeTableController {
    @Resource
    TimeTableMapper timeTableMapper;
    @Resource
    TimeTableService timeTableService;

    @PostMapping("/userCenter/timetable")               /* 实现日程新增 */
    public Result<?> add(@RequestBody TimeTable timeTable) {
        return timeTableService.add(timeTable);
    }

    @PutMapping("/userCenter/timetable")                /* 实现日程更新 */
    public Result<?> updateByUser(@RequestBody TimeTable timeTable) {
        return timeTableService.updateByUser(timeTable);
    }

    @DeleteMapping("/userCenter/timetable")             /* 实现日程删除 */
    public Result<?> delete(@RequestParam Integer cid) {
        return timeTableService.delete(cid);
    }

    @GetMapping("/userCenter/timetable")
    public Result<?> findClass(@RequestParam(defaultValue = "") Integer search) {
        return timeTableService.findClass(search);
    }
}
