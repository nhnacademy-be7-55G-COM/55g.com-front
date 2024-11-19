package shop.s5g.front.decorator;

import org.springframework.core.task.TaskDecorator;
import shop.s5g.front.utils.AuthTokenHolder;

public class TaskThreadLocalDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        String token = AuthTokenHolder.getToken();
        return () -> {
            try {
                AuthTokenHolder.setToken(token);
                runnable.run();
            }finally {
                AuthTokenHolder.free();
            }
        };
    }
}
