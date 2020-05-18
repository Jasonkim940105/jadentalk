package client.com;

public final class Protocol {
    public final static int LOGIN = 100; // 로그인
    public final static int LOGIN_OK = 101; // 로그인 성공
    public final static int LOGIN_NO = 102; // 로그인 실패
    public final static int LOGIN_SUCCESS = 108; // 로그인 성공후, myPage 이동
    public final static int LOGOUT = 109; // 로그아웃

    public final static int JOIN = 200; // 회원가입
    public final static int ID_CHECK = 201; //중복회원 확인
    public final static int ID_USINGNOW = 202; // 중복아이디가 있을 경우
    public final static int ID_UNUSING = 203; // 중복아이디가 없을 경우


    public final static int ADD_ID = 300; // 친구추가
    public final static int FRIEND_ID_NOT_EXIST = 301; //친구추가한 아이디가 없는경우
    public final static int FRIEND_REQUEST_SUCCESS = 302; //친구요청이 성공정으로 날라간 경우
    public final static int FRIEND_REQUEST_ACCEPT = 303; //친구요청 수락
    public final static int FRIEND_REQUEST_REFUSE = 304; //친구요청 거절
    public final static int FRIED_REQUEST_ACCEPT_OK = 305; //친구요청 수락 성공
    public final static int FRIED_REQUEST_ACCEPT_NO = 306; //친구요청 수락 거절
    public final static int FRIEND_REQUEST_FAIL = 309; //친구요청 실패


    public final static int FRIEND_REQUEST_TO_ME = 400; // 나에게 들어온 친구요청
    public final static int FRIEND_REQUEST_LIST_SHOW = 401; // 나에게 들어온 친구요청을 전부 보여줌
    public final static int FRIEND_LIST_EMPTY = 402; // 나에게 들어온 친구요청이 없음
    public final static int FRIEND_LIST_SHOW = 403; // 내 친구목록 요청
    public final static int FRIEND_LIST_SHOW_EXIST = 404; // 내 친구목록이 있는경우
    public final static int FRIEND_LIST_SHOW_EMPTY = 405; // 내 친구목록이 없는경우

    public final static int MESSAGE_SEND = 500; //메세지 보내기
    public final static int CHAT_START = 501; //채팅시작, 기존 글들 긁어오기
    public final static int PREVIOUS_MESSAGE_EXIST = 502; // 이전 메세지 있음
    public final static int PRRVIOUS_MESSAGE_EMPTY = 503; // 이전 메세지 없음


    public final static int TEST = 999;








}
