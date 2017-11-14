package in.hocg.web.modules.service.impl;

import in.hocg.web.filter.VariableFilter;
import in.hocg.web.lang.CheckError;
import in.hocg.web.modules.domain.Variable;
import in.hocg.web.modules.domain.repository.VariableRepository;
import in.hocg.web.modules.service.VariableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.datatables.mapping.DataTablesInput;
import org.springframework.data.mongodb.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * Created by hocgin on 2017/11/6.
 * email: hocgin@gmail.com
 */
@Service
public class VariableServiceImpl implements VariableService {
    
    private VariableRepository variableRepository;
    
    @Autowired
    public VariableServiceImpl(VariableRepository variableRepository) {
        this.variableRepository = variableRepository;
    }
    
    @Override
    public DataTablesOutput data(DataTablesInput input) {
        return variableRepository.findAll(input);
    }
    
    @Override
    public void insert(VariableFilter filter, CheckError checkError) {
        Variable variable = filter.get();
        if (variableRepository.countAllByKey(variable.getKey()) > 0) {
            checkError.putError("字段名已经存在");
            return;
        }
        variableRepository.insert(variable);
    }
    
    @Override
    public void update(VariableFilter filter, CheckError checkError) {
        Variable variable = variableRepository.findVariableByIdAndKey(filter.getId(), filter.getKey());
        if (ObjectUtils.isEmpty(variable)) {
            checkError.putError("该系统变量不存在");
            return;
        }
        
        variableRepository.save(filter.update(variable));
    }
    
    @Override
    public void delete(CheckError checkError, String... id) {
        for (Variable variable : variableRepository.findAllByIdIn(id)) {
            if (variable.getBuiltIn()) {
                checkError.putError("删除失败, 含有内置对象");
                return;
            }
        }
        variableRepository.deleteAllByIdIn(id);
    }
    
    @Override
    public Variable findById(String id) {
        return variableRepository.findOne(id);
    }
    
    
}
