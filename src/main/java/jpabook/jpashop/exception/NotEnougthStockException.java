package jpabook.jpashop.exception;

public class NotEnougthStockException extends RuntimeException{
    public NotEnougthStockException() {
        super();
    }

    public NotEnougthStockException(String message) {
        super(message);
    }

    public NotEnougthStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnougthStockException(Throwable cause) {
        super(cause);
    }

}
