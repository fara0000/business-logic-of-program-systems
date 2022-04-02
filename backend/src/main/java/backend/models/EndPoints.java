package backend.models;

public interface EndPoints {
    interface Pin {
        String CREATE_PIN = "/pin-builder";
        String UPLOAD_PHOTO  = "/pin-builder/upload-photo";
    }
    interface Board {
        String FIND_ALL  = "/pin-builder/find-boards";
        String CREATE_BOARD = "/pin-builder/create-board";
    }
    interface Auth {
        String LOGIN = "/login";
        String REGISTER = "/register";
    }
    interface Admin {
        String GET_ALL_USERS = "/users";
    }
}
