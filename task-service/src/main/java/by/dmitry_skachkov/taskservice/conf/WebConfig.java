package by.dmitry_skachkov.taskservice.conf;

import by.dmitry_skachkov.taskservice.core.resolver.TaskFilterResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final TaskFilterResolver taskFilterResolver;

    public WebConfig(TaskFilterResolver taskFilterResolver) {
        this.taskFilterResolver = taskFilterResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(taskFilterResolver);
    }
}
