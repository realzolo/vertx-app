package com.onezol.vertx.framework.component.approval.mapper;

import com.onezol.vertx.framework.common.skeleton.mapper.BaseMapper;
import com.onezol.vertx.framework.component.approval.model.entity.ApprovalFlowInstanceEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApprovalFlowInstanceMapper extends BaseMapper<ApprovalFlowInstanceEntity> {
}
