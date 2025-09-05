package ru.serggge.interceptors;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import ru.serggge.exception.UserInputException;
import java.util.logging.Logger;

@ExceptionHandling
@Interceptor
public class ExceptionInterceptor {

    private static final Logger log = Logger.getLogger(ExceptionInterceptor.class.getName());

    @AroundInvoke
    public Object interceptException(InvocationContext ctx) throws Exception {
        // реализация перехватчика исключений
        try {
            return ctx.proceed();
        } catch (UserInputException e) {
            // перехватывает ошибки ввода ParseInt\ParseLong
            System.out.println(e.getMessage());
            return null;
        } catch (IllegalArgumentException e) {
            // перехватывает ошибки, когда пользователь вводит операцию не добавленную в Енум
            return String.valueOf("INVALID_OPERATION");
        }
    }
}