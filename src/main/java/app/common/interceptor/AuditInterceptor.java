package app.common.interceptor;

import app.common.entity.BaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.time.LocalDateTime;
import java.util.Properties;

@Slf4j
@Intercepts({
    @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
public class AuditInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        try {
            MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
            Object parameter = invocation.getArgs()[1];
            
            if (parameter instanceof BaseEntity entity) {
                LocalDateTime now = LocalDateTime.now();
                
                if (ms.getSqlCommandType() == SqlCommandType.INSERT) {
                    entity.setCreatedAt(now);
                    entity.setUpdatedAt(now);
                    log.debug("Set audit fields for INSERT: {}", entity.getClass().getSimpleName());
                } else if (ms.getSqlCommandType() == SqlCommandType.UPDATE) {
                    entity.setUpdatedAt(now);
                    log.debug("Set audit fields for UPDATE: {}", entity.getClass().getSimpleName());
                }
            }
        } catch (Exception e) {
            log.error("Error in AuditInterceptor", e);
        }
        
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // 필요시 설정값 처리
    }
}