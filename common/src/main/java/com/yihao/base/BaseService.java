package com.yihao.base;

import com.yihao.core.Base;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;

/**
 * Created by lenovo on 2014/12/6.
 */
@Service
public class BaseService<T> extends SqlSessionTemplate implements Base,ApplicationContextAware,InitializingBean {
    protected javax.validation.Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    public String className = "";
    @Autowired
    public BaseService(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
//        Class clazz = ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()
        Type type = getClass().getGenericSuperclass();
        Class clazz = null;
        if (type instanceof ParameterizedType) {
            clazz = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
            className = clazz.getSimpleName();
        } else {
            String namee = ((Class) type).getSimpleName();
            className = namee.substring(0, namee.indexOf("Service")==-1?namee.length():namee.indexOf("Service"));
        }
//           System.out.println("***********\t"+className);
    }
    protected ApplicationContext applicationContext;
    protected String web_path;


    //    @Resource
//    protected Validator validator;



/*    @ExceptionHandler
    public BaseReturn exp(Exception ex) throws SQLException{  //TODO:可删除
        BaseReturn baseReturn=new BaseReturn();
        ex.printStackTrace();
        if (ex instanceof HttpMessageNotReadableException){
            baseReturn.setReturnCode(BaseReturn.Err_data_inValid);
            baseReturn.setMessageInfo("data.inValid");
        }else if(ex instanceof SQLException) {
            ex.printStackTrace();
            throw new SQLException("sql执行错误");
//            return new BaseReturn()
        } else{
            baseReturn.setReturnCode(BaseReturn.Err_system_error);
            baseReturn.setMessageInfo(ex.getMessage());
        }
        logger.error(ex.getMessage());
        return baseReturn;
    }*/

    public <E> BaseReturn validAndReturn(E t) {
        Set<ConstraintViolation<E>> errs = validator.validate(t);
        if (errs.size() > 0) {
            StringBuffer sb = new StringBuffer();
            for (ConstraintViolation<E> cons : errs)
                sb.append(cons.getPropertyPath() + ":" + cons.getInvalidValue() + "===>" + cons.getMessage() + "\r\n");
            return new BaseReturn(1, sb.toString());
        } else return new BaseReturn(0, null);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
