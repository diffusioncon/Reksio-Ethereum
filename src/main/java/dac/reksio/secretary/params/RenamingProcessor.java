package dac.reksio.secretary.params;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class RenamingProcessor extends ServletModelAttributeMethodProcessor {

    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    private final Map<Class<?>, Map<String, String>> replaceMap = new ConcurrentHashMap<>();

    RenamingProcessor() {
        super(false);
    }

    public RenamingProcessor withRequestMappingHandlerAdapter(RequestMappingHandlerAdapter requestMappingHandlerAdapter) {
        this.requestMappingHandlerAdapter = requestMappingHandlerAdapter;
        return this;
    }

    @Override
    protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest nativeWebRequest) {
        Object target = binder.getTarget();
        Class<?> targetClass = target.getClass();
        if (!replaceMap.containsKey(targetClass)) {
            Map<String, String> mapping = analyzeClass(targetClass);
            replaceMap.put(targetClass, mapping);
        }
        Map<String, String> mapping = replaceMap.get(targetClass);
        ParamNameDataBinder paramNameDataBinder = new ParamNameDataBinder(target, binder.getObjectName(), mapping);
        requestMappingHandlerAdapter.getWebBindingInitializer().initBinder(paramNameDataBinder, nativeWebRequest);
        super.bindRequestParameters(paramNameDataBinder, nativeWebRequest);
    }

    private static Map<String, String> analyzeClass(Class<?> targetClass) {
        Field[] fields = targetClass.getDeclaredFields();
        Map<String, String> renameMap = new HashMap<>();
        for (Field field : fields) {
            ParamName paramNameAnnotation = field.getAnnotation(ParamName.class);
            if (paramNameAnnotation != null && !paramNameAnnotation.value().isEmpty()) {
                renameMap.put(paramNameAnnotation.value(), field.getName());
            }
        }
        if (renameMap.isEmpty()) {
            return Map.of();
        }
        return renameMap;
    }
}
