package in.hocg.web.modules.system.service.impl;

import in.hocg.web.global.component.QuartzService;
import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.system.domain.SysTask;
import in.hocg.web.modules.system.domain.repository.SysTaskRepository;
import in.hocg.web.modules.system.filter.SysTaskFilter;
import in.hocg.web.modules.system.service.SysTaskService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.datatables.mapping.DataTablesInput;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * Created by hocgin on 2017/11/30.
 * email: hocgin@gmail.com
 */
@Service
public class SysTaskServiceImpl implements SysTaskService {
    
    private SysTaskRepository sysTaskRepository;
    private QuartzService quartzService;
    
    @Autowired
    public SysTaskServiceImpl(SysTaskRepository sysTaskRepository, QuartzService quartzService) {
        this.sysTaskRepository = sysTaskRepository;
        this.quartzService = quartzService;
    }
    
    @Override
    public DataTablesOutput data(DataTablesInput filter) {
        Criteria criteria = new Criteria();
        DataTablesOutput<SysTask> all = sysTaskRepository.findAll(filter, criteria);
        all.setDraw(0);
        return all;
    }
    
    @Override
    public SysTask findOne(String id) {
        return sysTaskRepository.findOne(id);
    }
    
    @Override
    public void insert(SysTaskFilter filter, CheckError checkError) {
        SysTask task = filter.get();
        task.setAvailable(false);
        task = sysTaskRepository.insert(task);
        try {
            if (filter.getAvailable()) {
                quartzService.add(task);
                task.setAvailable(true);
                sysTaskRepository.save(task);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
            checkError.putError("任务设定存在异常");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            checkError.putError("执行类未找到");
        }
    }
    
    @Override
    public void update(SysTaskFilter filter, CheckError checkError) {
        String id = filter.getId();
        SysTask task = sysTaskRepository.findOne(id);
        if (ObjectUtils.isEmpty(task)) {
            checkError.putError("更新的任务异常");
            return;
        }
        task = filter.update(task);
        try {
            // 关闭旧的任务
            if (quartzService.exists(filter.getId(), filter.getGroup())) {
                quartzService.delete(filter.getId(), filter.getGroup());
            }
            if (task.getAvailable()) {
                quartzService.restart(task);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            checkError.putError("执行类未找到");
            return;
        } catch (Exception e) {
            e.printStackTrace();
            checkError.putError("任务设定存在异常");
            return;
        }
        sysTaskRepository.save(task);
    }
    
    @Override
    public void delete(String... id) {
        List<SysTask> all = sysTaskRepository.findAllByIdIn(id);
        all.forEach(task -> {
            quartzService.delete(task.getId(), task.getGroup());
            sysTaskRepository.delete(task.getId());
        });
    }
    
    @Override
    public void available(String id, boolean available, CheckError checkError) {
        SysTask task = sysTaskRepository.findOne(id);
        if (!ObjectUtils.isEmpty(task)) {
            if (task.getAvailable()
                    && !available) { // 原来是启用, 现在要禁用
                quartzService.delete(task.getId(), task.getGroup());
            } else if (!task.getAvailable()
                    && available){// 原来是禁用, 现在要启用
                try {
                    quartzService.restart(task);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    checkError.putError("执行类未找到");
                    return;
                } catch (SchedulerException e) {
                    e.printStackTrace();
                    checkError.putError("任务设定存在异常");
                    return;
                }
            }
            task.setAvailable(available);
            sysTaskRepository.save(task);
        } else {
            checkError.putError("任务异常");
        }
    }
    
    @Override
    public void resume(String id, CheckError checkError) throws SchedulerException {
        SysTask task = sysTaskRepository.findOne(id);
        if (!ObjectUtils.isEmpty(task)
                && quartzService.exists(task.getId(), task.getGroup())) {
            quartzService.resume(task.getId(), task.getGroup());
        } else {
            checkError.putError("任务未启动");
        }
    }
    
    @Override
    public void restart(String id, CheckError checkError) {
        SysTask task = sysTaskRepository.findOne(id);
        if (!ObjectUtils.isEmpty(task)) {
            try {
                quartzService.restart(task);
                task.setAvailable(true);
                sysTaskRepository.save(task);
            }  catch (ClassNotFoundException e) {
                e.printStackTrace();
                checkError.putError("执行类未找到");
            } catch (SchedulerException e) {
                e.printStackTrace();
                checkError.putError("任务设定存在异常");
            }
        }else {
            checkError.putError("任务异常");
        }
    }
    
    @Override
    public void init() {
        sysTaskRepository.findAllByAvailable(true).forEach(task -> {
            try {
                quartzService.add(task);
            } catch (SchedulerException | ClassNotFoundException e) {
                task.setAvailable(false);
            }
            sysTaskRepository.save(task);
        });
    }
}
