package com.yihao.base;

import com.yihao.core.Base;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by lenovo on 2014/12/6.
 */
@ControllerAdvice
public class BaseAction implements Base,ServletContextAware {
    /*    public  @Value("#{config['hhn.login']}") String hhn_login;
        public @Value("#{config['hhn.admin.login']}") String hhn_admin_login;*/
    private Logger logger = LoggerFactory.getLogger(BaseAction.class);
    @Autowired
    protected BaseService baseService;

    public String getMessage(HttpServletRequest request, String key, Object... objs) {
        Locale locale = RequestContextUtils.getLocale(request);
        return baseService.applicationContext.getMessage(key, objs, locale);
    }
    //日期格式化为常用格式
    public static String formatDate(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    public static String formatDateTime(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return df.format(date);
    }

    @ExceptionHandler
    @ResponseBody
    public BaseReturn exp(HttpServletRequest request, Exception ex) {
        BaseReturn baseReturn=new BaseReturn();
        ex.printStackTrace();
        if (ex instanceof HttpMessageNotReadableException){
            baseReturn.setReturnCode(BaseReturn.Err_data_inValid);
            baseReturn.setMessageInfo(getMessage(request,"data.inValid",null));
        }else{
            baseReturn.setReturnCode(BaseReturn.Err_system_error);
            baseReturn.setMessageInfo(ex.getMessage());
        }

        logger.error(ex.getMessage());
        return baseReturn;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {

    }
}
