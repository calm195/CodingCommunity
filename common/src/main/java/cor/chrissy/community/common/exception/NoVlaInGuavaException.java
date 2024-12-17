package cor.chrissy.community.common.exception;

/**
 * Guava缓存未命中错误
 *
 * @author wx128
 * @createAt 2024/12/13
 */
public class NoVlaInGuavaException extends RuntimeException {
    public NoVlaInGuavaException(String msg) {
        super(msg);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
