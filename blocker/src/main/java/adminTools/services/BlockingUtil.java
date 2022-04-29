package adminTools.services;


public interface BlockingUtil {

     void blockUserPin(Long pinId) throws Exception;
     
     void blockUserBoard(Long boardId) throws Exception;

     void blockUser(Long userId) throws Exception;

}
