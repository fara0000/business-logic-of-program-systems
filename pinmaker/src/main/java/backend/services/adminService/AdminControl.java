package backend.services.adminService;

import backend.entities.Board;
import backend.entities.Pin;

public interface AdminControl {

     /**
      * Блокировка конкретного pin пользователя
      * @param pinId - id пина, которого нужно заблокировать
      */
     void blockUserPin(Long pinId);

     /**
      * Блокировка конкретной board пользователя
      * @param boardId - id доски, которую нужно заблокировать
      */
     void blockUserBoard(Long boardId);

     /**
      * Блокировка конкретного пользователя
      * @param userId - id пользователя, которого нужно заблокировать
      */
     void blockUser(Long userId);

     /**
      * Отправка пина на проверку Admin
      * @param pin - непосредственно сам пин
      */
     void sendPinToCheck(Pin pin);

     /**
      * Отправка доски на проверку Admin
      * @param board - непосредственно сама board
      */
     void sendBoardToCheck(Board board);
}
