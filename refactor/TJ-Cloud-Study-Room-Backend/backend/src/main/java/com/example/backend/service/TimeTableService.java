package com.example.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.backend.common.Result;
import com.example.backend.entity.TimeTable;
import com.example.backend.mapper.TimeTableMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Service
public class TimeTableService {
    @Resource
    TimeTableMapper timeTableMapper;

       /* 实现日程新增 */
    public Result<?> add(@RequestBody TimeTable timeTable) {
        timeTableMapper.insert(timeTable);
        return Result.success();
    }

              /* 实现日程更新 */
    public Result<?> updateByUser(@RequestBody TimeTable timeTable) {
        timeTableMapper.updateById(timeTable);
        return Result.success();
    }

            /* 实现日程删除 */
    public Result<?> delete(@RequestParam Integer cid) {
        timeTableMapper.deleteById(cid);
        return Result.success();
    }

    public Result<?> findClass(@RequestParam(defaultValue = "") Integer search) {
        LambdaQueryWrapper<TimeTable> wrapper = Wrappers.<TimeTable>lambdaQuery();

        List<TimeTable> res = timeTableMapper.selectList(wrapper.eq(TimeTable::getUid, search));

        return Result.success(res);
    }
}
