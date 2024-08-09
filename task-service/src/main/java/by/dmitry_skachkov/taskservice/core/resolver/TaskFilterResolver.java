package by.dmitry_skachkov.taskservice.core.resolver;

import by.dmitry_skachkov.taskservice.core.dto.task.TaskFilterDto;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.UUID;

@Component
public class TaskFilterResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return TaskFilterDto.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  org.springframework.web.bind.support.WebDataBinderFactory binderFactory) throws Exception {
        TaskFilterDto filterDto = new TaskFilterDto();

        String pageParam = webRequest.getParameter("page");
        String sizeParam = webRequest.getParameter("size");
        String uuidParam = webRequest.getParameter("uuid");
        String myParam = webRequest.getParameter("my");

        if (pageParam != null) {
            filterDto.setPage(Integer.parseInt(pageParam));
        }
        if (sizeParam != null) {
            filterDto.setSize(Integer.parseInt(sizeParam));
        }
        if (uuidParam != null) {
            filterDto.setUuid(UUID.fromString(uuidParam));
        }
        if (myParam != null) {
            filterDto.setMy(Boolean.parseBoolean(myParam));
        }

        return filterDto;
    }
}