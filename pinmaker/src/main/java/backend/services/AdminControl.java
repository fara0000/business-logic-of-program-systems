package backend.services;

import backend.entities.Board;
import backend.entities.Pin;

public interface AdminControl {

     void blockUserPin(Long pinId);

     void blockUserBoard(Long boardId);

     void blockUser(Long userId);

     void sendPinToCheck(Pin pin);

     void sendBoardToCheck(Board board);
}
