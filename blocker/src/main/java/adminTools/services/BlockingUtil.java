package adminTools.services;


public interface BlockingUtil {

     /**
      * Непосредственно блокируем в базе конкретный Pin
      * @param pinId - id пина, которого нужно заблокировать
      */
     void blockUserPin(Long pinId) throws Exception;

     /**
      * Непосредственно блокируем в базе конкретный Board и все Pins на ней
      * @param boardId - id доски, которую нужно заблокировать
      */
     void blockUserBoard(Long boardId) throws Exception;

     /**
      * Непосредственно блокируем в базе конкретного User, все его Boards и Pins на ней
      * @param userId - id пользователя, которого нужно заблокировать
      */
     void blockUser(Long userId) throws Exception;

}
