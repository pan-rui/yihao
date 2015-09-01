package com.yihao.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

/**
 * Created by lenovo on 2014/12/6.
 */
@ControllerAdvice
public class BaseAction implements Base,ServletContextAware {
    /*    public  @Value("#{config['hhn.login']}") String hhn_login;
        public @Value("#{config['hhn.admin.login']}") String hhn_admin_login;*/
    @Autowired
    protected javax.validation.Validator validator;
    private Logger logger = LoggerFactory.getLogger(BaseAction.class);
    protected ServletContext servletContext;
    protected String webPath;
    @Autowired
    protected BaseService baseService;

    public String getMessage(HttpServletRequest request, String key, Object... objs) {
        Locale locale = RequestContextUtils.getLocale(request);
        return baseService.applicationContext.getMessage(key, objs, locale);
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
        this.servletContext=servletContext;
        this.webPath = servletContext.getRealPath("/");
    }
    public <E> BaseReturn validAndReturn(E t) {
        Set<ConstraintViolation<E>> errs = validator.validate(t);
        if (errs.size() > 0) {
            StringBuffer sb = new StringBuffer();
            for (ConstraintViolation<E> cons : errs)
                sb.append(cons.getPropertyPath() + ":" + cons.getInvalidValue() + "===>" + cons.getMessage() + "\r\n");
            return new BaseReturn(1, sb.toString());
        } else return new BaseReturn(0, null);
    }
    public String getCookie(String key, HttpServletRequest request) {
        Cookie mycookies[] = request.getCookies();
        if (mycookies != null) {
            for (int i = 0; i < mycookies.length; i++) {
                if (key.equalsIgnoreCase(mycookies[i].getName())) {
                    try {
                        return URLDecoder.decode(mycookies[i].getValue(), "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return "";
    }
}
