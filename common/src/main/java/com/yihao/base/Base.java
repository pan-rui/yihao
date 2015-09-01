package com.yihao.base;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by lenovo on 2014/12/8.
 */
public interface Base {
//    @Resource
//    protected JedisPool jedisPool;
//    protected Logger logger = Logger.getLogger(this.getClass());
//    public static ApplicationContext applicationContext;
//    public static String webPath;
//    public static ApplicationContext otherApplicationContext;
//    public static String jsp_classpath;
//    public static ApplicationContext getApplicationContext(HttpServletRequest request) {
////        return applicationContext==null?applicationContext=WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext()):applicationContext;
//        return Base.applicationContext;
//    }

    default BaseReturn getFormatError(BindingResult result) {
        List<FieldError> fields = result.getFieldErrors();
        StringBuffer errors = new StringBuffer();
        for (FieldError fieldError : fields) {
            if (StringUtils.isNotEmpty(fieldError.getDefaultMessage()))
                errors.append(MessageFormat.format(fieldError.getDefaultMessage(), fieldError.getField(), fieldError.getRejectedValue()));
        }
        return new BaseReturn(BaseReturn.Err_data_inValid, errors.toString());
    }
    //日期格式化为常用格式
    static String formatDate(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    static String formatDateTime(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(date);
    }

}
